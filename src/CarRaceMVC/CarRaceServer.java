package CarRaceMVC;

import javafx.application.Application;
import javafx.stage.Stage;

public class CarRaceServer extends Application{
	static ServerController serv;
	public static void main (String args[])
	{
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		serv = new ServerController(stage);
	}
}
