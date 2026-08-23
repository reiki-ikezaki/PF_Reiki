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

    .like-btn {
        background: #3498db; color: white; padding: 5px 10px;
        border-radius: 4px; border: none; font-size: 13px; cursor: pointer;
    }
    .like-btn:disabled { background: #aaa; cursor: not-allowed; }
</style>

<script>
async function sendLike(btn, targetUserId) {
    btn.disabled = true;
    try {
        const res = await fetch("like", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: "targetUserId=" + encodeURIComponent(targetUserId)
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
    <h2>❤️ いいねランキング ❤️</h2>

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
                        <td><a href="accountDetail?id=<%= item.getUserId() %>"><%= item.getName() %></a></td>
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

</body>
</html>
