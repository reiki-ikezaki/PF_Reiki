package filter;

import java.io.IOException;
import java.util.Set;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.UserData;

// ログイン・権限の確認
@WebFilter("/*")
public class AuthFilter implements Filter {

    //  誰でも開けるURL
    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/login", "/logout", "/public_top", "/public_ranking", "/profileImage",
            "/contact", "/accountDetail", "/like",
            "/login.jsp", "/public_top.jsp", "/public_ranking.jsp", "/contact.jsp",
            "/contact_success.jsp", "/account-detail.jsp");

    //  ログインしていれば開けるURL
    private static final Set<String> LOGIN_REQUIRED_PATHS = Set.of(
            "/userDashboard", "/profileEdit", "/settings",
            "/user-dashboard.jsp", "/profile-edit.jsp", "/settings.jsp");

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

       
      String path = request.getServletPath();

        //  公開URLはそのまま通す
        if (PUBLIC_PATHS.contains(path)) {
            chain.doFilter(req, res);
            return;
        }

        // ログインが必要。セッションからログイン中のユーザーを取り出す
        HttpSession session = request.getSession(false);
        UserData loginUser = (session != null) ? (UserData) session.getAttribute("loginUser") : null;

        // 未ログイン → ログイン画面へ
        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        //  ログイン済みならOKのURL
        if (LOGIN_REQUIRED_PATHS.contains(path)) {
            chain.doFilter(req, res);
            return;
        }

        //  それ以外は管理者だけ。管理者でなければ、ログイン画面へ戻す
        if (!"admin".equals(loginUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        chain.doFilter(req, res);
    }
}
