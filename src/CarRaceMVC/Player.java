package CarRaceMVC;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public  class Player
{
	public static Lock lock;
	private static HashMap<Integer,Media> SONGS;
	static
	{
		lock= new ReentrantLock();
		SONGS = new HashMap<>();
		SONGS.put(1, new Media(Paths.get("1.mp3").toUri().toString()));
		SONGS.put(2, new Media(Paths.get("2.mp3").toUri().toString()));
		SONGS.put(3, new Media(Paths.get("3.mp3").toUri().toString()));
		SONGS.put(11,new Media(Paths.get("11.mp3").toUri().toString()));
		SONGS.put(12,new Media(Paths.get("12.mp3").toUri().toString()));

	}
	static public void playSound(int i)
	{
		MediaPlayer m = new MediaPlayer(SONGS.get(i));
		m.play();
	}
	
	static public void playSoundWithActionOnEnd(int i, Runnable action)
	{
		MediaPlayer m = new MediaPlayer(SONGS.get(i));
		m.play();
		m.setOnEndOfMedia(action);
	}
	static public long getSongDur(int i)
	{
		return (long) SONGS.get(i).getDuration().toMillis();
	}
	
}
