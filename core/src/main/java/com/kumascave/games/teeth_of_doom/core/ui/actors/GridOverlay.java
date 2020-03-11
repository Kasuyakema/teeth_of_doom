package com.kumascave.games.teeth_of_doom.core.ui.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.kumascave.games.teeth_of_doom.core.Constants;
import com.kumascave.games.teeth_of_doom.core.GameContext;
import com.kumascave.games.teeth_of_doom.util.DynamicVariables;

public class GridOverlay extends Actor implements HudElement {

	ShapeRenderer sr = new ShapeRenderer();

	public GridOverlay() {
		super();
		this.setTouchable(Touchable.disabled);
		this.setVisible(DynamicVariables.gridOverlay.getValue());
		DynamicVariables.gridOverlay.addPropertyChangeListener(evt -> this.setVisible((Boolean) evt.getNewValue()));
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// super.draw(batch, parentAlpha);
		batch.end();
		sr.begin(ShapeType.Line);
		sr.setColor(Color.RED);

		GameContext.inst();
		Vector2 zero = GameContext.getGameStage().screenToStageCoordinates(new Vector2(0, 0));
		float xShift = zero.x % Constants.TILEGRID_SIZE;
		float yShift = Constants.TILEGRID_SIZE - zero.y % Constants.TILEGRID_SIZE;

		float xMax = getWidth();
		float yMax = getHeight();
		for (float x = 0; x < xMax; x += Constants.TILEGRID_SIZE) {
			GameContext.inst();
			GameContext.inst();
			sr.line(GameContext.getGameStage().stageToScreenCoordinates(new Vector2(x - xShift, 0).add(zero)),
					GameContext.getGameStage().stageToScreenCoordinates(new Vector2(x - xShift, -yMax).add(zero)));
		}
		for (float y = 0; y < yMax; y += Constants.TILEGRID_SIZE) {
			GameContext.inst();
			GameContext.inst();
			sr.line(GameContext.getGameStage().stageToScreenCoordinates(new Vector2(0, -(y - yShift)).add(zero)),
					GameContext.getGameStage().stageToScreenCoordinates(new Vector2(xMax, -(y - yShift)).add(zero)));
		}
		sr.end();
		batch.begin();
	}

	@Override
	public void updateGeometry() {
		this.setSize(getParent().getWidth(), getParent().getHeight());
	}

}
