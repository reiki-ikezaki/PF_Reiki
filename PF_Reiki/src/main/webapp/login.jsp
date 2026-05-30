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
    }

 
  	.login-btn {
    		position: absolute;
    		top: 10px;
    		right: 10px;
    		padding: 2px 6px;  
    		font-size: 12px;    
    		width: 80px;        
    		background: #007bff;
    		color: white;
    		border: none;
    		border-radius: 4px;
	    cursor: pointer;
}

    .login-container {
        display: none;
        position: absolute;
        top: 120px;
        right: 20px;
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

<script>
    function toggleLogin() {
        const box = document.getElementById("loginBox");
        box.style.display = (box.style.display === "block") ? "none" : "block";
    }
</script>

</head>
<body>


<button class="login-btn" onclick="toggleLogin()">ログイン</button>

<div id="loginBox" class="login-container">
    <form action="login" method="post">
        <div class="form-group">
            <label>ユーザーネーム</label>
            <input type="text" name="username">
        </div>

        <div class="form-group">
            <label>パスワード</label>
            <input type="password" name="password">
        </div>

        <button type="submit">ログイン</button>
    </form>

    <button class="close-btn" onclick="toggleLogin()">閉じる</button>

    <div class="error">
        ${errorMessage}
    </div>
</div>
</body>
</html>
