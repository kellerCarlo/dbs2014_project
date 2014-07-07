package dbs_project;

import java.io.IOException;
import java.sql.SQLException;

/**
 * <p>Java-Programm zur Projektaufgabe im Kurs<br>
 * <b>Datenbanksysteme 2014.</b></p>
 * <p>Von Carsten Keller, Henrique Hepp und Jonas Grunert.</p>
 * 
 * @author Gruppenarbeit
 * @version 1.0
 */
public class bundesliga {
	
	/**
	 * Hauptfunktion, die die einzelnen Funktionalit�ten des Programms nacheinander aufruft.
	 * 
	 * @param args Komandozeilenparameter; ungenutzt
	 * @throws IOException Exception f�r Klassen create und features ben�tigt
	 * @throws SQLException Exception f�r alle weiteren genutzten Klassen ben�tigt
	 * @throws ClassNotFoundException Exception f�r alle weiteren genutzten Klassen ben�tigt
	 */
	public static void main(String args[]) throws IOException, SQLException, ClassNotFoundException {
	
		final String SRC = "jdbc:postgresql://localhost:5432/";
		final String USER = "postgres";
		final String PASSWORD = "dbs2014";
		final String DBNAME = "bundesliga";
		
		/*
		 * Zum erstellen der Datenbank "bundesliga"
		 * Fehlerhaft, wenn DB bereits existiert
		 */
		
			create.db_create(SRC, DBNAME, USER, PASSWORD);
		
		/*
		 * In Projektbeschreibung gegebene SELECT-Anfragen
		 * DB muss existieren
		 * Nr 6 fehlt noch
		 * 'select_e' ist zur eingabe eigener Select-Queries
		 */
		
			select.select1 (SRC + DBNAME, USER, PASSWORD);  // erstes Saisonspiel
		//	select.select2( SRC + DBNAME, USER, PASSWORD);  // Torsch�tzen
		//	select.select3( SRC + DBNAME, USER, PASSWORD);  // erster Spieltag -> Abend
		//	select.select4( SRC + DBNAME, USER, PASSWORD);  // Spieler bei 
		//	select.select5( SRC + DBNAME, USER, PASSWORD);  // Siege Hannover 96
		//	select.select6( SRC + DBNAME, USER, PASSWORD);  // schlechteste Mannschaft(en)
			select.select_e( SRC + DBNAME, USER, PASSWORD); // f�r die Eingabe eigener Queries
		
		/*
		 * Erstellt .arff-Dateien f�r geforderte und eigene Features
		 * DB muss existieren
		 */
			
			features.open_up(SRC + DBNAME, USER, PASSWORD);
	}
}
