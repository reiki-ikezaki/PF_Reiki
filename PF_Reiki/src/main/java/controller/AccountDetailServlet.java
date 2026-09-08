package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.LikeDao;
import dao.UserDao;
import model.UserData;

@WebServlet("/accountDetail")
public class AccountDetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        UserDao userDao = new UserDao();
        UserData user = userDao.findById(id);

        if (user == null || "deleted".equals(user.getStatus())) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        LikeDao likeDao = new LikeDao();
        int likeCount = likeDao.countLikesThisMonth(id);

        request.setAttribute("detailUser", user);
        request.setAttribute("likeCount", likeCount);
        request.setAttribute("backUrl", resolveBackUrl(request));

        request.getRequestDispatcher("account-detail.jsp").forward(request, response);
    }

    private String resolveBackUrl(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        boolean loggedIn = session != null && session.getAttribute("userId") != null;

        if (loggedIn) {
            String from = request.getParameter("from");
            if ("ranking".equals(from)) {
                return "ranking";
            }
            if ("general-list".equals(from)) {
                return "generalList";
            }
        }

        return "public_top";
    }
}
