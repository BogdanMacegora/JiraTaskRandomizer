package gui.controllers;

import common.AlertMessage;
import common.Config;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import toolkit.JiraToolkit;
import java.time.format.DateTimeFormatter;


public class CreateIssuesController {

    @FXML private ChoiceBox choiceBoxProject;
    @FXML private ChoiceBox choiceBoxType;
    @FXML private TextField textFieldParentIssue;
    @FXML private TextField textFieldSummary;
    @FXML private TextField textFieldDescription;
    @FXML private TextField textFieldAppName;
    @FXML private TextField textFieldAppVersion;
    @FXML private TextField textFieldAppVendor;
    @FXML private DatePicker datePickerDD;
    @FXML private TextField textFieldNumberOfCopies;
    @FXML private TextArea textAreaResult;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    private void initialize() {
        choiceBoxProject.setItems(FXCollections.observableArrayList(Config.JIRA_PROJECT_NAME));
        choiceBoxProject.setValue(Config.JIRA_PROJECT_NAME);
        choiceBoxType.setItems(FXCollections.observableArrayList(Config.ISSUE_TYPE_APP_CHANGE,
                Config.ISSUE_TYPE_APP_PACKAGE));
        choiceBoxType.setValue(Config.ISSUE_TYPE_APP_CHANGE);
        choiceBoxType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                checkIssueType(newValue);
            }
        });
    }

    @FXML
    public void createIssues(ActionEvent actionEvent) {
        textAreaResult.setText("");
        textAreaResult.setStyle(null);
        String project, type, parentIssue, summary, description, appName, appVersion, appVendor, dueDate;
        int countOfCopies = 0;
        textFieldNumberOfCopies.styleProperty().set(null);
        textFieldSummary.styleProperty().set(null);
        textFieldParentIssue.styleProperty().set(null);
        project = choiceBoxProject.getValue().toString();
        type = choiceBoxType.getValue().toString();
        parentIssue = textFieldParentIssue.getText().toUpperCase().trim();
        dueDate = (datePickerDD.getValue() == null) ? "" :
                datePickerDD.getValue().format(dateTimeFormatter).toString().trim();
        summary = textFieldSummary.getText().trim();
        description = textFieldDescription.getText().trim();
        appName = textFieldAppName.getText().trim();
        appVersion = textFieldAppVersion.getText().trim();
        appVendor = textFieldAppVendor.getText().trim();
        if(summary.equals("")) {
            textFieldSummary.styleProperty().set("-fx-background-color:#FFB6C1"); // red color
            return;
        }
        try {
            countOfCopies = Integer.parseInt(textFieldNumberOfCopies.getText().trim());
        } catch (NumberFormatException e) {
            textFieldNumberOfCopies.styleProperty().set("-fx-background-color:#FFB6C1");
            AlertMessage.showErrorMessage("Please specify correct number of copies.");
            return;
        }
        startThreadCreatingIssues(project, type, parentIssue, summary, description, appName, appVersion, appVendor,
                dueDate, countOfCopies);
    }

    private void checkIssueType(String selectedType) {
        if (selectedType.equals(Config.ISSUE_TYPE_APP_CHANGE)) {
            textFieldParentIssue.setDisable(true);
            textFieldParentIssue.setVisible(false);
        } else {
            textFieldParentIssue.setDisable(false);
            textFieldParentIssue.setVisible(true);
        }
    }

    private void startThreadCreatingIssues(String project,String type, String parentIssue, String summary,
                                           String description, String appName, String appVersion, String appVendor,
                                           String dueDate, int countOfCopy) {
        textAreaResult.styleProperty().set(null);
        JiraToolkit jiraToolkit = new JiraToolkit();
        StringBuilder stringBuilderResult = new StringBuilder();
        (new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < countOfCopy; i++) {
                    String resultKeyIssue = null;
                    try {
                        if(type.equals(Config.ISSUE_TYPE_APP_CHANGE)) {
                            resultKeyIssue = jiraToolkit.createIssue(project, type, summary, description, appName,
                                    appVersion, appVendor, dueDate);                            
                            textFieldParentIssue.setText(resultKeyIssue);
                        } else if (type.equals(Config.ISSUE_TYPE_APP_PACKAGE)) {
                            if(parentIssue.equals("")) {
                                textFieldParentIssue.styleProperty().set("-fx-background-color:#FFB6C1"); // red color
                                AlertMessage.showErrorMessage("Please specify parent issue.");
                                break;
                            }
                            resultKeyIssue = jiraToolkit.createSubIssue(project, parentIssue, type, summary, description,
                                    appName, appVersion, appVendor, dueDate);
                            jiraToolkit.setReadyForPackagingStatus(resultKeyIssue);
                        }
                    } catch (RuntimeException e) {
                        AlertMessage.showErrorMessage("Exception while creating issues. " + e.getMessage(), e);
                        textAreaResult.styleProperty().set("-fx-background-color:#FFB6C1");
                        break;
                    }
                    stringBuilderResult.append("Created: " + resultKeyIssue + "\n");
                    textAreaResult.setText(stringBuilderResult.toString());
                }
            }
        })).start();
    }
}
