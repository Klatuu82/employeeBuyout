package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Mitarbeiter
 * <p>
 * Var: String: Name
 * String: Vorname
 * Int:	PersonalNummer
 *
 * @author Marcel Daus
 */

public class Mitarbeiter implements Comparable<Mitarbeiter> {

    private String name;
    private String vorname;
    private int personalNummer;

    public Mitarbeiter(String name, int personalNummer) {
        this.name = name;
        this.personalNummer = personalNummer;
    }

    public Mitarbeiter(String name, String vorname, int personalNummer) {
        this.name = name;
        this.vorname = vorname;
        this.personalNummer = personalNummer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public int getPersonalNummer() {
        return personalNummer;
    }

    public void setPersonalNummer(int personalNummer) {
        // ID noch vergleichen mit schon vorhanden IDs
        this.personalNummer = personalNummer;
    }

    // Vergleicht Namen
    @Override
    public int compareTo(Mitarbeiter andererMa) {
        return this.name.compareToIgnoreCase(andererMa.name);
    }

    // Gibt den Namen zur ID wieder
    public static String getNameFromID(int personalNummer) {
        ObservableList<Mitarbeiter> mList = getMitarbeiterListe();
        for (Mitarbeiter mitarbeiter : mList) {
            if ((mitarbeiter.getPersonalNummer()) == personalNummer)
                return mitarbeiter.getName();
        }
        return "ID nicht gefunden";
    }

    // Gibt den Namen zur ID wieder
    public static String getVornameFromID(int personalNummer) {
        ObservableList<Mitarbeiter> mList = getMitarbeiterListe();
        for (Mitarbeiter mitarbeiter : mList) {
            if ((mitarbeiter.getPersonalNummer()) == personalNummer)
                return mitarbeiter.getVorname();
        }
        return "ID nicht gefunden";
    }

    // Liefert true wenn Mitarbeiter in Liste anhand von ID
    public static boolean mitarbeiterGefunden(int personalNummer) {

        ObservableList<Mitarbeiter> mList = getMitarbeiterListe();
        for (Mitarbeiter mitarbeiter : mList) {
            if ((mitarbeiter.getPersonalNummer()) == personalNummer)
                return true;
        }
        return false;
    }

    public static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c))
                return false;
        }
        return true;
    }

    public static boolean isChar(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c))
                return false;
        }
        return true;
    }

    // Speichert neuen Mitarbeiter wenn ID noch nicht in Liste
    public static boolean saveMitarbeiter(Mitarbeiter neuerMa) {
        if (mitarbeiterGefunden(neuerMa.getPersonalNummer())) {
            return false;
        } else {
            ObservableList<Mitarbeiter> mList = getMitarbeiterListe();
            mList.add(neuerMa);
            Collections.sort(mList);
            save(mList);
            return true;
        }

    }

    public static void save(ObservableList<Mitarbeiter> mList) {
        //speichert in Personal Datei
        String NameHomeVerzeichnis = System.getProperty("user.dir");
        Path pfadMitDatei = Paths.get(NameHomeVerzeichnis, "Mitarbeiter.csv");

        try (BufferedWriter schreibPuffer = Files.newBufferedWriter(pfadMitDatei)) {
            for (Mitarbeiter mitarbeiter : mList) {
                String zeile = String.format("%s;%s;%d%n", mitarbeiter.getName(), mitarbeiter.getVorname(), mitarbeiter.getPersonalNummer());
                schreibPuffer.write(zeile);
            }
        } catch (IOException ex) {
            System.out.printf("IO: %s%n", ex.getMessage());
        }
    }

    public static boolean deleteMitarbeiter(Mitarbeiter zuloeschenderMa) {
        int position = -1;
        int zuL = zuloeschenderMa.getPersonalNummer();
        int zuS;
        //löscht aus Personal Datei
        if (!mitarbeiterGefunden(zuloeschenderMa.getPersonalNummer())) {
            System.out.printf("Id nicht Vorhanden%n");
            return false;
        } else {
            ObservableList<Mitarbeiter> mList = getMitarbeiterListe();

            for (int i = 0; i < mList.size(); i++) {
                zuS = mList.get(i).getPersonalNummer();
                if (zuS == zuL) {
                    position = i;
                }
            }
            mList.remove(position);
            save(mList);
            return true;
        }
    }

    /**
     * Gibt eine ObservableListe von Mitarbeitern zurück
     *
     * @return ObservableList<Mitarbeiter>
     */
    public static ObservableList<Mitarbeiter> getMitarbeiterListe() {
        String NameHomeVerzeichnis = System.getProperty("user.dir");
        Path pfadMitDatei = Paths.get(NameHomeVerzeichnis, "Mitarbeiter.csv");
        ObservableList<Mitarbeiter> mList = FXCollections.observableArrayList();

        if (Files.exists(pfadMitDatei)) {

            String zeile;

            try (BufferedReader lesePuffer = Files.newBufferedReader(pfadMitDatei)) {
                zeile = lesePuffer.readLine();
                while (zeile != null) {
                    String[] teile = zeile.split(";");
                    mList.add(new Mitarbeiter(teile[0], teile[1], Integer.parseInt(teile[2])));
                    zeile = lesePuffer.readLine();
                }
            } catch (IOException ex) {
                System.out.printf("IO: %s%n", ex.getMessage());
            }
            return mList;
        } else
            return mList;

    }

    public static ObservableList<Mitarbeiter> getMitarbeiterListe(String buchstabe) {
        String NameHomeVerzeichnis = System.getProperty("user.dir");
        Path pfadMitDatei = Paths.get(NameHomeVerzeichnis, "Mitarbeiter.csv");
        ObservableList<Mitarbeiter> mList = FXCollections.observableArrayList();
        if (buchstabe != null) {
            if (Files.exists(pfadMitDatei))//Gibt ObservableList von Mitarbeitern zurück
            {

                String zeile;

                try (BufferedReader lesePuffer = Files.newBufferedReader(pfadMitDatei)) {
                    zeile = lesePuffer.readLine();
                    while (zeile != null) {
                        String[] teile = zeile.split(";");
                        char[] charName = teile[0].toLowerCase().toCharArray();
                        char[] charbuchstabe = buchstabe.toLowerCase().toCharArray();
                        if (charbuchstabe[0] == charName[0]) {
                            mList.add(new Mitarbeiter(teile[0], teile[1], Integer.parseInt(teile[2])));
                        }
                        zeile = lesePuffer.readLine();
                    }
                } catch (IOException ex) {
                    System.out.printf("IO: %s%n", ex.getMessage());
                }
                return mList;
            } else
                return mList;
        } else
            return mList;
    }
    /*
    public static Scene mitarbeiter() {
        ObservableList<Mitarbeiter> mitarbeiterListe = getMitarbeiterListe();

        Pane anchorPane = new Pane();
        Scene scene = new Scene(anchorPane, 1000, 800);

        ComboBox<Mitarbeiter> mitarbeiter = new ComboBox<>();
        AnchorPane.setTopAnchor(mitarbeiter, 520.0);
        AnchorPane.setLeftAnchor(mitarbeiter, 20.0);
        mitarbeiter.setMinWidth(160.0);
        mitarbeiter.setMaxWidth(160.0);
        mitarbeiter.setItems(mitarbeiterListe);
        mitarbeiter.setValue(mitarbeiterListe.get(0));
        mitarbeiter.setOnAction((ev) -> System.out.printf("%s%n", mitarbeiter.getValue()));
        anchorPane.getChildren().add(mitarbeiter);

        Label lbNeuerMaErstellen = new Label();
        lbNeuerMaErstellen.setLayoutX(120);
        lbNeuerMaErstellen.setLayoutY(100);
        lbNeuerMaErstellen.setFont(new Font("Tahoma", 20));
        lbNeuerMaErstellen.setText("Neuen Mitarbeiter erstellen");
        anchorPane.getChildren().add(lbNeuerMaErstellen);

        Label lbName1 = new Label();
        lbName1.setLayoutX(20);
        lbName1.setLayoutY(150);
        lbName1.setFont(new Font("Tahoma", 16));
        lbName1.setText("Name");
        anchorPane.getChildren().add(lbName1);

        TextField tfName1 = new TextField();
        tfName1.setLayoutX(20);
        tfName1.setLayoutY(180);
        tfName1.setMaxWidth(140.0);
        tfName1.textProperty().addListener((observer, alt, neu) -> {
            if (!Mitarbeiter.isChar(tfName1.getText()))
                tfName1.setText(alt);
        });
        anchorPane.getChildren().add(tfName1);

        Label lbVorname1 = new Label();
        lbVorname1.setLayoutX(170);
        lbVorname1.setLayoutY(150);
        lbVorname1.setFont(new Font("Tahoma", 16));
        lbVorname1.setText("Vorname");
        anchorPane.getChildren().add(lbVorname1);

        TextField tfVorname1 = new TextField();
        tfVorname1.setLayoutX(170);
        tfVorname1.setLayoutY(180);
        tfVorname1.setMaxWidth(140.0);
        tfVorname1.textProperty().addListener((observer, alt, neu) -> {
            if (!Mitarbeiter.isChar(tfVorname1.getText()))
                tfVorname1.setText(alt);
        });
        anchorPane.getChildren().add(tfVorname1);

        Label lbPersoNr1 = new Label();
        lbPersoNr1.setLayoutX(320);
        lbPersoNr1.setLayoutY(150);
        lbPersoNr1.setFont(new Font("Tahoma", 16));
        lbPersoNr1.setText("Personal Nummer");
        anchorPane.getChildren().add(lbPersoNr1);

        TextField tfPersoNr1 = new TextField();
        tfPersoNr1.setLayoutX(320);
        tfPersoNr1.setLayoutY(180);
        tfPersoNr1.setMaxWidth(140.0);
        tfPersoNr1.textProperty().addListener((observer, alt, neu) ->
        {
            if (!Mitarbeiter.isNumeric(tfPersoNr1.getText()))
                tfPersoNr1.setText(alt);
        });
        anchorPane.getChildren().add(tfPersoNr1);

        Label lbgespeichert = UIHelper.createLabel(223.0, 190.0, 500);
        anchorPane.getChildren().add(lbgespeichert);

        Button buSpeichernMa = new Button();
        buSpeichernMa.setText("Mitarbeiter Speichern");
        AnchorPane.setTopAnchor(buSpeichernMa, 220.0);
        AnchorPane.setLeftAnchor(buSpeichernMa, 20.0);
        buSpeichernMa.setMinWidth(140.0);
        buSpeichernMa.setMaxWidth(140.0);
        buSpeichernMa.setOnAction((ev) -> {
            boolean istGespeichert = saveMitarbeiter(new Mitarbeiter(tfName1.getText(), tfVorname1.getText(), Integer.parseInt(tfPersoNr1.getText())));
            if (istGespeichert) {
                ObservableList<Mitarbeiter> mitarbeiterListe1 = Mitarbeiter.getMitarbeiterListe();
                mitarbeiter.setItems(mitarbeiterListe1);
                mitarbeiter.setValue(mitarbeiterListe1.get(0));
                tfName1.setText("");
                tfVorname1.setText("");
                tfPersoNr1.setText("");
                lbgespeichert.setText("Mitarbeiter gespeichert");
            } else {
                lbgespeichert.setText("ID schon vorhanden und gehört " + Mitarbeiter.getNameFromID(Integer.parseInt(tfPersoNr1.getText())) + " " + Mitarbeiter
                        .getVornameFromID(Integer.parseInt(tfPersoNr1.getText())));
                ObservableList<Mitarbeiter> mitarbeiterListe2 = Mitarbeiter.getMitarbeiterListe();
                mitarbeiter.setItems(mitarbeiterListe2);
                mitarbeiter.setValue(mitarbeiterListe2.get(0));
            }
        });
        anchorPane.getChildren().add(buSpeichernMa);

        Label lbgeloescht = new Label();
        lbgeloescht.setFont(new Font("Tahoma", 16));
        //lbgeloescht.setText("Mitarbeiter gelöscht");
        AnchorPane.setTopAnchor(lbgeloescht, 263.0);
        AnchorPane.setLeftAnchor(lbgeloescht, 190.0);
        lbgeloescht.setMinWidth(500.0);
        anchorPane.getChildren().add(lbgeloescht);

        Button buLoeschenMa = new Button();
        buLoeschenMa.setText("Mitarbeiter Löschen");
        AnchorPane.setTopAnchor(buLoeschenMa, 260.0);
        AnchorPane.setLeftAnchor(buLoeschenMa, 20.0);
        buLoeschenMa.setMinWidth(140.0);
        buLoeschenMa.setMaxWidth(140.0);
        buLoeschenMa.setOnAction((ev) -> {
            boolean istGeloescht = deleteMitarbeiter(new Mitarbeiter(tfName1.getText(), tfVorname1.getText(), Integer.parseInt(tfPersoNr1.getText())));
            if (istGeloescht) {
                lbgeloescht.setText("Mitarbeiter mit der ID: " + tfPersoNr1.getText() + " gelöscht");
            } else
                lbgeloescht.setText("ID nicht gefunden");
        });
        anchorPane.getChildren().add(buLoeschenMa);

        return scene;
    }*/

    @Override
    public String toString() {
        return String.format("%s,%s,%d", this.name, this.vorname, this.personalNummer);
    }
}
