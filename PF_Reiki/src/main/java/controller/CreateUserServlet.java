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

        String role = request.getParameter("role");
        String status = request.getParameter("status");
        String username = request.getParameter("username");
        String email = request.getParameter("email");

        String furigana = request.getParameter("furigana");
        String gender = request.getParameter("gender");
        String age = request.getParameter("age");
        String bio = request.getParameter("bio");
        Part profileImage = request.getPart("profileImage");

        String error = null;

        if (username == null || username.length() > 255) {
            error = "ユーザー名は255文字以内で入力してください。";
        }

        if (email == null || email.length() > 255 ||
            !Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", email)) {
            error = "メールアドレスの形式が正しくありません。";
        }

        if ("user".equals(role)) {

            if (furigana == null || furigana.length() > 255 ||
                !Pattern.matches("^[ぁ-んー]+$", furigana)) {
                error = "ふりがなは255文字以内のひらがなのみで入力してください。";
            }

            if (!("male".equals(gender) || "female".equals(gender) || "other".equals(gender))) {
                error = "性別の選択が不正です。";
            }

            if (age == null || !Pattern.matches("^[0-9]{1,3}$", age)) {
                error = "年齢は数字のみ3桁以内で入力してください。";
            }

            if (bio != null && bio.length() > 1500) {
                error = "自己紹介は1500文字以内で入力してください。";
            }

            if (profileImage != null && profileImage.getSize() > (2 * 1024 * 1024)) {
                error = "プロフィール画像は2MB以内でアップロードしてください。";
            }
        }

        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("account-add.jsp").forward(request, response);
            return;
        }

        String savedFileName = null;

        if ("user".equals(role) && profileImage != null && profileImage.getSize() > 0) {

            String originalName = profileImage.getSubmittedFileName();

            String uploadDir = request.getServletContext().getRealPath("/img/profile");

            savedFileName = System.currentTimeMillis() + "_" + originalName;

            java.nio.file.Path path = java.nio.file.Paths.get(uploadDir, savedFileName);

            java.nio.file.Files.createDirectories(path.getParent());

            profileImage.write(path.toString());
        }

        UserData user = new UserData();
        user.setUsername(username);
        user.setEmail(email);
        user.setRole(role);
        user.setStatus(status);

        if ("user".equals(role)) {
            user.setFurigana(furigana);
            user.setGender(gender);
            user.setAge(Integer.parseInt(age));
            user.setBio(bio);
            user.setProfileImage(savedFileName); 
        }

        UserDao dao = new UserDao();
        dao.insertUser(user);

        response.sendRedirect("account-list.jsp");
    }
}
