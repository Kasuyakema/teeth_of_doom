package com.kumascave.games.teeth_of_doom.core.ui.actors;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.kumascave.games.teeth_of_doom.core.Constants;
import com.kumascave.games.teeth_of_doom.core.GameContext;
import com.kumascave.games.teeth_of_doom.core.ai.navigation.GlobalPlanner;
import com.kumascave.games.teeth_of_doom.core.ai.navigation.NavigationGraph;
import com.kumascave.games.teeth_of_doom.util.DynamicVariables;

public class NavigationOverlay extends Actor implements HudElement {

	ShapeRenderer sr = new ShapeRenderer();

	public NavigationOverlay() {
		super();
		this.setTouchable(Touchable.disabled);
		this.setVisible(DynamicVariables.navigationOverlay.getValue());
		DynamicVariables.navigationOverlay
				.addPropertyChangeListener(evt -> this.setVisible((Boolean) evt.getNewValue()));
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// super.draw(batch, parentAlpha);
		batch.end();
		sr.setProjectionMatrix(GameContext.getGameStage().getCamera().combined);
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.WHITE);

		NavigationGraph graph = GlobalPlanner.graph;

		for (Vector2 node : graph.getNodes()) {
			sr.rect(node.x - Constants.TILEGRID_SIZE / 20f, node.y - Constants.TILEGRID_SIZE / 20f,
					Constants.TILEGRID_SIZE / 10f, Constants.TILEGRID_SIZE / 10f);
		}

		sr.end();
		sr.begin(ShapeType.Line);

		for (Vector2 node : graph.getNodes()) {
			for (Connection<Vector2> connection : graph.getConnections(node)) {
				sr.line(connection.getFromNode(), connection.getToNode());
			}
		}

//		for (Connection<Vector2> connection : graph.getConnections(new Vector2(3.25f, 3.25f))) {
//			sr.line(connection.getFromNode(), connection.getToNode());
//		}

//		for (int y = screenBotLeft.y - mapBotleft.y; y < screenTopRight.y - mapBotleft.y + 1; y++) {
//			for (int x = screenBotLeft.x - mapBotleft.x; x < screenTopRight.x - mapBotleft.x + 1; x++) {
//				if (tiles[x][y]) {
//					sr.rect(mapBotleftWorld.x + x * Constants.GRIDSIZE, mapBotleftWorld.y + y * Constants.GRIDSIZE,
//							Constants.GRIDSIZE, Constants.GRIDSIZE);
//				}
//			}
//		}
		sr.end();
		batch.begin();
	}

	@Override
	public void updateGeometry() {
		this.setSize(getParent().getWidth(), getParent().getHeight());
	}

}
