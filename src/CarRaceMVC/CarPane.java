package CarRaceMVC;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

public class CarPane extends Pane  {
	
	final int MOVE = 1;
	final int STOP = 0;
	private double xCoor;
	private double yCoor;
	private Timeline tl; // speed=setRate()
	private Color color;
	private int r;// radius
	private Car car;

	public CarPane() {
		xCoor = 0;
		r = 5;
	}

	public void setCarModel(Car myCar) {
		car = myCar;

	}

	public Car getCarModel() {
		return car;
	}

	public void moveCar(int n) {
		yCoor = getHeight();
		setMinSize(10 * r, 6 * r);
		if (xCoor > getWidth()) {
			xCoor = -10 * r;
		} else {
			xCoor += n;
		}
		// Draw the car
		Polygon polygon = new Polygon(xCoor, yCoor - r, xCoor, yCoor - 4 * r,
				xCoor + 2 * r, yCoor - 4 * r, xCoor + 4 * r, yCoor - 6 * r,
				xCoor + 6 * r, yCoor - 6 * r, xCoor + 8 * r, yCoor - 4 * r,
				xCoor + 10 * r, yCoor - 4 * r, xCoor + 10 * r, yCoor - r);
		polygon.setFill(color);
		polygon.setStroke(Color.BLACK);
		// Draw the wheels
		Circle wheel1 = new Circle(xCoor + r * 3, yCoor - r, r, Color.BLACK);
		Circle wheel2 = new Circle(xCoor + r * 7, yCoor - r, r, Color.BLACK);
		getChildren().clear();
		getChildren().addAll(polygon, wheel1, wheel2);
	}

	public void createTimeline() {
		EventHandler<ActionEvent> eventHandler = e -> {
			moveCar(MOVE); // move car pane according to limits
		};
		tl = new Timeline();
		tl.setCycleCount(Timeline.INDEFINITE);
		KeyFrame kf = new KeyFrame(Duration.millis(50), eventHandler);
		tl.getKeyFrames().add(kf);
		tl.play();
	}

	public Timeline getTimeline() {
		return tl;
	}


	public void setSpeed(double speed) {
		if (speed == STOP) {
			tl.stop();
		} else {
			tl.setRate(speed);
			tl.play();
		}
	}

	public double getX() {
		return xCoor;
	}

	public double getY() {
		return yCoor;
	}
}
