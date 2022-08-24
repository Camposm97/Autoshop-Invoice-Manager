package model.ui;

import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import model.customer.Customer;
import model.work_order.Vehicle;

public class TableCellFactory {
    public TableCell<Vehicle, String> initVinTableCell() {
        CustomTableCell<Vehicle, String> cell = new CustomTableCell<>();
        cell.setChangeListener((o,x,y) -> {
            if (y != null) {
                if (y.length() > 17) {
                    cell.getTf().setText(x);
                } else {
                    cell.getTf().setText(y.toUpperCase());
                }
            }
        });
        return cell;
    }

    public TableCell<Vehicle, String> initLicensePlateTableCell() {
        CustomTableCell<Vehicle, String> cell = new CustomTableCell<>();
        cell.setChangeListener((o,x,y) -> {
            if (y != null) cell.getTf().setText(y.toUpperCase());
        });
        return cell;
    }

    public  TableCell<Customer, String>  initPhoneTableCell() {
        CustomTableCell<Customer, String> cell = new CustomTableCell<>();
        cell.setChangeListener((o,x,y) -> {
            if (y == null) return;
            if (y.length() == 3) {
                cell.getTf().appendText("-");
            }
            if (y.length() == 7) {
                cell.getTf().appendText("-");
            }
            if (y.length() > 12) {
                cell.getTf().setText(x);
            }
        });
        cell.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.BACK_SPACE)) cell.getTf().clear();
        });
        return cell;
    }
}
