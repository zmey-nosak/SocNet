/**
 * Created by Echetik on 21.11.2016.
 */
'use strict';

class FriendList {
    constructor(userId) {
        this.ownerId = userId;
        this.identification = "friends";
        socket.webSocket.addEventListener("message", evt=> {
            var obj = JSON.parse(evt.data, function (key, value) {
                if (key == 'date') return new Date(value);
                return value;
            });
            if (obj.type == 2 && flag == 1) {
                this.handle(obj);
            }
        }, false);
        this.flag = 0;//0-sendMessage, 1-activateFriendShip
    }

    init(id) {
        this.targetElement = document.getElementById(id);
        this.targetElement.innerHTML = '';
        this.div_content = document.createElement("div");
        this.div_content.className = "comm_prokrutka";
        this.div_content.id = "response_element_" + this.identification;
        this.targetElement.appendChild(this.div_content);
    }

    printFriends(id) {
        this.init(id);
        Server.getFriends(this.ownerId).then(friends=>this.addAll(friends));
    }

    printAllUsers(id) {
        this.init(id);
        Server.getUsers().then(users=>this.addAll(users));
    }

    printFriendRequest(id) {
        this.init(id);
        this.flag = 1;
        Server.getFriendReqDetail().then(users=>this.addAll(users));
    }

    activateFriendship(friendId) {
        Server.activateFriendShip(this.ownerId, friendId);
    }

    add(friend) {
        var additional = document.getElementById("additionalColumn");
        additional.innerHTML = '';
        var div_container = document.createElement("div");
        div_container.className = "comm_container";
        var div_main_img = document.createElement("div");
        div_main_img.className = "comm_mainImage";
        var img = document.createElement("img");
        img.src = "/files/" + friend.photoSrc;
        img.width = 40;
        img.height = 50;
        img.id = "image_" + friend.userId;
        div_main_img.appendChild(img);
        div_container.appendChild(div_main_img);

        var div_head = document.createElement("div");
        div_head.className = "comm_head";
        var a = document.createElement("a");
        a.setAttribute("href", "/userpage?userId=" + friend.userId);

        var txt_node = document.createTextNode(friend.surname + " "
            + friend.name);
        a.appendChild(txt_node);
        div_head.appendChild(a);
        div_container.appendChild(div_head);

        var div_mess = document.createElement("div");
        div_mess.className = "comm_message";

        var txt_node2;
        /** @type HTMLLinkElement */var a = document.createElement("a");
        if (this.flag == 1) {
            txt_node2 = document.createTextNode("Утвердить заявку");
            a.setAttribute("href", "#");
            a.addEventListener("click", evt=> {
                Server.activateFriendShip(this.ownerId, friend.userId).then(evt=> {
                    div_mess.innerHTML = '';
                    div_mess.appendChild(document.createTextNode("Заявка одобрена"));
                    var el = document.getElementById("newFriends");
                    if (el != null) {
                        if (el.innerHTML.replace(/\s/g, '').length) {
                            var cnt = Number(el.innerHTML);
                            el.innerHTML = '';
                            cnt = cnt - 1;
                            if (cnt > 0)
                                el.appendChild(document.createTextNode(cnt));
                        } else {
                            el.innerHTML = '';
                        }
                    }
                })
            });
        } else {
            txt_node2 = document.createTextNode("Написать сообщение");
            a.setAttribute("href", "#");
            a.addEventListener("click", evt=>modalWindow.modalShow(friend, this.ownerId), true);
        }

        a.appendChild(txt_node2);
        div_mess.appendChild(a);
        div_container.appendChild(div_mess);
        this.div_content.appendChild(div_container);

    }

    /**
     * @param {Array<User>} friends
     */
    addAll(friends = []) {
        friends.forEach(this.add.bind(this));
    }

    handle(obj) {
        if (document.getElementById(("response_element_" + this.identification)) != null) {
            this.add(obj);
        }
    }
}