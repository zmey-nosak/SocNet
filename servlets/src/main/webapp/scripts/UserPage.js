/**
 * Created by Echetik on 13.12.2016.
 */
'use strict'
class UserPage {
    constructor(userId) {
        this.UserInfo = {};
        this.load(userId);
    }
    getElement() {
        return this.div_content;
    }
    print(userInfo) {
        this.targetElement = document.getElementById('response_element');
        this.targetElement.width = 180;
        this.targetElement.align = "center";
        this.targetElement.innerHTML = '';

        var div_us_photo = document.createElement("div");
        div_us_photo.className = "user_photo";
        var img = document.createElement("img");
        img.id = "userImage";
        //img.src = "data:image/jpg;base64," + userInfo.photo;
        img.src = "/files/" + userInfo.photoSrc;
        img.width = 100;
        img.height = 150;
        div_us_photo.appendChild(img);
        Server.getOwner().then(owner=> {
            if (owner != null && owner.userId != userInfo.userId) {
                var edit_btn1 = document.createElement("input");
                edit_btn1.type = "button";
                edit_btn1.value = "Написать сообщение";
                edit_btn1.addEventListener("click", evt=>modalWindow.modalShow(userInfo, owner.userId), true);
                div_us_photo.appendChild(edit_btn1);
                if (owner.userFriends.find(fr=> {
                        return fr.userId == userInfo.userId
                    }) == null) {
                    if (ownerRequest.find(req=>req == userInfo.userId) == null) {
                        var edit_btn2 = document.createElement("input");
                        edit_btn2.type = "button";
                        edit_btn2.value = "Добавить в друзья";
                        div_us_photo.appendChild(edit_btn2);
                        edit_btn2.addEventListener("click", evt=> {
                            Server.addFriend(userInfo.userId).then(t=> {
                                    var mess = new TechnicalMessage();//0, this.owner.userId, this.partner.userId, textarea.value, new Date(),0,"",0,1);
                                    mess.setTypeMessage(2)
                                        .setUserFrom(owner.userId)
                                        .setUserTo(userInfo.userId);
                                    socket.doSend(JSON.stringify(mess));
                                    location.reload();
                                }
                            );

                        });
                    } else {
                        var div = document.createElement("div");
                        div.appendChild(document.createTextNode("Заявка отправлена"));
                        div_us_photo.appendChild(div);
                    }
                } else {

                    var edit_btn2 = document.createElement("input");
                    edit_btn2.type = "button";
                    edit_btn2.value = "Удалить из друзей";
                    edit_btn2.addEventListener("click", evt=> {
                        Server.deleteFriend(userInfo.userId).then(t=> {
                            location.reload()
                        });
                    });
                    div_us_photo.appendChild(edit_btn2);
                }
            } else {

                var edit_btn = document.createElement("input");
                edit_btn.setAttribute("type","file");
                edit_btn.setAttribute("name","file");
                edit_btn.addEventListener("change", evt=> {
                    var form = new FormData();
                    var upload_file = edit_btn.files[0];
                    form.append("file", upload_file);
                    Server.putUserPhoto(userInfo.userId, form);
                    Server.getUser(owner.userId).then(own=> {
                        location.reload();
                    });

                });
                var file_upload = document.createElement("div");
                file_upload.className = "file-upload";
                var label = document.createElement("label");
                var span = document.createElement("span");
                span.appendChild(document.createTextNode("Редактировать"))
                label.appendChild(edit_btn);
                label.appendChild(span);
                file_upload.appendChild(label);
                div_us_photo.appendChild(file_upload);
            }
        });


        this.targetElement.appendChild(div_us_photo);

        var fr_container = document.createElement("div");
        fr_container.className = "userFriends";

        var div_fr_header = document.createElement("div");
        div_fr_header.className = 'user_friend'
        div_fr_header.appendChild(document.createTextNode("Друзья"));
        fr_container.appendChild(div_fr_header);

        var i = 0;
        userInfo.userFriends.forEach(
            friend=> {
                var div = document.createElement("div");
                i = i + 1;
                switch (i) {
                    case 1:
                        div.setAttribute("class","user_friend_photo_f")
                        break;
                    case 2:
                        div.setAttribute("class","user_friend_photo_l")
                        break;
                    case 3:
                        div.setAttribute("class","user_friend_photo")
                        break;
                }
                var img = document.createElement("img");
                img.setAttribute("src","/files/" + friend.photoSrc);
                img.setAttribute("width",50);
                img.setAttribute("height",60);
                var br = document.createElement("br");
                var a = document.createElement("a");
                a.setAttribute("href",'/userpage?userId=' + friend.userId);
                a.setAttribute("align",'center');
                a.appendChild(document.createTextNode(friend.surname + " " + friend.name));
                div.appendChild(img);
                div.appendChild(br);
                div.appendChild(a);
                fr_container.appendChild(div);
                if (i == 3) {
                    var clear = document.createElement("div");
                    clear.className = "clear";
                    fr_container.appendChild(clear);
                    i = 1;
                }
            }
        )


        this.targetElement.appendChild(fr_container);
        var additional = document.getElementById('additionalColumn');
        additional.innerHTML = '';
        var user_additional_info = document.createElement("div");
        user_additional_info.className = "user_info";
        var userfio = document.createElement("div");
        userfio.appendChild(document.createTextNode(userInfo.surname + " " + userInfo.name));
        user_additional_info.appendChild(userfio);
        var div = document.createElement("div");
        div.appendChild(document.createTextNode(userInfo.dateOfBirth.toLocaleDateString()));
        user_additional_info.appendChild(div);
        additional.appendChild(user_additional_info);


        if (userInfo.userBooks.length > 0) {
            div = document.createElement("div");
            div.className = "userBooks";
            userInfo.userBooks.forEach(
                book=> {
                    var img = document.createElement("img");
                    img.setAttribute("class","user_book_img")
                    img.setAttribute("src",book.imageSrc)
                    var div_clear = document.createElement("div");
                    div_clear.setAttribute("class","clear")
                    div.appendChild(img);
                    var a = document.createElement("a");
                    a.setAttribute("href","#")
                    a.appendChild(document.createTextNode(book.bookName))
                    div.appendChild(a);
                    div.appendChild(div_clear);

                });
            additional.appendChild(div);
        }
    }

    load(userId) {
        Server.getUserInfo(userId).then(userInf=> {
            this.print(userInf);
        });

    }

}