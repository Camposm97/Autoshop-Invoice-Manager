package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import model.DateFilter;
import model.database.DB;
import model.work_order.WorkOrder;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GrossIncomeController {
    @FXML
    ComboBox<Integer> cbYear;
    @FXML
    StackedBarChart<String, Double> bcIncome;

    public void initialize() {
        /* Set the default value of {cbYear} which will be the current year */
        ObservableList<Integer> years = DB.get().workOrders().getYearOptions();
        cbYear.setItems(years);
        cbYear.setValue(LocalDate.now().getYear());
        cbYearCallback();
    }

    public void cbYearCallback() {
        /* Fetch the chosen year and update {bcIncome} */
        int chosenYear = cbYear.getValue();
        /* Set local dates to cover whole year for filtering work orders */
        LocalDate date1 = LocalDate.of(chosenYear, Month.JANUARY, 1);
        LocalDate date2 = LocalDate.of(chosenYear, Month.DECEMBER, 31);
        try { /* Try to fetch all work orders within {chosenYear} and display them */
            List<WorkOrder> list = DB.get().workOrders().filter(0, "", "", "",
                    "", "", "", DateFilter.BETWEEN, date1, date2);
            /* Get completed work orders (actual) */
            Map<Month, List<WorkOrder>> completedWorkOrders = list.stream()
                    .filter(WorkOrder::isCompleted)
                    .collect(Collectors.groupingBy(x -> x.getDateCompleted().toLocalDate().getMonth()));
            final XYChart.Series<String, Double> series1 = new XYChart.Series<>();
            /* Create a series to display the actual */
            completedWorkOrders.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach((entry) -> {
                String m = entry.getKey().toString().toLowerCase();
                m = Character.toUpperCase(m.charAt(0)) + m.substring(1);
                double sum = entry.getValue().stream().mapToDouble(x -> x.bill()).reduce(Double::sum).orElse(0);
                XYChart.Data<String, Double> data = new XYChart.Data<>(m, sum);
                series1.getData().add(data);
            });
            series1.setName("Actual");
            /* Get all work orders (expected) */
            Map<Month, List<WorkOrder>> allWorkOrders = list.stream()
                    .collect(Collectors.groupingBy(x -> x.getDateCreated().toLocalDate().getMonth()));
            final XYChart.Series<String, Double> series2 = new XYChart.Series<>();
            /* Create a series to display the expected */
            allWorkOrders.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
                String m = entry.getKey().toString().toLowerCase();
                m = Character.toUpperCase(m.charAt(0)) + m.substring(1);
                double sum = entry.getValue().stream().mapToDouble(x -> x.bill()).reduce(Double::sum).orElse(0);
                XYChart.Data<String, Double> data = new XYChart.Data<>(m, sum);
                series2.getData().add(data);
            });
            series2.setName("Expected");
            /* Display the series on the stacked bar chart */
            bcIncome.getData().setAll(series1, series2);
            bcIncome.setTitle(String.format("Gross Income Evaluation: Expected vs Actual - [%d]", chosenYear));
            for (XYChart.Series<String, Double> s : bcIncome.getData()) {
                for (XYChart.Data<String, Double> d : s.getData()) {
                    if (d.getYValue() == null) continue;
                    Label dataLabel = new Label(formatCurrency(d.getYValue()));
                    dataLabel.setStyle("-fx-text-fill: white;");
                    StackPane.setAlignment(dataLabel, Pos.CENTER);
                    StackPane bar = (StackPane) d.getNode();
                    bar.getChildren().add(dataLabel);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refresh() {
        cbYearCallback();
    }

    /**
     * Formats to thousands syntax (1.0K)
     * @param x
     * @return
     */
    private String formatCurrency(double x) {
        return String.format("%.1f K", (x / 1000.0));
    }
}
