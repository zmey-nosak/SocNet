<%@ page import="model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
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
<jsp:directive.include file="/index.jsp"/>
<table border="1">
    ${socnet:getList(requestScope["bookList"])}
</table>
</body>
</html>