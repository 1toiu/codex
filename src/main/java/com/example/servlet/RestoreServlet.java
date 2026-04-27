package com.example.servlet;

import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.util.MyBatisUtil;
import com.example.util.OperationLogUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.apache.ibatis.session.SqlSession;

@WebServlet(value = "/restore")
@MultipartConfig
public class RestoreServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Part filePart = request.getPart("file");

        if (filePart == null || filePart.getSize() == 0) {
            session.setAttribute("flash", "恢复失败：请选择要恢复的 CSV 文件。");
            OperationLogUtil.log(request, "RESTORE_USERS", "恢复失败：未选择 CSV 文件");
            response.sendRedirect(request.getContextPath() + "/list");
            return;
        }

        RestorePayload payload;
        try (InputStream inputStream = filePart.getInputStream()) {
            payload = parseRestoreFile(inputStream);
        } catch (IllegalArgumentException e) {
            session.setAttribute("flash", "恢复失败：" + e.getMessage());
            OperationLogUtil.log(request, "RESTORE_USERS", "恢复失败：" + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/list");
            return;
        }

        SqlSession sqlSession = MyBatisUtil.getSqlSession(false);
        try {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            mapper.deleteAll();

            for (RestoreRow row : payload.rows) {
                if (row.restoreWithId) {
                    mapper.addWithId(row.user);
                } else {
                    mapper.add(row.user);
                    payload.reassignedIdCount++;
                }
            }

            sqlSession.commit();
        } catch (RuntimeException e) {
            sqlSession.rollback();
            session.setAttribute("flash", "恢复失败：写入数据库时发生异常。");
            OperationLogUtil.log(request, "RESTORE_USERS", "恢复失败：写入数据库时发生异常");
            response.sendRedirect(request.getContextPath() + "/list");
            return;
        } finally {
            sqlSession.close();
        }

        session.setAttribute("flash", buildFlashMessage(payload));
        OperationLogUtil.log(request, "RESTORE_USERS",
                "恢复完成：成功恢复 " + payload.rows.size() + " 条，重新分配 ID " + payload.reassignedIdCount + " 条");
        response.sendRedirect(request.getContextPath() + "/list");
    }

    private RestorePayload parseRestoreFile(InputStream inputStream) throws IOException {
        RestorePayload payload = new RestorePayload();
        Set<Integer> seenIds = new HashSet<>();
        boolean headerHandled = false;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String normalizedLine = stripUtf8Bom(line).trim();
                if (normalizedLine.isEmpty()) {
                    continue;
                }

                List<String> columns = parseCsvLine(normalizedLine);
                if (!headerHandled) {
                    headerHandled = true;
                    if (!isHeaderRow(columns)) {
                        throw new IllegalArgumentException("第 " + lineNumber + " 行表头不正确，必须为 ID,姓名,年龄,邮箱。");
                    }
                    continue;
                }

                payload.rows.add(toRestoreRow(columns, lineNumber, seenIds));
            }
        }

        if (!headerHandled) {
            throw new IllegalArgumentException("文件内容为空，或缺少表头。");
        }

        return payload;
    }

    private String buildFlashMessage(RestorePayload payload) {
        return "恢复完成：成功恢复 " + payload.rows.size() + " 条，重新分配 ID " + payload.reassignedIdCount + " 条。";
    }

    private boolean isHeaderRow(List<String> columns) {
        if (columns.size() != 4) {
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

    private RestoreRow toRestoreRow(List<String> columns, int lineNumber, Set<Integer> seenIds) {
        if (columns.size() != 4) {
            throw new IllegalArgumentException("第 " + lineNumber + " 行列数不正确，必须严格为 4 列。");
        }

        String idText = cleanCell(columns.get(0));
        String name = cleanCell(columns.get(1));
        String ageText = cleanCell(columns.get(2));
        String email = cleanCell(columns.get(3));

        if (name.isEmpty() || ageText.isEmpty() || email.isEmpty()) {
            throw new IllegalArgumentException("第 " + lineNumber + " 行姓名、年龄、邮箱不能为空。");
        }

        Integer age;
        try {
            age = Integer.valueOf(ageText);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("第 " + lineNumber + " 行年龄格式不正确。");
        }

        User user = new User();
        user.setName(name);
        user.setAge(age);
        user.setEmail(email);

        RestoreRow row = new RestoreRow();
        row.user = user;

        if (idText.isEmpty()) {
            row.restoreWithId = false;
            return row;
        }

        Integer id;
        try {
            id = Integer.valueOf(idText);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("第 " + lineNumber + " 行 ID 格式不正确。");
        }

        if (id <= 0) {
            throw new IllegalArgumentException("第 " + lineNumber + " 行 ID 必须为正整数。");
        }

        if (seenIds.contains(id)) {
            row.restoreWithId = false;
            return row;
        }

        seenIds.add(id);
        user.setId(id);
        row.restoreWithId = true;
        return row;
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

    private static class RestorePayload {
        private final List<RestoreRow> rows = new ArrayList<>();
        private int reassignedIdCount;
    }

    private static class RestoreRow {
        private User user;
        private boolean restoreWithId;
    }
}
