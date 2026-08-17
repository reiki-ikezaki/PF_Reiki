<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.UserData" %>

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
</style>
</head>
<body>

<div class="container">
    <h2>一般ユーザー一覧</h2>

    <table>
        <tr>
            <th>ID</th>
            <th>名前</th>
            <th>ふりがな</th>
            <th>性別</th>
            <th>年齢</th>
            <th>紹介文</th>
        </tr>

        <%
            List<UserData> list = (List<UserData>) request.getAttribute("generalList");
            if (list != null) {
                for (UserData u : list) {
        %>
        <tr>
            <td><%= u.getId() %></td>
            <td><%= u.getName() %></td>
            <td><%= u.getFurigana() %></td>
            <td><%= u.getGender() %></td>
            <td><%= u.getAge() %></td>
            <td><%= u.getIntro() %></td>
        </tr>
        <%
                }
            }
        %>
    </table>
</div>

</body>
</html>
