package CarRaceMVC;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import sun.security.jca.GetInstance;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ClientController {
	private Socket socket;
	private DataOutputStream dataOutput;
	private DataInputStream dataInput;
	private Thread serverThread;
	private ArrayList<View> windows;
	private static boolean waitForResponseLogin;
	private static boolean waitForResponseSignUp;
	private static boolean waitForResponseBet;
	private boolean login = false;
	private boolean signUp = false;
	private boolean bet = false;
	private String userName;

	public ClientController(Stage stg) {

		try {
			socket = new Socket("localhost", 8000);
			dataInput = new DataInputStream(socket.getInputStream());
			dataOutput = new DataOutputStream(socket.getOutputStream());
			serverThread = new Thread(() -> run());
			serverThread.start();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		windows = new ArrayList<View>();
		start();
	}

	public void start() {
		System.out.println("START AT CLIENT");
		createNewWindow("Login");
	}

	public void run() {
		// start

		while (true) {
			try {
				String data = dataInput.readUTF();
				parseData(data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}
	}

	private void parseData(String data) {
		if ((data == null) || (data.isEmpty())) {
			System.out.println("Empty read is null");
			return;
		}
		String[] args = data.split(",");

		switch (Integer.parseInt(args[0])) // args in the 0 place will send the
											// message type
		{
		case Defines.SIGNIN:
			setLogin(Boolean.parseBoolean(args[1]));
			break;
		case Defines.SIGNUP:
			setSignUp(Boolean.parseBoolean(args[1]));
			break;
		case Defines.BET:
			setBet(Boolean.parseBoolean(args[1]));
			break;
		case Defines.GET_RACE_DATA:
			readData(args);
			break;
		}
		// TODO This function will parse the message from the server and execute
		// the right actions
	}

	private void readData(String[] data) {
		Platform.runLater(() -> {
			createNewWindow("Race");
		});
	}

	private void setBet(boolean parseBoolean) {
		bet = parseBoolean;
		waitForResponseBet = false;
		
	}

	private void setSignUp(boolean parseBoolean) {
		signUp = parseBoolean;
		waitForResponseSignUp = false;
	}

	private void setLogin(boolean parseBoolean) {
		login = parseBoolean;
		waitForResponseLogin = false;
	}

	private void bet(String bet, String carName,View race) {
		try {
			dataOutput.writeUTF(Defines.BET + "," + userName + "," + bet + "," + carName);
		} catch (IOException ex) {
			System.err.println(ex);
		}
		
		waitForResponseBet = true;
		while (waitForResponseBet) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if(this.bet)
			MessageAlert("Bet placed successfuly","Good bet",race);
		else
			MessageAlert("Bet cannot be placed","Bad bet",race);
	}

	public void MessageAlert(String msg, String title, View parent) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(parent);
		alert.setTitle(title);
		alert.setContentText(msg);
		alert.setOnCloseRequest(e -> {
			alert.close();
		});
		alert.show();
	}

	public void setOwnerStage(Stage stg) {
	}

	public void login(String user, String pass) {
		try {
			dataOutput.writeUTF(Defines.SIGNIN + "," + user + "," + pass);
		} catch (IOException e) {
			e.printStackTrace();
		}

		waitForResponseLogin = true;
		while (waitForResponseLogin) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (login) {
			windows.get(0).close();
			userName = user;
			openRaceWindows();
		} else
			MessageAlert("Invalid Username or Password, please try again",
					"Login Error", windows.get(0));
	}

	private void openRaceWindows() {
		try {
			dataOutput.writeUTF(Defines.GET_ALL_RACES_DATA + "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	/**
	 *This function sends the username and password to the server to be checked against the DB, waits for a response and Logs in if the information is correct
	 *or shows an error message if something went wrong
	 * @param user - username
	 * @param pass - password
	 * 
	 */
	public void signUp(String user, String pass) {
		try {
			dataOutput.writeUTF(Defines.SIGNUP + "," + user + "," + pass);
		} catch (IOException e) {
			e.printStackTrace();
		}
		waitForResponseSignUp = true;
		while (waitForResponseSignUp)
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		if (signUp)
			MessageAlert("Signed up succsesfully ! Way to go man!", "SignUp",
					windows.get(0));
		else
			MessageAlert("Username already used, please try again",
					"SignUp Error", windows.get(0));
	}

	public void createNewWindow(String type) {
		switch (type.toLowerCase()) {
		case "login":
			windows.add(new LoginView(this));
			break;
		case "race":
			windows.add(new RaceView(this));
		}

		windows.get(windows.size() - 1).show();

	}

	public void removeWindow(View view) {
		windows.remove(view);
	}
}
