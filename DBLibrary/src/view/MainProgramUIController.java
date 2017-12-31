package view;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.DBConnection;
import model.StartingTable;

public class MainProgramUIController implements Initializable{

	Connection c = null;
	@FXML
	private TableView<StartingTable> startingTable;
	@FXML
	private TableColumn<StartingTable, String> isbnColumn;
	@FXML
	private TableColumn<StartingTable, String> titleColumn;
	@FXML
	private TableColumn<StartingTable, String> authorColumn;
	@FXML
	private TableColumn<StartingTable, String> branchColumn;
	@FXML
	private TableColumn<StartingTable, String> statusColumn;
	@FXML
	private Button registerBooksBtn;
	@FXML
	private Button registerMembersBtn;
	@FXML
	private Button reservationsBtn;
	@FXML
	private Button authorsBtn;
	@FXML
	private Button publishersBtn;
	@FXML
	private Button goBtn;
	@FXML
	private Button reloadBtn;
	@FXML
	private ChoiceBox<String> dropDownChoose;
	@FXML
	private TextField search;
	@FXML 
	private MenuItem exitMenuItem;
	@FXML 
	private MenuItem about;
	
	private ObservableList<StartingTable>data;
	private DBConnection dbc;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setDbc(new DBConnection());
		Image reloadImg = new Image(getClass().getResource("/view/reload.png").toExternalForm(), 20, 17, true, true);
		reloadBtn.setGraphic(new ImageView(reloadImg));
		dropDownChoose.setValue("ISBN");
		dropDownChoose.getItems().add("ISBN");
		dropDownChoose.getItems().add("Title");
		dropDownChoose.getItems().add("Authors");
		dropDownChoose.getItems().add("Branch");
		LoadDatabaseData();
		isbnColumn.prefWidthProperty().bind(startingTable.widthProperty().divide(5));
		titleColumn.prefWidthProperty().bind(startingTable.widthProperty().divide(5));
		authorColumn.prefWidthProperty().bind(startingTable.widthProperty().divide(5));
		branchColumn.prefWidthProperty().bind(startingTable.widthProperty().divide(5));
		statusColumn.prefWidthProperty().bind(startingTable.widthProperty().divide(5));
		
	}
	@FXML
	private void handleAuthorsButton(ActionEvent event) throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainProgramUIController.class.getResource("/view/authors.fxml"));
		BorderPane authorsPane = loader.load();
		Stage s1 = new Stage();
		s1.initModality(Modality.WINDOW_MODAL);
		Image applicationIcon = new Image(getClass().getResourceAsStream("/view/icon.png"));
		s1.getIcons().add(applicationIcon);
		s1.setTitle("Books Database");
		Scene scene = new Scene(authorsPane);
		s1.setScene(scene);
		s1.show();
	}
	@FXML
	private void handlePublishersButton(ActionEvent event) throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainProgramUIController.class.getResource("/view/publishers.fxml"));
		BorderPane publishersPane = loader.load();
		Stage s1 = new Stage();
		s1.initModality(Modality.WINDOW_MODAL);
		Image applicationIcon = new Image(getClass().getResourceAsStream("/view/icon.png"));
		s1.getIcons().add(applicationIcon);
		s1.setTitle("Books Database");
		Scene scene = new Scene(publishersPane);
		s1.setScene(scene);
		s1.show();
	}
	@FXML
	private void reloadButton(ActionEvent event)
	{
		LoadDatabaseData();
	}
	
	@FXML
	private void handleQuit(ActionEvent event)
	{
		Platform.exit();
	}

	@FXML
	private void handleAbout(ActionEvent event) throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainProgramUIController.class.getResource("/view/about.fxml"));
		BorderPane aboutPane = loader.load();
		Stage s1 = new Stage();
		s1.initModality(Modality.WINDOW_MODAL);
		Image applicationIcon = new Image(getClass().getResourceAsStream("/view/icon.png"));
		s1.getIcons().add(applicationIcon);
		s1.setTitle("About LibraryDB");
		Scene scene = new Scene(aboutPane);
		s1.setScene(scene);
		s1.setResizable(false);
		s1.show();
	}
	
	private void LoadDatabaseData()
	{
		try {
			Connection conn = DBConnection.getConnection();
			this.data = FXCollections.observableArrayList();
			
			ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM BOOKS");
			while(rs.next())
			{
				this.data.add(new StartingTable(rs.getString(1), rs.getString(2), rs.getString(4), rs.getString(5), getStatus(rs.getInt(6))));			
			}
			rs.close();
			conn.close();
		} catch (SQLException e) {
			System.err.println("Error" + e);
		}
		
		this.isbnColumn.setCellValueFactory(new PropertyValueFactory<StartingTable, String>("isbn"));
		this.titleColumn.setCellValueFactory(new PropertyValueFactory<StartingTable, String>("title"));
		this.authorColumn.setCellValueFactory(new PropertyValueFactory<StartingTable, String>("author"));
		this.branchColumn.setCellValueFactory(new PropertyValueFactory<StartingTable, String>("branch"));
		this.statusColumn.setCellValueFactory(new PropertyValueFactory<StartingTable, String>("status"));
		
		this.startingTable.setItems(null);
		this.startingTable.setItems(this.data);
		
	}
	
	@FXML
	private void okToSearch()
	{

		try {
			Connection conn = DBConnection.getConnection();
			this.data = FXCollections.observableArrayList();
			if(dropDownChoose.getValue().equals("ISBN"))
			{
				ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM BOOKS WHERE isbn LIKE '%" + this.search.getText() + "%'");
				while(rs.next())
				{
					this.data.add(new StartingTable(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
				}
				rs.close();
			}
			else if(dropDownChoose.getValue().equals("Title"))
			{
				ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM BOOKS WHERE title LIKE '%" + this.search.getText() + "%'");
				while(rs.next())
				{
					this.data.add(new StartingTable(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
				}
				rs.close();
			}
			else if(dropDownChoose.getValue().equals("Authors"))
			{
				ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM BOOKS WHERE authors LIKE '%" + this.search.getText() + "%'");
				while(rs.next())
				{
					this.data.add(new StartingTable(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
				}
				rs.close();
			}
			else if(dropDownChoose.getValue().equals("Branch"))
			{
				ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM BOOKS WHERE branch LIKE '%" + this.search.getText() + "%'");
				while(rs.next())
				{
					this.data.add(new StartingTable(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
				}
				rs.close();
			}
			conn.close();
		} catch (SQLException e) {
			System.err.println("Error" + e);
		}
		
		this.isbnColumn.setCellValueFactory(new PropertyValueFactory<StartingTable, String>("isbn"));
		this.titleColumn.setCellValueFactory(new PropertyValueFactory<StartingTable, String>("title"));
		this.authorColumn.setCellValueFactory(new PropertyValueFactory<StartingTable, String>("author"));
		this.branchColumn.setCellValueFactory(new PropertyValueFactory<StartingTable, String>("branch"));
		this.statusColumn.setCellValueFactory(new PropertyValueFactory<StartingTable, String>("Status"));
		
		if(data.isEmpty())
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("No results!");
			alert.setHeaderText(null);
			alert.setContentText("Search completed with no results!");
			alert.showAndWait();
			LoadDatabaseData();
		}else{
			this.startingTable.setItems(null);
			this.startingTable.setItems(this.data);
		}
		

	
	}
	
	@FXML
	private void handleRegisterBooksBtn(ActionEvent event) throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainProgramUIController.class.getResource("/view/bookRegistration.fxml"));
		BorderPane bookRegistration = loader.load();
		Stage s1 = new Stage();
		s1.initModality(Modality.WINDOW_MODAL);
		Image applicationIcon = new Image(getClass().getResourceAsStream("/view/icon.png"));
		s1.getIcons().add(applicationIcon);
		s1.setTitle("Books Database");
		Scene scene = new Scene(bookRegistration);
		s1.setScene(scene);
		s1.show();
	}
	
	@FXML
	private void handleMembersButton(ActionEvent event) throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainProgramUIController.class.getResource("/view/createMember.fxml"));
		BorderPane memberReg = loader.load();
		Stage s2 = new Stage();
		s2.initModality(Modality.WINDOW_MODAL);
		Image applicationIcon = new Image(getClass().getResourceAsStream("/view/icon.png"));
		s2.getIcons().add(applicationIcon);
		s2.setTitle("Members Database");
		Scene scene = new Scene(memberReg);
		s2.setScene(scene);
		s2.show();
	}
	
	@FXML
	private void handleReservationButton(ActionEvent event) throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainProgramUIController.class.getResource("/view/reservations.fxml"));
		BorderPane bookRes = loader.load();
		Stage s3 = new Stage();
		s3.initModality(Modality.WINDOW_MODAL);
		Image applicationIcon = new Image(getClass().getResourceAsStream("/view/icon.png"));
		s3.getIcons().add(applicationIcon);
		s3.setTitle("Reservation Database");
		Scene scene = new Scene(bookRes);
		s3.setScene(scene);
		s3.show();
	}
	
	public String getStatus(int copies)
	{
			if(copies > 0)
			{
				return "Available";
			}
			else
			{
				return "Not available";
			
			}
	}

	public DBConnection getDbc() {
		return dbc;
	}

	public void setDbc(DBConnection dbc) {
		this.dbc = dbc;
	}
		
	
}
