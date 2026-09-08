<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.UserData" %>
<%@ page import="model.LikeRanking" %>
<%@ page import="util.HtmlUtil" %>

<%
    List<UserData> users = (List<UserData>) request.getAttribute("users");
    List<LikeRanking> ranking = (List<LikeRanking>) request.getAttribute("ranking");
    boolean loggedIn = Boolean.TRUE.equals(request.getAttribute("loggedIn"));
    String dashboardUrl = (String) request.getAttribute("dashboardUrl");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>公開トップページ</title>

<style>
    * { box-sizing: border-box; }
    body { font-family: Arial; background: #f5f5f5; padding: 20px; margin: 0; }
    .box { background: white; padding: 20px; border-radius: 8px; width: 800px; max-width: 100%; margin: 60px auto 20px; }
    .user-card { border-bottom: 1px solid #ddd; padding: 10px; }
    .like-btn { background: #3498db; color: white; padding: 6px 12px; border-radius: 4px; text-decoration: none; }
    .login-btn {
        position: absolute;
        top: 20px;
        right: 20px;
        background: #007bff;
        color: white;
        padding: 10px 18px;
        border-radius: 6px;
        text-decoration: none;
        font-weight: bold;
    }
    .login-btn:hover { background: #0056b3; }
    .header-actions {
        position: absolute;
        top: 20px;
        right: 20px;
        display: flex;
        gap: 10px;
        white-space: nowrap;
    }
    .header-actions .login-btn {
        position: static;
    }
    .header-actions form { margin: 0; }
    .header-actions button {
        background: #aaa;
        color: white;
        padding: 10px 18px;
        border: none;
        border-radius: 6px;
        font-weight: bold;
        cursor: pointer;
    }
    .like-btn:disabled { background: #aaa; cursor: not-allowed; }
    .detail-link {
        display: inline-block; margin-left: 10px;
        background: #3498db; color: white;
        padding: 6px 12px; border-radius: 4px;
        text-decoration: none;
    }
    .detail-link:hover { background: #2980b9; }
    .like-msg { color: #e74c3c; font-size: 13px; margin-top: 6px; }

    @media (max-width: 600px) {
        body { padding: 12px; }
        .login-btn {
            position: static;
            display: block;
            width: fit-content;
            margin: 0 0 12px auto;
        }
        .header-actions {
            position: static;
            flex-wrap: wrap;
            justify-content: flex-end;
            margin: 0 0 12px 0;
        }
        .header-actions .login-btn {
            margin: 0;
        }
        .box { margin-top: 0; }
    }
</style>

<script>
async function sendLike(btn, targetUserId) {
    btn.disabled = true;
    try {
        const res = await fetch("like", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: "targetUserId=" + encodeURIComponent(targetUserId) + "&scope=monthly"
        });
        const data = await res.json();

        if (!res.ok || !data.success) {
            const msg = document.getElementById("likeMsg-" + targetUserId);
            if (msg) msg.textContent = data.message || "いいねに失敗しました。";
            btn.disabled = false;
            return;
        }

        const countEl = document.getElementById("likeCount-" + targetUserId);
        if (countEl) countEl.textContent = data.likeCount;

    } catch (e) {
        btn.disabled = false;
    }
}
</script>

</head>
<body>

<!-- 右上：ログイン状態に応じて出し分け -->
<% if (loggedIn) { %>
    <div class="header-actions">
        <a href="<%= dashboardUrl %>" class="login-btn">マイページに戻る</a>
        <form action="logout" method="post">
            <button type="submit">ログアウト</button>
        </form>
    </div>
<% } else { %>
    <a href="login.jsp" class="login-btn">ログイン</a>
<% } %>

<div class="box">
    <h2>公開ユーザー一覧</h2>

    <% for (UserData u : users) { %>
        <div class="user-card">
            <img src="profileImage?id=<%= u.getId() %>"
                 style="width:80px; height:80px; object-fit:cover; border-radius:8px; border:1px solid #ccc; display:block; margin-bottom:8px;"
                 onerror="this.style.display='none';">
            <p><b>ユーザー名:</b> <%= u.getUsername() %></p>
            <p><b>ふりがな:</b> <%= u.getFurigana() %></p>
            <p><b>性別:</b> <%= HtmlUtil.genderLabel(u.getGender()) %></p>
            <p><b>年齢:</b> <%= u.getAge() %></p>

            <!-- ★ getProfile() → getBio() に修正済み -->
            <p><b>自己紹介:</b> <%= u.getBio() %></p>

            <p><b>いいね数:</b> <span id="likeCount-<%= u.getId() %>"><%= u.getLikeCount() %></span></p>

            <button type="button" class="like-btn" onclick="sendLike(this, <%= u.getId() %>)">いいね</button>
            <a class="detail-link" href="accountDetail?id=<%= u.getId() %>">詳細を見る</a>
            <div class="like-msg" id="likeMsg-<%= u.getId() %>"></div>
        </div>
    <% } %>

    <h2>いいねランキング（今月）</h2>

    <%
        int rankShown = 0;
        for (LikeRanking r : ranking) {
            if (rankShown >= 5) break;
            rankShown++;
    %>
        <p><%= r.getName() %> → <%= r.getLikeCount() %> いいね</p>
    <% } %>

</div>

</body>
</html>
