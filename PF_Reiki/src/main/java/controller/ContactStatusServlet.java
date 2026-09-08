package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.CategoryDao;
import dao.InquiryDao;
import model.Category;
import util.MailUtil;

@WebServlet("/contact")
public class ContactStatusServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CategoryDao categoryDao = new CategoryDao();
        List<Category> categories = categoryDao.findAll();
        request.setAttribute("categories", categories);

        request.getRequestDispatcher("contact.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");


        int categoryId = Integer.parseInt(request.getParameter("category_id"));
        String content = request.getParameter("content");
        String email = request.getParameter("email");

        InquiryDao dao = new InquiryDao();
        dao.insertInquiry(categoryId, content, email);

        // ▼ 通知メール送信（失敗してもお問い合わせ自体は成功させる）
        CategoryDao categoryDao = new CategoryDao();
        Category category = categoryDao.findById(categoryId);
        String categoryName = (category != null) ? category.getName() : "不明";
        MailUtil.sendInquiryNotification(getServletContext(), categoryName, content, email);

        response.sendRedirect("contact_success.jsp");
    }
}
