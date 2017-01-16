/**
 * Created by Echetik on 19.12.2016.
 */
'use strict';
/**
 * @interface
 */
class TechnicalNewFriend {
    constructor(user_id_from=0, user_id_to = 0) {
        this.user_id = user_id_from;
        this.user_id_to = user_id_to;
    }
}