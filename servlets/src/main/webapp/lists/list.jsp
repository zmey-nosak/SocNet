<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/socNet.tld" prefix="socnet" %>
<html>
<head>
    <title>Title</title>

    <link rel="stylesheet" href="/main_style.css">
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
    </script>
    ${socnet:getCSS(pageContext.request.getAttribute("css"))}
</head>
<body>
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
                                <li><a href="/list/messages">Сообщения</a> <a color="red" href=#></a></li>
                                <li><a href="/index.html">Сообщество</a></li>

                            </ul>
                        </div>
                    </td>
                    <td margin=0px valign=top style="border:#0000FF solid 1px" height=300px>
                        <table width=100% margin=0px>
                            <tr>
                                <td width=50 height=75 rowspan=2>
                                    ${socnet:getList(requestScope["list"])}
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
