<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>お問い合わせ送信完了</title>

<style>
    * { box-sizing: border-box; }
    body {
        font-family: Arial, sans-serif;
        background: #f5f5f5;
        text-align: center;
        padding: 48px 16px 24px;
        margin: 0;
    }
    .box {
        background: white;
        padding: 40px;
        margin: auto;
        width: 440px;
        max-width: 100%;
        border-radius: 8px;
        box-shadow: 0 0 10px #ccc;
    }
    h2 {
        font-size: 20px;
        line-height: 1.5;
        margin: 0 0 12px;
    }
    p {
        font-size: 15px;
        color: #555;
        margin: 0 0 24px;
    }
    .btn {
        display: inline-block;
        padding: 10px 20px;
        background: #3498db;
        color: white;
        text-decoration: none;
        border-radius: 4px;
    }
</style>

</head>
<body>

<div class="box">
    <h2>お問い合わせ<br>ありがとうございました</h2>
    <p>担当者が確認いたします。</p>

    <!-- ★ ログイン画面へ戻る -->
    <a class="btn" href="login.jsp">ログイン画面へ戻る</a>
</div>

</body>
</html>
