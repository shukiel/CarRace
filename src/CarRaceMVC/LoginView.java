package CarRaceMVC;


import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView extends View
{
	public LoginView(ClientController cont)
	{
		super(cont); 
		start();
	}

	public void start() {

		W = 650;
		H = 100;
		
		pane = new VBox();
		((VBox)pane).setAlignment(Pos.CENTER);
		((VBox)pane).setSpacing(20);
		HBox fields = new HBox();
		HBox buttons = new HBox();
		
		fields.setAlignment(Pos.CENTER);
		buttons.setAlignment(Pos.CENTER);
		
		fields.setSpacing(30);
		buttons.setSpacing(100);
		
		Label name = new Label("UserName: ");
		TextField userPrompt = new TextField();
		Label pass = new Label("Password: ");
		PasswordField passPrompt = new PasswordField();
		
		fields.getChildren().add(name);
		fields.getChildren().add(userPrompt);
		fields.getChildren().add(pass);
		fields.getChildren().add(passPrompt);
		
		Button login = new Button("LOGIN");
		Button signup = new Button("SignUp!");
		
		buttons.getChildren().addAll(login, signup);
		
		
		login.setOnAction(e-> {
			if(passPrompt.getText().isEmpty())
				cont.MessageAlert("Please enter a password", "Login Error", this);
			else
				cont.login(userPrompt.getText(), passPrompt.getText());
		});
		
		signup.setOnAction(e-> {
			if(passPrompt.getText().isEmpty())
				cont.MessageAlert("Please enter a password", "Signup Error", this);
			else
				cont.signUp(userPrompt.getText(), passPrompt.getText());
		});
		
		this.setTitle("Login Window !!!! Mother fucjer!!!");
		
		((Pane)pane).getChildren().addAll(fields,buttons);
		
		this.open();
		
	}

	
}
