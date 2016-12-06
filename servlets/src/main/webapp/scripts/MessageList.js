/**
 * Created by Echetik on 03.12.2016.
 */

'use strict';
/**
 * @interface
 */
class MessageList {
    constructor(communication_id, owner_id, partner_id) {
        Server.getUser(owner_id.toString()).then(own=>this.owner = own);
        Server.getUser(partner_id.toString()).then(part=>this.partner = part);
        this.communication_id = communication_id;
        this.targetElement = "";
        this.tmpuser = 0;
        this.tmpdate = new Date();
        this.b = false;
    }

    /*
     @return User
     */
    getUser(user_id) {
        if (this.owner.user_id == user_id) {
            return this.owner;
        } else {
            return this.partner;
        }
    }

    print(id, mess) {
        this.targetElement = document.getElementById(id);
        this.targetElement.innerHTML = '';
        Server.getUserMessages(this.user_id, this.communication_id).then(messages=>this.refresh(messages));
        this.targetElement.scrollTop = this.targetElement.scrollHeight;
    }

    addToElem(id, mess) {
        this.targetElement = document.getElementById(id);
        this.add(mess);
        this.targetElement.scrollTop = this.targetElement.scrollHeight;
    }

    /**
     * @param {Array<Message>} messages
     */
    refresh(messages = []) {
        this.tmpuser = 0;
        this.tmpdate = new Date();
        this.b = false;
        messages.forEach(this.add.bind(this));

    }


    add(message) {
        var usr = this.getUser(message.user_id);
        if (message.groupNum == 1) {
            var div = document.createElement("div");
            div.setAttribute("class", "dateheader");
            div.setAttribute("align", "center");
            var str_date = message.date.getDay() + "." + (message.date.getMonth() + 1) + "." + message.date.getYear();
            var node = document.createTextNode(str_date);
            div.appendChild(node);
            this.targetElement.appendChild(div);
        }
        var div_container = document.createElement("div");
        div_container.setAttribute("class", "container");
        this.targetElement.appendChild(div_container);
        if (this.tmpuser == message.user_id && this.tmpdate == message.date) {
            this.b = false;
        }
        else {
            var div_mainImage = document.createElement("div");
            div_mainImage.className = "mainImage";
            var image = document.createElement("image");
            image.setAttribute("src", "data:image/jpg;base64," + usr.photo);
            image.setAttribute("width", "30");
            image.setAttribute("height", "40");
            div_mainImage.appendChild(image);
            this.b = true;
            div_container.appendChild(div_mainImage);
        }
        var div_head = document.createElement("div");
        div_head.className = "head";
        if (this.b) {
            var node = document.createTextNode(usr.i_name + "   " + message.date.getHours() + ":" + message.date.getMinutes());
            div_head.appendChild(node);
        }
        div_container.appendChild(div_head);

        var div_message = document.createElement("div");
        div_message.className = "message";
        var node = document.createTextNode(message.message);
        div_message.appendChild(node);
        div_container.appendChild(div_message);
        this.tmpuser = message.user_id;
        this.tmpdate = message.date;
    }
}