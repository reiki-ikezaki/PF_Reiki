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

// お問い合わせ機能のコントローラ（URL: /contact）
//   GET  … 入力フォームを表示
//   POST … 入力内容をDB保存 ＋ 管理者へ通知メール送信 ＋ 完了画面へ
@WebServlet("/contact")
public class ContactStatusServlet extends HttpServlet {

    // ① お問い合わせフォームを開いたとき
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // カテゴリの選択肢をDBから取得してJSPへ渡す（画面側でプルダウンにする）
        CategoryDao categoryDao = new CategoryDao();
        List<Category> categories = categoryDao.findAll();
        request.setAttribute("categories", categories);

        request.getRequestDispatcher("contact.jsp").forward(request, response);
    }

    // ② フォームの「送信」ボタンが押されたとき
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8"); // 日本語の文字化け防止

        // 入力値を取り出す
        int categoryId = Integer.parseInt(request.getParameter("category_id"));
        String content = request.getParameter("content");
        String email = request.getParameter("email");

        // ③ inquiries テーブルへ1件登録（ステータスは常に「未対応」）
        InquiryDao dao = new InquiryDao();
        dao.insertInquiry(categoryId, content, email);

        // ④ 通知メール送信（メール失敗してもお問い合わせ自体は成功させる）
        //    まずメール本文に載せるカテゴリ名を取得
        CategoryDao categoryDao = new CategoryDao();
        Category category = categoryDao.findById(categoryId);
        String categoryName = (category != null) ? category.getName() : "不明";
        MailUtil.sendInquiryNotification(getServletContext(), categoryName, content, email);

        // ⑤ 送信完了画面へ（二重送信防止のためリダイレクト）
        response.sendRedirect("contact_success.jsp");
    }
}
