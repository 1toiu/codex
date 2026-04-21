<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>登录 - 用户管理</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
    <main class="shell auth-shell">
        <section class="hero auth-hero">
            <span class="eyebrow">ACCESS CONTROL</span>
            <h1>用户管理登录</h1>
            <p>管理员可以维护用户资料，普通用户只能查看用户列表。</p>
        </section>

        <section class="panel form-card login-card">
            <form class="form-grid" action="login" method="post">
                <div class="field">
                    <label for="username">账号</label>
                    <input class="input" id="username" type="text" name="username" placeholder="admin 或 user" required autofocus>
                </div>
                <div class="field">
                    <label for="password">密码</label>
                    <input class="input" id="password" type="password" name="password" placeholder="请输入密码" required>
                </div>
                <% if (request.getAttribute("error") != null) { %>
                    <div class="alert danger"><%= request.getAttribute("error") %></div>
                <% } %>
                <button class="btn" type="submit">登录系统</button>
                <div class="role-note"><strong>演示账号：</strong>管理员 admin / admin123，普通用户 user / user123</div>
            </form>
        </section>
    </main>
</body>
</html>
