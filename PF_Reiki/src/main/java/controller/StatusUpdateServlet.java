package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.InquiryDao;

@WebServlet("/status_update")
public class StatusUpdateServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        int id = Integer.parseInt(request.getParameter("id"));
        String status = request.getParameter("status");

        InquiryDao dao = new InquiryDao();
        dao.updateStatus(id, status);

        response.sendRedirect("contact_detail?id=" + id);
    }
}
