package cm3033.CyrilNiobe;
	
import java.net.URL;

import javax.swing.ImageIcon;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;

/**
 * Main class which is called when we run the program
 * Start the interface
 * @author Cyril Niobé - 2016
 * Module: Concurrent Programming Coursework - Robert Gordon University
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ServerGUI.fxml"));
			Parent root = (Parent) loader.load(getClass().getResource("ServerGUI.fxml").openStream());
			Scene scene = new Scene(root);
			MenuBar menubar = new MenuBar();
			((BorderPane) root).setBottom(menubar);
			menubar.setUseSystemMenuBar(true);
			Menu filemenu = new Menu("About");
			Menu editmenu = new Menu("Server");
			menubar.getMenus().addAll(filemenu,editmenu);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Big Brother house voting");
			primaryStage.show();
			

			ServerGUIController mainController = loader.<ServerGUIController>getController();
			
			Timeline timeline = new Timeline(new KeyFrame(
								Duration.millis(100),
								new EventHandler<ActionEvent>() {
							          @Override public void handle(ActionEvent actionEvent) {
							        	  if(mainController != null)
							        		  mainController.refresh();
							          }
					            }
		    ));
		    timeline.setCycleCount(Animation.INDEFINITE);
		    timeline.play();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
        /**
         * main method
         * @param args 
         */
	public static void main(String[] args) {
		try {
	        URL iconURL = Main.class.getResource("dock_icon.jpg");
	        java.awt.Image image = new ImageIcon(iconURL).getImage();
	        com.apple.eawt.Application.getApplication().setDockIconImage(image);
	    } catch (Exception e) {
	        // Won't work on Windows or Linux.
	    }
		
		launch(args);
	}
		
}
