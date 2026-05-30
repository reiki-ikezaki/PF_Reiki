<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
        padding: 20px;
    }
    .container {
        background: white;
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
        width: 600px;
        margin: auto;
    }
    h1 {
        text-align: center;
    }
    ul {
        list-style: none;
        padding: 0;
    }
    li {
        margin: 10px 0;
    }
    a {
        text-decoration: none;
        color: #007bff;
        font-weight: bold;
    }
    a:hover {
        text-decoration: underline;
    }
    .logout {
        margin-top: 30px;
        text-align: center;
    }
</style>
</head>
<body>

<div class="container">
    <h1>管理者ダッシュボード</h1>
    <p>管理者ログイン成功しました。</p>

    <ul>
        <li><a href="LikeRankingServlet">いいねランキング</a></li>
        <li><a href="AccountListServlet">アカウント一覧</a></li>
        <li><a href="InquiryListServlet">お問い合わせ一覧</a></li>
    </ul>

    <div class="logout">
        <form action="LogoutServlet" method="post">
            <button type="submit">ログアウト</button>
        </form>
    </div>
</div>

</body>
</html>
