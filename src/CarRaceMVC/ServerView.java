package CarRaceMVC;


import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ServerView {
	
	private TextArea srvTA;
	public ServerView(Stage stage)
	{
		srvTA = new TextArea();
		srvTA.setEditable(false);
		srvTA.setWrapText(true);
		Scene scene = new Scene(new ScrollPane(srvTA), 600, 250);
		stage.setTitle("ChatServer");
		stage.setScene(scene);
		stage.show();
	}
	
	public void appendText(String str)
	{
		srvTA.appendText(str);
	}

}
