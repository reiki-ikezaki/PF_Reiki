<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>アカウント追加</title>

<style>
    * {
        box-sizing: border-box;
    }

    body {
        font-family: Arial, sans-serif;
        background: #f5f5f5;
        margin: 0;
        padding: 40px 0;
    }

    .container {
        width: 600px;
        max-width: 90%;
        margin: 0 auto;
        background: white;
        padding: 30px;
        border-radius: 10px;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }

    h1 {
        text-align: center;
        margin-top: 0;
        margin-bottom: 20px;
    }

    .vertical {
        margin-bottom: 20px;
    }

    .radio-group {
        display: flex;
        flex-wrap: wrap;
        gap: 20px;
        align-items: center;
        margin-top: 6px;
    }

    .vertical label {
        display: block;
        font-weight: bold;
        margin-top: 15px;
        margin-bottom: 5px;
    }

    .vertical label:first-child {
        margin-top: 0;
    }

    .vertical input, .vertical select, .vertical textarea {
        width: 100%;
        padding: 8px;
        border: 1px solid #ccc;
        border-radius: 5px;
        font-family: inherit;
        font-size: 16px;
    }

    .vertical textarea {
        resize: none;
    }

    .btn-area button {
        padding: 10px 20px;
        background: #007bff;
        color: white;
        border: none;
        border-radius: 6px;
        font-weight: bold;
        cursor: pointer;
    }

    .btn-area button:hover { background: #0056b3; }

    .back-link {
        align-self: center;
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

    @media (max-width: 600px) {
        body { padding: 16px 0; }
        .container { padding: 20px; }
        .btn-area { flex-wrap: wrap; }
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

<div class="container">

<h1>アカウント追加</h1>

<div class="msg" style="color:red;">${error}</div>

<form action="/PF_Reiki/CreateUserServlet" method="post" enctype="multipart/form-data">

    <!-- ▼ ユーザー種別 -->
    <div class="vertical">
        <label>ユーザー種別</label>
        <div class="radio-group">
            <label>
                <input type="radio" name="role" value="user" checked onclick="toggleRole()">
                一般ユーザー
            </label>
            <label>
                <input type="radio" name="role" value="admin" onclick="toggleRole()">
                管理者
            </label>
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
        <input type="email" name="email" required>

        <label>名前</label>
        <input type="text" name="name" required>

        <label>パスワード（半角英数字と _ - で8〜32文字）</label>
        <input type="password" name="password" autocomplete="new-password" required>

        <label>プロフィール画像（jpg / jpeg / png / gif・2MB以下）</label>
        <input type="file" name="profileImage" id="profileImageInput" accept="image/*">
        <div id="fileError" style="color:red; margin-top:6px;"></div>

    </div>

    <!-- ▼ 一般ユーザー項目 -->
    <div id="userFields" class="vertical">

        <label>ふりがな（ひらがなのみ）</label>
        <input type="text" name="furigana">

        <label>性別</label>
        <select name="gender">
            <option value="male">男性</option>
            <option value="female">女性</option>
            <option value="other">その他</option>
        </select>

        <label>年齢</label>
        <input type="number" name="age" min="0" max="999">

        <label>自己紹介</label>
        <textarea name="bio" rows="4"></textarea>

    </div>

    <!-- ▼ 管理者項目 -->
    <div id="adminFields" class="vertical" style="display:none;">
    </div>

    <div class="btn-area" style="margin-top:20px; display:flex; gap:20px; align-items:center;">
        <button type="submit">登録する</button>
        <a href="accountList" class="back-link">アカウント一覧に戻る</a>
    </div>

</form>

</div>

<script>
    // 初期表示時に role に応じて切り替え
    toggleRole();

    // プロフィール画像の即時チェック（拡張子・2MB）
    document.getElementById("profileImageInput").addEventListener("change", function (e) {
        var file = e.target.files[0];
        var errorBox = document.getElementById("fileError");

        if (!file) { errorBox.textContent = ""; return; }

        var validExt = ["jpg", "jpeg", "png", "gif"];
        var ext = file.name.split(".").pop().toLowerCase();

        if (validExt.indexOf(ext) === -1) {
            errorBox.textContent = "正しい画像ファイル（jpg / jpeg / png / gif）を選択してください。";
            e.target.value = "";
            return;
        }

        if (file.size > 1024 * 1024 * 2) {
            errorBox.textContent = "プロフィール画像は2MB以下にしてください。";
            e.target.value = "";
            return;
        }

        errorBox.textContent = "";
    });
</script>

</body>
</html>
