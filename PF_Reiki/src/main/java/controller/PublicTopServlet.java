package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.LikeDao;
import dao.UserDao;
import model.UserData;

@WebServlet("/public_top")
public class PublicTopServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 一般ユーザー一覧
        UserDao userDao = new UserDao();
        List<UserData> users = userDao.getGeneralUserList();


        // いいねランキング
        LikeDao likeDao = new LikeDao();
        List<UserData> ranking = likeDao.getRanking();

        request.setAttribute("users", users);
        request.setAttribute("ranking", ranking);

        request.getRequestDispatcher("public_top.jsp").forward(request, response);
    }
}
