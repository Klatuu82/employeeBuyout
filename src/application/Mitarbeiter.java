package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Objects;

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

    Mitarbeiter(String name, String vorname, int personalNummer) {
        this.name = name;
        this.vorname = vorname;
        this.personalNummer = personalNummer;
    }

    String getName() {
        return name;
    }

    private String getVorname() {
        return vorname;
    }

    int getPersonalNummer() {
        return personalNummer;
    }

    // Vergleicht Namen
    @Override
    public int compareTo(Mitarbeiter o) {
        return this.name.compareToIgnoreCase(o.name);
    }

    // Gibt den Namen zur ID wieder
    static String getNameFromID(int personalNummer) {
        ObservableList<Mitarbeiter> mList = getMitarbeiterListe();
        for (Mitarbeiter mitarbeiter : mList) {
            if ((mitarbeiter.getPersonalNummer()) == personalNummer)
                return mitarbeiter.getName();
        }
        return "ID nicht gefunden";
    }

    // Gibt den Namen zur ID wieder
    static String getVornameFromID(int personalNummer) {
        ObservableList<Mitarbeiter> mList = getMitarbeiterListe();
        for (Mitarbeiter mitarbeiter : mList) {
            if ((mitarbeiter.getPersonalNummer()) == personalNummer)
                return mitarbeiter.getVorname();
        }
        return "ID nicht gefunden";
    }

    // Liefert true wenn Mitarbeiter in Liste anhand von ID
    private static boolean mitarbeiterGefunden(int personalNummer) {

        ObservableList<Mitarbeiter> mList = getMitarbeiterListe();
        for (Mitarbeiter mitarbeiter : mList) {
            if ((mitarbeiter.getPersonalNummer()) == personalNummer)
                return true;
        }
        return false;
    }

    static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c))
                return false;
        }
        return true;
    }

    static boolean isChar(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c))
                return false;
        }
        return true;
    }

    // Speichert neuen Mitarbeiter wenn ID noch nicht in Liste
    static boolean saveMitarbeiter(Mitarbeiter neuerMa) {
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

    private static void save(ObservableList<Mitarbeiter> mList) {
        //speichert in Personal Datei
        Path pfadMitDatei = FileHelper.getMitarbeiterCSV(Constant.MITARBEITER_CSV);

        try (BufferedWriter schreibPuffer = Files.newBufferedWriter(pfadMitDatei)) {
            for (Mitarbeiter mitarbeiter : mList) {
                String zeile = String.format("%s;%s;%d%n", mitarbeiter.getName(), mitarbeiter.getVorname(), mitarbeiter.getPersonalNummer());
                schreibPuffer.write(zeile);
            }
        } catch (IOException ex) {
            System.out.printf(Constant.IO_ERROR_FORMAT, ex.getMessage());
        }
    }

    static boolean deleteMitarbeiter(Mitarbeiter zuloeschenderMa) {
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
     * Gibt eine ObservableListe von Mitarbeitern zurück.
     *
     * @return ObservableList<Mitarbeiter>
     */
    static ObservableList<Mitarbeiter> getMitarbeiterListe() {
        Path pfadMitDatei = FileHelper.getMitarbeiterCSV(Constant.MITARBEITER_CSV);
        ObservableList<Mitarbeiter> mList = FXCollections.observableArrayList();

        if (pfadMitDatei.toFile().exists()) {

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

    static ObservableList<Mitarbeiter> getMitarbeiterListe(String buchstabe) {
        Path pfadMitDatei = FileHelper.getMitarbeiterCSV(Constant.MITARBEITER_CSV);
        ObservableList<Mitarbeiter> mList = FXCollections.observableArrayList();
        if (buchstabe != null) {
            if (pfadMitDatei.toFile().exists())//Gibt ObservableList von Mitarbeitern zurück
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

    @Override
    public String toString() {
        return String.format("%s,%s,%d", this.name, this.vorname, this.personalNummer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Mitarbeiter that = (Mitarbeiter) o;
        return personalNummer == that.personalNummer &&
                name.equals(that.name) &&
                vorname.equals(that.vorname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, vorname, personalNummer);
    }
}
