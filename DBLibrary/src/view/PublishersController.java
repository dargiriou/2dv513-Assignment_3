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
import model.DBConnection;
import model.PublishersData;

public class PublishersController implements Initializable{

	@FXML
	private TableView<PublishersData> publishersTable;
	@FXML
	private TableColumn<PublishersData, String> nameColumn;
	@FXML
	private TableColumn<PublishersData, String> isbnColumn;
	@FXML
	private TextField nameField;
	@FXML
	private TextField search;
	@FXML
	private Button UpdateBtn;
	@FXML
	private Button DeleteBtn;
	@FXML 
	private Button goBtn;
	@FXML 
	private Button reloadBtn;
	@FXML
	private ChoiceBox<String> choiceBoxPublishers;
	private ObservableList<PublishersData> publishersData;
	private ArrayList<PublishersData> rowList = new ArrayList<PublishersData>();
	PublishersData ad;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		choiceBoxPublishers.setValue("Available publishers");
		populateChoiceBox();
		LoadDatabaseData();
		setCellValueFromTableToTextField();
		Image image = new Image(getClass().getResource("/view/reload.png").toExternalForm(), 20, 20, true, true);
		reloadBtn.setGraphic(new ImageView(image));
		isbnColumn.prefWidthProperty().bind(publishersTable.widthProperty().divide(2));
		nameColumn.prefWidthProperty().bind(publishersTable.widthProperty().divide(2));
	}
	
	@FXML
	private void handleReloadButton(ActionEvent event)
	{
		LoadDatabaseData();
	}
	
	private void populateChoiceBox()
	{
			Connection conn;
			try {
				conn = DBConnection.getConnection();		
				ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM PUBLISHER");
				while(rs.next())
				{
					choiceBoxPublishers.getItems().add(rs.getString(1));
				}
				rs.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
	
	private void LoadDatabaseData()
	{
		try {
			Connection conn = DBConnection.getConnection();
			this.publishersData = FXCollections.observableArrayList();
			
			ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM PUBLISHER");

			while(rs.next())
			{
				this.publishersData.add(new PublishersData(rs.getString(1), rs.getString(2)));							
			}
			rs.close();
			conn.close();
		} catch (SQLException e) {
			System.err.println("Error" + e);
		}
		
		this.nameColumn.setCellValueFactory(new PropertyValueFactory<PublishersData, String>("name"));
		this.isbnColumn.setCellValueFactory(new PropertyValueFactory<PublishersData, String>("isbn"));
		
		this.publishersTable.setItems(null);
		this.publishersTable.setItems(this.publishersData);

	}
	
	private void setCellValueFromTableToTextField()
	{
		publishersTable.setOnMouseClicked(new EventHandler<MouseEvent>(){			
			@Override
			public void handle(MouseEvent event) {
				publishersTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
					ObservableList<PublishersData> selectedItems = publishersTable.getSelectionModel().getSelectedItems();
			        System.out.println(selectedItems.toString());
		      
			        if(selectedItems.size() >1)
			        {
				        for (PublishersData row : selectedItems) 
				        {
				        	rowList.add(row);
					    }
			        }
			        else
			        {
			        	ad = publishersTable.getItems().get(publishersTable.getSelectionModel().getSelectedIndex());
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
		alertC.setContentText("Are you sure you want to delete this entry?");

		Optional<ButtonType> result = alertC.showAndWait();
		if (result.get() == ButtonType.OK){
			String sqlDelete = "DELETE FROM PUBLISHER WHERE name = ?";
			String sqlDeleteFromBooks = "DELETE FROM BOOKS WHERE isbn = ?";
			
			try {
				Connection conn = DBConnection.getConnection();
				if(rowList.size()>1)
				{
					for(PublishersData item : rowList)
					{
						ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM BOOKS WHERE publishers ='"+item.getName()+"'");
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
						
					}
				}
				else
				{
					ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM BOOKS WHERE publishers ='"+ad.getName()+"'");
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

				conn.close();
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

		this.publishersData = FXCollections.observableArrayList();
			ResultSet rs;
			try {
				Connection conn = DBConnection.getConnection();
				rs = conn.createStatement().executeQuery("SELECT * FROM PUBLISHER WHERE name LIKE '%" + this.search.getText() + "%'");
				while(rs.next())
				{
					this.publishersData.add(new PublishersData(rs.getString(1), rs.getString(2)));
				}
			rs.close();
			conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.nameColumn.setCellValueFactory(new PropertyValueFactory<PublishersData, String>("name"));
			if(publishersData.isEmpty())
			{
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("No results!");
				alert.setHeaderText(null);
				alert.setContentText("Search completed with no results!");
				alert.showAndWait();
				LoadDatabaseData();
			}else{
				this.publishersTable.setItems(null);
				this.publishersTable.setItems(this.publishersData);
			}

		}	
}
