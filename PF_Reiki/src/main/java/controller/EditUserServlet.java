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
import jakarta.servlet.http.Part;

import dao.UserDao;
import model.UserData;
import util.AccountValidator;

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

        List<String> errors = new ArrayList<>();
        AccountValidator.addIfError(errors, AccountValidator.validateRole(role));
        AccountValidator.addIfError(errors, AccountValidator.validateStatus(status));
        String usernameError = AccountValidator.validateUsername(username);
        AccountValidator.addIfError(errors, usernameError);
        UserData current = dao.findById(id);
        boolean usernameChanged = current == null || username == null || !username.equals(current.getUsername());
        if (usernameError == null && usernameChanged && dao.existsUsername(username, id)) {
            errors.add("このユーザー名は既に使われています。");
        }
        AccountValidator.addIfError(errors, AccountValidator.validateEmail(email));
        AccountValidator.addIfError(errors, AccountValidator.validateName(name));
        AccountValidator.addIfError(errors, AccountValidator.validatePassword(password));

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
            showError(request, response, dao, id, errors);
            return;
        }

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

    private void showError(HttpServletRequest request, HttpServletResponse response,
            UserDao dao, int id, String message) throws ServletException, IOException {
        showError(request, response, dao, id, List.of(message));
    }

    private void showError(HttpServletRequest request, HttpServletResponse response,
            UserDao dao, int id, List<String> errors) throws ServletException, IOException {
        getServletContext().log("[アカウント編集] エラー: " + errors
                + " / Content-Type=" + request.getContentType()
                + " / 届いた項目=" + request.getParameterMap().keySet());

        request.setAttribute("errors", errors);
        request.setAttribute("user", dao.findById(id));
        request.getRequestDispatcher("edit-account.jsp").forward(request, response);
    }
}
