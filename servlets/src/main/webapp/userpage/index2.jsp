<%@ page import="model.Book" %>
<%@ page import="model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/socNet.tld" prefix="socnet" %>
<html>
<head>
    <title></title>
    <script src="/websocket.js"></script>
    <script>
        addEventListener("DOMContentLoaded", ()=>new MyWebSocket("mess", "", ""), false);
    </script>
    <style type="text/css">

        a.button15 {
            display: inline-block;
            width: 80%;
            margin: 0;
            font-family: arial, sans-serif;
            font-size: 11px;
            font-weight: bold;
            color: rgb(68, 68, 68);
            text-decoration: none;
            user-select: none;
            padding: .2em 1.2em;
            outline: none;
            border: 1px solid rgba(0, 0, 0, .1);
            border-radius: 2px;
            background: rgb(245, 245, 245) linear-gradient(#f4f4f4, #f1f1f1);
            transition: all .218s ease 0s;
        }

        a.button15:hover {
            color: rgb(24, 24, 24);
            border: 1px solid rgb(198, 198, 198);
            background: #f7f7f7 linear-gradient(#f7f7f7, #f1f1f1);
            box-shadow: 0 1px 2px rgba(0, 0, 0, .1);
        }

        a.button15:active {
            color: rgb(51, 51, 51);
            border: 1px solid rgb(204, 204, 204);
            background: rgb(238, 238, 238) linear-gradient(rgb(238, 238, 238), rgb(224, 224, 224));
            box-shadow: 0 1px 2px rgba(0, 0, 0, .1) inset;
        }

        a.link {
            font-size: 8px;
        }

    </style>
</head>
<body>
<jsp:useBean id="userInfo" class="model.UserInfo" scope="request"/>
<table width="800" border="0" align="center" height="700">
    <tr>
        <td valign="top" height="15" align=right><a href=#>Выход</a>
        </td>
    </tr>
    <tr>
        <td valign=top>
            <table width="100%" height="100%">
                <tr>
                    <td valign="top" width="200" style="border:#0000FF  solid 1px" height=300px>
                        <div class="left_menu_my">
                            <ul>
                                <li><a href="/index.html">На главную</a></li>
                                <li><a href="/list/friends">Друзья</a></li>
                                <li><a href="/list/messages">Сообщения</a> <a id="mess" color="red" href=#></a></li>
                                <li><a href="/index.html">Сообщество</a></li>

                            </ul>
                        </div>
                    </td>
                    <td width="200" margin=0px valign=top height=300px>
                        <table width=100% margin=0px>
                            <tr>
                                <td height=170px style="border-bottom:#0000FF solid 1px" align=center>
                                    <image border="1" width=120px height="170px" src=data:image/jpg;base64,<%=userInfo.getPhoto()%> />
                                    <a href=# class="button15">Изменить</a>
                                    <p></p>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div align="center">Друзья</div>
                                    <table>
                                        ${socnet:getList(requestScope["miniFriendList"])}
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td style="border-right:#0000FF solid 1px" valign="top">
                        <table width=100% margin=0px>
                            <tr>
                                <td width=100% height=170px style="border-bottom:#0000FF solid 1px" valign=top>
                                    <div align=left><h1><%=userInfo.getF_name() + " " + userInfo.getI_name()%></h1></div>
                                    <div>Дата рождения:<%=userInfo.getDob()%></div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <table width=100% cellspacing=10>
                                        <tr>
                                            ${socnet:getList(requestScope["miniBookList"])}
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>

                    </td>
                </tr>
                </td>
                </tr>
            </table>
</body>
</html>