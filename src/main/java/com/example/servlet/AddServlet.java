package com.example.servlet;

import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.util.MyBatisUtil;
import com.example.util.OperationLogUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;

@WebServlet(value = "/add")
public class AddServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");
        Integer age = Integer.parseInt(request.getParameter("age"));
        String email = request.getParameter("email");

        User user = new User();
        user.setName(name);
        user.setAge(age);
        user.setEmail(email);

        try (SqlSession sqlSession = MyBatisUtil.getSqlSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            mapper.add(user);
        }
        OperationLogUtil.log(request, "ADD_USER", "新增用户：" + name);
        response.sendRedirect("list");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/add.jsp").forward((ServletRequest) request, (ServletResponse) response);
    }
}
