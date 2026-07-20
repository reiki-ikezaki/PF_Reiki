<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.UserData" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>削除済みアカウント一覧</title>

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

    table {
        width: 100%;
        border-collapse: collapse;
        table-layout: fixed;
        margin-top: 20px;
    }

    table th, table td {
        border: 1px solid #999;
        padding: 12px;
        text-align: center;
        word-wrap: break-word;
    }

    table th {
        background: #6c757d;
        color: white;
        font-weight: bold;
    }

    tr:nth-child(even) {
        background: #f2f2f2;
    }

    .btn {
        padding: 6px 12px;
        border-radius: 4px;
        text-decoration: none;
        color: white;
        font-size: 14px;
        white-space: nowrap;
    }

    .delete { background: #dc3545; }
    .restore { background: #28a745; }

    .back-link {
        margin-top: 20px;
        display: inline-block;
        color: #007bff;
        text-decoration: none;
    }

    .back-link:hover {
        text-decoration: underline;
    }
</style>
</head>
<body>

<div class="container">

    <h1>削除済みアカウント一覧</h1>

    <table>
        <tr>
            <th>名前</th>
            <th>メールアドレス</th>
            <th>ステータス</th>
            <th>完全削除</th>
            <th>復活</th>
        </tr>

        <%
            List<UserData> deletedUsers = (List<UserData>) request.getAttribute("deletedUsers");

            if (deletedUsers != null) {
                for (UserData u : deletedUsers) {
        %>
        <tr>
            <td><%= u.getName() %></td>
            <td><%= u.getEmail() %></td>
            <td><%= u.getStatus() %></td>

            <td>
                <a href="deleteUserPermanent?id=<%= u.getId() %>" class="btn delete">完全削除</a>
            </td>

            <td>
                <a href="restoreUser?id=<%= u.getId() %>" class="btn restore">復活</a>
            </td>
        </tr>
        <%
                }
            }
        %>

    </table>

    <a href="accountList" class="back-link">アカウント一覧に戻る</a>

</div>

</body>
