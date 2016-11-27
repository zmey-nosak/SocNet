<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/socNet.tld" prefix="socnet" %>
<html>
<head>
    <title>Title</title>
    <style type="text/css">
        A {
            color: #FF9E26; /* Цвет ссылок */
        }
    </style>

    <script>
        function showParameters(img,id,fio) {
            var oParams = {img: img, id: id, fio:fio};
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
<body background=/images/index.gif style="color:#FF9E26">

<table border="1" id="tab">
   ${socnet:getList(requestScope["users"])};
</table>

</body>
</html>
