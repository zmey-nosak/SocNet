<%@ taglib prefix="socnet" uri="http://SocNet.com" %>
<%@ page import="model.Author" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="model.Genre" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Жанры</title>
    <style type="text/css">
        A {
            color: #FF9E26; /* Цвет ссылок */
        }
    </style>
    <link rel="stylesheet" href="/css/Menu.css">
</head>
<body background=/images/index.gif style="color:#FF9E26">
<jsp:useBean id="genres" class="java.util.ArrayList" scope="request"/>
<jsp:directive.include file="/index.jsp"/>
<% for (Genre genre : (ArrayList<Genre>) genres) {%>
<ul>
    <li><a href="/books?genre_id=<%=String.valueOf(genre.getGenre_id())%>"><%=genre.getGenre_name()%>
    </a></li>
</ul>
<%}%>
</body>
</html>