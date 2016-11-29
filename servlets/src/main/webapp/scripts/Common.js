/**
 * Created by Echetik on 27.11.2016.
 */
'use strict';
/**
 * @interface
 */
class Common {
    /**
     * @returns String
     */
    static getTemplate(){
    return
    '<html><head><link rel="stylesheet" href="/main_style.css"><script src="/websocket.js"></script>' +
    '<script>addEventListener("DOMContentLoaded", ()=>new MyWebSocket("mess", "", ""), false);</script></head>' +
    '<body class=body text=#666564><p align="right" font-size="14">' +
    '            <a class="A" align="right" href=#>Выход</a></p>' +
    '            <table class=table cellspacing="8px">' +
    '               <tr class=tr>' +
    '                   <td width="10%" valign="top">' +
    '                       <table>' +
    '                           <tr>' +
    '                               <td><a class="A" href="/index.html">На главную</a></td>' +
    '                               <td width=5px></td>' +
    '                           </tr>' +
    '                           <tr>' +
    '                               <td><a class="A" href="/userlist/">Мои друзья</a></td>' +
    '                               <td width=5px></td>' +
    '                           </tr>' +
    '                           <tr>' +
    '                               <td><a class="A" href="/index.html">Сообщения</a></td>' +
    '                               <td width=5px id="mess"></td>' +
    '                           </tr>' +
    '                           <tr>' +
    '                               <td><a class="A" href="/index.html">Сообщество</a></td>' +
    '                               <td width=5px></td>' +
    '                           </tr>' +
    '                       </table>' +
    '                   </td>' +
    '                   <td class=td cellpadding=0 cellspacing=0 align=center valign=top width=20%>' +
    '                       <table width=100% padding=0px margin=0px border="1" id>'+
    '                       </table>' +
    '                   </td>' +
    '               </tr>' +
    '           </table>'

     }
    }