/**
 * Created by Echetik on 27.11.2016.
 */
'use strict';
/**
 * @interface
 */
class Common {
    /**
     * @param String
     */
    static getTemplate(url) {
        if (url.indexOf("/list/messages")!=-1) {

            var link =document.createTextNode("<link rel='stylesheet' href=/lists.css type='text/css'>");
            document.head.appendChild(link);

        }
    }
}