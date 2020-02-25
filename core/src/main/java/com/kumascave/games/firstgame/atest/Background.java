package com.kumascave.games.firstgame.atest;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kumascave.games.firstgame.AppContext;
import com.kumascave.games.firstgame.core.GameContext;

public class Background extends Image {
	public Background() {
		super(AppContext.inst().getAssetManager().get("naturalstone.jpg", Texture.class));
		OrthographicCamera cam = (OrthographicCamera) GameContext.inst().getGameStage().getCamera();
		this.setSize(cam.viewportWidth * 10, cam.viewportHeight * 10);
		this.setPosition(-getWidth() / 2, -getHeight() / 2);
	}

}
