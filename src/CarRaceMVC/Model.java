package CarRaceMVC;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.paint.Color;

public class Model {
	private int raceCounter;
	private static int carCounter;
	private Connection connection;
	private boolean isRunningRace = false;
	private Map<Integer,Timer> timers;

	public Model() {
		initializeDB();
		initializeCounters();
		timers = new HashMap<Integer,Timer>();
	}

	public int getRaceCounter() {
		return raceCounter;
	}
	
	public void newRace()
	{
		raceCounter++;
		Random r = new Random();
		try {
			Statement s = connection.createStatement();
			s.execute("INSERT INTO RACE (raceID, startTime, songID) VALUES (" + String.format("%d,NULL,%d", raceCounter,r.nextInt(3))+")");
			for (int i=0 ; i < 5 ; ++i)
				s.execute("INSERT INTO CARS (carID,color,speed,model,size,manufacture,raceID) VALUES (" +
			String.format("%d,%d,%d,%d,%d,%d,%d",++carCounter,r.nextInt(3), (r.nextInt(150) + 50) ,r.nextInt(2), r.nextInt(2), r.nextInt(3), raceCounter)+")");
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void endRace(int raceNum, int winnerCar)
	{
		timers.get(raceNum).cancel();
		try {
			Statement s = connection.createStatement();
			ResultSet rs = s.executeQuery("SELECT userName,bet,sum(bet) FROM BETS WHERE carID = " + winnerCar);
			while(rs.next())
			{
				String userName = rs.getString("userName");
				int bet = rs.getInt("bet");
				int total = rs.getInt("sum(bet)");
				s.execute("INSERT INTO WINNINGS VALUES('" + userName + "', " + (bet * (1 + (bet/total))) + ", " + raceNum +")");
			}
			s.execute("UPDATE CARS SET isWinner = true WHERE carID = " + winnerCar);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isRunning()
	{
		return isRunningRace;
	}
	
	public ResultSet getAllData()
	{
		String query ="SELECT raceID FROM RACE WHERE startTime IS NULL";
		try {
		Statement s =  connection.createStatement();
		return  s.executeQuery(query);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public ResultSet getRaceData(int racenum)
	{
		System.out.println("In getRaceData :: racenum = " + racenum);
		String query ="SELECT r.*, c.* FROM RACE r JOIN CARS c ON c.raceID = r.raceID where r.raceID = " + racenum;
		try {
		Statement s =  connection.createStatement();
		return  s.executeQuery(query);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private void initializeDB() {
		try { // Load the JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			// Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("Driver loaded");
			// Establish a connection
			connection = DriverManager.getConnection("jdbc:mysql://localhost/car_race", "scott", "tiger");
			System.out.println("Database connected");

			

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void initializeCounters() {
		try {
			Statement s = connection.createStatement();
			ResultSet rs = s.executeQuery("SELECT MAX(raceID) FROM RACE");
			if(!rs.next())
				raceCounter = 0;
			else
				raceCounter = rs.getInt(1);
			rs = s.executeQuery("SELECT MAX(carID) FROM CARS");
			if(!rs.next())
				carCounter = 0;
			else
				carCounter = rs.getInt(1);
		} catch (SQLException e) {
			raceCounter = 0;
			carCounter = 0;
			e.printStackTrace();
		}
	}
	
	public boolean login(String user, String pass) {
		try{
		Statement s = connection.createStatement();
		ResultSet rs = s.executeQuery("SELECT * FROM USER WHERE userName='" + user + "' AND password='"+pass+"'");
		return rs.next();
		}catch(SQLException e ){
			e.printStackTrace();
			return false;
		}
	
	}
	
	public boolean signUp(String user, String pass) {
		try {
			connection.createStatement().execute("INSERT INTO USER (userName, password, balance) VALUES (" + String.format("'%s','%s',%d",user,pass,1000) + ")");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean bet(String userName, String betAmount, int carID) {
		Statement s;
		int commition = (int)(Integer.parseInt(betAmount)*0.05);
		try {
			s = connection.createStatement();
			ResultSet rs = s.executeQuery("SELECT balance FROM USER WHERE userName='" + userName + "'");
			if(!rs.next())
				return false;
			int newAmount = 0;
			try{
				newAmount =  rs.getInt("balance") - Integer.parseInt(betAmount);
			}
			catch(NumberFormatException e){
				return false;
			}
			rs = s.executeQuery("SELECT startTime FROM RACE WHERE raceID = " + (carID/5 + 1));
			rs.next();
			System.out.println(rs.getTime("startTime"));
			if(rs.getTime("startTime") != null)
				return false;
			if(newAmount < 0)
				return false;
			s.execute("UPDATE WINNINGS SET amount = amount - " + betAmount + " WHERE userName = '" + userName + "'");
			betAmount = Integer.toString(Integer.parseInt(betAmount) - commition);
			s.execute("UPDATE COMMITION SET commition = commition + " + commition + " WHERE userName= '" + userName +"'");
			s.execute("UPDATE USER SET balance = " + newAmount + " WHERE userName='" + userName +"'");
			s.execute("INSERT INTO BETS (userName,carID,bet) VALUES (" + String.format("'%s',%d,%s", userName,carID,betAmount) + ")"); 
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	public ResultSet SystemEarnings() {
		try {
			return connection.createStatement().executeQuery("");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	
		
	}

	public ResultSet RaceHistory() {
		try {
			return connection.createStatement().executeQuery("SELECT r.*, c.*, b.userName, b.bet FROM RACE r JOIN CARS c ON r.raceID = c.raceID JOIN BETS b ON b.carID = c.carID");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public boolean isCanStart(String string) {
		Statement s;
		try {
			int i=0;
			s = connection.createStatement();
			int carID = Integer.parseInt(string);
			int raceNum = carID/5 + 1;
			ResultSet rs = s.executeQuery("SELECT * FROM BETS WHERE carID BETWEEN " + ((raceNum-1) * 5 + 1) + " AND " + (raceNum* 5));
			while(rs.next())
				i++;
			System.out.println(i);
			if(i>=3)
			{
				startRace(raceNum);
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

	private void startRace(int raceNum) {
		isRunningRace = true;
		try {
			Timestamp timeStamp =  new java.sql.Timestamp(new java.util.Date().getTime());
			System.out.println("Race " + raceNum +" started at " + timeStamp );
			connection.createStatement().execute("UPDATE RACE SET startTime = '" + timeStamp + "' WHERE raceID = " + raceNum);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Race " + raceNum +" not started" );
		}
		
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run() 
			{
				updateSpeed(raceNum);
			}
			
		}, 0, 30000);
		timers.put(raceNum, t);
	}

	protected void updateSpeed(int raceNum) {
		Random R = new Random();
		try {
			Statement s = connection.createStatement();
			for(int i = raceNum*5; i < raceNum*5+5; i++)
			{
				s.execute("UPDATE CARS SET speed = " + R.nextInt(150)+50 + " WHERE carID = " + i);
				System.out.println("Race " + raceNum + " has Updated speeds on server");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public ResultSet betHistory() {
		try {
			return connection.createStatement().executeQuery("SELECT b.*, w.* FROM BETS b JOIN WINNINGS w ON b.userName = w.userName");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public ResultSet houseWinnings() {
		try {
			return connection.createStatement().executeQuery("SELECT * FROM WINNINGS ORDER BY amount DESC");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public ResultSet raceStatus() {
		try {
			return connection.createStatement().executeQuery("SELECT r.raceID,b.* FROM RACE r,BETS b,CARS c WHERE b.carID = c.carID AND r.raceID = c.raceID");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public ResultSet gamblerWinnings() {
		try {
			return connection.createStatement().executeQuery("SELECT userName,sum(amount) as total FROM WINNINGS GROUP BY userName ORDER BY total DESC");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}




/*
sql init MYSQL

DROP DATABASE car_race;
CREATE DATABASE car_race;
USE car_race;
CREATE TABLE RACE (raceID int PRIMARY KEY, startTime DATETIME, songID INT NOT NULL );
CREATE TABLE CARS (carID INT PRIMARY KEY, color INT NOT NULL, speed INT , model INT NOT NULL, size INT NOT NULL, manufacture INT NOT NULL, raceID int, isWinner BOOLEAN DEFAULT 0);
ALTER TABLE CARS
ADD FOREIGN KEY (raceID) REFERENCES RACE (raceID) ;
CREATE TABLE USER (userName NVARCHAR(100) PRIMARY KEY, password NVARCHAR(100) NOT NULL, balance INT DEFAULT 1000  );
CREATE TABLE BETS (userName NVARCHAR(100), carID INT , bet INT NOT NULL );
ALTER TABLE BETS
ADD PRIMARY KEY (userName, carID);
ALTER TABLE BETS 
ADD FOREIGN KEY (userName) REFERENCES USER(userName);
ALTER TABLE BETS
ADD FOREIGN KEY (carID) REFERENCES CARS(carID);
CREATE TABLE COMMITION (userName NVARCHAR(100) PRIMARY KEY, commition INT DEFAULT 0);
ALTER TABLE COMMITION
ADD FOREIGN KEY (userName) REFERENCES USER(userName); 
CREATE TABLE WINNINGS (userName NVARCHAR(100), amount INT DEFAULT 0, raceID INT);
ALTER TABLE WINNINGS
ADD FOREIGN KEY (userName) REFERENCES USER(userName);
ALTER TABLE WINNINGS
ADD FOREIGN KEY (raceID) REFERENCES RACE(raceID);
ALTER TABLE WINNINGS
ADD PRIMARY KEY (userName,raceID);
*/