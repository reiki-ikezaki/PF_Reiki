<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>プロフィール編集</title>

<style>
    body {
        font-family: Arial, sans-serif;
        background: #f5f5f5;
        margin: 0;
        padding: 0;
    }

    .logout-btn {
        position: absolute;
        top: 10px;
        right: 10px;
    }

    .logout-btn button {
        padding: 5px 10px;
        font-size: 12px;
        background: #aaa;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }

    .container {
        width: 80%;
        max-width: 500px;
        margin: 60px auto;
        background: white;
        padding: 30px;
        border-radius: 10px;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }

    h1 {
        text-align: center;
        margin-bottom: 20px;
    }

    label {
        display: block;
        margin-top: 15px;
        font-weight: bold;
    }

    input {
        width: 100%;
        padding: 10px;
        margin-top: 5px;
        border: 1px solid #ccc;
        border-radius: 6px;
        font-size: 16px;
    }

    .submit-btn {
        margin-top: 25px;
        width: 100%;
        padding: 12px;
        background: #007bff;
        color: white;
        border: none;
        border-radius: 6px;
        cursor: pointer;
        font-size: 16px;
    }

    .submit-btn:hover {
        background: #0056b3;
    }

    .msg {
        margin-top: 15px;
        text-align: center;
        font-size: 14px;
    }

    .error {
        color: red;
    }

    .success {
        color: green;
    }
</style>

</head>
<body>

<form action="logout" method="post" class="logout-btn">
    <button type="submit">ログアウト</button>
</form>


<div class="container">
    <h1>プロフィール編集</h1>

    <form action="profileEdit" method="post" enctype="multipart/form-data">

        <label>メールアドレス</label>
        <input type="text" name="email" value="${user.email}">

        <label>パスワード</label>
        <input type="password" name="password" placeholder="変更する場合のみ入力">

        <label>名前</label>
        <input type="text" name="name" value="${user.name}">

        <label>プロフィール画像（jpg/png/gif・2MB以下）</label>
        <input type="file" name="profileImage" accept="image/*">

        <button type="submit" class="submit-btn">更新</button>

        <div class="msg error">${error}</div>
        <div class="msg success">${success}</div>
    </form>
</div>

<!-- ★ ファイル選択時に即エラー表示するスクリプト -->
<script>
document.querySelector("input[name='profileImage']").addEventListener("change", function(e) {
    const file = e.target.files[0];
    if (!file) return;

    const errorBox = document.querySelector(".msg.error");

    // ▼ 拡張子チェック
    const validExt = ["jpg", "jpeg", "png", "gif"];
    const ext = file.name.split(".").pop().toLowerCase();

    if (!validExt.includes(ext)) {
        errorBox.textContent = "正しい画像ファイル（jpg / jpeg / png / gif）を選択してください。";
        e.target.value = ""; // 選択解除
        return;
    }

    // ▼ 2MBチェック
    if (file.size > 1024 * 1024 * 2) {
        errorBox.textContent = "画像は2MB以下にしてください。";
        e.target.value = ""; // 選択解除
        return;
    }

    // ▼ 問題なし → エラー消す
    errorBox.textContent = "";
});
</script>

</body>
</html>
