package model.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public enum Theme {
    LIGHT, DARK;

    public static ObservableList<Theme> list() {
        return FXCollections.observableArrayList(Theme.values());
    }
}
