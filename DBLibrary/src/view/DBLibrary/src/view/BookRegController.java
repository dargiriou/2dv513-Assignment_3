package view;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.BooksData;
import model.DBConnection;

public class BookRegController implements Initializable{
	
	@FXML
	private TextField isbn;
	@FXML
	private TextField title;
	@FXML
	private TextField author;
	@FXML
	private TextField publisher;
	@FXML
	private TextField branch;
	@FXML
	private TextField copies;
	@FXML
	private TextField search;
	
	@FXML
	private TableView<BooksData> booksTable;
	@FXML
	private TableColumn<BooksData, String> isbnColumn;
	@FXML
	private TableColumn<BooksData, String> titleColumn;
	@FXML
	private TableColumn<BooksData, String> authorColumn;
	@FXML
	private TableColumn<BooksData, String> publisherColumn;
	@FXML
	private TableColumn<BooksData, String> branchColumn;
	@FXML
	private TableColumn<BooksData, Integer> copiesColumn;
	
	@FXML
	private Button addBtn;
	@FXML
	private Button deleteBtn;
	@FXML
	private Button clearBtn;
	@FXML
	private Button updateBtn;
	@FXML
	private Button reloadBtn;
	
	@FXML
	private ChoiceBox<String> choiceBox1id;
	
	private DBConnection dc;
	private ObservableList<BooksData> bookData;
	private ArrayList<BooksData> rowList = new ArrayList<BooksData>();
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		LoadDatabaseData();
		this.setDc(new DBConnection());
		Image image = new Image(getClass().getResource("/view/reload.png").toExternalForm(), 20, 17, true, true);
		reloadBtn.setGraphic(new ImageView(image));
		choiceBox1id.setValue("ISBN");
		choiceBox1id.getItems().add("ISBN");
		choiceBox1id.getItems().add("Title");
		choiceBox1id.getItems().add("Authors");
		choiceBox1id.getItems().add("Publishers");
		choiceBox1id.getItems().add("Branch");		
		setCellValueFromTableToTextField();
		isbnColumn.prefWidthProperty().bind(booksTable.widthProperty().divide(6));
		titleColumn.prefWidthProperty().bind(booksTable.widthProperty().divide(6));
		authorColumn.prefWidthProperty().bind(booksTable.widthProperty().divide(6));
		publisherColumn.prefWidthProperty().bind(booksTable.widthProperty().divide(6));
		branchColumn.prefWidthProperty().bind(booksTable.widthProperty().divide(6));
		copiesColumn.prefWidthProperty().bind(booksTable.widthProperty().divide(6));
	}
	

	private void setCellValueFromTableToTextField()
	{
		booksTable.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				booksTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				ObservableList<BooksData> selectedItems = booksTable.getSelectionModel().getSelectedItems();
		        System.out.println(selectedItems.toString());
	      
		        if(selectedItems.size() >1)
		        {
			        for (BooksData row : selectedItems) 
			        {
			        	rowList.add(row);
				    }
		        }
		        else
		        {
					BooksData bd = booksTable.getItems().get(booksTable.getSelectionModel().getSelectedIndex());
					isbn.setText(bd.getIsbn());
					title.setText(bd.getTitle());
					author.setText(bd.getAuthor());
					publisher.setText(bd.getPublisher());
					branch.setText(bd.getBranch());
					copies.setText(String.valueOf(bd.getNumOfCopies()));
		        }				
			}			
		});
	}
	
	private void LoadDatabaseData()
	{
		try {
			Connection conn = DBConnection.getConnection();
			this.bookData = FXCollections.observableArrayList();
			
			ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM BOOKS");
			while(rs.next())
			{
				this.bookData.add(new BooksData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6)));
			}
			rs.close();
			conn.close();
		} catch (SQLException e) {
			System.err.println("Error" + e);
		}
		
		this.isbnColumn.setCellValueFactory(new PropertyValueFactory<BooksData, String>("isbn"));
		this.titleColumn.setCellValueFactory(new PropertyValueFactory<BooksData, String>("title"));
		this.authorColumn.setCellValueFactory(new PropertyValueFactory<BooksData, String>("author"));
		this.publisherColumn.setCellValueFactory(new PropertyValueFactory<BooksData, String>("publisher"));
		this.branchColumn.setCellValueFactory(new PropertyValueFactory<BooksData, String>("branch"));
		this.copiesColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumOfCopies()).asObject());
		
		this.booksTable.setItems(null);
		this.booksTable.setItems(this.bookData);

	}
	
	
	@FXML
	private void handleAddButton(ActionEvent event)
	{
		String sqlInsert = "INSERT INTO BOOKS(isbn, title, authors, publishers, branch, copies) VALUES (?,?,?,?,?,?)";
		
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement pstmnt = conn.prepareStatement(sqlInsert);
			if(validateISBN(isbn.getText()) == true)
			{
				if(validateCopies(copies.getText()) == true)
				{
					if(emptyValues(title.getText(),author.getText(), publisher.getText(), branch.getText()) == true)
					{
						pstmnt.setString(1, this.isbn.getText());
						pstmnt.setString(2, this.title.getText());
						pstmnt.setString(3, this.author.getText());
						pstmnt.setString(4, this.publisher.getText());
						pstmnt.setString(5, this.branch.getText());
						pstmnt.setInt(6, Integer.valueOf(this.copies.getText()));
						
						pstmnt.execute();
						conn.close();
						pstmnt.close();
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Information Dialog");
						alert.setHeaderText(null);
						alert.setContentText("You have successfully added an entry to the database!");
						alert.showAndWait();
						LoadDatabaseData();
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
					alert.setTitle("Invalid number of copies");
					alert.setHeaderText("You have entered an invalid input for number of book copies");
					alert.setContentText("Please enter a number!");

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
	private void clearFields()
	{
		this.isbn.setText("");
		this.title.setText("");
		this.author.setText("");
		this.publisher.setText("");
		this.branch.setText("");
		this.copies.setText("");
		this.search.setText("");
	}
	
	@FXML
	private void handleClearButton(ActionEvent event)
	{
		clearFields();
	}
	
	@FXML
	private void reloadButton(ActionEvent event)
	{
		LoadDatabaseData();
	}
	
	@FXML
	private void handleUpdateButton(ActionEvent event)
	{ 
		String sqlUpdate = "UPDATE BOOKS SET isbn=?, title=?, authors=?, publishers=?, branch=?, copies=? WHERE isbn='"+this.isbn.getText()+"'";
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement pstmnt = conn.prepareStatement(sqlUpdate);
			
			pstmnt.setString(1, this.isbn.getText());
			pstmnt.setString(2, this.title.getText());
			pstmnt.setString(3, this.author.getText());
			pstmnt.setString(4, this.publisher.getText());
			pstmnt.setString(5, this.branch.getText());
			pstmnt.setInt(6, Integer.parseInt(this.copies.getText()));
			
			pstmnt.executeUpdate();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("You have successfully updated an entry of the database!");
			alert.showAndWait();
			LoadDatabaseData();
			conn.close();
			pstmnt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	private void okToSearch(ActionEvent event)
	{
		try {
			Connection conn = DBConnection.getConnection();
			this.bookData = FXCollections.observableArrayList();
			if(choiceBox1id.getValue().equals("ISBN"))
			{
				ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM BOOKS WHERE isbn LIKE '%" + this.search.getText() + "%'");
				while(rs.next())
				{
					this.bookData.add(new BooksData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6)));
				}
				rs.close();
			}
			else if(choiceBox1id.getValue().equals("Title"))
			{
				ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM BOOKS WHERE title LIKE '%" + this.search.getText() + "%'");
				while(rs.next())
				{
					this.bookData.add(new BooksData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6)));
				}
				rs.close();
			}
			else if(choiceBox1id.getValue().equals("Authors"))
			{
				ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM BOOKS WHERE authors LIKE '%" + this.search.getText() + "%'");
				while(rs.next())
				{
					this.bookData.add(new BooksData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6)));
				}
				rs.close();
			}
			else if(choiceBox1id.getValue().equals("Publishers"))
			{
				ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM BOOKS WHERE publishers LIKE '%" + this.search.getText() + "%'");
				while(rs.next())
				{
					this.bookData.add(new BooksData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6)));
				}
				rs.close();
			}
			else if(choiceBox1id.getValue().equals("Branch"))
			{
				ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM BOOKS WHERE branch LIKE '%" + this.search.getText() + "%'");
				while(rs.next())
				{
					this.bookData.add(new BooksData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6)));
				}
				rs.close();
			}
			conn.close();
			
		} catch (SQLException e) {
			System.err.println("Error" + e);
		}
		
		this.isbnColumn.setCellValueFactory(new PropertyValueFactory<BooksData, String>("isbn"));
		this.titleColumn.setCellValueFactory(new PropertyValueFactory<BooksData, String>("title"));
		this.authorColumn.setCellValueFactory(new PropertyValueFactory<BooksData, String>("author"));
		this.publisherColumn.setCellValueFactory(new PropertyValueFactory<BooksData, String>("publisher"));
		this.branchColumn.setCellValueFactory(new PropertyValueFactory<BooksData, String>("branch"));
		this.copiesColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumOfCopies()).asObject());
		
		if(bookData.isEmpty())
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("No results!");
			alert.setHeaderText(null);
			alert.setContentText("Search completed with no results!");
			alert.showAndWait();
			LoadDatabaseData();
		}else{
			this.booksTable.setItems(null);
			this.booksTable.setItems(this.bookData);
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
			String sqlDelete = "DELETE FROM BOOKS WHERE isbn = ?";
			String sqlDeleteFromAuthors = "DELETE FROM AUTHORS WHERE isbn=?";
			String sqlDeleteFromPublishers = "DELETE FROM PUBLISHERS WHERE isbn=?";
			try {
				Connection conn = DBConnection.getConnection();
				if(rowList.size()>1)
				{
					for(BooksData item : rowList)
					{
						PreparedStatement pstmnt = conn.prepareStatement(sqlDelete);
						PreparedStatement pstmnt2 = conn.prepareStatement(sqlDeleteFromAuthors);
						PreparedStatement pstmnt3 = conn.prepareStatement(sqlDeleteFromPublishers);
						pstmnt.setString(1, item.getIsbn());
						pstmnt2.setString(1, item.getIsbn());
						pstmnt3.setString(1, item.getIsbn());
						pstmnt.executeUpdate();
						pstmnt2.executeUpdate();
						pstmnt3.executeUpdate();
					}
				}
				else
				{
					PreparedStatement pstmnt = conn.prepareStatement(sqlDelete);
					PreparedStatement pstmnt2 = conn.prepareStatement(sqlDeleteFromAuthors);
					pstmnt.setString(1, this.isbn.getText());
					
					pstmnt2.setString(1, this.isbn.getText());
					pstmnt.executeUpdate();
					pstmnt2.executeUpdate();
				}

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
	
	public boolean validateCopies(String copies)
	{
	    try
	    {
	        Integer.parseInt(copies);
	    }
	    catch(NumberFormatException ex)
	    {
	        return false;
	    }
	    return true;
	}
	
	public boolean emptyValues(String title,String author, String publisher, String branch)
	{
		if(title.length() == 0 || author.length() == 0 || publisher.length() == 0 || branch.length() == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}


	public DBConnection getDc() {
		return dc;
	}


	public void setDc(DBConnection dc) {
		this.dc = dc;
	}
}
