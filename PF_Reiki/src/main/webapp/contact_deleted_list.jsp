<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.InquiryData" %>
<%@ page import="util.HtmlUtil" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>削除済みお問い合わせ一覧</title>

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
        flex-wrap: wrap;
        justify-content: space-between;
        align-items: center;
        gap: 10px;
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

    .btn {
        display: inline-block;
        background: #007bff;
        color: white;
        padding: 8px 16px;
        border-radius: 6px;
        text-decoration: none;
        font-weight: bold;
    }

    .btn:hover { background: #0056b3; }

    .table-scroll {
        overflow-x: auto;
        margin-top: 20px;
    }

    table {
        width: 100%;
        min-width: 700px;
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
        background: #007bff;
        color: white;
        font-weight: bold;
    }

    tr:nth-child(even) {
        background: #f2f2f2;
    }

    .actions {
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
        gap: 8px;
    }

    .action-btn {
        display: inline-block;
        min-width: 60px;
        padding: 6px 12px;
        border-radius: 4px;
        text-decoration: none;
        color: white;
        font-size: 14px;
        text-align: center;
        white-space: nowrap;
    }

    .delete { background: #dc3545; }
    .restore { background: #28a745; }

</style>

</head>
<body>

<div class="container">

    <div class="top-menu">
        <a href="contact_list" class="btn">← お問い合わせ一覧に戻る</a>

        <form action="logout" method="post" class="logout-form">
            <button type="submit" class="logout-btn">ログアウト</button>
        </form>
    </div>

    <h1>削除済みお問い合わせ一覧</h1>

    <div class="table-scroll">
    <table>
        <tr>
            <th>カテゴリ</th>
            <th>メールアドレス</th>
            <th>お問い合わせ内容</th>
            <th>削除日</th>
            <th>操作</th>
        </tr>

        <%
            List<InquiryData> list = (List<InquiryData>) request.getAttribute("deletedList");
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
            <td><%= ing.getDeletedAt() %></td>
            <td>
                <div class="actions">
                    <a class="action-btn delete" href="contact_destroy?id=<%= ing.getId() %>"
                       onclick="return confirm('このお問い合わせを完全に削除します。この操作は取り消せません。よろしいですか？');">完全削除</a>
                    <a class="action-btn restore" href="contact_restore?id=<%= ing.getId() %>">復活</a>
                </div>
            </td>
        </tr>
        <%
                }
            }
        %>
    </table>
    </div>

</div>

</body>
</html>
