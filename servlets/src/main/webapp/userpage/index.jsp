<%@ page import="model.Book" %>
<%@ page import="model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style>

        body {
            /* Убираю отступы. */
            margin: 0px;
            /* Убираю еще отступы. */
            padding: 0px;
            /* Задаю шрифт. */
            font: 12px 'Verdana';
            COLOR: #FEDD8F;
            background: url(/images/index.gif);
        }

        .A {
            COLOR: #FEDD8F;
            font-size: 8px;
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
            width: 90%;
            COLOR: #FEDD8F;
        }

        .table .td {
            border-color: #ff6600;
            border-width: 5px;
            border-style: solid;
            padding: 5px;
            box-shadow: inset 0 1px #fab587, 0 3px 3px #222;
            border-radius: 3px;
            margin: 40px auto;
            align-self: center;
            TEXT-DECORATION: none;

        }

        .table .td1 {
            border-color: #ff6600;
            border-width: 1px;
            border-style: solid;
            padding: 0px;
            box-shadow: inset 0 1px #fab587, 0 3px 3px #222;
            border-radius: 3px;
            margin: 5px auto;
            align-self: center;
            TEXT-DECORATION: none;

        }

        .button {
            border: none;
            display: block;
            width: 81px;
            height: 46px;
            margin-left: 9px;
            float: left;
            border-radius: 3px;
            background: linear-gradient(#70787e, #4b545b);
            text-transform: uppercase;
            font-family: Helvetica, Neue;
            font-size: 13px;
            font-weight: bold;
            cursor: pointer;
            color: #11161a;
            text-shadow: 0 1px 0 #727980;
            box-shadow: 0 3px 3px #222, inset 0 1px 0 #a8a8a8;
        }
    </style>
    <title></title>
    <script>

        worker.port.postMessage("tttt");
    </script>
</head>

<body class=body text=#666564>
<p align="right" font-size="14">
    <a class="A" align="right" href=#>Выход</a>
</p>
<jsp:useBean id="userInfo" class="model.UserInfo" scope="request"/>
<table class=table cellspacing="8px">
    <tr class=tr>
        <td rowspan=4 width=15% valign=top>
            <li><a href="/index.html">На главную</a></li>
            <li><a href="/users.html">Мои друзья</a></li>
            <li><a href="#">Сообщения</a></li>
            <li><a href="/userlist/">Сообщество</a></li>
            <li>Группы</li>
            <li>Группы</li>
            <li>Группы</li>
            <li>Группы</li>
        </td>
        <td class=td rowspan=4 cellpadding=0 cellspacing=0 align=center valign=top width=20%>
            <table width=100% padding=0px margin=0px border="1">
                <tr class=tr>
                    <td height=150 align="center">
                        <image src=data:image/jpg;base64,<%=userInfo.getPhoto()%> width="100" height="150"/>
                    </td>
                </tr>
                <tr class=tr>
                    <td class=td1 align=center><input type="submit" value="Редактировать"></td>
                </tr>
            </table>
        </td>

        <td><%=userInfo.getF_name() + " " + userInfo.getI_name()%>
        </td>
    <tr>
        <td><%=userInfo.getDob()%>
        </td>
    </tr>
    <tr>
        <td rowspan=2 valign=top>ЕЩЁ ФИГНЯ</td>
    </tr>
    <tr>

    </tr>
    <tr>
        <td></td>
        <td class=td valign="top">
            <table border="1" width=180px border=1px valign="top">
                <th colspan="3" align="center" valign="top">Друзья</th>

                <%for (User user : userInfo.getUser_friends()) {%>
                <tr>
                    <td align=center>
                        <image src=data:image/jpg;base64,<%=user.getPhoto()%> width="60" height="60"/>
                        <br>
                        <a href="/userpage?user_id=<%=String.valueOf(user.getUser_id())%>"
                           class="A"><%=user.getI_name() + " " + user.getF_name()%>
                        </a>
                    </td>
                </tr>
                <%}%>
            </table>
        </td>
        <td class=td valign=top colspan=1 align="left">
            <p align="center">Мои книги</p>
            <table>

                <% for (Book book : userInfo.getUser_books()) {%>
                <tr>
                    <td>
                        <%=book.getBook_name()%>
                    </td>
                </tr>
                <%}%>
            </table>

    </tr>
</table>

</body>

</html>