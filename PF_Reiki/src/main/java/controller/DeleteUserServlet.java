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

        // URL の id を取得
        String idStr = request.getParameter("id");

        if (idStr != null) {
            int userId = Integer.parseInt(idStr);

            // DAO を呼んで論理削除
            UserDao dao = new UserDao();
            dao.logicalDelete(userId);
        }

        // 一覧に戻る
        response.sendRedirect("accountList");
    }
}
