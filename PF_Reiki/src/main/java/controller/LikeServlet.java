package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.LikeDao;

@WebServlet("/like")
public class LikeServlet extends HttpServlet {

    // ▼ 非同期いいね（fetchから呼ばれる）
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json; charset=UTF-8");

        // ▼ ログイン中ならセッションのユーザーIDを使い、未ログインなら匿名（0）としていいねを記録する
        HttpSession session = request.getSession(false);
        Object userIdObj = (session != null) ? session.getAttribute("userId") : null;
        int userId = (userIdObj != null) ? (Integer) userIdObj : 0;

        int targetUserId;
        try {
            targetUserId = Integer.parseInt(request.getParameter("targetUserId"));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"不正なリクエストです\"}");
            return;
        }

        LikeDao dao = new LikeDao();
        boolean success = dao.insertLike(userId, targetUserId);
        int likeCount = dao.countLikesTotal(targetUserId);

        response.getWriter().write(
                "{\"success\": " + success + ", \"likeCount\": " + likeCount + "}");
    }
}
