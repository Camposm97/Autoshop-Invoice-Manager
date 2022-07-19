package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.database.DB;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 *
 */
public class MyCompanyController {
    @FXML
    Label lblWorkOrderYearCount, lblWorkOrderMonthCount;
    @FXML
    Label lblYear, lblMonth, lblIncompletedWorkOrderCount;
    @FXML
    public void initialize() {
        int count = DB.get().workOrders().getUncompletedWorkOrderCount();

        LocalDate currentDate = LocalDate.now();
        String strYear = "(" + currentDate.getYear() + "):";
        String strMonth = "(" + currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + "):";

        lblWorkOrderMonthCount.setText(String.valueOf(DB.get().workOrders().getCompletedWorkOrdersThisYear()));
        lblWorkOrderYearCount.setText(String.valueOf(DB.get().workOrders().getCompletedWorkOrdersThisMonth()));
        lblYear.setText(strYear);
        lblMonth.setText(strMonth);
        lblIncompletedWorkOrderCount.setText(String.valueOf(count));
    }
}
