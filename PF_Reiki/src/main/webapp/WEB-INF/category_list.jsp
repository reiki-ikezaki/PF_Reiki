<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>カテゴリ一覧</title>
</head>
<body>

<h2>カテゴリ一覧</h2>

<table border="1">
    <tr>
        <th>ID</th>
        <th>カテゴリ名</th>
        <th>削除</th>
    </tr>

    <c:forEach var="cat" items="${categoryList}">
        <tr>
            <td>${cat.id}</td>
            <td>${cat.name}</td>
            <td>
                <form action="delete_category" method="post">
                    <input type="hidden" name="id" value="${cat.id}">
                    <input type="submit" value="削除">
                </form>
            </td>
        </tr>
    </c:forEach>

</table>

<br>
<a href="create_category">カテゴリ追加</a>

</body>
</html>
