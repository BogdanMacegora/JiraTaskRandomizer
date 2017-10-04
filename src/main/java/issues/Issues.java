package issues;

import common.ClientHttp;
import common.Config;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;


public class Issues {

    public static ArrayList <Issue> getIssuesByJQL(String query) {
        ArrayList <Issue> issuesArray = new ArrayList<>();
        JSONObject jObjectIssues = new JSONObject(ClientHttp.sendGetRequest(Config.API_URL + "/search?jql=" + query));
        JSONArray jArrayIssues = jObjectIssues.getJSONArray("issues");
        for (int i = 0; i < jArrayIssues.length(); i++) {
            String assignee = null;
            String packager = null;
            String qaEngineer = null;
            JSONObject jIssue = jArrayIssues.getJSONObject(i);
            int idIssue= Integer.parseInt(jIssue.getString("id"));
            String keyIssue = jIssue.getString("key");
            String status = jIssue.getJSONObject("fields").getJSONObject("status").getString("name");
            String summary = jIssue.getJSONObject("fields").getString("summary");
            String reporter = jIssue.getJSONObject("fields").getJSONObject("reporter").getString("name");
            assignee = (jIssue.getJSONObject("fields").isNull("assignee")) ?
                    "Unassigned" : jIssue.getJSONObject("fields").getJSONObject("assignee").getString("name");
            if(!jIssue.getJSONObject("fields").isNull(Config.JIRA_FILED_PACKAGER))
                packager = jIssue.getJSONObject("fields").getJSONObject(Config.JIRA_FILED_PACKAGER).getString("name");
            if(!jIssue.getJSONObject("fields").isNull(Config.JIRA_FILED_QA))
                qaEngineer = jIssue.getJSONObject("fields").getJSONObject(Config.JIRA_FILED_QA).getString("name");
            Issue issue = new Issue(idIssue, keyIssue, reporter, summary, status, assignee, packager, qaEngineer);
            issuesArray.add(issue);
        }
        return issuesArray;
    }
}
