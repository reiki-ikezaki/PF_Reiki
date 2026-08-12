package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.UserDao;
import model.UserData;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDao dao = new UserDao();
        UserData user = dao.findByLogin(username, password);

        if (user != null) {

            if ("deleted".equals(user.getStatus())) {
                request.setAttribute("errorMessage", "このアカウントは削除されています。");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            if ("admin".equals(user.getRole())) {
                response.sendRedirect("admin-dashboard.jsp");
            } else {
                response.sendRedirect("user-dashboard.jsp");
            }

        } else {
            request.setAttribute("errorMessage", "ユーザーネームまたはパスワードが違います");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
