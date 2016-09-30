import java.util.ArrayList;

/**
 * Created by WesleyLewis on 9/20/16.
 */
public class User {
    String name;
    String password;
    ArrayList<Message> messageList = new ArrayList<>();

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
    public boolean isPasswordValid(String password){
        return this.password.equals(password);
    }

}
