package CarRaceMVC;


import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ServerView {
	
	private TableView srvTV;
	private	ComboBox<String> srvCB;
	private ObservableList list;
	private ServerController controller;
	public ServerView(Stage stage, ServerController controller)
	{
		this.controller = controller;
		srvTV = new TableView();
		srvCB = new ComboBox<String>();
		srvCB.getItems().addAll("Race history","System earnings","");
		srvCB.valueProperty().addListener(e ->{
			String option = srvCB.getValue();
					switch(option){
					case "Race history":
						controller.getRaceHistory(srvTV);
						break;
					case "System earnings":
						controller.SystemEarnings(srvTV);
						break;
			}
		});
		
		Scene scene = new Scene(new VBox(srvCB,srvTV), 600, 250);
		stage.setTitle("RaceServer");
		stage.setScene(scene);
		stage.show();
	}

}
