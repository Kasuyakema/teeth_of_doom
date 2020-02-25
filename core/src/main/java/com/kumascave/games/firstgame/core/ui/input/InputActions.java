package com.kumascave.games.firstgame.core.ui.input;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.kumascave.games.firstgame.AppContext;
import com.kumascave.games.firstgame.ConfigKey;
import com.kumascave.games.firstgame.core.GameContext;
import com.kumascave.games.firstgame.core.ui.actors.InventoryWindow;

import lombok.Getter;

public class InputActions {
	@Getter
	public static final Map<ConfigKey, Runnable> bindingsKeyDown = buildKeyDown();

	private static Map<ConfigKey, Runnable> buildKeyDown() {
		Map<ConfigKey, Runnable> result = new HashMap<>();
		result.put(ConfigKey.KEYBIND_CAMERA_UP,
				() -> GameContext.inst().getCameraController().mergeDirection(new Vector2(0, 1)));
		result.put(ConfigKey.KEYBIND_CAMERA_DOWN,
				() -> GameContext.inst().getCameraController().mergeDirection(new Vector2(0, -1)));
		result.put(ConfigKey.KEYBIND_CAMERA_LEFT,
				() -> GameContext.inst().getCameraController().mergeDirection(new Vector2(-1, 0)));
		result.put(ConfigKey.KEYBIND_CAMERA_RIGHT,
				() -> GameContext.inst().getCameraController().mergeDirection(new Vector2(1, 0)));

		result.put(ConfigKey.KEYBIND_CAMERA_TOGGLE, () -> GameContext.inst().getCameraController().toggleMode());

		result.put(ConfigKey.KEYBIND_MOVE_UP, () -> GameContext.inst().getPlayer().getMovementDir().add(0f, 1f));
		result.put(ConfigKey.KEYBIND_MOVE_DOWN, () -> GameContext.inst().getPlayer().getMovementDir().add(0f, -1f));
		result.put(ConfigKey.KEYBIND_MOVE_LEFT, () -> GameContext.inst().getPlayer().getMovementDir().add(-1f, 0f));
		result.put(ConfigKey.KEYBIND_MOVE_RIGHT, () -> GameContext.inst().getPlayer().getMovementDir().add(1f, 0f));

		result.put(ConfigKey.KEYBIND_LEFT_HAND, () -> GameContext.inst().getPlayer().leftHandAction());
		result.put(ConfigKey.KEYBIND_RIGHT_HAND, () -> GameContext.inst().getPlayer().rightHandAction());

		result.put(ConfigKey.KEYBIND_USE,
				() -> GameContext.inst().getHudController().getHudRootActor().getCollisionOverlay().setVisible(
						!GameContext.inst().getHudController().getHudRootActor().getCollisionOverlay().isVisible()));

		result.put(ConfigKey.KEYBIND_INVENTORY, () -> InventoryWindow.toggle());

		return result;
	}

	@Getter
	public static final Map<ConfigKey, Runnable> bindingsKeyUp = buildKeyUp();

	private static Map<ConfigKey, Runnable> buildKeyUp() {
		Map<ConfigKey, Runnable> result = new HashMap<>();
		result.put(ConfigKey.KEYBIND_CAMERA_UP,
				() -> GameContext.inst().getCameraController().mergeDirection(new Vector2(0, -1)));
		result.put(ConfigKey.KEYBIND_CAMERA_DOWN,
				() -> GameContext.inst().getCameraController().mergeDirection(new Vector2(0, 1)));
		result.put(ConfigKey.KEYBIND_CAMERA_LEFT,
				() -> GameContext.inst().getCameraController().mergeDirection(new Vector2(1, 0)));
		result.put(ConfigKey.KEYBIND_CAMERA_RIGHT,
				() -> GameContext.inst().getCameraController().mergeDirection(new Vector2(-1, 0)));

		result.put(ConfigKey.KEYBIND_MOVE_UP, () -> GameContext.inst().getPlayer().getMovementDir().add(0f, -1f));
		result.put(ConfigKey.KEYBIND_MOVE_DOWN, () -> GameContext.inst().getPlayer().getMovementDir().add(0f, 1f));
		result.put(ConfigKey.KEYBIND_MOVE_LEFT, () -> GameContext.inst().getPlayer().getMovementDir().add(1f, 0f));
		result.put(ConfigKey.KEYBIND_MOVE_RIGHT, () -> GameContext.inst().getPlayer().getMovementDir().add(-1f, 0f));

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
