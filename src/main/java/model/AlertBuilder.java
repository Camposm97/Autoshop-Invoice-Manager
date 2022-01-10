package model;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class AlertBuilder {
    private String title;
    private String headerText;
    private Alert.AlertType alertType;
    private Function<Void, Void> callback;
    private ButtonType[] btList;
    private Node content;

    public AlertBuilder() {
        this.alertType = Alert.AlertType.INFORMATION;
        this.btList = new ButtonType[0];
    }

    public void setAlertType(Alert.AlertType alertType) {
        this.alertType = alertType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public void setHeaderTextInfo() {
        this.headerText = "Please fill out the following information";
    }

    public void setOnDefaultBtnClicked(Function<Void, Void> callback) {
        this.callback = callback;
    }

    public void setSaveCancelBtns() {
        ButtonType bt1 = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType bt2 = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        btList = new ButtonType[]{bt1, bt2};
    }

    public void setPrintWorkOrderBtns() {
        ButtonType bt1 = new ButtonType("Print", ButtonBar.ButtonData.OK_DONE);
        ButtonType bt2 = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        btList = new ButtonType[]{bt1, bt2};
    }

    public void setContent(Node content) {
        this.content = content;
    }

    public Alert buildAddDialog(String title, Node content) {
        setHeaderTextInfo();
        setSaveCancelBtns();
        setContent(content);
        return build();
    }

    public Alert build() {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.getDialogPane().setContent(content);
        alert.getButtonTypes().setAll(btList);
        return alert;
    }
}
