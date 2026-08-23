<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Category" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>カテゴリ一覧</title>

<style>
    body {
        margin: 0;
        font-family: Arial, sans-serif;
        background: #f5f5f5;
    }

    .header {
        background: #2c3e50;
        color: white;
        padding: 12px 20px;
        display: flex;
        align-items: center;
    }

    .back-btn {
        background: #3498db;
        color: white;
        padding: 8px 14px;
        border-radius: 4px;
        text-decoration: none;
        margin-right: 20px;
    }

    .logout {
        margin-left: auto;
        background: #e67e22;
        padding: 8px 14px;
        border-radius: 4px;
        color: white;
        text-decoration: none;
    }

    .main {
        padding: 20px;
    }

    table {
        width: 100%;
        border-collapse: collapse;
        background: white;
        border-radius: 8px;
        overflow: hidden;
    }

    th, td {
        padding: 12px;
        border-bottom: 1px solid #ddd;
        text-align: left;
    }

    th {
        background: #ecf0f1;
    }

    .btn {
        padding: 6px 12px;
        background: #3498db;
        color: white;
        border-radius: 4px;
        text-decoration: none;
    }

    .btn-delete {
        background: #e74c3c;
    }
</style>

</head>
<body>

<div class="header">
    <a class="back-btn" href="adminDashboard">戻る</a>
    <span>カテゴリ一覧</span>
    <form action="logout" method="post" class="logout-btn">
    <button type="submit">ログアウト</button>
</form>

</div>


<div class="main">

    <a class="btn" href="categoryAdd">カテゴリ追加</a>
    <br><br>

    <table>
        <tr>
            <th>ID</th>
            <th>カテゴリ名</th>
            <th>操作</th>
        </tr>

        <%
            List<Category> list = (List<Category>) request.getAttribute("categoryList");
            if (list != null) {
                for (Category cat : list) {
        %>
        <tr>
            <td><%= cat.getId() %></td>
            <td><%= cat.getName() %></td>
            <td>
                <a class="btn" href="categoryEdit?id=<%= cat.getId() %>">編集</a>
                <a class="btn btn-delete" href="categoryDelete?id=<%= cat.getId() %>"
                   onclick="return confirm('このカテゴリを削除します。よろしいですか？');">削除</a>
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
