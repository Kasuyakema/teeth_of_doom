package com.kumascave.games.firstgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kumascave.games.firstgame.core.GameContext;
import com.kumascave.games.firstgame.screens.TitleScreen;

public class FirstGame extends Game {
	static public Skin gameSkin;

	@Override
	public void create() {
		AppContext.inst();
		AppContext.inst().setGame(this);
		gameSkin = new Skin(Gdx.files.internal("skin/wooden_ui.json"));
		this.setScreen(new TitleScreen());
		Gdx.app.log("MainScreen", "show");
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		AppContext.inst().dispose();
		GameContext.inst().dispose();
	}
}
