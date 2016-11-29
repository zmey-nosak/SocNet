<%@ page import="model.Book" %>
<%@ page import="model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link rel="stylesheet" href="/main_style.css">
    <script src="/websocket.js"></script>
    <script>
        addEventListener("DOMContentLoaded", ()=>new MyWebSocket("mess", "", ""), false);
    </script>
</head>

<body class=body text=#666564>
<p align="right" font-size="14">
    <a class="A" align="right" href=#>Выход</a>
</p>
<jsp:useBean id="userInfo" class="model.UserInfo" scope="request"/>
<table class=table cellspacing="8px">
    <tr class=tr>
        <td width="10%" valign="top">
        <table>
            <tr>
                <td><a class="A" href="/index.html">На главную</a></td>
                <td width=5px></td>
            </tr>
            <tr>
                <td><a class="A" href="/list/friends">Мои друзья</a></td>
                <td width=5px></td>
            </tr>
            <tr>
                <td><a class="A" href="/list/messages">Сообщения</a>
                </td>
                <td width=5px id="mess"></td>
            </tr>
            <tr>
                <td><a class="A" href="/index.html">Сообщество</a></td>
                <td width=5px></td>
            </tr>
        </table>
        </td>
        <td class=td cellpadding=0 cellspacing=0 align=center valign=top width=20%>
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
        <td valign="top">
            <div/>
            <%=userInfo.getF_name() + " " + userInfo.getI_name()%>
            <div/>
            <%=userInfo.getDob()%>
        </td>
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
            <p align="center">Книги</p>
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