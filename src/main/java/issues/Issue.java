package issues;


public class Issue {

    private int idIssue;
    private String keyIssue;
    private String summary;
    private String reporter;
    private String status;
    private String assignee;
    private String packager;
    private String qaEngineer;

    public Issue(int idIssue, String keyIssue, String reporter, String summary, String status, String assignee,
                 String packager, String qaEngineer) {
        this.idIssue = idIssue;
        this.keyIssue = keyIssue;
        this.summary = summary;
        this.reporter = reporter;
        this.status = status;
        this.assignee = assignee;
        this.packager = packager;
        this.qaEngineer = qaEngineer;
    }
    public String getKeyIssue() {
        return keyIssue;
    }

    public int getIdIssue() {
        return idIssue;
    }

    public void setIdIssue(int idIssue) {
        this.idIssue = idIssue;
    }

    public void setKeyIssue(String keyIssue) {
        this.keyIssue = keyIssue;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getPackager() {
        return packager;
    }

    public void setPackager(String packager) {
        this.packager = packager;
    }

    public String getQaEngineer() {
        return qaEngineer;
    }

    public void setQaEngineer(String qaEngineer) {
        this.qaEngineer = qaEngineer;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "idIssue=" + idIssue +
                ", keyIssue='" + keyIssue + '\'' +
                ", summary='" + summary + '\'' +
                ", reporter='" + reporter + '\'' +
                ", status='" + status + '\'' +
                ", assignee='" + assignee + '\'' +
                ", packager='" + packager + '\'' +
                ", qaEngineer='" + qaEngineer + '\'' +
                '}';
    }
}
