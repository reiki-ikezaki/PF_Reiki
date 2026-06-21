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

@WebServlet("/profileEdit")
public class ProfileEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String name = request.getParameter("name");

        // ★ ログイン中のユーザー取得
        HttpSession session = request.getSession();
        UserData user = (UserData) session.getAttribute("user");

        // ★ メールチェック
        if (!isValidEmail(email)) {
            request.setAttribute("error", "メールアドレスの形式が不正、または255文字を超えています。");
            forward(request, response);
            return;
        }

        // ★ パスワード空欄 → エラー（アリアの仕様）
        if (password == null || password.isEmpty()) {
            request.setAttribute("error", "パスワードを入力してください。");
            forward(request, response);
            return;
        }

        // ★ パスワード形式チェック
        if (!isValidPassword(password)) {
            request.setAttribute("error", "パスワードは8〜32文字の半角英数字と _ - のみ使用できます。");
            forward(request, response);
            return;
        }

        // ★ 名前チェック
        if (!isValidName(name)) {
            request.setAttribute("error", "名前は255文字以内で入力してください。");
            forward(request, response);
            return;
        }

        // ★ DB 更新
        UserDao dao = new UserDao();
        boolean updated = dao.updateUser(user.getId(), email, password, name);

        if (!updated) {
            request.setAttribute("error", "更新に失敗しました。");
            forward(request, response);
            return;
        }

        // ★ セッション更新
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        session.setAttribute("user", user);

        // ★ 成功メッセージ
        request.setAttribute("success", "プロフィールを更新しました！");
        forward(request, response);
    }

    private void forward(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("profile-edit.jsp").forward(request, response);
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        if (email.length() > 255) return false;

        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(regex);
    }

    private boolean isValidPassword(String pass) {
        if (pass == null) return false;
        if (pass.length() < 8 || pass.length() > 32) return false;

        String regex = "^[A-Za-z0-9_-]+$";
        return pass.matches(regex);
    }

    private boolean isValidName(String name) {
        if (name == null || name.isEmpty()) return false;
        return name.length() <= 255;
    }
}
