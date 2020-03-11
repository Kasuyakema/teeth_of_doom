package com.kumascave.games.teeth_of_doom.core.entity.mobs.creeps;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kumascave.games.teeth_of_doom.AppContext;
import com.kumascave.games.teeth_of_doom.core.ai.navigation.GlobalPlanner;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.Mob;
import com.kumascave.games.teeth_of_doom.core.mechanics.alignment.Alignment;
import com.kumascave.games.teeth_of_doom.core.physics.Friction;
import com.kumascave.games.teeth_of_doom.core.physics.Pose;
import com.kumascave.games.teeth_of_doom.core.physics.shapes.Circle;

public class Creep extends Mob {

	public static Friction friction = new Friction(0.7f, 0.7f);
	public static float weight = 3f;
	public static float diam = 0.2f;
	private static float density = Circle.densityFromWeight(weight, diam);
	public static float restitution = 0.1f;

	private List<Vector2> path;
	private CompletableFuture<Void> future;

	public Creep(Pose pose, float weight) {
		super(new Vector2(diam, diam), pose, new Circle(diam), density, friction, restitution);
		maxMoveSpeed = 1;
		strength = 1;
		getHpMaxHolder().setValue(30);
		target = null;
		setAlignment(Alignment.HOSTILE);
		setDrawable(new TextureRegionDrawable(
				new TextureRegion(AppContext.inst().getAssetManager().get("creep.png", Texture.class))));
	}

	@Override
	public void act(float deltaT) {
		if (path == null && future == null) {
			// request path
			future = CompletableFuture.supplyAsync(() -> {
				return GlobalPlanner.findPath(getPose().getPos(), new Vector2(0, 0), diam / 2f);
			}).thenAccept(l -> path = l).thenRun(() -> future = null);
		}
		if (path == null && future != null) {
			// wait for path
		}
		if (path != null) {
			// follow path
			if (path.size() == 0) {
				// System.out.println("reached Mother");
			} else {
				if (target == null && path.size() > 0) {
					target = path.get(0);
				}
				if (getPose().getPos().cpy().sub(target).len2() < 0.1f) {
					// System.out.println("reached Node");
					path.remove(0);
					target = null;
				}
			}
		}
		if (target != null) {
			this.setMovementDir(target.cpy().sub(getPose().getPos()).setLength(1.0f));
		}
		super.act(deltaT);
	}

//	private static ShapeRenderer sr = new ShapeRenderer();
//	@Override
//	public void draw(Batch batch, float parentAlpha) {
//		super.draw(batch, parentAlpha);
//		if (path != null) {
//			batch.end();
//			sr.setProjectionMatrix(GameContext.getGameStage().getCamera().combined);
//			sr.begin(ShapeType.Line);
//			sr.setColor(Color.WHITE);
//			for (int i = 0; i < path.size() - 1; i++) {
//				sr.line(path.get(i), path.get(i + 1));
//			}
//			sr.end();
//			batch.begin();
//		}
//	}

}
