﻿<%@taglib prefix="socnet" uri="http://SocNet.com" %>
<%@ page import="model.Author" %>
<%@ page import="java.util.HashSet" %>

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
    <title>Авторы</title>
    <style type="text/css">
        A {
            color: #FF9E26; /* Цвет ссылок */
        }
    </style>
    <link rel="stylesheet" href="/css/Menu.css">
</head>
<body background=/images/index.gif style="color:#FF9E26">

<jsp:useBean id="authors" class="java.util.ArrayList" scope="request"/>
<jsp:directive.include file="/index.jsp"/>
<table border="1">
    <th>Автор</th>
    <th>Дата рождения</th>
    <% for (Author author : (ArrayList<Author>) authors) {%>
    <tr>
        <td>
            <a text-decoration="none" href="/books?author_id=<%=String.valueOf(author.getAuthor_id())%>">
                <%=author.getF_name() + " "
                        + author.getI_name() + " "
                        + (author.getO_name() == null ? "" : author.getO_name())%>
            </a>
        </td>
        <td><%=author.getStringDob()%>
        </td>
    </tr>
    <%}%>
</table>
</body>
</html>