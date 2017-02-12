/**
 * Created by Echetik on 12.02.2017.
 *//*
'use strict'
class Paginator {
   constructor() {
       window.Panel(
            {
                inrow: 6,
                items: 28,
                step: 3,
                current: 14,
                callback: clickPanel2,
                element: document.getElementById('page_panel2')
            }
        );
    }


    clickPanel2(page) {
        alert('clickPanel2, Page: ' + page);
    }



     () {
        function Panel(params) {
            if (!(this instanceof Panel)) {
                return new Panel(params);
            }
            this.initialize.apply(this, arguments);
        }

        Panel.prototype.initialize = function (arg) {
            var fragment = document.createDocumentFragment();
            var first = document.createElement('span');
            var ul = document.createElement('ul');
            var li = document.createElement('li');
            var last = first.cloneNode();
            var prev = first.cloneNode();
            var next = first.cloneNode();
            var callback = arg.callback;
            var element = arg.element;
            var ins_li, end;

            first.className = 'first';
            last.className = 'last';
            prev.className = 'prev';
            next.className = 'next';
            arg.step = arg.step || 1;
            arg.inrow = arg.inrow || 5;
            arg.items = arg.items || 25;
            arg.current = arg.current || 1;
            arg.prev_txt = arg.prev_txt || '<';
            arg.next_txt = arg.next_txt || '>';
            arg.last_txt = arg.last_txt || '>>';
            arg.first_txt = arg.first_txt || '<<';
            var show_button = arg.items > arg.inrow;
            if (show_button) {
                first.appendChild(document.createTextNode(arg.first_txt));
                last.appendChild(document.createTextNode(arg.last_txt));
                prev.appendChild(document.createTextNode(arg.prev_txt));
                next.appendChild(document.createTextNode(arg.next_txt));
            }
            element.className = 'page_panel';
            var start = Math.floor((arg.current - 1) / arg.inrow) * arg.inrow;

            function build(start) {
                for (end = arg.inrow + start; ++start <= end;) {
                    if (start > arg.items) break;
                    ins_li = li.cloneNode();
                    if (start == arg.current) {
                        ins_li.className = 'active';
                    }
                    ins_li.innerHTML = start;
                    fragment.appendChild(ins_li);
                }
                ul.innerHTML = '';
                ul.appendChild(fragment);
                if (show_button) {
                    fragment.appendChild(first);
                    fragment.appendChild(prev);
                }
                fragment.appendChild(ul);
                if (show_button) {
                    fragment.appendChild(next);
                    fragment.appendChild(last);
                }
                element.appendChild(fragment);
            }

            build(start);

            element.onclick = function (e) {
                var el = e ? e.target : window.event.srcElement;
                switch (el.tagName) {
                    case 'LI':
                        if (arg.current == +el.innerHTML) return;
                        var list = el.parentNode.children;
                        for (var i = 0; i < list.length; i++) {
                            list[i].className = list[i] == el ? 'active' : '';
                        }
                        arg.current = +el.innerHTML;
                        if (callback) callback(arg.current);
                        break;
                    case 'SPAN':
                        switch (el.className) {
                            case 'first':
                                if (start !== 0) {
                                    start = 0;
                                    build(start);
                                }
                                break;
                            case 'last':
                                end = arg.items - arg.inrow;
                                if (start != end) {
                                    start = end;
                                    build(start);
                                }
                                break;
                            case 'next':
                                start += arg.step;
                                if (start >= arg.items - arg.inrow) {
                                    start = arg.items - arg.inrow;
                                }
                                build(start);
                                break;
                            case 'prev':
                                if (start < arg.step) {
                                    start = 0;
                                } else {
                                    start -= arg.step;
                                    if (start >= arg.items - arg.inrow) {
                                        start = arg.items - arg.inrow - arg.step;
                                    }
                                }
                                build(start);
                                break;
                        }
                        break;
                }
            };
        };
        window.Panel = Panel;
    }

())
    ;

}*/