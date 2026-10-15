package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.UserDao;
import model.UserData;

// 公開画面「月のいいねランキング一覧」。URL: /public_ranking（ログイン不要）
// 一般ユーザーを、今月のいいね数が多い順に、一覧で表示する（公開トップと同じカード表示）。
@WebServlet("/public_ranking")
public class PublicRankingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserDao userDao = new UserDao();
        List<UserData> rankingUsers = userDao.getGeneralUserRankingThisMonth();

        // ?format=json のときは、順位の最新情報（id といいね数）だけをJSONで返す。
        // 画面の JavaScript が、定期的にこれを取りに行って、並び順を自動で更新する。
        if ("json".equals(request.getParameter("format"))) {
            writeRankingJson(response, rankingUsers);
            return;
        }

        request.setAttribute("rankingUsers", rankingUsers);

        // ログイン状態に応じて、右上のボタンを出し分ける（公開トップと同じ）
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            UserData loginUser = (UserData) session.getAttribute("loginUser");
            boolean isAdmin = loginUser != null && "admin".equals(loginUser.getRole());

            request.setAttribute("loggedIn", true);
            request.setAttribute("dashboardUrl", isAdmin ? "adminDashboard" : "userDashboard");
        }

        request.getRequestDispatcher("public_ranking.jsp").forward(request, response);
    }

    // 例：[{"id":3,"likeCount":5},{"id":2,"likeCount":2}]（いいねが多い順）
    private void writeRankingJson(HttpServletResponse response, List<UserData> users)
            throws IOException {

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < users.size(); i++) {
            UserData u = users.get(i);
            if (i > 0) {
                json.append(",");
            }
            json.append("{\"id\":").append(u.getId())
                .append(",\"likeCount\":").append(u.getLikeCount()).append("}");
        }
        json.append("]");

        response.setContentType("application/json; charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");
        response.getWriter().write(json.toString());
    }
}
