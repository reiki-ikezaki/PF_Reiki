package controller;

import java.io.File;
import java.io.IOException;

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

@WebServlet("/profileEdit")
@MultipartConfig(maxFileSize = 1024 * 1024 * 2) // ★ 2MB制限
public class ProfileEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        UserData user = (UserData) session.getAttribute("user");

        try {
            // ▼ 画像ファイル取得
            Part part = request.getPart("profileImage");

            if (part != null && part.getSize() > 0) {

                // ▼ ファイル名取得（小文字化して拡張子判定）
                String submittedName = part.getSubmittedFileName().toLowerCase();

                // ▼ 拡張子チェック（画像のみ許可）
                if (!(submittedName.endsWith(".jpg") ||
                      submittedName.endsWith(".jpeg") ||
                      submittedName.endsWith(".png") ||
                      submittedName.endsWith(".gif"))) {

                    request.setAttribute("error", "正しい画像ファイル（jpg / jpeg / png / gif）を選択してください。");
                    forward(request, response);
                    return;
                }

                // ▼ 2MBチェック
                if (part.getSize() > 1024 * 1024 * 2) {
                    request.setAttribute("error", "画像は2MB以下にしてください。");
                    forward(request, response);
                    return;
                }

                // ▼ 保存処理
                String fileName = System.currentTimeMillis() + "_" + submittedName;
                String uploadPath = request.getServletContext().getRealPath("/uploads");
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdirs();

                part.write(uploadPath + "/" + fileName);

                user.setProfileImage(fileName);
            }

        } catch (Exception e) {
            // ★ Tomcatの413例外をキャッチして JSP に戻す
            request.setAttribute("error", "画像は2MB以下にしてください。");
            forward(request, response);
            return;
        }

        // ▼ 以下はあなたの元コードそのまま
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String name = request.getParameter("name");

        if (!isValidEmail(email)) {
            request.setAttribute("error", "メールアドレスの形式が不正、または255文字を超えています。");
            forward(request, response);
            return;
        }

        if (password == null || password.isEmpty()) {
            request.setAttribute("error", "パスワードを入力してください。");
            forward(request, response);
            return;
        }

        if (!isValidPassword(password)) {
            request.setAttribute("error", "パスワードは8〜32文字の半角英数字と _ - のみ使用できます。");
            forward(request, response);
            return;
        }

        if (!isValidName(name)) {
            request.setAttribute("error", "名前は255文字以内で入力してください。");
            forward(request, response);
            return;
        }

        UserDao dao = new UserDao();
        boolean updated = dao.updateUser(user.getId(), email, password, name);

        if (!updated) {
            request.setAttribute("error", "更新に失敗しました。");
            forward(request, response);
            return;
        }

        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        session.setAttribute("user", user);

        request.setAttribute("success", "プロフィールを更新しました！");
        forward(request, response);
    }

    private void forward(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("profile-edit.jsp").forward(request, response);
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        if (email.length() > 255) return false;

        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(regex);
    }

    private boolean isValidPassword(String pass) {
        if (pass == null) return false;
        if (pass.length() < 8 || pass.length() > 32) return false;

        String regex = "^[A-Za-z0-9_-]+$";
        return pass.matches(regex);
    }

    private boolean isValidName(String name) {
        if (name == null || name.isEmpty()) return false;
        return name.length() <= 255;
    }
}
