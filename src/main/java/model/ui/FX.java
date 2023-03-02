package model.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

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

    public static String loadCSS(String src) {
        URL url = FX.class.getClassLoader().getResource("css/" + src);
        return url.toExternalForm();
    }

    public static void autoResizeColumns(@NotNull TableView<?> tv, final double OFFSET) {
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

    public static void autoResizeColumns(@NotNull TableView<?> tv, double... offsets) {
        tv.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        for (int i = 0; i < tv.getColumns().size(); i++) {
            var c = tv.getColumns().get(i);
            var t = new Text(c.getText());
            var longestWidth = t.getLayoutBounds().getWidth();
            for (int j = 0; j < tv.getItems().size(); j++) {
                if (c.getCellData(j) != null) {
                    t = new Text(c.getCellData(j).toString());
                    var width = t.getLayoutBounds().getWidth();
                    if (width > longestWidth) {
                        longestWidth = width;
                    }
                }
            }
            if (offsets.length < i) {
                c.setPrefWidth(longestWidth + offsets[i]);
            } else {
                c.setPrefWidth(longestWidth + offsets[offsets.length-1]);
            }
        }
    }
}
