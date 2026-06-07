<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="controller.AccountListServlet.User" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>アカウント一覧</title>

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

    .top-menu {
        display: flex;
        justify-content: space-between;
        margin-bottom: 20px;
    }

    .logout-form {
        margin: 0;
    }

    .logout-btn {
        background: #333;
        color: white;
        padding: 8px 16px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }

    .add-btn {
        background: #28a745;
        color: white;
        padding: 8px 16px;
        border-radius: 4px;
        text-decoration: none;
        font-weight: bold;
    }

    table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 20px;
    }

    table th, table td {
        border: 1px solid #ccc;
        padding: 10px;
        text-align: center;
    }

    table th {
        background: #007bff;
        color: white;
    }

    .btn {
        padding: 6px 12px;
        border-radius: 4px;
        text-decoration: none;
        color: white;
        font-size: 14px;
    }

    .edit { background: #28a745; }
    .ban { background: #ff9800; }
    .delete { background: #dc3545; }

</style>
</head>
<body>

<div class="container">

    <div class="top-menu">
        <a href="account-add.jsp" class="add-btn">アカウント追加</a>

        <form action="LogoutServlet" method="post" class="logout-form">
            <button type="submit" class="logout-btn">ログアウト</button>
        </form>
    </div>

    <h1>アカウント一覧</h1>

    <table>
        <tr>
            <th>ユーザー名</th>
            <th>メールアドレス</th>
            <th>作成日</th>
            <th>更新日</th>
            <th>操作</th>
        </tr>

        <%
            List<User> users = (List<User>) request.getAttribute("users");

            if (users != null) {
                for (User u : users) {
        %>
        <tr>
            <td><%= u.name %></td>
            <td><%= u.email %></td>
            <td><%= u.created %></td>
            <td><%= u.updated %></td>
            <td>
                <a class="btn edit">編集</a>
                <a class="btn ban">アクセス禁止</a>
                <a class="btn delete">削除</a>
            </td>
        </tr>
        <%
                }
            }
        %>

    </table>
</div>

</body>
</html>
