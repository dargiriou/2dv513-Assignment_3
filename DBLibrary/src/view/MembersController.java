package view;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.DBConnection;
import model.MembersData;

public class MembersController implements Initializable {
	
	@FXML
	private TextField nameField;
	@FXML 
	private TextField surnameField;
	@FXML
	private TextField memberIDField;
	@FXML
	private TextField emailField;
	@FXML
	private TextField addressField;
	@FXML
	private TextField phoneNumberField;
	@FXML
	private TextField search;
	@FXML
	private TableView<MembersData> membersTable;
	@FXML
	private TableColumn<MembersData, String> nameColumn;
	@FXML
	private TableColumn<MembersData, String> surnameColumn;
	@FXML
	private TableColumn<MembersData, String> memberIDColumn;
	@FXML
	private TableColumn<MembersData, String> emailColumn;
	@FXML
	private TableColumn<MembersData, String> addressColumn;
	@FXML
	private TableColumn<MembersData, String> phoneColumn;
	@FXML
	private Button addMemberBtn;
	@FXML
	private Button deleteMemberBtn;
	@FXML
	private Button clearFormBtn;
	@FXML
	private Button updateMemberButton;
	@FXML
	private Button reloadBtn;
	
	@FXML
	private ChoiceBox<String> choiceBox1id;
	
	private ObservableList<MembersData> memberData;
	private ArrayList<MembersData> rowList = new ArrayList<MembersData>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nameColumn.prefWidthProperty().bind(membersTable.widthProperty().divide(6));
		surnameColumn.prefWidthProperty().bind(membersTable.widthProperty().divide(6));
		memberIDColumn.prefWidthProperty().bind(membersTable.widthProperty().divide(6));
		emailColumn.prefWidthProperty().bind(membersTable.widthProperty().divide(6));
		addressColumn.prefWidthProperty().bind(membersTable.widthProperty().divide(6));
		phoneColumn.prefWidthProperty().bind(membersTable.widthProperty().divide(6));
		LoadDatabaseData();
		setCellValueFromTableToTextField();
		Image image = new Image(getClass().getResource("/view/reload.png").toExternalForm(), 20, 20, true, true);
		reloadBtn.setGraphic(new ImageView(image));
		choiceBox1id.setValue("Name");
		choiceBox1id.getItems().add("Name");
		choiceBox1id.getItems().add("Surname");
		choiceBox1id.getItems().add("Member ID");
		choiceBox1id.getItems().add("E-mail");
		choiceBox1id.getItems().add("Address");
		choiceBox1id.getItems().add("Phone number");
		choiceBox1id.setTooltip(
			    new Tooltip("You can also search for a partial string of what you are looking for. Example: 'a'")
			);
		
	}
	
	@FXML
	private void handleClearButton(ActionEvent event)
	{
		clearFields();
	}
	
	private void clearFields()
	{
		this.nameField.setText("");
		this.surnameField.setText("");
		this.memberIDField.setText("");
		this.emailField.setText("");
		this.addressField.setText("");
		this.phoneNumberField.setText("");
		this.search.setText("");
	}
	
	private void LoadDatabaseData()
	{
		try {
			Connection conn = DBConnection.getConnection();
			this.memberData = FXCollections.observableArrayList();
			
			ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM MEMBERS");
			while(rs.next())
			{
				this.memberData.add(new MembersData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
			}
			rs.close();
			conn.close();
		} catch (SQLException e) {
			System.err.println("Error" + e);
		}
		
		this.nameColumn.setCellValueFactory(new PropertyValueFactory<MembersData, String>("name"));
		this.surnameColumn.setCellValueFactory(new PropertyValueFactory<MembersData, String>("surname"));
		this.memberIDColumn.setCellValueFactory(new PropertyValueFactory<MembersData, String>("memberID"));
		this.emailColumn.setCellValueFactory(new PropertyValueFactory<MembersData, String>("email"));
		this.addressColumn.setCellValueFactory(new PropertyValueFactory<MembersData, String>("address"));
		this.phoneColumn.setCellValueFactory(new PropertyValueFactory<MembersData, String>("phone"));
		
		this.membersTable.setItems(null);
		this.membersTable.setItems(this.memberData);

	}
	
	@FXML
	private void reloadButton(ActionEvent event)
	{
		LoadDatabaseData();
	}

	private void setCellValueFromTableToTextField()
	{
		membersTable.setOnMouseClicked(new EventHandler<MouseEvent>(){			
			@Override
			public void handle(MouseEvent event) {
				membersTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
					ObservableList<MembersData> selectedItems = membersTable.getSelectionModel().getSelectedItems();
			        System.out.println(selectedItems.toString());
		      
			        if(selectedItems.size() >1)
			        {
				        for (MembersData row : selectedItems) 
				        {
				        	rowList.add(row);
					    }
			        }
			        else
			        {
			        	MembersData md = membersTable.getItems().get(membersTable.getSelectionModel().getSelectedIndex());
						nameField.setText(md.getName());
						surnameField.setText(md.getSurname());
						memberIDField.setText(String.valueOf(md.getmemberID()));
						emailField.setText(md.getEmail());
						addressField.setText(md.getAddress());
						phoneNumberField.setText(String.valueOf(md.getPhoneNumber()));
			        }
					
				}		
		});
	}
	@FXML
	private void handleAddButton(ActionEvent event)
	{
String sqlInsert = "INSERT INTO MEMBERS(name, surname, memberID, email, address, phone) VALUES (?,?,?,?,?,?)";
		
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement pstmnt = conn.prepareStatement(sqlInsert);
			
				if(validateEmail(emailField.getText()) == true)
				{
					if(emptyValues(nameField.getText(),surnameField.getText(),memberIDField.getText(), emailField.getText(), addressField.getText(), phoneNumberField.getText()) == true)
					{
						pstmnt.setString(1, this.nameField.getText());
						pstmnt.setString(2, this.surnameField.getText());
						pstmnt.setString(3, this.memberIDField.getText());
						pstmnt.setString(4, this.emailField.getText());
						pstmnt.setString(5, this.addressField.getText());
						pstmnt.setString(6, this.phoneNumberField.getText());
						
						pstmnt.execute();
						conn.close();
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Information Dialog");
						alert.setHeaderText(null);
						alert.setContentText("You have successfully added an entry to the database!");
						alert.showAndWait();
						LoadDatabaseData();
						pstmnt.close();
						conn.close();
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
					alert.setTitle("Invalid e-mail!");
					alert.setHeaderText("You have entered an email address!");
					alert.setContentText("Please enter a valid email address!");

					alert.showAndWait();
				}

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	private void handleUpdateButton(ActionEvent event)
	{
		String sqlUpdate = "UPDATE MEMBERS SET name=?, surname=?, memberID=?, email=?, address=?, phone=? WHERE memberID LIKE '"+this.memberIDField.getText()+"'";
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement pstmnt = conn.prepareStatement(sqlUpdate);
			
			pstmnt.setString(1, this.nameField.getText());
			pstmnt.setString(2, this.surnameField.getText());
			pstmnt.setString(3, this.memberIDField.getText());
			pstmnt.setString(4, this.emailField.getText());
			pstmnt.setString(5, this.addressField.getText());
			pstmnt.setString(6, this.phoneNumberField.getText());
			
			pstmnt.executeUpdate();
			pstmnt.close();
			conn.close();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("You have successfully updated an entry of the database!");
			alert.showAndWait();
			LoadDatabaseData();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	private void handleDeleteButton(ActionEvent event)
	{
		Alert alertC = new Alert(AlertType.CONFIRMATION);
		alertC.setTitle("Confirmation Dialog");
		alertC.setHeaderText("Delete entry confirmation Dialog");
		alertC.setContentText("Are you sure you want to delete this entry?");

		Optional<ButtonType> result = alertC.showAndWait();
		if (result.get() == ButtonType.OK){
			String sqlDelete = "DELETE FROM MEMBERS WHERE memberID = ?";
			try {
				Connection conn = DBConnection.getConnection();
				if(rowList.size()>1)
				{
					for(MembersData item : rowList)
					{
						PreparedStatement pstmnt = conn.prepareStatement(sqlDelete);
						
						pstmnt.setString(1, item.getmemberID());
						
						pstmnt.executeUpdate();
						pstmnt.close();
					}
					
				}
				else
				{
					PreparedStatement pstmnt = conn.prepareStatement(sqlDelete);
					
					pstmnt.setString(1, this.memberIDField.getText());
					
					pstmnt.executeUpdate();
					pstmnt.close();
				}

				conn.close();
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);
				alert.setContentText("You have successfully deleted an entry from the database!");
				alert.showAndWait();
				LoadDatabaseData();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			LoadDatabaseData();
		}
	}
	
	@FXML
	private void okToSearch(ActionEvent event)
	{
		try {
			Connection conn = DBConnection.getConnection();
			this.memberData = FXCollections.observableArrayList();
			if(choiceBox1id.getValue().equals("Name"))
			{
				ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM MEMBERS WHERE name LIKE '%" + this.search.getText() + "%'");
				while(rs.next())
				{
					this.memberData.add(new MembersData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
				}
				rs.close();
			}
			else if(choiceBox1id.getValue().equals("Surname"))
			{
				ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM MEMBERS WHERE surname LIKE '%" + this.search.getText() + "%'");
				while(rs.next())
				{
					this.memberData.add(new MembersData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
				}
				rs.close();
			}
			else if(choiceBox1id.getValue().equals("Member ID"))
			{
				ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM MEMBERS WHERE memberID LIKE '%" + this.search.getText() + "%'");
				while(rs.next())
				{
					this.memberData.add(new MembersData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
				}
				rs.close();
			}
			else if(choiceBox1id.getValue().equals("E-mail"))
			{
				ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM MEMBERS WHERE email LIKE '%" + this.search.getText() + "%'");
				while(rs.next())
				{
					this.memberData.add(new MembersData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
				}
				rs.close();
			}
			else if(choiceBox1id.getValue().equals("Address"))
			{
				ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM MEMBERS WHERE address LIKE '%" + this.search.getText() + "%'");
				while(rs.next())
				{
					this.memberData.add(new MembersData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
				}
				rs.close();
			}
			else if(choiceBox1id.getValue().equals("Phone number"))
			{
				ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM MEMBERS WHERE phone LIKE '%" + this.search.getText() + "%'");
				while(rs.next())
				{
					this.memberData.add(new MembersData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
				}
				rs.close();
			}
			conn.close();
		} catch (SQLException e) {
			System.err.println("Error" + e);
		}
		
		this.nameColumn.setCellValueFactory(new PropertyValueFactory<MembersData, String>("name"));
		this.surnameColumn.setCellValueFactory(new PropertyValueFactory<MembersData, String>("surname"));
		this.memberIDColumn.setCellValueFactory(new PropertyValueFactory<MembersData, String>("memberID"));
		this.emailColumn.setCellValueFactory(new PropertyValueFactory<MembersData, String>("email"));
		this.addressColumn.setCellValueFactory(new PropertyValueFactory<MembersData, String>("address"));
		this.phoneColumn.setCellValueFactory(new PropertyValueFactory<MembersData, String>("phone"));
		if(memberData.isEmpty())
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("No results!");
			alert.setHeaderText(null);
			alert.setContentText("Search completed with no results!");
			alert.showAndWait();
			LoadDatabaseData();
		}else{
			this.membersTable.setItems(null);
			this.membersTable.setItems(this.memberData);
		}

	}
	
	/**
	 * Validation methods
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
	
	public boolean validateEmail(String email)
	{
		if(email.contains("@"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean emptyValues(String name,String surname, String memberID, String email,String address, String phone)
	{
		if(name.length() == 0 || surname.length() == 0 || memberID.length() == 0 || email.length() == 0 || address.length() == 0 || phone.length() == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

}
