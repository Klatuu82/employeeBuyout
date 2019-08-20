package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class Ausgabe {
	
	public static void ausgabe(Scene scene)
	{
		Pane ap = new Pane();
		
		
		Label lbAusgabe = new Label();
	    lbAusgabe.setLayoutX(650);
	    lbAusgabe.setLayoutY(100);
	    lbAusgabe.setFont(new Font("Tahoma", 20));
	    lbAusgabe.setText("Ausgabe wählen");
	    ap.getChildren().add(lbAusgabe);
	    
	    Label lbFiliale1 = new Label();
	    lbFiliale1.setLayoutX(550);
	    lbFiliale1.setLayoutY(150);	    
	    lbFiliale1.setFont(new Font("Tahoma", 16));
	    lbFiliale1.setText("Filiale auswählen");
	    ap.getChildren().add(lbFiliale1);
	    
	    ObservableList<String> filialenListe = FXCollections.observableArrayList();
	    filialenListe.add("Alle");
	    filialenListe.add("Schuhhaus");
	    filialenListe.add("Kaufhaeusl");
	    filialenListe.add("Skihuette");
	    filialenListe.add("Outlet");
	    filialenListe.add("Perpedes");
	    
	    ComboBox<String> filialen = new ComboBox<String>();
	    filialen.setLayoutX(550);
	    filialen.setLayoutY(180);
	    filialen.setMinWidth(140.0);
	    filialen.setMaxWidth(140.0);
	    filialen.setItems(filialenListe);
	    filialen.setValue(filialenListe.get(1));
	    filialen.setOnAction( (ev) -> System.out.printf("%s%n", filialen.getValue()));
	    ap.getChildren().add(filialen);	 
	    	        
	    Button buDateiAusgabe = new Button();
	    buDateiAusgabe.setText("Datei Speichern");
	    buDateiAusgabe.setLayoutX(710);
	    buDateiAusgabe.setLayoutY(180);
	    buDateiAusgabe.setMinWidth(140.0);
	    buDateiAusgabe.setMaxWidth(140.0); 
	    buDateiAusgabe.setOnAction( (ev) -> {
	    										Einkauf.speichern(Einkauf.listeAusgebenFuerFilX(filialen.getValue()), filialen.getValue());
	    									});
	    ap.getChildren().add(buDateiAusgabe);
	    
	    Button ausgabe = new Button("Auf Ausgabe wechseln");
        ausgabe.setLayoutX(20.0);
        ausgabe.setLayoutY(20.0);
        ausgabe.setOnAction( (ev) -> ap.setVisible(false));
        ap.getChildren().add(ausgabe);
	    
	}
}
