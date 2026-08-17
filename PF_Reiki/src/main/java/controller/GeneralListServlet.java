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

@WebServlet("/generalList")
public class GeneralListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserDao dao = new UserDao();
        List<UserData> list = dao.getGeneralUserList();

        request.setAttribute("generalList", list);

        request.getRequestDispatcher("general-list.jsp").forward(request, response);
    }
}
