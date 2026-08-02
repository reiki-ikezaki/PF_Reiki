package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.InquiryDao;
import model.InquiryData;

@WebServlet("/contact_list")
public class ContactListServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        InquiryDao dao = new InquiryDao();
        List<InquiryData> list = dao.findAll();

        request.setAttribute("inquiryList", list);

        request.getRequestDispatcher("contact_list.jsp").forward(request, response);
    }
}
