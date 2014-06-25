package dbs_project;

import java.sql.*;

public class select {
	
	static Connection c = null;
	static Statement stmt = null;
	static String query = null;

	public static void open_up(int sel,String db,String user, String password){
		
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection(db, user, password);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}
	//	System.out.println("Verbindung erfolgreich hergestellt");
	//	System.out.println();
		
		switch (sel) {
		case 1:
			select1();
			break;
		case 2:
			select2();
			break;
		case 3:
			select3();
			break;
		case 4:
			select4();
			break;
		case 5:
			select5();
			break;
		case 6:
			select6();	
			break;	
		}
		
	}
	
	public static void select1() {
		System.out.println("Das erste Spiel der Saison:");
		
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT datum FROM bl.spiel ORDER BY datum ASC LIMIT 1;" );
			while ( rs.next() ) {
				Date datum = rs.getDate("datum");
				System.out.println( "Das erste Spiel der Saison fand statt am " + datum );
			}
			rs.close();
			stmt.close();
			c.close();

		} catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
		}
		System.out.println();
	}

	public static void select2() {
		System.out.println("Spieler mit mehr als 5 Toren");
		
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT s_name, tore FROM bl.spieler WHERE tore > 5 ORDER BY tore DESC;" );
			while ( rs.next() ) {
				String s_name = rs.getString("s_name");
				int tore = rs.getInt("tore");
				System.out.print( "Tore: " + tore + ";\tSpieler: " + s_name  );
				System.out.println();
			}
			rs.close();
			stmt.close();
			c.close(); 
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
		}
		System.out.println();
	}
	
	public static void select3() {
		System.out.println("Spiele am ersten Spieltag nach 17 Uhr:");
		
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
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
			stmt.close();
			c.close(); 
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
		}
		System.out.println();
	}
	
	public static void select4() {
		System.out.println("Spieler bei FC Bayern München:");
		
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
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
			stmt.close();
			c.close(); 
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
		}
		System.out.println();
	}
	
	public static void select5() {
		System.out.println("Siege von Hannover 96:");
		
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT * FROM bl.spiel, bl.verein WHERE verein.name='Hannover 96' AND ((verein.v_id=spiel.heim AND spiel.tore_heim > tore_gast) OR (verein.v_id=spiel.gast AND spiel.tore_heim < tore_gast));" );
			int rowCount = 0;
			while ( rs.next() ) {
				rowCount++;
			}
			System.out.println("Hannover 96 hat bis heute " + rowCount + " Spiele gewonnen.");
			rs.close();
			stmt.close();
			c.close(); 
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
		}
		System.out.println();
	}
	
	public static void select6() {
		System.out.println("Meiste Niederlagen:");
		
		// TO BE FILLED
		
		System.out.println("Fehlen noch.");
	}
}
