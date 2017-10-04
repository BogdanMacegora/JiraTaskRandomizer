package common;

import org.apache.commons.codec.binary.Base64;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;


public class Config {

    public static String API_URL;
    public static String USERS_GROUP_NAME;
    public static String JIRA_PROJECT_NAME;
    public static String CREDENTIALS;
    public static String AUTHORIZATION;
    public static String ISSUE_TYPE_APP_PACKAGE;
    public static String ISSUE_TYPE_APP_CHANGE;
    public static String GET_USER_URL;
    public static String JIRA_FILED_PACKAGER;
    public static String JIRA_FILED_QA;
    public static String JIRA_APP_NAME;
    public static String JIRA_APP_VERSION;
    public static String JIRA_APP_VENDOR;
    public static String JIRA_ID_READY_FOR_PACKAGING;
    public static ArrayList <String> JIRA_USERS_LIST_EXCLUDE;

    static {
        Properties properties = new Properties();
        try (InputStream inputStream = new FileInputStream("config.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            AlertMessage.showErrorMessage("Error while read \"config.properties\" file. \n" +
                    "Please check that the file is located near with executable file.", e);
        }
        API_URL = properties.getProperty("API_URL").trim();
        USERS_GROUP_NAME = properties.getProperty("USERS_GROUP_NAME").trim();
        GET_USER_URL = API_URL + "/group?groupname=" + USERS_GROUP_NAME + "&expand=users";
        JIRA_PROJECT_NAME = properties.getProperty("JIRA_PROJECT_NAME").trim();
        ISSUE_TYPE_APP_PACKAGE = properties.getProperty("ISSUE_TYPE_APP_PACKAGE").trim();
        ISSUE_TYPE_APP_CHANGE = properties.getProperty("ISSUE_TYPE_APP_CHANGE").trim();
        JIRA_FILED_PACKAGER = properties.getProperty("JIRA_FILED_PACKAGER").trim();
        JIRA_FILED_QA = properties.getProperty("JIRA_FILED_QA").trim();
        JIRA_APP_NAME = properties.getProperty("JIRA_APP_NAME").trim();
        JIRA_APP_VERSION = properties.getProperty("JIRA_APP_VERSION").trim();
        JIRA_APP_VENDOR = properties.getProperty("JIRA_APP_VENDOR").trim();
        JIRA_ID_READY_FOR_PACKAGING = properties.getProperty("JIRA_ID_READY_FOR_PACKAGING").trim();

        CREDENTIALS = properties.getProperty("CREDENTIALS").trim();
        byte[] bytesEncoded = Base64.encodeBase64(CREDENTIALS.getBytes());
        AUTHORIZATION = "Basic " + new String(bytesEncoded);

        String[]usersExcludeArray = properties.getProperty("JIRA_USERS_LIST_EXCLUDE").split(",");
        JIRA_USERS_LIST_EXCLUDE = new ArrayList<String>();
        for (String username : usersExcludeArray) {
            JIRA_USERS_LIST_EXCLUDE.add(username.trim());
        }
    }
}
