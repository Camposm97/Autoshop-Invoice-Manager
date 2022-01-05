package model;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class AlertBuilder {
    public static ButtonType[] buildDialogButtons() {
        ButtonType btSave = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType btCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        return new ButtonType[]{btSave, btCancel};
    }

    public static Alert buildDialog(String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText("Please fill out the following information");
        alert.getButtonTypes().setAll(buildDialogButtons());
        return alert;
    }
}
