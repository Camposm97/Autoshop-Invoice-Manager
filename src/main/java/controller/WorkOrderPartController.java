package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Item;
import model.WorkOrder;

public class WorkOrderPartController {
    WorkOrder workOrder;
    @FXML
    TextField tfPartNumber;
    @FXML
    TextArea taPartDesc;
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

    public WorkOrderPartController(WorkOrder workOrder) {
        this.workOrder = workOrder;
        Platform.runLater(() -> {
            tfPartNumberSearch.textProperty().addListener((o, oldValue, newValue) -> { // TODO
                System.out.println(newValue);
            });
            tfPartDescSearch.textProperty().addListener((o, oldValue, newValue) -> { // TODO
                System.out.println(newValue);
            });
        });
    }

    public void savePart() {
        String partNumber = tfPartNumber.getText();
        String desc = taPartDesc.getText();
        double retailPrice = Double.parseDouble(tfPartRetailPrice.getText());
        double listCost = Double.parseDouble(tfPartListCost.getText());
        int quantity = Integer.parseInt(tfPartQuantity.getText());
        boolean taxable = cbPartTaxable.isSelected();
        Item item = new Item(partNumber, desc, retailPrice, listCost, quantity, taxable);
        workOrder.addItem(item);
    }
}
