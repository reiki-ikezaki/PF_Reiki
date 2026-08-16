package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.CategoryDao;

@WebServlet("/categoryAdd")
public class CategoryAddServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/category_add.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");

        // バリデーション
        if (name == null || name.isEmpty() || name.length() > 255) {
            request.setAttribute("error", "カテゴリ名は1〜255文字で入力してください");
            request.getRequestDispatcher("/category_add.jsp").forward(request, response);
            return;
        }

        // DAO 呼び出し
        CategoryDao dao = new CategoryDao();
        dao.insert(name);

        // 一覧へ戻る
        response.sendRedirect("categoryList");
    }
}
