package controller;

import app.App;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import model.customer.Address;
import model.customer.Customer;
import model.database.DB;
import model.ui.AlertBuilder;
import model.ui.FX;
import model.ui.IOffsets;
import model.ui.TableCellFactory;
import model.work_order.Vehicle;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class CustomerTableController implements IOffsets {
    @FXML
    GridPane root, gridInputFields;
    @FXML HBox hBoxAllCustomers;
    @FXML
    TabPane tabPane;
    @FXML
    TextField tfFirstName, tfLastName, tfPhone, tfEmail, tfCompany, tfStreet, tfCity, tfState, tfZip;
    @FXML
    Button btAllCustomers;
    @FXML
    TableView<Customer> tvCustomer;
    @FXML
    TableColumn<Customer, String> colFirstName, colLastName, colPhone, colEmail, colCompany, colAddress, colCity, colState, colZip;
    @FXML
    TableView<Vehicle> tvVehicle;
    @FXML
    TableColumn<Vehicle, String> colVin, colLicensePlate, colColor, colYear, colMake, colModel, colEngine, colTransmission;
    @FXML
    Button btDelCustomer, btWorkOrderWithCustomer, btDelVehicle, btWorkOrderWithCustomerAndVehicle;
    @FXML
    HBox hBoxCusControls, hBoxVehControls;
    @FXML
    GridPane workOrderView;
    @FXML
    WorkOrderTableController workOrderViewController;

    private ChangeListener<Customer> customerSelectedListener, vehicleWorkspaceListener, workOrderWorkspaceListener;

    @FXML
    public void initialize() throws IOException {
        initCustomerInputFields();
        initCustomerTable();
        initVehicleTable();
        fetchCustomers();
        resizeCustomerTable();
        workOrderViewController.disableFields();
        workOrderViewController.clear();
        workOrderViewController.isEmbedded = true;
    }

    /**
     * Write down something here
     * @brief Initializes listeners for input fields
     * Hello there
     */
    public void initCustomerInputFields() {
        tfFirstName.textProperty().addListener((o, oldValue, newValue) -> handleSearchCustomers());
        tfLastName.textProperty().addListener((o, oldValue, newValue) -> handleSearchCustomers());
        tfPhone.textProperty().addListener((o, oldValue, newValue) -> handleSearchCustomers());
        tfEmail.textProperty().addListener((o, oldValue, newValue) -> handleSearchCustomers());
        tfCompany.textProperty().addListener((o, oldValue, newValue) -> handleSearchCustomers());
        tfStreet.textProperty().addListener((o, oldValue, newValue) -> handleSearchCustomers());
        tfCity.textProperty().addListener((o, oldValue, newValue) -> handleSearchCustomers());
        tfState.textProperty().addListener((o, oldValue, newValue) -> handleSearchCustomers());
        tfZip.textProperty().addListener((o, oldValue, newValue) -> handleSearchCustomers());
    }

    public void initCustomerTable() {
        customerSelectedListener = (o, m, c) -> {
            if (root.getChildren().contains(tabPane)) {
                if (c != null) {
                    int customerId = c.getId();
                    /* Get all vehicles with that customer id and display in vehicle table */
                    tvVehicle.setItems(DB.get().vehicles().getAllByCustomerId(customerId));
                    btDelCustomer.setDisable(false);
                    btWorkOrderWithCustomer.setDisable(false);
                    tvVehicle.setDisable(false);
                    workOrderViewController.tv.setDisable(false);
                    workOrderViewController.load(getSelectedCustomer());
                } else {
                    tvVehicle.getItems().clear();
                    workOrderViewController.tv.setDisable(true);
                    btDelCustomer.setDisable(true);
                    btWorkOrderWithCustomer.setDisable(true);
                    tvVehicle.setDisable(true);
                }
            }
        };
        vehicleWorkspaceListener = null;
        workOrderWorkspaceListener = null;
        TableCellFactory factory = new TableCellFactory();
        colFirstName.setCellValueFactory(c -> c.getValue().firstNameProperty());
        colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        colFirstName.setOnEditCommit(e -> {
            int i = e.getTablePosition().getRow();
            Customer customer = e.getTableView().getItems().get(i);
            customer.setFirstName(e.getNewValue());
            DB.get().customers().update(customer);
        });
        colLastName.setCellValueFactory(c -> c.getValue().lastNameProperty());
        colLastName.setCellFactory(TextFieldTableCell.forTableColumn());
        colLastName.setOnEditCommit(e -> {
            int i = e.getTablePosition().getRow();
            Customer customer = e.getTableView().getItems().get(i);
            customer.setLastName(e.getNewValue());
            DB.get().customers().update(customer);
        });
        colPhone.setCellValueFactory(c -> c.getValue().phoneProperty());
        colPhone.setCellFactory(x -> factory.initPhoneTableCell());
        colPhone.setOnEditCommit(e -> {
            int i = e.getTablePosition().getRow();
            Customer customer = e.getTableView().getItems().get(i);
            customer.setPhone(e.getNewValue());
            DB.get().customers().update(customer);
        });
        colEmail.setCellValueFactory(c -> c.getValue().emailProperty());
        colEmail.setCellFactory(TextFieldTableCell.forTableColumn());
        colEmail.setOnEditCommit(e -> {
            int i = e.getTablePosition().getRow();
            Customer customer = e.getTableView().getItems().get(i);
            customer.setEmail(e.getNewValue());
            DB.get().customers().update(customer);
        });
        colCompany.setCellValueFactory(c -> c.getValue().companyProperty());
        colCompany.setCellFactory(TextFieldTableCell.forTableColumn());
        colCompany.setOnEditCommit(e -> {
            int i = e.getTablePosition().getRow();
            Customer customer = e.getTableView().getItems().get(i);
            customer.setCompany(e.getNewValue());
            DB.get().customers().update(customer);
        });
        colAddress.setCellValueFactory(c -> c.getValue().getAddress().streetProperty());
        colAddress.setCellFactory(TextFieldTableCell.forTableColumn());
        colAddress.setOnEditCommit(e -> {
            int i = e.getTablePosition().getRow();
            Customer customer = e.getTableView().getItems().get(i);
            customer.getAddress().setStreet(e.getNewValue());
            DB.get().customers().update(customer);
        });
        colCity.setCellValueFactory(c -> c.getValue().getAddress().cityProperty());
        colCity.setCellFactory(TextFieldTableCell.forTableColumn());
        colCity.setOnEditCommit(e -> {
            int i = e.getTablePosition().getRow();
            Customer customer = e.getTableView().getItems().get(i);
            customer.getAddress().setCity(e.getNewValue());
            DB.get().customers().update(customer);
        });
        colState.setCellValueFactory(c -> c.getValue().getAddress().stateProperty());
        colState.setCellFactory(TextFieldTableCell.forTableColumn());
        colState.setOnEditCommit(e -> {
            int i = e.getTablePosition().getRow();
            Customer customer = e.getTableView().getItems().get(i);
            customer.getAddress().setState(e.getNewValue());
            DB.get().customers().update(customer);
        });
        colZip.setCellValueFactory(c -> c.getValue().getAddress().zipProperty());
        colZip.setCellFactory(TextFieldTableCell.forTableColumn());
        colZip.setOnEditCommit(e -> {
            int i = e.getTablePosition().getRow();
            Customer customer = e.getTableView().getItems().get(i);
            customer.getAddress().setZip(e.getNewValue());
            DB.get().customers().update(customer);
        });
//        ContextMenu cm = new ContextMenu();
//        MenuItem mi1 = new MenuItem("Show All Customers");
//        mi1.setOnAction(e -> tvCustomer.setItems(DB.get().customers().getAll(0)));
//        cm.getItems().add(mi1);
//        tvCustomer.setContextMenu(cm);
        /* Better way to add a listener when a customer is selected */
        tvCustomer.getSelectionModel().selectedItemProperty().addListener(customerSelectedListener);
    }

    public void initVehicleTable() {
        TableCellFactory factory = new TableCellFactory();
        colVin.setCellValueFactory(c -> c.getValue().vinProperty());
        colVin.setCellFactory(c -> factory.initVinTableCell());
        colVin.setOnEditCommit(e -> {
            int i = e.getTablePosition().getRow();
            Vehicle v = e.getTableView().getItems().get(i);
            v.setVin(e.getNewValue());
            DB.get().vehicles().update(v);
        });
        colLicensePlate.setCellValueFactory(c -> c.getValue().licensePlateProperty());
        colLicensePlate.setCellFactory(c -> factory.initLicensePlateTableCell());
        colLicensePlate.setOnEditCommit(e -> {
            int i = e.getTablePosition().getRow();
            Vehicle v = e.getTableView().getItems().get(i);
            v.setLicensePlate(e.getNewValue());
            DB.get().vehicles().update(v);
        });
        colColor.setCellValueFactory(c -> c.getValue().colorProperty());
        colColor.setCellFactory(TextFieldTableCell.forTableColumn());
        colColor.setOnEditCommit(e -> {
            int i = e.getTablePosition().getRow();
            Vehicle v = e.getTableView().getItems().get(i);
            v.setColor(e.getNewValue());
            DB.get().vehicles().update(v);
        });
        colYear.setCellValueFactory(c -> c.getValue().yearProperty());
        colYear.setCellFactory(TextFieldTableCell.forTableColumn());
        colYear.setOnEditCommit(e -> {
            int i = e.getTablePosition().getRow();
            Vehicle v = e.getTableView().getItems().get(i);
            v.setYear(e.getNewValue());
            DB.get().vehicles().update(v);
        });
        colMake.setCellValueFactory(c -> c.getValue().makeProperty());
        colMake.setCellFactory(TextFieldTableCell.forTableColumn());
        colMake.setOnEditCommit(e -> {
            int i = e.getTablePosition().getRow();
            Vehicle v = e.getTableView().getItems().get(i);
            v.setMake(e.getNewValue());
            DB.get().vehicles().update(v);
        });
        colModel.setCellValueFactory(c -> c.getValue().modelProperty());
        colModel.setCellFactory(TextFieldTableCell.forTableColumn());
        colModel.setOnEditCommit(e -> {
            int i = e.getTablePosition().getRow();
            Vehicle v = e.getTableView().getItems().get(i);
            v.setModel(e.getNewValue());
            DB.get().vehicles().update(v);
        });
        colEngine.setCellValueFactory(c -> c.getValue().engineProperty());
        colEngine.setCellFactory(TextFieldTableCell.forTableColumn());
        colEngine.setOnEditCommit(e -> {
            int i = e.getTablePosition().getRow();
            Vehicle v = e.getTableView().getItems().get(i);
            v.setEngine(e.getNewValue());
            DB.get().vehicles().update(v);
        });
        colTransmission.setCellValueFactory(c -> c.getValue().transmissionProperty());
        colTransmission.setCellFactory(TextFieldTableCell.forTableColumn());
        colTransmission.setOnEditCommit(e -> {
            int i = e.getTablePosition().getRow();
            Vehicle v = e.getTableView().getItems().get(i);
            v.setTransmission(e.getNewValue());
            DB.get().vehicles().update(v);
        });
        ChangeListener<Vehicle> listener = (o, m, v) -> {
            if (root.getChildren().contains(tvCustomer)) {/* Check if customer table is on display */
                if (getSelectedVehicle() != null) { /* Check if there is a selected vehicle */
                    /* Enable delete vehicle button */
                    btDelVehicle.setDisable(false);
                    /* Enable create work order with selected customer and vehicle button */
                    btWorkOrderWithCustomerAndVehicle.setDisable(false);
                } else { /* Otherwise, disable the buttons */
                    btDelVehicle.setDisable(true);
                    btWorkOrderWithCustomerAndVehicle.setDisable(true);
                }
            }
        };
        tvVehicle.getSelectionModel().selectedItemProperty().addListener(listener);
    }

    public void handleSearchCustomers() {
        new Thread(() -> {
            List<Customer> list = DB.get().customers().filter(buildCustomer());
            tvCustomer.getItems().setAll(list);
        }).start();
    }

    public void resizeCustomerTable() {
        /* Apply offsets and resize customer table columns */
        FX.autoResizeColumns(tvCustomer, CUS_OFFSETS);
    }

    /**
     * Fetches {50} customers from the database to display on customer table
     */
    public void fetchCustomers() {
        /* As of now, we get the first {LIMIT} which should be fine */
        final int LIMIT = 50;
        tvCustomer.setItems(DB.get().customers().getAll(LIMIT));
    }

    /**
     * Fetches all customers from the database to display on customer table
     */
    public void fetchAllCustomers() {
        new Thread(() -> tvCustomer.setItems(DB.get().customers().getAll(0))).start();
    }

    public void fetchVehiclesOfSelectedCustomer() {
        Customer c = getSelectedCustomer();
        if (c != null) {
            tvVehicle.getItems().setAll(DB.get().vehicles().getAllByCustomerId(c.getId()));
        }
    }

    /**
     * Enables the customer table controller to work with the vehicle workspace controller using
     * listener when a customer is selected to load customer information to the vehicle workspace.
     * @param controller
     * @see VehicleWorkspaceController
     */
    public void connect(@NotNull VehicleWorkspaceController controller) {
        disableEditableCustomers();
        /* Define listener */
        vehicleWorkspaceListener = (o, m, c) -> {
            Customer customer = getSelectedCustomer();
            if (customer != null) {
                controller.loadCustomer(customer);
                controller.cusPopOver.hide();
            }
        };
        updateListeners(vehicleWorkspaceListener);
    }

    /**
     * Enables the customer table controller to work with the work order workspace controller using
     * listener when a customer is selected to load customer information to the work order workspace.
     * @param controller
     */
    public void connect(@NotNull WorkOrderWorkspaceController controller) {
        disableEditableCustomers();
        /* Define listener */
        workOrderWorkspaceListener = (o, m, c) -> {
            Customer customer = getSelectedCustomer();
            if (customer != null) {
                controller.loadCustomer(customer);
                controller.cusPopOver.hide();
            }
        };
        updateListeners(workOrderWorkspaceListener);
    }

    /**
     * Removes {customerSelectedListener} and modifies layout to display only
     * the input fields and customer table
     * @param listener
     */
    public void updateListeners(ChangeListener<Customer> listener) {
        tvCustomer.getSelectionModel().selectedItemProperty().removeListener(customerSelectedListener);
        tvCustomer.getSelectionModel().selectedItemProperty().addListener(listener);
        gridInputFields.getChildren().remove(hBoxAllCustomers);
        root.getChildren().remove(hBoxCusControls);
        root.getChildren().remove(tabPane);
        root.getChildren().remove(hBoxVehControls);
        tvCustomer.setPrefHeight(Region.USE_COMPUTED_SIZE);
    }

    /**
     * Disables the feature to edit customer data via the customer table
     */
    public void disableEditableCustomers() {
        colFirstName.setEditable(false);
        colLastName.setEditable(false);
        colPhone.setEditable(false);
        colEmail.setEditable(false);
        colCompany.setEditable(false);
        colAddress.setEditable(false);
        colCity.setEditable(false);
        colState.setEditable(false);
        colZip.setEditable(false);
    }

    /**
     * @return Customer object that is never null where the customer data is generated by the input fields
     */
    public Customer buildCustomer() {
        String firstName = tfFirstName.getText();
        String lastName = tfLastName.getText();
        String phone = tfPhone.getText();
        String email = tfEmail.getText();
        String company = tfCompany.getText();
        String street = tfStreet.getText();
        String city = tfCity.getText();
        String state = tfState.getText();
        String zip = tfZip.getText();
        Address address = new Address(street, city, state, zip);
       return new Customer(firstName, lastName, phone, email, company, address);
    }

    public Customer getSelectedCustomer() {
        return tvCustomer.getSelectionModel().getSelectedItem();
    }

    public Vehicle getSelectedVehicle() {
        return tvVehicle.getSelectionModel().getSelectedItem();
    }

    /**
     * Displays a confirmation dialog to confirm deletion of selected customer
     */
    public void deleteSelectedCustomer() {
        // Delete Customer and Vehicles owned by customer
        Customer cus = getSelectedCustomer();
        AlertBuilder builder = new AlertBuilder();
        builder.setAlertType(Alert.AlertType.CONFIRMATION)
                .setTitle("Delete Customer")
                .setHeaderText("Are you sure you want to delete this customer?")
                .setContentText(cus.toFormattedString() + "\n" + "Deleting a customer will delete all associated vehicles.")
                .setYesNoBtns();
        Alert alert = builder.build();
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (!e.getButtonData().isCancelButton()) {
                DB.get().vehicles().deleteByCustomerId(cus.getId());
                DB.get().customers().deleteById(cus.getId());
                tvCustomer.getItems().remove(cus);
                tvVehicle.getItems().clear();
                btDelCustomer.setDisable(true);
                btWorkOrderWithCustomer.setDisable(true);
            }
        });
    }

    /**
     * Displays a confirmation dialog to confirm deleteion of selected vehicle
     */
    public void deleteSelectedVehicle() {
        Vehicle v = getSelectedVehicle();
        AlertBuilder builder = new AlertBuilder();
        builder.setAlertType(Alert.AlertType.CONFIRMATION).setTitle("Delete Vehicle").setHeaderText("Are you sure you want to delete this vehicle?").setContentText(v.toString()).setYesNoBtns();
        Optional<ButtonType> rs = builder.build().showAndWait();
        rs.ifPresent(e -> {
            if (!e.getButtonData().isCancelButton()) {
                DB.get().vehicles().deleteById(v.getId());
                tvVehicle.getItems().remove(v);
                btDelVehicle.setDisable(true);
                btWorkOrderWithCustomerAndVehicle.setDisable(true);
            }
        });
    }

    public void createWorkOrderWithSelectedCustomer() {
        FXMLLoader loader = FX.load("WorkOrderWorkspace.fxml");
        try {
            Parent node = loader.load();
            WorkOrderWorkspaceController c = loader.getController();
            c.loadCustomer(getSelectedCustomer());
            App.get().display(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createWorkOrderWithSelectedCustomerAndVehicle() {
        FXMLLoader loader = FX.load("WorkOrderWorkspace.fxml");
        try {
            Parent node = loader.load();
            WorkOrderWorkspaceController c = loader.getController();
            c.loadCustomer(getSelectedCustomer());
            c.loadVehicle(getSelectedVehicle());
            App.get().display(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
