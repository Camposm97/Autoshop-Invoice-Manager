package controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.Model;
import model.Preferences;
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
    Label cusHeader, vehHeader, partIdHeader, partDescHeader, partPriceHeader, partQtyHeader, partTotalHeader, lbrCodeHeader, lbrDescHeader, lbrTotalHeader;
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
        if (Preferences.get().getTheme() == Theme.Dark) {
            cusHeader.getStyleClass().add("titled-lbl-dark");
            vehHeader.getStyleClass().add("titled-lbl-dark");
            partIdHeader.getStyleClass().add("titled-lbl-dark");
            partDescHeader.getStyleClass().add("titled-lbl-dark");
            partPriceHeader.getStyleClass().add("titled-lbl-dark");
            partQtyHeader.getStyleClass().add("titled-lbl-dark");
            partTotalHeader.getStyleClass().add("titled-lbl-dark");
            lbrCodeHeader.getStyleClass().add("titled-lbl-dark");
            lbrDescHeader.getStyleClass().add("titled-lbl-dark");
            lbrTotalHeader.getStyleClass().add("titled-lbl-dark");
            cusHeader.getStyleClass().remove("titled-lbl");
            vehHeader.getStyleClass().remove("titled-lbl");
            partIdHeader.getStyleClass().remove("titled-lbl");
            partDescHeader.getStyleClass().remove("titled-lbl");
            partPriceHeader.getStyleClass().remove("titled-lbl");
            partQtyHeader.getStyleClass().remove("titled-lbl");
            partTotalHeader.getStyleClass().remove("titled-lbl");
            lbrCodeHeader.getStyleClass().remove("titled-lbl");
            lbrDescHeader.getStyleClass().remove("titled-lbl");
            lbrTotalHeader.getStyleClass().remove("titled-lbl");
        }
        lblDate.setText(workOrder.getDateCreated().toLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/u")));
        lblWorkOrderId.setText(formatWorkOrderId());
        lblOwnerCompany.setText(Model.get().preferences().getCompany());
        lblOwnerAddress.setText(getAddress());
        lblOwnerPhone.setText(Model.get().preferences().getPhone());
        lblShop.setText(getShopDetail());
        lblTitle.setText(Model.get().preferences().getSpecialTitle());

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
            lblName.getStyleClass().add("lbl-item-id");
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
            lblCode.getStyleClass().add("lbl-item-id");
            Text txtDesc = new Text(lbr.getDesc());
            Label lblSubtotal = new Label(f.apply(lbr.subtotal()));
            txtDesc.setWrappingWidth(400);
            if (Model.get().preferences().getTheme().equals(Theme.Dark)) txtDesc.setFill(Color.LIGHTGRAY);
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

    /**
     * Formats the work order id to a presentable format
     * @return Work order id prepended with 'Work Order # ' followed by the id as ##### (e.g. 00001 where the id is 1)
     */
    public String formatWorkOrderId() {
        String s = workOrder.getId().toString();
        if (workOrder.isNew()) s = DB.get().workOrders().getNextId().toString();
        final int SIZE = 5;
        while (s.length() < SIZE) {
            s = '0' + s;
        }
        return "Work Order # " + s;
    }

    public String getAddress() {
        return Model.get().preferences().getAddress() + ' ' + Model.get().preferences().getCity() + ' ' + Model.get().preferences().getState() + ' ' + Model.get().preferences().getZip();
    }

    public String getShopDetail() {
     return Model.get().preferences().getState().getAbbreviation() + "S Repair Shop #" + Model.get().preferences().getRepairShopId();
    }

    private void getAllLabels(Pane root, List<Label> labels) {
        for (Node n : root.getChildrenUnmodifiable()) {
            if (n instanceof Pane) {
                getAllLabels((Pane) n, labels);
            } else if (n instanceof Label) {
                var lbl = (Label) n;
                var styles = lbl.getStyleClass();
                if (!styles.contains("titled-lbl-dark") && !styles.contains("titled-lbl"))
                    labels.add(lbl);
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
        if (Preferences.get().getTheme() == Theme.Dark) {
            cusHeader.getStyleClass().remove("titled-lbl-dark");
            vehHeader.getStyleClass().remove("titled-lbl-dark");
            partIdHeader.getStyleClass().remove("titled-lbl-dark");
            partDescHeader.getStyleClass().remove("titled-lbl-dark");
            partPriceHeader.getStyleClass().remove("titled-lbl-dark");
            partQtyHeader.getStyleClass().remove("titled-lbl-dark");
            partTotalHeader.getStyleClass().remove("titled-lbl-dark");
            lbrCodeHeader.getStyleClass().remove("titled-lbl-dark");
            lbrDescHeader.getStyleClass().remove("titled-lbl-dark");
            lbrTotalHeader.getStyleClass().remove("titled-lbl-dark");
            cusHeader.getStyleClass().add("titled-lbl");
            vehHeader.getStyleClass().add("titled-lbl");
            partIdHeader.getStyleClass().add("titled-lbl");
            partDescHeader.getStyleClass().add("titled-lbl");
            partPriceHeader.getStyleClass().add("titled-lbl");
            partQtyHeader.getStyleClass().add("titled-lbl");
            partTotalHeader.getStyleClass().add("titled-lbl");
            lbrCodeHeader.getStyleClass().add("titled-lbl");
            lbrDescHeader.getStyleClass().add("titled-lbl");
            lbrTotalHeader.getStyleClass().add("titled-lbl");
        }
    }
}
