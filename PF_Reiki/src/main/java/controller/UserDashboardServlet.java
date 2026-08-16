package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.LikeDao;

@WebServlet("/userDashboard")
public class UserDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ▼ セッションからユーザー情報取得
        int userId = (int) request.getSession().getAttribute("userId");
        String username = (String) request.getSession().getAttribute("username");

        // ▼ DAO 呼び出し
        LikeDao likeDao = new LikeDao();

        // 今月のいいね数
        int monthlyLikes = likeDao.countLikesThisMonth(userId);

        // 今年のいいね数
        int yearlyLikes = likeDao.countLikesThisYear(userId);

        // ▼ JSP に渡す
        request.setAttribute("username", username);
        request.setAttribute("monthlyLikes", monthlyLikes);
        request.setAttribute("yearlyLikes", yearlyLikes);

        // ▼ ダッシュボードへ
        request.getRequestDispatcher("/user-dashboard.jsp").forward(request, response);
    }
}
