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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.AuthorsData;
import model.DBConnection;

public class AuthorsController implements Initializable{

	@FXML
	private TableView<AuthorsData> authorsTable;
	@FXML
	private TableColumn<AuthorsData, String> nameColumn;
	@FXML
	private TableColumn<AuthorsData, String> isbnColumn;
	@FXML
	private TextField nameField;
	@FXML
	private TextField search;
	@FXML
	private Button UpdateBtn;
	@FXML
	private Button DeleteBtn;
	@FXML 
	private Button reloadBtn;
	@FXML 
	private Button goBtn;
	@FXML
	private ChoiceBox<String> choiceBoxAuthors;
	private ObservableList<AuthorsData> authorsData;
	private ArrayList<AuthorsData> rowList = new ArrayList<AuthorsData>();
	AuthorsData ad;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		choiceBoxAuthors.setValue("Available authors");
		populateChoiceBox();
		LoadDatabaseData();
		setCellValueFromTableToTextField();
		Image image = new Image(getClass().getResource("/view/reload.png").toExternalForm(), 20, 20, true, true);
		reloadBtn.setGraphic(new ImageView(image));
		isbnColumn.prefWidthProperty().bind(authorsTable.widthProperty().divide(2));
		nameColumn.prefWidthProperty().bind(authorsTable.widthProperty().divide(2));
		
	}
	
	private void populateChoiceBox()
	{
			Connection conn;
			try {
				conn = DBConnection.getConnection();		
				ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM AUTHORS");
				while(rs.next())
				{
					choiceBoxAuthors.getItems().add(rs.getString(1));
				}
				conn.close();
				rs.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
	
	@FXML
	private void handleReloadButton(ActionEvent event)
	{
		LoadDatabaseData();
	}
	
	private void LoadDatabaseData()
	{
		try {
			Connection conn = DBConnection.getConnection();
			this.authorsData = FXCollections.observableArrayList();
			
			ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM AUTHORS");

			while(rs.next())
			{
				this.authorsData.add(new AuthorsData(rs.getString(1), rs.getString(2)));							
			}
			rs.close();
			conn.close();
		} catch (SQLException e) {
			System.err.println("Error" + e);
		}
		
		this.nameColumn.setCellValueFactory(new PropertyValueFactory<AuthorsData, String>("name"));
		this.isbnColumn.setCellValueFactory(new PropertyValueFactory<AuthorsData, String>("isbn"));
		
		this.authorsTable.setItems(null);
		this.authorsTable.setItems(this.authorsData);

	}
	
	private void setCellValueFromTableToTextField()
	{
		authorsTable.setOnMouseClicked(new EventHandler<MouseEvent>(){			
			@Override
			public void handle(MouseEvent event) {
				authorsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
					ObservableList<AuthorsData> selectedItems = authorsTable.getSelectionModel().getSelectedItems();
			        System.out.println(selectedItems.toString());
		      
			        if(selectedItems.size() >1)
			        {
				        for (AuthorsData row : selectedItems) 
				        {
				        	rowList.add(row);
					    }
			        }
			        else
			        {
			        	ad = authorsTable.getItems().get(authorsTable.getSelectionModel().getSelectedIndex());
			        }
					
				}		
		});
	}
	
	@FXML
	private void handleDeleteBtn(ActionEvent event)
	{
		Alert alertC = new Alert(AlertType.CONFIRMATION);
		alertC.setTitle("Confirmation Dialog");
		alertC.setHeaderText("Delete entry confirmation Dialog");
		alertC.setContentText("Are you sure you want to delete these entries?");

		Optional<ButtonType> result = alertC.showAndWait();
		if (result.get() == ButtonType.OK){
			String sqlDelete = "DELETE FROM AUTHORS WHERE name = ?";
			String sqlDeleteFromBooks = "DELETE FROM BOOKS WHERE isbn = ?";
			
			try {
				Connection conn = DBConnection.getConnection();
				if(rowList.size()>1)
				{
					for(AuthorsData item : rowList)
					{
						ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM BOOKS WHERE authors ='"+item.getName()+"'");
						String fetchedIsdn = rs.getString(1);
						System.out.println(rs.getString(1));
						PreparedStatement pstmnt = conn.prepareStatement(sqlDelete);
						PreparedStatement pstmnt2 = conn.prepareStatement(sqlDeleteFromBooks);
						pstmnt.setString(1, item.getName());
						pstmnt2.setString(1, fetchedIsdn);
						pstmnt.executeUpdate();
						pstmnt2.executeUpdate();
						pstmnt.close();
						pstmnt2.close();
						rs.close();
					}
				}
				else
				{
					ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM BOOKS WHERE authors ='"+ad.getName()+"'");
					String fetchedIsdn = rs.getString(1);
					System.out.println(rs.getString(1));

					PreparedStatement pstmnt = conn.prepareStatement(sqlDelete);
					PreparedStatement pstmnt2 = conn.prepareStatement(sqlDeleteFromBooks);
					pstmnt.setString(1, ad.getName());
					pstmnt2.setString(1, fetchedIsdn);
					pstmnt.executeUpdate();
					pstmnt2.executeUpdate();
					pstmnt.close();
					pstmnt2.close();
					rs.close();
				}


				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);
				alert.setContentText("You have successfully deleted entries from the database!");
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
	private void handleGoButton(ActionEvent event)
	{

		this.authorsData = FXCollections.observableArrayList();
			ResultSet rs;
			try {
				Connection conn = DBConnection.getConnection();
				rs = conn.createStatement().executeQuery("SELECT * FROM AUTHORS WHERE name LIKE '%" + this.search.getText() + "%'");
				while(rs.next())
				{
					this.authorsData.add(new AuthorsData(rs.getString(1), rs.getString(2)));
				}
				conn.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.nameColumn.setCellValueFactory(new PropertyValueFactory<AuthorsData, String>("name"));
			if(authorsData.isEmpty())
			{
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("No results!");
				alert.setHeaderText(null);
				alert.setContentText("Search completed with no results!");
				alert.showAndWait();
				LoadDatabaseData();
			}else{
				this.authorsTable.setItems(null);
				this.authorsTable.setItems(this.authorsData);
			}
			

		}	
}
