package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        
        if(username == null || username.isEmpty()) {
        	request.setAttribute("errorMessage","ユーザーネームを入力してください");
        	request.getRequestDispatcher("login.jsp").forward(request, response);
        return;       
        }
        
        if(username.length() > 255) {
        	request.setAttribute("errorMessage","ユーザーネームは255文字以内で入力してください");
        	request.getRequestDispatcher("login.jsp").forward(request, response);
        	return;
        }
        
        if(password == null || password.isEmpty()) {
        	request.setAttribute("errorMessage","パスワードを入力してください");
        	request.getRequestDispatcher("login.jsp").forward(request, response);
        	return;
        }
        
        if (!password.matches("^[a-zA-Z0-9_-]{8,32}$")) {
            request.setAttribute("errorMessage", "パスワードは8〜32文字の半角英数字と _ - のみ使用できます");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
        
        
        if ("admin".equals(username) && "admin123".equals(password)) {
            response.sendRedirect("admin-dashboard.jsp");
        } else if ("user".equals(username) && "user1234".equals(password)) {
            response.sendRedirect("user-dashboard.jsp");
        } else {
            request.setAttribute("errorMessage", "ユーザーネームまたはパスワードが違います");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}

