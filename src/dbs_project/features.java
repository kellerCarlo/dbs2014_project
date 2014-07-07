package dbs_project;

import java.io.*;
import java.sql.*;

/**
 * Generiert das Feature für die Data-Mining-Aufgabe.
 * 
 * @author Gruppenarbeit
 * @version 1.2.2
 */
public class features {

	static Connection c = null;
	static Statement stmt = null;
	static String output = "";
	static String SRC = "";
	static String USER = "";
	static String PASSWORD = "";

	/**
	 * Initialisiert lokale Variablen und ruft die einzelnen Methoden auf.
	 * 
	 * @param db Hostadresse der Datenbank
	 * @param user Benutzername für Datenbank
	 * @param password Passwort für Datenbank
	 * @throws SQLException Exception für Datenbankzugriffe benötigt
	 * @throws IOException Exception für Dateizugriffe benötigt
	 * @throws ClassNotFoundException Exception für Postgres-Verbindung benötigt
	 * @see #connect()
	 * @see #disconnect()
	 */
	public static void open_up(String db, String user, String password) throws SQLException, IOException, ClassNotFoundException {
		SRC = db;
		USER = user;
		PASSWORD = password;
		System.out.println("Erstelle \".arff\"-Dateien");
		connect();
		all_features();
		
		/* 
		 * Ausgabe einzelner Informationen.
		 * Auskommentiert, da in all_features() zusamengefasst.
		 * 
		 * //letzte3e();
		 * //letzte3g();
		 * //niederlagen5();
		 * //siege5();
		 * //draw5();
		 * 
		 */
		
		disconnect();
	}
	
	/**
	 * Stellt die Verbindung zur Datenbank her.<br>
	 * Führt zu Fehlern, wenn Datenbank nicht existiert.
	 * @throws ClassNotFoundException Exception für Postgres-Verbindung benötigt
	 * @throws SQLException Exception für Datenbankzugriffe benötigt
	 */
	private static void connect() throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		c = DriverManager.getConnection(SRC, USER, PASSWORD);
		c.setAutoCommit(false);
	}
	
	/**
	 * Schließt die Verbindung zur Datenbank.
	 * 
	 * @throws SQLException Exception für Datenbankzugriffe benötigt
	 */
	private static void disconnect() throws SQLException {
		stmt.close();
		c.close();
	}
	
	/**
	 * Fügt die für alle Spieltage und Vereine die Informationen über
	 * Tore-Zahl(3 Spiele) und Ausgang(5 Spiele) zusammen.<br>
	 * 
	 * @throws IOException Exception für Dateizugriffe benötigt
	 * @throws SQLException Exception für Datenbankzugriffe benötigt
	 */
	private static void all_features() throws IOException, SQLException {
		File file = new File ("features.arff");
		file.createNewFile();
		FileWriter writer = new FileWriter(file);
		writer.write("@relation spiel\n\n");
		writer.write("@attribute Verein numeric\n");
		writer.write("@attribute Name string\n");
		writer.write("@attribute Spieltag numeric\n");
		writer.write("@attribute letzte3Tore numeric\n");
		writer.write("@attribute letzte3GTore numeric\n");
		writer.write("@attribute niederlagen5 numeric\n");
		writer.write("@attribute siege5 numeric\n");
		writer.write("@attribute draws5 numeric\n");
		writer.write("@attribute real_outcome {Sieg, Unentschieden, Niederlage}\n\n");
		writer.write("@data\n");
		stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery( "SELECT COUNT (v_id) FROM bl.verein;");
		rs.next();
		int v_count = rs.getInt(1);
		for (int i = 1; i <= v_count; i++) {
			rs = stmt.executeQuery(" SELECT name FROM bl.verein WHERE v_id = " + i + " ;");
			String vName = "";
			rs.next();
			vName = rs.getString(1);
			rs = stmt.executeQuery( "SELECT COUNT (*) FROM bl.spiel WHERE ( heim = " + i + " OR gast = " + i + " );" );
			rs.next();
			int s_count = rs.getInt(1);
			for (int j = s_count; j > 3 ; j--) {
				output = "";
				//Verein und Spieltag schreiben
				output = output + i + ",'" + vName + "'," + j;
				//Tore und Gegentore der letzten 3 Spiele
				rs = stmt.executeQuery( "SELECT * FROM bl.spiel WHERE (spieltag < " + j + ") AND (heim = " + i + "OR gast = " + i + ") ORDER BY spieltag DESC LIMIT 3;" );
				int temp_e = 0;
				int temp_g = 0;
				while (rs.next()) {
					if (rs.getInt("heim") == i) {
						temp_e += rs.getInt("tore_heim");
						temp_g += rs.getInt("tore_gast");
					} else {
						temp_g += rs.getInt("tore_heim");
						temp_e += rs.getInt("tore_gast");
					}
				}
				output = output + "," + temp_e + "," + temp_g;
				//Niederlagen, Siege, Unentschieden der letzten 5 Spiele
				rs = stmt.executeQuery( "SELECT * FROM bl.spiel WHERE (spieltag < " + j + ") AND (heim = " + i + "OR gast = " + i + ") ORDER BY spieltag DESC LIMIT 5;" );
				int temp_n = 0;
				int temp_s = 0;
				int temp_u = 0;
				while (rs.next()) {
					if (rs.getInt("heim") == i) {
						if ( rs.getInt("tore_heim") < rs.getInt("tore_gast") ) {
							temp_n++;
						} else if (rs.getInt("tore_heim") > rs.getInt("tore_gast")) {
							temp_s++;
						} else {
							temp_u++;
						}
					} else {
						if ( rs.getInt("tore_heim") > rs.getInt("tore_gast") ) {
							temp_n++;
						} else if (rs.getInt("tore_heim") < rs.getInt("tore_gast")) {
							temp_s++;
						} else {
							temp_u++;
						}
					}
				}
				output = output + "," + temp_n + "," + temp_s + "," + temp_u;
				//Tatsächliches Ergebnis
				rs = stmt.executeQuery( "SELECT * FROM bl.spiel WHERE (spieltag = " + j + ") AND (heim = " + i + "OR gast = " + i + ") ;" );
				rs.next();
				if (rs.getInt("heim") == i) {
					if ( rs.getInt("tore_heim") > rs.getInt("tore_gast") ) {
						output = output +  ",Sieg";
					} else if (rs.getInt("tore_heim") > rs.getInt("tore_gast")) {
						output = output + ",Niederlage";
					} else {
						output = output + ",Unentschieden";
					}
				} else {
					if ( rs.getInt("tore_heim") > rs.getInt("tore_gast") ) {
						output = output + ",Niederlage";
					} else if (rs.getInt("tore_heim") < rs.getInt("tore_gast")) {
						output = output + ",Sieg";
					} else {
						output = output + ",Unentschieden";
					}
				}
				output = output + "\n";
				writer.write(output);
			}
		}
		writer.flush();
		writer.close();
		System.out.println("Kombinierte Features");		
	}
	
	/**
	 * 
	 * Letzte 3 Tore für alle Vereine und Spieltage.
	 * 
	 * @throws IOException Exception für Dateizugriffe benötigt
	 * @throws SQLException Exception für Datenbankzugriffe benötigt
	 */
	private static void letzte3e() throws IOException, SQLException {	
		File file = new File ("letzte_3e.arff");
		file.createNewFile();
		FileWriter writer = new FileWriter(file);
		writer.write("@relation spiel");
		writer.write("\n\n");
		writer.write("@attribute Verein numeric\n");
		writer.write("@attribute Spieltag numeric\n");
		writer.write("@attribute Tore1 numeric\n");
		writer.write("@attribute Tore2 numeric\n");
		writer.write("@attribute Tore3 numeric\n\n");
		writer.write("@data\n");		
		stmt = c.createStatement();
		for (int i=1; i <= 56; i++) {
			for (int j=38;j > 3;j--) {
				output = "";
				output = output + i + "," + j;
				ResultSet rs = stmt.executeQuery( "SELECT * FROM bl.spiel WHERE (spieltag < " + j + ") AND (heim = " + i + "OR gast = " + i + ") ORDER BY spieltag DESC LIMIT 3;" );
				while (rs.next()) {
					if (rs.getInt("heim") == i) {
						output = output + "," + rs.getInt("tore_heim");
					} else {
						output = output + "," + rs.getInt("tore_gast");
					}		
				}
				output = output + "\n";
				writer.write(output);
			}
		}		
		writer.flush();
		writer.close();
		System.out.println("letzte 3 Tore");
	}
	
	/**
	 * Letzte 3 Gegentore für alle Vereine und Spieltage.
	 * 
	 * @throws IOException Exception für Dateizugriffe benötigt
	 * @throws SQLException Exception für Datenbankzugriffe benötigt
	 */
	private static void letzte3g() throws IOException, SQLException {
		File file = new File ("letzte_3g.arff");
		file.createNewFile();
		FileWriter writer = new FileWriter(file);
		writer.write("@relation spiel");
		writer.write("\n\n");
		writer.write("@attribute Verein numeric\n");
		writer.write("@attribute Spieltag numeric\n");
		writer.write("@attribute Tore1 numeric\n");
		writer.write("@attribute Tore2 numeric\n");
		writer.write("@attribute Tore3 numeric\n\n");
		writer.write("@data\n");		
		stmt = c.createStatement();
		for (int i=1; i <= 56; i++) {
			for (int j=38;j > 3;j--) {
				output = "";
				output = output + i + "," + j;
				ResultSet rs = stmt.executeQuery( "SELECT * FROM bl.spiel WHERE (spieltag < " + j + ") AND (heim = " + i + "OR gast = " + i + ") ORDER BY spieltag DESC LIMIT 3;" );
				while (rs.next()) {
					if (rs.getInt("heim") != i) {
						output = output + "," + rs.getInt("tore_heim");
					} else {
						output = output + "," + rs.getInt("tore_gast");
					}		
				}
				output = output + "\n";
				writer.write(output);
			}
		}		
		writer.flush();
		writer.close();
		System.out.println("letzte 3 Gegentore");
	}
	
	/**
	 * Anzahl Niederlagen der letzte 5 Spiele für alle Spieltage und Vereine.
	 * 
	 * @throws IOException Exception für Dateizugriffe benötigt
	 * @throws SQLException Exception für Datenbankzugriffe benötigt
	 */
	private static void niederlagen5() throws IOException, SQLException {
		File file = new File ("niederlagen_5.arff");
		file.createNewFile();
		FileWriter writer = new FileWriter(file);
		writer.write("@relation spiel");
		writer.write("\n\n");
		writer.write("@attribute Verein numeric\n");
		writer.write("@attribute Spieltag numeric\n");
		writer.write("@attribute Niederlagen numeric\n\n");
		writer.write("@data\n");
		stmt = c.createStatement();
		for (int i=1; i <= 56; i++) {
			for (int j=38;j > 5;j--) {
				output = "";
				output = output + i + "," + j;
				ResultSet rs = stmt.executeQuery( "SELECT * FROM bl.spiel WHERE (spieltag < " + j + ") AND (heim = " + i + "OR gast = " + i + ") ORDER BY spieltag DESC LIMIT 5;" );
				int lost = 0;
				while (rs.next()) {
					if (rs.getInt("heim") == i) {
						if ( rs.getInt("tore_heim") < rs.getInt("tore_gast") ) {
							lost++;	}
					} else {
						if ( rs.getInt("tore_heim") > rs.getInt("tore_gast") ) {
							lost++;	}
					}
				}
				output = output + "," + lost;
				output = output + "\n";
				writer.write(output);
			}
		}		
		writer.flush();
		writer.close();
		System.out.println("Niederlagen der letzten 5 Spiele");
	}
	
	/**
	 * Anzahl Siege der letzten 5 Spiele für alle Spieltage und Vereine.
	 * 
	 * @throws IOException Exception für Dateizugriffe benötigt
	 * @throws SQLException Exception für Datenbankzugriffe benötigt
	 */
	private static void siege5() throws IOException, SQLException {
		File file = new File ("siege_5.arff");
		file.createNewFile();
		FileWriter writer = new FileWriter(file);
		writer.write("@relation spiel");
		writer.write("\n\n");
		writer.write("@attribute Verein numeric\n");
		writer.write("@attribute Spieltag numeric\n");
		writer.write("@attribute Niederlagen numeric\n\n");
		writer.write("@data\n");
		stmt = c.createStatement();
		for (int i=1; i <= 56; i++) {
			for (int j=38;j > 5;j--) {
				output = "";
				output = output + i + "," + j;
				ResultSet rs = stmt.executeQuery( "SELECT * FROM bl.spiel WHERE (spieltag < " + j + ") AND (heim = " + i + "OR gast = " + i + ") ORDER BY spieltag DESC LIMIT 5;" );
				int won = 0;
				while (rs.next()) {
					if (rs.getInt("heim") == i) {
						if ( rs.getInt("tore_heim") > rs.getInt("tore_gast") ) {
							won++;	}
					} else {
						if ( rs.getInt("tore_heim") < rs.getInt("tore_gast") ) {
							won++;	}
					}
				}
				output = output + "," + won;
				output = output + "\n";
				writer.write(output);
			}
		}		
		writer.flush();
		writer.close();
		System.out.println("Siege der letzten 5 Spiele");
	}

	/**
	 * Anzahl Unentschieden der letzten 5 Spiele für alle Spieltage und Vereine.
	 * 
	 * @throws IOException Exception für Dateizugriffe benötigt
	 * @throws SQLException Exception für Datenbankzugriffe benötigt
	 */
	private static void draw5() throws IOException, SQLException {
		File file = new File ("draws_5.arff");
		file.createNewFile();
		FileWriter writer = new FileWriter(file);
		writer.write("@relation spiel");
		writer.write("\n\n");
		writer.write("@attribute Verein numeric\n");
		writer.write("@attribute Spieltag numeric\n");
		writer.write("@attribute Niederlagen numeric\n\n");
		writer.write("@data\n");
		stmt = c.createStatement();
		for (int i=1; i <= 56; i++) {
			for (int j=38;j > 5;j--) {
				output = "";
				output = output + i + "," + j;
				ResultSet rs = stmt.executeQuery( "SELECT * FROM bl.spiel WHERE (spieltag < " + j + ") AND (heim = " + i + "OR gast = " + i + ") ORDER BY spieltag DESC LIMIT 5;" );
				int draws = 0;
				while (rs.next()) {
					if ( rs.getInt("tore_heim") == rs.getInt("tore_gast") ) {
							draws++; 
					}
				}
				output = output + "," + draws;
				output = output + "\n";
				writer.write(output);
			}
		}		
		writer.flush();
		writer.close();
		System.out.println("Unentschieden der letzten 5 Spiele");
	}
}
