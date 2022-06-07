package model.ui;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

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
    }

    public AlertBuilder setAlertType(Alert.AlertType alertType) {
        this.alertType = alertType;
        return this;
    }

    public AlertBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public AlertBuilder setHeaderText(String headerText) {
        this.headerText = headerText;
        return this;
    }

    public AlertBuilder setHeaderTextInfo() {
        this.headerText = "Please fill out the following information";
        return this;
    }

    public AlertBuilder setOnDefaultBtnClicked(Function<Void, Void> callback) {
        this.callback = callback;
        return this;
    }

    public AlertBuilder setSaveCancelBtns() {
        ButtonType bt1 = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType bt2 = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        btList = new ButtonType[]{bt1, bt2};
        return this;
    }

    public AlertBuilder setPrintWorkOrderBtns() {
        ButtonType bt1 = new ButtonType("Print", ButtonBar.ButtonData.OK_DONE);
        ButtonType bt2 = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        btList = new ButtonType[]{bt1, bt2};
        return this;
    }

    public AlertBuilder setDefaultBtn() {
        ButtonType bt = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        btList = new ButtonType[]{bt};
        return this;
    }

    public AlertBuilder setContent(Node content) {
        this.content = content;
        return this;
    }

    public Alert buildAddDialog(String title, Node content) {
        setTitle(title);
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
