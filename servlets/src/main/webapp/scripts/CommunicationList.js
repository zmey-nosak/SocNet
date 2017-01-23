/**
 * Created by Echetik on 27.11.2016.
 */
'use strict';

class CommunicationList {

    constructor(user_id = 0) {
        this.owner_id = user_id;
        this.identification = "communication";
        this.print('response_element');
        socket.webSocket.addEventListener("message", evt=> {
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
                this.print('response_element');
            }
        } 
    }

    getElement() {
        return this.div_content;
    }

    add(communication) {
        var div_container = document.createElement("div");
        div_container.className = "comm_container";
        var div_main_img = document.createElement("div");
        div_main_img.className = "comm_mainImage";
        var img = document.createElement("img");
        img.src = "/files/" + communication.photo_src;
        img.width = 40;
        img.height = 50;
        div_main_img.appendChild(img);
        div_container.appendChild(div_main_img);

        var div_head = document.createElement("div");
        div_head.className = "comm_head";
        var txt_node = document.createTextNode(communication.f_name + " "
            + communication.i_name
            + " "
            + communication.date.toLocaleDateString());

        div_head.appendChild(txt_node);
        div_container.appendChild(div_head);

        var div_child_img = document.createElement("div");
        div_child_img.className = "comm_childImage";
        var ch_img = document.createElement("img");
        ch_img.src = "/files/" + communication.ownerPhoto_src;
        ch_img.width = 20;
        ch_img.height = 25;
        div_child_img.appendChild(ch_img);
        div_container.appendChild(div_child_img);

        var div_mess = document.createElement("div");
        div_mess.className = "comm_message";

        /** @type HTMLLinkElement */var a = document.createElement("a");
        var txt_node2 = document.createTextNode(communication.message);
        a.setAttribute("href", "/messages?userId=" + this.owner_id + "&communicationId=" + communication.communication_id + "&partnerId=" + communication.partner);
        a.appendChild(txt_node2);
        div_mess.appendChild(a);
        div_container.appendChild(div_mess);
        this.div_content.appendChild(div_container);
    }

    /**
     * @param {Array<Communication>} communications
     */
    addAll(communications = []) {
        communications.forEach(this.add.bind(this));
    }

    print(id) {
        this.targetElement = document.getElementById(id);
        this.targetElement.innerHTML = '';
        document.getElementById("additionalColumn").innerHTML = '';
        this.div_content = document.createElement("div");
        this.div_content.className = "comm_prokrutka";
        this.div_content.id = "response_element_" + this.identification;
        this.targetElement.appendChild(this.div_content);
        Server.getUserCommunications(this.owner_id).then(communications=>this.addAll(communications));
        this.targetElement.scrollTop = this.targetElement.scrollHeight;
    }

}
