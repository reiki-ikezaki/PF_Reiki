<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>お問い合わせ</title>
</head>
<body>

<h2>お問い合わせ</h2>

<form action="contact" method="post">

    <label>カテゴリ:</label>
    <select name="category_id">
        <option value="1">意見</option>
        <option value="2">質問</option>
        <option value="3">その他</option>
    </select>
    <br><br>

    <label>メッセージ:</label><br>
    <textarea name="content" rows="5" cols="40"></textarea>
    <br><br>

    <label>メールアドレス:</label><br>
    <input type="email" name="email">
    <br><br>

    <button type="submit">送信</button>
</form>

<br>
<a href="login.jsp">ログイン画面へ</a>

</body>
</html>
