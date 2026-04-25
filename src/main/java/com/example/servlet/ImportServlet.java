package com.example.servlet;

import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.util.MyBatisUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.apache.ibatis.session.SqlSession;

@WebServlet(value = "/import")
@MultipartConfig
public class ImportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Part filePart = request.getPart("file");

        if (filePart == null || filePart.getSize() == 0) {
            session.setAttribute("flash", "导入失败：请选择 CSV 文件。");
            response.sendRedirect(request.getContextPath() + "/list");
            return;
        }

        ImportResult result;
        try (InputStream inputStream = filePart.getInputStream()) {
            result = importCsv(inputStream);
        } catch (IllegalArgumentException e) {
            session.setAttribute("flash", "导入失败：" + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/list");
            return;
        }

        session.setAttribute("flash", buildFlashMessage(result));
        response.sendRedirect(request.getContextPath() + "/list");
    }

    private ImportResult importCsv(InputStream inputStream) throws IOException {
        ImportResult result = new ImportResult();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             SqlSession sqlSession = MyBatisUtil.getSqlSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            String line;
            int lineNumber = 0;
            boolean headerHandled = false;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String normalizedLine = stripUtf8Bom(line).trim();
                if (normalizedLine.isEmpty()) {
                    continue;
                }

                if (!headerHandled) {
                    headerHandled = true;
                    if (isHeaderRow(normalizedLine)) {
                        result.skipped++;
                        continue;
                    }
                }

                try {
                    User user = toUser(parseCsvLine(normalizedLine), lineNumber);
                    mapper.add(user);
                    result.imported++;
                } catch (RuntimeException e) {
                    result.failed++;
                }
            }
        }

        return result;
    }

    private String buildFlashMessage(ImportResult result) {
        return "导入完成：成功 " + result.imported + " 条，跳过 " + result.skipped + " 条，失败 " + result.failed + " 条。";
    }

    private boolean isHeaderRow(String line) {
        List<String> columns = parseCsvLine(line);
        if (columns.size() < 4) {
            return false;
        }

        String first = cleanCell(columns.get(0));
        String second = cleanCell(columns.get(1));
        String third = cleanCell(columns.get(2));
        String fourth = cleanCell(columns.get(3));
        return "ID".equalsIgnoreCase(first)
                && ("姓名".equals(second) || "name".equalsIgnoreCase(second))
                && ("年龄".equals(third) || "age".equalsIgnoreCase(third))
                && ("邮箱".equals(fourth) || "email".equalsIgnoreCase(fourth));
    }

    private User toUser(List<String> columns, int lineNumber) {
        if (columns.size() < 4) {
            throw new IllegalArgumentException("第 " + lineNumber + " 行列数不足，至少需要 4 列。");
        }

        String name = cleanCell(columns.get(1));
        String ageText = cleanCell(columns.get(2));
        String email = cleanCell(columns.get(3));

        if (name.isEmpty() || ageText.isEmpty() || email.isEmpty()) {
            throw new IllegalArgumentException("第 " + lineNumber + " 行姓名、年龄、邮箱不能为空。");
        }

        User user = new User();
        user.setName(name);
        user.setAge(Integer.parseInt(ageText));
        user.setEmail(email);
        return user;
    }

    private String stripUtf8Bom(String value) {
        if (!value.isEmpty() && value.charAt(0) == '\uFEFF') {
            return value.substring(1);
        }
        return value;
    }

    private String cleanCell(String value) {
        String text = stripUtf8Bom(value).trim();
        if (text.length() >= 2 && text.startsWith("\"") && text.endsWith("\"")) {
            text = text.substring(1, text.length() - 1).replace("\"\"", "\"").trim();
        }
        return text;
    }

    private List<String> parseCsvLine(String line) {
        List<String> columns = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (ch == ',' && !inQuotes) {
                columns.add(current.toString());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }

        columns.add(current.toString());
        return columns;
    }

    private static class ImportResult {
        private int imported;
        private int skipped;
        private int failed;
    }
}
