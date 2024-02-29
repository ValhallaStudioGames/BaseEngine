package com.valhalla.engine.io;

import com.valhalla.engine.GameLoop;
import org.valhalla.openal.util.ALException;
import org.valhalla.openal.OpenAL;

import com.valhalla.engine.internal.Internal;

@Internal
public class SoundInterface {
	
	static OpenAL _openAl;
	
	@Internal
	public SoundInterface() {
		try {
			_openAl = new OpenAL();
		} catch (ALException e) {
			GameLoop.engineOutput.println("Error setting up the internal sound system: " + e.getMessage());
		}
	}
	
	@Internal
	public static void cleanUp() {
		_openAl.close();
	}
}
