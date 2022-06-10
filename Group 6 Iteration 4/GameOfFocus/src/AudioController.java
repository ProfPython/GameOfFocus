/**
 * Handles the audio files and the audio functionality used in the game.
 */

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioController implements java.io.Serializable {

	// define storage for start position
	private Long nowFrame;
	private transient Clip clip;

	// get the clip status
	private String thestatus;

	private transient AudioInputStream audioStream;

	private String[] fxNames = new String[4];

	static String thePath;

	public AudioController(String filepath) throws UnsupportedAudioFileException, LineUnavailableException {

		fxNames[0] = "click.wav";
		fxNames[1] = "confirm.wav";
		fxNames[2] = "chimes.wav";
		fxNames[3] = "womp.wav";

		File audio = new File(filepath).getAbsoluteFile();

		try {
			audioStream = AudioSystem.getAudioInputStream(audio);
		} catch (UnsupportedAudioFileException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// the reference to the clip
		clip = AudioSystem.getClip();

		try {
			clip.open(audioStream);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (LineUnavailableException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void startMusic() {
		// TODO Auto-generated method stub

		clip.start();

	}

	public void playSoundEffect(int index) throws IOException, LineUnavailableException {

		Path file = Files.createTempFile(null, ".txt");
		String filepath = file.toString();
		try (InputStream stream = this.getClass().getResourceAsStream(fxNames[index])) {
			Files.copy(stream, file, StandardCopyOption.REPLACE_EXISTING);
			File soundEffect = new File(filepath).getAbsoluteFile();
			AudioInputStream sfStream = null;
			try {
				sfStream = AudioSystem.getAudioInputStream(soundEffect);
			} catch (UnsupportedAudioFileException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// the reference to the clip
			Clip fx = AudioSystem.getClip();

			try {
				fx.open(sfStream);
			} catch (LineUnavailableException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			fx.start();

		}

	}

	public void stopMusic() {
		clip.stop();
	}

}