package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.UserDao;
import model.UserData;
import util.AccountValidator;
import util.PasswordUtil;

@WebServlet("/settings")
public class SettingsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("settings.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        UserData user = (UserData) session.getAttribute("user");
        boolean isAdmin = "admin".equals(user.getRole());

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String name = request.getParameter("name");

        List<String> errors = new ArrayList<>();
        AccountValidator.addIfError(errors, AccountValidator.validateEmail(email));
        AccountValidator.addIfError(errors, AccountValidator.validatePassword(password));
        if (isAdmin) {
            AccountValidator.addIfError(errors, AccountValidator.validateName(name));
        }

        if (!errors.isEmpty()) {
            showError(request, response, errors);
            return;
        }

        UserDao dao = new UserDao();
        boolean updated = isAdmin
                ? dao.updateAdminSettings(user.getId(), email, password, name)
                : dao.updateEmailAndPassword(user.getId(), email, password);

        if (!updated) {
            showError(request, response, "更新に失敗しました。");
            return;
        }

        user.setEmail(email);
        if (password != null && !password.isEmpty()) {
            user.setPassword(PasswordUtil.hash(password));
        }
        if (isAdmin) {
            user.setName(name);
        }
        session.setAttribute("user", user);

        request.setAttribute("success", "設定を更新しました！");
        request.getRequestDispatcher("settings.jsp").forward(request, response);
    }

    private void showError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        showError(request, response, List.of(message));
    }

    private void showError(HttpServletRequest request, HttpServletResponse response, List<String> errors)
            throws ServletException, IOException {
        request.setAttribute("errors", errors);
        request.getRequestDispatcher("settings.jsp").forward(request, response);
    }
}
