<%@ page import="tags.Printable" %>
<%@ page import="model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="/websocket.js"></script>
    <script src="/scripts/server.js"></script>
    <script src="/scripts/CommunicationList.js"></script>
    <script src="/scripts/MessageList.js"></script>
    <script src="/scripts/TechnicalMessage.js"></script>
    <script src="/scripts/FriendList.js"></script>
    <script src="/scripts/ModalWindow.js"></script>
    <script src="/scripts/UserPage.js"></script>
    <script src="/scripts/Common.js"></script>
    <link rel="stylesheet" href="/css/list_messages.css">
    <link rel="stylesheet" href="/css/lists.css">
    <link rel="stylesheet" href="/css/modal_window.css">
    <link rel="stylesheet" href="/css/user_page.css">

    <script>
        function setup() {
            <%=((Printable)request.getAttribute("printable")).getScript()%>
            Server.getOwnerRequests().then(requests=> {
                requests.forEach(it=>ownerRequest.push(it));
            });
            Server.getFriendRequests().then(requests=> {
                setNewFriend(requests);
            });
            Server.getUnreadMessCnt().then(cnt=> {
                setNewMess(cnt);
            })
            initWebSocket();
        }

    </script>


</head>
<body onload="setup()">
<table width="800" border="0" align="center" height="600" id=page">
    <tr>
        <td valign="top" height="15" align=right><a href="/logout">Выход</a>
        </td>
    </tr>
    <tr>
        <td valign=top>
            <table width="100%" height="100%">
                <tr>
                    <td valign="top" width=200px style="border:#0000FF solid 1px" height=300px>
                        <div class="left_menu_my">
                            <ul>
                                <li><a href="/index.jsp">На главную</a></li>
                                <li>
                                    <a href="/userpage?userId=<%=((User)request.getSession().getAttribute("user")).getUserId()%>">Моя
                                        страница</a><a class="notification" href="/profile"> ред.</a></li>
                                </li>
                                <li>
                                    <a href="/friends?userId=<%=((User)request.getSession().getAttribute("user")).getUserId()%>">Друзья</a>
                                    <a class="notification"
                                       href=/friends/requests?userId=<%=((User) request.getSession().getAttribute("user")).getUserId()%>
                                       id="newFriends"></a></li>
                                </li>
                                <li>
                                    <a href="/messages?userId=<%=((User)request.getSession().getAttribute("user")).getUserId()%>">Сообщения</a>
                                    <a class="notification" href=# id="newMess"></a></li>
                                <li>
                                    <a href="/users?userId=<%=((User)request.getSession().getAttribute("user")).getUserId()%>">Сообщество</a>
                                </li>

                            </ul>
                        </div>
                    </td>
                    <td margin=0px valign=top style="border:#0000FF solid 1px" height=300px>
                        <table width=100% margin=0px>
                            <tr>
                                <td valign="top" id="response_element" align="left">
                                </td>
                                <td height="100%" valign=top id="additionalColumn">
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>

<div class="modal-overlay" id="modal_window"
     aria-hidden="true" role="dialog"
     aria-labelledby="modal_title">
    <div class=modal-content id="modal_holder" role="document">
        <div class=modal_header>Новое сообщение</div>
        <div class=modal_mainImage><img src=# width=20 height=25 id=modal_mainImage></div>
        <div class=modal_head id=modal_user><a href=#>Дядя Ваня</a></div>
        <div class=modal_message><textarea class=modal_content_text id=modal_textarea cols=34 rows=5></textarea></div>
        <button class=modal_send_btn id="snd_btn">Отправить</button>
        <button class="btn-close" id="modal_close" type="button" aria-label="close">
            &times;
        </button>
    </div>
</div>
</body>
</html>
