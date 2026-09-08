<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="model.LikeRanking" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
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

    .table-scroll { overflow-x: auto; }

    table {
        width: 100%;
        min-width: 500px;
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

    .like-btn {
        background: #3498db; color: white; padding: 5px 10px;
        border-radius: 4px; border: none; font-size: 13px; cursor: pointer;
    }
    .like-btn:disabled { background: #aaa; cursor: not-allowed; }

    .name-link {
        color: #3498db;
        text-decoration: none;
    }
    .name-link:hover { text-decoration: underline; }

    .back-link {
        display: inline-block;
        margin-bottom: 15px;
        background: #007bff;
        color: white;
        padding: 8px 16px;
        border-radius: 6px;
        text-decoration: none;
        font-weight: bold;
    }
    .back-link:hover { background: #0056b3; }

</style>

<script>
async function sendLike(btn, targetUserId) {
    btn.disabled = true;
    try {
        const res = await fetch("like", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: "targetUserId=" + encodeURIComponent(targetUserId) + "&scope=year"
        });
        const data = await res.json();

        if (!res.ok || !data.success) {
            btn.textContent = data.message || "失敗";
            btn.disabled = false;
            return;
        }

        const countEl = document.getElementById("likeCount-" + targetUserId);
        if (countEl) countEl.textContent = data.likeCount;

    } catch (e) {
        btn.disabled = false;
    }
}
</script>
</head>
<body>

<div class="container">
    <a class="back-link" href="adminDashboard">← 管理者ダッシュボードに戻る</a>
    <h2>❤️ いいねランキング ❤️</h2>

    <div class="table-scroll">
    <table>
        <tr>
            <th>順位</th>
            <th>ユーザー名</th>
            <th>獲得いいね数</th>
            <th>操作</th>
        </tr>

        <%
            List<LikeRanking> list = (List<LikeRanking>) request.getAttribute("rankingList");
            int index = 1;

            if (list != null) {
                for (LikeRanking item : list) {
        %>
                    <tr>
                        <td class="rank"><%= index++ %></td>
                        <td><a class="name-link" href="accountDetail?id=<%= item.getUserId() %>&from=ranking"><%= item.getName() %></a></td>
                        <td id="likeCount-<%= item.getUserId() %>"><%= item.getLikeCount() %></td>
                        <td>
                            <button type="button" class="like-btn"
                                    onclick="sendLike(this, <%= item.getUserId() %>)">いいね</button>
                        </td>
                    </tr>
        <%
                }
            }
        %>

    </table>
    </div>
</div>

</body>
</html>
