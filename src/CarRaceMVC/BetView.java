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
	private int raceNum;
	
	public BetView(ClientController cont, String[] carNames, String title, int raceNum) {
		super(cont);
		
		this.raceNum = raceNum;
		setW(600);
		setH(200);
		
		this.setTitle(title);
		
		this.pane = new HBox();
		
		cb = new ComboBox<>();
		tf= new TextField("Bet Amount");
		btn = new Button("Bet!!!");
		
		btn.setOnAction((e)->{
			String bet = tf.getText();
			if (! bet.matches("\\d+"))
			{
				tf.setText("");
				return;
			}
			cont.bet(bet, Integer.toString(cb.getSelectionModel().getSelectedIndex()),raceNum ,this);
		});
		
		ObservableList<String> data = FXCollections.observableArrayList();
		data.addAll(carNames);
		
		cb.setItems( data );
		
		((HBox)pane).getChildren().addAll(cb, tf, btn);
	}

}
