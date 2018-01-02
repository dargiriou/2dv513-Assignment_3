package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BooksData {

	private final StringProperty isbn;
	private final StringProperty title;
	private final StringProperty author;
	private final StringProperty publisher;
	private final StringProperty branch;
	private final IntegerProperty copies;

	public BooksData(String isbn, String title, String author, String publisher, String branch, int numOfcopies)
	{
		this.isbn = new SimpleStringProperty(isbn);
		this.title = new SimpleStringProperty(title);
		this.author = new SimpleStringProperty(author);
		this.publisher = new SimpleStringProperty(publisher);
		this.branch = new SimpleStringProperty(branch);
		this.copies = new SimpleIntegerProperty(numOfcopies);
	}
	
	public String getIsbn(){
		return isbn.get();
		}
	
	public String getTitle(){
		return title.get();
		}
	
	public String getAuthor(){
		return author.get();
		}
	
	public String getPublisher(){
		return publisher.get();
		}
	
	public String getBranch(){
		return branch.get();
		}
	
	public int getNumOfCopies(){
		return copies.get();
		}

	public void setIsbn(String value){
		isbn.set(value);
	}
	
	public void setTitle(String value){
		title.set(value);
	}
	
	public void setAuthor(String value){
		author.set(value);
	}
	
	public void setPublisher(String value){
		publisher.set(value);
	}
	
	public void setBranch(String value){
		branch.set(value);
	}
	
	public void setNumOfCopies(int value){
		copies.set(value);
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
	
	public StringProperty publisherProperty(){
		return this.publisher;
	}
	
	public StringProperty branchProperty(){
		return this.branch;
	}
	
	public IntegerProperty numOfCopiesProperty(){
		return this.copies;
	}
}
