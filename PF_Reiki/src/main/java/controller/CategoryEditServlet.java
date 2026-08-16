package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.CategoryDao;
import model.Category;

@WebServlet("/categoryEdit")
public class CategoryEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 編集画面表示（GET）
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        int id = Integer.parseInt(idStr);

        CategoryDao dao = new CategoryDao();
        Category category = dao.findById(id);

        request.setAttribute("category", category);
        request.getRequestDispatcher("/category_edit.jsp").forward(request, response);
    }

    // 更新処理（POST）
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");

        // ▼ バリデーション
        if (name == null || name.isEmpty() || name.length() > 255) {
            request.setAttribute("error", "カテゴリ名は1〜255文字で入力してください");

            // 入力値を保持して再表示
            Category c = new Category();
            c.setId(id);
            c.setName(name);
            request.setAttribute("category", c);

            request.getRequestDispatcher("/category_edit.jsp").forward(request, response);
            return;
        }

        // ▼ 更新処理
        CategoryDao dao = new CategoryDao();
        dao.update(id, name);

        // ▼ 一覧へ戻る
        response.sendRedirect("categoryList");
    }
}
