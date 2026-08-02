package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.InquiryDao;
import model.InquiryData;

@WebServlet("/contact_detail")
public class ContactDetailServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        InquiryDao dao = new InquiryDao();
        InquiryData data = dao.findById(id);

        request.setAttribute("detail", data);

        request.getRequestDispatcher("contact_detail.jsp").forward(request, response);
    }
}
