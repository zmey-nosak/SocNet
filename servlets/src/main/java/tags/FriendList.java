package tags;

/**
 * Created by Echetik on 07.01.2017.
 */
public class FriendList implements Printable {
    private int userId;

    public FriendList(int userId) {
        this.userId = userId;
    }


    public String getString() {
        return null;
    }


    @Override
    public String getScript() {
        return "friendList = new FriendList(" + this.userId + ");" +
                "friendList.printFriends('response_element');" +
                "modalWindow = new ModalWindow();" ;
    }

}
