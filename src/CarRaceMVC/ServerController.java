package CarRaceMVC;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.stage.Stage;

public class ServerController {

	private HashMap<Socket, DataOutputStream> outputStreams = new HashMap<Socket, DataOutputStream>();
	private ServerSocket serverSocket;
	private ServerView serverView;
	private Model model;
	private static int raceNum;
	private String data;

	public ServerController(Stage stage) {
		raceNum = 0;
		model = new Model();
		newRace(); // Should be 3 eventually
		serverView = new ServerView(stage);
		new Thread(() -> listen()).start();
	}

	private void listen() {
		try {
			serverSocket = new ServerSocket(8000);
			Platform.runLater(() -> serverView
					.appendText("Server Started"));

			while (true) {
				Socket socket = serverSocket.accept();
				outputStreams.put(socket,
						new DataOutputStream(socket.getOutputStream()));
				Platform.runLater(() -> serverView
						.appendText("New socket connected: " + socket + '\n'));
				new ServerThread(this, socket);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	void sendToAll(String message) {
		for (DataOutputStream dout : outputStreams.values()) {
			try {
				dout.writeUTF(message);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	void newRace() {
		model.newRace();
		// TODO:: Insert to DB
	}

	// ------------------------------------------------------------------------------------//
	class ServerThread extends Thread {
		private ServerController server;
		private Socket socket;

		public ServerThread(ServerController server, Socket socket) {
			this.socket = socket;
			this.server = server;
			start();
		}

		public void run() {
			DataInputStream din = null ;
			try {
				din = new DataInputStream(socket.getInputStream());
			} catch (IOException ex) {
				ex.printStackTrace();
				System.out.println("INVALID SOCKET INPUT STREAM");
			}
			String data ="";
			while (true) {
				try {
					data = din.readUTF();
				} catch (IOException e1) {
					e1.printStackTrace();
					break;
				}
				
				String[] datatemp = data.split(",");
				
				
				switch (Integer.parseInt(datatemp[0])){
					case Defines.GET_RACE_DATA :
						parseRaceData(datatemp);
						break;
					case Defines.SIGNIN :
						signIn(datatemp);
						break;
					case Defines.SIGNUP:
						signUp(datatemp);
						break;
					case Defines.BET:
						bet(datatemp);
						break;
					case Defines.GET_ALL_RACES_DATA:
						getAllRaceData(datatemp);
						break;
				}
			}
		
		}

		private void getAllRaceData(String[] datatemp) {
			ResultSet rs = model.getAllData();
			for(int i=0; i<3; i++){
				try {
					if(!rs.next())
						model.newRace();
					else{
						String[] temp = {"",String.valueOf(rs.getInt("raceID"))};
						parseRaceData(temp);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		private void bet(String[] datatemp) {
			boolean isValidBet = model.bet(datatemp[1],datatemp[2],Integer.parseInt(datatemp[3]));
			try {
				outputStreams.get(socket).flush();
				outputStreams.get(socket).writeUTF(Defines.BET + "," + isValidBet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void signUp(String[] datatemp) {
			boolean isValidSignup = model.signUp(datatemp[1], datatemp[2]);
			try {
				outputStreams.get(socket).flush();
				outputStreams.get(socket).writeUTF(Defines.SIGNUP + "," + isValidSignup);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/**
		 * @param datatemp
		 */
		private void signIn(String[] datatemp) {
			boolean isLogin = model.login(datatemp[1], datatemp[2]);
			try {
				outputStreams.get(socket).writeUTF(
						Defines.SIGNIN + "," + isLogin);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/**
		 * @param datatemp
		 */
		private void parseRaceData(String[] datatemp) {
			String data;
			int raceNum = Integer.parseInt(datatemp[1]);
			ResultSet rs = model.getRaceData(raceNum);
			data = Defines.GET_RACE_DATA + "";
			try {
				if (!rs.next())
					return;
				data += rs.getInt(1) + ",";
				if (rs.getTime(2) != null)
					data += rs.getTime(2).toString();
				else
					data += "0";
				data += "," + rs.getInt(3) + ",";
				do {
					data += rs.getInt(4) + "," + rs.getInt(5) + ","
							+ rs.getInt(6) + "," + rs.getInt(7) + ","
							+ rs.getInt(8) + "," + rs.getInt(9) + ",";
				} while (rs.next());
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("INVALID RESULT SET IN " + this.getName());
			}
			data = data.substring(0, data.length() - 1);

			try {
				outputStreams.get(socket).writeUTF(data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	// ------------------------------------------------------------------------------------//
}
