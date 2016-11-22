<%@ page import="model.Author" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="model.Genre" %>
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
    <title>Жанры</title>
    <style type="text/css">
        A {
            color: #FF9E26; /* Цвет ссылок */
        }
    </style>
</head>
<body background=/images/index.gif style="color:#FF9E26">

<jsp:useBean id="genres" class="java.util.HashSet" scope="request"/>
<% for (Genre genre : (HashSet<Genre>) genres) {%>
<ul>
    <li><a href="/books?genre_id=<%=String.valueOf(genre.getGenre_id())%>"><%=genre.getGenre_name()%>
    </a></li>
</ul>
<%}%>
</body>
</html>