package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.UserDao;
import model.UserData;

@WebServlet("/accountList")
public class AccountListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        
        int page = 1;      
        int limit = 5;     
        
        String pageStr = request.getParameter("page");
        if (pageStr != null) {
            try {
                page = Integer.parseInt(pageStr);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int offset = (page - 1) * limit;
        UserDao dao = new UserDao();

        
        List<UserData> users = dao.findPage(offset, limit);

        
        int totalUsers = dao.countUsers();

       
        int totalPages = (int) Math.ceil((double) totalUsers / limit);

        
        request.setAttribute("users", users);
        request.setAttribute("page", page);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("account-list.jsp").forward(request, response);
    }
}
