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

        int id = Integer.parseInt(request.getParameter("id"));
        UserDao dao = new UserDao();

        Part filePart;
        try {
            filePart = request.getPart("profileImage");
        } catch (Exception e) {
            request.setAttribute("error", "プロフィール画像は2MB以下にしてください。");
            request.setAttribute("user", dao.findById(id));
            request.getRequestDispatcher("edit-account.jsp").forward(request, response);
            return;
        }

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String furigana = request.getParameter("furigana");
        String gender = request.getParameter("gender");
        String ageStr = request.getParameter("age");
        String bio = request.getParameter("bio");

        String error = AccountValidator.validateName(name);
        if (error == null) error = AccountValidator.validateEmail(email);
        if (error == null) error = AccountValidator.validatePassword(password);
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

        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("user", dao.findById(id));
            request.getRequestDispatcher("edit-account.jsp").forward(request, response);
            return;
        }

        byte[] imageBytes = null;
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

        UserData user = new UserData();
        user.setId(id);
        user.setRole(request.getParameter("role"));
        user.setStatus(request.getParameter("status"));
        user.setUsername(request.getParameter("username"));
        user.setEmail(email);
        user.setName(name);
        user.setFurigana(furigana);
        user.setGender(gender);
        user.setBio(bio);

        int age = 0;
        if (ageStr != null && !ageStr.isEmpty()) {
            age = Integer.parseInt(ageStr);
        }
        user.setAge(age);

        boolean result = dao.updateAccount(user, password, imageBytes);

        if (result) {
            response.sendRedirect("accountList");
        } else {
            request.setAttribute("error", "更新に失敗しました");
            request.setAttribute("user", dao.findById(id));
            request.getRequestDispatcher("edit-account.jsp").forward(request, response);
        }
    }
}
