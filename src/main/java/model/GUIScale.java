package model;

import java.util.List;

public enum GUIScale {
    Small, Medium, Large, X_Large, XX_Large;

    public static String getStyleClass(GUIScale scale) {
        switch (scale) {
            case Small:
                return "scale_100";
            case Medium:
                return "scale_125";
            case Large:
                return "scale_150";
            case X_Large:
                return "scale_175";
            case XX_Large:
                return "scale_200";
        }
        return "scale_100";
    }

    public static List<String> styleClasses() {
        return List.of("scale_100", "scale_125", "scale_150", "scale_175", "scale_200");
    }

    public static List<GUIScale> list() {
        return List.of(GUIScale.values());
    }
}
