package controller;

import app.App;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import model.ui.AlertFactory;
import model.ui.FX;

import javax.sound.sampled.Port;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.rmi.server.ExportException;
import java.util.List;
import java.util.Scanner;

public class AppController {
    @FXML
    BorderPane root;
    @FXML
    MenuBar menuBar;

    public void addCustomer() {
        AlertFactory.showAddCustomer();
    }

    public void addVehicle() {
        AlertFactory.showAddVehicle();
    }

    public void addWorkOrder() {
        App.display(FX.view("WorkOrderWorkspace.fxml"));
    }

    public void exportCustomers() throws Exception {
        ProcessBuilder builder = new ProcessBuilder("ls");
        builder.redirectErrorStream(true);
        Process process = builder.start();
        Scanner s = new Scanner(process.getInputStream());
        while (s.hasNextLine()) {
            System.out.println(s.nextLine());
        }
        int code = process.waitFor();
        System.out.println(code);
    }

    public void exportVehicles() {

    }

    public void exportAutoPartSuggestions() {

    }

    public void exportWorkOrders() {

    }

    public void viewMyCompany() {
        App.display(FX.view("MyCompany.fxml"));
    }

    public void viewCustomers() {
        App.display(FX.view("CustomerTable.fxml"));
    }

    public void viewWorkOrders() {
        App.display(FX.view("WorkOrderTable.fxml"));
    }

    public void preferences() {
        AlertFactory.showPreferences();
    }

    public void about() {
        AlertFactory.showAbout();
    }

    public void exit() {
        App.getRecentWorkOrders().save();
        Platform.exit();
    }
}
