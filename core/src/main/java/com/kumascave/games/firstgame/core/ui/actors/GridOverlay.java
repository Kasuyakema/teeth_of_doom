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

public class GridOverlay extends Actor implements HudElement {

	public GridOverlay() {
		super();
		this.setTouchable(Touchable.disabled);
		this.setVisible(false);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// super.draw(batch, parentAlpha);
		batch.end();
		ShapeRenderer sr = new ShapeRenderer();
		sr.begin(ShapeType.Line);
		sr.setColor(Color.RED);

		Vector2 zero = GameContext.inst().getGameStage().screenToStageCoordinates(new Vector2(0, 0));
		float xShift = zero.x % Constants.GRIDSIZE;
		float yShift = Constants.GRIDSIZE - zero.y % Constants.GRIDSIZE;

		float xMax = getWidth();
		float yMax = getHeight();
		for (float x = 0; x < xMax; x += Constants.GRIDSIZE) {
			sr.line(GameContext.inst().getGameStage().stageToScreenCoordinates(new Vector2(x - xShift, 0).add(zero)),
					GameContext.inst().getGameStage()
							.stageToScreenCoordinates(new Vector2(x - xShift, -yMax).add(zero)));
		}
		for (float y = 0; y < yMax; y += Constants.GRIDSIZE) {
			sr.line(GameContext.inst().getGameStage().stageToScreenCoordinates(new Vector2(0, -(y - yShift)).add(zero)),
					GameContext.inst().getGameStage()
							.stageToScreenCoordinates(new Vector2(xMax, -(y - yShift)).add(zero)));
		}
		sr.end();
		batch.begin();
	}

	@Override
	public void updateGeometry() {
		this.setSize(getParent().getWidth(), getParent().getHeight());
	}

}
