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

@WebServlet("/ranking")
public class RankingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LikeDao dao = new LikeDao();

        List<LikeRanking> rankingList = dao.getLikeRankingThisYear();

        request.setAttribute("rankingList", rankingList);

        request.getRequestDispatcher("ranking.jsp").forward(request, response);
    }
}
