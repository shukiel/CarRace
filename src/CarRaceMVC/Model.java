package CarRaceMVC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;

public class Model {
	private int raceCounter;
	private static int carCounter;
	private Connection connection;

	public Model() {
		initializeDB();
		initializeCounters();
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
			s.execute("INSERT INTO RACE (raceID, startTime, songID) VALUES (" + String.format("%d,NULL,%d", raceCounter,r.nextInt(3)));
			for (int i=0 ; i < 5 ; ++i)
				s.execute("INSERT INTO CARS (carID,color,speed,model,size,manufacture,raceID) VALUES (" + String.format("%d,1,%d,%d,%d,%d,%d",carCounter++,r.nextInt(150) + 50 ,r.nextInt(3), r.nextInt(3), r.nextInt(4)));
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void endRace()
	{
		/*TODO:: send to database*/
	}
	
	public ResultSet getAllData()
	{
		String query ="SELECT r.raceID FROM RACE WHERE startTime IS NULL";
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
				raceCounter = rs.getInt(0);
			rs = s.executeQuery("SELECT MAX(carID) FROM CARS");
			if(!rs.next())
				carCounter = 0;
			else
				carCounter = rs.getInt(0);
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
		try {
			s = connection.createStatement();
			ResultSet rs = s.executeQuery("SELECT balance FROM USER WHERE userName='" + userName + "'");
			if(!rs.next())
				return false;
			int newAmount =  rs.getInt("balance") - Integer.parseInt(betAmount);
			if(newAmount < 0)
				return false;
			rs = s.executeQuery("SELECT carName FROM CARS WHERE carID =" + carID +"");
			if(!rs.next())
				return false;
			s.execute("UPDATE USER SET balance=" + newAmount + "WHERE userName='" + userName +"'");
			s.execute("INSERT INTO BET (userName,carID,bet) VALUES (" + String.format("'%s',%d,%d", userName,carID,betAmount) + ")"); 
		} catch (SQLException e) {
			return false;
		}
		return true;
	}
}




/*
sql init MYSQL

DROP DATABASE car_race;
CREATE DATABASE car_race;
USE car_race;
CREATE TABLE RACE (raceID int PRIMARY KEY, startTime DATETIME, songID INT NOT NULL );
CREATE TABLE CARS (carID INT PRIMARY KEY, color INT NOT NULL, speed INT , model INT NOT NULL, size INT NOT NULL, manufacture INT NOT NULL, raceID int);
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

*/