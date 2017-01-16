<%@ page import="model.Author" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="model.Book" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.User" %><%--
  Created by IntelliJ IDEA.
  User: Echetik
  Date: 26.10.2016
  Time: 21:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ taglib uri="/WEB-INF/socNet.tld" prefix="socnet" %>
<html>
<head>
    <title>Книги</title>
    <style type="text/css">
        A {
            color: #FF9E26; /* Цвет ссылок */
        }
    </style>
    <script src="/scripts/server.js"></script>
    <script src="/scripts/Notification.js"></script>
    <link rel="stylesheet" href="/css/Menu.css">
    <script>
        function addBook(bookId) {
            Server.getUserBooks(<%=((User)request.getSession().getAttribute("user")).getUser_id()%>).then(books=> {
                if (bookId==books.find(p=>p==bookId)) {
                    sendNotification("Книга уже у Вас в списке!");
                } else {
                    Server.addBook(bookId).then(p=> {
                        sendNotification("Книга добавлена!");
                    })
                }
            });

        }
    </script>
</head>
<body background=/images/index.gif style="color:#FF9E26">
<socnet:menu user_id="${userId}"></socnet:menu>
<table border="1">
    ${socnet:getList(requestScope["bookList"])}
</table>
</body>
</html>