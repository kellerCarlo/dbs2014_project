package dbs_project;

import java.sql.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//Datenbank erstellen und f�llen
/**
 * Klasse erstellt die Datenbank.
 * 
 * @author Gruppenarbeit
 * @version 1.1
 */
public class create {

	/**
	 * Erstellt die Datenbank von Grund auf.<br>
	 * F�hrt zu Fehlern, wenn Datenbank bereits existiert.
	 * 
	 * @param db Hostadresse f�r Postgres
	 * @param dbname Name der zu erzeugenden Datenbank
	 * @param user Benutzername f�r Datenbank
	 * @param password Passwort f�r Datenbank
	 * @throws ClassNotFoundException Exception f�r Postgres-Verbindung ben�tigt
	 * @throws SQLException Exception f�r Datenbankzugriffe ben�tigt
	 * @throws IOException Exception f�r Dateizugriffe ben�tigt
	 */
	public static void db_create(String db, String dbname, String user, String password) throws ClassNotFoundException, SQLException, IOException {

		Connection c = null;
		Statement stmt = null;
		String query = null;

		//Mit PostgreSQL verbinden		
		Class.forName("org.postgresql.Driver");
		c = DriverManager.getConnection(db, user, password);
		System.out.println("Verbindung erfolgreich hergestellt");
		
		//Datenbank erstellen
		stmt = c.createStatement();
		query = "CREATE DATABASE " + dbname + ";";
		stmt.execute( query );
		c = DriverManager.getConnection(db + dbname, user, password);
		System.out.println("Datenbank erfolgreich erstellt");
		
		//Tabellen erzeugen
		String file = "create_tables.txt";
		stmt = c.createStatement();
    	BufferedReader in = new BufferedReader(new FileReader(file));
	    while ((query = in.readLine()) != null) {
	    	stmt.executeUpdate(query);
	    }
	    in.close();
	    System.out.println("Tabellen erfolgreich erstellt");
	
		//Tabellen f�llen
		file = "insert_values.txt";
		in = new BufferedReader(new FileReader(file));
	    while ((query = in.readLine()) != null) {
	    	stmt.executeUpdate(query);
	    }
		System.out.println("Tabellen erfolgreich gef�llt");
	    in.close();	
	    
		//Tabellen �ndern
		file = "alter_tables.txt";
		in = new BufferedReader(new FileReader(file));
	    while ((query = in.readLine()) != null) {
	    	stmt.executeUpdate(query);
	    }
		System.out.println("Tabellen erfolgreich ge�ndert");
		
		//Verbindungen schlie�en
	    in.close();
		stmt.close();
		c.close();
		}
}		
