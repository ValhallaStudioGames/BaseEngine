package com.valhalla.engine.io;

import org.valhalla.openal.ALException;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Internal //closes openAL service
	public static void cleanUp() {
		_openAl.close();
	}
}
