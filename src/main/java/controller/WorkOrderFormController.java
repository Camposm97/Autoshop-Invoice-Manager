package controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.AppModel;
import model.database.DB;
import model.ui.Theme;
import model.work_order.AutoPart;
import model.work_order.Labor;
import model.work_order.WorkOrder;

import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class WorkOrderFormController {
    WorkOrder workOrder;
    @FXML
    AnchorPane root;
    @FXML
    Label lblDate, lblOwnerCompany, lblOwnerAddress, lblOwnerPhone, lblShop, lblTitle;
    @FXML
    Label lblWorkOrderId;
    @FXML
    Label lblName, lblPhone, lblEmail, lblCompany, lblBillingAddress;
    @FXML
    Label lblVin, lblYear, lblMake, lblModel, lblLicensePlate, lblColor, lblEngine, lblTransmission, lblMileageInAndOut;
    @FXML
    GridPane gridPaneParts, gridPaneLabor;
    @FXML
    Label lblPartsTotal, lblLaborTotal, lblSubtotal, lblSalesTax, lblWorkOrderTotal, lblTotalPayment, lblAmountDue;

    public WorkOrderFormController(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    @FXML
    public void initialize() {
        lblDate.setText(workOrder.getDateCreated().toLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/u")));
        lblWorkOrderId.setText(getWorkOrderId());
        lblOwnerCompany.setText(AppModel.get().preferences().getCompany());
        lblOwnerAddress.setText(getAddress());
        lblOwnerPhone.setText(AppModel.get().preferences().getPhone());
        lblShop.setText(getShopDetail());
        lblTitle.setText(AppModel.get().preferences().getSpecialTitle());

        if (workOrder.getCustomer() != null) {
            lblName.setText(workOrder.getCustomer().getName());
            lblPhone.setText(workOrder.getCustomer().getPhone());
            lblEmail.setText(workOrder.getCustomer().getEmail());
            lblCompany.setText(workOrder.getCustomer().getCompany());
            lblBillingAddress.setText(workOrder.getCustomer().getAddress().toString());
        }

        if (workOrder.getVehicle() != null) {
            lblVin.setText(workOrder.getVehicle().getVin());
            lblYear.setText(String.valueOf(workOrder.getVehicle().getYear()));
            lblMake.setText(workOrder.getVehicle().getMake());
            lblModel.setText(workOrder.getVehicle().getModel());
            lblLicensePlate.setText(workOrder.getVehicle().getLicensePlate());
            lblColor.setText(workOrder.getVehicle().getColor());
            lblEngine.setText(workOrder.getVehicle().getEngine());
            lblTransmission.setText(workOrder.getVehicle().getTransmission());
            lblMileageInAndOut.setText(workOrder.getVehicle().getMileageInAndOut());
        }


        Function<Double, String> f = x -> String.format("%.2f", x);
        Function<Double, String> g = x -> String.format("$ %.2f", x);

        // Add parts to the form
        Iterator<AutoPart> autoPartIterator = workOrder.autoPartIterator();
        for (int i = 1; autoPartIterator.hasNext(); i++) {
            AutoPart a = autoPartIterator.next();
            Label lblName = new Label(a.getName());
            Label lblDesc = new Label(a.getDesc());
            Label lblUnitPrice = new Label(f.apply(a.getRetailPrice()));
            Label lblQty = new Label(String.valueOf(a.getQuantity()));
            Label lblSubtotal = new Label(f.apply(a.subtotal()));
            lblDesc.setWrapText(true);
            gridPaneParts.addRow(i, lblName, lblDesc, lblUnitPrice, lblQty, lblSubtotal);
        }

        // Add labors to the form
        Iterator<Labor> laborIterator = workOrder.laborIterator();
        for (int i = 1; laborIterator.hasNext(); i++) {
            Labor lbr = laborIterator.next();
            Label lblCode = new Label(lbr.getName());
            Text txtDesc = new Text(lbr.getDesc());
            Label lblSubtotal = new Label(f.apply(lbr.subtotal()));
            txtDesc.setWrappingWidth(400);
            if (AppModel.get().preferences().getTheme().equals(Theme.Dark)) txtDesc.setFill(Color.LIGHTGRAY);
//            txtDesc.getStyleClass().add("text");
//            txtDesc.setWrapText(true);
            gridPaneLabor.addRow(i, lblCode, txtDesc, lblSubtotal);
        }
        lblPartsTotal.setText(g.apply(workOrder.partsSubtotal()));
        lblLaborTotal.setText(g.apply(workOrder.laborSubtotal()));
        lblSubtotal.setText(g.apply(workOrder.subtotal()));
        lblSalesTax.setText(g.apply(workOrder.tax()));
        lblWorkOrderTotal.setText(g.apply(workOrder.bill()));
        lblTotalPayment.setText(g.apply(workOrder.totalPayments()));
        lblAmountDue.setText(g.apply(workOrder.balance()));
    }

    public String getWorkOrderId() {
        String s = workOrder.getId().toString();
        if (workOrder.isNew()) s = DB.get().workOrders().getNextId().toString();
        final int SIZE = 5;
        while (s.length() < SIZE) {
            s = '0' + s;
        }
        return "Work Order # " + s;
    }

    public String getAddress() {
        return AppModel.get().preferences().getAddress() + ' ' + AppModel.get().preferences().getCity() + ' ' + AppModel.get().preferences().getState() + ' ' + AppModel.get().preferences().getZip();
    }

    public String getShopDetail() {
     return AppModel.get().preferences().getState().getAbbreviation() + "S Repair Shop #" + AppModel.get().preferences().getRepairShopId();
    }

    private void getAllLabels(Pane root, List<Label> labels) {
        for (Node n : root.getChildrenUnmodifiable()) {
            if (n instanceof Pane) {
                getAllLabels((Pane) n, labels);
            } else if (n instanceof Label) {
                labels.add((Label) n);
                if (((Label) n).getGraphic() != null)
                    labels.add((Label) ((Label) n).getGraphic());
            } else if (n instanceof Text) {
                n.getStyleClass().add("light-mode-txt");
            }
        }
    }

    public void lightMode() {
        List<Label> labels = new LinkedList<>();
        getAllLabels(root, labels);
        labels.forEach(e -> e.getStyleClass().add("light-mode-lbl"));
        root.setStyle("-fx-background-color: transparent;");
    }
}
