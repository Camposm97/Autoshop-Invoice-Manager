package model.ui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Cell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.security.Key;

public class CustomTableCell<S,T> extends TableCell<S,T> {
    private TextField tf;
    private ObjectProperty<StringConverter<T>> converter;
    private ChangeListener<String> changeListener;
    private EventHandler<KeyEvent> keyPressedHandler;

    public CustomTableCell() {
        this.getStyleClass().add("text-field-table-cell");
        this.converter = new SimpleObjectProperty<>(this, "converter");
        this.converter.set(new StringConverter<T>() {
            @Override
            public String toString(T t) {
                return String.valueOf(t);
            }

            @Override
            public T fromString(String s) {
                return (T) s;
            }
        }); {

        }
    }

    public void setChangeListener(ChangeListener<String> changeListener) {
        this.changeListener = changeListener;
    }

    public void setKeyPressedHandler(EventHandler<KeyEvent> keyPressedHandler) {
        this.keyPressedHandler = keyPressedHandler;
    }

    @Override
    public void startEdit() {
        if (isEditable() && getTableView().isEditable() && this.getTableColumn().isEditable()) {
            super.startEdit();
            if (this.isEditing()) {
                if (tf == null) {
                    tf = Helper.createTextField(this, converter.get());
                    if (changeListener != null) tf.textProperty().addListener(changeListener);
                    if (keyPressedHandler != null) tf.setOnKeyPressed(keyPressedHandler);
                }
                Helper.startEdit(this, converter.get(), null, null, tf);
            }
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        Helper.cancelEdit(this, converter.get(), null);
    }

    @Override
    protected void updateItem(T t, boolean b) {
        super.updateItem(t, b);
        Helper.updateItem(this, converter.get(), null, null, tf);
    }

    public TextField getTf() {
        return tf;
    }

    class Helper {
        private static <T> String getItemText(Cell<T> var0, StringConverter<T> var1) {
            return var1 == null ? (var0.getItem() == null ? "" : var0.getItem().toString()) : var1.toString(var0.getItem());
        }

        static <T> void updateItem(Cell<T> var0, StringConverter<T> var1, HBox var2, Node var3, TextField var4) {
            if (var0.isEmpty()) {
                var0.setText((String) null);
                var0.setGraphic((Node) null);
            } else if (var0.isEditing()) {
                if (var4 != null) {
                    var4.setText(getItemText(var0, var1));
                }

                var0.setText((String) null);
                if (var3 != null) {
                    var2.getChildren().setAll(new Node[]{var3, var4});
                    var0.setGraphic(var2);
                } else {
                    var0.setGraphic(var4);
                }
            } else {
//                var0.setText(getItemText(var0, var1));
                var0.setText(var0 == null ? "" : var0.getItem().toString());
                var0.setGraphic(var3);
            }

        }

        static <T> void startEdit(Cell<T> var0, StringConverter<T> var1, HBox var2, Node var3, TextField var4) {
            if (var4 != null) {
                var4.setText(getItemText(var0, var1));
            }

            var0.setText((String) null);
            if (var3 != null) {
                var2.getChildren().setAll(new Node[]{var3, var4});
                var0.setGraphic(var2);
            } else {
                var0.setGraphic(var4);
            }

            var4.selectAll();
            var4.requestFocus();
        }

        static <T> void cancelEdit(Cell<T> var0, StringConverter<T> var1, Node var2) {
            var0.setText(getItemText(var0, var1));
            var0.setGraphic(var2);
        }

        static <T> TextField createTextField(Cell<T> var0, StringConverter<T> var1) {
            TextField var2 = new TextField(getItemText(var0, var1));
            var2.setOnAction((var3) -> {
                if (var1 == null) {
                    throw new IllegalStateException("Attempting to convert text input into Object, but provided StringConverter is null. Be sure to set a StringConverter in your cell factory.");
                } else {
                    var0.commitEdit(var1.fromString(var2.getText()));
                    var3.consume();
                }
            });
            var2.setOnKeyReleased((var1x) -> {
                if (var1x.getCode() == KeyCode.ESCAPE) {
                    var0.cancelEdit();
                    var1x.consume();
                }

            });
            return var2;
        }
    }
}
