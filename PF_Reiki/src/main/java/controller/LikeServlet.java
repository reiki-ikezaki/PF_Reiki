package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.LikeDao;   // ★ 必須

@WebServlet("/like")
public class LikeServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int userId = Integer.parseInt(request.getParameter("userId"));
        int targetUserId = Integer.parseInt(request.getParameter("targetUserId"));

        LikeDao dao = new LikeDao();
        boolean success = dao.insertLike(userId, targetUserId);

        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write("{\"success\": " + success + "}");
    }
}
