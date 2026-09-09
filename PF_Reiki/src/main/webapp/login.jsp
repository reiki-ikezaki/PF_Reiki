<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>ログイン</title>

<style>
    * {
        box-sizing: border-box;
    }

    body {
        font-family: Arial, sans-serif;
        background: #f5f5f5;
        margin: 0;
        min-height: 100vh;
        padding: 24px 16px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
    }

    .login-container {
        display: block;
        background: white;
        padding: 24px;
        width: 320px;
        max-width: 100%;
        border-radius: 10px;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }

    .form-group {
        margin-bottom: 15px;
    }

    label {
        display: block;
        font-weight: bold;
        margin-bottom: 5px;
    }

    input {
        display: block;
        width: 100%;
        padding: 10px;
        border: 1px solid #ccc;
        border-radius: 5px;
        font-size: 16px;
    }

    /* ブラウザ標準の「目」「×」アイコンが右端に重なって表示崩れするのを抑止 */
    input::-ms-reveal,
    input::-ms-clear {
        display: none;
    }

    button {
        width: 100%;
        padding: 10px;
        background: #007bff;
        color: white;
        border: none;
        border-radius: 5px;
        cursor: pointer;
    }

    .error {
        color: red;
        text-align: center;
        margin-top: 10px;
    }

    .contact-link {
        display: inline-block;
        margin-top: 10px;
        background: #aaa;
        color: white;
        padding: 8px 16px;
        border-radius: 5px;
        text-decoration: none;
        font-weight: bold;
    }

    .contact-link:hover {
        background: #888;
    }
</style>

</head>
<body>

<div id="loginBox" class="login-container">
    <form action="/PF_Reiki/login" method="post">

        <div class="form-group">
            <label>ユーザーネーム</label>
            <input type="text" name="username" required>
        </div>

        <div class="form-group">
            <label>パスワード</label>
            <input type="password" name="password" required>
        </div>

        <button type="submit">ログイン</button>
    </form>

    <div class="error">
        ${errorMessage}
    </div>

    <br>
    <a href="contact" class="contact-link">お問い合わせはこちら</a>

</div>

</body>
</html>
