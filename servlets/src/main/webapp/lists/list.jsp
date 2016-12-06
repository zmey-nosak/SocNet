<%@ page import="tags.Printable" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/socNet.tld" prefix="socnet" %>
<html>
<head>
    <title>Title</title>
    <script src="/websocket.js"></script>
    <script src="/scripts/server.js"></script>
    <%=request.getAttribute("js")%>


    <script>
        function showParameters(img, id, fio) {
            var oParams = {img: img, id: id, fio: fio};
            var oResult = window.showModalDialog("/dialogs/sendMessage.html", oParams, "dialogHeight:280px;dialogWidth:400px;scroll:no center=1");

            if (oResult != null) {

                bWrap = oResult.wrap;

                sColor = oResult.color;

                var oTxtText = document.getElementById("txtText");

                oTxtText.wrap = bWrap ? "soft" : "off";

                oTxtText.style.color = sColor;

            }
        }
        ;

        function setup() {
            var socket = new MyWebSocket("<%=((Printable)request.getAttribute("list")).getResponseElementName()%>",
                    "<%=((Printable)request.getAttribute("list")).getContentElementName()%>",
                    <%=request.getAttribute("partner")%>,
                    "<%=((Printable)request.getAttribute("list")).getEventElementName()%>",
                    <%=((Printable)request.getAttribute("list")).getPrintObject()%>);
        }
        ;
    </script>
    ${socnet:getCSS(pageContext.request.getAttribute("css"))}


</head>
<body onload="setup()">
<table width="800" border="0" align="center" height="700">
    <tr>
        <td valign="top" height="15" align=right><a href=#>Выход</a>
        </td>
    </tr>
    <tr>
        <td valign=top>
            <table width="100%" height="100%">
                <tr>
                    <td valign="top" width="200px" style="border:#0000FF solid 1px" height=300px>
                        <div class="left_menu_my">
                            <ul>
                                <li><a href="/index.html">На главную</a></li>
                                <li><a href="/list/friends">Друзья</a></li>
                                <li><a href="/list/communications">Сообщения</a> <a color="red" href=#></a></li>
                                <li><a href="/index.html">Сообщество</a></li>

                            </ul>
                        </div>
                    </td>
                    <td margin=0px valign=top style="border:#0000FF solid 1px" height=300px>
                        <table width=100% margin=0px>
                            <tr>
                                <td valign="top">
                                    ${socnet:getList(requestScope["list"])}
                                </td>
                                <td height="100%" valign=top>
                                    <p><b>Введите сообщение:</b></p>
                                    <p><textarea rows="10" cols="45" name="text"
                                                 id="<%=((Printable)request.getAttribute("list")).getContentElementName()%>"></textarea>
                                    </p>
                                    <p><input type="button" value="Отправить"
                                              id="<%=((Printable)request.getAttribute("list")).getEventElementName()%>">
                                    </p>
                                </td>
                            </tr>
                        </table>
                    </td>

                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>
