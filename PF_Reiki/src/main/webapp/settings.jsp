<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.UserData" %>
<%@ page import="java.util.List" %>
<%@ page import="util.HtmlUtil" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>設定</title>

<style>
    * {
        box-sizing: border-box;
    }

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
        max-width: 500px;
        margin: 24px auto 40px;
        background: white;
        padding: 30px;
        border-radius: 10px;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }

    @media (max-width: 600px) {
        .container {
            width: 92%;
            padding: 20px;
        }
    }

    h1 {
        text-align: center;
        margin-bottom: 20px;
    }

    label {
        display: block;
        margin-top: 15px;
        font-weight: bold;
    }

    input, select, textarea {
        width: 100%;
        padding: 10px;
        margin-top: 5px;
        border: 1px solid #ccc;
        border-radius: 6px;
        font-size: 16px;
        font-family: inherit;
        box-sizing: border-box;
    }

    textarea {
        resize: none;
    }

    .submit-btn {
        margin-top: 25px;
        width: 100%;
        padding: 12px;
        background: #007bff;
        color: white;
        border: none;
        border-radius: 6px;
        cursor: pointer;
        font-size: 16px;
    }

    .submit-btn:hover {
        background: #0056b3;
    }

    .msg {
        margin-top: 15px;
        text-align: center;
        font-size: 14px;
    }

    .error {
        color: red;
    }

    .success {
        color: green;
    }

    .back-link {
        display: inline-block;
        background: #007bff;
        color: white;
        padding: 8px 16px;
        border-radius: 6px;
        text-decoration: none;
        font-weight: bold;
    }

    .back-link:hover {
        background: #0056b3;
    }
</style>

</head>
<body>

<form action="logout" method="post" class="logout-btn">
    <button type="submit">ログアウト</button>
</form>


<%
    // ログイン中のユーザー（管理者か一般かで、名前の欄を出し分ける）
    UserData me = (UserData) session.getAttribute("user");
    boolean isAdmin = me != null && "admin".equals(me.getRole());
%>

<div class="container">
    <h1>設定</h1>

    <form action="settings" method="post">

        <div class="msg error">${error}<%
    List<String> errs = (List<String>) request.getAttribute("errors");
    if (errs != null) { for (String m : errs) {
%><div><%= HtmlUtil.escape(m) %></div><% } } %></div>
        <div class="msg success">${success}</div>


        <label>メールアドレス</label>
        <input type="text" name="email" value="<%= HtmlUtil.paramOr(request, "email", me.getEmail()) %>">

        <label>パスワード（変更する場合のみ入力・半角英数字（英字・数字を両方含む）と _ - で8〜32文字）</label>
        <input type="password" name="password" placeholder="変更する場合のみ入力" autocomplete="new-password">

        <% if (isAdmin) { %>
        <!-- 名前の変更は、管理者の設定画面だけ（一般ユーザーは「プロフィール」画面で変更する） -->
        <label>名前</label>
        <input type="text" name="name" value="<%= HtmlUtil.paramOr(request, "name", me.getName()) %>">
        <% } %>

        <button type="submit" class="submit-btn">更新</button>

    </form>

    <div style="text-align:center; margin-top:15px;">
        <a href="<%= isAdmin ? "adminDashboard" : "userDashboard" %>" class="back-link">← ダッシュボードに戻る</a>
    </div>
</div>

</body>
</html>
