<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>アカウント追加</title>

<style>
    .vertical { margin-bottom: 20px; }
    .radio-group { display: flex; gap: 20px; align-items: center; margin-top: 6px; }
    .vertical input, .vertical select, .vertical textarea {
        width: 100%; padding: 8px; margin-bottom: 12px;
    }
    .file-error { color: red; margin-top: 10px; }
    .msg { margin-top: 10px; color: red; }
    .btn-area { margin-top: 20px; display: flex; gap: 20px; }
    .btn-area button { padding: 10px 20px; }
</style>

<script>
function toggleRole() {
    const role = document.querySelector('input[name="role"]:checked').value;
    document.getElementById("adminFields").style.display = (role === "admin") ? "block" : "none";
    document.getElementById("userFields").style.display = (role === "user") ? "block" : "none";
}
</script>

</head>
<body>

<h1>アカウント追加</h1>

<div class="msg">${error}</div>

<form action="/PF_Reiki/CreateUserServlet"
      method="post" enctype="multipart/form-data">

    <!-- ▼ ユーザー種別 -->
    <div class="vertical">
        <label>ユーザー種別</label>
        <div class="radio-group">
            <label><input type="radio" name="role" value="user" checked onclick="toggleRole()"> 一般ユーザー</label>
            <label><input type="radio" name="role" value="admin" onclick="toggleRole()"> 管理者</label>
        </div>
    </div>

    <!-- ▼ ステータス -->
    <div class="vertical">
        <label>ステータス</label>
        <div class="radio-group">
            <label><input type="radio" name="status" value="active" checked> 有効</label>
            <label><input type="radio" name="status" value="banned"> アクセス禁止</label>
        </div>
    </div>

    <!-- ▼ 共通項目（ユーザー種別によらず必須） -->
    <div class="vertical">

        <label>ユーザー名</label>
        <input type="text" name="username" required>

        <label>メールアドレス</label>
        <input type="email" name="email" maxlength="255" required>

        <label>名前</label>
        <input type="text" name="name" maxlength="255" required>

    </div>

    <!-- ▼ 一般ユーザー項目 -->
    <div id="userFields" class="vertical">

        <label>ふりがな（ひらがなのみ）</label>
        <input type="text" name="furigana" maxlength="255">

        <label>性別</label>
        <select name="gender">
            <option value="male">男性</option>
            <option value="female">女性</option>
            <option value="other">その他</option>
        </select>

        <label>年齢</label>
        <input type="number" name="age" min="0" max="999">

        <label>自己紹介</label>
        <textarea name="bio" rows="4" maxlength="1500"></textarea>

        <label>プロフィール画像</label>
        <input type="file" name="profileImage" id="profileImageInput">
        <div class="file-error" id="fileError"></div>

    </div>

    <!-- ▼ 管理者項目 -->
    <div id="adminFields" class="vertical" style="display:none;">
    </div>

    <div class="btn-area">
        <button type="submit">登録</button>
        <a href="accountList" class="back-link">アカウント一覧に戻る</a>
    </div>

</form>

<script>
document.getElementById("profileImageInput").addEventListener("change", function(e) {
    const file = e.target.files[0];
    const errorBox = document.getElementById("fileError");

    if (!file) { errorBox.textContent = ""; return; }

    const validExt = ["jpg", "jpeg", "png", "gif"];
    const ext = file.name.split(".").pop().toLowerCase();

    if (!validExt.includes(ext)) {
        errorBox.textContent = "正しい画像ファイルを選択してください。";
        e.target.value = "";
        return;
    }

    if (file.size > 2 * 1024 * 1024) {
        errorBox.textContent = "画像は2MB以下にしてください。";
        e.target.value = "";
        return;
    }

    errorBox.textContent = "";
});
</script>

</body>
</html>
