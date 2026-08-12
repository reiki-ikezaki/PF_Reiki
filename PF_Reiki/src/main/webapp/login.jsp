<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログイン</title>

<style>
    body {
        font-family: Arial, sans-serif;
        background: #f5f5f5;
        margin: 0;
        height: 100vh;
        display: flex;
        flex-direction: column;   
        justify-content: center;  
        align-items: center;       
    }

    .login-container {
        display: block;   
        position: fixed;          
        top: 20%;               
        left: 50%;
        transform: translate(-50%, 0);
        background: white;
        padding: 20px;
        width: 300px;
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
        width: 100%;
        padding: 10px;
        border: 1px solid #ccc;
        border-radius: 5px;
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

    .close-btn {
        background: #aaa;
        margin-top: 10px;
    }

    .error {
        color: red;
        text-align: center;
        margin-top: 10px;
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
</div>

</body>
</html>
