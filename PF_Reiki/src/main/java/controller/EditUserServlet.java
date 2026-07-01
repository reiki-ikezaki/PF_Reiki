package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.UserDao;
import model.UserData;

@WebServlet("/editUser")
public class EditUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // accountList.jsp の編集ボタンから id を受け取る
        String idStr = request.getParameter("id");

        if (idStr != null) {
            int userId = Integer.parseInt(idStr);

            // DB からユーザー情報を1件取得
            UserDao dao = new UserDao();
            UserData user = dao.findById(userId);

            // JSP に渡す
            request.setAttribute("user", user);
        }

        // 編集画面へ遷移
        request.getRequestDispatcher("editUser.jsp").forward(request, response);
    }
}
