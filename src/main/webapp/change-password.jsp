<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>修改密码</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
    <main class="shell auth-shell">
        <div class="topbar">
            <div>
                <span class="badge ${sessionScope.role == 'admin' ? 'admin' : 'user'}">${sessionScope.role == 'admin' ? '管理员' : '普通用户'}</span>
                <span class="welcome">当前登录：${sessionScope.loginUser}</span>
            </div>
            <div class="actions">
                <a class="btn secondary compact" href="${pageContext.request.contextPath}/list">返回列表</a>
                <a class="btn secondary compact" href="${pageContext.request.contextPath}/logout">退出登录</a>
            </div>
        </div>

        <section class="hero auth-hero">
            <span class="eyebrow">PASSWORD</span>
            <h1>修改密码</h1>
            <p>修改成功后将退出当前登录，请使用新密码重新进入系统。</p>
        </section>

        <section class="panel form-card login-card">
            <form class="form-grid" action="${pageContext.request.contextPath}/change-password" method="post">
                <div class="field">
                    <label for="oldPassword">旧密码</label>
                    <input class="input" id="oldPassword" type="password" name="oldPassword" placeholder="请输入当前密码" required autofocus>
                </div>
                <div class="field">
                    <label for="newPassword">新密码</label>
                    <input class="input" id="newPassword" type="password" name="newPassword" placeholder="请输入新密码" required>
                </div>
                <div class="field">
                    <label for="confirmPassword">确认新密码</label>
                    <input class="input" id="confirmPassword" type="password" name="confirmPassword" placeholder="请再次输入新密码" required>
                </div>
                <% if (request.getAttribute("error") != null) { %>
                    <div class="alert danger"><%= request.getAttribute("error") %></div>
                <% } %>
                <div class="form-actions">
                    <button class="btn" type="submit">确认修改</button>
                    <a class="btn secondary" href="${pageContext.request.contextPath}/list">取消</a>
                </div>
            </form>
        </section>
    </main>
</body>
</html>
