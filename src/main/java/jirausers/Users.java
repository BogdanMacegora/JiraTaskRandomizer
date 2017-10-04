package jirausers;

import common.ClientHttp;
import common.Config;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;

public class Users {
    private static ArrayList <User> users;
    private static HashMap <User, User> mapUsers;

    private static void initUsers() {
        users = new ArrayList<>();
        String response = ClientHttp.sendGetRequest(Config.GET_USER_URL);
        JSONArray jsonArrayUsers = new JSONObject(response).getJSONObject("users").getJSONArray("items");
        for (int i = 0; i < jsonArrayUsers.length(); i++) {
            JSONObject jUser = jsonArrayUsers.getJSONObject(i);
            String userName = jUser.get("name").toString();
            if (Config.JIRA_USERS_LIST_EXCLUDE.contains(userName))
                continue;
            String emailAddress = jUser.get("emailAddress").toString();
            String displayName = jUser.get("displayName").toString();
            User user = new User(userName, emailAddress, displayName);
            users.add(user);
        }
    }

    public static ArrayList <User> getUsers() {
        if (users == null)
            initUsers();
        return users;
    }

    public static HashMap <User, User> getRandomMapUsers() {
        mapUsers = new HashMap <User, User>();
        ArrayList <User> arrayUsers = getUsers();
        Collections.shuffle(arrayUsers);                     // randomize ArrayList.
        for (int i = 0; i < arrayUsers.size(); i = i+2)
            mapUsers.put(arrayUsers.get(i), arrayUsers.get(i + 1));
        return mapUsers;
    }
}
