package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
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
    StackedBarChart<String, Integer> bcIncome;

    public void initialize() {
        /* Set the default value of {cbYear} which will be the current year */
        ObservableList<Integer> years = DB.get().workOrders().getYearOptions();
        cbYear.setItems(years);
        cbYear.setValue(LocalDate.now().getYear());
        fetchIncomeData();
    }

    public void fetchIncomeData() {
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
            final XYChart.Series<String, Integer> series1 = new XYChart.Series<>();
            /* Create a series to display the actual */
            completedWorkOrders.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach((entry) -> {
                String m = entry.getKey().toString().toLowerCase();
                m = Character.toUpperCase(m.charAt(0)) + m.substring(1);
                int sum = entry.getValue().stream().mapToInt(x -> (int) x.bill()).reduce(Integer::sum).orElse(0);
                series1.getData().add(new XYChart.Data<>(m, sum));
            });
            series1.setName("Actual");
            /* Get all work orders (expected) */
            Map<Month, List<WorkOrder>> allWorkOrders = list.stream()
                    .collect(Collectors.groupingBy(x -> x.getDateCreated().toLocalDate().getMonth()));
            final XYChart.Series<String, Integer> series2 = new XYChart.Series<>();
            /* Create a series to display the expected */
            allWorkOrders.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
                String m = entry.getKey().toString().toLowerCase();
                m = Character.toUpperCase(m.charAt(0)) + m.substring(1);
                int sum = entry.getValue().stream().mapToInt(x -> (int) x.bill()).reduce(Integer::sum).orElse(0);
                series2.getData().add(new XYChart.Data<>(m, sum));
            });
            series2.setName("Expected");
            /* Display the series on the stacked bar chart */
            bcIncome.getData().setAll(series1, series2);
            bcIncome.setTitle(String.format("Gross Income Evaluation: Expected vs Actual - [%d]", chosenYear));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
