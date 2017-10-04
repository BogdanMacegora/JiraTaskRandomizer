package common;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import java.io.PrintWriter;
import java.io.StringWriter;

public class AlertMessage {

    public static void showErrorMessage(String message) {
        Platform.runLater(
                () -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Exception");
                    alert.setHeaderText("An error has occurred");
                    alert.setContentText(message);
                    alert.showAndWait();
                }
        );
    }
    public static void showErrorMessage(String message, Exception e) {
        Platform.runLater(
                () -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Exception");
                    alert.setHeaderText("An error has occurred");
                    alert.setContentText(message);
                    StringWriter stringWriter = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(stringWriter);
                    e.printStackTrace(printWriter);
                    String exceptionText = stringWriter.toString();
                    Label label = new Label("Stacktrace:");
                    TextArea textArea = new TextArea(exceptionText);
                    textArea.setEditable(false);
                    textArea.setWrapText(true);
                    textArea.setMaxWidth(Double.MAX_VALUE);
                    textArea.setMaxHeight(Double.MAX_VALUE);
                    GridPane.setVgrow(textArea, Priority.ALWAYS);
                    GridPane.setHgrow(textArea, Priority.ALWAYS);
                    GridPane expContent = new GridPane();
                    expContent.setMaxWidth(Double.MAX_VALUE);
                    expContent.add(label, 0, 0);
                    expContent.add(textArea, 0, 1);
                    alert.getDialogPane().setExpandableContent(expContent);
                    alert.showAndWait();
                }
        );
    }
    public static void showInfoMessage(String message) {
        Platform.runLater(
                () -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Info");
                    alert.setHeaderText("Information message");
                    alert.setContentText(message);
                    alert.showAndWait();
                }
        );
    }

}
