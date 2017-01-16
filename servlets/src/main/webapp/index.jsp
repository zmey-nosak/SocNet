<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<html>
<head>
    <meta http-equiv=Content-Type content="text/html; charset=UTF-8" charset=UTF-8>
    <style>
        body {
            /* Убираю отступы. */
            margin: 0px;
            /* Убираю еще отступы. */
            padding: 0px;
            /* Задаю шрифт. */
            font: 12px 'Verdana';
            COLOR: #FEDD8F;
        }
        .a {
            /* Делаю элементы блочными: */
            display: block;
            /* Задаю белый цвет. */
            color: #FF9E26;
            /* Задаю отступ 10px. */
            padding: 10px;
            /* Убираю форматирование*/
            text-decoration: none;
            /* Задаю цвет. */
            background-color: transparent;

        }
        .table {
            /*background: linear-gradient(#fa7f2d, #ff6600, #fa7f2d);*/
            border-color: #ff6600;
            border-width: 5px;
            border-style: solid;
            padding: 5px;
            box-shadow: inset 0 1px #fab587, 0 3px 3px #222;
            border-radius: 3px;
            margin: 40px auto;
            align-self: center;
            background: url(/images/fonder.gif);

            TEXT-DECORATION: none;
        }

        .login-form {
            width: 70%;
            margin: 40px auto;
        }

        .login-form .field {
            background: linear-gradient(#fa7f2d, #ff6600, #fa7f2d);
            padding: 5px;
            box-shadow: inset 0 1px #fab587, 0 3px 3px #222;
            border-radius: 3px;
            margin-bottom: 9px;
        }

        .login-form .field input {
            border: 1px solid #96450f;
            display: block;
            width: 277px;
            padding: 5px;
            font-size: 19px;
            font-family: Helvetica Neue;
            font-weight: 600;
            box-shadow: 0 1px #fcb483, inset 0 3px 5px #aaa;
            border-radius: 3px;
            color: #666564;
        }

        .login-form .field input:focus {
            outline: none;
            color: #333;
        }

        .login-form .field.with-btn {
            width: 200px;
            float: left;
        }

        .login-form .field.with-btn input {
            width: 188px;
        }

        ul {
            /* Убираю маркеры у списка*/
            list-style: none;
            /* Делаю элементы блочными. */
            display: block;
            /* Убираю отступы. */
            margin: 0px;
            /* Убираю еще отступы! */
            padding: 0px;
        }

        ul:after {
            /* Делаю элементы блочными. */
            display: block;
            /* Убираю выравнивание. */
            float: none;
            content: ' ';
            clear: both;
        }

        ul.mmenuu > li {
            /* Задаю выравнивание и позиционирование. */
            float: left;
            /* Считаем координаты относительно исходного места*/
            position: relative;
        }

        ul.mmenuu > li > a {
            /* Делаю элементы блочными: */
            display: block;
            /* Задаю белый цвет. */
            color: #FF9E26;
            /* Задаю отступ 10px. */
            padding: 10px;
            /* Убираю форматирование*/
            text-decoration: none;
            /* Задаю цвет. */
            background-color: transparent;

        }

        ul.mmenuu > li > a:hover {
            /* Задаю цвет при наведении. */
            background-color: transparent;
            /*color: #FFF;*/
            text-decoration: underline;
        }

        ul.ssubmenuu {
            position: absolute;
            width: 120px;
            top: 30px;
            left: 0px;
            /* Делаю субменю скрытыми. */
            display: none;
            /* Цвет — белый. */
            background-color: transparent;
        }

        ul.ssubmenuu > li {
            /* Блочное расположение элементов*/
            display: block;
        }

        ul.ssubmenuu > li > a {
            /* Делаю элементы блочными. */
            display: block;
            /* Убираю форматирование*/
            text-decoration: none;
            /* Задаю отступ. */
            padding-left: 10px;
            padding-right: 10px;
            padding-top: 2px;
            padding-bottom: 2px;
            /* Задаю цвет. */
            color: #FF9E26;

            /* Еще цвет. */
            background-color: transparent;
        }

        ul.ssubmenuu > li > a:hover {
            /* Цвет бэкграунда при наведении. */
            /*background-color: #573D1D;*/
            /* Задаю подчеркивание*/
            text-decoration: underline;
            color: #ffffff;
        }

        ul.mmenuu > li:hover > ul.ssubmenuu {
            /* Делаю элементы блочными. */
            display: block;
        }
    </style>

</head>

<body background=images/index.gif bgColor=#000000 text=#FCDD9B bgproperties=fixed>
<table class="table">
    <tr>
        <td valign="top" height="15" align=right><a class="a" href="/logout">Выход</a>
        </td>
    </tr>
    <tr>
        <td height="80" align="left" valign="top" padding="0">
            <ul class="mmenuu">
                <li><a href="/userpage?userId=<%=((User)request.getSession().getAttribute("user")).getUser_id()%>">Моя страница</a></li>
                <li><a href=#>Книги</a>
                    <ul class="ssubmenuu">
                        <li><a href="/authors/">По авторам</a></li>
                        <li><a href="/genres/">По жанрам</a></li>
                    </ul>
                </li>
                <li><a href=#>Библиотеки</a>
                    <ul class="ssubmenuu">
                        <li><a href="">Онлайн</a></li>
                        <li><a href=#>В городе</a></li>
                    </ul>
                </li>
                <li><a href=#>Рецензии</a>
                    <ul class="ssubmenuu">
                        <li><a href=#>По авторам</a></li>
                        <li><a href="">По книгам</a></li>
                    </ul>
                </li>
                <li><a href=#>Книжные новинки</a></li>
            </ul>
        </td>
    </tr>
</table>

</body>
</html>