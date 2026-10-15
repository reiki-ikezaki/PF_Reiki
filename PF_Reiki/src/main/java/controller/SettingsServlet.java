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
import util.AccountValidator;
import util.PasswordUtil;

// 「設定」画面（ログイン中のユーザー本人）。URL: /settings
//   管理者       … メールアドレス・パスワード・名前 を変更できる
//   一般ユーザー … メールアドレス・パスワード を変更できる（名前などは「プロフィール」画面）
//   GET  … 設定画面を表示
//   POST … 入力を検証して更新
@WebServlet("/settings")
public class SettingsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("settings.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        UserData user = (UserData) session.getAttribute("user");
        boolean isAdmin = "admin".equals(user.getRole());

        String email = request.getParameter("email");
        String password = request.getParameter("password");   // 空なら変更しない
        String name = request.getParameter("name");           // 管理者だけが使う

        // 検証：メールアドレス・パスワード（管理者は、名前も）
        String error = AccountValidator.validateEmail(email);
        if (error == null) error = AccountValidator.validatePassword(password);
        if (error == null && isAdmin) error = AccountValidator.validateName(name);

        if (error != null) {
            showError(request, response, error);
            return;
        }

        UserDao dao = new UserDao();
        boolean updated = isAdmin
                ? dao.updateAdminSettings(user.getId(), email, password, name)
                : dao.updateEmailAndPassword(user.getId(), email, password);

        if (!updated) {
            showError(request, response, "更新に失敗しました。");
            return;
        }

        // セッション内のユーザー情報にも反映する
        user.setEmail(email);
        if (password != null && !password.isEmpty()) {
            user.setPassword(PasswordUtil.hash(password));
        }
        if (isAdmin) {
            user.setName(name);
        }
        session.setAttribute("user", user);

        request.setAttribute("success", "設定を更新しました！");
        request.getRequestDispatcher("settings.jsp").forward(request, response);
    }

    private void showError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute("error", message);
        request.getRequestDispatcher("settings.jsp").forward(request, response);
    }
}
