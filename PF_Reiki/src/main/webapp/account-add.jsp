<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>アカウント追加</title>

<style>
    
    .vertical label {
        display: block;
        margin-bottom: 10px;
        font-weight: bold;
    }
</style>

</head>
<body>

<!-- ▼ ユーザー種別（縦並び） ▼ -->
<div class="vertical">
    <label>
        <input type="radio" name="role" value="user" checked> 一般
    </label>
    <label>
        <input type="radio" name="role" value="admin"> 管理者
    </label>
</div>


<div class="vertical">
    <label>名前</label>
    <input type="text" name="name">
</div>


<div class="vertical">
    <label>メールアドレス</label>
    <input type="email" name="email">
</div>

<div class="vertical">
    <label>パスワード</label>
    <input type="password" name="password">
</div>

</body>

<div class="vertical">
    <label>ステータス</label>
    <label>
        <input type="radio" name="status" value="active" checked> アクセス許可
    </label>
    <label>
        <input type="radio" name="status" value="banned"> アクセス禁止
    </label>
</div>

</html>
