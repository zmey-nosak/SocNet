package tags;

/**
 * Created by Echetik on 07.01.2017.
 */
public class UserPage implements Printable {

    private int userId;

    public UserPage(int userId) {
        this.userId = userId;
    }

    @Override
    public String getString() {
        return null;
    }

    @Override
    public String getScript() {
        return "userPage = new UserPage(" + this.userId + ");" +
                "modalWindow = new ModalWindow();" ;
    }
}
