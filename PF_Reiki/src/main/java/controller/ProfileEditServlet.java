package controller;

import java.io.ByteArrayOutputStream;
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
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import dao.UserDao;
import model.UserData;
import util.AccountValidator;

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

        List<String> errors = new ArrayList<>();
        String imageError = null;
        byte[] imageBytes = null;

        try {
            Part part = request.getPart("profileImage");

            if (part != null && part.getSize() > 0) {
                imageError = AccountValidator.validateImageExtension(part.getSubmittedFileName());
                if (imageError == null) imageError = AccountValidator.validateImageSize(part.getSize());

                if (imageError == null) {
                    try (InputStream in = part.getInputStream();
                         ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                        byte[] buf = new byte[8192];
                        int n;
                        while ((n = in.read(buf)) != -1) {
                            out.write(buf, 0, n);
                        }
                        imageBytes = out.toByteArray();
                    }
                    imageError = AccountValidator.validateImageContent(imageBytes);
                }
            }

        } catch (Exception e) {
            imageError = "プロフィール画像は2MB以下にしてください。";
        }
        AccountValidator.addIfError(errors, imageError);

        AccountValidator.addIfError(errors, AccountValidator.validateName(name));
        AccountValidator.addIfError(errors, AccountValidator.validateFurigana(furigana));
        AccountValidator.addIfError(errors, AccountValidator.validateGender(gender));
        AccountValidator.addIfError(errors, AccountValidator.validateAge(ageStr));
        AccountValidator.addIfError(errors, AccountValidator.validateBio(bio));

        if (!errors.isEmpty()) {
            showError(request, response, errors);
            return;
        }

        int age = 0;
        if (ageStr != null && !ageStr.isEmpty()) {
            age = Integer.parseInt(ageStr);
        }

        UserDao dao = new UserDao();
        boolean updated = dao.updateProfile(user.getId(), name, furigana, gender, age, bio, imageBytes);

        if (!updated) {
            showError(request, response, "更新に失敗しました。");
            return;
        }

        user.setName(name);
        user.setFurigana(furigana);
        user.setGender(gender);
        user.setAge(age);
        user.setBio(bio);
        if (imageBytes != null) {
            user.setHasImage(true);
        }
        session.setAttribute("user", user);

        request.setAttribute("success", "プロフィールを更新しました！");
        forward(request, response);
    }

    private void showError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        showError(request, response, List.of(message));
    }

    private void showError(HttpServletRequest request, HttpServletResponse response, List<String> errors)
            throws ServletException, IOException {
        request.setAttribute("errors", errors);
        forward(request, response);
    }

    private void forward(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("profile-edit.jsp").forward(request, response);
    }
}
