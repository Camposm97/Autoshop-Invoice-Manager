package controller;

import app.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import model.customer.Address;
import model.customer.Customer;
import model.database.DB;
import model.ui.AlertBuilder;
import model.ui.FX;
import model.ui.TableCellFactory;
import model.work_order.Vehicle;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Optional;

public class CustomerTableController {
    @FXML
    GridPane root;
    @FXML
    TextField tfFirstName, tfLastName, tfPhone, tfEmail, tfCompany, tfStreet, tfCity, tfState, tfZip;
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
    public void initialize() {
        tfFirstName.textProperty().addListener((o, oldValue, newValue) -> tvCustomer.getItems().setAll(DB.get().customers().filter(buildCustomer())));
        tfLastName.textProperty().addListener((o, oldValue, newValue) -> tvCustomer.getItems().setAll(DB.get().customers().filter(buildCustomer())));
        tfPhone.textProperty().addListener((o, oldValue, newValue) -> tvCustomer.getItems().setAll(DB.get().customers().filter(buildCustomer())));
        tfEmail.textProperty().addListener((o, oldValue, newValue) -> tvCustomer.getItems().setAll(DB.get().customers().filter(buildCustomer())));
        tfCompany.textProperty().addListener((o, oldValue, newValue) -> tvCustomer.getItems().setAll(DB.get().customers().filter(buildCustomer())));
        tfStreet.textProperty().addListener((o, oldValue, newValue) -> tvCustomer.getItems().setAll(DB.get().customers().filter(buildCustomer())));
        tfCity.textProperty().addListener((o, oldValue, newValue) -> tvCustomer.getItems().setAll(DB.get().customers().filter(buildCustomer())));
        tfState.textProperty().addListener((o, oldValue, newValue) -> tvCustomer.getItems().setAll(DB.get().customers().filter(buildCustomer())));
        tfZip.textProperty().addListener((o, oldValue, newValue) -> tvCustomer.getItems().setAll(DB.get().customers().filter(buildCustomer())));
        initCustomerTable();
        initVehicleTable();
    }

    public void initCustomerTable() {
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
        tvCustomer.getItems().setAll(DB.get().customers().getAll());
        FX.autoResizeColumns(tvCustomer, 10);

        tvCustomer.setOnMouseClicked(e -> {
            if (root.getChildren().contains(tvVehicle)) {
                if (getSelectedCustomer() != null) {
                    int customerId = getSelectedCustomer().getId();
                    // Get all vehicles with that customer id and display in vehicle table
                    tvVehicle.getItems().setAll(DB.get().vehicles().getAllByCustomerId(customerId));
                    FX.autoResizeColumns(tvVehicle, 25);
                    btDelCustomer.setDisable(false);
                    btWorkOrderWithCustomer.setDisable(false);
                    tvVehicle.setDisable(false);
                } else {
                    tvVehicle.getItems().clear();
                    btDelCustomer.setDisable(true);
                    btWorkOrderWithCustomer.setDisable(true);
                    tvVehicle.setDisable(true);
                }
            }
        });
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

        tvVehicle.setOnMouseClicked(e -> {
            if (root.getChildren().contains(tvCustomer)) {
                if (getSelectedVehicle() != null) {
                    btDelVehicle.setDisable(false);
                    btWorkOrderWithCustomerAndVehicle.setDisable(false);
                } else {
                    btDelVehicle.setDisable(true);
                    btWorkOrderWithCustomerAndVehicle.setDisable(true);
                }
            }
        });
    }

    public void refresh() {
        tvCustomer.getItems().setAll(DB.get().customers().getAll());
    }

    public void refreshCustomer() {
        Customer c = getSelectedCustomer();
        if (c != null) {
            tvVehicle.getItems().setAll(DB.get().vehicles().getAllByCustomerId(c.getId()));
        }
    }

    public void connect(@NotNull VehicleWorkspaceController controller) {
        disableEditing();
        tvCustomer.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 1) {
                Customer customer = getSelectedCustomer();
                if (customer != null) {
                    controller.loadCustomer(customer);

                    controller.customerPopOver.hide();
                }
            }
        });
        root.getChildren().remove(hBoxCusControls);
        root.getChildren().remove(tvVehicle);
        root.getChildren().remove(hBoxVehControls);
    }

    public void connect(@NotNull WorkOrderWorkspaceController controller) {
        disableEditing();
        tvCustomer.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 1) {
                Customer customer = getSelectedCustomer();
                if (customer != null) {
                    controller.loadCustomer(customer);
                    controller.customerPopOver.hide();
                }
            }
        });
        root.getChildren().remove(hBoxCusControls);
        root.getChildren().remove(tvVehicle);
        root.getChildren().remove(hBoxVehControls);
    }

    public void disableEditing() {
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

    public void deleteCustomer() {
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

    public void deleteVehicle() {
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

    public void createWorkOrderWithCustomer() {
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

    public void createWorkOrderWithCustomerAndVehicle() {
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
