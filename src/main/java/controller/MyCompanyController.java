package controller;

import app.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import model.Observable;
import model.database.DB;
import model.ui.FX;
import model.work_order.WorkOrder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

/**
 * Lists completed work orders in the year and month, also displays recently edited work orders and incompleted work orders
 */
public class MyCompanyController implements Observable {
    @FXML
    Label lblWorkOrderYearCount, lblWorkOrderMonthCount;
    @FXML
    Label lblYear, lblMonth, lblIncompletedWorkOrderCount;
    @FXML
    TableView<WorkOrder> tvRecentWorkOrders;
    @FXML
    TableColumn<WorkOrder, Integer> colRecentId;
    @FXML
    TableColumn<WorkOrder, String> colRecentCustomer;
    @FXML
    TableColumn<WorkOrder, String> colRecentCompany;
    @FXML
    TableColumn<WorkOrder, String> colRecentVehicle;
    @FXML
    TableColumn<WorkOrder, String> colRecentDateCreated;
    @FXML
    TableColumn<WorkOrder, String> colRecentDateCompleted;
    @FXML
    TableColumn<WorkOrder, String> colRecentInvoiceTotal;
    @FXML
    TableView<WorkOrder> tvIncompletedWorkOrders;
    @FXML
    TableColumn<WorkOrder, Integer> colIncompletedId;
    @FXML
    TableColumn<WorkOrder, String> colIncompletedCustomer;
    @FXML
    TableColumn<WorkOrder, String> colIncompletedCompany;
    @FXML
    TableColumn<WorkOrder, String> colIncompletedVehicle;
    @FXML
    TableColumn<WorkOrder, String> colIncompletedDateCreated;
    @FXML
    TableColumn<WorkOrder, String> colIncompletedDateCompleted;
    @FXML
    TableColumn<WorkOrder, String> colIncompletedInvoiceTotal;

    @FXML
    public void initialize() {
        App.get().model().recentWorkOrders().addObserver(this);
        Function<MouseEvent, Boolean> selectWorkOrder = x -> x.getClickCount() == 2 && x.getButton().equals(MouseButton.PRIMARY);

        colRecentId.setCellValueFactory(c -> c.getValue().idProperty());
        colRecentCustomer.setCellValueFactory(c -> c.getValue().getCustomer().nameProperty());
        colRecentCompany.setCellValueFactory(c -> c.getValue().getCustomer().companyProperty());
        colRecentVehicle.setCellValueFactory(c -> c.getValue().vehicleProperty());
        colRecentDateCreated.setCellValueFactory(c -> c.getValue().dateCreatedProperty());
        colRecentDateCompleted.setCellValueFactory(c -> c.getValue().dateCompletedProperty());
        colRecentInvoiceTotal.setCellValueFactory(c -> c.getValue().billProperty());
        tvRecentWorkOrders.setOnMouseClicked(e -> {
            if (selectWorkOrder.apply(e)) editWorkOrder(tvRecentWorkOrders);
        });

        colIncompletedId.setCellValueFactory(c -> c.getValue().idProperty());
        colIncompletedCustomer.setCellValueFactory(c -> c.getValue().getCustomer().nameProperty());
        colIncompletedCompany.setCellValueFactory(c -> c.getValue().getCustomer().companyProperty());
        colIncompletedVehicle.setCellValueFactory(c -> c.getValue().vehicleProperty());
        colIncompletedDateCreated.setCellValueFactory(c -> c.getValue().dateCreatedProperty());
        colIncompletedDateCompleted.setCellValueFactory(c -> c.getValue().dateCompletedProperty());
        colIncompletedInvoiceTotal.setCellValueFactory(c -> c.getValue().billProperty());
        tvIncompletedWorkOrders.setOnMouseClicked(e -> {
            if (selectWorkOrder.apply(e)) editWorkOrder(tvIncompletedWorkOrders);
        });

        refreshCompleted();
        refreshIncompleted();
    }

    public void editWorkOrder(TableView<WorkOrder> tv) {
        WorkOrder workOrder = tv.getSelectionModel().getSelectedItem();
        if (workOrder != null) {
            try {
                FXMLLoader loader = FX.load("WorkOrderWorkspace.fxml");
                Parent node = loader.load();
                WorkOrderWorkspaceController controller = loader.getController();
                controller.loadWorkOrder(workOrder);
                App.get().display(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update() {
        tvRecentWorkOrders.getItems().setAll(DB.get().workOrders().getRecents());
        FX.autoResizeColumns(tvRecentWorkOrders,25);
    }

    public void refreshCompleted() {
        LocalDate currentDate = LocalDate.now();
        String strYear = "(" + currentDate.getYear() + "):";
        String strMonth = "(" + currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + "):";

        lblWorkOrderMonthCount.setText(String.valueOf(DB.get().workOrders().getCompletedWorkOrdersThisMonth()));
        lblWorkOrderYearCount.setText(String.valueOf(DB.get().workOrders().getCompletedWorkOrdersThisYear()));
        lblYear.setText(strYear);
        lblMonth.setText(strMonth);
    }

    public void refreshIncompleted() {
        List<WorkOrder> incompletedWorkOrders = DB.get().workOrders().getIncompletedWorkOrders();
        int incompletedWorkOrderCount = incompletedWorkOrders.size();

        lblIncompletedWorkOrderCount.setText(String.valueOf(incompletedWorkOrderCount));
        tvIncompletedWorkOrders.getItems().setAll(incompletedWorkOrders);

        FX.autoResizeColumns(tvIncompletedWorkOrders,75);
    }

    interface IObserve {
        void update(Object o);
    }
}
