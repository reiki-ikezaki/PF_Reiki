package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.CategoryDao;
import model.Category;

@WebServlet("/categoryDeletedList")
public class CategoryDeletedListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CategoryDao dao = new CategoryDao();
        List<Category> deletedList = dao.findDeleted();

        request.setAttribute("deletedList", deletedList);

        request.getRequestDispatcher("category_deleted_list.jsp").forward(request, response);
    }
}
