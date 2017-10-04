package jirausers;

public class User {

    private String userName;
    private String emailAddress;
    private String displayName;

    public User(String userName, String emailAddress, String displayName) {
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.displayName = displayName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return  "Username: " + userName + " | Email address: " + emailAddress + " | Display name: " + displayName;
    }
}
