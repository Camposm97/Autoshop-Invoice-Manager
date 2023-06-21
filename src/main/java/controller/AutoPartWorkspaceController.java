package controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import model.database.DB;
import model.ui.FX;
import model.ui.IOffsets;
import model.work_order.AutoPart;

import java.util.function.Function;

public class AutoPartWorkspaceController implements IOffsets {
    @FXML
    Text lblId;
    @FXML
    TextField tfPartNumber, tfPartDesc, tfPartRetailPrice, tfPartListCost, tfPartQuantity;
    @FXML
    CheckBox cbPartTaxable;
    @FXML
    TableView<AutoPart> tvParts;
    @FXML
    TableColumn<AutoPart, String> colPartNumber, colPartDesc, colPartRetailPrice;

    @FXML
    public void initialize() {
        tfPartDesc.textProperty().addListener((o, oldValue, newValue) -> {
            tvParts.getItems().setAll(DB.get().autoParts().getAutoPartSuggestions(newValue));
            FX.autoResizeColumns(tvParts, AP_OFFSET);
            genID();
        });
        tfPartQuantity.setText("1");
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

    /**
     * Creates AutoPart object and passes to callback function
     * @param callback Called after AutoPart object is created
     */
    public void savePart(Function<AutoPart, Void> callback) {
        AutoPart autoPart = buildPart();
        callback.apply(autoPart);
    }

    /**
     * Updates AutoPart parameters and passes to callback function
     * @param callback Called after AutoPart object is created
     */
    public void updatePart(Function<AutoPart, Void> callback) {
        AutoPart newAutoPart = buildPart();
        callback.apply(newAutoPart);
    }

    /**
     * Sets the control fields with the attributes of given {ap}
     * @param ap Used to set the control fields with its attributes
     */
    public void loadPart(AutoPart ap) {
        lblId.setText(String.valueOf(ap.getId()));
        tfPartNumber.setText(ap.getName());
        tfPartDesc.setText(ap.getDesc());
        tfPartRetailPrice.setText(String.valueOf(ap.getRetailPrice()));
        tfPartListCost.setText(String.valueOf(ap.getListPrice()));
        tfPartQuantity.setText(String.valueOf(ap.getQuantity()));
        cbPartTaxable.setSelected(ap.isTaxable());
    }

    /**
     * Creates AutoPart object and returns it
     * @return AutoPart object to be returned
     */
    public AutoPart buildPart() {
        int id = Integer.parseInt(lblId.getText());
        String partNumber = tfPartNumber.getText();
        String desc = tfPartDesc.getText();
        double retailPrice, listCost;
        int quantity;
        try {
            retailPrice = Double.parseDouble(tfPartRetailPrice.getText());
        } catch (NumberFormatException e) {
            retailPrice = 0.0;
        }
        try {
            listCost = Double.parseDouble(tfPartListCost.getText());
        } catch (NumberFormatException e) {
            listCost = 0.0;
        }
        try {
            quantity = Integer.parseInt(tfPartQuantity.getText());
        } catch (NumberFormatException e) {
            quantity = 0;
        }
        boolean taxable = cbPartTaxable.isSelected();
        AutoPart autoPart = new AutoPart(partNumber, desc, retailPrice, listCost, quantity, taxable);
        autoPart.setId(id);
        DB.get().autoParts().add(autoPart);
        return autoPart;
    }

    /**
     * Generates ID for AutoPart object (created later) and displays it on the {tfPartNumber} field.
     * Set the {tfPartNumber} field with generated ID.
     */
    public void genID() {
        final String REGEX = "\\s+"; /* Used to split {s} by spaces */
        String s = tfPartDesc.getText();
        String[] tokens = s.trim().split(REGEX); /* Split {s} by {REGEX} */
        var strId = new StringBuilder(); /* Used to build the ID of the auto part */
        for (int i = 0; i < tokens.length; i++) { /* Iterate each string in {tokens} */
            String x = tokens[i].toUpperCase(); /* Assign tokens[i] to {x} */
            var len = x.length();
            try { /* Try to parse {x} to Integer */
                var n = Integer.parseInt(x); /**/
                strId.append(n);
            } catch (NumberFormatException e) {
                /* Otherwise, analyze {x} to generate the uato part ID */
                char[] arr = x.toCharArray();
                if (len <= 2) { /* If the length of {x} <= 2, then use {x} as the ID */
                    strId.append(x);
                } else if (len == 3) {
                    if (x.contains("A/C")) /* Remove '/' in "A/C" */
                        strId.append("AC");
                    else /* Otherwise, append {x} to {stdId} */
                        strId.append(x);
                } else if (len > 3 && len <= 5) {
                    /* Append first 3 chars, then append last char of {x} */
                    strId.append(arr[0]);
                    strId.append(arr[1]);
                    strId.append(arr[2]);
                    strId.append(arr[len-1]);
                } else {
                    /* Append first 2 chars and last 2 chars of {x} */
                    strId.append(arr[0]);
                    strId.append(arr[1]);
                    strId.append(arr[len-2]);
                    strId.append(arr[len-1]);
                }
            }
        }
        tfPartNumber.setText(strId.toString());
    }
}
