package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import model.Item;
import model.WorkOrder;

public class WorkOrderPartWorkspaceController {
    @FXML
    TextField tfPartNumber;
    @FXML
    TextField tfPartDesc;
    @FXML
    TextField tfPartRetailPrice;
    @FXML
    TextField tfPartListCost;
    @FXML
    TextField tfPartQuantity;
    @FXML
    CheckBox cbPartTaxable;
    @FXML
    TextField tfPartNumberSearch;
    @FXML
    TextField tfPartDescSearch;

    public WorkOrderPartWorkspaceController() {
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
        Item item = buildPart();
        workOrder.addItem(item);
    }

    public void updatePart(WorkOrder workOrder, Item oldItem) {
        Item newItem = buildPart();
        workOrder.updateItem(oldItem, newItem);
    }

    public void loadPart(Item item) {
        tfPartNumber.setText(item.getId());
        tfPartDesc.setText(item.getDesc());
        tfPartRetailPrice.setText(String.valueOf(item.getRetailPrice()));
        tfPartListCost.setText(String.valueOf(item.getListPrice()));
        tfPartQuantity.setText(String.valueOf(item.getQuantity()));
        cbPartTaxable.setSelected(item.isTaxable());
    }

    public Item buildPart() {
        String partNumber = tfPartNumber.getText();
        String desc = tfPartDesc.getText();
        double retailPrice = Double.parseDouble(tfPartRetailPrice.getText());
        double listCost = Double.parseDouble(tfPartListCost.getText());
        int quantity = Integer.parseInt(tfPartQuantity.getText());
        boolean taxable = cbPartTaxable.isSelected();
        Item item = new Item(partNumber, desc, retailPrice, listCost, quantity, taxable);
        return item;
    }
}
