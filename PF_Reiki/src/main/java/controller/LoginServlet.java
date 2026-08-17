package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.UserDao;
import model.UserData;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // ▼ ユーザーネーム空チェック
        if (username == null || username.isEmpty()) {
            request.setAttribute("errorMessage", "ユーザーネームを入力してください");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // ▼ ユーザーネーム255文字チェック
        if (username.length() > 255) {
            request.setAttribute("errorMessage", "ユーザーネームは255文字以内で入力してください");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // ▼ パスワード空チェック
        if (password == null || password.isEmpty()) {
            request.setAttribute("errorMessage", "パスワードを入力してください");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // ▼ パスワード形式チェック（8〜32文字、半角英数字と _ -）
        if (!password.matches("^[a-zA-Z0-9_-]{8,32}$")) {
            request.setAttribute("errorMessage", "パスワードは8〜32文字の半角英数字と _ - のみ使用できます");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // ▼ DB照合
        UserDao dao = new UserDao();
        UserData user = dao.findByLogin(username, password);

        if (user != null) {

            // ▼ 削除済みユーザーはログイン不可
            if ("deleted".equals(user.getStatus())) {
                request.setAttribute("errorMessage", "このアカウントは削除されています。");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            // ▼ セッション保存（ダッシュボードで使用）
            HttpSession session = request.getSession();
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getName());
            session.setAttribute("user", user);
            session.setAttribute("loginUser", user);


            // ▼ ロールで画面振り分け
            if ("admin".equals(user.getRole())) {
                response.sendRedirect("admin-dashboard.jsp");
            } else {
                response.sendRedirect("user-dashboard.jsp");
            }

        } else {
            request.setAttribute("errorMessage", "ユーザーネームまたはパスワードが違います");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
