package model;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateDatabase {

	
	private Connection c;
	CreateDatabase(Connection c)
	{
		this.c = c;
	}
	
	private String BOOKS = "BOOKS";
	private String AUTHORS = "AUTHORS";
	private String MEMBERS = "MEMBERS";
	private String RESERVATIONS = "RESERVATIONS";
	private String PUBLISHERS = "PUBLISHER";
	
	public void createTables() {  
        Statement stmnt;
        String query;

        try {
        	stmnt = c.createStatement();
        	query = "CREATE TABLE IF NOT EXISTS " + BOOKS +
                    "( isbn TEXT PRIMARY KEY, " +
            		"title TEXT NOT NULL ," +
                    "publishers TEXT NOT NULL,"+
                    "authors TEXT NOT NULL,"+
                    "branch TEXT NOT NULL,"+
                    "copies INT NOT NULL ," +
                    " FOREIGN KEY(authors) REFERENCES "
                    + AUTHORS
                    + " (name) ON DELETE CASCADE  ," +
                    " FOREIGN KEY(publishers) REFERENCES "
                    +  PUBLISHERS 
                    +" (name) ON DELETE CASCADE )";          		
            stmnt.executeUpdate(query);
            stmnt.close();

            stmnt = c.createStatement();
            query = "CREATE TABLE IF NOT EXISTS " + RESERVATIONS +
                    " (isbn TEXT NOT NULL ," +
            		"name TEXT NOT NULL ," +
                    "surname TEXT NOT NULL ," +
                    "memberID INT NOT NULL ," +
                    "startDate TEXT NOT NULL ," +
                    "dueDate TEXT NOT NULL)";
            stmnt.executeUpdate(query);
            stmnt.close();

            stmnt = c.createStatement();
            query = "CREATE TABLE IF NOT EXISTS " + AUTHORS +
                    "( name TEXT PRIMARY KEY," +
                    "isbn TEXT NOT NULL," +
                    " FOREIGN KEY(isbn) REFERENCES "
                    + BOOKS
                    + " (isbn) ON DELETE CASCADE)";
            stmnt.executeUpdate(query);
            stmnt.close();
            
            stmnt = c.createStatement();
            query = "CREATE TABLE IF NOT EXISTS " + PUBLISHERS +
                    "( name TEXT PRIMARY KEY," +
                    "isbn TEXT NOT NULL, " +
                    "FOREIGN KEY(isbn) REFERENCES BOOKS (isbn) ON DELETE CASCADE)";
            stmnt.executeUpdate(query);
            stmnt.close();
            
            stmnt = c.createStatement();
            query = "CREATE TABLE IF NOT EXISTS " + MEMBERS +
                    "( name TEXT NOT NULL , " +
                    "surname TEXT NOT NULL , " +
                    "memberID TEXT PRIMARY KEY , " +
                    "email TEXT NOT NULL , " +
                    "address TEXT NOT NULL ," +
                    "phone TEXT NOT NULL )";
            stmnt.executeUpdate(query);
            stmnt.close();
            
            stmnt = c.createStatement();
            query = "CREATE TRIGGER IF NOT EXISTS "
            		+ "insertAfterToAutors AFTER INSERT ON BOOKS "
            		+ "FOR EACH ROW "
            		+ "BEGIN "
            		+ "INSERT INTO AUTHORS ( name, isbn ) VALUES ( NEW.authors, NEW.isbn ); END";
            stmnt.executeUpdate(query);
            stmnt.close();
            
            stmnt = c.createStatement();
            query = "CREATE TRIGGER IF NOT EXISTS "
            		+ "insertAfterToPublishers AFTER INSERT ON BOOKS "
            		+ "FOR EACH ROW "
            		+ "BEGIN "
            		+ "INSERT INTO PUBLISHER ( name,isbn ) VALUES ( NEW.publishers, NEW.isbn); END";
            stmnt.executeUpdate(query);
            stmnt.close();
            
            stmnt = c.createStatement();
            query = "CREATE TRIGGER IF NOT EXISTS "
            		+ "updateAuthors BEFORE UPDATE ON BOOKS "
            		+ "BEGIN "
            		+ "UPDATE AUTHORS "
            		+ "SET name=new.authors, isbn=new.isbn ; END";
            stmnt.executeUpdate(query);
            stmnt.close();
            

            
        } catch (SQLException e) {
            e.printStackTrace();
        }
}
}
