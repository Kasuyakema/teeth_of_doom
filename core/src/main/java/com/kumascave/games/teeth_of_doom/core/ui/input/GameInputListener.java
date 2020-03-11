package com.kumascave.games.teeth_of_doom.core.ui.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.kumascave.games.teeth_of_doom.AppContext;
import com.kumascave.games.teeth_of_doom.ConfigKey;
import com.kumascave.games.teeth_of_doom.core.GameContext;
import com.kumascave.games.teeth_of_doom.screens.OptionScreen;

public class GameInputListener extends InputListener implements EventListener {

	public GameInputListener() {
		super();
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		switch (button) {
		default:
			ConfigKey key = Keybindings.instance().getBindings().get(button);
			if (key != null) {
				Runnable action = InputActions.getBindingsKeyDown().get(key);
				if (action != null) {
					action.run();
				}
			}
			break;
		}
		return true;
	}

	@Override
	public boolean keyDown(InputEvent event, int keycode) {
		// Gdx.app.log("KeyDown:", "" + keycode);
		switch (keycode) {
		case Keys.ESCAPE:
			AppContext.inst().getGame().setScreen(new OptionScreen(GameContext.getGameScreen()));
		default:
			ConfigKey key = Keybindings.instance().getBindings().get(keycode);
			if (key != null) {
				Runnable action = InputActions.getBindingsKeyDown().get(key);
				if (action != null) {
					action.run();
				}
			}
			break;
		}
		return true;
	}

	@Override
	public boolean keyUp(InputEvent event, int keycode) {
		// Gdx.app.log("KeyUp:", "" + keycode);
		switch (keycode) {
		default:
			ConfigKey key = Keybindings.instance().getBindings().get(keycode);
			if (key != null) {
				Runnable action = InputActions.getBindingsKeyUp().get(key);
				if (action != null) {
					action.run();
				}
			}
			break;
		}
		return true;
	}

	@Override
	public boolean mouseMoved(InputEvent event, float x, float y) {
		return true;
	}

	@Override
	public boolean scrolled(InputEvent event, float x, float y, int amount) {
		GameContext.inst();
		GameContext.inst();
		GameContext.getCameraController().setZoom(GameContext.getCameraController().getZoom() * (1 + 0.2f * amount));
		return true;
	}

	@Override
	public boolean keyTyped(InputEvent event, char character) {
		// Gdx.app.log("KeyTyped:", "" + character);
		return true;
	}

}
