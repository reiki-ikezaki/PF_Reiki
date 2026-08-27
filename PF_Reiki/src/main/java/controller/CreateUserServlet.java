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

@WebServlet("/CreateUserServlet")
@MultipartConfig(maxFileSize = 1024 * 1024 * 2)
public class CreateUserServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Part filePart;
        try {
            filePart = request.getPart("profileImage");
        } catch (Exception e) {
            request.setAttribute("error", "プロフィール画像は2MB以下にしてください。");
            request.getRequestDispatcher("account-add.jsp").forward(request, response);
            return;
        }

        String role = request.getParameter("role");
        String status = request.getParameter("status");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String furigana = request.getParameter("furigana");
        String gender = request.getParameter("gender");
        String ageStr = request.getParameter("age");
        String bio = request.getParameter("bio");

        String error = AccountValidator.validateName(name);
        if (error == null) error = AccountValidator.validateEmail(email);
        if (error == null) error = AccountValidator.validateFurigana(furigana);
        if (error == null) error = AccountValidator.validateGender(gender);
        if (error == null) error = AccountValidator.validateAge(ageStr);
        if (error == null) error = AccountValidator.validateBio(bio);
        if (error == null && filePart != null) {
            error = AccountValidator.validateImageSize(filePart.getSize());
        }

        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("account-add.jsp").forward(request, response);
            return;
        }

        InputStream img = null;
        if (filePart != null && filePart.getSize() > 0) {
            img = filePart.getInputStream();
        }

        int age = 0;
        if (ageStr != null && !ageStr.isEmpty()) {
            age = Integer.parseInt(ageStr);
        }

        UserData user = new UserData();
        user.setRole(role);
        user.setStatus(status);
        user.setUsername(username);
        user.setEmail(email);
        user.setName(name);
        user.setFurigana(furigana);
        user.setGender(gender);
        user.setAge(age);
        user.setBio(bio);

        UserDao dao = new UserDao();
        boolean result = dao.insertUser(user, img);

        if (result) {
            response.sendRedirect("accountList");
        } else {
            request.setAttribute("error", "登録に失敗しました");
            request.getRequestDispatcher("account-add.jsp").forward(request, response);
        }
    }
}
