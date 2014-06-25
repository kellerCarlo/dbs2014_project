package dbs_project;

import java.io.IOException;

public class bundesliga {
	public static void main(String args[]) throws IOException {
	
		final String SRC = "jdbc:postgresql://localhost:5432/";
		final String USER = "postgres";
		final String PASSWORD = "dbs2014";
		final String DBNAME = "bundesliga";
		
	//	create.db_create(SRC, USER, PASSWORD);
		
		select.open_up(1, SRC + DBNAME, USER, PASSWORD);
	//	select.open_up(2, SRC + DBNAME, USER, PASSWORD);
	//	select.open_up(3, SRC + DBNAME, USER, PASSWORD);
	//	select.open_up(4, SRC + DBNAME, USER, PASSWORD);
		select.open_up(5, SRC + DBNAME, USER, PASSWORD);
	//	select.open_up(6, SRC + DBNAME, USER, PASSWORD);
		
	//	features.open_up(SRC + DBNAME, USER, PASSWORD);
	}

}
