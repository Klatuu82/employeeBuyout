package application;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Programm zum verarbeiten von Mitarbeiter Einkäufen.
 */
public class Main extends Application {

    private AnchorPane anchorPane = new AnchorPane();

    private Map<String, TextField> textFields = new HashMap<>();
    private UIHelper uiHelper = new UIHelper(textFields, anchorPane.getChildren());

    private boolean istGespeichert = false;
    private boolean istGeloescht = false;

    @Override
    public void start(Stage hauptFenster) {
        final Label lbgespeichert = uiHelper.createLabel(null, 223, 190, 500, 0, 16);
        Label lbgeloescht;
        try {
            uiHelper.initLabels();
            ObservableList<Mitarbeiter> mitarbeiterListe = Mitarbeiter.getMitarbeiterListe();

            //Combobox mit MitarbeiterListe für die Auswahl
            ComboBox<Mitarbeiter> mitarbeiterComboBox =
                    uiHelper.getMitarbeiterComboBox(520, 20, 160, 160, mitarbeiterListe);

            uiHelper.initTextFields(mitarbeiterComboBox);

            //Zurücksetzen des Filter, komplette Liste wird geladen
            Button comboBoxReset = uiHelper.getButton(Constant.DELETE_FILTER, 520, 200);

            DatePicker datePicker = uiHelper.getDatePicker();

            /*
             * Speichert neuen Mitarbeiter in Mitarbeiterliste
             * Check ob ID schon vorhanden
             * fügt Mitarbeiter in mitarbeiterListe ein
             */
            Button buSpeichernMa = uiHelper.getButton(Constant.SAVE_EMPLOYEE, 220, 20);
            buSpeichernMa.setOnAction(ev -> {
                Mitarbeiter mitarbeiter = new Mitarbeiter(textFields.get(Constant.NAME).getText(), textFields.get(Constant.VORNAME).getText(),
                        Integer.parseInt(textFields.get(Constant.EMPLOYEE_NUMBER).getText()));
                istGespeichert = Mitarbeiter.saveMitarbeiter(mitarbeiter);
                if (istGespeichert) {
                    uiHelper.resetEmployee();
                    lbgespeichert.setText("Mitarbeiter gespeichert");
                } else {
                    lbgespeichert.setText(
                            "ID schon vorhanden und gehört " + Mitarbeiter.getNameFromID(Integer.parseInt(textFields.get(Constant.EMPLOYEE_NUMBER).getText()))
                                    + " "
                                    + Mitarbeiter
                                    .getVornameFromID(Integer.parseInt(textFields.get(Constant.EMPLOYEE_NUMBER).getText())));
                }
                uiHelper.setComboBox(mitarbeiterComboBox);
                Thread resetLabel = new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        resetSaveLabel(lbgespeichert, "");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                resetLabel.start();
            });

            lbgeloescht = uiHelper.createLabel(null, 263, 190, 500, 0, 16);

            /*
             * Mitarbeiter Löschen mit der Mitarbeiter ID
             */
            Button buLoeschenMa = uiHelper.getButton(Constant.DELETE_EMPLOYEE, 260, 20);
            buLoeschenMa.setOnAction(ev -> {
                Mitarbeiter mitarbeiter = new Mitarbeiter(textFields.get(Constant.NAME).getText(), textFields.get(Constant.VORNAME).getText(),
                        Integer.parseInt(textFields.get(Constant.EMPLOYEE_NUMBER).getText()));
                istGeloescht = Mitarbeiter.deleteMitarbeiter(mitarbeiter);
                if (istGeloescht) {
                    lbgeloescht.setText("Mitarbeiter mit der ID: " + textFields.get(Constant.EMPLOYEE_NUMBER).getText() + " gelöscht");
                } else {
                    lbgeloescht.setText("ID nicht gefunden");
                }
                uiHelper.setComboBox(mitarbeiterComboBox);
            });

            //Liste mit Filialen für die ComboBox
            ObservableList<String> filialenListe = FXCollections.observableArrayList();
            filialenListe.addAll("Alle", "Schuhhaus", "Kaufhaeusl", "Skihuette", "Outlet", "Perpedes");

            // ComboBox für die Ausgabeauswahl
            ComboBox<String> filialen = uiHelper.getMitarbeiterComboBox(180, 550, 140, 140, filialenListe);

            //Speichernmethode aus Einkaufklasse mit übergabe der Filiale und
            Button buDateiAusgabe = uiHelper.getButton(Constant.SAVE, 180, 710);
            buDateiAusgabe.setOnAction(ev ->
                    Einkauf.speichern(Einkauf.listeAusgebenFuerFilX(filialen.getValue()), filialen.getValue()));

            ToggleGroup tg = new ToggleGroup();
            RadioButton rbProzent = uiHelper.getRadioButton(Constant.PERCENTAGE, 480);
            rbProzent.setToggleGroup(tg);
            rbProzent.setOnAction(ev ->
                    textFields.get(Constant.FINAL_PRICE)
                            .setText(Einkauf.getDiscountedListPricePercentage(textFields.get(Constant.PLAIN_PRICE).getText(),
                                    textFields.get(Constant.PERCENTAGE).getText())));
            RadioButton rbWert = uiHelper.getRadioButton(Constant.VALUE, 505);
            rbWert.setToggleGroup(tg);
            rbWert.setOnAction(ev ->
                    textFields.get(Constant.FINAL_PRICE)
                            .setText(Einkauf.getDiscountedListPrice(textFields.get(Constant.PLAIN_PRICE).getText(),
                                    textFields.get(Constant.PERCENTAGE).getText())));
            RadioButton rbMwSt = uiHelper.getRadioButton(Constant.TAX, 530);
            rbMwSt.setToggleGroup(tg);
            rbMwSt.setOnAction(ev ->
                    textFields.get(Constant.FINAL_PRICE)
                            .setText(Einkauf.getPriceWithTax(textFields.get(Constant.BUYING_PRICE).getText(), textFields.get(Constant.PERCENTAGE).getText())));

            textFields.get(Constant.PLAIN_PRICE).textProperty()
                    .addListener((observer, alt, neu) -> textFields.get(Constant.FINAL_PRICE).setText(textFields.get(Constant.PLAIN_PRICE).getText()));
            textFields.get(Constant.BUYING_PRICE).textProperty()
                    .addListener((observer, alt, neu) -> textFields.get(Constant.FINAL_PRICE).setText(textFields.get(Constant.BUYING_PRICE).getText()));

            comboBoxReset.setOnAction(ev -> {
                uiHelper.setComboBox(mitarbeiterComboBox);
                textFields.get("tfMaAuswahl").setText("");
            });

            /*
             * Speichern der Einkäufe in Datei, mit allen angaben.
             * Name, Vorname, ID, Datum, Filiale, Artnr., Größe, VK Preis, EK Preis, Rabatte, Endpreis
             */
            Button buSpeichernEi = uiHelper.getButton(Constant.SAVE_SHOPPING, 480, 830);
            buSpeichernEi.setOnAction(ev -> {
                LocalDate date = datePicker.getValue();
                String datum = date.toString();
                istGespeichert = Einkauf.neuenEiSpeichern(new Einkauf(mitarbeiterComboBox.getValue(),
                        datum,
                        filialen.getValue(), textFields.get(Constant.ARTICLE_NUMBER).getText(), textFields.get(Constant.SIZE).getText(),
                        Double.parseDouble(textFields.get(Constant.PLAIN_PRICE).getText().replace(",", ".")),
                        Double.parseDouble(textFields.get(Constant.BUYING_PRICE).getText().replace(",", ".")),
                        Double.parseDouble(textFields.get(Constant.PERCENTAGE).getText().replace(",", ".")),
                        Double.parseDouble(textFields.get(Constant.FINAL_PRICE).getText().replace(",", "."))));
                uiHelper.resetPurchase();
            });

            Scene hauptScene = new Scene(anchorPane, 1000, 800);

            hauptFenster.setTitle(Constant.TITLE);
            hauptFenster.setScene(hauptScene);
            hauptFenster.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetSaveLabel(Label lbgespeichert, String s) {
        lbgespeichert.setText(s);
    }

    /**
     * Main Method.
     *
     * @param args String args
     */
    public static void main(String[] args) {
        Mitarbeiter erster = new Mitarbeiter(" Name", "Vorname", 0);
        Mitarbeiter.saveMitarbeiter(erster);
        launch(args);
    }
}
