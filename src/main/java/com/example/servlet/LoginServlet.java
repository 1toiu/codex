package com.example.servlet;

import com.example.auth.AuthUtil;
import com.example.entity.AuthAccount;
import com.example.mapper.AuthAccountMapper;
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
import javax.servlet.http.HttpSession;
import org.apache.ibatis.session.SqlSession;

@WebServlet(value = "/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (AuthUtil.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/list");
            return;
        }
        request.getRequestDispatcher("/login.jsp").forward((ServletRequest) request, (ServletResponse) response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String username = trim(request.getParameter("username"));
        String password = trim(request.getParameter("password"));
        String role = resolveRole(username, password);

        if (role == null) {
            request.setAttribute("error", "账号或密码错误，请重新输入。");
            request.getRequestDispatcher("/login.jsp").forward((ServletRequest) request, (ServletResponse) response);
            return;
        }

        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }
        HttpSession session = request.getSession(true);
        session.setAttribute(AuthUtil.LOGIN_USER, username);
        session.setAttribute(AuthUtil.ROLE, role);
        OperationLogUtil.log(username, "LOGIN", "登录系统，角色=" + role);
        response.sendRedirect(request.getContextPath() + "/list");
    }

    private String resolveRole(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return null;
        }

        try (SqlSession sqlSession = MyBatisUtil.getSqlSession()) {
            AuthAccountMapper mapper = sqlSession.getMapper(AuthAccountMapper.class);
            AuthAccount account = mapper.findByUsernameAndPassword(username, password);
            if (account == null) {
                return null;
            }
            return AuthUtil.ADMIN.equals(username) ? AuthUtil.ADMIN : AuthUtil.USER;
        }
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
