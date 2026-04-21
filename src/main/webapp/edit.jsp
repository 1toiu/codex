<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>编辑用户</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
    <main class="shell">
        <div class="topbar">
            <span class="badge admin">管理员</span>
            <a class="btn secondary compact" href="${pageContext.request.contextPath}/logout">退出登录</a>
        </div>

        <section class="hero">
            <span class="eyebrow">EDIT PROFILE</span>
            <h1>编辑用户</h1>
            <p>调整用户资料后保存，变更会立即写入数据库。</p>
        </section>

        <section class="panel form-card">
            <form class="form-grid" action="update" method="post">
                <input type="hidden" name="id" value="${user.id}">
                <div class="field"><label for="name">姓名</label><input class="input" id="name" type="text" name="name" value="${user.name}" required></div>
                <div class="field"><label for="age">年龄</label><input class="input" id="age" type="number" name="age" min="0" max="150" value="${user.age}" required></div>
                <div class="field"><label for="email">邮箱</label><input class="input" id="email" type="email" name="email" value="${user.email}" required></div>
                <div class="form-actions"><button class="btn" type="submit">保存修改</button><a class="btn secondary" href="${pageContext.request.contextPath}/list">返回列表</a></div>
            </form>
        </section>
    </main>
</body>
</html>
