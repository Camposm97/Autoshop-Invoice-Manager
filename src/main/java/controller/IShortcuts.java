package controller;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 * For declaration of shortcuts for use throughout the program
 */
public interface IShortcuts {
    KeyCodeCombination ACCEL_CLOSE =
            new KeyCodeCombination(KeyCode.W, KeyCodeCombination.SHORTCUT_DOWN);
    KeyCodeCombination ACCEL_SAVE =
            new KeyCodeCombination(KeyCode.W, KeyCodeCombination.SHORTCUT_DOWN);
    KeyCodeCombination ACCEL_PRINT =
            new KeyCodeCombination(KeyCode.P, KeyCodeCombination.SHORTCUT_DOWN);
    KeyCodeCombination ACCEL_UNDO =
            new KeyCodeCombination(KeyCode.Z, KeyCodeCombination.SHORTCUT_DOWN);
    KeyCodeCombination ACCEL_REDO =
            new KeyCodeCombination(KeyCode.Z, KeyCodeCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN);
}
