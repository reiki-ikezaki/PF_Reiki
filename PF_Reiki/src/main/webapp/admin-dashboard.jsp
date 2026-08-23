<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.LikeRanking" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>管理者ダッシュボード</title>

<style>
    body {
        font-family: Arial, sans-serif;
        background: #f5f5f5;
        margin: 0;
        padding: 0;
    }

    .logout-btn {
        position: absolute;
        top: 10px;
        right: 10px;
    }

    .logout-btn button {
        padding: 5px 10px;
        font-size: 12px;
        background: #aaa;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }

    .container {
        width: 80%;
        max-width: 900px;
        margin: 80px auto;
        background: white;
        padding: 30px;
        border-radius: 10px;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
        text-align: center;
    }

    h1 {
        margin-bottom: 20px;
    }

    .menu {
        margin-top: 30px;
    }

    .menu a {
        display: inline-block;
        margin: 10px 20px;
        padding: 10px 20px;
        background: #007bff;
        color: white;
        text-decoration: none;
        border-radius: 6px;
    }

    .menu a:hover {
        background: #0056b3;
    }

    .likes-box {
        margin-top: 30px;
        padding: 20px;
        background: #f0f0f0;
        border-radius: 8px;
        text-align: left;
    }
</style>
</head>
<body>

<form action="logout" method="post" class="logout-btn">
    <button type="submit">ログアウト</button>
</form>


<div class="container">
    <h1>管理者ダッシュボード</h1>

    <div class="likes-box">
        <h2>いいねランキング（今年・上位5件）</h2>
        <ul>
            <%
                List<LikeRanking> rankingList = (List<LikeRanking>) request.getAttribute("rankingList");
                if (rankingList != null) {
                    int shown = 0;
                    for (LikeRanking r : rankingList) {
                        if (shown >= 5) break;
                        shown++;
            %>
                <li><%= r.getName() %> → <%= r.getLikeCount() %>いいね</li>
            <%
                    }
                    if (shown == 0) {
            %>
                <li>まだいいねがありません</li>
            <%
                    }
                }
            %>
        </ul>
    </div>

    <div class="menu">
        <a href="accountList">アカウント一覧</a>
        <a href="contact_list">お問い合わせ一覧</a>
        <a href="deleteAccountList">削除済みアカウント一覧</a>
        <a href="ranking">いいねランキング</a>
        <a href="generalList">一般ユーザー一覧</a>
        <a href="profile-edit.jsp">アカウント設定</a>

    </div>

</div>

</body>
</html>
