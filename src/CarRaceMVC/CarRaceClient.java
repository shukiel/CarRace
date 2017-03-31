package CarRaceMVC;

import javafx.application.Application;
import javafx.stage.Stage;

public class CarRaceClient  extends Application{
	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stg) throws Exception {
		new ClientController(stg);		
	}
}