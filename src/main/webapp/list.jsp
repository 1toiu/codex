<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>用户管理</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
    <main class="shell">
        <div class="topbar">
            <div>
                <span class="badge ${sessionScope.role == 'admin' ? 'admin' : 'user'}">${sessionScope.role == 'admin' ? '管理员' : '普通用户'}</span>
                <span class="welcome">当前登录：${sessionScope.loginUser}</span>
            </div>
            <a class="btn secondary compact" href="${pageContext.request.contextPath}/logout">退出登录</a>
        </div>

        <section class="hero">
            <span class="eyebrow">USER DIRECTORY</span>
            <h1>用户管理系统</h1>
            <p>集中查看和搜索用户资料。管理员可以新增、编辑、删除用户；普通用户只拥有查看权限。</p>
        </section>

        <c:if test="${not empty sessionScope.flash}">
            <div class="alert">${sessionScope.flash}</div>
            <c:remove var="flash" scope="session" />
        </c:if>

        <section class="panel">
            <div class="toolbar">
                <form class="search-form" action="list" method="get">
                    <input class="input" type="text" name="name" value="${param.name}" placeholder="输入姓名进行搜索">
                    <button class="btn" type="submit">查询用户</button>
                    <a class="btn secondary" href="list">重置</a>
                </form>
                <div class="actions">
                    <a class="btn secondary" href="${pageContext.request.contextPath}/export?name=${param.name}">导出文件</a>
                    <c:if test="${sessionScope.role == 'admin'}">
                        <form class="import-form" id="importForm" action="${pageContext.request.contextPath}/import" method="post" enctype="multipart/form-data">
                            <button class="btn secondary" id="importTrigger" type="button">导入文件</button>
                        </form>
                        <a class="btn warning" href="${pageContext.request.contextPath}/add">添加用户</a>
                    </c:if>
                </div>
            </div>

            <div class="table-wrap">
                <table class="user-table">
                    <thead><tr><th>ID</th><th>姓名</th><th>年龄</th><th>邮箱</th><th>操作</th></tr></thead>
                    <tbody>
                        <c:forEach var="user" items="${userList}">
                            <tr>
                                <td><span class="id-pill">${user.id}</span></td>
                                <td>${user.name}</td>
                                <td>${user.age}</td>
                                <td>${user.email}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${sessionScope.role == 'admin'}">
                                            <div class="actions">
                                                <a class="link-action" href="${pageContext.request.contextPath}/edit?id=${user.id}">编辑</a>
                                                <a class="link-action danger" href="${pageContext.request.contextPath}/delete?id=${user.id}" onclick="return confirm('确定要删除这个用户吗？')">删除</a>
                                            </div>
                                        </c:when>
                                        <c:otherwise><span class="readonly-pill">仅可查看</span></c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <c:if test="${empty userList}"><div class="empty">暂无匹配的用户，换个关键词试试。</div></c:if>
            </div>
        </section>
    </main>
    <script>
        (function () {
            var importForm = document.getElementById('importForm');
            var importTrigger = document.getElementById('importTrigger');
            if (!importForm || !importTrigger) {
                return;
            }

            importTrigger.addEventListener('click', function () {
                var oldInput = importForm.querySelector('input[type="file"]');
                if (oldInput) {
                    importForm.removeChild(oldInput);
                }

                var fileInput = document.createElement('input');
                fileInput.type = 'file';
                fileInput.name = 'file';
                fileInput.accept = '.csv,text/csv';
                fileInput.required = true;
                fileInput.className = 'import-input';
                fileInput.addEventListener('change', function () {
                    if (fileInput.files && fileInput.files.length > 0) {
                        importForm.appendChild(fileInput);
                        importForm.submit();
                    }
                });
                fileInput.click();
            });
        })();
    </script>
</body>
</html>
