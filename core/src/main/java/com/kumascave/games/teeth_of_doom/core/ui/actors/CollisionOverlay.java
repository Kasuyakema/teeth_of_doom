package com.kumascave.games.teeth_of_doom.core.ui.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.kumascave.games.teeth_of_doom.core.Constants;
import com.kumascave.games.teeth_of_doom.core.GameContext;
import com.kumascave.games.teeth_of_doom.core.world.TPos;
import com.kumascave.games.teeth_of_doom.util.DynamicVariables;

public class CollisionOverlay extends Actor implements HudElement {

	public CollisionOverlay() {
		super();
		this.setTouchable(Touchable.disabled);
		this.setVisible(DynamicVariables.collisionOverlay.getValue());
		DynamicVariables.collisionOverlay
				.addPropertyChangeListener(evt -> this.setVisible((Boolean) evt.getNewValue()));
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// super.draw(batch, parentAlpha);
		batch.end();
		ShapeRenderer sr = new ShapeRenderer();
		sr.setProjectionMatrix(GameContext.getGameStage().getCamera().combined);
		sr.begin(ShapeType.Filled);
		Color color = Color.RED;
		sr.setColor(color);

		Boolean[][] tiles = GameContext.getGameStage().getTileLayer().getMap().getCollisionMap().getTiles();
		TPos screenBotLeft = TPos.fromWorldPos(GameContext.getGameStage()
				.screenToStageCoordinates(new Vector2(0, GameContext.getGameStage().getViewport().getScreenHeight())));
		TPos screenTopRight = TPos.fromWorldPos(GameContext.getGameStage()
				.screenToStageCoordinates(new Vector2(GameContext.getGameStage().getViewport().getScreenWidth(), 0)));
		TPos mapBotleft = GameContext.getGameStage().getTileLayer().getMap().getCollisionMap().getBotLeft();
		Vector2 mapBotleftWorld = mapBotleft.botLeftWorldPos();

		for (int y = screenBotLeft.y - mapBotleft.y; y < screenTopRight.y - mapBotleft.y + 1; y++) {
			for (int x = screenBotLeft.x - mapBotleft.x; x < screenTopRight.x - mapBotleft.x + 1; x++) {
				if (tiles[x][y]) {
					sr.rect(mapBotleftWorld.x + x * Constants.TILEGRID_SIZE,
							mapBotleftWorld.y + y * Constants.TILEGRID_SIZE, Constants.TILEGRID_SIZE,
							Constants.TILEGRID_SIZE);
				}
			}
		}
		sr.end();
		Gdx.gl.glEnable(GL30.GL_BLEND);
		Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.YELLOW);
		GameContext.getGameStage().getTileLayer().getMap().getDistanceMap().iterate((pos, dist) -> {
			if (dist != 0 && dist < Float.MAX_VALUE) {
				sr.getColor().a = .6f * (3 - dist) / 3;
				sr.getColor().clamp();
				sr.rect(pos.botLeftWorldPos().x, pos.botLeftWorldPos().y, Constants.PLANNINGGRID_SIZE,
						Constants.PLANNINGGRID_SIZE);
			}
		});

		sr.end();
		Gdx.gl.glDisable(GL30.GL_BLEND);
		batch.begin();
	}

	@Override
	public void updateGeometry() {
		this.setSize(getParent().getWidth(), getParent().getHeight());
	}

}
