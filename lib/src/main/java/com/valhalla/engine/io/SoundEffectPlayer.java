package com.valhalla.engine.io;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.valhalla.openal.util.ALException;
import org.valhalla.openal.intermediate.Source;

import com.valhalla.engine.GameLoop;
import com.valhalla.engine.internal.Internal;

/**
 * Class used for playing back short sound effects
 *
 * @author BauwenDR
 */
public class SoundEffectPlayer {

    private static float _soundEffectVolume = 1;

    private static HashMap<String, Source> _soundEffectList;

    @Internal
    public SoundEffectPlayer() {
        _soundEffectList = new HashMap<>();
    }

    /**
     * Loads in a sound effect with a specified name used for playing it back.<br>
     * <u>Note:</u> A sound effect only has to be loaded in once and can be played back an unlimited amount of times.
     *
     * @param soundEffectName <b>(String)</b> soundEffectName
     * @param soundEffectPath <b>(String)</b> soundEffectPath
     * @see #playSoundEffect(String)
     */
    public static void addSoundEffect(String soundEffectName, String soundEffectPath) {
        try {
            File music = new File(soundEffectPath);
            Source soundEffect = SoundInterface._openAl.createSource(music);
            _soundEffectList.put(soundEffectName, soundEffect);
        } catch (ALException | IOException | UnsupportedAudioFileException e) {
            GameLoop.engineOutput.println("BaseEngine error: " + e.getMessage());
        }
    }

    /**
     * Gets the OpenAL Source behind a sound effect, this provides the freedom to fully configure a sound effect.
     *
     * @param soundEffectName <b>(String)</b> soundEffectName
     * @return The Source associated with a sound effect or NULL when the sound effect was not loaded
     */
    public static Source getSoundEffect(String soundEffectName) {
        return _soundEffectList.get(soundEffectName);
    }

    /**
     * Plays a sound effect that has been loaded (sound effects can be played more than once).<br>
     * <u>Note:</u> If this function throws a null-pointer exception, the soundeffect was most likely not loaded.
     *
     * @param soundEffectName <b>(String)</b> soundEffectName
     */
    public static void playSoundEffect(String soundEffectName) {
        try {
            Source soundEffect = _soundEffectList.get(soundEffectName);
            soundEffect.setGain(_soundEffectVolume);
            soundEffect.play();
        } catch (Exception e) {
            GameLoop.engineOutput.println("BaseEngine error: " + e.getMessage());
        }
    }

    /**
     * Removes a sound effect from memory.
     *
     * @param soundEffectName <b>(String)</b> soundEffectName
     */
    public static void removeSoundEffect(String soundEffectName) {
        try {
            Source soundEffect = _soundEffectList.get(soundEffectName);
            _soundEffectList.remove(soundEffectName);
            soundEffect.close();
        } catch (Exception e) {
            GameLoop.engineOutput.println("BaseEngine error: " + e.getMessage());
        }
    }

    /**
     * Changes volume of all sound effects.
     *
     * @param newVolume <b>(Integer)</b> volume for audio (from 0 to 100)
     */
    public static void setVolume(int newVolume) {
        _soundEffectVolume = (float) newVolume / 100;    //rescale from [0; 100] to [0; 1]
    }

    /**
     * Getter for the current volume of sound effects
     *
     * @return musicVolume (Integer)
     */
    public static int getVolume() {
        return (int) (_soundEffectVolume * 100);
    }
}