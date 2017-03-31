package CarRaceMVC;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public abstract class View extends Stage {
	/*
										 * 
										 * 
										 * private ArrayList<HBox> race;
										 */
	protected ClientController cont;
	protected Scene scene;

	protected Parent pane;
	protected int W;
	protected int H;

	public View(ClientController cont) {
		this.cont = cont;
		this.setResizable(false);
	}

	public Parent getPane() {
		return pane;
	}

	public int getW() {
		return W;
	}

	public int getH() {
		return H;
	}

	public void setH(int h) {
		this.H = h;
	}

	public void setW(int w) {
		this.W = w;
	}

	public void open() {
		scene = new Scene(pane, W, H);
		this.setScene(scene);
	}

	public void close() {
		super.close();
		cont.removeWindow(this);
	}
}