package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AuthorsData {

	private final StringProperty name;
	private final StringProperty isbn;
	
	public AuthorsData(String name, String isbn)
	{
		this.name = new SimpleStringProperty(name);
		this.isbn = new SimpleStringProperty(isbn);
	}

	public String getName() {
		return name.get();
	}

	public String getIsbn() {
		return isbn.get();
	}
	
	public void setIsbn(String value){
		name.set(value);
	}
	
	public void setTitle(String value){
		isbn.set(value);
	}
	
	public StringProperty nameProperty(){
		return this.name;
	}
	
	public StringProperty isbnProperty(){
		return this.isbn;
	}
}
