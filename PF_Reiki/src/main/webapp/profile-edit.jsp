<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.UserData" %>
<%@ page import="util.HtmlUtil" %>
<%
    // 管理者にはプロフィールが無いので、「設定」画面へ移す
    UserData guardUser = (UserData) session.getAttribute("user");
    if (guardUser != null && "admin".equals(guardUser.getRole())) {
        response.sendRedirect("settings");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>プロフィール編集</title>

<style>
    * {
        box-sizing: border-box;
    }

    body {
        font-family: Arial, sans-serif;
        background: #f5f5f5;
        margin: 0;
        padding: 0;
    }

    .logout-btn {
        display: flex;
        justify-content: flex-end;
        padding: 10px 16px 0;
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
        width: 90%;
        max-width: 500px;
        margin: 24px auto 40px;
        background: white;
        padding: 30px;
        border-radius: 10px;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }

    @media (max-width: 600px) {
        .container {
            width: 92%;
            padding: 20px;
        }
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

    input, select, textarea {
        width: 100%;
        padding: 10px;
        margin-top: 5px;
        border: 1px solid #ccc;
        border-radius: 6px;
        font-size: 16px;
        font-family: inherit;
        box-sizing: border-box;
    }

    textarea {
        resize: none;
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

    .back-link {
        display: inline-block;
        background: #007bff;
        color: white;
        padding: 8px 16px;
        border-radius: 6px;
        text-decoration: none;
        font-weight: bold;
    }

    .back-link:hover {
        background: #0056b3;
    }
</style>

</head>
<body>

<form action="logout" method="post" class="logout-btn">
    <button type="submit">ログアウト</button>
</form>


<%
    // ログイン中のユーザー（一般ユーザーのプロフィール）
    UserData me = (UserData) session.getAttribute("user");

    // エラーで戻ってきたときは、送信された値を表示する（入力内容を消さない）
    String fGender = HtmlUtil.paramOr(request, "gender", me.getGender());
    if (fGender.isEmpty()) { fGender = "male"; }
%>

<div class="container">
    <h1>プロフィール編集</h1>

    <form action="profileEdit" method="post" enctype="multipart/form-data">

        <label>プロフィール画像（jpg/png/gif・2MB以下）</label><br>
        <img src="profileImage?id=<%= me.getId() %>"
             style="width:100px; height:100px; object-fit:cover; border-radius:8px; border:1px solid #ccc; margin:6px 0;"
             onerror="this.style.display='none';">
        <br>
        <input type="file" name="profileImage" accept="image/*">

        <label>名前</label>
        <input type="text" name="name" value="<%= HtmlUtil.paramOr(request, "name", me.getName()) %>">

        <label>フリガナ（ひらがなのみ）</label>
        <input type="text" name="furigana" value="<%= HtmlUtil.paramOr(request, "furigana", me.getFurigana()) %>">

        <label>性別</label>
        <select name="gender">
            <option value="male"   <%= "male".equals(fGender)   ? "selected" : "" %>>男性</option>
            <option value="female" <%= "female".equals(fGender) ? "selected" : "" %>>女性</option>
            <option value="other"  <%= "other".equals(fGender)  ? "selected" : "" %>>その他</option>
        </select>

        <label>年齢</label>
        <input type="number" name="age" value="<%= HtmlUtil.paramOr(request, "age", String.valueOf(me.getAge())) %>" min="0" max="999">

        <label>自己紹介</label>
        <textarea name="bio" rows="4"><%= HtmlUtil.paramOr(request, "bio", me.getBio()) %></textarea>

        <button type="submit" class="submit-btn">更新</button>

        <div class="msg error">${error}</div>
        <div class="msg success">${success}</div>
    </form>

    <div style="text-align:center; margin-top:15px;">
        <a href="userDashboard" class="back-link">← ダッシュボードに戻る</a>
    </div>
</div>

<!-- ファイル選択時に即エラー表示するスクリプト -->
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
