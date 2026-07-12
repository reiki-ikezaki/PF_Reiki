<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>アカウント編集</title>

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

<h1>アカウント編集</h1>

<form action="EditUserServlet" method="post" enctype="multipart/form-data">

    <!-- ▼ ID（編集時は変更不可） -->
    <input type="hidden" name="id" value="${user.id}">

    <!-- ▼ ユーザー種別 -->
    <div class="vertical">
        <label>ユーザー種別</label>
        <div class="radio-group">
            <label>
                <input type="radio" name="role" value="user"
                    ${user.role == 'user' ? 'checked' : ''} onclick="toggleRole()">
                一般ユーザー
            </label>
            <label>
                <input type="radio" name="role" value="admin"
                    ${user.role == 'admin' ? 'checked' : ''} onclick="toggleRole()">
                管理者
            </label>
        </div>
    </div>

    <!-- ▼ ステータス -->
    <div class="vertical">
        <label>ステータス</label>
        <div class="radio-group">
            <label><input type="radio" name="status" value="active"
                ${user.status == 'active' ? 'checked' : ''}> 有効</label>
            <label><input type="radio" name="status" value="banned"
                ${user.status == 'banned' ? 'checked' : ''}> アクセス禁止</label>
        </div>
    </div>

    <!-- ▼ 一般ユーザー項目 -->
    <div id="userFields" class="vertical" style="${user.role == 'user' ? '' : 'display:none;'}">

        <label>ユーザー名</label>
        <input type="text" name="username" value="${user.username}">

        <label>メールアドレス</label>
        <input type="email" name="email" value="${user.email}">

        <label>ふりがな</label>
        <input type="text" name="furigana" value="${user.furigana}">

        <label>性別</label>
        <select name="gender">
            <option value="male"   ${user.gender == 'male' ? 'selected' : ''}>男性</option>
            <option value="female" ${user.gender == 'female' ? 'selected' : ''}>女性</option>
            <option value="other"  ${user.gender == 'other' ? 'selected' : ''}>その他</option>
        </select>

        <label>年齢</label>
        <input type="number" name="age" value="${user.age}">

        <label>自己紹介</label>
        <textarea name="bio" rows="4">${user.bio}</textarea>

        <label>プロフィール画像（変更する場合のみ選択）</label>
        <input type="file" name="profileImage">

    </div>

    <!-- ▼ 管理者項目 -->
    <div id="adminFields" class="vertical" style="${user.role == 'admin' ? '' : 'display:none;'}">

        <label>ユーザー名</label>
        <input type="text" name="username" value="${user.username}">

        <label>メールアドレス</label>
        <input type="email" name="email" value="${user.email}">

    </div>

    <button type="submit">更新する</button>

</form>

<script>
    // 初期表示時に role に応じて切り替え
    toggleRole();
</script>

</body>
</html>
