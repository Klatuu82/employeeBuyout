package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.util.StringConverter;

/**
 * Hilfsklasse zur Erstellung und Bearbeitung der UI.
 */
class UIHelper {

    private final Map<String, TextField> textFields;
    private ObservableList<Node> nodes;

    UIHelper(Map<String, TextField> textFields, ObservableList<Node> nodes) {
        this.textFields = textFields;
        this.nodes = nodes;
    }

    Label createLabel(String name, double topAnchor, double leftAnchor, double minWidth, double maxWidth, int textSize) {
        Label label = new Label();
        label.setFont(new Font("Tahoma", textSize));
        AnchorPane.setTopAnchor(label, topAnchor);
        AnchorPane.setLeftAnchor(label, leftAnchor);
        if (minWidth != 0) {
            label.setMinWidth(minWidth);
        }
        if (maxWidth != 0) {
            label.setMaxWidth(maxWidth);
        }
        if (name != null) {
            label.setText(name);
        }
        nodes.add(label);
        return label;
    }

    private void createTextField(String name, double topAnchor, double leftAnchor, double minWidth, double maxWidth) {
        TextField textField = new TextField();
        AnchorPane.setTopAnchor(textField, topAnchor);
        AnchorPane.setLeftAnchor(textField, leftAnchor);
        if (minWidth != 0) {
            textField.setMaxWidth(minWidth);
        }
        if (maxWidth != 0) {
            textField.setMinWidth(maxWidth);
        }
        nodes.add(textField);
        textFields.put(name, textField);
    }

    private void setRestrictedChar(TextField textField) {
        textField.textProperty().addListener((observer, alt, neu) -> {
            if (!Mitarbeiter.isChar(textField.getText())) {
                textField.setText(alt);
            }
        });
    }

    private void setRestrictedNumber(TextField textField) {
        textField.textProperty().addListener((observer, alt, neu) -> {
            if (!Mitarbeiter.isNumeric(textField.getText())) {
                textField.setText(alt);
            }
        });
    }

    /**
     * Gibt eine neue ComboBox zurück vom Type X.
     *
     * @param topAnchor Ankerpunkt von oben.
     * @param leftAnchor Ankerpunkt von links.
     * @param minWidth minimale Breite
     * @param maxWidth maximale Breite
     * @param list ObservableList
     * @param <X> Type
     * @return {@link ComboBox}
     */
    <X> ComboBox<X> getMitarbeiterComboBox(double topAnchor, double leftAnchor, double minWidth, double maxWidth,
            ObservableList<X> list) {
        ComboBox<X> comboBox = new ComboBox<>();
        AnchorPane.setTopAnchor(comboBox, topAnchor);
        AnchorPane.setLeftAnchor(comboBox, leftAnchor);
        if (minWidth != 0) {
            comboBox.setMaxWidth(minWidth);
        }
        if (maxWidth != 0) {
            comboBox.setMinWidth(maxWidth);
        }
        comboBox.setItems(list);
        comboBox.setValue(list.get(0));
        nodes.add(comboBox);
        return comboBox;
    }

    Button getButton(String text, double topAnchor, double leftAnchor) {
        Button button = new Button();
        AnchorPane.setTopAnchor(button, topAnchor);
        AnchorPane.setLeftAnchor(button, leftAnchor);
        button.setMinWidth(140.0);
        button.setMaxWidth(140.0);
        if (text != null) {
            button.setText(text);
        }
        nodes.add(button);
        return button;
    }

    RadioButton getRadioButton(String text, double topAnchor) {
        RadioButton radioButton = new RadioButton();
        AnchorPane.setTopAnchor(radioButton, topAnchor);
        AnchorPane.setLeftAnchor(radioButton, (double) 655);
        if (text != null) {
            radioButton.setText(text);
        }
        nodes.add(radioButton);
        return radioButton;
    }

    DatePicker getDatePicker() {
        DatePicker datePicker = new DatePicker();
        AnchorPane.setTopAnchor(datePicker, (double) 520);
        AnchorPane.setLeftAnchor(datePicker, (double) 360);
        datePicker.setPromptText("dd.MM.yyyy".toLowerCase());
        datePicker.setConverter(new StringConverter<LocalDate>() {

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
        });
        datePicker.setShowWeekNumbers(false);
        nodes.add(datePicker);
        return datePicker;
    }

    void initLabels() {
        createLabel(Constant.CHOOSE_EMPLOYEE, 450, 20, 160, 160, 16);
        createLabel(Constant.TITLE, 10, 350, 0, 0, 40);
        createLabel(Constant.NEW_EMPLOYEE, 100, 120, 0, 0, 20);
        createLabel(Constant.NAME, 150, 20, 0, 0, 16);
        createLabel(Constant.VORNAME, 150, 170, 0, 0, 16);
        createLabel(Constant.EMPLOYEE_NUMBER, 150, 320, 0, 0, 16);
        createLabel(Constant.OUTPUT, 100, 600, 0, 0, 20);
        createLabel(Constant.BRANCH_OFFICE, 150, 550, 0, 0, 16);
        createLabel(Constant.ENTER_PURCHASE, 350, 400, 0, 0, 24);
        createLabel(Constant.ARTICLE_NUMBER, 450, 200, 0, 0, 16);
        createLabel(Constant.SIZE, 450, 350, 0, 0, 16);
        createLabel(Constant.PLAIN_PRICE, 450, 430, 0, 0, 16);
        createLabel(Constant.BUYING_PRICE, 450, 510, 0, 0, 16);
        createLabel(Constant.PERCENTAGE_TEXT, 440, 600, 0, 0, 16);
        createLabel(Constant.FINAL_PRICE, 450, 720, 0, 0, 16);
    }

    void initTextFields(ComboBox<Mitarbeiter> mitarbeiterComboBox) {
        createTextField(Constant.NAME, 180, 20, 0, 140);
        setRestrictedChar(textFields.get(Constant.NAME));
        createTextField(Constant.VORNAME, 180, 170, 0, 140);
        setRestrictedChar(textFields.get(Constant.VORNAME));
        createTextField(Constant.EMPLOYEE_NUMBER, 180, 320, 0, 140);
        setRestrictedNumber(textFields.get(Constant.EMPLOYEE_NUMBER));
        createTextField(Constant.CHOOSE_EMPLOYEE, 480, 20, 160, 160);
        // Textfeld zum filtern der MitarbeiterListe
        textFields.get(Constant.CHOOSE_EMPLOYEE).textProperty().addListener((observer, alt, neu) ->
        {
            if (textFields.get(Constant.CHOOSE_EMPLOYEE).getText() != null) {
                ObservableList<Mitarbeiter> mitarbeiterListe = Mitarbeiter.getMitarbeiterListe(textFields.get(Constant.CHOOSE_EMPLOYEE).getText());
                mitarbeiterComboBox.setItems(mitarbeiterListe);
                mitarbeiterComboBox.setValue(mitarbeiterListe.get(0));
            } else {
                ObservableList<Mitarbeiter> mitarbeiterListe = Mitarbeiter.getMitarbeiterListe();
                mitarbeiterComboBox.setItems(mitarbeiterListe);
                mitarbeiterComboBox.setValue(mitarbeiterListe.get(0));
            }
        });
        createTextField(Constant.ARTICLE_NUMBER, 480, 200, 140, 140);
        createTextField(Constant.SIZE, 480, 350, 70, 70);
        createTextField(Constant.PLAIN_PRICE, 480, 430, 70, 70);
        textFields.get(Constant.PLAIN_PRICE).setText("0,00");
        createTextField(Constant.BUYING_PRICE, 480, 510, 70, 70);
        textFields.get(Constant.BUYING_PRICE).setText("0,00");
        createTextField(Constant.PERCENTAGE, 480, 600, 50, 50);
        textFields.get(Constant.PERCENTAGE).setText("0");
        createTextField(Constant.FINAL_PRICE, 480, 720, 90, 90);
        createTextField(Constant.LAST_INPUT, 560, 20, 0, 600);
    }

    void resetEmployee() {
        textFields.get(Constant.NAME).setText("");
        textFields.get(Constant.VORNAME).setText("");
        textFields.get(Constant.EMPLOYEE_NUMBER).setText("");
    }

    void resetPurchase() {
        textFields.get(Constant.ARTICLE_NUMBER).setText("");
        textFields.get(Constant.SIZE).setText("");
        textFields.get(Constant.PLAIN_PRICE).setText("0,00");
        textFields.get(Constant.BUYING_PRICE).setText("0,00");
        textFields.get(Constant.PERCENTAGE).setText("0");
        textFields.get(Constant.FINAL_PRICE).setText("");
    }

    void setComboBox(ComboBox<Mitarbeiter> mitarbeiterComboBox) {
        ObservableList<Mitarbeiter> newMitarbeiterListe = Mitarbeiter.getMitarbeiterListe();
        mitarbeiterComboBox.setItems(newMitarbeiterListe);
        mitarbeiterComboBox.setValue(newMitarbeiterListe.get(0));
    }
}
