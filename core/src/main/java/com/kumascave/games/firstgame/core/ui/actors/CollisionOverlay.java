package com.kumascave.games.firstgame.core.ui.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.kumascave.games.firstgame.core.Constants;
import com.kumascave.games.firstgame.core.GameContext;
import com.kumascave.games.firstgame.core.world.GPos;

public class CollisionOverlay extends Actor implements HudElement {

	public CollisionOverlay() {
		super();
		this.setTouchable(Touchable.disabled);
		this.setVisible(false);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// super.draw(batch, parentAlpha);
		batch.end();
		ShapeRenderer sr = new ShapeRenderer();
		sr.setProjectionMatrix(GameContext.inst().getGameStage().getCamera().combined);
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.RED);

		Boolean[][] tiles = GameContext.inst().getGameStage().getTileLayer().getMap().getcollisionMap().getTiles();
		GPos screenBotLeft = GPos.fromWorldPos(GameContext.inst().getGameStage().screenToStageCoordinates(
				new Vector2(0, GameContext.inst().getGameStage().getViewport().getScreenHeight())));
		GPos screenTopRight = GPos.fromWorldPos(GameContext.inst().getGameStage().screenToStageCoordinates(
				new Vector2(GameContext.inst().getGameStage().getViewport().getScreenWidth(), 0)));
		GPos mapBotleft = GameContext.inst().getGameStage().getTileLayer().getMap().getcollisionMap().getBotLeft();
		Vector2 mapBotleftWorld = mapBotleft.toWorldPos();

		for (int y = screenBotLeft.y - mapBotleft.y; y < screenTopRight.y - mapBotleft.y + 1; y++) {
			for (int x = screenBotLeft.x - mapBotleft.x; x < screenTopRight.x - mapBotleft.x + 1; x++) {
				if (tiles[x][y]) {
					sr.rect(mapBotleftWorld.x + x * Constants.GRIDSIZE, mapBotleftWorld.y + y * Constants.GRIDSIZE,
							Constants.GRIDSIZE, Constants.GRIDSIZE);
				}
			}
		}
		sr.end();
		batch.begin();
	}

	@Override
	public void updateGeometry() {
		this.setSize(getParent().getWidth(), getParent().getHeight());
	}

}
