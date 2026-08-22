package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.InquiryDao;

@WebServlet("/contact_destroy")
public class InquiryDestroyServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ▼ 削除対象IDを取得
        int id = Integer.parseInt(request.getParameter("id"));

        // ▼ DAO呼び出し（物理削除）
        InquiryDao dao = new InquiryDao();
        dao.destroy(id);

        // ▼ 削除後は一覧へ戻る
        response.sendRedirect("contact_list");
    }
}
