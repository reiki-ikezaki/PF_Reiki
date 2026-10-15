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
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import dao.UserDao;
import model.UserData;
import util.AccountValidator;

// 一般ユーザーの「自己紹介編集（プロフィール）」画面の処理。URL: /profileEdit（POST）
//   編集できる項目：プロフィール画像・名前・ふりがな・性別・年齢・自己紹介
//   （メールアドレス・パスワードは「設定」画面 /settings で変更する）
//   管理者にはプロフィールが無いので、管理者が来たら「設定」画面へ移す。
@WebServlet("/profileEdit")
@MultipartConfig(maxFileSize = 1024 * 1024 * 10)
public class ProfileEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        UserData user = (UserData) session.getAttribute("user");

        if ("admin".equals(user.getRole())) {
            response.sendRedirect("settings");
            return;
        }

        String name = request.getParameter("name");
        String furigana = request.getParameter("furigana");
        String gender = request.getParameter("gender");
        String ageStr = request.getParameter("age");
        String bio = request.getParameter("bio");

        // 検証は、画面の上から下の並び順で行い、最初に見つかった1件のエラーを表示する。
        //   画面の並び：プロフィール画像 → 名前 → ふりがな → 性別 → 年齢 → 自己紹介

        // ① プロフィール画像の検証（選択されたときだけ）
        String error = null;
        byte[] imageBytes = null;

        try {
            Part part = request.getPart("profileImage");

            if (part != null && part.getSize() > 0) {
                error = AccountValidator.validateImageExtension(part.getSubmittedFileName());
                if (error == null) error = AccountValidator.validateImageSize(part.getSize());

                if (error == null) {
                    try (InputStream in = part.getInputStream();
                         ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                        byte[] buf = new byte[8192];
                        int n;
                        while ((n = in.read(buf)) != -1) {
                            out.write(buf, 0, n);
                        }
                        imageBytes = out.toByteArray();
                    }
                }
            }

        } catch (Exception e) {
            error = "プロフィール画像は2MB以下にしてください。";
        }

        // ② 名前・ふりがな・性別・年齢・自己紹介の検証
        if (error == null) error = AccountValidator.validateName(name);
        if (error == null) error = AccountValidator.validateFurigana(furigana);
        if (error == null) error = AccountValidator.validateGender(gender);
        if (error == null) error = AccountValidator.validateAge(ageStr);
        if (error == null) error = AccountValidator.validateBio(bio);

        if (error != null) {
            showError(request, response, error);
            return;
        }

        int age = 0;
        if (ageStr != null && !ageStr.isEmpty()) {
            age = Integer.parseInt(ageStr);
        }

        // ③ 更新
        UserDao dao = new UserDao();
        boolean updated = dao.updateProfile(user.getId(), name, furigana, gender, age, bio, imageBytes);

        if (!updated) {
            showError(request, response, "更新に失敗しました。");
            return;
        }

        // セッション内のユーザー情報にも反映する
        user.setName(name);
        user.setFurigana(furigana);
        user.setGender(gender);
        user.setAge(age);
        user.setBio(bio);
        session.setAttribute("user", user);

        request.setAttribute("success", "プロフィールを更新しました！");
        forward(request, response);
    }

    private void showError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute("error", message);
        forward(request, response);
    }

    private void forward(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("profile-edit.jsp").forward(request, response);
    }
}
