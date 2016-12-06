/**
 * Created by Echetik on 06.12.2016.
 */
'use strict';
/**
 * @interface
 */
class TechnoMessage {
    constructor(user_id_to = 0, message = "", date = new Date()) {
        this.user_id_to = user_id_to;
        this.message = message;
        this.date = date;
    }
}