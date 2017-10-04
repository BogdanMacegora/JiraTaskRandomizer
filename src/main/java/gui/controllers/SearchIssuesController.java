package gui.controllers;

import common.AlertMessage;
import issues.Issue;
import issues.Issues;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import jirausers.User;
import jirausers.Users;
import toolkit.JiraToolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class SearchIssuesController {

    private ArrayList<Issue> issues;
    @FXML private TextField fieldSearchIssues;
    @FXML private TableView tableViewSearchIssues;
    @FXML private TableColumn columnSearchIssue;
    @FXML private TableColumn columnSearchSummary;
    @FXML private TableColumn columnSearchStatus;
    @FXML private TableColumn columnSearchAssignee;
    @FXML private TableColumn columnSearchPackager;
    @FXML private TableColumn columnSearchQA;
    @FXML private Label labelSearchCountResult;
    @FXML private Label labelRandomAssignInfo;

    @FXML
    private void initialize() {
        columnSearchIssue.setCellValueFactory(new PropertyValueFactory<Issue, String>("keyIssue"));
        columnSearchSummary.setCellValueFactory(new PropertyValueFactory<Issue, String>("summary"));
        columnSearchStatus.setCellValueFactory(new PropertyValueFactory<Issue, String>("status"));
        columnSearchAssignee.setCellValueFactory(new PropertyValueFactory<Issue, String>("assignee"));
        columnSearchPackager.setCellValueFactory(new PropertyValueFactory<Issue, String>("packager"));
        columnSearchQA.setCellValueFactory(new PropertyValueFactory<Issue, String>("qaEngineer"));
    }

    @FXML
    public void searchIssues(ActionEvent actionEvent) {
        String query = fieldSearchIssues.getText().trim().replace(" ", "%20").replace("=", "%3D").replace("\"", "%22");
        fieldSearchIssues.setStyle(null);
        labelSearchCountResult.setText("");
        if (fieldSearchIssues.getText().trim().equals("")) {
            fieldSearchIssues.styleProperty().set("-fx-background-color:#FFB6C1"); //red color
            return;
        }
        try {
            issues = Issues.getIssuesByJQL(query);
        } catch (Exception e) {
            fieldSearchIssues.styleProperty().set("-fx-background-color:#FFB6C1");
            return;
        }
        ObservableList<Issue> issuesObservableList = FXCollections.observableArrayList();
        issuesObservableList.addAll(issues);
        tableViewSearchIssues.setItems(issuesObservableList);
        labelSearchCountResult.setText("Count: " + issues.size());
    }


    public void randomAssignAllUsers(ActionEvent actionEvent) {
        labelRandomAssignInfo.setText("In progress.");
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Collections.shuffle(issues); // randomize issues
                    HashMap<User, User> randomMapUsers = Users.getRandomMapUsers();
                    JiraToolkit jiraToolkit = new JiraToolkit();
                    if (jiraToolkit.assignAllUsers(randomMapUsers, issues)) {
                        Platform.runLater(
                            () -> {
                                labelRandomAssignInfo.setTextFill(Color.GREEN);
                                labelRandomAssignInfo.setText("Completed.");
                            }
                        );
                    }
                } catch (RuntimeException e) {
                    Platform.runLater(
                            () -> {
                                labelRandomAssignInfo.setText("Stopped.");
                                labelRandomAssignInfo.setTextFill(Color.RED);
                            }
                    );
                    AlertMessage.showErrorMessage("Error while assign users: " + e.getMessage(), e);
                }
            }
        })).start();
    }
}
