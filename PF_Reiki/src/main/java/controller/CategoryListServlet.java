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

@WebServlet("/categoryList")
public class CategoryListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // DAO 呼び出し
        CategoryDao dao = new CategoryDao();
        List<Category> categoryList = dao.findAll();

        // JSP に渡す
        request.setAttribute("categoryList", categoryList);

        // 画面遷移
        request.getRequestDispatcher("category_list.jsp").forward(request, response);
    }
}
