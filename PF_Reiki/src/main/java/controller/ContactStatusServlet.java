package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// ★ InquiryDao を使うために必要
import dao.InquiryDao;

@WebServlet("/contactStatus")
public class ContactStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        String status = request.getParameter("status");

        if (idStr != null && status != null) {
            int id = Integer.parseInt(idStr);

            InquiryDao dao = new InquiryDao();
            dao.updateStatus(id, status);
        }

        response.sendRedirect("contact_detail?id=" + idStr);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
