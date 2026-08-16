<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="model.LikeRanking" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>いいねランキング</title>

<style>
    body {
        font-family: Arial, sans-serif;
        background: #f5f5f5;
        margin: 0;
        padding: 0;
    }

    .container {
        width: 80%;
        margin: 40px auto;
        background: white;
        padding: 30px;
        border-radius: 10px;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }

    h2 {
        text-align: center;
        margin-bottom: 30px;
    }

    table {
        width: 100%;
        border-collapse: collapse;
    }

    th, td {
        padding: 12px;
        border-bottom: 1px solid #ddd;
        text-align: left;
    }

    th {
        background: #007bff;
        color: white;
    }

    tr:hover {
        background: #f1f1f1;
    }

    .rank {
        font-weight: bold;
        color: #007bff;
    }
</style>
</head>
<body>

<div class="container">
    <h2>❤️ いいねランキング ❤️</h2>

    <table>
        <tr>
            <th>順位</th>
            <th>ユーザー名</th>
            <th>獲得いいね数</th>
        </tr>

        <%
            List<LikeRanking> list = (List<LikeRanking>) request.getAttribute("rankingList");
            int index = 1;

            if (list != null) {
                for (LikeRanking item : list) {
        %>
                    <tr>
                        <td class="rank"><%= index++ %></td>
                        <td><%= item.getUsername() %></td>
                        <td><%= item.getLikeCount() %></td>
                    </tr>
        <%
                }
            }
        %>

    </table>
</div>

</body>
</html>
