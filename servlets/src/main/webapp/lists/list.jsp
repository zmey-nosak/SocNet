<%@ page import="tags.Printable" %>
<%@ page import="model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/socNet.tld" prefix="socnet" %>
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

    <link rel="stylesheet" href="/css/list_messages.css">
    <link rel="stylesheet" href="/css/lists.css">
    <link rel="stylesheet" href="/css/modal_window.css">
    <link rel="stylesheet" href="/css/user_page.css">

    <script>
        var messageList = {};
        var socket = {};
        var communicationList = {};
        var friendList = {};
        var modalWindow = {};
        var userPage = {};
        function setup() {
            socket = new MyWebSocket();
            userPage = new UserPage(<%=((User)request.getSession().getAttribute("user")).getUser_id()%>);
            communicationList = new CommunicationList(<%=((User)request.getSession().getAttribute("user")).getUser_id()%>, socket);
            messageList = new MessageList(socket);
            friendList = new FriendList(<%=((User)request.getSession().getAttribute("user")).getUser_id()%>);
            modalWindow = new ModalWindow();

        }
        ;
        function setModal(fio, img, user_id) {

        }

    </script>


</head>
<body onload="setup()">
<table width="800" border="0" align="center" height="600" id=page">
    <tr>
        <td valign="top" height="15" align=right><a href=#>Выход</a>
        </td>
    </tr>
    <tr>
        <td valign=top>
            <table width="100%" height="100%">
                <tr>
                    <td valign="top" width=200px style="border:#0000FF solid 1px" height=300px>
                        <div class="left_menu_my">
                            <ul>
                                <li><a href="/index.html">На главную</a></li>
                                <li><a href="#"
                                       onclick="userPage.load(<%=((User)request.getSession().getAttribute("user")).getUser_id()%>)">Моя
                                    страница</a></li>
                                <li><a href="#" onclick="friendList.print('response_element')">Друзья</a></li>
                                <li><a href="#" onclick="communicationList.print('response_element')">Сообщения</a><a
                                        color="red" href=#></a></li>
                                <li><a href="/index.html">Сообщество</a></li>

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
