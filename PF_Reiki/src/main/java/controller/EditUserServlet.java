package controller;

import java.io.ByteArrayOutputStream;
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

// アカウント編集（管理者用）。URL: /editUser
//   GET  … 編集画面を表示
//   POST … 入力を検証して更新（ユーザー種別に応じて、検証・保存する項目を切り替える）
@WebServlet("/editUser")
@MultipartConfig(maxFileSize = 1024 * 1024 * 10)
public class EditUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");

        if (idStr != null) {
            int userId = Integer.parseInt(idStr);

            UserDao dao = new UserDao();
            UserData user = dao.findById(userId);


            request.setAttribute("user", user);
        }


        request.getRequestDispatcher("edit-account.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 日本語が文字化けしないよう、先にUTF-8を指定する
        request.setCharacterEncoding("UTF-8");

        int id = Integer.parseInt(request.getParameter("id"));
        UserDao dao = new UserDao();

        Part filePart = null;
        try {
            filePart = request.getPart("profileImage");
        } catch (Exception e) {
            getServletContext().log("[アカウント編集] フォームの受け取りに失敗", e);
            showError(request, response, dao, id, "プロフィール画像は2MB以下にしてください。");
            return;
        }

        String role = request.getParameter("role");
        String status = request.getParameter("status");
        String username = request.getParameter("username");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        boolean isAdmin = "admin".equals(role);

        // 検証は、画面の上から下の並び順で行い、最初に見つかった1件のエラーを表示する。
        //   画面の並び：ユーザー種別 → ステータス → ユーザー名 → メール → 名前 → パスワード
        //               →（一般のみ）画像 → ふりがな → 性別 → 年齢 → 自己紹介

        // ① 共通項目の検証（パスワードは「変更する場合のみ」入力。空なら変更しない）
        String error = AccountValidator.validateRole(role);
        if (error == null) error = AccountValidator.validateStatus(status);
        if (error == null) error = AccountValidator.validateUsername(username);
        if (error == null && dao.existsUsername(username, id)) {
            error = "このユーザー名は既に使われています。";
        }
        if (error == null) error = AccountValidator.validateEmail(email);
        if (error == null) error = AccountValidator.validateName(name);
        if (error == null) error = AccountValidator.validatePassword(password);

        // ② 一般項目の検証（一般ユーザーのときだけ）
        String furigana = null;
        String gender = null;
        String ageStr = null;
        String bio = null;

        if (!isAdmin) {
            furigana = request.getParameter("furigana");
            gender = request.getParameter("gender");
            ageStr = request.getParameter("age");
            bio = request.getParameter("bio");

            if (error == null && filePart != null && filePart.getSize() > 0) {
                error = AccountValidator.validateImageExtension(filePart.getSubmittedFileName());
                if (error == null) {
                    error = AccountValidator.validateImageSize(filePart.getSize());
                }
            }
            if (error == null) error = AccountValidator.validateFurigana(furigana);
            if (error == null) error = AccountValidator.validateGender(gender);
            if (error == null) error = AccountValidator.validateAge(ageStr);
            if (error == null) error = AccountValidator.validateBio(bio);
        }

        if (error != null) {
            showError(request, response, dao, id, error);
            return;
        }

        // ③ 保存する（管理者には、一般項目を保存しない）
        byte[] imageBytes = null;
        int age = 0;

        if (!isAdmin) {
            if (filePart != null && filePart.getSize() > 0) {
                try (InputStream in = filePart.getInputStream();
                     ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                    byte[] buf = new byte[8192];
                    int n;
                    while ((n = in.read(buf)) != -1) {
                        out.write(buf, 0, n);
                    }
                    imageBytes = out.toByteArray();
                }
            }
            if (ageStr != null && !ageStr.isEmpty()) {
                age = Integer.parseInt(ageStr);
            }
        }

        UserData user = new UserData();
        user.setId(id);
        user.setRole(role);
        user.setStatus(status);
        user.setUsername(username);
        user.setEmail(email);
        user.setName(name);
        user.setFurigana(isAdmin ? null : furigana);
        user.setGender(isAdmin || gender == null || gender.isEmpty() ? null : gender);
        user.setAge(age);
        user.setBio(isAdmin ? null : bio);

        boolean result = dao.updateAccount(user, password, imageBytes);

        if (result) {
            response.sendRedirect("accountList");
        } else {
            showError(request, response, dao, id, "更新に失敗しました。");
        }
    }

    // エラーメッセージを付けて、編集画面へ戻す。
    // 送信された値はリクエストのパラメータに残っているので、画面側（edit-account.jsp）が
    // それを入力欄に戻す（入力内容が消えない）。パラメータが無い項目は、DBの値を表示する。
    private void showError(HttpServletRequest request, HttpServletResponse response,
            UserDao dao, int id, String message) throws ServletException, IOException {
        // 原因調査用のログ（入力値そのものは出さず、届いた項目名だけを記録する）
        getServletContext().log("[アカウント編集] エラー: " + message
                + " / Content-Type=" + request.getContentType()
                + " / 届いた項目=" + request.getParameterMap().keySet());

        request.setAttribute("error", message);
        request.setAttribute("user", dao.findById(id));
        request.getRequestDispatcher("edit-account.jsp").forward(request, response);
    }
}
