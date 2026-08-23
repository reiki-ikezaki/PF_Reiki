package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.LikeDao;
import model.LikeRanking;

@WebServlet("/adminDashboard")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ▼ いいねランキング（今年・上位のみダッシュボードに表示）
        LikeDao likeDao = new LikeDao();
        List<LikeRanking> rankingList = likeDao.getLikeRankingThisYear();

        request.setAttribute("rankingList", rankingList);

        request.getRequestDispatcher("admin-dashboard.jsp").forward(request, response);
    }
}
