<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザーダッシュボード</title>

<style>
    body {
        font-family: Arial, sans-serif;
        background: #f5f5f5;
        margin: 0;
        padding: 0;
        display: flex;
    }

    .sidebar {
        width: 200px;
        background: #007bff;
        height: 100vh;
        padding-top: 30px;
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .sidebar a {
        width: 160px;
        padding: 12px;
        margin: 10px 0;
        background: white;
        color: #007bff;
        text-align: center;
        text-decoration: none;
        border-radius: 6px;
        font-weight: bold;
    }

    .sidebar a:hover {
        background: #e6e6e6;
    }

    .logout-btn {
        position: absolute;
        top: 10px;
        right: 10px;
    }

    .logout-btn button {
        padding: 6px 12px;
        background: #333;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }

    .main {
        flex: 1;
        padding: 40px;
        text-align: center;
    }

    .likes-box {
        background: white;
        padding: 30px;
        border-radius: 10px;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
        display: inline-block;
        min-width: 400px;
    }

    .likes-box h2 {
        margin-bottom: 20px;
    }
</style>
</head>
<body>

<div class="sidebar">
    <a href="#">プロフィール</a>
    <a href="#">タイムライン</a>
</div>

<form action="LogoutServlet" method="post" class="logout-btn">
    <button type="submit">ログアウト</button>
</form>

<div class="main">
    <div class="likes-box">
        <h2>❤️${username}さんのいいね件数❤️</h2>

        <p>今月の獲得いいね数：${monthlyLikes}</p>
        <p>今年の獲得いいね数：${yearlyLikes}</p>
    </div>
</div>

</body>
</html>
