package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StartingTable {
	private final StringProperty isbn;
	private final StringProperty title;
	private final StringProperty author;
	private final StringProperty branch;
	private final StringProperty status;

	public StartingTable(String isbn, String title, String author, String branch, String status)
	{
		this.isbn = new SimpleStringProperty(isbn);
		this.title = new SimpleStringProperty(title);
		this.author = new SimpleStringProperty(author);
		this.branch = new SimpleStringProperty(branch);
		this.status = new SimpleStringProperty(status);
	}
	
	public String getIsbn(){
		return this.isbn.get();
		}
	
	public String getTitle(){
		return this.title.get();
		}
	
	public String getAuthor(){
		return this.author.get();
		}
	
	public String getBranch(){
		return this.branch.get();
		}
	
	public String getStatus(){
		return this.status.get();
		}
	

	public void setIsbn(String value){
		this.isbn.set(value);
	}
	
	public void setTitle(String value){
		this.title.set(value);
	}
	
	public void setAuthor(String value){
		this.author.set(value);
	}
	
	public void setBranch(String value){
		this.branch.set(value);
	}
	
	public void setStatus(String value){
		this.status.set(value);
	}
	
	
	public StringProperty isbnProperty(){
		return this.isbn;
	}
	
	public StringProperty titleProperty(){
		return this.title;
	}
	
	public StringProperty authorProperty(){
		return this.author;
	}
	
	public StringProperty branchProperty(){
		return this.branch;
	}
	public StringProperty statusProperty(){
		return this.status;
	}
}
