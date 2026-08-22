<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.UserData" %>

<%
    List<UserData> users = (List<UserData>) request.getAttribute("users");
    List<UserData> ranking = (List<UserData>) request.getAttribute("ranking");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>公開トップページ</title>

<style>
    body { 
        font-family: Arial; 
        background: #f5f5f5; 
        padding: 20px; 
    }

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
    .login-btn:hover {
        background: #0056b3;
    }

    .box { 
        background: white; 
        padding: 20px; 
        border-radius: 8px; 
        width: 800px; 
        margin: auto; 
    }
    .user-card { 
        border-bottom: 1px solid #ddd; 
        padding: 10px; 
    }
    .like-btn { 
        background: #3498db; 
        color: white; 
        padding: 6px 12px; 
        border-radius: 4px; 
        text-decoration: none; 
    }
</style>

</head>
<body>

<!-- 右上ログインボタン -->
<a href="login.jsp" class="login-btn">ログイン</a>

<div class="box">
    <h2>公開ユーザー一覧</h2>

    <% for (UserData u : users) { %>
        <div class="user-card">
            <p><b>名前:</b> <%= u.getName() %></p>
            <p><b>自己紹介:</b> <%= u.getProfile() %></p>
            <p><b>いいね数:</b> <%= u.getLikeCount() %></p>

            <!-- 公開画面用のいいねボタン -->
            <a class="like-btn" href="like?userId=0&targetUserId=<%= u.getId() %>">いいね</a>
        </div>
    <% } %>

    <h2>いいねランキング</h2>

    <% for (UserData r : ranking) { %>
        <p><%= r.getName() %> → <%= r.getLikeCount() %> いいね</p>
    <% } %>

</div>

</body>
</html>
