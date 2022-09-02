package model.ui;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.AppModel;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class AlertBuilder {
    private String title, headerText, contentText;
    private Alert.AlertType alertType;
    private List<ButtonType> btList;
    private Parent content;

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

    public AlertBuilder setContentText(String contentText) {
        this.contentText = contentText;
        return this;
    }

    public AlertBuilder setSaveCancelBtns() {
        ButtonType bt1 = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType bt2 = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        btList = new LinkedList<>();
        btList.add(bt1);
        btList.add(bt2);
        return this;
    }

    public AlertBuilder setPrintWorkOrderBtns() {
        ButtonType bt1 = new ButtonType("Print", ButtonBar.ButtonData.OK_DONE);
        ButtonType bt2 = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        btList = new LinkedList<>();
        btList.add(bt1);
        btList.add(bt2);
        return this;
    }

    public AlertBuilder setYesNoBtns() {
        ButtonType bt1 = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType bt2 = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        btList = new LinkedList<>();
        btList.add(bt1);
        btList.add(bt2);
        return this;
    }

    public AlertBuilder setDefaultBtn() {
        ButtonType bt = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        btList = new LinkedList<>();
        btList.add(bt);
        return this;
    }

    public AlertBuilder addApplyBtn() {
        ButtonType bt = ButtonType.APPLY;
        if (btList == null) btList = new LinkedList<>();
        btList.add(bt);
        return this;
    }

    public AlertBuilder setContent(Parent content) {
        this.content = content;
        return this;
    }

    public Alert buildAddDialog(String title, Parent content) {
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
        if (btList != null)
            alert.getButtonTypes().setAll(btList);
        if (contentText != null) {
            alert.setContentText(contentText);
        } else {
            alert.getDialogPane().setContent(content);
        }
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(FX.loadCSS("default.css"));
        if (AppModel.get().preferences().getTheme().equals(Theme.Dark)) {
            dialogPane.getStylesheets().add(FX.loadCSS("dark-mode.css"));
        }
        dialogPane.getStyleClass().add(GUIScale.getStyleClass(AppModel.get().preferences().getGuiScale()));
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("icon.png"));
        return alert;
    }
}
