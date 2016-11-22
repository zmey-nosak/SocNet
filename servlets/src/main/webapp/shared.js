var ws = null
var url = "ws://localhost:8080/chat"
var connections = 0;
self.addEventListener("connect", function (e) {
    var port = e.ports[0];
    port.addEventListener("message", function (e) {
        if (e.data === "start") {
            if (ws === null) {
                ws = new WebSocket(url);
                port.postMessage("started connection to " + url);
            } else {
                port.postMessage("reusing connection to " + url);
            }
        } else {
            ws.send(e.data);
        }
    }, false);
    port.start();
}, false);