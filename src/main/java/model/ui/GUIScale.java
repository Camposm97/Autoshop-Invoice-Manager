package model.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.List;

public enum GUIScale {
    Small, Medium, Large, X_Large;

    public static String getStyleClass(GUIScale scale) {
        switch (scale) {
            case Small:
                return "scale-small";
            case Medium:
                return "scale-medium";
            case Large:
                return "scale-large";
            case X_Large:
                return "scale-x-large";
        }
        return "scale-small";
    }

    public static List<String> styleClasses() {
        return Arrays.stream(GUIScale.values()).map(x -> getStyleClass(x)).toList();
    }

    public static ObservableList<GUIScale> list() {
        return FXCollections.observableArrayList(GUIScale.values());
    }
}
