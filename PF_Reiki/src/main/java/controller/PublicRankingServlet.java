package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.UserDao;
import model.UserData;

@WebServlet("/public_ranking")
public class PublicRankingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserDao userDao = new UserDao();
        List<UserData> rankingUsers = userDao.getGeneralUserRankingThisMonth();

        if ("json".equals(request.getParameter("format"))) {
            writeRankingJson(response, rankingUsers);
            return;
        }

        request.setAttribute("rankingUsers", rankingUsers);

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            UserData loginUser = (UserData) session.getAttribute("loginUser");
            boolean isAdmin = loginUser != null && "admin".equals(loginUser.getRole());

            request.setAttribute("loggedIn", true);
            request.setAttribute("dashboardUrl", isAdmin ? "adminDashboard" : "userDashboard");
        }

        request.getRequestDispatcher("public_ranking.jsp").forward(request, response);
    }

    private void writeRankingJson(HttpServletResponse response, List<UserData> users)
            throws IOException {

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < users.size(); i++) {
            UserData u = users.get(i);
            if (i > 0) {
                json.append(",");
            }
            json.append("{\"id\":").append(u.getId())
                .append(",\"likeCount\":").append(u.getLikeCount()).append("}");
        }
        json.append("]");

        response.setContentType("application/json; charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");
        response.getWriter().write(json.toString());
    }
}
