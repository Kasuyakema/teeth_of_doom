package com.kumascave.games.teeth_of_doom.core.ui.actors;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.kumascave.games.teeth_of_doom.AppContext;
import com.kumascave.games.teeth_of_doom.core.GameContext;
import com.kumascave.games.teeth_of_doom.screens.OptionScreen;

public class MenuButton extends Button implements HudElement {

	public MenuButton() {
		super(new SpriteDrawable(new Sprite(AppContext.inst().getAssetManager().get("gear.png", Texture.class))));
		initButton();
	}

	private void initButton() {
		this.addListener(new MenuButtonListener());
	}

	public void hide() {
		this.setPosition(getParent().getWidth() - getWidth() / 2.0f, getParent().getHeight() - getHeight() / 2.0f);
	}

	public void show() {
		this.setPosition(getParent().getWidth() - getWidth(), getParent().getHeight() - getHeight());
	}

	public void showMenuScreen() {
		Game game = AppContext.inst().getGame();
		GameContext.inst();
		game.setScreen(new OptionScreen(GameContext.getGameScreen()));
	}

	@Override
	protected void setParent(Group parent) {
		super.setParent(parent);
		updateGeometry();
	}

	private class MenuButtonListener extends ClickListener {
		@Override
		public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
			show();
			super.enter(event, x, y, pointer, fromActor);
		}

		@Override
		public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
			hide();
			super.exit(event, x, y, pointer, toActor);
		}

		@Override
		public void clicked(InputEvent event, float x, float y) {
			showMenuScreen();
			super.clicked(event, x, y);
		}
	}

	@Override
	public void updateGeometry() {

		this.setHeight(getParent().getHeight() / 10);
		this.setWidth(getParent().getHeight() / 10);

		GameContext.inst();
		Vector2 mouse = GameContext.getMousePosition();
		GameContext.inst();
		if (GameContext.getGameStage().hit(mouse.x, mouse.y, true) == this) {
			show();
		} else {
			hide();
		}

	}
}
