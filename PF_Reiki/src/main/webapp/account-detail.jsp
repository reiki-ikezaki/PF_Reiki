<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="model.UserData" %>
<%@ page import="util.HtmlUtil" %>

<%
    UserData u = (UserData) request.getAttribute("detailUser");
    Integer likeCount = (Integer) request.getAttribute("likeCount");
    String backUrl = (String) request.getAttribute("backUrl");
    if (backUrl == null) backUrl = "public_top";
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>アカウント詳細</title>

<style>
    * { box-sizing: border-box; }
    body { font-family: Arial, sans-serif; background: #f5f5f5; padding: 16px; margin: 0; }
    .box {
        background: white; padding: 30px; border-radius: 10px;
        width: 500px; max-width: 100%; margin: 24px auto;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
        text-align: center;
    }
    img.profile {
        width: 140px; height: 140px; object-fit: cover;
        border-radius: 50%; border: 1px solid #ccc; margin-bottom: 16px;
    }
    p { text-align: left; word-break: break-word; }

    @media (max-width: 600px) {
        .box { padding: 20px; }
    }
    .like-btn {
        background: #3498db; color: white; padding: 8px 16px;
        border-radius: 4px; border: none; text-decoration: none;
        font-size: 15px; cursor: pointer;
    }
    .like-btn:disabled { background: #aaa; cursor: not-allowed; }
    .like-msg { color: #e74c3c; font-size: 13px; margin-top: 8px; }
    .back-link {
        display: inline-block; margin-top: 20px;
        background: #3498db; color: white;
        padding: 8px 16px; border-radius: 6px;
        text-decoration: none; font-weight: bold;
    }
    .back-link:hover { background: #2980b9; }
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
            document.getElementById("likeMsg").textContent = data.message || "いいねに失敗しました。";
            btn.disabled = false;
            return;
        }

        document.getElementById("likeCount").textContent = data.likeCount;

    } catch (e) {
        btn.disabled = false;
    }
}
</script>

</head>
<body>

<% if (u == null) { %>
    <div class="box"><p>ユーザーが見つかりません。</p></div>
<% } else { %>
    <div class="box">
        <img class="profile" src="profileImage?id=<%= u.getId() %>"
             onerror="this.style.display='none';">

        <p><b>名前:</b> <%= u.getName() %></p>
        <p><b>フリガナ:</b> <%= u.getFurigana() %></p>
        <p><b>性別:</b> <%= HtmlUtil.genderLabel(u.getGender()) %></p>
        <p><b>年齢:</b> <%= u.getAge() %></p>
        <p><b>自己紹介:</b><br><%= u.getBio() %></p>
        <p><b>いいね数:</b> <span id="likeCount"><%= likeCount %></span></p>

        <button type="button" class="like-btn" onclick="sendLike(this, <%= u.getId() %>)">いいね</button>
        <div class="like-msg" id="likeMsg"></div>

        <br>
        <a class="back-link" href="<%= backUrl %>">← 一覧に戻る</a>
    </div>
<% } %>

</body>
</html>
