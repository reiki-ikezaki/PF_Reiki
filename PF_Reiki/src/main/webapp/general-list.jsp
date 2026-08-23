<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.UserData" %>
<%@ page import="util.HtmlUtil" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>一般ユーザー一覧</title>

<style>
    body { font-family: Arial; background: #f5f5f5; margin: 0; padding: 0; }
    .container {
        width: 80%; margin: 40px auto; background: white;
        padding: 30px; border-radius: 10px;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }
    h2 { text-align: center; margin-bottom: 20px; }
    table { width: 100%; border-collapse: collapse; }
    th, td { padding: 10px; border-bottom: 1px solid #ddd; }
    th { background: #007bff; color: white; }
    .back-link { display: inline-block; margin-bottom: 15px; color: #007bff; text-decoration: none; }
    .detail-link { color: #3498db; text-decoration: none; }
</style>
</head>
<body>

<div class="container">
    <a class="back-link" href="adminDashboard">← 管理者ダッシュボードに戻る</a>
    <h2>一般ユーザー一覧</h2>

    <table>
        <tr>
            <th>ID</th>
            <th>画像</th>
            <th>名前</th>
            <th>ふりがな</th>
            <th>性別</th>
            <th>年齢</th>
            <th>紹介文</th>
            <th>操作</th>
        </tr>

        <%
            List<UserData> list = (List<UserData>) request.getAttribute("generalList");
            if (list != null) {
                for (UserData u : list) {
        %>
        <tr>
            <td><%= u.getId() %></td>
            <td>
                <img src="profileImage?id=<%= u.getId() %>"
                     style="width:60px; height:60px; object-fit:cover; border-radius:8px; border:1px solid #ccc;"
                     onerror="this.style.display='none';">
            </td>
            <td><%= u.getName() %></td>
            <td><%= u.getFurigana() %></td>
            <td><%= HtmlUtil.genderLabel(u.getGender()) %></td>
            <td><%= u.getAge() %></td>
            <td><%= u.getBio() %></td>
            <td><a class="detail-link" href="accountDetail?id=<%= u.getId() %>">詳細</a></td>
        </tr>
        <%
                }
            }
        %>
    </table>
</div>

</body>
</html>
