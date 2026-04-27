package com.example.servlet;

import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.util.MyBatisUtil;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;

@WebServlet(value = "/export")
public class ExportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");
        List<User> userList;

        try (SqlSession sqlSession = MyBatisUtil.getSqlSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            userList = name == null || name.trim().isEmpty() ? mapper.findAll() : mapper.findByName(name.trim());
        }

        String fileName = "users-" + LocalDateTime.now().format(FORMATTER) + ".csv";
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()).replace("+", "%20");

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/csv;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);

        StringBuilder csv = new StringBuilder();
        csv.append('\uFEFF');
        csv.append("ID,\u59d3\u540d,\u5e74\u9f84,\u90ae\u7bb1\r\n");

        for (User user : userList) {
            csv.append(safe(user.getId()))
               .append(',')
               .append(escapeCsv(user.getName()))
               .append(',')
               .append(safe(user.getAge()))
               .append(',')
               .append(escapeCsv(user.getEmail()))
               .append("\r\n");
        }

        response.getWriter().write(csv.toString());
        response.getWriter().flush();
    }

    private String safe(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        String escaped = value.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\n") || escaped.contains("\r") || escaped.contains("\"")) {
            return '"' + escaped + '"';
        }
        return escaped;
    }
}
