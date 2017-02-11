'use strict';
/**
 * Created by Echetik on 09.12.2016.
 */
class ModalWindow {
    constructor() {
        this.m = document.getElementById('modal_window');
        this.p = document.getElementById('page');
        this.mOverlay = this.getId('modal_window');
        this.mClose = this.getId('modal_close');
        this.modal = this.getId('modal_holder');
        this.friend = {};
        this.user_id = 0;
        this.textarea = document.getElementById('modal_textarea');
        this.snd_btn = this.getId('snd_btn');
        this.snd_btn.addEventListener('click', evt=> {
            var mess = new TechnicalMessage();//0, this.ownerId, this.friend.userId, this.textarea.value, new Date());
            mess.setTypeMessage(0)
                .setUserFrom(this.ownerId)
                .setUserTo(this.friend.userId)
                .setTextContent(this.textarea.value)
                .setDate(new Date());
            socket.doSend(JSON.stringify(mess));
            this.textarea.value = "";
            evt.preventDefault();
            if (this.modalOpen && ( !evt.keyCode || evt.keyCode === 27 )) {
                this.mOverlay.setAttribute('aria-hidden', 'true');
                this.modal.setAttribute('tabindex', '-1');
                this.modalOpen = false;
                this.lastFocus.focus();
            }
        }, true);
        this.allNodes = document.querySelectorAll("*");
        this.user_id = 0;
        // restrict tab focus on elements only inside modal window
        var i = 0;
        for (i = 0; i < this.allNodes.length; i++) {
            this.allNodes.item(i).addEventListener('focus', this.focusRestrict);
        }
        this.modalOpen = false;
        this.lastFocus;
        this.mOverlay.addEventListener('click',
            function (e) {
                if (e.target == this.modal.parentNode) {
                    this.modalClose(e);
                }
            }, false);
        this.mClose.addEventListener('click', evt=> {
            if (this.modalOpen && ( !evt.keyCode || evt.keyCode === 27 )) {
                this.mOverlay.setAttribute('aria-hidden', 'true');
                this.modal.setAttribute('tabindex', '-1');
                this.modalOpen = false;
                this.lastFocus.focus();
            }
        });
        document.addEventListener('keydown', evt=> {
            if (this.modalOpen && ( !evt.keyCode || evt.keyCode === 27 )) {
                this.mOverlay.setAttribute('aria-hidden', 'true');
                this.modal.setAttribute('tabindex', '-1');
                this.modalOpen = false;
                this.lastFocus.focus();
            }
        });

    };

    swap() {
        this.p.parentNode.insertBefore(this.m, this.p);
    }


    getId(id) {
        return document.getElementById(id);
    }


    // Let's open the modal
    modalShow(friend, userId) {
        this.friend = friend;
        this.ownerId = userId;
        var title = document.getElementById("modal_user");
        title.innerHTML = '';
        title.appendChild(document.createTextNode(friend.surname + " " + friend.name));
        var img = document.getElementById("modal_mainImage");
        img.src = "/files/" + friend.photoSrc;
        this.lastFocus = document.activeElement;
        this.mOverlay.setAttribute('aria-hidden', 'false');
        this.modalOpen = true;
        this.modal.setAttribute('tabindex', '0');
        this.modal.focus();
    }

    focusRestrict(event) {
        if (this.modalOpen && !this.modal.contains(event.target)) {
            event.stopPropagation();
            this.modal.focus();
        }
    }
}