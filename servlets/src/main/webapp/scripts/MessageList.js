/**
 * Created by Echetik on 03.12.2016.
 */

'use strict';
/**
 * @interface
 */
class MessageList {

    constructor(soket, communicationId, ownerId, partnerId) {
        this.identification = "messages";
        this.socket = soket;
        this.communicationId = communicationId;
        this.targetElement = "";
        this.tmpuser = 0;
        this.tmpdate = new Date();
        this.b = false;
        this.owner = {};
        this.partner = {};
        this.load(communicationId, ownerId, partnerId);
        this.socket.webSocket.addEventListener("message", evt=> {
            this.handle(evt)
        }, false);
    }

    handle(evt) {
        var obj = JSON.parse(evt.data, function (key, value) {
            if (key == 'date') return new Date(value);
            return value;
        });
        if (obj.type == 0) {
            if (document.getElementById(("response_element_" + this.identification)) != null) {
                this.add(obj);
                if (obj.userId != obj.userIdTo) {
                    setTimeout(t=> {
                        var div = document.getElementById(obj.id);
                        if (div != null)
                            div.setAttribute("class", "mess_container");

                    }, 1000);
                    var mess_for_update = obj.id + ',';
                    var mess = new TechnicalMessage();
                    mess.setTypeMessage(1)
                        .setUserTo(this.partner.userId)
                        .setMessagesForUpdate(mess_for_update);
                    this.socket.doSend(JSON.stringify(mess));
                }
            }
        } else if (obj.type == 1) {
            var mass = obj.messagesForUpdate.split(',');
            setTimeout(t=> {
                mass.forEach(it=> {
                    var div = document.getElementById(it);
                    if (div != null)
                        div.setAttribute("class", "mess_container");
                })
            }, 1000);
        }
    }

    getElement() {
        return this.div_content;
    }

    setOwner(result) {
        this.owner = result;
        Server.getUser(this.partnerId).then(partner=>this.setPartner(partner));
    }

    setPartner(result) {
        this.partner = result;
        this.print("response_element");
    }

    getUser(userId) {
        if (this.owner.userId == userId) {
            return this.owner;
        } else {
            return this.partner;
        }
    }

    print(id) {
        this.targetElement = document.getElementById(id);
        this.targetElement.innerHTML = '';
        this.div_content = document.createElement("div");
        this.div_content.className = "mess_prokrutka";
        this.div_content.id = "response_element_" + this.identification;
        this.targetElement.appendChild(this.div_content);
        Server.getUserMessages(this.owner.userId, this.communicationId).then(messages=> {
            this.addAll(messages);
            this.div_content.scrollTop = this.div_content.scrollHeight;
            var mess_for_update = '';
            messages.forEach(t=> {
                if (t.userId != this.owner.userId && t.active == 1)
                    mess_for_update = mess_for_update + t.id + ','
            });
            if (mess_for_update.replace(/\s/g, '').length) {
                var mess = new TechnicalMessage();//(1, 0, this.partner.userId, '', null, 0, mess_for_update, 0);
                mess.setTypeMessage(1)
                    .setUserTo(this.partner.userId)
                    .setMessagesForUpdate(mess_for_update);
                this.socket.doSend(JSON.stringify(mess));
            }

        });
    }

    load(communicationId, ownerId, partnerId) {
        this.communicationId = communicationId;
        Server.getUser(ownerId).then(owner=> {
            this.owner = owner;
            Server.getUser(partnerId).then(partner=> {
                this.partner = partner;
                this.showSendMessage();
                this.print("response_element");
            })
        });
    }

    showSendMessage() {
        var additional = document.getElementById("additionalColumn");
        additional.innerHTML = '';
        var p1 = document.createElement("p");
        var textarea = document.createElement("textarea");
        textarea.setAttribute("rows", 10);
        textarea.setAttribute("cols", 30);
        textarea.id = "content_element";
        p1.appendChild(textarea);
        var p2 = document.createElement("p");
        var input = document.createElement("input");
        input.setAttribute("type", "button");
        input.value = "Отправить";
        input.id = "start_sending";
        input.addEventListener("click", evt => {
            if (textarea.value.replace(/\s/g, '').length) {
                var mess = new TechnicalMessage();//0, this.owner.userId, this.partner.userId, textarea.value, new Date(),0,"",0,1);
                mess.setTypeMessage(0)
                    .setUserFrom(this.owner.userId)
                    .setUserTo(this.partner.userId)
                    .setTextContent(textarea.value)
                    .setDate(new Date())
                    .setActive(1);
                this.socket.doSend(JSON.stringify(mess));
                // this.add(mess);
                textarea.value = "";
            }
            evt.preventDefault();
        }, false);

        p2.appendChild(input);
        additional.appendChild(p1);
        additional.appendChild(p2);
    }

    /**
     * @param {Array<Message>} messages
     */
    addAll(messages = []) {
        this.b = false;
        messages.forEach(this.add.bind(this));
    }

    add(message) {
        var usr = this.getUser(message.userId);
        if (this.tmpdate == null || message.date.toLocaleDateString() != this.tmpdate.toLocaleDateString()) {
            var div = document.createElement("div");
            div.setAttribute("class", "mess_dateheader");
            div.setAttribute("align", "center");
            var str_date = message.date.toLocaleDateString();
            var node = document.createTextNode(str_date);
            div.appendChild(node);
            this.div_content.appendChild(div);
        }
        var div_container = document.createElement("div");
        div_container.id = message.id;
        if (message.active == 1) {
            div_container.setAttribute("class", "mess_container_havent_read");
        } else {
            div_container.setAttribute("class", "mess_container");
        }
        if (this.tmpuser == message.userId && this.tmpdate.getMinutes() == message.date.getMinutes() && this.tmpdate.toLocaleDateString() == message.date.toLocaleDateString()) {
            this.b = false;
        }
        else {
            var div_mainImage = document.createElement("div");
            div_mainImage.className = "mess_mainImage";
            var image = document.createElement("img");
            image.setAttribute("src", "/files/" + usr.photoSrc);
            image.setAttribute("width", "30");
            image.setAttribute("height", "40");
            div_mainImage.appendChild(image);
            this.b = true;
            div_container.appendChild(div_mainImage);
        }
        var div_head = document.createElement("div");
        div_head.className = "mess_head";
        if (this.b) {
            var a = document.createElement("a");
            a.setAttribute("href", '/userpage?userId=' + message.userId);
            a.appendChild(document.createTextNode(usr.name));
            div_head.appendChild(a);
            div_head.appendChild(document.createTextNode(" " + message.date.toLocaleTimeString()));
        }
        div_container.appendChild(div_head);

        var div_message = document.createElement("div");
        div_message.className = "mess_message";
        var node = document.createTextNode(message.message);
        div_message.appendChild(node);
        div_container.appendChild(div_message);
        this.div_content.appendChild(div_container);
        this.tmpuser = message.userId;
        this.tmpdate = message.date;
    }
}