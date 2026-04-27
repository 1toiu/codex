<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>操作日志</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
    <main class="shell">
        <div class="topbar">
            <div>
                <span class="badge admin">管理员</span>
                <span class="welcome">当前登录：${sessionScope.loginUser}</span>
            </div>
            <div class="actions">
                <a class="btn secondary compact" href="${pageContext.request.contextPath}/list">返回列表</a>
                <a class="btn secondary compact" href="${pageContext.request.contextPath}/change-password">修改密码</a>
                <a class="btn secondary compact" href="${pageContext.request.contextPath}/logout">退出登录</a>
            </div>
        </div>

        <section class="hero">
            <span class="eyebrow">OPERATION LOG</span>
            <h1>操作日志</h1>
            <p>记录管理员和登录用户的关键操作，方便课程设计展示系统留痕能力。</p>
        </section>

        <section class="panel">
            <div class="table-wrap">
                <table class="user-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>操作人</th>
                            <th>操作类型</th>
                            <th>操作详情</th>
                            <th>时间</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="log" items="${logList}">
                            <tr>
                                <td><span class="id-pill">${log.id}</span></td>
                                <td>${log.username}</td>
                                <td>${log.action}</td>
                                <td class="detail-cell">${log.detail}</td>
                                <td>${log.createdAt}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <c:if test="${empty logList}">
                    <div class="empty">暂无操作日志记录。</div>
                </c:if>
            </div>

            <c:if test="${totalPages > 1}">
                <div class="pagination">
                    <a class="page-link ${currentPage == 1 ? 'disabled' : ''}" href="${pageContext.request.contextPath}/logs?page=1">首页</a>
                    <a class="page-link ${currentPage == 1 ? 'disabled' : ''}" href="${pageContext.request.contextPath}/logs?page=${currentPage - 1}">上一页</a>
                    <span class="page-status">第 ${currentPage} / ${totalPages} 页，共 ${totalCount} 条</span>
                    <a class="page-link ${currentPage == totalPages ? 'disabled' : ''}" href="${pageContext.request.contextPath}/logs?page=${currentPage + 1}">下一页</a>
                    <a class="page-link ${currentPage == totalPages ? 'disabled' : ''}" href="${pageContext.request.contextPath}/logs?page=${totalPages}">末页</a>
                </div>
            </c:if>
        </section>
    </main>
</body>
</html>
