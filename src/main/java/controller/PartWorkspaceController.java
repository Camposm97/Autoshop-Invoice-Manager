package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.AutoPart;
import model.DB;
import model.WorkOrder;

import java.util.List;

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
        tfPartNumberSearch.textProperty().addListener((o, oldValue, newValue) -> { // TODO
            List<AutoPart> list = DB.get().getFilteredItems(newValue, tfPartDescSearch.getText());
            tvParts.getItems().setAll(list);
        });
        tfPartDescSearch.textProperty().addListener((o, oldValue, newValue) -> { // TODO
            List<AutoPart> list = DB.get().getFilteredItems(tfPartNumberSearch.getText(), newValue);
            tvParts.getItems().setAll(list);
        });
        colPartNumber.setCellValueFactory(e -> e.getValue().nameProperty());
        colPartDesc.setCellValueFactory(e -> e.getValue().descProperty());
        colPartRetailPrice.setCellValueFactory(e -> e.getValue().retailPriceProperty());
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
