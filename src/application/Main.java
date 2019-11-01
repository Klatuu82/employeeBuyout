package application;

import java.time.LocalDate;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.CustomButton;
import javafx.scene.control.CustomLabel;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Programm zum verarbeiten von Mitarbeiter Einkäufen.
 */
public class Main extends Application {

    //TODO: Artikelnummer im format 000-00-00-00
    //TODO Menge angeben und rechnen
    private AnchorPane anchorPane = new AnchorPane();

    private UIHelper uiHelper = new UIHelper(anchorPane.getChildren());

    private boolean istGespeichert = false;
    private boolean istGeloescht = false;

    @Override
    public void start(Stage hauptFenster) {
        final Label lbgespeichert = new CustomLabel(null, 223, 190, 500, 0, 16);
        Label lbgeloescht;
        try {
            ObservableList<Mitarbeiter> mitarbeiterListe = Mitarbeiter.getMitarbeiterListe();

            //Combobox mit MitarbeiterListe für die Auswahl
            ComboBox<Mitarbeiter> mitarbeiterComboBox =
                    uiHelper.getMitarbeiterComboBox(520, 20, 160, 160, mitarbeiterListe);

            uiHelper.initLabels();
            uiHelper.initTextFields();
            uiHelper.setFilterToTextField(mitarbeiterComboBox);
            //Zurücksetzen des Filter, komplette Liste wird geladen
            Button comboBoxReset = new CustomButton(Constant.DELETE_FILTER, 520, 200);
            anchorPane.getChildren().add(comboBoxReset);
            DatePicker datePicker = uiHelper.getDatePicker();

            /*
             * Speichert neuen Mitarbeiter in Mitarbeiterliste
             * Check ob ID schon vorhanden
             * fügt Mitarbeiter in mitarbeiterListe ein
             */

            Button buSpeichernMa = new CustomButton(Constant.SAVE_EMPLOYEE, 220, 20);
            anchorPane.getChildren().add(buSpeichernMa);
            buSpeichernMa.setOnAction(ev -> {
                Mitarbeiter mitarbeiter = new Mitarbeiter(uiHelper.getNode(Constant.TF_NAME).getText(), uiHelper.getNode(Constant.TF_VORNAME).getText(),
                        Integer.parseInt(uiHelper.getNode(Constant.TF_EMPLOYEE_NUMBER).getText()));
                istGespeichert = Mitarbeiter.saveMitarbeiter(mitarbeiter);
                if (istGespeichert) {
                    uiHelper.resetEmployee();
                    lbgespeichert.setText("Mitarbeiter gespeichert");
                } else {
                    lbgespeichert.setText(
                            "ID schon vorhanden und gehört " + Mitarbeiter
                                    .getNameFromID(Integer.parseInt(uiHelper.getNode(Constant.TF_EMPLOYEE_NUMBER).getText()))
                                    + " "
                                    + Mitarbeiter
                                    .getVornameFromID(Integer.parseInt(uiHelper.getNode(Constant.TF_EMPLOYEE_NUMBER).getText())));
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

            lbgeloescht = new CustomLabel(null, 263, 190, 500, 0, 16);

            /*
             * Mitarbeiter Löschen mit der Mitarbeiter ID
             */
            Button buLoeschenMa = new CustomButton(Constant.DELETE_EMPLOYEE, 260, 20);
            anchorPane.getChildren().add(buLoeschenMa);
            buLoeschenMa.setOnAction(ev -> {
                Mitarbeiter mitarbeiter = new Mitarbeiter(uiHelper.getNode(Constant.TF_NAME).getText(), uiHelper.getNode(Constant.TF_VORNAME).getText(),
                        Integer.parseInt(uiHelper.getNode(Constant.TF_EMPLOYEE_NUMBER).getText()));
                istGeloescht = Mitarbeiter.deleteMitarbeiter(mitarbeiter);
                if (istGeloescht) {
                    lbgeloescht.setText("Mitarbeiter mit der ID: " + uiHelper.getNode(Constant.TF_EMPLOYEE_NUMBER).getText() + " gelöscht");
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
            Button buDateiAusgabe = new CustomButton(Constant.SAVE, 180, 710);
            anchorPane.getChildren().add(buDateiAusgabe);
            buDateiAusgabe.setOnAction(ev ->
                    Einkauf.speichern(Einkauf.listeAusgebenFuerFilX(filialen.getValue()), filialen.getValue()));

            ToggleGroup tg = new ToggleGroup();
            RadioButton rbProzent = uiHelper.getRadioButton(Constant.TF_PERCENTAGE, 480);
            rbProzent.setToggleGroup(tg);
            rbProzent.setOnAction(ev ->
                    uiHelper.getNode(Constant.TF_FINAL_PRICE)
                            .setText(Einkauf.getDiscountedListPricePercentage(uiHelper.getNode(Constant.TF_PLAIN_PRICE).getText(),
                                    uiHelper.getNode(Constant.TF_PERCENTAGE).getText())));
            RadioButton rbWert = uiHelper.getRadioButton(Constant.VALUE, 505);
            rbWert.setToggleGroup(tg);
            rbWert.setOnAction(ev ->
                    uiHelper.getNode(Constant.TF_FINAL_PRICE)
                            .setText(Einkauf.getDiscountedListPrice(uiHelper.getNode(Constant.TF_PLAIN_PRICE).getText(),
                                    uiHelper.getNode(Constant.TF_PERCENTAGE).getText())));
            RadioButton rbMwSt = uiHelper.getRadioButton(Constant.TAX, 530);
            rbMwSt.setToggleGroup(tg);
            rbMwSt.setOnAction(ev ->
                    uiHelper.getNode(Constant.TF_FINAL_PRICE)
                            .setText(Einkauf.getPriceWithTax(uiHelper.getNode(Constant.TF_BUYING_PRICE).getText(),
                                    uiHelper.getNode(Constant.TF_PERCENTAGE).getText())));

            uiHelper.getNode(Constant.TF_PLAIN_PRICE).textProperty()
                    .addListener(
                            (observer, alt, neu) -> uiHelper.getNode(Constant.TF_FINAL_PRICE).setText(uiHelper.getNode(Constant.TF_PLAIN_PRICE).getText()));
            uiHelper.getNode(Constant.TF_BUYING_PRICE).textProperty()
                    .addListener(
                            (observer, alt, neu) -> uiHelper.getNode(Constant.TF_FINAL_PRICE).setText(uiHelper.getNode(Constant.TF_BUYING_PRICE).getText()));

            comboBoxReset.setOnAction(ev -> {
                uiHelper.setComboBox(mitarbeiterComboBox);
                uiHelper.getNode("tfMaAuswahl").setText("");
            });

            /*
             * Speichern der Einkäufe in Datei, mit allen angaben.
             * Name, Vorname, ID, Datum, Filiale, Artnr., Größe, VK Preis, EK Preis, Rabatte, Endpreis
             */
            Button buSpeichernEi = new CustomButton(Constant.SAVE_SHOPPING, 480, 830);
            anchorPane.getChildren().add(buSpeichernEi);
            buSpeichernEi.setOnAction(ev -> {
                System.out.println("Der Style" + datePicker.getStyle());
                if (datePicker.getValue() == null) {
                    uiHelper.setNodeStyle(datePicker, Constant.FX_BORDER_COLOR_RED + Constant.FX_BORDER_WIDTH_2_PX);
                    return;
                } else {
                    uiHelper.setNodeStyle(datePicker, "");
                }
                if (uiHelper.getNode(Constant.TF_ARTICLE_NUMBER).getText().isEmpty()) {
                    uiHelper.setNodeStyle(uiHelper.getNode(Constant.TF_ARTICLE_NUMBER), Constant.FX_BORDER_COLOR_RED + Constant.FX_BORDER_WIDTH_2_PX);
                    return;
                } else {
                    uiHelper.setNodeStyle(uiHelper.getNode(Constant.TF_ARTICLE_NUMBER), "");
                }
                LocalDate date = datePicker.getValue();
                String datum = date.toString();
                Einkauf neuerEinkauf = new Einkauf(mitarbeiterComboBox.getValue(),
                        datum,
                        filialen.getValue(),
                        uiHelper.getNode(Constant.TF_ARTICLE_NUMBER).getText(),
                        uiHelper.getNode(Constant.TF_SIZE).getText(),
                        Double.parseDouble(uiHelper.getNode(Constant.TF_PLAIN_PRICE).getText().replace(",", ".")),
                        Double.parseDouble(uiHelper.getNode(Constant.TF_BUYING_PRICE).getText().replace(",", ".")),
                        Double.parseDouble(uiHelper.getNode(Constant.TF_PERCENTAGE).getText().replace(",", ".")),
                        Double.parseDouble(uiHelper.getNode(Constant.TF_FINAL_PRICE).getText().replace(",", ".")));
                istGespeichert = Einkauf.neuenEiSpeichern(neuerEinkauf);
                if (istGespeichert) {
                    uiHelper.getNode(Constant.TF_LAST_INPUT).setText(neuerEinkauf.toString());
                }
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
