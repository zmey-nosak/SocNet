/**
 * Created by Echetik on 21.11.2016.
 */
'use strict';

class Authors {
    constructor(offset) {
        this.offset = offset;
    }

    fillTable(id, itemsOnPage) {
        Server.getAllAuthors(itemsOnPage, this.offset).then(authors=> {
            var pageCount = authors.total / itemsOnPage;

            var table = document.getElementById(id);
            if (table != null) {
                authors.objects.forEach(auth=> {
                    var tr = document.createElement("tr");
                    var td = document.createElement("td");
                    var a = document.createElement("a");
                    a.href = "/books?authorId=" + auth.authorId;
                    a.appendChild(document.createTextNode(auth.surname + " " + auth.name));
                    td.appendChild(a);
                    tr.appendChild(td);
                    td = document.createElement("td");
                    td.appendChild(document.createTextNode(auth.stringDob));
                    tr.appendChild(td);
                    table.appendChild(tr);
                })

            }
            var links = document.getElementById("links");

            if (pageCount > 1) {
                var a = document.createElement("a");
                a.appendChild(document.createTextNode("<PREVIOUS PAGE"));
                a.href = "/authors?offset=" + (this.offset - itemsOnPage);
                links.appendChild(a);
                if ((this.offset - itemsOnPage) >= 0) {

                }

                var a1 = document.createElement("a");
                a1.appendChild(document.createTextNode("        NEXT PAGE>"));
                a1.href = "/authors?offset=" + (this.offset + itemsOnPage);
                links.appendChild(document.createTextNode("                 "));
                links.appendChild(a1);
                if ((this.offset + itemsOnPage) < authors.total) {

                }

            }
        })
    }
}