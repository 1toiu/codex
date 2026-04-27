package com.example.servlet;

import com.example.auth.AuthUtil;
import com.example.entity.AuthAccount;
import com.example.mapper.AuthAccountMapper;
import com.example.util.MyBatisUtil;
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

@WebServlet("/change-password")
public class ChangePasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/change-password.jsp").forward((ServletRequest) request, (ServletResponse) response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String username = getLoginUser(request);
        if (username.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String oldPassword = trim(request.getParameter("oldPassword"));
        String newPassword = trim(request.getParameter("newPassword"));
        String confirmPassword = trim(request.getParameter("confirmPassword"));

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            request.setAttribute("error", "请完整填写旧密码、新密码和确认密码。");
            request.getRequestDispatcher("/change-password.jsp").forward((ServletRequest) request, (ServletResponse) response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "两次输入的新密码不一致，请重新确认。");
            request.getRequestDispatcher("/change-password.jsp").forward((ServletRequest) request, (ServletResponse) response);
            return;
        }

        try (SqlSession sqlSession = MyBatisUtil.getSqlSession()) {
            AuthAccountMapper mapper = sqlSession.getMapper(AuthAccountMapper.class);
            AuthAccount account = mapper.findByUsername(username);
            if (account == null || !oldPassword.equals(account.getPassword())) {
                request.setAttribute("error", "旧密码输入错误，请重新输入。");
                request.getRequestDispatcher("/change-password.jsp").forward((ServletRequest) request, (ServletResponse) response);
                return;
            }

            if (mapper.updatePassword(username, newPassword) != 1) {
                request.setAttribute("error", "密码修改失败，请稍后重试。");
                request.getRequestDispatcher("/change-password.jsp").forward((ServletRequest) request, (ServletResponse) response);
                return;
            }
        }

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        HttpSession newSession = request.getSession(true);
        newSession.setAttribute("flash", "密码修改成功，请使用新密码重新登录。");
        response.sendRedirect(request.getContextPath() + "/login");
    }

    private String getLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "";
        }
        Object loginUser = session.getAttribute(AuthUtil.LOGIN_USER);
        return loginUser == null ? "" : loginUser.toString();
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
