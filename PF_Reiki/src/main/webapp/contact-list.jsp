<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>お問い合わせ一覧</title>

<style>
    body {
        font-family: Arial, sans-serif;
        background: #f5f5f5;
        margin: 0;
        padding: 0;
    }

    .container {
        width: 80%;
        margin: 40px auto;
        background: white;
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }

    h1 {
        text-align: center;
        margin-bottom: 20px;
    }

    textarea {
        width: 100%;
        height: 200px;
        padding: 10px;
        font-size: 16px;
        border-radius: 6px;
        border: 1px solid #ccc;
        resize: vertical;
    }

    .submit-btn {
        margin-top: 20px;
        background: #007bff;
        color: white;
        padding: 10px 20px;
        border: none;
        border-radius: 6px;
        cursor: pointer;
        font-size: 16px;
    }
</style>

</head>
<body>

<div class="container">
    <h1>お問い合わせ内容</h1>

    <!-- ★ 今は機能なし。後で contactListServlet を作る -->
    <form action="contactList" method="post">
        <textarea name="message" placeholder="お問い合わせ内容を入力してください"></textarea>

        <button type="submit" class="submit-btn">送信</button>
    </form>
</div>

</body>
</html>
