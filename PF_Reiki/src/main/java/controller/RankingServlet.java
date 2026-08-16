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

        // ▼ DAO 呼び出し
        LikeDao dao = new LikeDao();

        // ▼ いいねランキング（今年のランキング）
        List<LikeRanking> rankingList = dao.getLikeRankingThisYear();

        // ▼ JSP に渡す
        request.setAttribute("rankingList", rankingList);

        // ▼ ランキング画面へ
        request.getRequestDispatcher("ranking.jsp").forward(request, response);
    }
}
