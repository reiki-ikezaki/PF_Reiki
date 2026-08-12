package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.UserDao;

@WebServlet("/deleteUser")
public class DeleteUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        
        String idStr = request.getParameter("id");

        if (idStr != null) {
            int userId = Integer.parseInt(idStr);

            UserDao dao = new UserDao();
            dao.logicalDelete(userId);
        }

        response.sendRedirect("accountList");
    }
}
