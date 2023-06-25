package controller;

import app.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import model.Model;
import model.Observable;
import model.database.DB;
import model.ui.FX;
import model.ui.IOffsets;
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
public class MyCompanyController implements Observable, IOffsets {
    @FXML
    TitledPane tpRecentWorkOrders;
    @FXML
    TitledPane tpUncompletedWorkOrders;
    @FXML
    TitledPane tpIncome;
    @FXML
    Label lblWorkOrderYearCount, lblWorkOrderMonthCount;
    @FXML
    Label lblYear, lblMonth;
    @FXML
    TableView<WorkOrder> tvRecentWorkOrders;
    @FXML
    TableColumn<WorkOrder, Integer> colRecentId;
    @FXML
    TableColumn<WorkOrder, String> colRecentCustomer;
    @FXML
    TableColumn<WorkOrder, String> colRecentCompany,  colRecentVehicle, colRecentDateCreated, colRecentDateCompleted, colRecentInvoiceTotal;
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
    StackPane spIncome;

    /* Class Fields */
    Parent incomeUI;
    GrossIncomeController controller;

    @FXML
    public void initialize() {
        Model.get().recentWorkOrders().addObserver(this);
        Function<MouseEvent, Boolean> doubleClick = x -> x.getClickCount() == 2 && x.getButton().equals(MouseButton.PRIMARY);
        fetchCompletedWorkOrderStats();

        /* Initialize recent work order table */
        colRecentId.setCellValueFactory(c -> c.getValue().idProperty());
        colRecentCustomer.setCellValueFactory(c -> c.getValue().getCustomer().nameProperty());
        colRecentCompany.setCellValueFactory(c -> c.getValue().getCustomer().companyProperty());
        colRecentVehicle.setCellValueFactory(c -> c.getValue().vehicleProperty());
        colRecentDateCreated.setCellValueFactory(c -> c.getValue().dateCreatedProperty());
        colRecentDateCompleted.setCellValueFactory(c -> c.getValue().dateCompletedProperty());
        colRecentInvoiceTotal.setCellValueFactory(c -> c.getValue().billProperty());
        tvRecentWorkOrders.setOnMouseClicked(e -> {
            if (doubleClick.apply(e)) editWorkOrder(tvRecentWorkOrders);
        });
        fetchRecentWorkOrders();

        /* Initialize incomplete work order table */
        colIncompletedId.setCellValueFactory(c -> c.getValue().idProperty());
        colIncompletedCustomer.setCellValueFactory(c -> c.getValue().getCustomer().nameProperty());
        colIncompletedCompany.setCellValueFactory(c -> c.getValue().getCustomer().companyProperty());
        colIncompletedVehicle.setCellValueFactory(c -> c.getValue().vehicleProperty());
        colIncompletedDateCreated.setCellValueFactory(c -> c.getValue().dateCreatedProperty());
        colIncompletedDateCompleted.setCellValueFactory(c -> c.getValue().dateCompletedProperty());
        colIncompletedInvoiceTotal.setCellValueFactory(c -> c.getValue().billProperty());
        tvIncompletedWorkOrders.setOnMouseClicked(e -> {
            if (doubleClick.apply(e)) editWorkOrder(tvIncompletedWorkOrders);
        });
        fetchIncompletedWorkOrders();

        /* Load gross income overview of work orders */
        tpIncome.expandedProperty().addListener((o, x, y) -> {
            if (y) {
                if (incomeUI == null) {
                    FXMLLoader fxmlLoader = FX.load("GrossIncome.fxml");
                    try {
                        incomeUI = fxmlLoader.load();
                        spIncome.getChildren().add(incomeUI);
                        controller = fxmlLoader.getController();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public void editWorkOrder(TableView<WorkOrder> tv) {
        WorkOrder workOrder = tv.getSelectionModel().getSelectedItem();
        App.get().showWorkOrder(workOrder);
    }

    @Override
    public void update() {
        tvRecentWorkOrders.getItems().setAll(DB.get().workOrders().getRecentWorkOrderEdits());
        FX.autoResizeColumns(tvRecentWorkOrders, WO_OFFSET);
    }

    public void fetchRecentWorkOrders() {
        tvRecentWorkOrders.getItems().setAll(DB.get().workOrders().getRecentWorkOrderEdits());
        FX.autoResizeColumns(tvRecentWorkOrders, WO_OFFSET);
    }

    public void fetchCompletedWorkOrderStats() {
        LocalDate currentDate = LocalDate.now();
        String strYear = "(" + currentDate.getYear() + "):";
        String strMonth = "(" + currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + "):";

        lblWorkOrderMonthCount.setText(String.valueOf(DB.get().workOrders().getCompletedWorkOrdersThisMonth()));
        lblWorkOrderYearCount.setText(String.valueOf(DB.get().workOrders().getCompletedWorkOrdersThisYear()));
        lblYear.setText(strYear);
        lblMonth.setText(strMonth);
    }

    public void fetchIncompletedWorkOrders() {
        List<WorkOrder> uncompletedWorkOrders = DB.get().workOrders().getUncompletedWorkOrders();
        int uncompletedWorkOrdersCount = uncompletedWorkOrders.size();
        final String TITLE = "Uncompleted Work Orders (" + uncompletedWorkOrdersCount + ")";
        tpUncompletedWorkOrders.setText(TITLE);
        tvIncompletedWorkOrders.getItems().setAll(uncompletedWorkOrders);

        FX.autoResizeColumns(tvIncompletedWorkOrders, WO_OFFSET);
    }
}
