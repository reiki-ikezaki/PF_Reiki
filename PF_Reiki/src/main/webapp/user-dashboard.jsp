<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>ユーザーダッシュボード</title>

<style>
    body {
        font-family: Arial, sans-serif;
        background: #f5f5f5;
        margin: 0;
        padding: 0;
    }

    .logout-btn {
        display: flex;
        justify-content: flex-end;
        padding: 10px 16px 0;
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
        width: 90%;
        max-width: 900px;
        margin: 24px auto;
        background: white;
        padding: 30px;
        border-radius: 10px;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
        text-align: center;
    }

    h1 {
        margin-bottom: 20px;
    }

    .likes-box {
        margin-top: 30px;
        padding: 20px;
        background: #f0f0f0;
        border-radius: 8px;
        text-align: left;
    }

    .likes-box h2 {
        margin-top: 0;
        text-align: center;
    }

    .like-stats {
        display: flex;
        gap: 16px;
        justify-content: center;
        flex-wrap: wrap;
    }

    .like-stat {
        flex: 1 1 180px;
        max-width: 260px;
        background: white;
        border-radius: 8px;
        padding: 16px;
        text-align: center;
        box-shadow: 0 0 6px rgba(0,0,0,0.08);
    }

    .like-stat .label {
        font-size: 14px;
        color: #555;
    }

    .like-stat .value {
        margin-top: 6px;
        font-size: 28px;
        font-weight: bold;
        color: #e0245e;
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

    @media (max-width: 600px) {
        .container {
            width: 90%;
            margin: 20px auto;
            padding: 20px;
        }
    }
</style>
</head>
<body>

<form action="logout" method="post" class="logout-btn">
    <button type="submit">ログアウト</button>
</form>

<div class="container">
    <h1>ユーザーダッシュボード</h1>

    <div class="likes-box">
        <h2>❤️ ${username}さんの獲得いいね ❤️</h2>

        <div class="like-stats">
            <div class="like-stat">
                <div class="label">今月の獲得いいね数</div>
                <div class="value">${monthlyLikes}</div>
            </div>
            <div class="like-stat">
                <div class="label">今年の獲得いいね数</div>
                <div class="value">${yearlyLikes}</div>
            </div>
        </div>
    </div>

    <div class="menu">
        <!-- ★ プロフィール編集画面へ遷移 -->
        <a href="profile-edit.jsp">プロフィール</a>
    </div>
</div>

</body>
</html>
