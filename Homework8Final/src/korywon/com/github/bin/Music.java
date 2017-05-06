package korywon.com.github.bin;

import java.io.*;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * Controls music and sound engine of the game.
 * Constructor takes in file name of .wav file.
 * @author Alvin Huynh (C) 2017
 *
 */

public class Music {
	InputStream inputStream;
	Clip clip;
	FloatControl volume;
	
	public Music(String fileName) {
		try {
			inputStream = getClass().getResourceAsStream("/korywon/com/github/music/" + fileName);
			clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(inputStream));
			volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	public void playSong() {
		if (clip.isOpen()) {
			clip.loop(clip.LOOP_CONTINUOUSLY);
			clip.start();
		}
	}
	
	public void playSong(long start) {
		if (clip.isOpen()) {
			clip.loop(clip.LOOP_CONTINUOUSLY);
			clip.setMicrosecondPosition((long) start);
			clip.start();
		}
		
	}
	
	public void stop() {
		if (clip.isOpen() && clip.isRunning()) {
			clip.stop();
		}
	}
	
	public void close() {
		if (clip.isOpen()) {
			if (clip.isRunning()) {
				clip.stop();
			}
			clip.close();
		}
	}
	
	public float getVolume() {
		return (float) Math.pow(10f, volume.getValue() / 20f);
	}
	
	public void setVolume(float vol) {
		volume.setValue(20f * (float) Math.log10(vol));
	}
	
	
	
}
