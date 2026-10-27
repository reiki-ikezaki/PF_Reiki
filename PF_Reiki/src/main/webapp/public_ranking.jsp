<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.UserData" %>
<%@ page import="util.HtmlUtil" %>

<%
    List<UserData> users = (List<UserData>) request.getAttribute("rankingUsers");
    boolean loggedIn = Boolean.TRUE.equals(request.getAttribute("loggedIn"));
    String dashboardUrl = (String) request.getAttribute("dashboardUrl");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>月のいいねランキング</title>

<style>
    /* 公開トップ（public_top.jsp）と同じ見た目にそろえている */
    * { box-sizing: border-box; }
    body { font-family: Arial; background: #f5f5f5; padding: 20px; margin: 0; }
    .box { background: white; padding: 20px; border-radius: 8px; width: 800px; max-width: 100%; margin: 60px auto 20px; }
    .user-card { border-bottom: 1px solid #ddd; padding: 10px; }
    .rank { font-weight: bold; color: #007bff; font-size: 18px; margin: 0 0 8px; }
    .like-btn { background: #3498db; color: white; padding: 6px 12px; border-radius: 4px; text-decoration: none; border: none; cursor: pointer; }
    .like-btn:disabled { background: #aaa; cursor: not-allowed; }
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
    .header-actions .login-btn { position: static; }
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
    .detail-link {
        display: inline-block; margin-left: 10px;
        background: #3498db; color: white;
        padding: 6px 12px; border-radius: 4px;
        text-decoration: none;
    }
    .detail-link:hover { background: #2980b9; }
    .like-msg { color: #e74c3c; font-size: 13px; margin-top: 6px; }
    .back-link {
        display: inline-block;
        background: #007bff;
        color: white;
        padding: 8px 16px;
        border-radius: 6px;
        text-decoration: none;
        font-weight: bold;
    }
    .back-link:hover { background: #0056b3; }

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
        .header-actions .login-btn { margin: 0; }
        .box { margin-top: 0; }
    }
</style>

<script>
// 「いいね」ボタン：この画面の中で、非同期（画面を再読み込みせず）にいいねを付けて、数だけ更新する
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

        // いいねを押したら、すぐに、いいね順へ並び替える
        refreshRanking();

    } catch (e) {
        btn.disabled = false;
    }
}

// 最新の順位をサーバーから取得して、カードを「いいねが多い順」に並び替える（画面は再読み込みしない）
async function refreshRanking() {
    try {
        const res = await fetch("public_ranking?format=json", { cache: "no-store" });
        if (!res.ok) return;
        const list = await res.json();

        const container = document.getElementById("rankingList");
        const cards = {};
        container.querySelectorAll(".user-card").forEach(function (card) {
            cards[card.dataset.userId] = card;
        });

        // ユーザーが増えた・減った場合は、画面を読み込み直す
        if (list.length !== Object.keys(cards).length || list.some(function (u) { return !cards[u.id]; })) {
            location.reload();
            return;
        }

        list.forEach(function (u, i) {
            const card = cards[u.id];
            card.querySelector(".rank").textContent = (i + 1) + " 位";
            card.querySelector(".like-count").textContent = u.likeCount;
            container.appendChild(card);   // 順番に、末尾へ移す ＝ 並び替え
        });
    } catch (e) {
        // 通信エラーのときは、何もしない（次の更新で、取り直す）
    }
}

// ほかの画面・ほかの人のいいねも反映するため、一定時間ごとに自動で更新する
setInterval(function () {
    if (!document.hidden) refreshRanking();
}, 5000);

// 別のタブから戻ってきたときも、すぐに更新する
document.addEventListener("visibilitychange", function () {
    if (!document.hidden) refreshRanking();
});
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
    <a class="back-link" href="public_top">← 公開ユーザー一覧に戻る</a>
    <h2>月のいいねランキング</h2>

<div id="rankingList">
    <%
        int rank = 1;
        if (users != null) {
            for (UserData u : users) {
    %>
        <div class="user-card" data-user-id="<%= u.getId() %>">
            <p class="rank"><%= rank++ %> 位</p>
            <% if (u.isHasImage()) { %>
            <img src="profileImage?id=<%= u.getId() %>"
                 style="width:80px; height:80px; object-fit:cover; border-radius:8px; border:1px solid #ccc; display:block; margin-bottom:8px;">
            <% } %>
            <p><b>ユーザー名:</b> <%= HtmlUtil.escape(u.getUsername()) %></p>
            <p><b>ふりがな:</b> <%= HtmlUtil.escape(u.getFurigana()) %></p>
            <p><b>性別:</b> <%= HtmlUtil.genderLabel(u.getGender()) %></p>
            <p><b>年齢:</b> <%= u.getAge() %></p>
            <p><b>自己紹介:</b> <%= HtmlUtil.escapeAndBr(u.getBio()) %></p>

            <p><b>今月のいいね数:</b> <span class="like-count" id="likeCount-<%= u.getId() %>"><%= u.getLikeCount() %></span></p>

            <button type="button" class="like-btn" onclick="sendLike(this, <%= u.getId() %>)">いいね</button>
            <a class="detail-link" href="accountDetail?id=<%= u.getId() %>&from=public_ranking">詳細を見る</a>
            <div class="like-msg" id="likeMsg-<%= u.getId() %>"></div>
        </div>
    <%
            }
        }
    %>
</div>

</div>

</body>
</html>
