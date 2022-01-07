package controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.Labor;
import model.WorkOrder;

public class LaborWorkspaceController {
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

    public void saveLabor(WorkOrder workOrder) {
        Labor labor = buildLabor();
        workOrder.addLabor(labor);
    }

    public void updateLabor(WorkOrder workOrder, Labor oldLabor) {
        Labor newLabor = buildLabor();
        workOrder.updateLabor(oldLabor, newLabor);
    }

    public void loadLabor(Labor labor) {
        lblId.setText(String.valueOf(labor.getId()));
        tfLaborCode.setText(labor.getName());
        taDesc.setText(labor.getDesc());
        tfBilledHrs.setText(String.valueOf(labor.getBilledHrs()));
        tfRate.setText(String.valueOf(labor.getRate()));
        cbTaxable.setSelected(labor.isTaxable());
    }

    public Labor buildLabor() {
        int id = Integer.parseInt(lblId.getText());
        String laborCode = tfLaborCode.getText();
        String desc = taDesc.getText();
        double billedHrs = Double.parseDouble(tfBilledHrs.getText());
        double rate = Double.parseDouble(tfRate.getText());
        boolean taxable = cbTaxable.isSelected();
        Labor labor = new Labor(laborCode, desc, billedHrs, rate, taxable);
        labor.setId(id);
        return labor;
    }
}
