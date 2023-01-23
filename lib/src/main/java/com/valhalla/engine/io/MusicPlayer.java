package com.valhalla.engine.io;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.valhalla.openal.ALException;
import org.valhalla.openal.Source;
import org.valhalla.openal.SourceState;

import com.valhalla.engine.GameLoop;

/**
 * Class used for playing back music files.
 * @author BauwenDR
 */
public class MusicPlayer {

	private static float _musicVolume = 1;

	private static final LinkedList<Source> _musicList = new LinkedList<>();
	
	/**
	 * Adds an audio clip given as a file path to the Internal playlist.<br>
	 * <u>Note:</u> In order to play the music call {@link #playMusic} with the returned number.
	 * @param Filepath <b>(String)</b> the location for the music file.
	 * @return (Integer) The location of the music clip in the playlist.
	 * @throws IOException if there was an error reading a file
	 * @throws ALException if there is an error with the sound interface
	 * @throws UnsupportedAudioFileException if the specified file is in an unknown format
	 * @see #playMusic
	 */
	public static int addMusic(String Filepath) throws IOException, ALException, UnsupportedAudioFileException {
		File music = new File(Filepath);
		Source source = SoundInterface._openAl.createSource(music);
		_musicList.add(source);
		return _musicList.size()-1;
	}
	
	/**
	 * Plays the audio clip at a certain point in the playlist, with the option to loop it.
	 * @param clipNumber <b>(Integer)</b> Number of the clip in the playlist.
	 * @param repeat <b>(Boolean)</b> Play the clip continuously until the clip is stopped.
	 * @see #addMusic
	 * @see #stopMusic
	 */
	public static void playMusic(int clipNumber, boolean repeat) {
		try {
			_musicList.get(clipNumber).setGain(_musicVolume);
			_musicList.get(clipNumber).play();
			_musicList.get(clipNumber).setLooping(repeat);
		} catch (ALException e) {
			GameLoop.engineOutput.println("BaseEngine Error: Unable to play sound song: " + clipNumber);
			e.printStackTrace();
		}
	}
	
	/**
	 * Stops a music clip at a certain position in the playlist.<br>
	 * <u>Note:</u> In order to clear the playlist use {@link #clearMusic}.
	 * @param clipNumber <b>(Integer)</b> Number of the clip in the playlist.
	 * @see #playMusic
	 * @see #clearMusic
	 */
	public static void stopMusic(int clipNumber) {
		try {
			_musicList.get(clipNumber).stop();
		} catch (ALException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Function the can check for any music clip to see if it has stopped playing or not<br>
	 * <u>Note:</u> This function should only be used on clips that don't loop.
	 * @param clipNumber <b>(Integer)</b> Number of the clip in the playlist.
	 * @return hasStoppedPlaying (boolean)
	 * @throws ALException if there is an error with the sound interface
	 */
	public static boolean hasClipFinished(int clipNumber) throws ALException {
		return _musicList.get(clipNumber).getSourceState() == SourceState.STOPPED;
	}
	
	/**
	 * Stops and clears all audio clips in the internal playlist.<br>
	 * <u>Note:</u> It is recommended to clear the playlist everytime you change GameStates and to only add an audio clip once.
	 */
	public static void clearMusic() {
		for(int i = 0; i < _musicList.size(); i++) {
			stopMusic(i);
			_musicList.get(i).close();
		}
		_musicList.clear();
	}

	/**
	 * Changes volume of music tracks
	 * @param newVolume <b>(Integer)</b> volume for audio (from 0 to 100)
	 */
	public static void setVolume(int newVolume) {
		_musicVolume = (float) newVolume/100;	//rescale from [0; 100] to [0; 10]
		for(Source song : _musicList) {
			try {
				song.setGain(_musicVolume);
			} catch (ALException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Getter for the current volume of music
	 * @return musicVolume (Integer)
	 */
	public static int getVolume() {
		return (int) (_musicVolume*100);
	}
}
