package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Einkauf
 * <p>
 * Var:	String: Datum
 * String:	Art.Nr.
 * Int:	Groesse
 * Double:	VK
 * Int:	Rabatt %
 * Double:	Preis
 * Double:	Gutschein
 * Double:	Summe
 *
 * @author Marcel Daus
 */
public class Einkauf implements Comparable<Einkauf> {

    private String datum;
    private String artNummer;
    private String groesse;
    private double vk;
    private double ek;
    private double rabattwert;
    private double endPreis;
    private Mitarbeiter mitarbeiter;
    private String filiale;

    Einkauf(Mitarbeiter mitarbeiter, String datum, String filiale, String artNummer, String groesse,
            double vk, double ek, double rabattwert, double endPreis) {
        this.datum = datum;
        this.mitarbeiter = mitarbeiter;
        this.filiale = filiale;
        this.artNummer = artNummer;
        this.groesse = groesse;
        this.vk = vk;
        this.ek = ek;
        this.rabattwert = rabattwert;
        this.endPreis = endPreis;
    }

    private String getDatum() {
        return datum;
    }

    private String getArtNummer() {
        return artNummer;
    }

    private String getGroesse() {
        return groesse;
    }

    private double getVk() {
        return vk;
    }

    private double getEk() {
        return ek;
    }

    private double getRabattwert() {
        return rabattwert;
    }

    private double getEndPreis() {
        return endPreis;
    }

    private Mitarbeiter getMitarbeiter() {
        return mitarbeiter;
    }

    private String getFiliale() {
        return filiale;
    }

    /*
     * Ausgabe in einer Datei
     */
    private static void speichern(ObservableList<Einkauf> eList) {

        Path pfadMitDatei = FileHelper.getMitarbeiterCSV(getFileName());

        try (BufferedWriter schreibPuffer = Files.newBufferedWriter(pfadMitDatei)) {
            for (Einkauf einkauf : eList) {
                String zeile = String.format("%s;%s;%s;%s;%s;%.2f;%.2f;%.2f;%.2f%n",
                        einkauf.getMitarbeiter(),
                        einkauf.getDatum(),
                        einkauf.getFiliale(),
                        einkauf.getArtNummer(),
                        einkauf.getGroesse(),
                        einkauf.getVk(),
                        einkauf.getEk(),
                        einkauf.getRabattwert(),
                        einkauf.getEndPreis());
                schreibPuffer.write(zeile);
            }
        } catch (IOException ex) {
            System.out.printf(Constant.IO_ERROR_FORMAT, ex.getMessage());
        }
    }

    // AusgabeDatei mit Monat
    private static String getFileName() {
        LocalDateTime z = LocalDateTime.now();
        String monat = String.format("%tB", z);//enthält den aktuellen Monat
        return "Einkauf" + monat + ".csv";
    }

    /*
     * Ausgabe der Einkäufe der Filiale entsprechend.
     */
    static void speichern(ObservableList<Einkauf> eList, String filiale) {

        LocalDateTime z = LocalDateTime.now();
        String monat = String.format("%tB", z);
        Path pfadMitDatei = FileHelper.getMitarbeiterCSV(filiale + monat + ".csv");
        double summe = 0.0;

        try (BufferedWriter schreibPuffer = Files.newBufferedWriter(pfadMitDatei)) {
            if (filiale.equals("Alle")) {
                for (int i = 0; i < eList.size(); i++) {
                    if (i + 1 < eList.size()) {
                        if (eList.get(i).getMitarbeiter().getName().equals(eList.get(i + 1).getMitarbeiter().getName())) {
                            summe = eList.get(i).getEndPreis() + summe;
                            String zeile = String.format("%s;%s;%.2f;%.2f;%.2f;0,00%n",
                                    eList.get(i).getMitarbeiter(),
                                    eList.get(i).getArtNummer(),
                                    eList.get(i).getVk(),
                                    eList.get(i).getEk(),
                                    eList.get(i).getEndPreis());
                            schreibPuffer.write(zeile);
                        } else {
                            String zeile = getFormattedString(eList, summe, i);
                            schreibPuffer.write(zeile);
                            summe = 0.00;
                        }
                    } else {
                        String zeile = getFormattedString(eList, summe, i);
                        schreibPuffer.write(zeile);
                        summe = 0.00;
                    }
                }
            } else {
                for (Einkauf einkauf : eList) {
                    String zeile = String.format("%s;%s,%s;%s;%s;%.2f;%.2f;%.2f;%.2f%n",
                            einkauf.getMitarbeiter(),
                            einkauf.getDatum(),
                            einkauf.getFiliale(),
                            einkauf.getArtNummer(),
                            einkauf.getGroesse(),
                            einkauf.getVk(),
                            einkauf.getEk(),
                            einkauf.getRabattwert(),
                            einkauf.getEndPreis());
                    schreibPuffer.write(zeile);
                }
            }
        } catch (IOException ex) {
            System.out.printf(Constant.IO_ERROR_FORMAT, ex.getMessage());
        }
    }

    private static String getFormattedString(ObservableList<Einkauf> eList, double summe, int i) {
        summe = eList.get(i).getEndPreis() + summe;
        return String.format("%s;%s;%.2f;%.2f;%.2f;%.2f%n",
                eList.get(i).getMitarbeiter(),
                eList.get(i).getArtNummer(),
                eList.get(i).getVk(),
                eList.get(i).getEk(),
                eList.get(i).getEndPreis(),
                summe);
    }

    // Vergleicht Namen
    @Override
    public int compareTo(Einkauf andererMa) {
        return this.mitarbeiter.getName().compareToIgnoreCase(andererMa.mitarbeiter.getName());
    }

    // Speichert neuen Mitarbeiter wenn ID noch nicht in Liste
    static boolean neuenEiSpeichern(Einkauf neuerEi) {

        ObservableList<Einkauf> eList = getEinkaufsListe();
        eList.add(neuerEi);
        Collections.sort(eList);
        speichern(eList);
        return true;

    }

    private static ObservableList<Einkauf> getEinkaufsListe() {
        Path pfadMitDatei = FileHelper.getMitarbeiterCSV(getFileName());
        ObservableList<Einkauf> eList = FXCollections.observableArrayList();

        if (pfadMitDatei.toFile().exists())//Gibt ObservableList von Einkauf zurück
        {

            String zeile = "";

            try (BufferedReader lesePuffer = Files.newBufferedReader(pfadMitDatei)) {
                zeile = lesePuffer.readLine();
                while (zeile != null) {
                    String[] teile = zeile.split(";");
                    String[] mitarbeiter = teile[0].split(",");
                    Mitarbeiter m = new Mitarbeiter(mitarbeiter[0], mitarbeiter[1], Integer.parseInt(mitarbeiter[2]));
                    eList.add(new Einkauf(m, teile[1], teile[2], teile[3], teile[4], Double.parseDouble(teile[5].replace(",", ".")),
                            Double.parseDouble(teile[6].replace(",", ".")), Double.parseDouble(teile[7].replace(",", ".")),
                            Double.parseDouble(teile[8].replace(",", "."))));
                    zeile = lesePuffer.readLine();
                }
            } catch (IOException ex) {
                System.out.printf(Constant.IO_ERROR_FORMAT, ex.getMessage());
            }
            return eList;
        } else
            return eList;

    }

    /**
     * Gibt eine Liste aller Einkäufe für die Angegebene Filiale.
     *
     * @param filiale zur Ausgabe.
     */
    static ObservableList<Einkauf> listeAusgebenFuerFilX(String filiale) {
        Path pfadMitDatei = FileHelper.getMitarbeiterCSV(getFileName());
        ObservableList<Einkauf> eList = FXCollections.observableArrayList();
        //Gibt ObservableList von Einkauf zurück
        if (pfadMitDatei.toFile().exists()) {

            String zeile = "";

            try (BufferedReader lesePuffer = Files.newBufferedReader(pfadMitDatei)) {
                zeile = lesePuffer.readLine();
                while (zeile != null) {
                    String[] teile = zeile.split(";");
                    if ("Alle".equals(filiale) || teile[2].equals(filiale)) {
                        String[] mitarbeiter = teile[0].split(",");
                        Mitarbeiter m = new Mitarbeiter(mitarbeiter[0], mitarbeiter[1], Integer.parseInt(mitarbeiter[2]));
                        eList.add(new Einkauf(m, teile[1], teile[2], teile[3], teile[4], Double.parseDouble(teile[5].replace(",", ".")),
                                Double.parseDouble(teile[6].replace(",", ".")), Double.parseDouble(teile[7].replace(",", ".")),
                                Double.parseDouble(teile[8].replace(",", "."))));

                    }
                    zeile = lesePuffer.readLine();
                }
            } catch (IOException ex) {
                System.out.printf(Constant.IO_ERROR_FORMAT, ex.getMessage());
            }
            return eList;
        } else
            return eList;

    }

    static String getDiscountedListPricePercentage(String listPrice, String percentage) {
        double listPriceValue = Double.parseDouble(listPrice.replace(",", "."));
        double percentageValue = Double.parseDouble(percentage.replace(",", ".")) / 100;
        return String.format(Constant.EURO_FORMAT, listPriceValue - (listPriceValue * percentageValue));
    }

    static String getDiscountedListPrice(String listPrice, String discount) {
        double listPriceValue = Double.parseDouble(listPrice.replace(",", "."));
        double discountValue = Double.parseDouble(discount.replace(",", "."));
        return String.format(Constant.EURO_FORMAT, listPriceValue - discountValue);
    }

    static String getPriceWithTax(String purchasingPrice, String tax) {
        double purchasingPriceValue = Double.parseDouble(purchasingPrice.replace(",", "."));
        return String.format(Constant.EURO_FORMAT, purchasingPriceValue + (purchasingPriceValue * 0.19));
    }

    @Override
    public String toString() {
        return String
                .format("Mitarbeiter: %s, Filiale: %s, Artikelnummer: %s, VK-Preis: %.2f, EK-Preis: %.2f, Rabattwert: %.2f, End-Preis: %.2f", this.mitarbeiter,
                        this.filiale, this.artNummer,
                        this.vk, this.ek, this.rabattwert, this.endPreis);
    }

}
