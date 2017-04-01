package CarRaceMVC;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
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
		setW(400);
		setH(100);
		
		
		this.setTitle(title);
		
		this.pane = new HBox();
		((HBox)pane).setAlignment(Pos.CENTER);
		
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
		
		
		this.setOnCloseRequest((e)->{
			this.hide();
		});
		ObservableList<String> data = FXCollections.observableArrayList();
		data.addAll(carNames);
		
		cb.setItems( data );
		
		((HBox)pane).getChildren().addAll(cb, tf, btn);
	}

}
