package com.kumascave.games.firstgame.core.world;

import static com.kumascave.games.firstgame.core.Constants.GRIDSIZE;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.kumascave.games.firstgame.core.Constants;
import com.kumascave.games.firstgame.core.GameContext;
import com.kumascave.games.firstgame.core.world.tiles.Floor;
import com.kumascave.games.firstgame.core.world.tiles.Tile;
import com.kumascave.games.firstgame.core.world.tiles.Wall;

import lombok.Getter;

public class TiledLayer extends Group {

	@Getter
	private ChunkMap map;

	private Body walls;

	private boolean collisionMapUpdated = false;

	public TiledLayer(LayeredStage layeredStage, CPos startingPos) {
		super();
		BodyDef wallBodyDef = new BodyDef();
		wallBodyDef.position.set(new Vector2(0, 0));
		walls = GameContext.inst().getWorld().createBody(wallBodyDef);
		walls.setUserData(this);
		map = new ChunkMap(layeredStage, startingPos);
		map.getcollisionMap().addPropertyChangeListener((evt) -> {
			collisionMapUpdated = true;
		});
		updateWalls();
	}

	private void updateWalls() {
		List<ChainShape> chains = map.getcollisionMap().findWalls();

		Array<Fixture> fixes = walls.getFixtureList();
		if (fixes != null) {
			fixes.forEach(x -> walls.destroyFixture(x));
		}
		chains.forEach(x -> {
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = x;
			fixtureDef.filter.categoryBits = CollisionFilters.ALL_CATEGORY;
			Fixture fix = walls.createFixture(fixtureDef);
			fix.setUserData(this);
		});

		chains.forEach(x -> x.dispose());
	}

	@Override
	public void addActor(Actor tile) {
		tile.setX(round(tile.getX()));
		tile.setY(round(tile.getY()));
		super.addActor(tile);
	}

	public void setTile(Tile tile) {
		map.setTile(tile);
	}

	public Tile getTile(GPos pos) {
		return map.getTile(pos);
	}

	private float round(float x) {
		return Math.round(x / GRIDSIZE) * GRIDSIZE;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		// TODO: update Map?
		if (collisionMapUpdated) {
			updateWalls();
			collisionMapUpdated = false;
		}

	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// super.draw(batch, parentAlpha);
		batch.end();
		ShapeRenderer sr = new ShapeRenderer();
		sr.setProjectionMatrix(GameContext.inst().getGameStage().getCamera().combined);
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.GRAY);

		GPos screenBotLeft = GPos.fromWorldPos(GameContext.inst().getGameStage().screenToStageCoordinates(
				new Vector2(0, GameContext.inst().getGameStage().getViewport().getScreenHeight())));

		Vector2 screenBotLeftWorld = screenBotLeft.toWorldPos();

		GPos screenTopRight = GPos.fromWorldPos(GameContext.inst().getGameStage().screenToStageCoordinates(
				new Vector2(GameContext.inst().getGameStage().getViewport().getScreenWidth(), 0)));
		// GPos screenTopRight = new GPos(40, 40);

		Tile[][] tiles = map.getTiles(screenBotLeft, screenTopRight.add(1, 1));

		for (int y = 0; y < tiles[0].length - 1; y++) {
			for (int x = 0; x < tiles.length - 1; x++) {
				if (tiles[x][y] instanceof Wall) {
					sr.setColor(Color.GRAY);
					sr.rect(screenBotLeftWorld.x + x * Constants.GRIDSIZE,
							screenBotLeftWorld.y + y * Constants.GRIDSIZE, Constants.GRIDSIZE, Constants.GRIDSIZE);
				} else if (tiles[x][y] instanceof Floor) {
					sr.setColor(Color.BROWN);
					sr.rect(screenBotLeftWorld.x + x * Constants.GRIDSIZE,
							screenBotLeftWorld.y + y * Constants.GRIDSIZE, Constants.GRIDSIZE, Constants.GRIDSIZE);
				}
			}
		}
		sr.end();
		batch.begin();
	}

}
