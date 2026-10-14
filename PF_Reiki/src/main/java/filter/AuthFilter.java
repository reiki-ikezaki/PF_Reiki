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

// ログイン・権限の確認をする「門番」。すべてのURLが、サーブレット／JSPに届く前にここを通る。
//   ① PUBLIC_PATHS      … 誰でも開ける（ログイン不要）
//   ② LOGIN_REQUIRED_PATHS … ログイン済みなら開ける（一般ユーザー・管理者どちらも可）
//   ③ 上記以外すべて     … 管理者（role = admin）だけが開ける
// 新しい画面を追加したとき、ここに書き忘れても「管理者だけ」になるので、安全側に倒れる。
@WebFilter("/*")
public class AuthFilter implements Filter {

    // ① 誰でも開けるURL
    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/login", "/logout", "/public_top", "/profileImage",
            "/contact", "/accountDetail", "/like",
            "/login.jsp", "/public_top.jsp", "/contact.jsp",
            "/contact_success.jsp", "/account-detail.jsp");

    // ② ログインしていれば開けるURL
    private static final Set<String> LOGIN_REQUIRED_PATHS = Set.of(
            "/userDashboard", "/profileEdit",
            "/user-dashboard.jsp", "/profile-edit.jsp");

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // アプリ内のパス（例：/accountList）。?id=1 などのパラメータは含まれない
        String path = request.getServletPath();

        // ① 公開URLはそのまま通す
        if (PUBLIC_PATHS.contains(path)) {
            chain.doFilter(req, res);
            return;
        }

        // ここから先はログインが必要。セッションからログイン中のユーザーを取り出す
        HttpSession session = request.getSession(false);
        UserData loginUser = (session != null) ? (UserData) session.getAttribute("loginUser") : null;

        // 未ログイン → ログイン画面へ
        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // ② ログイン済みならOKのURL
        if (LOGIN_REQUIRED_PATHS.contains(path)) {
            chain.doFilter(req, res);
            return;
        }

        // ③ それ以外は管理者だけ。管理者でなければ、ログイン画面へ戻す
        if (!"admin".equals(loginUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        chain.doFilter(req, res);
    }
}
