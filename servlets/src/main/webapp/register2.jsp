<%--
  Created by IntelliJ IDEA.
  User: Echetik
  Date: 25.01.2017
  Time: 0:10
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
    <input type="hidden" name="command" value="register"/>
    Login:<br/>
    <input type="text" name="login" value="${user.email}"><br/>
    First Name:<br/>
    <input type="text" name="i_name" pattern="^[А-Яа-яЁёA-z\s]+$" value="${user.i_name}" required>
    <br/>
    Last Name:<br/>
    <input type="text" name="f_name" pattern="^[А-Яа-яЁёA-z\s]+$" value="${user.f_name}" required>
    <br/>
    Birthday:<br/>
    <input type="text" autocomplete=off name="dob" pattern="\d{2}\.\d{2}\.\d{4}" value="${user.getDobString()}" required><label>dd.mm.yyyy</label>
    <br/>
    Password:<br/>
    <input type="password" name="password" autocomplete=off value="" >
    <br/>
    Confirm Password:<br/>
    <input type="password" name="confirm_password" value="">
    <br/>
    <input type="submit" value="Enter">
</form>
<hr/>
<p><i>${messages}</i></p>
</body>
</html>