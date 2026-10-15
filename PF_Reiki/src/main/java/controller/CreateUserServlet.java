package controller;

import java.io.IOException;
import java.io.InputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import dao.UserDao;
import model.UserData;
import util.AccountValidator;

// アカウント追加（管理者用）。URL: /CreateUserServlet（POST）
//   管理者項目 … ユーザー名・メール・名前・パスワード・ステータス
//   一般項目   … 上記 ＋ プロフィール画像・ふりがな・性別・年齢・自己紹介
// ユーザー種別（role）に応じて、検証する項目と保存する項目を切り替える。
@WebServlet("/CreateUserServlet")
@MultipartConfig(maxFileSize = 1024 * 1024 * 10)
public class CreateUserServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 日本語（名前・ふりがななど）が文字化けしないよう、先にUTF-8を指定する
        request.setCharacterEncoding("UTF-8");

        Part filePart = null;
        try {
            filePart = request.getPart("profileImage");
        } catch (Exception e) {
            // 10MBを超える画像など、受け取り自体に失敗した場合
            getServletContext().log("[アカウント追加] フォームの受け取りに失敗", e);
            showError(request, response, "プロフィール画像は2MB以下にしてください。");
            return;
        }

        String role = request.getParameter("role");
        String status = request.getParameter("status");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String password = request.getParameter("password");

        boolean isAdmin = "admin".equals(role);

        // ① 共通項目の検証（管理者・一般 どちらでも必要）
        String error = AccountValidator.validateRole(role);
        if (error == null) error = AccountValidator.validateStatus(status);
        if (error == null) error = AccountValidator.validateUsername(username);
        if (error == null) error = AccountValidator.validateName(name);
        if (error == null) error = AccountValidator.validateEmail(email);
        if (error == null && (password == null || password.isEmpty())) {
            error = "パスワードを入力してください。";
        }
        if (error == null) error = AccountValidator.validatePassword(password);
        if (error == null && new UserDao().existsUsername(username, 0)) {
            error = "このユーザー名は既に使われています。";
        }

        // ② 一般項目の検証（一般ユーザーを追加するときだけ）
        String furigana = null;
        String gender = null;
        String ageStr = null;
        String bio = null;

        if (!isAdmin) {
            furigana = request.getParameter("furigana");
            gender = request.getParameter("gender");
            ageStr = request.getParameter("age");
            bio = request.getParameter("bio");

            if (error == null) error = AccountValidator.validateFurigana(furigana);
            if (error == null) error = AccountValidator.validateGender(gender);
            if (error == null) error = AccountValidator.validateAge(ageStr);
            if (error == null) error = AccountValidator.validateBio(bio);
            if (error == null && filePart != null && filePart.getSize() > 0) {
                error = AccountValidator.validateImageExtension(filePart.getSubmittedFileName());
                if (error == null) {
                    error = AccountValidator.validateImageSize(filePart.getSize());
                }
            }
        }

        if (error != null) {
            showError(request, response, error);
            return;
        }

        // ③ 保存する（管理者には、一般項目を保存しない）
        InputStream img = null;
        int age = 0;

        if (!isAdmin) {
            if (filePart != null && filePart.getSize() > 0) {
                img = filePart.getInputStream();
            }
            if (ageStr != null && !ageStr.isEmpty()) {
                age = Integer.parseInt(ageStr);
            }
        }

        UserData user = new UserData();
        user.setRole(role);
        user.setStatus(status);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setFurigana(isAdmin ? null : furigana);
        user.setGender(isAdmin || gender == null || gender.isEmpty() ? null : gender);
        user.setAge(age);
        user.setBio(isAdmin ? null : bio);

        UserDao dao = new UserDao();
        boolean result = dao.insertUser(user, img);

        if (result) {
            response.sendRedirect("accountList");
        } else {
            showError(request, response, "登録に失敗しました。");
        }
    }

    // エラーメッセージを付けて、追加画面へ戻す。
    // 送信された値はリクエストのパラメータに残っているので、画面側（account-add.jsp）が
    // それを入力欄に戻す（入力内容が消えない）。
    private void showError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        // 原因調査用のログ（入力値そのものは出さず、届いた項目名だけを記録する）
        getServletContext().log("[アカウント追加] エラー: " + message
                + " / Content-Type=" + request.getContentType()
                + " / 届いた項目=" + request.getParameterMap().keySet());

        request.setAttribute("error", message);
        request.getRequestDispatcher("account-add.jsp").forward(request, response);
    }
}
