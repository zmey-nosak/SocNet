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
</head>
<body class=body text=#666564>
<p align="right" font-size="14">
    <a class="A" align="right" href=#>Выход</a>
</p>
<table class=table cellspacing="8px" width="80%">
    <tr class=tr>
        <td width="5%" valign="top">
            <table width="5%">
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
        <td class=td cellpadding=0 cellspacing=0 align=center valign=top width=40%>
            <table padding=0px margin=0px border="1">
                ${socnet:getList(requestScope["list"])}
            </table>
        </td>
    </tr>
</table>

</body>
</html>
