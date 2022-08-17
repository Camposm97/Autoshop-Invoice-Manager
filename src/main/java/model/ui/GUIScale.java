package model.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.List;

public enum GUIScale {
    Small, Medium, Large, X_Large, XX_Large;

    public static String getStyleClass(GUIScale scale) {
        switch (scale) {
            case Small:
                return "scale-100";
            case Medium:
                return "scale-125";
            case Large:
                return "scale-150";
            case X_Large:
                return "scale-175";
            case XX_Large:
                return "scale-200";
        }
        return "scale-100";
    }

    public static List<String> styleClasses() {
        return Arrays.stream(GUIScale.values()).map(x -> x.toString()).toList();
    }

    public static ObservableList<GUIScale> list() {
        return FXCollections.observableArrayList(GUIScale.values());
    }
}
