package view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class aboutController implements Initializable{

	final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
    
	@FXML
	private Hyperlink hyperLink;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		hyperLink.setOnAction(new EventHandler<ActionEvent>() {
	        @Override public void handle(ActionEvent e) {
	        	Stage s1 = new Stage();
	        	VBox root = new VBox();
	        	
	        	root.setPrefSize(1000, 600);
	        	Scene scene = new Scene(new Group());
	            webEngine.load("https://github.com/dargiriou/2dv513_Assignment_3");
	            root.getChildren().addAll(hyperLink,browser);
	            scene.setRoot(root);
	     
	            s1.setScene(scene);
	            s1.show();
	        }
	    });
		
	}
	


	
}
