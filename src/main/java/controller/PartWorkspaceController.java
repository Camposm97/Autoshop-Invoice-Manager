package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.AutoPart;
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

    public PartWorkspaceController() {
        Platform.runLater(() -> {
            tfPartNumberSearch.textProperty().addListener((o, oldValue, newValue) -> { // TODO
                System.out.println(newValue);
            });
            tfPartDescSearch.textProperty().addListener((o, oldValue, newValue) -> { // TODO
                System.out.println(newValue);
            });
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

    public void loadPart(AutoPart item) {
        lblId.setText(String.valueOf(item.getId()));
        tfPartNumber.setText(item.getName());
        tfPartDesc.setText(item.getDesc());
        tfPartRetailPrice.setText(String.valueOf(item.getRetailPrice()));
        tfPartListCost.setText(String.valueOf(item.getListPrice()));
        tfPartQuantity.setText(String.valueOf(item.getQuantity()));
        cbPartTaxable.setSelected(item.isTaxable());
    }

    public AutoPart buildPart() {
        int id = Integer.parseInt(lblId.getText());
        String partNumber = tfPartNumber.getText();
        String desc = tfPartDesc.getText();
        double retailPrice = Double.parseDouble(tfPartRetailPrice.getText());
        double listCost = Double.parseDouble(tfPartListCost.getText());
        int quantity = Integer.parseInt(tfPartQuantity.getText());
        boolean taxable = cbPartTaxable.isSelected();
        AutoPart item = new AutoPart(partNumber, desc, retailPrice, listCost, quantity, taxable);
        item.setId(id);
        return item;
    }
}
