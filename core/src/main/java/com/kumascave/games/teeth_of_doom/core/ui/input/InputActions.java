package com.kumascave.games.teeth_of_doom.core.ui.input;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.kumascave.games.teeth_of_doom.AppContext;
import com.kumascave.games.teeth_of_doom.ConfigKey;
import com.kumascave.games.teeth_of_doom.core.GameContext;
import com.kumascave.games.teeth_of_doom.core.ui.actors.windows.DynamicVariableWindow;
import com.kumascave.games.teeth_of_doom.core.ui.actors.windows.InventoryWindow;

import lombok.Getter;

public class InputActions {
	@Getter
	public static final Map<ConfigKey, Runnable> bindingsKeyDown = buildKeyDown();

	private static Map<ConfigKey, Runnable> buildKeyDown() {
		Map<ConfigKey, Runnable> result = new HashMap<>();
		GameContext.inst();
		result.put(ConfigKey.KEYBIND_CAMERA_UP,
				() -> GameContext.getCameraController().mergeDirection(new Vector2(0, 1)));
		GameContext.inst();
		result.put(ConfigKey.KEYBIND_CAMERA_DOWN,
				() -> GameContext.getCameraController().mergeDirection(new Vector2(0, -1)));
		GameContext.inst();
		result.put(ConfigKey.KEYBIND_CAMERA_LEFT,
				() -> GameContext.getCameraController().mergeDirection(new Vector2(-1, 0)));
		GameContext.inst();
		result.put(ConfigKey.KEYBIND_CAMERA_RIGHT,
				() -> GameContext.getCameraController().mergeDirection(new Vector2(1, 0)));

		GameContext.inst();
		result.put(ConfigKey.KEYBIND_CAMERA_TOGGLE, () -> GameContext.getCameraController().toggleMode());

		GameContext.inst();
		result.put(ConfigKey.KEYBIND_MOVE_UP, () -> GameContext.getPlayer().getMovementDir().add(0f, 1f));
		GameContext.inst();
		result.put(ConfigKey.KEYBIND_MOVE_DOWN, () -> GameContext.getPlayer().getMovementDir().add(0f, -1f));
		GameContext.inst();
		result.put(ConfigKey.KEYBIND_MOVE_LEFT, () -> GameContext.getPlayer().getMovementDir().add(-1f, 0f));
		GameContext.inst();
		result.put(ConfigKey.KEYBIND_MOVE_RIGHT, () -> GameContext.getPlayer().getMovementDir().add(1f, 0f));

		GameContext.inst();
		result.put(ConfigKey.KEYBIND_LEFT_HAND, () -> GameContext.getPlayer().leftHandAction());
		GameContext.inst();
		result.put(ConfigKey.KEYBIND_RIGHT_HAND, () -> GameContext.getPlayer().rightHandAction());

		GameContext.inst();
		GameContext.inst();
		result.put(ConfigKey.KEYBIND_USE, () -> {
			DynamicVariableWindow dynamicVarWindow = new DynamicVariableWindow(GameContext.getHudStage().getHeight());
			dynamicVarWindow.setX(GameContext.getHudStage().getWidth() / 2 - dynamicVarWindow.getWidth() / 2);
			GameContext.getHudStage().addActor(dynamicVarWindow);
			dynamicVarWindow.requestFocus();
		});
		result.put(ConfigKey.KEYBIND_INVENTORY, () -> InventoryWindow.toggle());

		return result;
	}

	@Getter
	public static final Map<ConfigKey, Runnable> bindingsKeyUp = buildKeyUp();

	private static Map<ConfigKey, Runnable> buildKeyUp() {
		Map<ConfigKey, Runnable> result = new HashMap<>();
		GameContext.inst();
		result.put(ConfigKey.KEYBIND_CAMERA_UP,
				() -> GameContext.getCameraController().mergeDirection(new Vector2(0, -1)));
		GameContext.inst();
		result.put(ConfigKey.KEYBIND_CAMERA_DOWN,
				() -> GameContext.getCameraController().mergeDirection(new Vector2(0, 1)));
		GameContext.inst();
		result.put(ConfigKey.KEYBIND_CAMERA_LEFT,
				() -> GameContext.getCameraController().mergeDirection(new Vector2(1, 0)));
		GameContext.inst();
		result.put(ConfigKey.KEYBIND_CAMERA_RIGHT,
				() -> GameContext.getCameraController().mergeDirection(new Vector2(-1, 0)));

		GameContext.inst();
		result.put(ConfigKey.KEYBIND_MOVE_UP, () -> GameContext.getPlayer().getMovementDir().add(0f, -1f));
		GameContext.inst();
		result.put(ConfigKey.KEYBIND_MOVE_DOWN, () -> GameContext.getPlayer().getMovementDir().add(0f, 1f));
		GameContext.inst();
		result.put(ConfigKey.KEYBIND_MOVE_LEFT, () -> GameContext.getPlayer().getMovementDir().add(1f, 0f));
		GameContext.inst();
		result.put(ConfigKey.KEYBIND_MOVE_RIGHT, () -> GameContext.getPlayer().getMovementDir().add(-1f, 0f));

		return result;
	}

	// TODO: this ignores some keyboard inputs
	public static boolean isPressed(ConfigKey key) {
		int inputKey = AppContext.inst().getConfigManager().getInteger(key);
		if (inputKey <= 4) {
			// its a mouse Button
			return Gdx.input.isButtonPressed(inputKey);
		}
		// its keyboard Input
		return Gdx.input.isKeyPressed(inputKey);
	}

}
