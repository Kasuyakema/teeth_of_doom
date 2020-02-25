package com.kumascave.games.firstgame;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Disposable;

public class ConfigManager implements Disposable {
	private Preferences prefs;

	public ConfigManager() {
		prefs = Gdx.app.getPreferences("user");
	}

	public String getString(ConfigKey conf) {
		return prefs.getString(conf.getPrefKey(), conf.getDefaultVal().toString());
	}

	public boolean getBoolean(ConfigKey conf) {
		return prefs.getBoolean(conf.getPrefKey(), (Boolean) conf.getDefaultVal());
	}

	public float getFloat(ConfigKey conf) {
		return prefs.getFloat(conf.getPrefKey(), (Float) conf.getDefaultVal());
	}

	public int getInteger(ConfigKey conf) {
		return prefs.getInteger(conf.getPrefKey(), (Integer) conf.getDefaultVal());
	}

	public Map<ConfigKey, Integer> getMatchingIntegers(Pattern pattern) {
		Map<ConfigKey, Integer> result = new HashMap<>();
		Arrays.stream(ConfigKey.values()).filter(x -> pattern.matcher(x.name()).matches())
				.forEach(x -> result.put(x, getInteger(x)));
		return result;
	}

	@Override
	public void dispose() {
		prefs = null;
	}

}
