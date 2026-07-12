package controller;

import java.io.IOException;
import java.util.regex.Pattern;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import dao.UserDao;
import model.UserData;

@WebServlet("/CreateUserServlet")
@MultipartConfig(maxFileSize = 2 * 1024 * 1024) // 2MB
public class CreateUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // ▼ 共通項目
        String role = request.getParameter("role");
        String status = request.getParameter("status");
        String username = request.getParameter("username");
        String email = request.getParameter("email");

        // ▼ 一般ユーザー専用項目
        String furigana = request.getParameter("furigana");
        String gender = request.getParameter("gender");
        String age = request.getParameter("age");
        String bio = request.getParameter("bio");
        Part profileImage = request.getPart("profileImage");

        // ▼ エラーメッセージ
        String error = null;

        // ▼ 名前：255文字以内
        if (username == null || username.length() > 255) {
            error = "ユーザー名は255文字以内で入力してください。";
        }

        // ▼ メールアドレス：形式チェック + 255文字以内
        if (email == null || email.length() > 255 ||
            !Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", email)) {
            error = "メールアドレスの形式が正しくありません。";
        }

        // ▼ 一般ユーザー専用バリデーション
        if ("user".equals(role)) {

            // ▼ ふりがな：255文字以内 + ひらがなのみ
            if (furigana == null || furigana.length() > 255 ||
                !Pattern.matches("^[ぁ-んー]+$", furigana)) {
                error = "ふりがなは255文字以内のひらがなのみで入力してください。";
            }

            // ▼ 性別：選択式以外はエラー
            if (!("male".equals(gender) || "female".equals(gender) || "other".equals(gender))) {
                error = "性別の選択が不正です。";
            }

            // ▼ 年齢：数字のみ + 3桁まで
            if (age == null || !Pattern.matches("^[0-9]{1,3}$", age)) {
                error = "年齢は数字のみ3桁以内で入力してください。";
            }

            // ▼ 自己紹介：1500文字以内（絵文字OK）
            if (bio != null && bio.length() > 1500) {
                error = "自己紹介は1500文字以内で入力してください。";
            }

            // ▼ プロフィール画像：2MB以内（MultipartConfig で制限済み）
            if (profileImage != null && profileImage.getSize() > (2 * 1024 * 1024)) {
                error = "プロフィール画像は2MB以内でアップロードしてください。";
            }
        }

        // ▼ エラーがあれば JSP に戻す
        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("account-add.jsp").forward(request, response);
            return;
        }

        // ▼ 画像保存処理（一般ユーザーのみ）
        String savedFileName = null;

        if ("user".equals(role) && profileImage != null && profileImage.getSize() > 0) {

            // アップロードされたファイル名
            String originalName = profileImage.getSubmittedFileName();

            // 保存先フォルダ（webapp/img/profile）
            String uploadDir = request.getServletContext().getRealPath("/img/profile");

            // 保存ファイル名（重複防止）
            savedFileName = System.currentTimeMillis() + "_" + originalName;

            // 保存先パス
            java.nio.file.Path path = java.nio.file.Paths.get(uploadDir, savedFileName);

            // フォルダが無ければ作成
            java.nio.file.Files.createDirectories(path.getParent());

            // ファイル保存
            profileImage.write(path.toString());
        }

        // ▼ UserData に詰める
        UserData user = new UserData();
        user.setUsername(username);
        user.setEmail(email);
        user.setRole(role);
        user.setStatus(status);

        if ("user".equals(role)) {
            user.setFurigana(furigana);
            user.setGender(gender);
            user.setAge(age);
            user.setBio(bio);
            user.setProfileImage(savedFileName); // 保存したファイル名
        }

        // ▼ DAO 呼び出し
        UserDao dao = new UserDao();
        dao.insertUser(user);

        // ▼ 完了後一覧へ
        response.sendRedirect("account-list.jsp");
    }
}
