package dbs_project;

import java.sql.*;

import javax.swing.JOptionPane;

/**
 * Führt die geforderten SELECT-Statements aus
 * und gibt die Ergebnisse in der Konsole aus.
 * 
 * @author Gruppenarbeit
 * @version 2.1
 */
public class select {
	
	static Connection c = null;
	static Statement stmt = null;
	static String SRC = "";
	static String USER = "";
	static String PASSWORD = "";

	/**
	 * Stellt die Verbindung zur Datenbank her.<br>
	 * Führt zu Fehlern, wenn Datenbank nicht existiert.
	 * 
	 * @param db Hostadresse der Datenbank
	 * @param user Benutzername für Datenbank
	 * @param password Passwort für Datenbank
	 * @throws ClassNotFoundException Exception für Postgres-Verbindung benötigt
	 * @throws SQLException Exception für Datenbankzugriffe benötigt
	 */
	private static void connect(String db, String user, String password) throws ClassNotFoundException, SQLException {
		SRC = db;
		USER = user;
		PASSWORD = password;		
		Class.forName("org.postgresql.Driver");
		c = DriverManager.getConnection(SRC, USER, PASSWORD);
		c.setAutoCommit(false);
		stmt = c.createStatement();
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
	 * An welchem Tag fand das erste Spiel der Saison statt?
	 * 
	* @param db Hostadresse der Datenbank
	 * @param user Benutzername für Datenbank
	 * @param password Passwort für Datenbank
	 * @throws SQLException Exception für Datenbankzugriffe benötigt
	 * @throws ClassNotFoundException Exception für Postgres-Verbindung benötigt
	 * @see #connect(String, String, String)
	 * @see #disconnect()
	 */
	public static void select1(String db, String user, String password) throws SQLException, ClassNotFoundException {
		connect(db, user, password);
		System.out.println("Das erste Spiel der Saison:");
		ResultSet rs = stmt.executeQuery( "SELECT datum FROM bl.spiel ORDER BY datum ASC LIMIT 1;" );
		while ( rs.next() ) {
			Date datum = rs.getDate("datum");
			System.out.println( "Das erste Spiel der Saison fand statt am " + datum );
		}
		rs.close();
		System.out.println();
		disconnect();
	}

	/**
	 * Welche Spieler haben in dieser Saison bereits mehr als fünf Tore geschossen?
	 * 
	 * @param db Hostadresse der Datenbank
	 * @param user Benutzername für Datenbank
	 * @param password Passwort für Datenbank
	 * @throws SQLException Exception für Datenbankzugriffe benötigt
	 * @throws ClassNotFoundException Exception für Postgres-Verbindung benötigt
	 * @see #connect(String, String, String)
	 * @see #disconnect()
	 */
	public static void select2(String db, String user, String password) throws SQLException, ClassNotFoundException {
		connect(db, user, password);
		System.out.println("Spieler mit mehr als 5 Toren");
		ResultSet rs = stmt.executeQuery( "SELECT s_name, tore FROM bl.spieler WHERE tore > 5 ORDER BY tore DESC;" );
		while ( rs.next() ) {
			String s_name = rs.getString("s_name");
			int tore = rs.getInt("tore");
			System.out.print( "Tore: " + tore + ";\tSpieler: " + s_name  );
			System.out.println();
		}
		rs.close();
		System.out.println();
		disconnect();
	}
	
	/**
	 * Zeige die Daten aller Spiele an, die am ersten Spieltag aller drei Ligen nach 17 Uhr begonnen haben.
	 * 
	* @param db Hostadresse der Datenbank
	 * @param user Benutzername für Datenbank
	 * @param password Passwort für Datenbank
	 * @throws SQLException Exception für Datenbankzugriffe benötigt
	 * @throws ClassNotFoundException Exception für Postgres-Verbindung benötigt
	 * @see #connect(String, String, String)
	 * @see #disconnect()
	 */
	public static void select3(String db, String user, String password) throws SQLException, ClassNotFoundException {
		connect(db, user, password);
		System.out.println("Spiele am ersten Spieltag nach 17 Uhr:");
		ResultSet rs = stmt.executeQuery( "SELECT * FROM bl.spiel WHERE spieltag = 1 AND uhrzeit > '17:00:00';" );
		while ( rs.next() ) {
			int spiel_id = rs.getInt("spiel_id");
			int spieltag = rs.getInt("spieltag");
			Date datum = rs.getDate("datum");
			Time uhrzeit = rs.getTime("uhrzeit");
			int heim = rs.getInt("heim");
			int gast = rs.getInt("gast");
			int tore_heim = rs.getInt("tore_heim");
			int tore_gast = rs.getInt("tore_gast");
			System.out.print( "ID: " + spiel_id + ";   \t");
			System.out.print( "Spieltag: " + spieltag + ";\t");
			System.out.print( "Datum: " + datum + ";\t");
			System.out.print( "Uhrzeit: " + uhrzeit + ";\t");
			System.out.print( "Heim: " + heim + ";\t");
			System.out.print( "Gast: " + gast + ";\t");
			System.out.print( "Tore Heim: " + tore_heim + ";\t");
			System.out.print( "Tore Gast: " + tore_gast + ";");
			System.out.println();
		}
		rs.close();
		System.out.println();
		disconnect();
	}
	
	/**
	 * Welche Spieler spielen für den Verein "FC Bayern München"? Gib auch die Trikotnummer 
	 * und das Heimatland jedes Spielers sowie die Anzahl seiner Tore mit aus. Ordne die 
	 * Ergebnisse aufsteigend nach der Trikotnummer.
	 * 
	* @param db Hostadresse der Datenbank
	 * @param user Benutzername für Datenbank
	 * @param password Passwort für Datenbank
	 * @throws SQLException Exception für Datenbankzugriffe benötigt
	 * @throws ClassNotFoundException Exception für Postgres-Verbindung benötigt
	 * @see #connect(String, String, String)
	 * @see #disconnect()
	 */
	public static void select4(String db, String user, String password) throws SQLException, ClassNotFoundException {
		connect(db, user, password);
		System.out.println("Spieler bei FC Bayern München:");
		ResultSet rs = stmt.executeQuery( "SELECT s_name, trikot_nr, land, tore FROM bl.spieler, bl.verein WHERE verein.name='FC Bayern München' AND verein.v_id=spieler.vereins_id ORDER BY trikot_nr ASC;" );
		while ( rs.next() ) {
			String s_name = rs.getString("s_name");
			int trikot_nr = rs.getInt("trikot_nr");
			String land = rs.getString("land");
			int tore = rs.getInt("tore");
			System.out.print( "Trikotnummer: " + trikot_nr + ";\t");
			System.out.print( "Land: " + land + ";\t");
			System.out.print( "Tore: " + tore + ";\t");
			System.out.print( "Spieler: " + s_name + ";");
			System.out.println();
		}
		rs.close();
		System.out.println();
		disconnect();
	}
	
	/**
	 * Wie viele Spiele hat "Hannover 96" bis heute gewonnen?
	 * 
	* @param db Hostadresse der Datenbank
	 * @param user Benutzername für Datenbank
	 * @param password Passwort für Datenbank
	 * @throws SQLException Exception für Datenbankzugriffe benötigt
	 * @throws ClassNotFoundException Exception für Postgres-Verbindung benötigt
	 * @see #connect(String, String, String)
	 * @see #disconnect()
	 */
	public static void select5(String db, String user, String password) throws SQLException, ClassNotFoundException {
		connect(db, user, password);
		System.out.println("Siege von Hannover 96:");
		ResultSet rs = stmt.executeQuery( "SELECT * FROM bl.spiel, bl.verein WHERE verein.name='Hannover 96' AND ((verein.v_id=spiel.heim AND spiel.tore_heim > tore_gast) OR (verein.v_id=spiel.gast AND spiel.tore_heim < tore_gast));" );
		int rowCount = 0;
		while ( rs.next() ) {
			rowCount++;
		}
		System.out.println("Hannover 96 hat bis heute " + rowCount + " Spiele gewonnen.");
		rs.close();
		System.out.println();
		disconnect();
	}
	
	/**
	 * Gesucht sind Vereinsname, Spieler_ID, Trikotnummer und Name aller Spieler, die für den 
	 * Verein spielen, der in dieser Saison die meisten Niederlagen erlitten hat (auch mehrere 
	 * Vereine mit gleicher Anzahl möglich).
	 * 
	 * @param db Hostadresse der Datenbank
	 * @param user Benutzername für Datenbank
	 * @param password Passwort für Datenbank
	 * @throws SQLException Exception für Datenbankzugriffe benötigt
	 * @throws ClassNotFoundException Exception für Postgres-Verbindung benötigt
	 * @see #connect(String, String, String)
	 * @see #disconnect()
	 */
	public static void select6(String db, String user, String password) throws SQLException, ClassNotFoundException {
		connect(db, user, password);
		System.out.print("Meiste Niederlagen: ");
		ResultSet rs = stmt.executeQuery( "SELECT COUNT (v_id) FROM bl.verein;");
		rs.next();
		int v_count = rs.getInt(1);
		int[] out = new int [1];
		int most_l = 0;
		for (int i = 1; i <= v_count; i++) {
			rs = stmt.executeQuery( "SELECT COUNT (*) FROM bl.spiel WHERE ( heim = " + i + " AND tore_heim < tore_gast) OR ( gast = " + i + " AND tore_gast < tore_heim ) ;" );
			rs.next();
			if (rs.getInt(1) > most_l) {
				out = new int[1];
				most_l = rs.getInt(1);
				out[0] = i;
			} else if (rs.getInt(1) == most_l) {
				int[] tmpArr = new int[out.length + 1];
				for (int k = 0; k < out.length; k++) {
					tmpArr[k] = out[k];
				}
				tmpArr[tmpArr.length-1] = i;
				out = tmpArr;
			}
		}
		System.out.println(most_l);
		for (int j = 0; j < out.length;j++){
			rs = stmt.executeQuery( "SELECT verein.name, spieler_id, trikot_nr, s_name FROM bl.spieler, bl.verein WHERE spieler.vereins_id = " + out[j] + " AND v_id  = " + out[j] + ";" );
			while ( rs.next() ) {
				String name = rs.getString("name");
				int spieler_id = rs.getInt("spieler_id");
				int trikot_nr = rs.getInt("trikot_nr");
				String s_name = rs.getString("s_name");
				System.out.print( "Verein: " + name + ";\t");
				System.out.print( "S_ID: " + spieler_id + ";\t");
				System.out.print( "Trikotnummer: " + trikot_nr + ";\t");
				System.out.print( "Spieler: " + s_name + ";");
				System.out.println();
			}
		}
		rs.close();
		System.out.println();
		disconnect();
	}

	/**
	 * <p>Bietet die Möglichkeit ein eigenes Query einzugeben und ausführen zu lassen.</p>
	 * Die Postgres-Syntax muss eingehalten werden.<br>
	 * Nutzt Standard-Query bei leerer Eingabe.<br>
	 * Führt zu Fehlern bei falscher Eingabe und Abbruch.
	 * 
	 * @param db Hostadresse der Datenbank
	 * @param user Benutzername für Datenbank
	 * @param password Passwort für Datenbank
	 * @throws SQLException Exception für Datenbankzugriffe benötigt
	 * @throws ClassNotFoundException Exception für Postgres-Verbindung benötigt
	 * @see #connect(String, String, String)
	 * @see #disconnect()
	 */
	public static void select_e(String db, String user, String password) throws SQLException, ClassNotFoundException {
		connect(db, user, password);
		System.out.println("Query nach Benutzereingabe:");
		String query = JOptionPane.showInputDialog( "Bitte hier Query eingeben: \n" +
													"(Postgres-Syntax benutzen)");
		if ( query.isEmpty()) {
			System.out.println("Da muss schon was kommen!\nZ.B.: SELECT * FROM bl.liga;");
			query = "SELECT * FROM bl.liga;";
		}
		ResultSet rs = stmt.executeQuery( query );
		ResultSetMetaData rsmd = rs.getMetaData();
		while ( rs.next() ) {
			String out = "";
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				if (i > 1) {
					out = out + ",\t";
				}
				rsmd.getColumnType(i);
				int type = rsmd.getColumnType(i);
	            if (type == Types.VARCHAR ) {
	                out = out + rs.getString(i);
	            } else if (type == Types.INTEGER ) {
	                out = out + rs.getInt(i);
	            } else if (type == Types.DATE) {
	            	out = out + rs.getDate(i);
	            } else if (type == Types.TIME) {
	            	out = out + rs.getTime(i);
	            }
			}
			System.out.print( out  );
			System.out.println();
		}
		rs.close();
		System.out.println();
		disconnect();
	}
}
