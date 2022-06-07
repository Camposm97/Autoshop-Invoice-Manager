package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Preferences;
import model.State;

public class PreferencesController {
    @FXML
    TextField tfCompany, tfAddress, tfCity, tfZip;
    @FXML
    ComboBox<State> cbState;
    @FXML
    TextField tfPhone, tfRepairShopId;

    @FXML
    public void initialize() {
        tfCompany.setText(Preferences.get().getCompany());
        tfCompany.textProperty().addListener((o, oldValue, newValue) -> Preferences.get().setCompany(newValue));
        tfAddress.setText(Preferences.get().getAddress());
        tfAddress.textProperty().addListener((o, oldValue, newValue) -> Preferences.get().setAddress(newValue));
        tfCity.setText(Preferences.get().getCity());
        tfCity.textProperty().addListener((o, oldValue, newValue) -> Preferences.get().setCity(newValue));
        cbState.getItems().setAll(State.list());
        cbState.setValue(Preferences.get().getState());
        cbState.setOnAction(e -> Preferences.get().setState(cbState.getValue()));
        tfZip.setText(Preferences.get().getZip());
        tfZip.textProperty().addListener((o, oldValue, newValue) -> Preferences.get().setZip(newValue));
        tfPhone.setText(Preferences.get().getPhone());
        tfPhone.textProperty().addListener((o, newValue, oldValue) -> Preferences.get().setPhone(newValue));
        tfRepairShopId.setText(Preferences.get().getRepairShopId());
        tfRepairShopId.textProperty().addListener((o, oldValue, newValue) -> Preferences.get().setRepairShopId(newValue));
    }
}
