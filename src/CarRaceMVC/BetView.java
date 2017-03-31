package CarRaceMVC;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class BetView extends View 
{

	private ComboBox<String> cb;
	private TextField tf;
	private Button btn;
	
	public BetView(ClientController cont, String[] carNames, String title) {
		super(cont);
		
		setW(600);
		setH(200);
		
		this.setTitle(title);
		
		this.pane = new HBox();
		
		cb = new ComboBox<>();
		tf= new TextField("Bet Amount");
		btn = new Button("Bet!!!");
		
		ObservableList<String> data = FXCollections.observableArrayList();
		data.addAll(carNames);
		
		cb.setItems( data );
		
		((HBox)pane).getChildren().addAll(cb, tf, btn);
	}

}
