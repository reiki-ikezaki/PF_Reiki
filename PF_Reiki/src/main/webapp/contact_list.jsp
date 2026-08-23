<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.InquiryData" %>
<%@ page import="util.HtmlUtil" %>

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
    <span>お問い合わせ一覧</span>
    <form action="logout" method="post" class="logout-btn">
    <button type="submit">ログアウト</button>
</form>

</div>

<div class="main">

    <!-- ▼ 追加したカテゴリ一覧ボタン -->
    <a class="btn" href="categoryList">カテゴリ一覧</a>
    <br><br>

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
        <%
            String content = ing.getContent();
            String preview = (content == null) ? "" :
                (content.length() > 10 ? content.substring(0, 10) + "…" : content);
        %>
        <tr>
            <td><%= HtmlUtil.escape(ing.getCategoryName()) %></td>
            <td><%= HtmlUtil.escape(ing.getEmail()) %></td>
            <td><%= HtmlUtil.escape(preview) %></td>
            <td><%= ing.getStatus() %></td>
            <td><%= ing.getCreatedAt() %></td>
            <td><%= ing.getUpdatedAt() %></td>
            <td>
                <a class="btn" href="contact_detail?id=<%= ing.getId() %>">詳細</a>
                <a class="btn btn-delete" href="contact_destroy?id=<%= ing.getId() %>">削除</a>
                
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
