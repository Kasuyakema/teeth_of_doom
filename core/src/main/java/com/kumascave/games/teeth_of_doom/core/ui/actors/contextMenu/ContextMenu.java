package com.kumascave.games.teeth_of_doom.core.ui.actors.contextMenu;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.kumascave.games.teeth_of_doom.core.ui.GuiDefaults;
import com.kumascave.games.teeth_of_doom.core.ui.actors.windows.DisposableWindow;

public class ContextMenu extends DisposableWindow {

	InputListener listener;

	Vector2 desiredPosition;

	public ContextMenu(Actor parent, Vector2 pos) {
		this(parent, pos, null);
	}

	public ContextMenu(Actor parent, Vector2 pos, InputEvent event) {
		super("", "context_menu");
		if (event != null) {
			event.setRelatedActor(this);
		}
		desiredPosition = pos.cpy();
		getBackground().setMinHeight(0);
		getBackground().setMinWidth(0);
		pad(GuiDefaults.CONTEXT_MENU_BORDERS);
		listener = new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (event.getRelatedActor() == null || !event.getRelatedActor().equals(ContextMenu.this)) {
					Vector2 hit = stageToLocalCoordinates(new Vector2(x, y));
					if (ContextMenu.this.hit(hit.x, hit.y, false) == null) {
						dispose();
					}
				}
				return super.touchDown(event, x, y, pointer, button);
			}
		};

		parent.getStage().addActor(this);
		parent.getStage().setKeyboardFocus(this);
		parent.getStage().setScrollFocus(this);

		getStage().addListener(listener);
	}

	@Override
	public void dispose() {
		getStage().removeListener(listener);
		listener = null;
		super.dispose();
	}

	@Override
	public void act(float delta) {
		update();
		super.act(delta);
	}

	protected void update() {
		setHeight(getPrefHeight());
		setWidth(getPrefWidth());
		setPosition(desiredPosition.x, desiredPosition.y - getHeight());
	}

}
