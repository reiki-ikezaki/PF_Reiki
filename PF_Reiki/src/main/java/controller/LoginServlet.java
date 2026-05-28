package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

   
//TES
        if ("admin".equals(username) && "admin123".equals(password)) {
            response.sendRedirect("admin-dashboard.jsp");
        } else if ("user".equals(username) && "user123".equals(password)) {
            response.sendRedirect("user-dashboard.jsp");
        } else {
            request.setAttribute("errorMessage", "ユーザーネームまたはパスワードが違います");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}

