import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

/**
 * Created by WesleyLewis on 9/20/16.
 */
public class Messages {
    static User user;
    static Message message;
    static HashMap<String, User> users = new HashMap<>();

    public static void main(String[] args) {
        Spark.init();

        Spark.get(
                "/",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("userName");
                    User user = users.get(name);

                    HashMap m = new HashMap();
                    if (user == null) {
                        return new ModelAndView(m, "index.html");
                    } else {
                        return new ModelAndView(user, "messages.html");
                    }
                }),
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/create-user",
                ((request, response) -> {
                    String userName = request.queryParams("userName");
                    String password = request.queryParams("password");
                    if (users.containsKey(userName)) {

                        if (users.get(userName).isPasswordValid(password)) {
                            Session session = request.session();
                            session.attribute("userName", userName);
                            response.redirect("/");
                        } else {
                            response.redirect("/");
                        }
                    } else {
                        user = new User(userName, password);
                        users.put(userName, user);
                        Session session = request.session();
                        session.attribute("userName", userName);
                        response.redirect("/");
                        return "";
                    }
                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/create-message",
                (((request, response) -> {
                    String userMessage = request.queryParams("userMessage");
                    message = new Message(userMessage);
                    user.messageList.add(message);
                    response.redirect("/");
                    return "";
                }))
        );
        Spark.post("/edit-message", ((request, response) -> {
                    int selectedMessage = Integer.parseInt(request.queryParams("selectMessageEdit"));
                    String editMessage = request.queryParams("textMessageEdit");
                    user.messageList.set(selectedMessage-1, new Message(editMessage));
                    response.redirect("/");
                    return "";
                })
        );
        Spark.post("/delete-message", ((request, response) -> {
                    int selectedMessage = Integer.parseInt(request.queryParams("deleteMessage"));
                    user.messageList.remove(selectedMessage-1);
                    response.redirect("/");
                    return "";
                })
        );
        Spark.post(
                "/logout",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                })
        );

    }
}
