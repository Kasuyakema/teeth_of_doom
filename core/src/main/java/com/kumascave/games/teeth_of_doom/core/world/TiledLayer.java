package com.kumascave.games.teeth_of_doom.core.world;

import static com.kumascave.games.teeth_of_doom.core.Constants.TILEGRID_SIZE;

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
import com.kumascave.games.teeth_of_doom.core.Constants;
import com.kumascave.games.teeth_of_doom.core.GameContext;
import com.kumascave.games.teeth_of_doom.core.physics.WorldUtil;
import com.kumascave.games.teeth_of_doom.core.world.tiles.Floor;
import com.kumascave.games.teeth_of_doom.core.world.tiles.Tile;
import com.kumascave.games.teeth_of_doom.core.world.tiles.Wall;

import lombok.Getter;

public class TiledLayer extends Group {

	@Getter
	private ChunkMap map;

	private Body walls;

	private boolean collisionMapUpdated = false;

	private ShapeRenderer sr = new ShapeRenderer();

	public TiledLayer(LayeredStage layeredStage, CPos startingPos) {
		super();
		BodyDef wallBodyDef = new BodyDef();
		wallBodyDef.position.set(new Vector2(0, 0));
		GameContext.inst();
		walls = WorldUtil.createBody(wallBodyDef);
		walls.setUserData(this);
		map = new ChunkMap(layeredStage, startingPos);
		map.getCollisionMap().addPropertyChangeListener((evt) -> {
			collisionMapUpdated = true;
		});
		updateWalls();
	}

	private void updateWalls() {
		List<ChainShape> chains = map.getCollisionMap().findWalls();

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

	public Tile getTile(TPos pos) {
		return map.getTile(pos);
	}

	private float round(float x) {
		return Math.round(x / TILEGRID_SIZE) * TILEGRID_SIZE;
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
		GameContext.inst();
		sr.setProjectionMatrix(GameContext.getGameStage().getCamera().combined);
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.GRAY);

		GameContext.inst();
		GameContext.inst();
		TPos screenBotLeft = TPos.fromWorldPos(GameContext.getGameStage()
				.screenToStageCoordinates(new Vector2(0, GameContext.getGameStage().getViewport().getScreenHeight())));

		Vector2 screenBotLeftWorld = screenBotLeft.botLeftWorldPos();

		GameContext.inst();
		GameContext.inst();
		TPos screenTopRight = TPos.fromWorldPos(GameContext.getGameStage()
				.screenToStageCoordinates(new Vector2(GameContext.getGameStage().getViewport().getScreenWidth(), 0)));
		// GPos screenTopRight = new GPos(40, 40);

		Tile[][] tiles = map.getTiles(screenBotLeft, screenTopRight.add(1, 1));

		for (int y = 0; y < tiles[0].length - 1; y++) {
			for (int x = 0; x < tiles.length - 1; x++) {
				if (tiles[x][y] instanceof Wall) {
					sr.setColor(Color.GRAY);
					sr.rect(screenBotLeftWorld.x + x * Constants.TILEGRID_SIZE,
							screenBotLeftWorld.y + y * Constants.TILEGRID_SIZE, Constants.TILEGRID_SIZE,
							Constants.TILEGRID_SIZE);
				} else if (tiles[x][y] instanceof Floor) {
					sr.setColor(Color.BROWN);
					sr.rect(screenBotLeftWorld.x + x * Constants.TILEGRID_SIZE,
							screenBotLeftWorld.y + y * Constants.TILEGRID_SIZE, Constants.TILEGRID_SIZE,
							Constants.TILEGRID_SIZE);
				}
			}
		}
		sr.end();
		batch.begin();
	}

}
