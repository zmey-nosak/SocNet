<%--
  Created by IntelliJ IDEA.
  User: Echetik
  Date: 24.01.2017
  Time: 23:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html><head><title>Error</title></head>
<body>
<h3>Error</h3>
<hr />
<jsp:expression>
    (request.getAttribute("errorMessage") != null)
    ? (String) request.getAttribute("errorMessage")
    : "unknown error"</jsp:expression>
<hr />
<a href="controller">Return to login page</a>
</body></html>