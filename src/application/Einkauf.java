package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

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
    private double vk, ek;
    private double rabattwert;
    private double endPreis;
    private Mitarbeiter mitarbeiter;
    private String filiale;

    public Einkauf(Mitarbeiter mitarbeiter, String datum, String filiale, String artNummer, String groesse,
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

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getArtNummer() {
        return artNummer;
    }

    public void setArtNummer(String artNummer) {
        this.artNummer = artNummer;
    }

    public String getGroesse() {
        return groesse;
    }

    public void setGroesse(String groesse) {
        this.groesse = groesse;
    }

    public double getVk() {
        return vk;
    }

    public void setVk(double vk) {
        this.vk = vk;
    }

    public double getEk() {
        return ek;
    }

    public void setEk(double ek) {
        this.ek = ek;
    }

    public double getRabattwert() {
        return rabattwert;
    }

    public void setRabattwert(double rabattwert) {
        this.rabattwert = rabattwert;
    }

    public double getEndPreis() {
        return endPreis;
    }

    public void setEndPreis(double endPreis) {
        this.endPreis = endPreis;
    }

    public Mitarbeiter getMitarbeiter() {
        return mitarbeiter;
    }

    public void setMitarbeiter(Mitarbeiter mitarbeiter) {
        this.mitarbeiter = mitarbeiter;
    }

    public String getFiliale() {
        return filiale;
    }

    public void setFiliale(String filiale) {
        this.filiale = filiale;
    }

    /*
     * Ausgabe in einer Datei
     */
    public static void speichern(ObservableList<Einkauf> eList) {

        LocalDateTime z = LocalDateTime.now();
        String monat = String.format("%tB", z);//enthält den aktuellen Monat
        String NameHomeVerzeichnis = System.getProperty("user.dir");
        Path pfadMitDatei = Paths.get(NameHomeVerzeichnis, "Einkauf" + monat + ".csv");// AusgabeDatei mit Monat

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
            System.out.printf("IO: %s%n", ex.getMessage());
        }
    }

    /*
     * Ausgabe der Einkäufe der Filiale entsprechend.
     */
    public static void speichern(ObservableList<Einkauf> eList, String filiale) {

        LocalDateTime z = LocalDateTime.now();
        String monat = String.format("%tB", z);
        String NameHomeVerzeichnis = System.getProperty("user.dir");
        Path pfadMitDatei = Paths.get(NameHomeVerzeichnis, filiale + monat + ".csv");
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
            System.out.printf("IO: %s%n", ex.getMessage());
        }
    }

    public static String getFormattedString(ObservableList<Einkauf> eList, double summe, int i) {
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
    public static boolean neuenEiSpeichern(Einkauf neuerEi) {

        ObservableList<Einkauf> eList = getEinkaufsListe();
        eList.add(neuerEi);
        Collections.sort(eList);
        speichern(eList);
        return true;

    }

    public static ObservableList<Einkauf> getEinkaufsListe() {
        LocalDateTime z = LocalDateTime.now();
        String monat = String.format("%tB", z);
        String NameHomeVerzeichnis = System.getProperty("user.dir");
        Path pfadMitDatei = Paths.get(NameHomeVerzeichnis, "Einkauf" + monat + ".csv");
        ObservableList<Einkauf> eList = FXCollections.observableArrayList();

        if (Files.exists(pfadMitDatei))//Gibt ObservableList von Einkauf zurück
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
                System.out.printf("IO: %s%n", ex.getMessage());
            }
            return eList;
        } else
            return eList;

    }

    /**
     * Für einzel Liste von Filialen in .cvs
     *
     * @param filiale
     */
    public static ObservableList<Einkauf> listeAusgebenFuerFilX(String filiale) {
        LocalDateTime z = LocalDateTime.now();
        String monat = String.format("%tB", z);
        String NameHomeVerzeichnis = System.getProperty("user.dir");
        Path pfadMitDatei = Paths.get(NameHomeVerzeichnis, "Einkauf" + monat + ".csv");
        ObservableList<Einkauf> eList = FXCollections.observableArrayList();
        //Gibt ObservableList von Einkauf zurück
        if (Files.exists(pfadMitDatei)) {

            String zeile = "";

            try (BufferedReader lesePuffer = Files.newBufferedReader(pfadMitDatei)) {
                zeile = lesePuffer.readLine();
                while (zeile != null) {
                    String[] teile = zeile.split(";");
                    if ("Alle".equals(filiale)) {
                        String[] mitarbeiter = teile[0].split(",");
                        Mitarbeiter m = new Mitarbeiter(mitarbeiter[0], mitarbeiter[1], Integer.parseInt(mitarbeiter[2]));
                        eList.add(new Einkauf(m, teile[1], teile[2], teile[3], teile[4], Double.parseDouble(teile[5].replace(",", ".")),
                                Double.parseDouble(teile[6].replace(",", ".")), Double.parseDouble(teile[7].replace(",", ".")),
                                Double.parseDouble(teile[8].replace(",", "."))));

                    } else if (teile[2].equals(filiale)) {
                        String[] mitarbeiter = teile[0].split(",");
                        Mitarbeiter m = new Mitarbeiter(mitarbeiter[0], mitarbeiter[1], Integer.parseInt(mitarbeiter[2]));
                        eList.add(new Einkauf(m, teile[1], teile[2], teile[3], teile[4], Double.parseDouble(teile[5].replace(",", ".")),
                                Double.parseDouble(teile[6].replace(",", ".")), Double.parseDouble(teile[7].replace(",", ".")),
                                Double.parseDouble(teile[8].replace(",", "."))));
                    }
                    zeile = lesePuffer.readLine();
                }
            } catch (IOException ex) {
                System.out.printf("IO: %s%n", ex.getMessage());
            }
            return eList;
        } else
            return eList;

    }

    public static String getDiscountedListPricePercentage(String listPrice, String percentage) {
        double listPriceValue = Double.parseDouble(listPrice.replace(",", "."));
        double percentageValue = Double.parseDouble(percentage.replace(",", ".")) / 100;
        return String.format("%.02f", listPriceValue - (listPriceValue * percentageValue));
    }

    public static String getDiscountedListPrice(String listPrice, String discount) {
        double listPriceValue = Double.parseDouble(listPrice.replace(",", "."));
        double discountValue = Double.parseDouble(discount.replace(",", "."));
        return String.format("%.02f", listPriceValue - discountValue);
    }

    public static String getPriceWithTax(String purchasingPrice, String tax) {
        double purchasingPriceValue = Double.parseDouble(purchasingPrice.replace(",", "."));
        return String.format("%.02f", purchasingPriceValue + (purchasingPriceValue * 0.19));
    }

    public static Scene einkauf() {
        Pane ap = new Pane();
        return new Scene(ap, 1000, 800);
    }

    @Override
    public String toString() {
        return String
                .format("Mitarbeiter: %s, Filiale: %s, Artikelnummer: %s, VK-Preis: %.2f, EK-Preis: %.2f, Rabattwert: %.2f, End-Preis: %.2f", this.mitarbeiter,
                        this.filiale, this.artNummer,
                        this.vk, this.ek, this.rabattwert, this.endPreis);
    }

}
