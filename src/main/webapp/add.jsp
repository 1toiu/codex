<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>添加用户</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
    <main class="shell">
        <div class="topbar">
            <span class="badge admin">管理员</span>
            <a class="btn secondary compact" href="${pageContext.request.contextPath}/logout">退出登录</a>
        </div>

        <section class="hero">
            <span class="eyebrow">NEW PROFILE</span>
            <h1>添加用户</h1>
            <p>录入基础资料后提交，系统会自动保存并返回用户列表。</p>
        </section>

        <section class="panel form-card">
            <form class="form-grid" action="add" method="post">
                <div class="field"><label for="name">姓名</label><input class="input" id="name" type="text" name="name" placeholder="例如：张三" required></div>
                <div class="field"><label for="age">年龄</label><input class="input" id="age" type="number" name="age" min="0" max="150" placeholder="例如：28" required></div>
                <div class="field"><label for="email">邮箱</label><input class="input" id="email" type="email" name="email" placeholder="name@example.com" required></div>
                <div class="form-actions"><button class="btn" type="submit">提交保存</button><a class="btn secondary" href="${pageContext.request.contextPath}/list">返回列表</a></div>
            </form>
        </section>
    </main>
</body>
</html>
