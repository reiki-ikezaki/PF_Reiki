package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/accountList")
public class AccountListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ★ 仮データ（後で DB に変更）
        List<User> users = new ArrayList<>();
        users.add(new User("user1", "user1@example.com", "2025/05/25", "2025/06/16"));
        users.add(new User("user2", "user2@example.com", "2025/05/25", "2025/06/16"));
        users.add(new User("user3", "user3@example.com", "2025/05/25", "2025/06/16"));

        request.setAttribute("users", users);

        request.getRequestDispatcher("account-list.jsp").forward(request, response);
    }

    // ★ JSP に渡すための簡易 User クラス
    public static class User {
        public String name;
        public String email;
        public String created;
        public String updated;

        public User(String name, String email, String created, String updated) {
            this.name = name;
            this.email = email;
            this.created = created;
            this.updated = updated;
        }
    }
}
