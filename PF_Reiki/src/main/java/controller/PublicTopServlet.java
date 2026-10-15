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

@WebServlet("/public_top")
public class PublicTopServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserDao userDao = new UserDao();
        List<UserData> users = userDao.getGeneralUserList();

        request.setAttribute("users", users);

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            UserData loginUser = (UserData) session.getAttribute("loginUser");
            boolean isAdmin = loginUser != null && "admin".equals(loginUser.getRole());

            request.setAttribute("loggedIn", true);
            request.setAttribute("dashboardUrl", isAdmin ? "adminDashboard" : "userDashboard");
        }

        request.getRequestDispatcher("public_top.jsp").forward(request, response);
    }
}
