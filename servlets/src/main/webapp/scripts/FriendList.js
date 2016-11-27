/**
 * Created by Echetik on 21.11.2016.
 */
'use strict';

class FriendList {

    /**
     * @param {string} id
     */
    constructor(id = "tab") {
        /**
         * @private
         * @type HTMLTableElement
         */
        this.targetElement = document.getElementById(id);
    }

    /**
     * @param {User} friend
     */
    add(friend) {
        var wr = '<table><tr><td height=150 align=\"center\"><image src=data:image/jpg;base64,'
            + friend.photo
            + ' width=\"100\" height=\"150\"/></td><td>'
            + '<a href=\"/userpage/?user_id=' + friend.user_id + '"\">'
            + friend.f_name + ' ' + friend.i_name + '</a>'
            + '<br><INPUT TYPE=button VALUE=\"Send Message\" ONCLICK=\"showParameters(\"' + friend.photo + '\",'
            + '\"' + friend.user_id + '\",'
            + '\"' + friend.f_name + ' ' + friend.i_name + '\");>'
            + '</td></tr></table>';

        //noinspection JSValidateTypes
        // var row = this.targetElement.insertRow();
        //  var cell = row.insertCell(0);
        //var cell_1 = row.insertCell(1);
        //const /** @type HTMLImageElement */ img = document.createElement("img");
        //img.src = 'data:image/jpg;base64,' + friend.photo;
        //img.width = '50';
        //img.height = '75';
        //cell.appendChild(img);
        //var link = document.createElement("a");
        //link.href = '/userpage/id=' + friend.user_id;
        // var link_text = document.createTextNode(friend.i_name + ' ' + friend.f_name);
        //link.appendChild(link_text);
        //cell_1.appendChild(link);
        document.write(wr);
    }

    /**
     * @param {Array<User>} friends
     */
    addAll(friends = []) {
        friends.forEach(this.add.bind(this));
    }
}