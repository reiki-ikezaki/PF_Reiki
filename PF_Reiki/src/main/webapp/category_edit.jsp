<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="model.Category" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>カテゴリ編集</title>

<style>
    body { font-family: Arial; background: #f5f5f5; margin: 0; }
    .main { padding: 20px; }
    .form-box {
        background: white; padding: 20px; border-radius: 8px;
        width: 400px; margin: auto;
    }
    .btn { padding: 8px 14px; background: #3498db; color: white;
           border-radius: 4px; text-decoration: none; }
</style>

</head>
<body>

<div class="main">

    <div class="form-box">
        <h2>カテゴリ編集</h2>

        <% String error = (String) request.getAttribute("error"); %>
        <% if (error != null) { %>
            <p style="color:red;"><%= error %></p>
        <% } %>

        <% Category c = (Category) request.getAttribute("category"); %>

        <form action="categoryEdit" method="post">
            <input type="hidden" name="id" value="<%= c.getId() %>">

            <label>カテゴリ名：</label><br>
            <input type="text" name="name" value="<%= c.getName() %>"
                   style="width:100%; padding:8px;"><br><br>

            <button class="btn" type="submit">更新</button>
            <a class="btn" href="categoryList">戻る</a>
        </form>
    </div>

</div>

</body>
</html>
