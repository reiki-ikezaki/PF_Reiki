<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>カテゴリ追加</title>

<style>
    * { box-sizing: border-box; }
    body { font-family: Arial; background: #f5f5f5; margin: 0; }
    .main { padding: 20px; }
    .form-box {
        background: white; padding: 20px; border-radius: 8px;
        width: 400px; max-width: 100%; margin: auto;
    }
    .btn { display: inline-block; padding: 10px 16px; background: #3498db; color: white;
           border-radius: 4px; text-decoration: none; border: none; cursor: pointer; font-size: 15px; }
    input[type="text"], input:not([type]) { font-size: 16px; border: 1px solid #ccc; border-radius: 4px; }
    button.btn { font-family: inherit; }
</style>

</head>
<body>

<div class="main">

    <div class="form-box">
        <h2>カテゴリ追加</h2>

        <% String error = (String) request.getAttribute("error"); %>
        <% if (error != null) { %>
            <p style="color:red;"><%= error %></p>
        <% } %>

        <form action="categoryAdd" method="post">
            <label>カテゴリ名：</label><br>
            <input type="text" name="name" style="width:100%; padding:8px;"><br><br>

            <button class="btn" type="submit">登録</button>
            <a class="btn" href="categoryList">戻る</a>
        </form>
    </div>

</div>

</body>
</html>
