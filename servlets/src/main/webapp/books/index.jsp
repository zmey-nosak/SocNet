<%@ page import="model.Author" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="model.Book" %>
<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: Echetik
  Date: 26.10.2016
  Time: 21:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Книги</title>
    <style type="text/css">
        A {
            color: #FF9E26; /* Цвет ссылок */
        }
    </style>
</head>
<body background=/images/index.gif style="color:#FF9E26">
<jsp:useBean id="books" class="java.util.ArrayList" scope="request"/>
<table border="1">
    <% for (Book book : (ArrayList<Book>) books) {%>
    <tr>
        <td>
            <%=book.getBook_name() + "."%>
        </td>
        <td><img src=<%=book.getImage_src()%> width="60" height="100">
        </td>
    </tr>
    <%}%>
</table>
</body>
</html>