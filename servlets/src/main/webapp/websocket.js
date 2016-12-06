'use strict';

const WS_URI = "ws://localhost:8080/chat";

class MyWebSocket {
    constructor(responseElementId = "", contentElementId = "", to_user = "", eventElementId = "", object) {
        this.eventElement = document.getElementById(eventElementId);
        this.contentElement = document.getElementById(contentElementId);
        if (this.eventElement != null) {
            this.eventElement.addEventListener("click", evt => {
                var mess = new TechnoMessage(to_user, this.contentElement.value, new Date());
                this.doSend(JSON.stringify(mess));
                this.contentElement.value = "";
                evt.preventDefault();
            }, true);
        }
        this.webSocket = new WebSocket(WS_URI);

        this.webSocket.onmessage = function (evt) {
            object.addToElem(responseElementId, JSON.parse(evt.data, function (key, value) {
                    if (key == 'date') return new Date(value);
                    return value;
                })
            );
        };
        addEventListener("beforeunload", this.webSocket.close.bind(this.webSocket));
    }

    /*writeToScreen(message) {
     this.output = document.getElementById(outputElementId);
     var t = document.createTextNode("1");
     if (this.output != null) {
     this.output.appendChild(t);
     }
     }*/

    doSend(message) {
        this.webSocket.send(message);
    }

    /* onClose(evt) {
     webSocket.close();
     writeAttributeValues('onClose Event Fired');
     writeToScreen("DISCONNECTED");
     }

     onMessage(message) {
     Server.getUserMessages().then(messages=>object.writeToScreen(messages));
     }

     onError(evt) {
     writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
     writeAttributeValues('onError Event Fired');
     }
     */
    /*  doSend(message) {
     websocket.send(message);
     writeAttributeValues('onSend Event Fired');
     writeToScreen("SENT: " + message);
     }*/
    /*
     writeAttributeValues(prefix) {
     var pre = document.createElement("p");
     pre.style.wordWrap = "break-word";
     pre.innerHTML = "INFO " + getCurrentDate() + " " + prefix + "<b> readyState: " + websocket.readyState + " bufferedAmount: " + websocket.bufferedAmount + "</b>";
     ;
     attributes_log.appendChild(pre);
     }


     getCurrentDate() {
     var now = new Date();
     var datetime = now.getFullYear() + '/' + (now.getMonth() + 1) + '/' + now.getDate();
     datetime += ' ' + now.getHours() + ':' + now.getMinutes() + ':' + now.getSeconds();

     return datetime;
     }

     browserSupportsWebSockets() {
     if ("WebSocket" in window) {
     return true;
     }
     else {
     return false;
     }
     }*/

}
