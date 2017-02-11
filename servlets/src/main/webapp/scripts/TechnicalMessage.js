/**
 * Created by Echetik on 06.12.2016.
 */
'use strict';
/**
 * @interface
 */
class TechnicalMessage {
    constructor(/*type = 0, user_id_from, user_id_to = 0, message = "", date = new Date(), new_friend_id = 0, messages_for_update = '', active = 0*/) {
        this.type =0;//0-newMess //1-updateMess //2-newFriend
        this.userId = 0;
        this.userIdTo = 0;
        this.message = "";
        this.date = new Date();
        this.newFriendId = 0;
        this.messagesForUpdate = "";
        this.id = 0;
        this.active = 0;
    }

    /**
     * @returns {TechnicalMessage}
     */
    setTypeMessage(type) {
        this.type = type;
        return this;
    }
    /**
     * @returns {TechnicalMessage}
     */
    setUserFrom(userIdFrom) {
        this.userId = userIdFrom;
        return this;
    }
    /**
     * @returns {TechnicalMessage}
     */
    setUserTo(userIdTo) {
        this.userIdTo = userIdTo;
        return this;
    }
    /**
     * @returns {TechnicalMessage}
     */
    setTextContent(message) {
        this.message = message;
        return this;
    }
    /**
     * @returns {TechnicalMessage}
     */
    setDate(date) {
        this.date = date;
        return this;
    }
    /**
     * @returns {TechnicalMessage}
     */
    setNewFriendId(newFriendId) {
        this.newFriendId = newFriendId;
        return this;
    }
    /**
     * @returns {TechnicalMessage}
     */
    setMessagesForUpdate(messagesForUpdate) {
        this.messagesForUpdate = messagesForUpdate;
        return this;
    }
    /**
     * @returns {TechnicalMessage}
     */
    setId(id) {
        this.id = id;
        return this;
    }
    /**
     * @returns {TechnicalMessage}
     */
    setActive(active) {
        this.active = active;
        return this;
    }
}