package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ReservationData {

	private final StringProperty isbn;
	private final StringProperty name;
	private final StringProperty surname;
	private final StringProperty memberID;
	private final StringProperty startDate;
	private final StringProperty dueDate;

	public ReservationData(String isbn, String name, String surname, String memberID, String startDate, String dueDate)
	{
		this.isbn = new SimpleStringProperty(isbn);
		this.name = new SimpleStringProperty(name);
		this.surname = new SimpleStringProperty(surname);
		this.memberID = new SimpleStringProperty(memberID);
		this.startDate = new SimpleStringProperty(startDate);
		this.dueDate = new SimpleStringProperty(dueDate);
	}
	
	public String getIsbn(){
		return isbn.get();
		}
	
	public String getName(){
		return name.get();
		}
	
	public String getSurname(){
		return surname.get();
		}
	
	public String getMemberID(){
		return memberID.get();
		}
	
	public String getStartDate(){
		return startDate.get();
		}
	
	public String getDueDate(){
		return dueDate.get();
		}

	public void setIsbn(String value){
		isbn.set(value);
	}
	
	public void setName(String value){
		name.set(value);
	}
	
	public void setSurname(String value){
		surname.set(value);
	}
	
	public void setMemberID(String value){
		memberID.set(value);
	}
	
	public void setStartDate(String value){
		startDate.set(value);
	}
	
	public void setDueDate(String value){
		dueDate.set(value);
	}
	
	public StringProperty isbnProperty(){
		return this.isbn;
	}
	
	public StringProperty nameProperty(){
		return this.name;
	}
	
	public StringProperty surnameProperty(){
		return this.surname;
	}
	
	public StringProperty memberIDProperty(){
		return this.memberID;
	}
	
	public StringProperty startDateProperty(){
		return this.startDate;
	}
	
	public StringProperty dueDateProperty(){
		return this.dueDate;
	}
}
