package controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

        // 検証は、画面の上から下の並び順で、全項目を行い、エラーをまとめて表示する。
        //   画面の並び：ユーザー種別 → ステータス → ユーザー名 → メール → 名前 → パスワード
        //               →（一般のみ）画像 → ふりがな → 性別 → 年齢 → 自己紹介

        // ① 共通項目の検証（管理者・一般 どちらでも必要）
        List<String> errors = new ArrayList<>();
        AccountValidator.addIfError(errors, AccountValidator.validateRole(role));
        AccountValidator.addIfError(errors, AccountValidator.validateStatus(status));
        String usernameError = AccountValidator.validateUsername(username);
        AccountValidator.addIfError(errors, usernameError);
        if (usernameError == null && new UserDao().existsUsername(username, 0)) {
            errors.add("このユーザー名は既に使われています。");
        }
        AccountValidator.addIfError(errors, AccountValidator.validateEmail(email));
        AccountValidator.addIfError(errors, AccountValidator.validateName(name));
        if (password == null || password.isEmpty()) {
            errors.add("パスワードを入力してください。");
        } else {
            AccountValidator.addIfError(errors, AccountValidator.validatePassword(password));
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

            if (filePart != null && filePart.getSize() > 0) {
                String imageError = AccountValidator.validateImageExtension(filePart.getSubmittedFileName());
                if (imageError == null) {
                    imageError = AccountValidator.validateImageSize(filePart.getSize());
                }
                if (imageError == null) {
                    // 拡張子だけでなく、中身も画像か、Java側で確認する
                    try (InputStream in = filePart.getInputStream()) {
                        imageError = AccountValidator.validateImageContent(in.readNBytes(8));
                    }
                }
                AccountValidator.addIfError(errors, imageError);
            }
            AccountValidator.addIfError(errors, AccountValidator.validateFurigana(furigana));
            AccountValidator.addIfError(errors, AccountValidator.validateGender(gender));
            AccountValidator.addIfError(errors, AccountValidator.validateAge(ageStr));
            AccountValidator.addIfError(errors, AccountValidator.validateBio(bio));
        }

        if (!errors.isEmpty()) {
            showError(request, response, errors);
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
        showError(request, response, List.of(message));
    }

    private void showError(HttpServletRequest request, HttpServletResponse response, List<String> errors)
            throws ServletException, IOException {
        // 原因調査用のログ（入力値そのものは出さず、届いた項目名だけを記録する）
        getServletContext().log("[アカウント追加] エラー: " + errors
                + " / Content-Type=" + request.getContentType()
                + " / 届いた項目=" + request.getParameterMap().keySet());

        request.setAttribute("errors", errors);
        request.getRequestDispatcher("account-add.jsp").forward(request, response);
    }
}
