package controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import model.AutoPart;
import model.DB;
import model.WorkOrder;

public class PartWorkspaceController {
    @FXML
    Text lblId;
    @FXML
    TextField tfPartNumber, tfPartDesc, tfPartRetailPrice, tfPartListCost, tfPartQuantity;
    @FXML
    CheckBox cbPartTaxable;
    @FXML
    TextField tfPartNumberSearch, tfPartDescSearch;
    @FXML
    TableView<AutoPart> tvParts;
    @FXML
    TableColumn<AutoPart, String> colPartNumber, colPartDesc, colPartRetailPrice;

    @FXML
    public void initialize() {
        tfPartNumber.textProperty().addListener((o, oldValue, newValue) -> tvParts.getItems().setAll(DB.get().getFilteredItems(newValue, tfPartDesc.getText())));
        tfPartDesc.textProperty().addListener((o, oldValue, newValue) -> tvParts.getItems().setAll(DB.get().getFilteredItems(tfPartNumber.getText(), newValue)));
        tfPartRetailPrice.textProperty().addListener((o,oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                tfPartRetailPrice.setText("0.00");
            }
        });
        colPartNumber.setCellValueFactory(e -> e.getValue().nameProperty());
        colPartDesc.setCellValueFactory(e -> e.getValue().descProperty());
        colPartRetailPrice.setCellValueFactory(e -> e.getValue().retailPriceProperty());
        tvParts.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && e.getButton().equals(MouseButton.PRIMARY)) {
                AutoPart autoPart = tvParts.getSelectionModel().getSelectedItem();
                if (autoPart != null) {
                    loadPart(autoPart);
                }
            }
        });
    }

    public void savePart(WorkOrder workOrder) {
        AutoPart item = buildPart();
        workOrder.addItem(item);
    }

    public void updatePart(WorkOrder workOrder, AutoPart oldItem) {
        AutoPart newItem = buildPart();
        workOrder.updateItem(oldItem, newItem);
    }

    public void loadPart(AutoPart ap) {
        lblId.setText(String.valueOf(ap.getId()));
        tfPartNumber.setText(ap.getName());
        tfPartDesc.setText(ap.getDesc());
        tfPartRetailPrice.setText(String.valueOf(ap.getRetailPrice()));
        tfPartListCost.setText(String.valueOf(ap.getListPrice()));
        tfPartQuantity.setText(String.valueOf(ap.getQuantity()));
        cbPartTaxable.setSelected(ap.isTaxable());
    }

    public AutoPart buildPart() {
        int id = Integer.parseInt(lblId.getText());
        String partNumber = tfPartNumber.getText();
        String desc = tfPartDesc.getText();
        double retailPrice = Double.parseDouble(tfPartRetailPrice.getText());
        double listCost = Double.parseDouble(tfPartListCost.getText());
        int quantity = Integer.parseInt(tfPartQuantity.getText());
        boolean taxable = cbPartTaxable.isSelected();
        AutoPart autoPart = new AutoPart(partNumber, desc, retailPrice, listCost, quantity, taxable);
        autoPart.setId(id);
        DB.get().saveAutoPart(autoPart);
        return autoPart;
    }
}
