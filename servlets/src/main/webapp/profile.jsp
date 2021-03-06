<%@ page import="model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <style>
        * {
            margin: 0;
            padding: 0;
            -webkit-box-sizing: border-box;
            -moz-box-sizing: border-box;
            -o-box-sizing: border-box;
            box-sizing: border-box;
        }

        body {
            COLOR: black;
        }

        html {
            background-color: #fff;
            font-size: 10px;
        }

        .container {
            margin: 1% auto;
            width: 45%;
            border: 1px solid #E5E2E5;
            -webkit- border-radius: 3px;
            -moz-border-radius: 3px;
            -o-border-radius: 3px;
            border-radius: 3px;
        }

        .container.one {
            font-weight: bold;
            width: 45%;
            padding: 1%;
        }

        span, input, label, img {
            display: inline-block;
        }

        input {
            margin: 1%;
            padding: 0.8%;
            border: 1px solid #BEBEBE;
            width: 50%;
        }

        label, img {
            width: 7.5em;
        }

        label {
            position: relative;
        }

        input ~ span::after {
            content: '';
            padding-left: 5px;
            vertical-align: middle;
        }

        /* Раскрашиваем input в фокусе */
        input:focus ~ span::after,
        input:focus:valid ~ span::after,
        input:focus:invalid ~ span::after {
            content: '';
        }

        /* Раскрашиваем корректные input  */
        input:valid ~ span::after {
            content: '✓';
            color: #4E9B5A;
        }

        /* Раскрашиваем ошибочные input */
        input:invalid ~ span::after {
            content: '×';
            color: #B11117;
        }

        .captcha input:invalid ~ span::after {
            content: '× Неправильное число';
        }

        button {
            /*margin-bottom : 30px;*/
            margin-top: 10px;
            color: #fefefe;
            text-shadow: 0 1px 0 #777;
            border: 1px solid #b6733c;
            box-shadow: inset 0 0 1px 1px #ffcb9c;
            text-transform: uppercase;
            background: -moz-linear-gradient(top, rgba(249, 159, 107, 1) 0%, rgba(238, 148, 96, 1) 57%, rgba(236, 146, 94, 1) 89%, rgba(232, 144, 94, 1) 94%, rgba(234, 146, 96, 1) 96%, rgba(244, 161, 111, 1) 100%);
            background: -webkit-linear-gradient(top, rgba(249, 159, 107, 1) 0%, rgba(238, 148, 96, 1) 57%, rgba(236, 146, 94, 1) 89%, rgba(232, 144, 94, 1) 94%, rgba(234, 146, 96, 1) 96%, rgba(244, 161, 111, 1) 100%);
            font-size: 17px;
            align: center;
        }

        button:hover {
            cursor: pointer;
            background: -moz-linear-gradient(top, rgba(244, 161, 111, 1) 0%, rgba(234, 146, 96, 1) 4%, rgba(232, 144, 94, 1) 6%, rgba(236, 146, 94, 1) 12%, rgba(238, 148, 96, 1) 43%, rgba(249, 159, 107, 1) 100%);
            background: -webkit-linear-gradient(top, rgba(244, 161, 111, 1) 0%, rgba(234, 146, 96, 1) 4%, rgba(232, 144, 94, 1) 6%, rgba(236, 146, 94, 1) 12%, rgba(238, 148, 96, 1) 43%, rgba(249, 159, 107, 1) 100%);
        }
    </style>
</head>
<body onload="setup()">
<link rel="stylesheet" type="text/css" href="tcal.css">
<script src="/tcal.js"></script>
<p><i>${message}</i></p>


<input type="hidden" name="action" value="edit">
<section class="container one">
    <form action="/profile" method="post">
        <p><label for="i_name">Имя</label>
            <input type="text" name="i_name" pattern="^[А-Яа-яЁё\s]+$" value="${user.i_name}" required><span></span></p>

        <p><label for="f_name">Фамилия</label>
            <input type="text" name="f_name" pattern="^[А-Яа-яЁё\s]+$" value="${user.f_name}" required><span></span></p>

        <p><label for="dob">Дата рождения</label>
            <input name="dob" value="${user.getDobString()}" pattern="\d{1,2}\.\d{1,2}\.\d{4}" required><span></span>
        </p>

        <p><label for="email">Эл. почта</label>
            <input type="email" name="email" value="${user.email}" required><span></span></p>

        <p><label for="old_password">Старый пароль</label>
            <input type="password" name="old_password" value=""><span></span></p>

        <p><label for="new_password">Новый пароль</label>
            <input type="password" name="new_password" value=""><span></span></p>

        <p><label for="conf_password">Повторите</label>
            <input type="password" name="conf_password" value=""><span></span></p>
        <p>
            <button type="submit">Сохранить</button>
        </p>
    </form>
        <button onclick="location.href='/userpage?userId=<%=((User)session.getAttribute("user")).getUserId()%>'">
            Отмена
        </button>

</section>

</body>
</html>