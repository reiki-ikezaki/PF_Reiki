<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.UserData" %>
<%@ page import="util.HtmlUtil" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
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

    .table-scroll {
        overflow-x: auto;
        margin-top: 20px;
    }

    table {
        width: 100%;
        min-width: 600px;
        border-collapse: collapse;
        table-layout: fixed;
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

<div class="container">

    <h1>削除済みアカウント一覧</h1>

    <div class="table-scroll">
    <table>
        <tr>
            <th>名前</th>
            <th>メールアドレス</th>
            <th>ステータス</th>
            <th>削除日</th>
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
            <td><%= HtmlUtil.statusLabel(u.getStatus()) %></td>
            <td><%= HtmlUtil.formatDateTime(u.getDeletedAt()) %></td>

            <td>
                <a href="deleteUserPermanent?id=<%= u.getId() %>" class="btn delete"
                   onclick="return confirm('このアカウントを完全に削除します。この操作は取り消せません。よろしいですか？');">完全削除</a>
            </td>

            <td>
                <a href="restoreAccount?id=<%= u.getId() %>" class="btn restore">復活</a>
            </td>
        </tr>
        <%
                }
            }
        %>

    </table>
    </div>

    <a href="accountList" class="back-link">アカウント一覧に戻る</a>

</div>

</body>
</html>
