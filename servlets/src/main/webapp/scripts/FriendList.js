/**
 * Created by Echetik on 21.11.2016.
 */
 'use strict';

 class FriendList {

    /**
 * @param {string} id
 */
constructor(id="instancies") {
    /**
     * @private
     * @type HTMLOListElement
     */
    this.targetElement = document.getElementById(id);
}

/**
 * @param {User} friend
 */
add(friend) {
    //noinspection JSValidateTypes
    const /** @type HTMLLIElement */ instanceLi = document.createElement("li");
    instanceLi.appendChild(document.createTextNode(friend.f_name));
    this.targetElement.appendChild(instanceLi);
}

/**
 * @param {Array<User>} friends
 */
addAll(friends=[]) {
    friends.forEach(this.add.bind(this));
}
}