/**
 * Created by Echetik on 03.12.2016.
 */
'use strict';
/**
 * @interface
 */
class Message {
    constructor() {
        this.message = "";
        this.user_id = 0;
        this.active = 0;
        this.date = new Date();
        this.groupNum = 0;
    }
}