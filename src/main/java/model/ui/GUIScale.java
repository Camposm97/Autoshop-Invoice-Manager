package model.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.List;

public enum GUIScale {
    SMALL, MEDIUM, LARGE, X_LARGE, XX_LARGE;

    public static String getStyleClass(GUIScale scale) {
        switch (scale) {
            case SMALL:
                return "scale-100";
            case MEDIUM:
                return "scale-125";
            case LARGE:
                return "scale-150";
            case X_LARGE:
                return "scale-175";
            case XX_LARGE:
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
