package controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Model;
import model.work_order.AutoPart;
import model.work_order.Labor;

import java.util.Iterator;
import java.util.function.Function;

public class LaborWorkspaceController {
    private Iterator<AutoPart> items;
    @FXML
    GridPane fieldGrid;
    @FXML
    Text lblId;
    @FXML
    TextField tfLaborCode;
    @FXML
    TextArea taDesc;
    @FXML
    TextField tfBilledHrs, tfRate;
    @FXML
    CheckBox cbTaxable;
    @FXML
    CheckBox cbAutoGen;

    @FXML
    public void initialize() {
        tfLaborCode.setText("0");
        tfBilledHrs.setText("1.0");
        tfBilledHrs.setOnScroll(e -> {
            var value = Double.parseDouble(tfBilledHrs.getText());
            if (e.getDeltaY() > 0) {
                value += 0.1d;
            } else {
                if (value >= 0) {
                    value -= 0.1d;
                }
            }
            tfBilledHrs.setText(String.format("%.1f",value));
        });
        tfRate.setText(Model.get().preferences().getLaborRate().toString());

        // Initialize listeners for fields
        tfLaborCode.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.TAB) {
                taDesc.requestFocus();
                taDesc.positionCaret(taDesc.getText().length());
            }
        });
        taDesc.textProperty().addListener((ov,strOld,strCurrent) -> {
            if (strCurrent.contains("\t")) {
                taDesc.setText(strOld);
                tfBilledHrs.requestFocus();
            }
        });
        tfRate.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.TAB) cbTaxable.requestFocus();
        });
    }

    public void loadItems(Iterator<AutoPart> items) {
        this.items = items;
    }

    public void saveLabor(Function<Labor, Void> callback) {
        Labor labor = buildLabor();
        callback.apply(labor);
    }

    public void updateLabor(Function<Labor, Void> callback) {
        Labor newLabor = buildLabor();
        callback.apply(newLabor);
    }

    public void loadLabor(Labor labor) {
        lblId.setText(String.valueOf(labor.getId()));
        tfLaborCode.setText(labor.getName());
        taDesc.setText(labor.getDesc());
        tfBilledHrs.setText(String.valueOf(labor.getBilledHrs()));
        tfRate.setText(String.valueOf(labor.getRate()));
        cbTaxable.setSelected(labor.isTaxable());
        cbAutoGen.setSelected(false);
        taDesc.setDisable(false);
    }

    public Labor buildLabor() {
        int id = Integer.parseInt(lblId.getText());
        String laborCode = tfLaborCode.getText();
        final StringBuilder desc = new StringBuilder("Installed ");
        /* If auto-gen checkbox is selected, then generate the labor description */
        if (cbAutoGen.isSelected()) {
            for (int i = 0; i < vbox.getChildren().size();  i++) {
                CheckBox cb = (CheckBox) vbox.getChildren().get(i);
                if (cb.isSelected()) {
                    desc.append(cb.getText()).append(", ");
                }
            }
            if (!vbox.getChildren().isEmpty()) desc.delete(desc.length()-2, desc.length());
        } else desc.delete(0,desc.length()).append(taDesc.getText());
        double billedHrs, rate;
        try {
            billedHrs = Double.parseDouble(tfBilledHrs.getText());
        } catch (NumberFormatException e) {
            billedHrs = 0.0;
        }
        try {
            rate = Double.parseDouble(tfRate.getText());
        } catch (NumberFormatException e) {
            rate = 0.0;
        }
        boolean taxable = cbTaxable.isSelected();
        Labor labor = new Labor(laborCode, desc.toString(), billedHrs, rate, taxable);
        labor.setId(id);
        return labor;
    }

    private VBox vbox;

    public void genDesc() {
        if (cbAutoGen.isSelected()) {
            if (vbox == null) {
                vbox = new VBox(1);
                items.forEachRemaining(x -> {
                    CheckBox cb = new CheckBox(x.getDesc());
                    cb.setSelected(true);
                    vbox.getChildren().add(cb);
                });
            } else vbox.getChildren().forEach(x -> ((CheckBox) x).setSelected(true));
            final int FIT_HEIGHT = 200;
            ScrollPane scrollPane = new ScrollPane(vbox);
            scrollPane.setPrefHeight(FIT_HEIGHT);
            fieldGrid.add(scrollPane, 1, 1);
            fieldGrid.getChildren().remove(taDesc);
        } else {
            fieldGrid.getChildren().remove(1, 1); /* remove scroll pane */
            fieldGrid.add(taDesc, 1, 1);
        }
    }
}
