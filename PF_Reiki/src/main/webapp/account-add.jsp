<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>アカウント追加</title>

<style>
    .vertical {
        margin-bottom: 20px;
    }

    .radio-group {
        display: flex;
        gap: 20px;
        align-items: center;
        margin-top: 6px;
    }

    .vertical input, .vertical select, .vertical textarea {
        width: 100%;
        padding: 8px;
        margin-bottom: 12px;
    }

    .file-error {
        color: red;
        margin-top: 10px;
    }
</style>

<script>
function toggleRole() {
    const role = document.querySelector('input[name="role"]:checked').value;

    if (role === "admin") {
        document.getElementById("adminFields").style.display = "block";
        document.getElementById("userFields").style.display = "none";
    } else {
        document.getElementById("adminFields").style.display = "none";
        document.getElementById("userFields").style.display = "block";
    }
}
</script>

</head>
<body>

<h1>アカウント追加</h1>

<!-- ★★★ ここからフォーム ★★★ -->
<form action="CreateUserServlet" method="post" enctype="multipart/form-data">

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

    <!-- ▼ 一般ユーザー項目 -->
    <div id="userFields" class="vertical">

        <label>ユーザー名</label>
        <input type="text" name="username">

        <label>メールアドレス</label>
        <input type="email" name="email">

        <label>ふりがな</label>
        <input type="text" name="furigana">

        <label>性別</label>
        <select name="gender">
            <option value="male">男性</option>
            <option value="female">女性</option>
            <option value="other">その他</option>
        </select>

        <label>年齢</label>
        <input type="number" name="age">

        <label>自己紹介</label>
        <textarea name="bio" rows="4"></textarea>

        <label>プロフィール画像</label>
        <input type="file" name="profileImage" id="profileImageInput">
        <div class="file-error" id="fileError"></div>

    </div>

    <!-- ▼ 管理者項目 -->
    <div id="adminFields" class="vertical" style="display:none;">

        <label>ユーザー名</label>
        <input type="text" name="username">

        <label>メールアドレス</label>
        <input type="email" name="email">

    </div>

    <button type="submit">登録</button>

</form>
<!-- ★★★ フォームここまで ★★★ -->

<!-- ★★★ 画像チェック（貼るだけで完成） ★★★ -->
<script>
document.getElementById("profileImageInput").addEventListener("change", function(e) {
    const file = e.target.files[0];
    const errorBox = document.getElementById("fileError");

    if (!file) {
        errorBox.textContent = "";
        return;
    }

    // ▼ 許可する拡張子
    const validExt = ["jpg", "jpeg", "png", "gif"];
    const ext = file.name.split(".").pop().toLowerCase();

    // ▼ 拡張子チェック
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
