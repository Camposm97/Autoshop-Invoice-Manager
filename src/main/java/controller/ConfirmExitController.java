package controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import model.Model;

public class ConfirmExitController {
    @FXML
    CheckBox cbExit;

    public void cbExitHandler() {
        Model.get().preferences().setConfirmExit(!cbExit.isSelected());
    }
}
