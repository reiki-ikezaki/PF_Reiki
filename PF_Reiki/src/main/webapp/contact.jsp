<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Category" %>
<%@ page import="util.HtmlUtil" %>
<%
    List<Category> categories = (List<Category>) request.getAttribute("categories");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>お問い合わせ</title>

<style>
    * {
        box-sizing: border-box;
    }

    body {
        font-family: Arial, sans-serif;
        background: #f5f5f5;
        margin: 0;
        min-height: 100vh;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        padding: 24px 16px;
    }

    .box {
        background: white;
        padding: 24px;
        width: 400px;
        max-width: 100%;
        border-radius: 10px;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }

    h2 {
        text-align: center;
        margin-top: 0;
    }

    .form-group {
        margin-bottom: 15px;
    }

    label {
        display: block;
        font-weight: bold;
        margin-bottom: 5px;
    }

    select, textarea, input[type="email"] {
        width: 100%;
        padding: 8px;
        border: 1px solid #ccc;
        border-radius: 5px;
        font-family: inherit;
        font-size: 16px;
    }

    textarea {
        resize: none;
    }

    button {
        width: 100%;
        padding: 10px;
        background: #007bff;
        color: white;
        border: none;
        border-radius: 5px;
        font-weight: bold;
        cursor: pointer;
    }

    button:hover {
        background: #0056b3;
    }

    .btn {
        display: block;
        text-align: center;
        margin-top: 15px;
        background: #aaa;
        color: white;
        padding: 8px 16px;
        border-radius: 5px;
        text-decoration: none;
        font-weight: bold;
    }

    .btn:hover { background: #888; }
</style>
</head>
<body>

<div class="box">
    <h2>お問い合わせ</h2>

    <form action="contact" method="post">

        <div class="form-group">
            <label>カテゴリ</label>
            <select name="category_id" required>
                <%
                    if (categories != null) {
                        for (Category cat : categories) {
                %>
                <option value="<%= cat.getId() %>"><%= HtmlUtil.escape(cat.getName()) %></option>
                <%
                        }
                    }
                %>
            </select>
        </div>

        <div class="form-group">
            <label>メッセージ</label>
            <textarea name="content" rows="5"></textarea>
        </div>

        <div class="form-group">
            <label>メールアドレス</label>
            <input type="email" name="email">
        </div>

        <button type="submit">送信</button>
    </form>

    <a href="login.jsp" class="btn">ログイン画面へ</a>
</div>

</body>
</html>
