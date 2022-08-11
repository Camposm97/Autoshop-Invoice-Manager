package model.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;

public class FX {
    public static Parent view(String src) {
        try {
            URL url = FX.class.getClassLoader().getResource("view/" + src);
            return FXMLLoader.load(url);
        } catch (IOException e) {
            e.printStackTrace();
            return new Pane();
        }
    }

    public static FXMLLoader load(String src) {
        try {
            URL url = FX.class.getClassLoader().getResource("view/" + src);
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(url);
            return fxmlLoader;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static Parent view(String src, Object controller) {
        try {
            URL url = FX.class.getClassLoader().getResource("view/" + src);
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setController(controller);
            fxmlLoader.setLocation(url);
            return fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return new Pane();
        }
    }

    public static void autoResizeColumns(TableView<?> tv, final double OFFSET) {
        tv.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tv.getColumns().stream().forEach((col) -> {
            Text t = new Text(col.getText());
            double currentWidth = t.getLayoutBounds().getWidth();
            for (int i = 0; i < tv.getItems().size(); i++) {
                if (col.getCellData(i) != null) {
                    t = new Text(col.getCellData(i).toString());
                    double width = t.getLayoutBounds().getWidth();
                    if (width > currentWidth) {
                        currentWidth = width;
                    }
                }
            }
            col.setPrefWidth(currentWidth + OFFSET);
        });
    }
}
