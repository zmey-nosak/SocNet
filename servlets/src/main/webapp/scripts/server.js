'use strict';

const /** @type string */ METHOD = 'GET';
const /** @type string */ METHOD_POST = 'POST';
const /** @type string */ METHOD_DELETE = 'DELETE';
const /** @type string */ BASE_URL = '/openapi/';

class Server {

    /**
     * @param {string} path
     * @param {function(string)} resolve
     * @param {function(string)} reject
     */
    static getTextAsync(path, resolve, reject) {
        const /** @type XMLHttpRequest */ xhr = new XMLHttpRequest();
        xhr.open(METHOD, BASE_URL + path, true);
        //noinspection SpellCheckingInspection
        xhr.onreadystatechange = ()=> {
            if (xhr.readyState !== 4) return;
            if (xhr.status === 200)
                resolve(xhr.responseText);
            else
                reject(xhr.statusText);
        };
        xhr.send(null);

    }

    /**
     * @template T
     * @param {string} path
     * @param {function(T)} resolve
     * @param {function(Error)} reject
     */
    static getObjectAsync(path, resolve, reject, data) {
        this.getTextAsync(path,
            text=>resolve(JSON.parse(text, function (key, value) {
                if (key == 'date' || key == 'dateOfBirth') {
                    return new Date(value);
                }
                return value;
            })),
            status=>reject(Error('JSON didn\'t load successfully; error code:' + status)));
    }

    /**
     * @template T
     * @param {string} path
     * @param {function(T)} resolve
     * @param {function(Error)} reject
     */
    static getObjectAsyncPost(path, resolve, reject, data) {
        this.getTextAsyncPost(path,
            text=> {
                if (text != "")
                    resolve(JSON.parse(text, function (key, value) {
                        if (key == 'date' || key == 'dateOfBirth') {
                            return new Date(value);
                        }
                        return value;
                    }))
                return "";
            },
            status=>reject(Error('JSON didn\'t load successfully; error code:' + status)), data);
    }

    /**
     * @param {string} path
     * @param {function(string)} resolve
     * @param {function(string)} reject
     */
    static getTextAsyncPost(path, resolve, reject, data = null) {
        const /** @type XMLHttpRequest */ xhr = new XMLHttpRequest();
        xhr.open(METHOD_POST, BASE_URL + path, true);
        //  xhr.setRequestHeader("Content-Type", "application/json");
        //noinspection SpellCheckingInspection
        xhr.onreadystatechange = () => {
            if (xhr.readyState !== 4) return;
            if (xhr.status === 200 || xhr.status === 204)
                resolve(xhr.responseText);
            else
                reject(xhr.statusText);
        };
        if (data != null) {

            xhr.send(data);
        }
        else xhr.send(null);

    }

    /**
     * @template T
     * @param {string} path
     * @param {function(T)} resolve
     * @param {function(Error)} reject
     */
    static getObjectAsyncDelete(path, resolve, reject, data) {
        this.getTextAsyncDelete(path,
            text=> {
                if (text != "")
                    resolve(JSON.parse(text, function (key, value) {
                        if (key == 'date' || key == 'dateOfBirth') {
                            return new Date(value);
                        }
                        return value;
                    }))
                return "";
            },
            status=>reject(Error('JSON didn\'t load successfully; error code:' + status)), data);
    }

    /**
     * @param {string} path
     * @param {function(string)} resolve
     * @param {function(string)} reject
     */
    static getTextAsyncDelete(path, resolve, reject, data = null) {
        const /** @type XMLHttpRequest */ xhr = new XMLHttpRequest();
        xhr.open(METHOD_DELETE, BASE_URL + path, true);
        xhr.onreadystatechange = () => {
            if (xhr.readyState !== 4) return;
            if (xhr.status === 200 || xhr.status === 204)
                resolve(xhr.responseText);
            else
                reject(xhr.statusText);
        };
        if (data != null) {

            xhr.send(data);
        }
        else xhr.send(null);
    }

    /** @returns {Promise<User>} */
    static getUsers() {
        return new Promise((resolve, reject) => this.getObjectAsync('users', resolve, reject));
    }

    /**
     * @param {string} id
     * @returns Promise<User[]>
     */
    static getFriends(id) {
        return new Promise((resolve, reject) => this.getObjectAsync('users/' + id + '/friends', resolve, reject))
    }

    /**
     * @returns Promise<User>
     */
    static getUser(id) {
        return new Promise((resolve, reject) => this.getObjectAsync('users/' + id, resolve, reject));
    }

    /**
     * @param {string} id
     * @returns Promise<Communication[]>
     */
    static getUserCommunications(id) {
        return new Promise((resolve, reject) => this.getObjectAsync('users/' + id + '/communications', resolve, reject));
    }

    /**
     * @param {string} id
     * @param {string} communicationId
     * @returns Promise<UserCommunications[]>
     */
    static getUserCommunicationInfo(id, communicationId) {
        return new Promise((resolve, reject) => this.getObjectAsync('users/' + id + '/communications/' + communicationId, resolve, reject));
    }

    /**
     * @returns Promise<Message[]>
     */
    static getUserMessages(userId, communicationId) {
        return new Promise((resolve, reject) => this.getObjectAsync('users/' + userId + '/communications/' + communicationId + '/messages', resolve, reject));
    }


    static getUserInfo(userId) {
        return new Promise((resolve, reject) => this.getObjectAsync('users/' + userId + "/userInfo", resolve, reject));
    }

    static putUserPhoto(userId, object) {
        return new Promise((resolve, reject) => this.getObjectAsyncPost('users/' + userId + "/upload", resolve, reject, object));
    }

    /**
     * @param {Array<Message>} messages
     */
    static updateMessages(messages = []) {
        var json = JSON.stringify(messages);
        return new Promise((resolve, reject) => this.getObjectAsyncPost('users/updateMessages', resolve, reject, json));
    }

    static addBook(bookId) {
        return new Promise((resolve, reject) =>this.getObjectAsyncPost('users/books/' + bookId, resolve, reject));
    }

    static getUserBooks(userId) {
        return new Promise((resolve, reject) =>this.getObjectAsync('users/' + userId + '/books', resolve, reject));
    }

    static getOwner() {
        return new Promise((resolve, reject) =>this.getObjectAsync('users/owner', resolve, reject));
    }

    static deleteFriend(friendId) {
        return new Promise((resolve, reject) =>this.getObjectAsyncDelete('users/deleteFriend/' + friendId, resolve, reject));
    }

    static addFriend(friendId) {
        return new Promise((resolve, reject) =>this.getObjectAsyncPost('users/friends/add/' + friendId, resolve, reject));
    }

    static getFriendRequests() {
        return new Promise((resolve, reject) =>this.getObjectAsync('users/friendRequests', resolve, reject));
    }

    static getOwnerRequests() {
        return new Promise((resolve, reject) =>this.getObjectAsync('users/ownerRequests', resolve, reject));
    }

    static getUnreadMessCnt() {
        return new Promise((resolve, reject) =>this.getObjectAsync('users/unreadMessCnt', resolve, reject));
    }

    static getFriendReqDetail() {
        return new Promise((resolve, reject) =>this.getObjectAsync('users/friendReqDetail', resolve, reject));
    }

    static activateFriendShip(ownerId, friendId) {
        return new Promise((resolve, reject) =>this.getObjectAsyncPost('users/' + ownerId + '/friendship/' + friendId + '/activate', resolve, reject));
    }

    static getAllAuthors(limit, offset) {
        return new Promise((resolve, reject) =>this.getObjectAsync('authors?limit=' + limit + '&offset=' + offset, resolve, reject));
    }
}