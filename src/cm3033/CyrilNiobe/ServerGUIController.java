package cm3033.CyrilNiobe;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
* @author Cyril Niobé - 2016
* Module: Concurrent Programming Coursework - Robert Gordon University
*/
public class ServerGUIController implements Initializable {
	
        /**
         * Server
         */
	public Server serv;

        /**
         * Add Button
         */
	@FXML
	private Button BtnAdd;

        /**
         * Delete Button
         */
	@FXML
	private Button BtnDelete;
	
        /**
         * Start Button
         */
	@FXML
	private Button BtnStart;
	
        /**
         * Open button
         */
	@FXML
	private Button BtnOpen;
	
        /**
         * Close Button
         */
	@FXML
	private Button BtnClose;
	
        /**
         * End Button
         */
	@FXML
	private Button BtnEnd;
	
        /**
         * Label of the name of the highest vote
         */
	@FXML
	private Label LblHighestVote;
	
        /**
         * Text Area use for display logs
         */
	@FXML
	private TextArea TxtAreaLogs;

        /**
         * ListView of names which represents the nominated persons
         */
	@FXML
	private ListView<String> ListviewNames;

        /**
         * TextField to add a name
         */
	@FXML
	private TextField TxtFieldName;
	
        /**
         * BarChart of votes
         */
	@FXML
	private BarChart<String,Integer> ChartVotes;

        /**
         * CategoryAxis use for data in BarChart
         */
        @FXML
        private CategoryAxis xAxis;

	/**
         * ObservableList of string which contains all the names added by the administrator
         */
	final ObservableList<String> listItems = FXCollections.observableArrayList();

		
	// Add event handlers on the add button
	@FXML
	private void addAction(ActionEvent action) {
		if (!TxtFieldName.getText().isEmpty()) {
			if (!listItems.contains(TxtFieldName.getText())) {
				listItems.add(TxtFieldName.getText());
				TxtFieldName.clear();
			}
		}
	}

        // Add event handlers on the delete button
	@FXML
	private void deleteAction(ActionEvent action) {
		int selectedItem = ListviewNames.getSelectionModel().getSelectedIndex();
		listItems.remove(selectedItem);
	}

       /**
        * Add event handlers on the start button
        * Method which start the server
        * @param action 
        */
	@FXML
	private void startAction(ActionEvent action) {
		
		//Show an error if the list doesn't contain at least 2 nominated.
		if (listItems.size() <= 1 || listItems.size() > 10) {
			Alert alertList = new Alert(AlertType.ERROR, "Your list of nominated should have a minimum of 2 candidates and a maximum of 10");
			alertList.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
					.forEach(node -> ((Label) node).setMinHeight(Region.USE_PREF_SIZE));
			alertList.showAndWait();
		} else {
			
			// Start voting round
			List<Nominated> listNominated = new ArrayList<Nominated>();
			for (String nominatedNames : listItems) {
				listNominated.add(new Nominated(nominatedNames));
			}
			VotingRound votingRound = new VotingRound(listNominated);
			
			// Start server
			this.serv = new Server(votingRound);
			Thread threadServ = new Thread(serv);
			threadServ.start();
			Alert infoServ = new Alert(AlertType.INFORMATION, "Server started. You still have to start the voting round.");
			infoServ.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
					.forEach(node -> ((Label) node).setMinHeight(Region.USE_PREF_SIZE));
			infoServ.show();
			
			BtnOpen.setDisable(false);
			BtnEnd.setDisable(false);
			BtnAdd.setDisable(true);
			BtnDelete.setDisable(true);
                        BtnAdd.setDisable(true);
			BtnDelete.setDisable(true);
                        TxtFieldName.setDisable(true);
			ListviewNames.setDisable(true);
			xAxis.setCategories(listItems);
		}
	}
	
	/**
         * Add event handlers on the open button
	 * Method to open the voting round and set the opening date
	 * @param action
	 */
	@FXML
	private void openAction(ActionEvent action){
		this.serv.getVotingRound().setOpen(true);
		BtnClose.setDisable(false);
	
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
		Date date = new Date();
		this.serv.votingRound.setDateOpening(date);
		this.serv.votingRound.addLogs("Voting round opened at: " + dateFormat.format(date));
		
		Alert infoOpen = new Alert(AlertType.INFORMATION, "Voting Round is now open. Clients can vote.");
		infoOpen.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
				.forEach(node -> ((Label) node).setMinHeight(Region.USE_PREF_SIZE));
		infoOpen.showAndWait();
	}
	
	/**
         * Add event handlers on the close button
	 * Method to close the voting round and set the ending date
	 * @param action
	 */
	@FXML
	private void closeAction(ActionEvent action) {
		this.serv.getVotingRound().setOpen(false);
		
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
		Date date = new Date();
		this.serv.votingRound.setDateEnding(date);
		this.serv.votingRound.addLogs("Voting round closed at: " + dateFormat.format(date));
		
		Alert infoClose = new Alert(AlertType.INFORMATION, "Voting Round is now closed.");
		infoClose.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
				.forEach(node -> ((Label) node).setMinHeight(Region.USE_PREF_SIZE));
		infoClose.showAndWait();
		
	}
	
        /**
         * Add event handlers on the start button
         * Method to end the server and quit the application
         * @param action 
         */
	@FXML
	private void endAction(ActionEvent action){
		this.serv.getPool().shutdownNow();
		Stage stage = (Stage) BtnEnd.getScene().getWindow();
	    stage.close();
	}

	/**
         * Method which will be called first to initialize the interface and components
         * @param location
         * @param resources 
         */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// Set the ListView with a list of items
		ListviewNames.setItems(listItems);

		// Disable buttons at start
		BtnAdd.setDisable(true);
		BtnDelete.setDisable(true);
		BtnOpen.setDisable(true);
		BtnClose.setDisable(true);
		BtnEnd.setDisable(true);

		// Add a ChangeListener to TextField to look for change in focus
		TxtFieldName.focusedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (TxtFieldName.isFocused()) {
					BtnAdd.setDisable(false);

				}
				if (listItems.isEmpty()) {
					BtnDelete.setDisable(true);
				}
			}
		});

		// Add a KeyPressed event to the TxtFieldName
		TxtFieldName.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					if (!TxtFieldName.getText().isEmpty()) {
						if (!listItems.contains(TxtFieldName.getText())) {
							listItems.add(TxtFieldName.getText());
							TxtFieldName.clear();
						}
					}
				}
			}
		});

		// Add a ChangeListener to ListView to look for change in focus
		ListviewNames.focusedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (ListviewNames.isFocused()) {
					BtnDelete.setDisable(false);
				}
				if (listItems.isEmpty())
					BtnDelete.setDisable(true);
			}
		});
		
		// Add a Listener on the text area to scroll to the bottom on each new text
		TxtAreaLogs.textProperty().addListener(new ChangeListener<Object>() {
		    @Override
		    public void changed(ObservableValue<?> observable, Object oldValue,
		            Object newValue) {
		    	TxtAreaLogs.setScrollTop(Double.MAX_VALUE);
		    }
		});	
		
	}
	
	/**
	 * Method tp fill with data the BarChart
	 * @param listNominated : list of nominated to display in the chart
	 */
	public void setDataChart(List<Nominated> listNominated){
		XYChart.Series<String, Integer> series = new XYChart.Series<>();
		for (Nominated nominated : listNominated) {
			series.getData().add(new XYChart.Data<>(nominated.getName(),nominated.getNumberOfVote()));
		}
		ChartVotes.getData().clear();
		ChartVotes.getData().add(series);
		ChartVotes.setAnimated(false);
	}
	
	/**
	 * Method to refresh the GUI data (logs,chart...)
	 */
	public void refresh(){
		if(this.serv != null){
			LblHighestVote.setText(this.serv.votingRound.getNameOfHighestVote());
			TxtAreaLogs.appendText(this.serv.votingRound.getLogs());
			this.setDataChart(this.serv.votingRound.listNominated);
		}
	}

}
