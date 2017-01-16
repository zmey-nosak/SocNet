package tags;

/**
 * Created by Echetik on 07.01.2017.
 */
public class FriendRequest implements Printable {
    private int userId;

    public FriendRequest(int userId) {
        this.userId = userId;
    }


    public String getString() {
        return null;
    }


    @Override
    public String getScript() {
        return "friendList = new FriendList(" + this.userId + ");" +
                "friendList.printFriendRequest('response_element');" ;
    }

}
