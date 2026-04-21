package com.example.servlet;

import com.example.auth.AuthUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(value = "/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward((ServletRequest) request, (ServletResponse) response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
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
        response.sendRedirect(request.getContextPath() + "/list");
    }

    private String resolveRole(String username, String password) {
        if ("admin".equals(username) && "admin123".equals(password)) {
            return AuthUtil.ADMIN;
        }
        if ("user".equals(username) && "user123".equals(password)) {
            return AuthUtil.USER;
        }
        return null;
    }
}
