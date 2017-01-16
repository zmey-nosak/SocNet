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
        this.user_id = 0;
        this.user_id_to = 0;
        this.message = "";
        this.date = new Date();
        this.new_friend_id = 0;
        this.messages_for_update = "";
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
    setUserFrom(user_id_from) {
        this.user_id = user_id_from;
        return this;
    }
    /**
     * @returns {TechnicalMessage}
     */
    setUserTo(user_id_to) {
        this.user_id_to = user_id_to;
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
    setNewFriendId(new_friend_id) {
        this.new_friend_id = new_friend_id;
        return this;
    }
    /**
     * @returns {TechnicalMessage}
     */
    setMessagesForUpdate(messages_for_update) {
        this.messages_for_update = messages_for_update;
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