<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page import="model.Book" %>
<%@ page import="model.User" %>
<%@ taglib uri="/WEB-INF/socNet.tld" prefix="socnet" %>
<html>
<head>
    <title>Книги</title>
    <link rel="stylesheet" href="/css/Menu.css">
    <script src="/scripts/server.js"></script>
    <script src="/scripts/Notification.js"></script>
</head>
<jsp:useBean id="book" class="model.Book" scope="request"/>
<body background=/images/index.gif style="color:#FF9E26">
<socnet:menu userId="${userId}"></socnet:menu>
<table border="1">
    <tr>
        <td valign="top">
            <img src="<%=book.getImage_src()%>" width="80" height="100"><br>
            <%=book.getBook_name()%>
        </td>
        <td>
            <form action="/books?book_id=<%=book.getBook_id()%>" method="post" name="confirmationForm"
                  id="confirmationForm" accept-charset="utf-8">
                <textarea rows="10" cols="30" id="confirmationText" name="confirmationText"></textarea>
                <br>
                <input type="submit" value="Oтправить">
            </form>
        </td>
    </tr>
    ${socnet:getList(requestScope["bookReviews"])}
</table>
</body>
</html>