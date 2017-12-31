package model;

import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Program extends Application{
	private Stage primaryStage;
	
	
	
	public static void main(String[] args) throws SQLException {

		CreateDatabase cd = new CreateDatabase(DBConnection.getConnection());
		cd.createTables();
				launch(args);				
	}
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		showMainLayout();
		Image applicationIcon = new Image(getClass().getResourceAsStream("/view/icon.png"));
        primaryStage.getIcons().add(applicationIcon);
        primaryStage.setTitle("Library Database");
	}
	
	public void showMainLayout() throws IOException
	{
		Parent root = FXMLLoader.load(Program.class.getResource("/view/mainProgram.fxml"));	
		Scene scene = new Scene(root);
		scene.getStylesheets().add(this.getClass().getResource("/view/LibDb.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
}
