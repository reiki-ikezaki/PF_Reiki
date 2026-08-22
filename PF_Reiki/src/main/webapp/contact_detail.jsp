<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="model.InquiryData" %>

<%
    InquiryData d = (InquiryData) request.getAttribute("detail");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>お問い合わせ詳細</title>

<style>
    body { font-family: Arial; background: #f5f5f5; padding: 20px; }
    .box {
        background: white;
        padding: 20px;
        border-radius: 8px;
        width: 600px;
        margin: auto;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }
    .back-btn {
        background: #3498db;
        color: white;
        padding: 8px 14px;
        border-radius: 4px;
        text-decoration: none;
        display: inline-block;
        margin-top: 20px;
    }
</style>

</head>
<body>

<div class="box">
    <h2>お問い合わせ詳細</h2>

    <p><b>ID:</b> <%= d.getId() %></p>
    <p><b>カテゴリ:</b> <%= d.getCategoryName() %></p>
    <p><b>メールアドレス:</b> <%= d.getEmail() %></p>
    <p><b>内容:</b><br><%= d.getContent() %></p>
    <p><b>ステータス:</b> <%= d.getStatus() %></p>
    
    <form action="status_update" method="post" style="margin-top:20px;">
    <input type="hidden" name="id" value="<%= d.getId() %>">

    <label><b>ステータス変更：</b></label>
    <select name="status">
        <option value="未対応" <%= d.getStatus().equals("未対応") ? "selected" : "" %>>未対応</option>
        <option value="対応中" <%= d.getStatus().equals("対応中") ? "selected" : "" %>>対応中</option>
        <option value="対応済み" <%= d.getStatus().equals("対応済み") ? "selected" : "" %>>対応済み</option>
    </select>

    <button type="submit" style="margin-left:10px;">変更する</button>
</form>
    
    <p><b>送信日:</b> <%= d.getCreatedAt() %></p>
    <p><b>更新日:</b> <%= d.getUpdatedAt() %></p>
    
    

    <a class="back-btn" href="contact_list">戻る</a>
</div>

</body>
</html>
