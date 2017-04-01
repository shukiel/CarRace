package CarRaceMVC;

import java.nio.file.Paths;
import java.util.HashMap;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public  class Player
{
	private static HashMap<Integer,MediaPlayer> SONGS;
	static
	{
		SONGS = new HashMap<>();
		SONGS.put(1, new MediaPlayer(new Media(Paths.get("1.mp3").toUri().toString())));
		SONGS.put(2, new MediaPlayer(new Media(Paths.get("2.mp3").toUri().toString())));
		SONGS.put(3, new MediaPlayer(new Media(Paths.get("3.mp3").toUri().toString())));
		SONGS.put(11, new MediaPlayer(new Media(Paths.get("11.mp3").toUri().toString())));
		SONGS.put(12, new MediaPlayer(new Media(Paths.get("12.mp3").toUri().toString())));

	}
	static public void playSound(int i)
	{
		SONGS.get(i).play();
	}
	static public long getSongDur(int i)
	{
		return (long) SONGS.get(i).getCycleDuration().toMillis();
	}

	
	
}
