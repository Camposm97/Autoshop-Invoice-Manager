package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Preferences;
import model.GUIScale;
import model.State;

public class PreferencesController {
    @FXML
    TextField tfCompany, tfAddress, tfCity, tfZip;
    @FXML
    ComboBox<State> cbState;
    @FXML
    TextField tfPhone, tfRepairShopId, tfTitle, tfLaborRate, tfTaxRate;
    @FXML
    ComboBox<GUIScale> cbScale;

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
        tfTitle.setText(Preferences.get().getSpecialTitle());
        tfTitle.textProperty().addListener((o, x, y) -> Preferences.get().setSpecialTitle(y));
        tfLaborRate.setText(Preferences.get().getLaborRate().toString());
        tfLaborRate.textProperty().addListener((o, oldValue, newValue) -> {
            try {
                Preferences.get().setLaborRate(Double.parseDouble(newValue));
            } catch (NumberFormatException e) {}
        });
        tfTaxRate.setText(Preferences.get().getTaxRate().toString());
        tfTaxRate.textProperty().addListener((o, oldValue, newValue) -> {
            try {
                Preferences.get().setTaxRate(Double.parseDouble(newValue));
            } catch (NumberFormatException e) {}
        });
        cbScale.getItems().setAll(GUIScale.list());
        cbScale.setValue(Preferences.get().getGuiScale());
        cbScale.setOnAction(e -> Preferences.get().setGuiScale(cbScale.getValue()));
    }
}
