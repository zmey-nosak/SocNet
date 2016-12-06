/**
 * Created by Echetik on 27.11.2016.
 */
'use strict';

class CommunicationList {

    /**
     * @param {string} id
     */
    constructor(id = "output") {
        /**
         * @private
         * @type HTMLTableElement
         */
        this.targetElement = document.getElementById(id);
    }

    /**
     * @param {Communication} communication
     */
    add(communication) {


        //noinspection JSValidateTypes
        var row = this.targetElement.insertRow();
        var cell = row.insertCell(0);
        var cell1 = row.insertCell(1);
        const /** @type HTMLImageElement */ img = document.createElement("img");
        img.src = 'data:image/jpg;base64,' + communication.photo;
        img.width = '50';
        img.height = '75';
        cell.appendChild(img);
        var div = document.createElement("div")
        var fio = document.createTextNode(communication.f_name);
        div.appendChild(fio);
    }

    /**
     * @param {Array<Communication>} communications
     */
    addAll(communications = []) {
        communications.forEach(this.add.bind(this));
    }
}
