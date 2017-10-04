package toolkit;

import common.ClientHttp;
import common.Config;
import issues.Issue;
import jirausers.User;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class JiraToolkit {

    public void assignIssue(String issueKey, String assignee) {
        String url = Config.API_URL + "/issue/" + issueKey;
        String data = "{ \"fields\": { \"assignee\":{\"name\":\"" + assignee + "\"}}}";
        String response = ClientHttp.sendPutRequest(url, data).toString();
        if (!response.contains("204 No Content"))
            throw new RuntimeException("Issue has not been assigned: " + response);
    }

    public void setPackager(String issueKey, String packager) {
        String url = Config.API_URL + "/issue/" + issueKey;
        String data = "{ \"fields\": { \""+ Config.JIRA_FILED_PACKAGER +"\":{\"name\":\"" + packager + "\"}}}";
        String response = ClientHttp.sendPutRequest(url, data).toString();
        if (!response.contains("204 No Content"))
            throw new RuntimeException("Packager has not been assigned: " + response);
    }

    public void setQAengineer(String issueKey, String qaEngineer) {
        String url = Config.API_URL + "/issue/" + issueKey;
        String data = "{ \"fields\": { \""+ Config.JIRA_FILED_QA +"\":{\"name\":\"" + qaEngineer + "\"}}}";
        String response = ClientHttp.sendPutRequest(url, data).toString();
        if (!response.contains("204 No Content"))
            throw new RuntimeException("QA Engineer has not been assigned: " + response);
    }

    public String createSubIssue(String project, String parent, String type, String summary, String description,
                              String appName, String appVersion, String appVendor, String duedate) {
        String url = Config.API_URL + "/issue/";
        StringBuilder data = new StringBuilder();
        data.append("{ \"fields\":{" +
                "\"project\":{\"key\":\""+ project + "\"}," +
                " \"parent\":{\"key\":\""+ parent+ "\"}," +
                "\"summary\":\"" + summary + "\"," +
                "\"description\":\"" + description + "\"," +
                "\"issuetype\": {\"name\": \"" + type + "\"}," +
                "\"" + Config.JIRA_APP_NAME + "\": \"" + appName + "\"," +
                "\"" + Config.JIRA_APP_VERSION + "\": \"" + appVersion  + "\"," +
                "\"" + Config.JIRA_APP_VENDOR + "\": \"" + appVendor + "\"");
        if (!duedate.equals(""))
            data.append(", \"duedate\":\"" + duedate + "\"");
        data.append("}}");
        try {
            HttpResponse response = ClientHttp.sendPostRequest(url, data.toString());
            String responseEntity = EntityUtils.toString(response.getEntity());
            return new JSONObject(responseEntity).get("key").toString();
        } catch (IOException e) {
            throw new RuntimeException("Error while creating issue: " + e.getMessage());
        }
    }

    public String createIssue(String project, String type, String summary, String description,
                              String appName, String appVersion, String appVendor, String duedate) {
        String url = Config.API_URL + "/issue/";
        StringBuilder data = new StringBuilder();
        data.append("{ \"fields\":{" +
                    "\"project\":{\"key\":\""+ project + "\"}," +
                    "\"summary\":\"" + summary + "\"," +
                    "\"description\":\"" + description + "\"," +
                    "\"issuetype\": {\"name\": \"" + type + "\"}," +
                    "\"" + Config.JIRA_APP_NAME + "\": \"" + appName + "\"," +
                    "\"" + Config.JIRA_APP_VERSION + "\": \"" + appVersion  + "\"," +
                    "\"" + Config.JIRA_APP_VENDOR + "\": \"" + appVendor + "\"");
        if (!duedate.equals(""))
            data.append(", \"duedate\":\"" + duedate + "\"");
        data.append("}}");
        try {
            HttpResponse response = ClientHttp.sendPostRequest(url, data.toString());
            String responseEntity = EntityUtils.toString(response.getEntity());
            return new JSONObject(responseEntity).get("key").toString();
        } catch (IOException e) {
            throw new RuntimeException("Error while creating issue: " + e.getMessage());
        }
    }


    public boolean assignAllUsers(HashMap<User, User> mapUsers, ArrayList<Issue> issues) {
        if(issues.size() != mapUsers.size() * 2) {
            throw new IllegalArgumentException("Number of issues: " + issues.size() + " | Number of users: "
                    + mapUsers.size() * 2 + " It should be equal.");
        }
        int counterIssue = 0;
        for (Map.Entry <User, User> entry : mapUsers.entrySet()) {
            String userNameOne = entry.getKey().getUserName();
            String issueKeyOne = issues.get(counterIssue).getKeyIssue();
            String userNameTwo = entry.getValue().getUserName();
            String issueKeyTwo = issues.get(counterIssue + 1).getKeyIssue();
            assignIssue(issueKeyOne, userNameOne);
            setPackager(issueKeyOne, userNameOne);
            setQAengineer(issueKeyOne, userNameTwo);
            assignIssue(issueKeyTwo, userNameTwo);
            setPackager(issueKeyTwo, userNameTwo);
            setQAengineer(issueKeyTwo, userNameOne);
            counterIssue += 2;
        }
        return true;
    }

    public void setReadyForPackagingStatus(String issueKey) {
        String url = Config.API_URL + "/issue/" + issueKey + "/transitions?expand=transitions.fields";
        String data = "{ \"transition\" : { \"id\" : \"" + Config.JIRA_ID_READY_FOR_PACKAGING + "\" } }";
        HttpResponse response = ClientHttp.sendPostRequest(url, data);
        if (!response.toString().contains("204 No Content"))
            throw new RuntimeException("Status of issue was not changed: " + response.toString());
    }

    public void sendComment(String issueKey, String message) {
        String url = Config.API_URL + "/issue/" + issueKey + "/comment";
        boolean result = false;
        HttpResponse response = ClientHttp.sendPostRequest(url, "{ \"body\": \"" + message + "\" }");
        String responseEntity = null;
        try {
            responseEntity = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RuntimeException("Comment has not been sent: " + e.getMessage());
        }
        if (!responseEntity.contains("201 Created"))
            throw new RuntimeException("Comment has not been sent: " + responseEntity.toString());
    }
}
