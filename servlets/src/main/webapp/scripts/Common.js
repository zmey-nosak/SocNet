/**
 * Created by Echetik on 27.11.2016.
 */
'use strict';
var modalWindow = {};
var friendList = {};
var userPage = {};
var messageList = {};
var communicationList = {};
var ownerRequest = new Array();
var friendRequest = new Array();
var socket = new MyWebSocket();
/////////////////////////////OnlineNotification////////////////////////////////////
function initWebSocket() {
    socket.webSocket.addEventListener("message", evt=> {
        var obj = JSON.parse(evt.data, function (key, value) {
            if (key == 'date') return new Date(value);
            return value;
        });
        if (obj.type == 0) {
            if (obj.user_id != obj.user_id_to)
                increaseMessCnt(1);
        }
        if (obj.type == 1) {
            var mass = obj.messages_for_update.split(',');
            decreaseMessCnt(mass.length - 1);
        }
        if (obj.type == 2) {
            increaseFriendReq(1);
        }
    }, false);
}

function setNewMess(cnt) {
    var el = document.getElementById("newMess");
    el.innerHTML = '';
    el.appendChild(document.createTextNode(cnt));
}
function setNewFriend(requests) {
    requests.forEach(it=>friendRequest.push(it));
    var el = document.getElementById("newFriends");
    el.innerHTML = '';
    el.appendChild(document.createTextNode(requests.length));
}
function decreaseMessCnt(cnt) {
    var el = document.getElementById(("newMess"));
    if (el != null && el.innerHTML.replace(/\s/g, '').length) {
        var a = Number(el.innerHTML);
        el.innerHTML = '';
        a = a - cnt;
        if (a > 0) {
            el.appendChild(document.createTextNode(a));
        }
    }
}

function increaseMessCnt(cnt) {
    var el = document.getElementById(("newMess"));
    if (el != null) {
        if (el.innerHTML.replace(/\s/g, '').length) {
            var a = Number(el.innerHTML);
            el.innerHTML = '';
            el.appendChild(document.createTextNode(a + cnt));
        }
        else {
            el.innerHTML = '';
            el.appendChild(document.createTextNode(cnt));
        }
    }
}

function decreaseFriendReq(cnt) {
    var el = document.getElementById("newFriends");
    if (el != null) {
        if (el.innerHTML.replace(/\s/g, '').length) {
            var a = Number(el.innerHTML);
            el.innerHTML = '';
            a = a - cnt;
            if (a > 0)
                el.appendChild(document.createTextNode(a));
        } else {
            el.innerHTML = '';
        }
    }
}

function increaseFriendReq(cnt) {
    var el = document.getElementById("newFriends");
    if (el != null) {
        if (el.innerHTML.replace(/\s/g, '').length) {
            var a = Number(el.innerHTML);
            el.innerHTML = '';
            a = a + cnt;
            el.appendChild(document.createTextNode(a));
        } else {
            el.innerHTML = '';
            el.appendChild(document.createTextNode(cnt));
        }
    }
}