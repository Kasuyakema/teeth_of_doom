package com.kumascave.games.teeth_of_doom;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ConfigKey {
	CAMERA_SPEED(20.0f),

	CAMERA_LERP(0.1f),

	KEYBIND_CAMERA_UP(Keys.UP),

	KEYBIND_CAMERA_DOWN(Keys.DOWN),

	KEYBIND_CAMERA_LEFT(Keys.LEFT),

	KEYBIND_CAMERA_RIGHT(Keys.RIGHT),

	KEYBIND_CAMERA_TOGGLE(Keys.T),

	KEYBIND_MOVE_UP(Keys.W),

	KEYBIND_MOVE_DOWN(Keys.S),

	KEYBIND_MOVE_LEFT(Keys.A),

	KEYBIND_MOVE_RIGHT(Keys.D),

	KEYBIND_LEFT_HAND(Buttons.RIGHT),

	KEYBIND_RIGHT_HAND(Buttons.LEFT),

	KEYBIND_THROW(Keys.Q),

	KEYBIND_USE(Keys.F),

	// multiple Keybinds? Keyboard Pause-Button?
	KEYBIND_PAUSE(Keys.SPACE),

	KEYBIND_INVENTORY(Keys.I),

	WORLD_CHUNK_LOOKAHEAD(2),

	WORLD_CHUNK_FORGET(5),

	;

	public static final Pattern KEYBIND_ALL = Pattern.compile("KEYBIND.*");
	@Getter
	private Object defaultVal;

	public String getPrefKey() {
		String key = name().toLowerCase();
		return StringUtils.replace(key, "_", ".");
	}
}
