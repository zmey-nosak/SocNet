<%@ page import="model.Author" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="model.Book" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <link rel="stylesheet" href="/css/Menu.css">
    <script src="/scripts/Notification.js"></script>
    <script>
        function addBook(bookId) {
            Server.addBook(bookId);
            sendNotification("Книга добавлена!");
        }
    </script>
</head>
<body background=/images/index.gif style="color:#FF9E26">
<socnet:menu userId="${userId}"></socnet:menu>
<jsp:useBean id="books" class="java.util.ArrayList" scope="request"/>
<jsp:useBean id="userInfo" class="model.UserInfo" scope="request"/>

<table border="1">
    <% for (Book book : (ArrayList<Book>) books) {%>
    <tr>
        <td>
            <%=book.getBook_name() + "."%>
        </td>
        <td><img src=<%=book.getImage_src()%> width="60" height="100">
        </td>
        <td>
            <button onclick="addBook(<%=book.getBook_id()%>)">Добавить к себе</button>
        </td>
    </tr>
    <%}%>
</table>
</body>
</html>