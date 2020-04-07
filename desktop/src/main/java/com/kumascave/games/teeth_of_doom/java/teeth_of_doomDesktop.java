package com.kumascave.games.teeth_of_doom.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kumascave.games.teeth_of_doom.FirstGame;
import com.kumascave.games.teeth_of_doom.core.Constants;

public class teeth_of_doomDesktop {
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());
		config.fullscreen = false;
		config.resizable = true;
		config.forceExit = true;
		config.foregroundFPS = Constants.TARGET_FPS;
		config.backgroundFPS = Constants.TARGET_FPS;
		config.vSyncEnabled = false;
		config.samples = 3;
		new LwjglApplication(new FirstGame(), config);
	}
}
