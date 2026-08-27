package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.InquiryDao;

@WebServlet("/contact")
public class ContactStatusServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int categoryId = Integer.parseInt(request.getParameter("category_id"));
        String content = request.getParameter("content");
        String email = request.getParameter("email");

        InquiryDao dao = new InquiryDao();
        dao.insertInquiry(categoryId, content, email);

        response.sendRedirect("contact_success.jsp");
    }
}
