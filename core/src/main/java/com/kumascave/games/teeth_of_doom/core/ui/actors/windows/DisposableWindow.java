package com.kumascave.games.teeth_of_doom.core.ui.actors.windows;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Disposable;
import com.kumascave.games.teeth_of_doom.FirstGame;

public class DisposableWindow extends ScalableWindow implements Disposable {

	public DisposableWindow(String title, String styleName) {
		super(title, FirstGame.gameSkin, styleName);
		pad(0f);
		addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				// dont propagate clicks in window
				return true;
			}

			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				switch (keycode) {
				case Keys.ESCAPE:
					DisposableWindow.this.dispose();
					return true;
				default:
					break;
				}
				return false;
			}
		});
	}

	public DisposableWindow(String title) {
		this(title, "default");
	}

	@Override
	public void dispose() {
		remove();
		this.clear();
	}
}
