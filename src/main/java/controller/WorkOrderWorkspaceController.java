package controller;

import app.App;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import model.Model;
import model.Observable;
import model.State;
import model.customer.Address;
import model.customer.Customer;
import model.customer.OwnedVehicle;
import model.database.DB;
import model.tps.*;
import model.ui.ChangeListenerFactory;
import model.ui.DialogFactory;
import model.ui.FX;
import model.ui.IOffsets;
import model.work_order.*;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.control.textfield.TextFields;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * @brief Workspace for work orders
 */
@SuppressWarnings("unused")
public class WorkOrderWorkspaceController implements Observable, IOffsets, IShortcuts {
    protected int chosenCustomerId;
    protected WorkOrder workOrder;
    protected TPS tpsProducts, tpsPayments;
    protected List<Product> productsMarkedForDeletion;
    protected List<WorkOrderPayment> paymentsMarkedForDeletion;
    protected CustomerTableController cusTableController;
    protected VehicleTableController vehTableController;
    @FXML
    Tab tabPartsAndLabor, tabWorkOrderInfo;
    @FXML
    Button btPrint, btAddLabor, btSideAddLabor;
    @FXML
    TextField tfFirstName, tfLastName, tfPhone, tfEmail, tfCompany, tfAddress, tfCity;
    @FXML
    SearchableComboBox<State> cbState;
    @FXML
    TextField tfZip;
    @FXML
    TextField tfVin, tfLicensePlate, tfColor, tfYear, tfMake, tfModel, tfEngine, tfTransmission, tfMileageIn, tfMileageOut;
    @FXML
    TableView<AutoPart> tvParts;
    @FXML
    TableColumn<AutoPart, String> colPartNumber, colPartDesc;
    @FXML
    TableColumn<AutoPart, Integer> colPartQuantity;
    @FXML
    TableColumn<AutoPart, String> colPartRetailPrice, colPartListPrice;
    @FXML
    TableColumn<AutoPart, String> colPartTotal;
    @FXML
    TableView<Labor> tvLabor;
    @FXML
    TableColumn<Labor, String> colLaborCode, colLaborDesc;
    @FXML
    TableColumn<Labor, Double> colLaborBilledHrs;
    @FXML
    TableColumn<Labor, String> colLaborRate;
    @FXML
    TableColumn<Labor, String> colLaborTotal;
    @FXML
    TextField tfPartsTotal, tfTaxTotal, tfDiscount, tfLaborTotal, tfSubtotal, tfTotal;
    @FXML
    TextField tfWorkOrderId;
    @FXML
    DatePicker dateCreated, dateCompletedPicker;
    @FXML
    TextField tfTaxRate;
    @FXML
    Button btCus, btVeh, btAddVeh, btEditAutoPart, btDelAutoPart, btEditLabor, btDelLabor, btEditPayment, btDelPayment;
    @FXML
    PopOver cusPopOver, vehPopOver;
    @FXML
    TableView<WorkOrderPayment> tvPayment;
    @FXML
    TableColumn<WorkOrderPayment, String> colPaymentDate;
    @FXML
    TableColumn<WorkOrderPayment, PaymentMethod> colPaymentType;
    @FXML
    TableColumn<WorkOrderPayment, String> colPaymentAmount;
    @FXML
    TextField tfTotalPayment, tfInvoiceBalance;

    @FXML
    GridPane vehicleGridPane1;

    public WorkOrderWorkspaceController() {
//        Model.get().addExitObservable(this);
        this.workOrder = new WorkOrder();
        this.tpsProducts = new TPS();
        this.tpsPayments = new TPS();
        this.productsMarkedForDeletion = new LinkedList<>();
        this.paymentsMarkedForDeletion = new LinkedList<>();
    }

    /**
     * Initializes what columns hold what values binds tables to
     * display parts and labor of a work order
     */
    @FXML
    public void initialize() throws IOException {
        loadShortcuts();
        /* Bind TextFields for auto-completion */
        TextFields.bindAutoCompletion(tfCompany, DB.get().customers().getUniqueCompanies());
        TextFields.bindAutoCompletion(tfAddress, DB.get().customers().getUniqueAddresses());
        TextFields.bindAutoCompletion(tfCity, DB.get().customers().getUniqueCities());
        cbState.setValue(State.UNKNOWN);
        cbState.setItems(State.list());
        TextFields.bindAutoCompletion(tfZip, DB.get().customers().getUniqueZips());
        TextFields.bindAutoCompletion(tfYear, DB.get().vehicles().getUniqueYear());
        TextFields.bindAutoCompletion(tfMake, DB.get().vehicles().getUniqueMake());
        TextFields.bindAutoCompletion(tfModel, DB.get().vehicles().getUniqueModel());
        TextFields.bindAutoCompletion(tfColor, DB.get().vehicles().getUniqueColor());
        TextFields.bindAutoCompletion(tfEngine, DB.get().vehicles().getUniqueEngine());
        TextFields.bindAutoCompletion(tfTransmission, DB.get().vehicles().getUniqueYear());

        tfWorkOrderId.setText(DB.get().workOrders().getNextId().toString());

        // Add Listeners to text fields vin and license plate
        ChangeListenerFactory f = new ChangeListenerFactory();
        f.setAlphaNums(tfFirstName);
        f.setAlphaNums(tfLastName);
        f.setVINFormat(tfVin);
        f.setUpperCase(tfLicensePlate);

        // Bind columns to appropriate fields in WorkOrder
        colPartNumber.setCellValueFactory(c -> c.getValue().nameProperty());
        colPartDesc.setCellValueFactory(c -> c.getValue().descProperty());
        colPartQuantity.setCellValueFactory(c -> c.getValue().quantityProperty());
        colPartRetailPrice.setCellValueFactory(c -> c.getValue().retailPriceProperty());
        colPartListPrice.setCellValueFactory(c -> c.getValue().listPriceProperty());
        colPartTotal.setCellValueFactory(c -> c.getValue().subtotalProperty());
        colLaborCode.setCellValueFactory(c -> c.getValue().nameProperty());
        colLaborDesc.setCellValueFactory(c -> c.getValue().descProperty());
        colLaborBilledHrs.setCellValueFactory(c -> c.getValue().billedHrsProperty());
        colLaborRate.setCellValueFactory(c -> c.getValue().rateProperty());
        colLaborTotal.setCellValueFactory(c -> c.getValue().subtotalProperty());
        colPaymentDate.setCellValueFactory(c -> c.getValue().dateProperty());
        colPaymentType.setCellValueFactory(c -> c.getValue().typeProperty());
        colPaymentAmount.setCellValueFactory(c -> c.getValue().amountProperty());

        tvParts.setItems(workOrder.itemList());
        tvLabor.setItems(workOrder.laborList());
        tvPayment.setItems(workOrder.paymentList());

        // Set double-click function for parts and labor tables
        // Set Parts and Labor Items
        final int DOUBLE_CLICK = 2;
        Function<MouseEvent, Boolean> doubleClicked = x -> x.getButton().equals(MouseButton.PRIMARY) && x.getClickCount() == DOUBLE_CLICK;
        tvParts.setOnMouseClicked(e -> {
            if (doubleClicked.apply(e)) editPart();
            if (tvParts.getSelectionModel().getSelectedItem() != null) {
                btEditAutoPart.setDisable(false);
                btDelAutoPart.setDisable(false);
            } else {
                btEditAutoPart.setDisable(true);
                btDelAutoPart.setDisable(true);
            }
        });
        tvParts.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.BACK_SPACE))
                deletePart();
        });
        tvLabor.setOnMouseClicked(e -> {
            if (doubleClicked.apply(e)) editLabor();
            if (tvLabor.getSelectionModel().getSelectedItem() != null) {
                btEditLabor.setDisable(false);
                btDelLabor.setDisable(false);
            } else {
                btEditLabor.setDisable(true);
                btDelLabor.setDisable(true);
            }
        });
        tvLabor.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.BACK_SPACE))
                deleteLabor();
        });
        tvLabor.getItems().addListener(laborChangeListener());
        tvPayment.setOnMouseClicked(e -> {
            if (doubleClicked.apply(e)) editPayment();
            if (tvPayment.getSelectionModel().getSelectedItem() != null) {
                btEditPayment.setDisable(false);
                btDelPayment.setDisable(false);
            } else {
                btEditPayment.setDisable(true);
                btDelPayment.setDisable(true);
            }
        });
        tvPayment.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.BACK_SPACE))
                deletePayment();
        });

        FX.autoResizeColumns(tvPayment, PAY_OFFSET);
        tvPayment.getItems().addListener((ListChangeListener<WorkOrderPayment>) change -> FX.autoResizeColumns(tvPayment, PAY_OFFSET));

        /* set date created value to current date */
        dateCreated.setValue(workOrder.getDateCreated().toLocalDate());
        dateCreated.setOnAction(e -> workOrder.setDateCreated(Date.valueOf(dateCreated.getValue())));

        tfTaxRate.setText(Model.get().preferences().getTaxRatePrettyString());

        btVeh.setDisable(true);
        btAddVeh.setDisable(true);
        btEditAutoPart.setDisable(true);
        btDelAutoPart.setDisable(true);
        btEditLabor.setDisable(true);
        btDelLabor.setDisable(true);
        btEditPayment.setDisable(true);
        btDelPayment.setDisable(true);

        /* initialize customer pop over */
        FXMLLoader fxmlLoader = FX.load("CustomerTable.fxml");
        assert fxmlLoader != null;
        Node cusTable = fxmlLoader.load();
        cusTableController = fxmlLoader.getController();
        cusTableController.connect(this);
        cusPopOver = new PopOver(cusTable);
        cusPopOver.setHeaderAlwaysVisible(true);
        cusPopOver.setTitle("Customer Picker");
        cusPopOver.setDetached(true);
        cusPopOver.setArrowSize(0);
        /* initialize vehicle pop over */
        fxmlLoader = FX.load("VehicleTable.fxml");
        assert fxmlLoader != null;
        Node vehTable = fxmlLoader.load();
        vehTableController = fxmlLoader.getController();
        vehTableController.connect(this);
        vehPopOver = new PopOver(vehTable);
        vehPopOver.setHeaderAlwaysVisible(true);
        vehPopOver.setTitle("Vehicle Picker");
        vehPopOver.setDetached(true);
        vehPopOver.setArrowSize(0);
        /* add listener */
        Model.get().preferences().addObserver(this);
    }

    public void loadShortcuts() {
        Runnable save = this::saveAndClose;
        Runnable print = this::print;
        Runnable undo = () -> {
            if (tabPartsAndLabor.isSelected()) {
                tpsProducts.undoTransaction();
            } else if (tabWorkOrderInfo.isSelected()) {
                tpsPayments.undoTransaction();
            }
            updateTotals();
        };
        Runnable redo = () -> {
            if (tabPartsAndLabor.isSelected()) {
                tpsProducts.doTransaction();
            } else if (tabWorkOrderInfo.isSelected()) {
                tpsPayments.doTransaction();
            }
            updateTotals();
        };
        App.get().accels().put(ACCEL_SAVE, save);
        App.get().accels().put(ACCEL_PRINT, print);
        App.get().accels().put(ACCEL_UNDO, undo);
        App.get().accels().put(ACCEL_REDO, redo);
    }

    public ListChangeListener<Labor> laborChangeListener() {
        return e -> {
            if (e.next()) {
                if (e.getList().size() >= 1) {
                    btAddLabor.setDisable(true);
                    btSideAddLabor.setDisable(true);
                } else {
                    btAddLabor.setDisable(false);
                    btSideAddLabor.setDisable(false);
                }
            }
        };
    }

    public void showCustomerPopOver() {
        btVeh.setDisable(true);
        btAddVeh.setDisable(true);
        cusTableController.fetchCustomers();
        cusPopOver.show(btCus);
    }

    public void showVehiclePopOver() {
        vehTableController.refresh(chosenCustomerId);
        vehPopOver.show(btVeh);
    }

    public void print() {
        buildWorkOrder();
        DialogFactory.initPrintWorkOrder(workOrder);
    }

    public void save() {
        buildWorkOrder();
        if (workOrder.isNew()) {
            workOrder = DB.get().workOrders().add(workOrder);
        } else {
            DB.get().workOrders().update(workOrder);
            DB.get().deleteProductsMarkedForDeletion(productsMarkedForDeletion);
            DB.get().deletePaymentMarkedForDeletion(paymentsMarkedForDeletion);
        }
        Model.get().recentWorkOrders().add(workOrder.getId());
        App.get().compView.fetchCompletedWorkOrderStats();
        App.get().compView.fetchIncompletedWorkOrders();
        App.get().log("Saved Work Order #" + workOrder.getId());
    }

    public void saveAndClose() {
        save();
        close();
    }

    /**
     * Event handler for when the button is clicked
     */
    public void close() {
//        Model.get().removeExitObservable(this);
        Model.get().preferences().removeObserver(this);
//        App.get().accels().remove(ACCEL_SAVE);
//        App.get().accels().remove(ACCEL_PRINT);
//        App.get().accels().remove(ACCEL_UNDO);
//        App.get().accels().remove(ACCEL_REDO);
        App.get().closeCurrentTab();
        /* Remove work order id from {currOWOs} if not new, otherwise enable menu item  */

        App.get().setDisableMIWO(false);
        Model.get().currOWOs().remove(workOrder.getId());
        /*
            Since this function is called after {save} the current work order is given an id and thus,
            checking if the work order is new is never true.
         */
    }

    /**
     * Event handler for when the tab is closed
     */
    public void closeTab() {
        /* Remove this controller from preference's observables */
        Model.get().preferences().removeObserver(this);
        if (workOrder.isNew()) {
            /* work order is new, enable the feature to create a new work order*/
            App.get().setDisableMIWO(false);
            App.get().log("Cancel and closed new work order");
        } else {
            /* work order not new, remove it from the model's list of currowos */
            Model.get().currOWOs().remove(workOrder.getId());
            App.get().log("Cancel and closed work order #" + workOrder.getId());
        }
    }

    /* TODO - Look into email APIs to start an email or maybe use Java's built-in AWT library to create an email and attach the work order as PDF */
    @Deprecated
    public void email() {
        Desktop desktop;
        if (Desktop.isDesktopSupported() && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {
            String email = tfEmail.getText();
            try {
                String subject = (Model.get().preferences().getCompany() + " Invoice #" + tfWorkOrderId.getText());
                subject = subject.replaceAll("\\s", "%20");
                String s = "mailto:%s?subject=%s";
                s = s.formatted(email, subject);
                URI mailto = new URI(s);
                desktop.mail(mailto);
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadWorkOrder(@NotNull WorkOrder workOrder) {
        this.workOrder = workOrder;
        loadCustomer(workOrder.getCustomer());
        loadVehicle(workOrder.getVehicle());
        tfTaxRate.setText(workOrder.getTaxRatePrettyString());
        tfWorkOrderId.setText(workOrder.getId().toString());
        if (workOrder.isCompleted()) {
            dateCompletedPicker.setValue(workOrder.getDateCompleted().toLocalDate());
        }
        tvParts.setItems(workOrder.itemList());
        tvLabor.setItems(workOrder.laborList());
        tvLabor.getItems().addListener(laborChangeListener());
        if (tvLabor.getItems().size() >= 1) {
            btAddLabor.setDisable(true);
            btSideAddLabor.setDisable(true);
        } else {
            btAddLabor.setDisable(false);
            btSideAddLabor.setDisable(false);
        }
        tvPayment.setItems(workOrder.paymentList());
        dateCreated.setValue(workOrder.getDateCreated().toLocalDate());
        updateTotals();
    }

    public void buildWorkOrder() {
        Customer customer = buildCustomer();
        Vehicle vehicle = buildVehicle();
        workOrder.setCustomer(customer);
        workOrder.setVehicle(vehicle);

        String strTaxRate = tfTaxRate.getText();
        strTaxRate = strTaxRate.substring(0, strTaxRate.length()-1);
        double taxRate = (Double.parseDouble(strTaxRate) / 100.0);
        workOrder.setTaxRate(taxRate);
        LocalDate dateCompleted = dateCompletedPicker.getValue();
        if (dateCompleted != null) {
            workOrder.setDateCompleted(Date.valueOf(dateCompleted));
        }
    }

    public void loadCustomer(@NotNull Customer c) {
        this.chosenCustomerId = c.getId();
        if (this.chosenCustomerId > 0) {
            /* customer saved in database */
            this.btVeh.setDisable(false);
            this.btAddVeh.setDisable(false);
        } else {
            /* customer not saved in database */
            this.btVeh.setDisable(true);
            this.btAddVeh.setDisable(true);
        }
        tfFirstName.setText(c.getFirstName());
        tfLastName.setText(c.getLastName());
        tfPhone.setText(c.getPhone());
        tfEmail.setText(c.getEmail());
        tfCompany.setText(c.getCompany());
        tfAddress.setText(c.getAddress().getStreet());
        tfCity.setText(c.getAddress().getCity());
        cbState.setValue(State.valueOfName(c.getAddress().getState()));
        tfZip.setText(c.getAddress().getZip());
    }

    public Customer buildCustomer() {
        String firstName = tfFirstName.getText();
        String lastName = tfLastName.getText();
        String phone = tfPhone.getText();
        String email = tfEmail.getText();
        String company = tfCompany.getText();
        String street = tfAddress.getText();
        String city = tfCity.getText();
        String state = cbState.getValue() == null ? State.UNKNOWN.name() : cbState.getValue().name();
        String zip = tfZip.getText();
        Address address = new Address(street, city, state, zip);
        return new Customer(chosenCustomerId, firstName, lastName, phone, email, company, address);
    }

    public void loadVehicle(@NotNull Vehicle v) {
        tfVin.setText(v.getVin());
        tfLicensePlate.setText(v.getLicensePlate());
        tfColor.setText(v.getColor());
        tfYear.setText(String.valueOf(v.getYear()));
        tfMake.setText(v.getMake());
        tfModel.setText(v.getModel());
        tfEngine.setText(v.getEngine());
        tfTransmission.setText(v.getTransmission());
        tfMileageIn.setText(v.getMileageIn());
        tfMileageOut.setText(v.getMileageOut());
    }

    public Vehicle buildVehicle() {
        String vin = tfVin.getText();
        String licensePlate = tfLicensePlate.getText();
        String color = tfColor.getText();
        String year = tfYear.getText();
        String make = tfMake.getText();
        String model = tfModel.getText();
        String engine = tfEngine.getText();
        String transmission = tfTransmission.getText();
        String mileageIn = tfMileageIn.getText();
        String mileageOut = tfMileageOut.getText();
        return new Vehicle(vin, year, make, model, licensePlate, color, engine, transmission, mileageIn, mileageOut);
    }

    public void addPart() {
        Function<AutoPart, Void> callback = x -> {
            AddAutoPartTransaction transaction = new AddAutoPartTransaction(workOrder, x);
            tpsProducts.addTransaction(transaction);
            return null;
        };
        DialogFactory.initAddPart(callback);
        updateTotals();
    }

    public void editPart() {
        AutoPart selectedItem = tvParts.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Function<AutoPart, Void> callback = x -> {
                UpdateAutoPartTransaction transaction = new UpdateAutoPartTransaction(workOrder, selectedItem, x);
                tpsProducts.addTransaction(transaction);
              return null;
            };
            DialogFactory.initEditPart(callback, selectedItem);
            updateTotals();
        }
    }

    public void deletePart() {
        AutoPart autoPart = tvParts.getSelectionModel().getSelectedItem();
        if (autoPart != null) {
            DeleteAutoPartTransaction transaction = new DeleteAutoPartTransaction(workOrder, autoPart, productsMarkedForDeletion);
            tpsProducts.addTransaction(transaction);
            updateTotals();
        }
    }

    public void addLabor() {
        Function<Labor, Void> callback = x -> {
//            if (x.getDesc().equals("AUTO_GENERATE")) {
//                Iterator<AutoPart> iterator = workOrder.autoPartIterator();
//                StringBuilder sb = new StringBuilder("Installed ");
//                while (iterator.hasNext()) {
//                    sb.append(iterator.next().getDesc());
//                    if (iterator.hasNext())
//                        sb.append(", ");
//                }
//                x.setDesc(sb.toString());
//            }
            AddLaborTransaction transaction = new AddLaborTransaction(workOrder, x);
            tpsProducts.addTransaction(transaction);
            return null;
        };
        DialogFactory.initAddLabor(workOrder.autoPartIterator(), callback);
        updateTotals();
    }

    public void editLabor() {
        Labor labor = tvLabor.getSelectionModel().getSelectedItem();
        if (labor != null) {
            Function<Labor, Void> callback = x -> {
                UpdateLaborTransaction transaction = new UpdateLaborTransaction(workOrder, labor, x);
                tpsProducts.addTransaction(transaction);
                return null;
            };
            DialogFactory.initEditLabor(workOrder.autoPartIterator(), callback, labor);
            updateTotals();
        }
    }

    public void deleteLabor() {
        Labor labor = tvLabor.getSelectionModel().getSelectedItem();
        if (labor != null) {
            DeleteLaborTransaction transaction = new DeleteLaborTransaction(workOrder, labor, productsMarkedForDeletion);
            tpsProducts.addTransaction(transaction);
            updateTotals();
        }
    }

    public void addPayment() {
        Function<WorkOrderPayment, Void> callback = x -> {
            AddPaymentTransaction transaction = new AddPaymentTransaction(workOrder, x);
            tpsPayments.addTransaction(transaction);
            return null;
        };
        DialogFactory.initAddPayment(callback);
        updateTotals();
    }

    public void editPayment() {
        WorkOrderPayment payment = tvPayment.getSelectionModel().getSelectedItem();
        if (payment != null) {
            Function<WorkOrderPayment, Void> callback = x -> {
                UpdatePaymentTransaction transaction = new UpdatePaymentTransaction(workOrder, payment, x);
                tpsPayments.addTransaction(transaction);
                return null;
            };
            DialogFactory.initEditPayment(callback, payment);
            updateTotals();
        }
    }

    public void deletePayment() {
        WorkOrderPayment payment = tvPayment.getSelectionModel().getSelectedItem();
        if (payment != null) {
            DeletePaymentTransaction transaction = new DeletePaymentTransaction(workOrder, payment, paymentsMarkedForDeletion);
            tpsPayments.addTransaction(transaction);
//            if (!payment.isNew()) {
//                paymentsMarkedForDeletion.add(payment);
//            }
//            workOrder.removePayment(payment);
            updateTotals();
        }
    }

    public void updateTotals() {
        Function<Double, String> f = x -> String.format("%.2f", x);
        tfPartsTotal.setText(f.apply(workOrder.partsSubtotal()));
        tfTaxTotal.setText(f.apply(workOrder.tax()));
        tfLaborTotal.setText(f.apply(workOrder.laborSubtotal()));
        tfSubtotal.setText(f.apply(workOrder.subtotal()));
        tfTotal.setText(f.apply(workOrder.bill()));
        tfTotalPayment.setText(f.apply(workOrder.totalPayments()));
        tfInvoiceBalance.setText(f.apply(workOrder.balance()));
    }

    public void fetchVehicleData() {
        String vin = tfVin.getText();
        DialogFactory.initDecodeVIN(vin, (v) -> {
            if (!v.getYear().isBlank())
                tfYear.setText(v.getYear());
            if (!v.getMake().isBlank())
                tfMake.setText(v.getMake());
            if (!v.getModel().isBlank())
                tfModel.setText(v.getModel());
            if (!v.getEngine().isBlank())
                tfEngine.setText(v.getEngine());
            if (!v.getTransmission().isBlank())
                tfTransmission.setText(v.getTransmission());
        });
    }

    /**
     * @brief Updates a customer's information if a customer is selected, or adds a new customer
     */
    public void updateCustomer() {
        if (this.chosenCustomerId > 0) {
            /* a customer is chosen */
            var c = buildCustomer();
            DB.get().customers().update(c);
            App.get().log("Updated customer: " + c.toPrettyString());
        } else {
            Customer c = buildCustomer();
            if (c.getId() > 0) {
                /* update customer */
                DB.get().customers().update(c);
                loadCustomer(c);
                App.get().log("Updated csutomer: " + c.toPrettyString());
            } else {
                /* add customer */
                var success = DB.get().customers().add(c);
                if (success) {
                    List<Customer> list = DB.get().customers().filter(c);
                    if (list.size() > 0) {
                        loadCustomer(list.get(0));
                        App.get().log("Added new customer");
                    } else {
                        App.get().log("Failed to add customer");
                    }
                } else {
                    App.get().log("Failed to add customer, there already exists a customer " + c.toPrettyString());
                }
            }
        }
    }

    /*
        TODO - Implement updateVehicle method
            Does the same thing as {addVehicle}. In addition, method will also handle updating an existing vehicle
            assuming its already selected. We know an existing vehicle is selected if it contains an id. The rpogram
            can then
     */

    /**
     * @brief Adds vehicle for selected customer of current work order
     * If the customer's id is valid, then a vehicle object is built and is passed to the vehicle store to be added
     * in the database, otherwise tell the user a customer needs to be selected first. If the vehicle was added
     * successfully, display feedback to the user that the action was a success, otherwise display feedback that the
     * vehicle couldn't be added.
     */
    public void addVehicle() {
        if (this.chosenCustomerId > 0) {
            /* valid customer, add a vehicle */
            Vehicle v = buildVehicle();
            OwnedVehicle ov = new OwnedVehicle(this.chosenCustomerId, v);
            var success = DB.get().vehicles().add(ov);
            if (success) { /* give feedback that the action was a success */
                App.get().log("Added new vehicle");
            } else { /* otherwise, give feedback of the failed action */
                App.get().log("Failed to add vehicle, customer is already assigned vehicle with VIN: " + tfVin.getText());
            }
        } else {
            /* failed to add vehicle */
            App.get().log("Please select a customer to add a vehicle");
        }
    }

    @Override
    public void update() {
        updateTotals();
        if (workOrder != null && workOrder.isNew()) {
            tfTaxRate.setText(Model.get().preferences().getTaxRatePrettyString());
        }
    }
}
