<%--
  Created by IntelliJ IDEA.
  User: Echetik
  Date: 24.01.2017
  Time: 23:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style type="text/css">
        .a {
            text-decoration: none;
        }
    </style>
    <title>Login</title></head>
<body><h3><a class="a" href="login.jsp">Login</a> <a class="a" href="register2.jsp">Registration</a></h3>
<hr/>
<form name="loginForm" method="POST"
      action="controller">
    <input type="hidden" name="command" value="login"/>
    Login:<br/>
    <input type="text" name="login" value=""><br/>
    Password:<br/>
    <input type="password" name="password" value="">
    <br/>
    <input type="submit" value="Enter">
</form>
<hr/>
</body>
</html>