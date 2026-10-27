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

@WebServlet("/CreateUserServlet")
@MultipartConfig(maxFileSize = 1024 * 1024 * 10)
public class CreateUserServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        Part filePart = null;
        try {
            filePart = request.getPart("profileImage");
        } catch (Exception e) {
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

    private void showError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        showError(request, response, List.of(message));
    }

    private void showError(HttpServletRequest request, HttpServletResponse response, List<String> errors)
            throws ServletException, IOException {
        getServletContext().log("[アカウント追加] エラー: " + errors
                + " / Content-Type=" + request.getContentType()
                + " / 届いた項目=" + request.getParameterMap().keySet());

        request.setAttribute("errors", errors);
        request.getRequestDispatcher("account-add.jsp").forward(request, response);
    }
}
