package com.kumascave.games.firstgame.atest;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.kumascave.games.firstgame.AppContext;
import com.kumascave.games.firstgame.core.Constants;
import com.kumascave.games.firstgame.core.GameContext;

import lombok.Getter;

public class Foreground extends Image {
	private Texture texture;
	@Getter
	private Pixmap pixmapBase;
	private Pixmap pixmapClear;

	public Foreground() {
		super();
		pixmapBase = AppContext.inst().getAssetManager().get("stone_wall.png", Pixmap.class);
		texture = new Texture(pixmapBase);

		setDrawable(new SpriteDrawable(new Sprite(texture)));
		OrthographicCamera cam = (OrthographicCamera) GameContext.inst().getGameStage().getCamera();
		this.setSize(cam.viewportWidth * 10, cam.viewportHeight * 10);
		this.setPosition(-getWidth() / 2, -getHeight() / 2);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {

		Boolean[][] tiles = GameContext.inst().getGameStage().getTileLayer().getMap().getcollisionMap().getTiles();

		Vector2 zero = GameContext.inst().getGameStage().screenToStageCoordinates(new Vector2(0, 0));
		float xShift = zero.x % Constants.GRIDSIZE;
		float yShift = Constants.GRIDSIZE - zero.y % Constants.GRIDSIZE;
		// world3D.x = zero.x;
		// world3D.y = zero.y;
		// world3D.z = 0;
		// cam.project(zero);
		// int tileWidthInPx = (int) world3D.x;
		// System.out.println(zero);

		// pixmapClear = new Pixmap(tileWidthInPx, tileWidthInPx, Format.RGBA8888);
		// pixmapClear.setColor(Color.CLEAR);
		// pixmapClear.drawPixel(0, 0);
		//
		// pixmapBase.setBlending(Blending.None);
		// pixmapBase.setColor(Color.CLEAR);
		// pixmapBase.drawLine(0, 0, tileWidthInPx, tileWidthInPx);

		int size = tiles.length;
		// for (int y = 1; y < size - 1; y++) {
		// for (int x = 1; x < size - 1; x++) {
		// if (tiles[x][y]) {
		// // texture.draw(pixmapClear, x * tileWidthInPx, y * tileWidthInPx);
		// pixmapBase.drawLine(x * tileWidthInPx, y * tileWidthInPx, 0, 0);
		// }
		// }
		// }
		// pixmapClear.dispose();
		texture.draw(pixmapBase, 0, 0);
		super.draw(batch, parentAlpha);
	}

}
