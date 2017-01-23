'use strict';

const WS_URI = "ws://localhost:8080/chat";

class MyWebSocket {
    constructor() {
        this.webSocket = new WebSocket(WS_URI);
        this.webSocket.addEventListener("close",evt=>{
            console.log("websocketClose");
        });
        addEventListener("beforeunload", this.webSocket.close.bind(this.webSocket));
    }

    doSend(message) {
        this.webSocket.send(message);
    }
}
