<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.UserData" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>アカウント一覧</title>

<style>
    body {
        font-family: Arial, sans-serif;
        background: #f5f5f5;
        margin: 0;
        padding: 0;
    }

    .container {
        width: 90%;
        margin: 40px auto;
        background: white;
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }

    h1 {
        text-align: center;
        margin-bottom: 20px;
    }

    .top-menu {
        display: flex;
        justify-content: space-between;
        margin-bottom: 20px;
    }

    .logout-form {
        margin: 0;
    }

    .logout-btn {
        background: #333;
        color: white;
        padding: 8px 16px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }

    .add-btn {
        background: #28a745;
        color: white;
        padding: 8px 16px;
        border-radius: 4px;
        text-decoration: none;
        font-weight: bold;
    }


    table {
        width: 100%;
        border-collapse: collapse;  
        table-layout: fixed;         
        margin-top: 20px;
    }

    table th, table td {
        border: 1px solid #999;      
        padding: 12px;
        text-align: center;
        word-wrap: break-word;       
}

    table th {
        background: #007bff;
        color: white;
        font-weight: bold;
    }

    tr:nth-child(even) {
        background: #f2f2f2;         
    }

    .btn {
        padding: 6px 12px;
        border-radius: 4px;
        text-decoration: none;
        color: white;
        font-size: 14px;
        white-space: nowrap; 
        margin-right: 6px;
    }

    .edit { background: #28a745; }
    .ban { background: #ff9800; }
    .delete { background: #dc3545; }
    
     td:last-child {
        width: 280px;
        }
        

</style>
</head>
<body>

<div class="container">

    <div class="top-menu">
        <a href="account-add.jsp" class="add-btn">アカウント追加</a>

        <form action="LogoutServlet" method="post" class="logout-form">
            <button type="submit" class="logout-btn">ログアウト</button>
        </form>
    </div>

    <h1>アカウント一覧</h1>

    <table>
        <tr>
            <th>ユーザー名</th>
            <th>メールアドレス</th>
            <th>名前</th>
            <th>権限</th>
            <th>ステータス</th>
            <th>操作</th>
            
        </tr>

        <%
            List<UserData> users = (List<UserData>) request.getAttribute("users");

            if (users != null) {
                for (UserData u : users) {
        %>
        <tr>
            <td><%= u.getUsername() %></td>
            <td><%= u.getEmail() %></td>
            <td><%= u.getName() %></td>
            <td><%= u.getRole() %></td>
            <td><%= u.getStatus() %></td>
            
            <td>
   
        <a href="editUser?id=<%= u.getId() %>" class="btn edit">編集</a>

        <% if ("active".equals(u.getStatus())) { %>
            <a href="accountStatus?id=<%= u.getId() %>" class="btn ban">アクセス禁止</a>
        <% } else { %>
            <a href="accountStatus?id=<%= u.getId() %>" class="btn edit">アクセス許可</a>
        <% } %>

        <a href="deleteUser?id=<%= u.getId() %>" class="btn delete">削除</a>

            </td>
        </tr>
        <%
                }
            }
        %>

    </table>
    <!-- ▼▼▼ ページネーション ▼▼▼ -->
<div class="pagination" style="text-align:center; margin-top:20px;">

    <% 
        int currentPage = (int) request.getAttribute("page");
        int totalPages = (int) request.getAttribute("totalPages");
    %>

    <!-- 前へ -->
    <% if (currentPage > 1) { %>
        <a href="accountList?page=<%= currentPage - 1 %>">前へ</a>
    <% } else { %>
        <span style="color:#ccc;">前へ</span>
    <% } %>

    <!-- ページ番号 -->
    <% for (int i = 1; i <= totalPages; i++) { %>
        <% if (i == currentPage) { %>
            <strong>[<%= i %>]</strong>
        <% } else { %>
            <a href="accountList?page=<%= i %>">[<%= i %>]</a>
        <% } %>
    <% } %>

    <!-- 次へ -->
    <% if (currentPage < totalPages) { %>
        <a href="accountList?page=<%= currentPage + 1 %>">次へ</a>
    <% } else { %>
        <span style="color:#ccc;">次へ</span>
    <% } %>

</div>
<!-- ▲▲▲ ページネーション ▲▲▲ -->

    
</div>

</body>
</html>
