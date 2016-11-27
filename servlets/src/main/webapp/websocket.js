'use strict';

const WS_URI = "ws://localhost:8080/chat";

class MyWebSocket {

    constructor(outputElementId = "output", inputElementId = "message", to_user = "") {
        /** @type HTMLInputElement*/
        this.submit = document.getElementById("submit");
        /** @type HTMLTextAreaElement */
        this.input = document.getElementById("message");
        if (this.submit != null) {
            this.submit.addEventListener("click", evt => {
                this.doSend(
                    "{\"user_id_to\":" + to_user +
                    ",\"message\":\"" + this.input.value + "\"}");
                this.input.value = "";
                evt.preventDefault();
            }, true);
        }

        //noinspection JSUnusedGlobalSymbols,SpellCheckingInspection
        this.webSocket = Object.assign(new WebSocket(WS_URI), {
     //       onopen: () => this.writeToScreen(`Connected to Endpoint!`),
            onmessage: evt => this.writeToScreen(`Message Received: ${evt.data}`),
     //       onerror: evt => this.writeToScreen(`ERROR: ${evt.data}`)
        });

        //noinspection SpellCheckingInspection
        addEventListener(
            "beforeunload"
            ,
            this
                .webSocket
                .close
                .bind(
                    this
                        .webSocket
                ))
        ;
    }

    writeToScreen(message) {
        /** @type HTMLTableColElement*/
        this.output = document.getElementById("mess");
        var t= document.createTextNode("1");
        if (this.output != null) {
            this.output.appendChild(t);
        }
    }

    doSend(message) {
        this.webSocket.send(message);
    }


}
