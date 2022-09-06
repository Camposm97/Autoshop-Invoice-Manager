package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.AppModel;
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
        tfCompany.setText(AppModel.get().preferences().getCompany());
        tfCompany.textProperty().addListener((o, oldValue, newValue) -> AppModel.get().preferences().setCompany(newValue));
        tfAddress.setText(AppModel.get().preferences().getAddress());
        tfAddress.textProperty().addListener((o, oldValue, newValue) -> AppModel.get().preferences().setAddress(newValue));
        tfCity.setText(AppModel.get().preferences().getCity());
        tfCity.textProperty().addListener((o, oldValue, newValue) -> AppModel.get().preferences().setCity(newValue));
        cbState.setItems(State.list());
        cbState.setValue(AppModel.get().preferences().getState());
        cbState.setOnAction(e -> AppModel.get().preferences().setState(cbState.getValue()));
        tfZip.setText(AppModel.get().preferences().getZip());
        tfZip.textProperty().addListener((o, oldValue, newValue) -> AppModel.get().preferences().setZip(newValue));
        tfPhone.setText(AppModel.get().preferences().getPhone());
        tfPhone.textProperty().addListener((o, newValue, oldValue) -> AppModel.get().preferences().setPhone(newValue));
        tfRepairShopId.setText(AppModel.get().preferences().getRepairShopId());
        tfRepairShopId.textProperty().addListener((o, oldValue, newValue) -> AppModel.get().preferences().setRepairShopId(newValue));
        tfTitle.setText(AppModel.get().preferences().getSpecialTitle());
        tfTitle.textProperty().addListener((o, x, y) -> AppModel.get().preferences().setSpecialTitle(y));
        tfLaborRate.setText(AppModel.get().preferences().getLaborRate().toString());
        tfLaborRate.textProperty().addListener((o, oldValue, newValue) -> {
            try {
                AppModel.get().preferences().setLaborRate(Double.parseDouble(newValue));
            } catch (NumberFormatException e) {}
        });
        tfTaxRate.setText(AppModel.get().preferences().getTaxRate().toString());
        tfTaxRate.textProperty().addListener((o, oldValue, newValue) -> {
            try {
                AppModel.get().preferences().setTaxRate(Double.parseDouble(newValue));
            } catch (NumberFormatException e) {}
        });
        cbScale.setItems(GUIScale.list());
        cbScale.setValue(AppModel.get().preferences().getGuiScale());
        cbScale.setOnAction(e -> AppModel.get().preferences().setGuiScale(cbScale.getValue()));
        cbTheme.setItems(Theme.list());
        cbTheme.setValue(AppModel.get().preferences().getTheme());
        cbTheme.setOnAction(e -> AppModel.get().preferences().setTheme(cbTheme.getValue()));
    }
}
