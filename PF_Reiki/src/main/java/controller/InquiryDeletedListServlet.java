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

@WebServlet("/contact_deleted_list")
public class InquiryDeletedListServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        InquiryDao dao = new InquiryDao();
        List<InquiryData> list = dao.findDeleted();

        request.setAttribute("deletedList", list);

        request.getRequestDispatcher("contact_deleted_list.jsp").forward(request, response);
    }
}
