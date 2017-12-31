package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MembersData {

	private final StringProperty name;
	private final StringProperty surname;
	private final StringProperty memberID;
	private final StringProperty email;
	private final StringProperty address;
	private final StringProperty phone;

	public MembersData(String name, String surname, String memberID, String email, String address, String phone)
	{
		this.name = new SimpleStringProperty(name);
		this.surname = new SimpleStringProperty(surname);
		this.memberID = new SimpleStringProperty(memberID);
		this.email = new SimpleStringProperty(email);
		this.address = new SimpleStringProperty(address);
		this.phone = new SimpleStringProperty(phone);
	}
	
	public String getName(){
		return this.name.get();
		}
	
	public String getSurname(){
		return this.surname.get();
		}
	
	public String getmemberID(){
		return this.memberID.get();
		}
	
	public String getEmail(){
		return this.email.get();
		}
	
	public String getAddress(){
		return this.address.get();
		}
	
	public String getPhoneNumber(){
		return this.phone.get();
		}
	

	public void setName(String value){
		this.name.set(value);
	}
	
	public void setSurname(String value){
		this.surname.set(value);
	}
	
	public void setMembersID(String value){
		this.memberID.set(value);
	}
	
	public void setAddress(String value){
		this.address.set(value);
	}
	
	public void setPhonenumber(String value){
		this.phone.set(value);
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
	
	public StringProperty emailProperty(){
		return this.email;
	}
	public StringProperty addressProperty(){
		return this.address;
	}
	public StringProperty phoneProperty(){
		return this.phone;
	}
}
