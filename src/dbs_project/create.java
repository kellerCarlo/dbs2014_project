package dbs_project;

import java.sql.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//Datenbank erstellen und füllen
public class create {

	public static void db_create(String db,String user, String password) {

		Connection c = null;
		Statement stmt = null;
		String query = null;
		final String DBNAME = "bundesliga";

		//Mit PostgreSQL verbinden		
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection(db, user, password);
			
		} catch (Exception e) {
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}
		System.out.println("Verbindung erfolgreich hergestellt");
		
		//Datenbank erstellen
		try {
			stmt = c.createStatement();
			query = "CREATE DATABASE " + DBNAME + ";";
			stmt.execute( query );
			c = DriverManager.getConnection(db + DBNAME, user, password);
			System.out.println("Datenbank erfolgreich erstellt");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}
			
		//Tabellen erzeugen
		String file = "create_tables.txt";
		try {
	    	stmt = c.createStatement();
	    	BufferedReader in = new BufferedReader(new FileReader(file));
		    while ((query = in.readLine()) != null) {
		    	stmt.executeUpdate(query);
		    }
		    in.close();
		    System.out.println("Tabellen erfolgreich erstellt");
		} catch (IOException e) {
			System.out.println("Exception: IO");
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
		}
		
		//Tabellen füllen
		file = "insert_values.txt";
		try {
		    BufferedReader in = new BufferedReader(new FileReader(file));
		    while ((query = in.readLine()) != null) {
		    	stmt.executeUpdate(query);
		    }
			System.out.println("Tabellen erfolgreich gefüllt");
		    in.close();
		} catch (IOException e) {
			System.out.println("Exception: IO");
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
		}
		
		//Tabellen ändern
		file = "alter_tables.txt";
		try {
		    BufferedReader in = new BufferedReader(new FileReader(file));
		    while ((query = in.readLine()) != null) {
		    	stmt.executeUpdate(query);
		    }
			System.out.println("Tabellen erfolgreich geändert");
		    in.close();
			stmt.close();
			c.close();
		} catch (IOException e) {
			System.out.println("Exception: IO");
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
		}
	}
}		
