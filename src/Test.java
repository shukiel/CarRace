import java.util.ArrayList;


import CarRaceMVC.Car;
import CarRaceMVC.Player;
import CarRaceMVC.RaceView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import CarRaceMVC.Defines;
import CarRaceMVC.Defines.carType;
import CarRaceMVC.Defines.manufacture;
import CarRaceMVC.Defines.size;

public class Test extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		ArrayList<Car> carList = new ArrayList<>();
		carList.add( new Car(0, manufacture.JAGUAR.ordinal(), null, Color.BLUE, size.REGULAR.ordinal(), 400, carType.SALOON.ordinal()));
		carList.add( new Car(1, manufacture.MERC.ordinal(), null, Color.RED, size.REGULAR.ordinal(), 190, carType.JEEP.ordinal()));
		carList.add( new Car(2, manufacture.NISSAN.ordinal(), null, Color.GREEN, size.REGULAR.ordinal(), 120, carType.SPORT.ordinal()));
		carList.add( new Car(3, manufacture.SUSITA.ordinal(), null, Color.CYAN, size.REGULAR.ordinal(), 300, carType.JEEP.ordinal()));
		carList.add( new Car(4, manufacture.JAGUAR.ordinal(), null, Color.YELLOW, size.REGULAR.ordinal(), 200, carType.SALOON.ordinal()));
		
		Platform.runLater(()->{RaceView r = new RaceView(null, "Test",3, carList,0);
								r.open();});
	}
	
	public static void main(String[] args){
		launch(args);
	}
}