/**
 * Created by Echetik on 27.11.2016.
 */
'use strict';

/**
 * @interface
 */
class Communication {
    constructor() {
        this.communication_id = 0;
        this.message = "";
        this.user_from = 0;
        this.f_name = "";
        this.i_name = "";
        this.photo = "";
        this.ownerPhoto = "";
        this.active = 1;
        this.date = new Date();
        this.partner = 0;

    }
}