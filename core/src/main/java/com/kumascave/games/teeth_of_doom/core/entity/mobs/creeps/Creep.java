package com.kumascave.games.teeth_of_doom.core.entity.mobs.creeps;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kumascave.games.teeth_of_doom.AppContext;
import com.kumascave.games.teeth_of_doom.core.DelayedAction;
import com.kumascave.games.teeth_of_doom.core.ai.behavior.CreepTree;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.Mob;
import com.kumascave.games.teeth_of_doom.core.mechanics.alignment.Alignment;
import com.kumascave.games.teeth_of_doom.core.mechanics.damage.Damage;
import com.kumascave.games.teeth_of_doom.core.mechanics.damage.DmgResolving;
import com.kumascave.games.teeth_of_doom.core.physics.Friction;
import com.kumascave.games.teeth_of_doom.core.physics.Pose;
import com.kumascave.games.teeth_of_doom.core.physics.shapes.Circle;

public class Creep extends Mob {

	CreepTree behavoirTree;

	public static Friction friction = new Friction(20f, 20f, 2f, 2f, 1.0f);
	public static float weight = 3f;
	public static float diam = 0.2f;
	public static float radius = diam / 2f;
	private static float density = Circle.densityFromWeight(weight, diam);
	public static float restitution = 0.1f;
	static final float meleeRange = .25f;
	static final float meleeWarntime = .75f;
	static final float meleeCooldown = 3f;
	static final int meleeDamage = 15;

	private CreepMelee melee = new CreepMelee(this);

	public Creep(Pose pose, float weight) {
		super(new Vector2(diam, diam), pose, new Circle(diam), density, friction, restitution);
		maxMoveSpeed = 1;
		strength = 1;
		attackCooldown = meleeCooldown;
		attackRange = meleeRange;
		getHpMaxHolder().setValue(30);
		setAlignment(Alignment.HOSTILE);
		components.get(0).setDrawable(new TextureRegionDrawable(
				new TextureRegion(AppContext.inst().getAssetManager().get("creep.png", Texture.class))));
		behavoirTree = new CreepTree(this);
	}

	@Override
	public void act(float deltaT) {
		if (isAlive()) {
			behavoirTree.step();
		}
//		if (path == null && future == null) {
//			// request path
//			future = CompletableFuture.supplyAsync(() -> {
//				return GlobalPlanner.findPath(getPose().getPos(), new Vector2(0, 0), radius);
//			}).thenAccept(l -> path = l).thenRun(() -> future = null);
//		}
//		if (path == null && future != null) {
//			// wait for path
//		}
//		if (path != null) {
//			// follow path
//			if (path.size() == 0) {
//				// System.out.println("reached Mother");
//			} else {
//				if (getPose().getPos().cpy().sub(path.get(0)).len2() < 0.1f) {
//					// System.out.println("reached Node");
//					path.remove(0);
//				}
//			}
//		}
//		if (path.size() > 0) {
//			this.setMovementDir(path.get(0).cpy().sub(getPose().getPos()).setLength(1.0f));
//		}
		super.act(deltaT);
	}

	@Override
	public float getRadius() {
		return radius;
	}

	private static ShapeRenderer sr = new ShapeRenderer();

//	@Override
//	public void draw(Batch batch, float parentAlpha) {
//		super.draw(batch, parentAlpha);
//		if (getPath() != null) {
//			batch.end();
//			sr.setProjectionMatrix(GameContext.getGameStage().getCamera().combined);
//			sr.begin(ShapeType.Line);
//			sr.setColor(Color.WHITE);
//			for (int i = 0; i < getPath().size() - 1; i++) {
//				sr.line(getPath().get(i), getPath().get(i + 1));
//			}
//			sr.end();
//			batch.begin();
//		}
//	}

	@Override
	public void addToWorld() {
		super.addToWorld();
		melee.addSensor(getBody());
	}

	@Override
	public void attack() {
		super.attack();
		melee.setVisible(true);
		components.get(0).addAction(new DelayedAction(meleeWarntime, () -> endAttack()));
	}

	private void endAttack() {
		melee.setVisible(false);
		melee.playAnimation();
		melee.getObserved().forEach(x -> ((DmgResolving) x).resolveDmg(new Damage(this, meleeDamage)));
	}
}
