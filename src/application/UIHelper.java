package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.CustomLabel;
import javafx.scene.control.CustomTextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

/**
 * Hilfsklasse zur Erstellung und Bearbeitung der UI.
 */
class UIHelper {

    private ObservableList<Node> nodes;

    UIHelper(ObservableList<Node> nodes) {
        this.nodes = nodes;
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
        nodes.add(new CustomLabel(Constant.TF_CHOOSE_EMPLOYEE, 450, 20, 160, 160, 16));
        nodes.add(new CustomLabel(Constant.TITLE, 10, 350, 0, 0, 40));
        nodes.add(new CustomLabel(Constant.NEW_EMPLOYEE, 100, 120, 0, 0, 20));
        nodes.add(new CustomLabel(Constant.TF_NAME, 150, 20, 0, 0, 16));
        nodes.add(new CustomLabel(Constant.TF_VORNAME, 150, 170, 0, 0, 16));
        nodes.add(new CustomLabel(Constant.TF_EMPLOYEE_NUMBER, 150, 320, 0, 0, 16));
        nodes.add(new CustomLabel(Constant.OUTPUT, 100, 600, 0, 0, 20));
        nodes.add(new CustomLabel(Constant.BRANCH_OFFICE, 150, 550, 0, 0, 16));
        nodes.add(new CustomLabel(Constant.ENTER_PURCHASE, 350, 400, 0, 0, 24));
        nodes.add(new CustomLabel(Constant.TF_ARTICLE_NUMBER, 450, 200, 0, 0, 16));
        nodes.add(new CustomLabel(Constant.TF_SIZE, 450, 350, 0, 0, 16));
        nodes.add(new CustomLabel(Constant.TF_PLAIN_PRICE, 450, 430, 0, 0, 16));
        nodes.add(new CustomLabel(Constant.TF_BUYING_PRICE, 450, 510, 0, 0, 16));
        nodes.add(new CustomLabel(Constant.PERCENTAGE_TEXT, 440, 600, 0, 0, 16));
        nodes.add(new CustomLabel(Constant.TF_FINAL_PRICE, 450, 720, 0, 0, 16));
    }

    void initTextFields() {
        nodes.add(new CustomTextField(Constant.TF_NAME, 180, 20, 0, 140));
        setRestrictedChar(getNode(Constant.TF_NAME));
        nodes.add(new CustomTextField(Constant.TF_VORNAME, 180, 170, 0, 140));
        setRestrictedChar(getNode(Constant.TF_VORNAME));
        nodes.add(new CustomTextField(Constant.TF_EMPLOYEE_NUMBER, 180, 320, 0, 140));
        setRestrictedNumber(getNode(Constant.TF_EMPLOYEE_NUMBER));
        nodes.add(new CustomTextField(Constant.TF_CHOOSE_EMPLOYEE, 480, 20, 160, 160));
        nodes.add(new CustomTextField(Constant.TF_ARTICLE_NUMBER, 480, 200, 140, 140));
        nodes.add(new CustomTextField(Constant.TF_SIZE, 480, 350, 70, 70));
        nodes.add(new CustomTextField(Constant.TF_PLAIN_PRICE, 480, 430, 70, 70));
        getNode(Constant.TF_PLAIN_PRICE).setText("0,00");
        nodes.add(new CustomTextField(Constant.TF_BUYING_PRICE, 480, 510, 70, 70));
        getNode(Constant.TF_BUYING_PRICE).setText("0,00");
        nodes.add(new CustomTextField(Constant.TF_PERCENTAGE, 480, 600, 50, 50));
        getNode(Constant.TF_PERCENTAGE).setText("0");
        nodes.add(new CustomTextField(Constant.TF_FINAL_PRICE, 480, 720, 90, 90));
        nodes.add(new CustomTextField(Constant.TF_LAST_INPUT, 560, 20, 0, 600));
    }

    public TextField getNode(String id) {
        return (TextField) nodes.filtered(node -> id.equals(node.getId())).get(0);
    }

    public void setFilterToTextField(ComboBox<Mitarbeiter> mitarbeiterComboBox) {
        // Textfeld zum filtern der MitarbeiterListe
        getNode(Constant.TF_CHOOSE_EMPLOYEE).textProperty().addListener((observer, alt, neu) ->
        {
            if (getNode(Constant.TF_CHOOSE_EMPLOYEE).getText() != null) {
                ObservableList<Mitarbeiter> mitarbeiterListe = Mitarbeiter.getMitarbeiterListe(getNode(Constant.TF_CHOOSE_EMPLOYEE).getText());
                mitarbeiterComboBox.setItems(mitarbeiterListe);
                mitarbeiterComboBox.setValue(mitarbeiterListe.get(0));
            } else {
                ObservableList<Mitarbeiter> mitarbeiterListe = Mitarbeiter.getMitarbeiterListe();
                mitarbeiterComboBox.setItems(mitarbeiterListe);
                mitarbeiterComboBox.setValue(mitarbeiterListe.get(0));
            }
        });
    }

    void resetEmployee() {
        getNode(Constant.TF_NAME).setText("");
        getNode(Constant.TF_VORNAME).setText("");
        getNode(Constant.TF_EMPLOYEE_NUMBER).setText("");
    }

    void resetPurchase() {
        getNode(Constant.TF_ARTICLE_NUMBER).setText("");
        getNode(Constant.TF_SIZE).setText("");
        getNode(Constant.TF_PLAIN_PRICE).setText("0,00");
        getNode(Constant.TF_BUYING_PRICE).setText("0,00");
        getNode(Constant.TF_PERCENTAGE).setText("0");
        getNode(Constant.TF_FINAL_PRICE).setText("");
    }

    void setComboBox(ComboBox<Mitarbeiter> mitarbeiterComboBox) {
        ObservableList<Mitarbeiter> newMitarbeiterListe = Mitarbeiter.getMitarbeiterListe();
        mitarbeiterComboBox.setItems(newMitarbeiterListe);
        mitarbeiterComboBox.setValue(newMitarbeiterListe.get(0));
    }

    void setNodeStyle(Node node, String style) {
        node.setStyle(style);
    }
}
