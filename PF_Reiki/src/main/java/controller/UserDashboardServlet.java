package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.LikeDao;   // ★ 必須

@WebServlet("/userDashboard")
public class UserDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int userId = (int) request.getSession().getAttribute("userId");
        String username = (String) request.getSession().getAttribute("username");

        LikeDao likeDao = new LikeDao();

        int monthlyLikes = likeDao.countLikesThisMonth(userId);
        int yearlyLikes = likeDao.countLikesThisYear(userId);

        request.setAttribute("username", username);
        request.setAttribute("monthlyLikes", monthlyLikes);
        request.setAttribute("yearlyLikes", yearlyLikes);

        request.getRequestDispatcher("/user-dashboard.jsp").forward(request, response);
    }
}
