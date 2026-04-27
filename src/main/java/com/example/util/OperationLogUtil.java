package com.example.util;

import com.example.auth.AuthUtil;
import com.example.entity.OperationLog;
import com.example.mapper.OperationLogMapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.ibatis.session.SqlSession;

public final class OperationLogUtil {
    private OperationLogUtil() {
    }

    public static void log(HttpServletRequest request, String action, String detail) {
        String username = resolveLoginUser(request);
        if (username.isEmpty()) {
            return;
        }
        log(username, action, detail);
    }

    public static void log(String username, String action, String detail) {
        if (username == null || username.trim().isEmpty()) {
            return;
        }

        OperationLog log = new OperationLog();
        log.setUsername(username.trim());
        log.setAction(action);
        log.setDetail(detail);

        try (SqlSession sqlSession = MyBatisUtil.getSqlSession()) {
            OperationLogMapper mapper = sqlSession.getMapper(OperationLogMapper.class);
            mapper.add(log);
        }
    }

    private static String resolveLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "";
        }
        Object loginUser = session.getAttribute(AuthUtil.LOGIN_USER);
        return loginUser == null ? "" : loginUser.toString().trim();
    }
}
