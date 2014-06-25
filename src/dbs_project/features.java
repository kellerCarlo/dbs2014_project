package dbs_project;

import java.io.*;
import java.sql.*;

public class features {

	static Connection c = null;
	static Statement stm1 = null;
	static Statement stm2 = null;
	static String query = null;

	public static void open_up(String db,String user, String password) throws IOException {

		File file = new File ("letzte_3.arff");
		file.createNewFile();
		FileWriter writer = new FileWriter(file);
		writer.write("@relation spiel");
		writer.write("\n\n");
		writer.write("@attribute Verein string\n");
		writer.write("@attribute Spieltag numeric\n");
		writer.write("@attribute Tore1 numeric\n");
		writer.write("@attribute Tore2 numeric\n");
		writer.write("@attribute Tore3 numeric\n\n");
		writer.write("@data\n");

		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection(db, user, password);
			c.setAutoCommit(false);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}
		System.out.println("-->");
		String output = "";
		try {
			stm1 = c.createStatement();
			for (int i=1; i <= 56; i++) {
				for (int j=38;j > 3;j--) {
					output = "";
					output = output + i + "," + j;
					ResultSet rs = stm1.executeQuery( "SELECT * FROM bl.spiel WHERE (spieltag < " + j + ") AND (heim = " + i + "OR gast = " + i + ") ORDER BY spieltag DESC LIMIT 3;" );
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
			
			stm1.close();
			c.close();

		} catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
		}
		System.out.println();
	}
}

			
			/*
			rs = stmt.executeQuery( "SELECT * FROM bl.spiel WHERE (spieltag BETWEEN " + st_a + " AND " + st_b + ") AND heim=11;" );
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
			rs = stmt.executeQuery( "SELECT * FROM bl.spiel WHERE (spieltag BETWEEN " + st_a + " AND " + st_b + ") AND gast=11;" );
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
			}*/
