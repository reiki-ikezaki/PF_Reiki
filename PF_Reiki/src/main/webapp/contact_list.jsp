<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.InquiryData" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>お問い合わせ一覧</title>

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

    h1 {
        margin-bottom: 20px;
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
    <a class="back-btn" href="admin-dashboard.jsp">戻る</a>
    <span>お問い合わせ一覧</span>
    <a class="logout" href="logout">ログアウト</a>
</div>

<div class="main">

    <table>
        <tr>
            <th>カテゴリ</th>
            <th>メールアドレス</th>
            <th>お問い合わせ内容</th>
            <th>ステータス</th>
            <th>送信日</th>
            <th>更新日</th>
            <th>操作</th>
        </tr>

        <%
            List<InquiryData> list = (List<InquiryData>) request.getAttribute("inquiryList");
            if (list != null) {
                for (InquiryData ing : list) {
        %>
        <tr>
            <td><%= ing.getCategoryName() %></td>
            <td><%= ing.getEmail() %></td>
            <td><%= ing.getContent() %></td>
            <td><%= ing.getStatus() %></td>
            <td><%= ing.getCreatedAt() %></td>
            <td><%= ing.getUpdatedAt() %></td>
            <td>
                <a class="btn" href="contact_detail?id=<%= ing.getId() %>">詳細</a>
                <a class="btn btn-delete" href="contact_delete?id=<%= ing.getId() %>">削除</a>
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
