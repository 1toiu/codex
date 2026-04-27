package com.example.servlet;

import com.example.entity.OperationLog;
import com.example.mapper.OperationLogMapper;
import com.example.util.MyBatisUtil;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;

@WebServlet("/logs")
public class OperationLogServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 20;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        try (SqlSession sqlSession = MyBatisUtil.getSqlSession()) {
            OperationLogMapper mapper = sqlSession.getMapper(OperationLogMapper.class);
            int totalCount = mapper.countAll();
            int totalPages = Math.max(1, (int) Math.ceil(totalCount / (double) PAGE_SIZE));
            int currentPage = resolvePage(request.getParameter("page"), totalPages);
            int offset = (currentPage - 1) * PAGE_SIZE;
            List<OperationLog> logList = mapper.findPage(offset, PAGE_SIZE);

            request.setAttribute("logList", logList);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalCount", totalCount);
            request.getRequestDispatcher("/logs.jsp").forward((ServletRequest) request, (ServletResponse) response);
        }
    }

    private int resolvePage(String pageText, int totalPages) {
        if (pageText == null || pageText.trim().isEmpty()) {
            return 1;
        }
        try {
            int page = Integer.parseInt(pageText.trim());
            if (page < 1) {
                return 1;
            }
            return Math.min(page, totalPages);
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}
