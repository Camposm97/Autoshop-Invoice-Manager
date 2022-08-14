package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.work_order.AutoPart;
import model.work_order.Labor;
import model.Preferences;
import model.work_order.WorkOrder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.function.Function;

public class WorkOrderPrintController {
    WorkOrder workOrder;
    @FXML
    Text txtDate, txtOwnerCompany, txtOwnerAddress, txtOwnerPhone, txtShop, txtTitle;
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
        txtDate.setText(workOrder.getDateCreated().toLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/u")));
        String workOrderId = String.valueOf(workOrder.getId());
        while (workOrderId.length() < 4) {
            workOrderId = '0' + workOrderId;
        }
        workOrderId = "Work Order # " + workOrderId;
        txtWorkOrderId.setText(workOrderId);
        txtOwnerCompany.setText(Preferences.get().getCompany());
        txtOwnerAddress.setText(getAddress());
        txtOwnerPhone.setText(Preferences.get().getPhone());
        txtShop.setText(getShopDetail());
        txtTitle.setText(Preferences.get().getSpecialTitle());
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

        Function<Double, String> f = x -> String.format("%.2f", x);
        Function<Double, String> g = x -> String.format("$ %.2f", x);

        Iterator<AutoPart> autoPartIterator = workOrder.autoPartIterator();
        for (int i = 1; autoPartIterator.hasNext(); i++) {
            AutoPart a = autoPartIterator.next();
            Text txtName = new Text(a.getName());
            Text txtDesc = new Text(a.getDesc());
            Text txtUnitPrice = new Text(f.apply(a.getRetailPrice()));
            Text txtQty = new Text(String.valueOf(a.getQuantity()));
            Text txtSubtotal = new Text(f.apply(a.subtotal()));
            txtName.setFont(Font.font(10));
            txtDesc.setFont(Font.font(10));
            txtUnitPrice.setFont(Font.font(10));
            txtQty.setFont(Font.font(10));
            txtSubtotal.setFont(Font.font(10));
            gridPaneParts.addRow(i, txtName, txtDesc, txtUnitPrice, txtQty, txtSubtotal);
        }
        Iterator<Labor> laborIterator = workOrder.laborIterator();
        for (int i = 1; laborIterator.hasNext(); i++) {
            Labor lbr = laborIterator.next();
            Text txtCode = new Text(lbr.getName());
            Text txtDesc = new Text(lbr.getDesc());
            Text txtSubtotal = new Text(f.apply(lbr.subtotal()));
            txtCode.setFont(Font.font(10));
            txtDesc.setFont(Font.font(10));
            txtDesc.setWrappingWidth(400);
            txtSubtotal.setFont(Font.font(10));
            gridPaneLabor.addRow(i, txtCode, txtDesc, txtSubtotal);
        }
        txtPartsTotal.setText(g.apply(workOrder.partsSubtotal()));
        txtLaborTotal.setText(g.apply(workOrder.laborSubtotal()));
        txtSubtotal.setText(g.apply(workOrder.subtotal()));
        txtSalesTax.setText(g.apply(workOrder.tax()));
        txtWorkOrderTotal.setText(g.apply(workOrder.bill()));
        txtTotalPayment.setText(g.apply(workOrder.totalPayments()));
        txtAmountDue.setText(g.apply(workOrder.balance()));
    }

    public String getAddress() {
        return Preferences.get().getAddress() + ' ' + Preferences.get().getCity() + ' ' + Preferences.get().getState() + ' ' + Preferences.get().getZip();
    }

    public String getShopDetail() {
     return Preferences.get().getState().getAbbreviation() + "S Repair Shop #" + Preferences.get().getRepairShopId();
    }
}
