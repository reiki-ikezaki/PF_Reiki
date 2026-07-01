<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.UserData" %>

<%
    UserData user = (UserData) request.getAttribute("user");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>アカウント編集</title>

<style>
    body {
        font-family: Arial, sans-serif;
        background: #f5f5f5;
        margin: 0;
        padding: 0;
    }

    .container {
        width: 90%;
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

    .form-group {
        margin-bottom: 15px;
    }

    label {
        font-weight: bold;
    }

    input[type="text"], input[type="password"] {
        width: 100%;
        padding: 8px;
        border: 1px solid #ccc;
        border-radius: 4px;
        margin-top: 5px;
    }

    .btn {
        background: #007bff;
        color: white;
        padding: 10px 20px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 16px;
    }

    .btn:hover {
        background: #0056b3;
    }
</style>

</head>
<body>

<div class="container">

    <h1>アカウント編集</h1>

    <form action="updateUser" method="post">

        <input type="hidden" name="id" value="<%= user.getId() %>">

        <div class="form-group">
            <label>ユーザー名（変更不可）</label>
            <div><%= user.getUsername() %></div>
        </div>

        <div class="form-group">
            <label>メールアドレス</label>
            <input type="text" name="email" value="<%= user.getEmail() %>">
        </div>

        <div class="form-group">
            <label>パスワード</label>
            <input type="text" name="password" value="<%= user.getPassword() %>">
        </div>

        <div class="form-group">
            <label>名前</label>
            <input type="text" name="name" value="<%= user.getName() %>">
        </div>

        <button type="submit" class="btn">更新する</button>

    </form>

</div>

</body>
</html>
