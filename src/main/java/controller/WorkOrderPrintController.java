package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.AutoPart;
import model.Labor;
import model.Preferences;
import model.WorkOrder;

import java.util.Iterator;

public class WorkOrderPrintController {
    WorkOrder workOrder;
    @FXML
    Text txtOwnerCompany, txtOwnerStreet, txtOwnerCityStateAndZip, txtOwnerPhone;
    @FXML
    Text txtWorkOrderId;
    @FXML
    Text txtName, txtPhone, txtEmail, txtCompany, txtBillingAddress;
    @FXML
    Text txtVin, txtYear, txtMake, txtModel, txtLicensePlate, txtColor, txtEngine, txtTransmission, txtMileageInAndOut;
    @FXML
    GridPane gridPaneParts, gridPaneLabor;
    @FXML
    Text txtPartsTotal, txtLaborTotal, txtSubtotal, txtSalesTax, txtWorkOrderTotal, txtTotalPayment, txtAmountDue;

    public WorkOrderPrintController(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    @FXML
    public void initialize() {
        String workOrderId = String.valueOf(workOrder.getId());
        while (workOrderId.length() < 4) {
            workOrderId = '0' + workOrderId;
        }
        txtOwnerCompany.setText(Preferences.get().getCompany());
        txtOwnerStreet.setText(Preferences.get().getAddress());
        txtOwnerCityStateAndZip.setText(Preferences.get().getCity() + ", " + Preferences.get().getState() + " " + Preferences.get().getZip());
        txtOwnerPhone.setText(Preferences.get().getPhone());

        txtWorkOrderId.setText(workOrderId);
        txtName.setText(workOrder.getCustomer().getName());
        txtPhone.setText(workOrder.getCustomer().getPhone());
        txtEmail.setText(workOrder.getCustomer().getEmail());
        txtCompany.setText(workOrder.getCustomer().getCompany());
        txtBillingAddress.setText(workOrder.getCustomer().getAddress().toString());
        txtVin.setText(workOrder.getVehicle().getVin());
        txtYear.setText(String.valueOf(workOrder.getVehicle().getYear()));
        txtMake.setText(workOrder.getVehicle().getMake());
        txtModel.setText(workOrder.getVehicle().getModel());
        txtLicensePlate.setText(workOrder.getVehicle().getLicensePlate());
        txtColor.setText(workOrder.getVehicle().getColor());
        txtEngine.setText(workOrder.getVehicle().getEngine());
        txtTransmission.setText(workOrder.getVehicle().getTransmission());
        txtMileageInAndOut.setText(workOrder.getVehicle().getMileageInAndOut());
        Iterator<AutoPart> autoPartIterator = workOrder.autoPartIterator();
        for (int i = 1; autoPartIterator.hasNext(); i++) {
            AutoPart ap = autoPartIterator.next();
            Text txtName = new Text(ap.getName());
            Text txtDesc = new Text(ap.getDesc());
            Text txtUnitPrice = new Text(String.format("%.2f", ap.getRetailPrice()));
            Text txtQty = new Text(String.valueOf(ap.getQuantity()));
            Text txtSubtotal = new Text(String.format("%.2f", ap.subtotal()));
            gridPaneParts.addRow(i, txtName, txtDesc, txtUnitPrice, txtQty, txtSubtotal);
        }
        Iterator<Labor> laborIterator = workOrder.laborIterator();
        for (int i = 1; laborIterator.hasNext(); i++) {
            Labor lbr = laborIterator.next();
            Text txtCode = new Text(lbr.getName());
            Text txtDesc = new Text(lbr.getDesc());
            Text txtRate = new Text(String.format("%.2f", lbr.getRate()));
            Text txtBilledHrs = new Text(String.valueOf(lbr.getBilledHrs()));
            Text txtSubtotal = new Text(String.format("%.2f", lbr.subtotal()));
            gridPaneLabor.addRow(i, txtCode, txtDesc, txtRate, txtBilledHrs, txtSubtotal);
        }
    }
}
