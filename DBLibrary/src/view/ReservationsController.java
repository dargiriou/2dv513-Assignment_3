package view;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import model.DBConnection;
import model.ReservationData;

public class ReservationsController  implements Initializable{

	@FXML
	private TextField isbnField;
	@FXML
	private TextField nameField;
	@FXML 
	private TextField surnameField;
	@FXML
	private TextField memberIDField;
	@FXML
	private TextField search;
	@FXML
	private TableView<ReservationData> reservationsTable;
	@FXML
	private TableColumn<ReservationData, String> isbnColumn;
	@FXML
	private TableColumn<ReservationData, String> nameColumn;
	@FXML
	private TableColumn<ReservationData, String> surnameColumn;
	@FXML
	private TableColumn<ReservationData, String> memberIDColumn;
	@FXML
	private TableColumn<ReservationData, String> startDateColumn;
	@FXML
	private TableColumn<ReservationData, String> dueDateColumn;
	@FXML
	private Button startReservationBtn;
	@FXML
	private Button endReservationBtn;
	@FXML
	private Button clearFormBtn;
	@FXML
	private Button goButton;
	@FXML
	private Button reloadBtn;
	@FXML
	private DatePicker startDatePicker;
	@FXML
	private DatePicker dueDatePicker;
	@FXML
	private ChoiceBox<String> choiceBox1id;
	@FXML
	private BorderPane bookRes;

	
	private ObservableList<ReservationData> reservationData;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		bookRes.setMinSize(1361, 581);
		setCellValueFromTableToTextField();
		LoadDatabaseData();
		Image image = new Image(getClass().getResource("/view/reload.png").toExternalForm(), 20, 17, true, true);
		reloadBtn.setGraphic(new ImageView(image));
		choiceBox1id.setValue("Member ID");
		choiceBox1id.getItems().add("Name");
		choiceBox1id.getItems().add("Surname");
		choiceBox1id.getItems().add("Member ID");
		choiceBox1id.getItems().add("ISBN");
		choiceBox1id.getItems().add("Starting date");
		choiceBox1id.getItems().add("Due date");	
		
		isbnColumn.prefWidthProperty().bind(reservationsTable.widthProperty().divide(6));
		nameColumn.prefWidthProperty().bind(reservationsTable.widthProperty().divide(6));
		surnameColumn.prefWidthProperty().bind(reservationsTable.widthProperty().divide(6));
		memberIDColumn.prefWidthProperty().bind(reservationsTable.widthProperty().divide(6));
		startDateColumn.prefWidthProperty().bind(reservationsTable.widthProperty().divide(6));
		dueDateColumn.prefWidthProperty().bind(reservationsTable.widthProperty().divide(6));
	}
	
	/**
	 * Button action event method to clear all the text fields
	 * @param event
	 */
	@FXML
	private void handleClearButton(ActionEvent event)
	{
		this.nameField.setText("");
		this.surnameField.setText("");
		this.memberIDField.setText("");
		this.isbnField.setText("");
	}
	
	/**
	 * Button action event method to reload all the data in the table view
	 * @param event
	 */
	@FXML
	private void reloadButton(ActionEvent event)
	{
		LoadDatabaseData();
	}
	
	/**
	 * Connects to the database, selects everything from the table Reservations and loads the values to the appropriate cells
	 * in the table view
	 */
	private void LoadDatabaseData()
	{
		try {
			Connection conn = DBConnection.getConnection();
			this.reservationData = FXCollections.observableArrayList();
			
			ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM RESERVATIONS");
			while(rs.next())
			{
				this.reservationData.add(new ReservationData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
			}
		rs.close();
		conn.close();
		} catch (SQLException e) {
			System.err.println("Error" + e);
		}
		
		this.isbnColumn.setCellValueFactory(new PropertyValueFactory<ReservationData, String>("isbn"));
		this.nameColumn.setCellValueFactory(new PropertyValueFactory<ReservationData, String>("name"));
		this.surnameColumn.setCellValueFactory(new PropertyValueFactory<ReservationData, String>("surname"));
		this.memberIDColumn.setCellValueFactory(new PropertyValueFactory<ReservationData, String>("memberID"));
		this.startDateColumn.setCellValueFactory(new PropertyValueFactory<ReservationData, String>("startDate"));
		this.dueDateColumn.setCellValueFactory(new PropertyValueFactory<ReservationData, String>("dueDate"));
		
		this.reservationsTable.setItems(null);
		this.reservationsTable.setItems(this.reservationData);

	}
	/**
	 * Gets the date and formats it appropriately into LocalDate form
	 * @param dateString
	 * @return
	 */
	public static final LocalDate LOCAL_DATE (String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
	    return localDate;
	   
	}

	/**
	 * when a row on the table view is clicked the attributes of each cell are fetched and
	 * automatically fill all the appropriate text fields.
	 */
	
	
	private void setCellValueFromTableToTextField()
	{
		reservationsTable.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				
				ReservationData rd = reservationsTable.getItems().get(reservationsTable.getSelectionModel().getSelectedIndex());
					isbnField.setText(rd.getIsbn());
					nameField.setText(rd.getName());
					surnameField.setText(rd.getSurname());
					memberIDField.setText(String.valueOf(rd.getMemberID()));	
					startDatePicker.setValue(LOCAL_DATE(rd.getStartDate()));
					dueDatePicker.setValue(LOCAL_DATE(rd.getDueDate()));
			}			
		});
	}
	StringBuilder sb = new StringBuilder();
	/**
	 * On keyTyped event it gets everything that is typed and checks the members table for the ID,
	 * if it finds an ID, it returns the name and surname and automatically fills the rest of the text fields.
	 * @param event
	 */
	@FXML
	private void handleKeyTyped(KeyEvent event)
	{
		if(event.getCharacter().matches(("^.*[^a-zA-Z0-9 ].*$")))
		{
			sb = new StringBuilder();
		}
		else {
			sb.append(event.getCharacter());
		}	
		try {
			Connection conn = DBConnection.getConnection();
			ResultSet rs = conn.createStatement().executeQuery("SELECT name, surname FROM MEMBERS WHERE memberID='"+sb.toString()+"'");
			try {		
				nameField.setText(rs.getString(1));
				surnameField.setText(rs.getString(2));
				if(rs.getString(1) != null)
				{
					sb = new StringBuilder();
				}
				
		} catch (SQLException e) {
			System.err.println("No results");
		}
			rs.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	/**
	 * Connects to the database and inserts all the text field values to the appropriate columns in the database
	 * @param event
	 */
	@FXML
	private void handleStartReservation(ActionEvent event)
	{
		String sqlInsert = "INSERT INTO RESERVATIONS(isbn, name, surname, memberID, startDate, dueDate) VALUES (?,?,?,?,?,?)";
		String sqlUpdateAfterReservation = "UPDATE BOOKS SET copies=?  WHERE isbn='"+this.isbnField.getText()+"'";
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement pstmnt = conn.prepareStatement(sqlInsert);
			PreparedStatement pstmnt2 = conn.prepareStatement(sqlUpdateAfterReservation);
			if(validateISBN(isbnField.getText()) == true)
			{
				
					if(emptyValues(nameField.getText(),surnameField.getText(), memberIDField.getText()) == true)
					{
						if(bookExists(isbnField.getText()))
						{
							if(memberExists(memberIDField.getText()))
							{
								if(datesSelected(startDatePicker.getEditor().getText(),dueDatePicker.getEditor().getText()))
								{
									ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM BOOKS");
									int copiesAfterReservation = (rs.getInt(6)) - 1;
									
									pstmnt.setString(1, this.isbnField.getText());
									pstmnt.setString(2, this.nameField.getText());
									pstmnt.setString(3, this.surnameField.getText());
									pstmnt.setString(4, this.memberIDField.getText());
									pstmnt.setString(5, this.startDatePicker.getEditor().getText());
									pstmnt.setString(6, this.dueDatePicker.getEditor().getText());
									pstmnt2.setInt(1, copiesAfterReservation);
									
									if(copiesAfterReservation ==0)
									{
										Alert alert = new Alert(AlertType.WARNING);
										alert.setTitle("No more copies of that book left");
										alert.setHeaderText("You cannot complete the reservation");
										alert.setContentText("A book will be available when one is returned.");
										alert.showAndWait();
									}else
									{
										pstmnt.execute();
										pstmnt2.executeUpdate();
										pstmnt.close();
										pstmnt2.close();
										
										conn.close();
										Alert alert = new Alert(AlertType.INFORMATION);
										alert.setTitle("Information Dialog");
										alert.setHeaderText(null);
										alert.setContentText("You have successfully added an entry to the database!");
										alert.showAndWait();
									}						

									LoadDatabaseData();
									rs.close();
								}
								else
								{
									Alert alert = new Alert(AlertType.WARNING);
									alert.setTitle("Dates not selected");
									alert.setHeaderText("You cannot reserve the book without selecting valid dates");
									alert.setContentText("Please choose dates for the reservation");
									alert.showAndWait();
								}
							}
							else
							{
								Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("Member not found in the database");
								alert.setHeaderText("You cannot reserve the book on this member ID");
								alert.setContentText("Please enter a valid member ID");
								alert.showAndWait();
							}

						}
						else
						{
							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("Book not found in the database");
							alert.setHeaderText("You cannot reserve the book with this ISBN");
							alert.setContentText("Please enter a valid book ISBN number");
							alert.showAndWait();
						}

					}
					else
					{
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Empty text fields");
						alert.setHeaderText("You have not entered a value in at least one of the text fields");
						alert.setContentText("Please enter a value in all of the text fileds!");

						alert.showAndWait();
					}
					
				}
			
			else
			{
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Invalid ISBN");
				alert.setHeaderText("You have entered an invalid ISBN number");
				alert.setContentText("Allowed ISBN between 10 and 13 numbers long");

				alert.showAndWait();
			}

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Connects to the database and updates the values of the columns appropriately to the values that the
	 * text fields hold
	 * @param event
	 */
	@FXML
	private void handleUpdateReservation(ActionEvent event)
	{
		String sqlUpdate = "UPDATE RESERVATIONS SET isbn=?, name=?, surname=?, memberID=?, startDate=?, dueDate=? WHERE isbn LIKE '"+this.isbnField.getText()+"'";
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement pstmnt = conn.prepareStatement(sqlUpdate);
			
			pstmnt.setString(1, this.isbnField.getText());
			pstmnt.setString(2, this.nameField.getText());
			pstmnt.setString(3, this.surnameField.getText());
			pstmnt.setString(4, this.memberIDField.getText());
			pstmnt.setString(5, this.startDatePicker.getEditor().getText());
			pstmnt.setString(6, this.startDatePicker.getEditor().getText());
			
			pstmnt.executeUpdate();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("You have successfully updated a reservation entry of the database!");
			alert.showAndWait();
			LoadDatabaseData();
			pstmnt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes the chosen entry from the database and calculates the days that has passed from the due date
	 * and multiplies the dates with 20 Swedish crowns for each day.
	 * @param event
	 */
	@FXML
	private void handleEndReservation(ActionEvent event)
	{
		Alert alertC = new Alert(AlertType.CONFIRMATION);
		alertC.setTitle("Confirmation Dialog");
		alertC.setHeaderText("End reservation confirmation Dialog");
		alertC.setContentText("Are you sure you want to end this reservation?");

		Optional<ButtonType> result = alertC.showAndWait();
		if (result.get() == ButtonType.OK){
			String sqlDelete = "DELETE FROM RESERVATIONS WHERE memberID = ?";
			
			String sqlUpdateAfterReservation = "UPDATE BOOKS SET copies=?  WHERE isbn='"+this.isbnField.getText()+"'";
			try {
				Connection conn = DBConnection.getConnection();
				ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM BOOKS");
				int copiesAfterReservationEnded = (rs.getInt(6)) + 1;
				PreparedStatement pstmnt = conn.prepareStatement(sqlDelete);
				PreparedStatement pstmnt2 = conn.prepareStatement(sqlUpdateAfterReservation);
				
				pstmnt.setString(1, this.memberIDField.getText());
				pstmnt2.setInt(1, copiesAfterReservationEnded);
				pstmnt.executeUpdate();
				pstmnt2.executeUpdate();
				pstmnt.close();
				pstmnt2.close();
				LocalDate dueDate = dueDatePicker.getValue();
				LocalDateTime localDateTime = LocalDateTime.now();
				LocalDate localDate = localDateTime.toLocalDate();
				Long due = dueDate.toEpochDay();
				Long now = localDate.toEpochDay();
				Long timePassed = now - due;
				double fees = timePassed * 20;
				if(timePassed > 0)
				{
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Delayed return fee must be paid!");
					alert.setHeaderText("Delayed return of: " + timePassed + " days.");
					alert.setContentText("Delay return fee: " + fees + " SEK");
					alert.showAndWait();
				}
				else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Book return successful!");
					alert.setHeaderText(null);
					alert.setContentText("You have successfully ended a book reservation!");
					alert.showAndWait();
				}
				LoadDatabaseData();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			LoadDatabaseData();
		}
	}
	/**
	 * Searches for attributes in the database but not with '=', with LIKE command, it is slower but it is easier to use 
	 * if not exact attribute names are not remembered.
	 * @param event
	 */
	@FXML
	private void handleGoButton(ActionEvent event)
	{
		try {
			Connection conn = DBConnection.getConnection();
			this.reservationData = FXCollections.observableArrayList();
			if(choiceBox1id.getValue().equals("Name"))
			{
				ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM RESERVATIONS WHERE isbn LIKE '%" + this.search.getText() + "%'");
				while(rs.next())
				{
					this.reservationData.add(new ReservationData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
				}
				rs.close();
			}
			else if(choiceBox1id.getValue().equals("Surname"))
			{
				ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM RESERVATIONS WHERE name LIKE '%" + this.search.getText() + "%'");
				while(rs.next())
				{
					this.reservationData.add(new ReservationData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
				}
				rs.close();
			}
			else if(choiceBox1id.getValue().equals("Member ID"))
			{
				ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM RESERVATIONS WHERE surname LIKE '%" + this.search.getText() + "%'");
				while(rs.next())
				{
					this.reservationData.add(new ReservationData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
				}
				rs.close();
			}
			else if(choiceBox1id.getValue().equals("E-mail"))
			{
				ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM RESERVATIONS WHERE memberID LIKE '%" + this.search.getText() + "%'");
				while(rs.next())
				{
					this.reservationData.add(new ReservationData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
				}
				rs.close();
			}
			else if(choiceBox1id.getValue().equals("Address"))
			{
				ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM RESERVATIONS WHERE startDate LIKE '%" + this.search.getText() + "%'");
				while(rs.next())
				{
					this.reservationData.add(new ReservationData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
				}
				rs.close();
			}
			else if(choiceBox1id.getValue().equals("Phone number"))
			{
				ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM RESERVATIONS WHERE endDate LIKE '%" + this.search.getText() + "%'");
				while(rs.next())
				{
					this.reservationData.add(new ReservationData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
				}
				rs.close();
			}
			conn.close();
		} catch (SQLException e) {
			System.err.println("Error" + e);
		}
		
		this.isbnColumn.setCellValueFactory(new PropertyValueFactory<ReservationData, String>("isbn"));
		this.nameColumn.setCellValueFactory(new PropertyValueFactory<ReservationData, String>("name"));
		this.surnameColumn.setCellValueFactory(new PropertyValueFactory<ReservationData, String>("surname"));
		this.memberIDColumn.setCellValueFactory(new PropertyValueFactory<ReservationData, String>("memberID"));
		this.startDateColumn.setCellValueFactory(new PropertyValueFactory<ReservationData, String>("startDate"));
		this.dueDateColumn.setCellValueFactory(new PropertyValueFactory<ReservationData, String>("dueDate"));
		if(reservationData.isEmpty())
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("No results!");
			alert.setHeaderText(null);
			alert.setContentText("Search completed with no results!");
			alert.showAndWait();
			LoadDatabaseData();
		}else{
			this.reservationsTable.setItems(null);
			this.reservationsTable.setItems(this.reservationData);
		}
	}
	
	/**
	 * validate isbn method
	 * @param isbn
	 * @return
	 */
	public boolean validateISBN(String isbn)
	{
		if(isbn.length() > 13 || isbn.length() < 10)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	/*
	 *Validates if data has been entered to all of the text fields 
	 */
	public boolean emptyValues(String name,String surname, String memberID)
	{
		if(name.length() == 0 || surname.length() == 0 || memberID.length() == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public boolean bookExists(String isbn) throws SQLException
	{
		Connection conn = DBConnection.getConnection();
		ResultSet rs = conn.createStatement().executeQuery("SELECT isbn FROM BOOKS WHERE isbn='"+isbn+"'");
		while(rs.next())
		{
			if(rs.getString(1) != null)
			{
				rs.close();
				conn.close();
				return true;
			}
			else
			{
				rs.close();
				conn.close();
				return false;
			}
		}
		rs.close();
		conn.close();
		return false;
	}
	
	public boolean memberExists(String member) throws SQLException
	{
		Connection conn = DBConnection.getConnection();
		ResultSet rs = conn.createStatement().executeQuery("SELECT memberID FROM MEMBERS WHERE memberID='"+member+"'");
		while(rs.next())
		{
			if(rs.getString(1) != null)
			{
				rs.close();
				conn.close();
				return true;
			}
			else
			{
				rs.close();
				conn.close();
				return false;
			}
		}
		rs.close();
		conn.close();
		return false;
	}
	
	public boolean datesSelected(String startDate, String dueDate)
	{
		if(startDate == null || dueDate == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}
