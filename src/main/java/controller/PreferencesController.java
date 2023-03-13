package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Model;
import model.State;
import model.ui.GUIScale;
import model.ui.Theme;

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
    ComboBox<Theme> cbTheme;


    @FXML
    public void initialize() {
        tfCompany.setText(Model.get().preferences().getCompany());
        tfCompany.textProperty().addListener((o, oldValue, newValue) -> Model.get().preferences().setCompany(newValue));
        tfAddress.setText(Model.get().preferences().getAddress());
        tfAddress.textProperty().addListener((o, oldValue, newValue) -> Model.get().preferences().setAddress(newValue));
        tfCity.setText(Model.get().preferences().getCity());
        tfCity.textProperty().addListener((o, oldValue, newValue) -> Model.get().preferences().setCity(newValue));
        cbState.setItems(State.list());
        cbState.setValue(Model.get().preferences().getState());
        cbState.setOnAction(e -> Model.get().preferences().setState(cbState.getValue()));
        tfZip.setText(Model.get().preferences().getZip());
        tfZip.textProperty().addListener((o, oldValue, newValue) -> Model.get().preferences().setZip(newValue));
        tfPhone.setText(Model.get().preferences().getPhone());
        tfPhone.textProperty().addListener((o, oldValue, newValue) -> Model.get().preferences().setPhone(newValue));
        tfRepairShopId.setText(Model.get().preferences().getRepairShopId());
        tfRepairShopId.textProperty().addListener((o, oldValue, newValue) -> Model.get().preferences().setRepairShopId(newValue));
        tfTitle.setText(Model.get().preferences().getSpecialTitle());
        tfTitle.textProperty().addListener((o, x, y) -> Model.get().preferences().setSpecialTitle(y));
        tfLaborRate.setText(Model.get().preferences().getLaborRate().toString());
        tfLaborRate.textProperty().addListener((o, oldValue, newValue) -> {
            try {
                Model.get().preferences().setLaborRate(Double.parseDouble(newValue));
            } catch (NumberFormatException e) {}
        });
        tfTaxRate.setText(Model.get().preferences().getTaxRate().toString());
        tfTaxRate.textProperty().addListener((o, oldValue, newValue) -> {
            try {
                Model.get().preferences().setTaxRate(Double.parseDouble(newValue));
            } catch (NumberFormatException e) {}
        });
        cbScale.setItems(GUIScale.list());
        cbScale.setValue(Model.get().preferences().getGuiScale());
        cbScale.setOnAction(e -> Model.get().preferences().setGuiScale(cbScale.getValue()));
        cbTheme.setItems(Theme.list());
        cbTheme.setValue(Model.get().preferences().getTheme());
        cbTheme.setOnAction(e -> Model.get().preferences().setTheme(cbTheme.getValue()));
    }
}
