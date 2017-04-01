package CarRaceMVC;

import java.util.HashMap;
import java.util.Map;

import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.prism.paint.Color;

public interface Defines
{ 		
	enum eventType			{RADIUS, COLOR, SPEED}
	enum size				{MINI, REGULAR, LARGE}
	enum carType			{SALOON, SPORT, JEEP}
	enum manufacture		{JAGUAR, NISSAN, MERC, SUSITA}
	
	
	
	public static final int SIGNUP 				= 0 ;
	public static final int SIGNIN 				= 1 ;
	public static final int GET_RACE_DATA 		= 2 ;
	public static final int GET_ALL_RACES_DATA 	= 3 ;
	public static final int BET 				= 4 ;
	public static final int UPDATE				= 5 ;
	public static final int END_RACE			= 6 ;
}
