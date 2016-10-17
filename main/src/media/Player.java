package media;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Player 
{

	public static void playSound(Media sound)
	{
		MediaPlayer mediaPlayer = new MediaPlayer(sound);

		/*
		mediaPlayer.setOnPlaying(() -> {

			FillTransition ft = new FillTransition(Duration.millis(seconds * 1000), rect, Color.TRANSPARENT, Color.GREEN);
			ft.setCycleCount(2);
			ft.setAutoReverse(true);

			ft.play();
		});
		*/
		mediaPlayer.play();
	}

	/* does not wok at all
	public static void bell()
	{
		Media bell = new Media("file:resources/bell_high.wav");
		bell.getDuration();
		//Media bell = new Media("jar://bell_high.wav");
		playSound(bell);
	}
	*/
	
	public static void bell()
	{
		/* works, but -
		AudioClip bellSound = new AudioClip("file:resources/bell_high.wav"); // TODO won't run in JAR!
		
		bellSound.play();
		*/
	}

	public static void main(String[] args) {
		bell();
	}

	
}
