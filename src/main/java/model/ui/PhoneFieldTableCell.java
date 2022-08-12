//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package model.ui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

public class PhoneFieldTableCell<S, T> extends TableCell<S, T> {
    private TextField textField;
    private ObjectProperty<StringConverter<T>> converter;

    public PhoneFieldTableCell() {
        this.converter = new SimpleObjectProperty(this, "converter");
        this.getStyleClass().add("text-field-table-cell");
        this.setConverter(new StringConverter<T>() {
            @Override
            public String toString(T t) {
                return String.valueOf(t);
            }

            @Override
            public T fromString(String s) {
                return (T) s;
            }
        });
    }

    public final ObjectProperty<StringConverter<T>> converterProperty() {
        return this.converter;
    }

    public final void setConverter(StringConverter<T> var1) {
        this.converterProperty().set(var1);
    }

    public final StringConverter<T> getConverter() {
        return (StringConverter)this.converterProperty().get();
    }


    @Override
    public void startEdit() {
        if (this.isEditable() && this.getTableView().isEditable() && this.getTableColumn().isEditable()) {
            super.startEdit();
            if (this.isEditing()) {
                if (this.textField == null) {
                    this.textField = CellUtils.createTextField(this, this.getConverter());
                    textField.textProperty().addListener((o, oldValue, newValue) -> {
                        if (newValue == null) return;
                        if (newValue.length() == 3) {
                            textField.appendText("-");
                        }
                        if (newValue.length() == 7) {
                            textField.appendText("-");
                        }
                        if (newValue.length() > 12) {
                            textField.setText(oldValue);
                        }
                    });
                    textField.setOnKeyPressed(e -> {
                        if (e.getCode().equals(KeyCode.BACK_SPACE)) {
                            textField.clear();
                        }
                    });
                }

                CellUtils.startEdit(this, this.getConverter(), (HBox)null, (Node)null, this.textField);
            }

        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        CellUtils.cancelEdit(this, this.getConverter(), (Node)null);
    }

    @Override
    public void updateItem(T var1, boolean var2) {
        super.updateItem(var1, var2);
        CellUtils.updateItem(this, this.getConverter(), (HBox)null, (Node)null, this.textField);
    }

    class CellUtils {
        private static <T> String getItemText(Cell<T> var0, StringConverter<T> var1) {
            return var1 == null ? (var0.getItem() == null ? "" : var0.getItem().toString()) : var1.toString(var0.getItem());
        }

        static <T> void updateItem(Cell<T> var0, StringConverter<T> var1, HBox var2, Node var3, ChoiceBox<T> var4) {
            if (var0.isEmpty()) {
                var0.setText((String) null);
                var0.setGraphic((Node) null);
            } else if (var0.isEditing()) {
                if (var4 != null) {
                    var4.getSelectionModel().select(var0.getItem());
                }

                var0.setText((String) null);
                if (var3 != null) {
                    var2.getChildren().setAll(new Node[]{var3, var4});
                    var0.setGraphic(var2);
                } else {
                    var0.setGraphic(var4);
                }
            } else {
                var0.setText(getItemText(var0, var1));
                var0.setGraphic(var3);
            }

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
                var0.setText(getItemText(var0, var1));
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
