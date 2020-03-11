package com.kumascave.games.teeth_of_doom.core.ui.input;

import java.util.HashMap;
import java.util.Map;

import com.kumascave.games.teeth_of_doom.AppContext;
import com.kumascave.games.teeth_of_doom.ConfigKey;

import lombok.Getter;

public class Keybindings {
	private static Keybindings instance;

	@Getter
	private Map<Integer, ConfigKey> bindings;

	private Keybindings() {
		buildBindings();
	}

	public void update() {
		buildBindings();
	}

	private void buildBindings() {
		this.bindings = new HashMap<>();
		// load bindings from Config and inverse the map
		AppContext.inst().getConfigManager().getMatchingIntegers(ConfigKey.KEYBIND_ALL).entrySet().stream()
				.forEach(x -> bindings.put(x.getValue(), x.getKey()));
	}

	public static Keybindings instance() {
		if (instance == null) {
			instance = new Keybindings();
		}
		return instance;
	}
}
