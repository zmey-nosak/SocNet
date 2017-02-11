<%@taglib prefix="socnet" uri="http://SocNet.com" %>
<%@ page import="model.Author" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Авторы</title>
    <style type="text/css">
        A {
            color: #FF9E26; /* Цвет ссылок */
            text-decoration: none;
        }
    </style>
    <link rel="stylesheet" href="/css/Menu.css">
    <script src="/scripts/server.js"></script>
    <script src="/scripts/Authors.js"></script>
</head>
<script>
    function init() {
        var authors = new Authors(<%=request.getAttribute("offset")%>);
        authors.fillTable("authorTable",10);
    }
</script>
<body background=/images/index.gif style="color:#FF9E26" onload="init()">

<jsp:useBean id="authors" class="java.util.ArrayList" scope="request"/>
<jsp:directive.include file="/index.jsp"/>

<table border="1" id="authorTable">
    <th>Автор</th>
    <th>Дата рождения</th>
</table>
<div id="links"></div>
</body>
</html>
