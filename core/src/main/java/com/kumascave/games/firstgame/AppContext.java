package com.kumascave.games.firstgame;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Disposable;

import lombok.Getter;
import lombok.Setter;

public class AppContext implements Disposable {
	private static AppContext instance;

	@Getter
	@Setter
	private FirstGame game;
	@Getter
	private AssetManager assetManager;
	@Getter
	private ConfigManager configManager;

	private AppContext() {
		assetManager = new AssetManager();
		configManager = new ConfigManager();
	}

	public static AppContext inst() {
		if (instance == null) {
			instance = new AppContext();
		}
		return instance;
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		assetManager = null;
		configManager.dispose();
		configManager = null;
		game = null;
		instance = null;
	}
}
