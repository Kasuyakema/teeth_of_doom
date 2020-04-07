package com.kumascave.games.teeth_of_doom.core.entity;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Disposable;
import com.kumascave.games.teeth_of_doom.core.GameContext;
import com.kumascave.games.teeth_of_doom.core.mechanics.damage.Damage;
import com.kumascave.games.teeth_of_doom.core.mechanics.damage.DmgResolving;
import com.kumascave.games.teeth_of_doom.core.mechanics.damage.HasHitpoints;
import com.kumascave.games.teeth_of_doom.core.mechanics.damage.HpHolder;
import com.kumascave.games.teeth_of_doom.core.physics.Friction;
import com.kumascave.games.teeth_of_doom.core.physics.PhysicalEntity;
import com.kumascave.games.teeth_of_doom.core.physics.Pose;
import com.kumascave.games.teeth_of_doom.core.world.CollisionFilters;
import com.kumascave.games.teeth_of_doom.core.world.TPos;
import com.kumascave.games.teeth_of_doom.util.jgoodies.VHolder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public abstract class Entity implements PhysicalEntity, HasHitpoints, Disposable {

	EntityAction action = new EntityAction(this);

	@Getter
	@Setter
	protected PropertyChangeSupport changeSupport;

	@Getter
	private UUID id = UUID.randomUUID();

	@Getter
	protected Friction friction;

	private final Integer hpMaxBase = 100;

	@Getter
	private VHolder<Boolean> aliveHolder = new VHolder<>(true);

	@Getter
	private VHolder<Integer> hpMaxHolder = new VHolder<>(hpMaxBase);
	@Getter
	private HpHolder hpHolder = new HpHolder(this);

	@Getter
	private Map<UUID, Long> collisionCooldowns = new HashMap<>();

	@Getter
	@Setter
	private Long lastCollisionTime = null;

	@Getter
	protected List<EntityComponent> components = new ArrayList<>();

	@Getter
	protected Group actorGroup = new Group();

	@Getter
	protected Group hudGroup = new Group();

	@Getter
	private boolean disposing = false;
	private boolean removeFromWorld = false;

	protected Entity(Vector2 actorSize, Pose startingPose, Shape shape, BodyType bodyType, float density,
			Friction friction, float restitution) {
		super();

		this.friction = friction;

		actorGroup.setTouchable(Touchable.disabled);
		actorGroup.setSize(actorSize.x, actorSize.y);
		actorGroup.setOrigin(actorGroup.getWidth() / 2, actorGroup.getHeight() / 2);
		actorGroup.rotateBy(startingPose.getAngle());
		actorGroup.setPosition(startingPose.getX(), startingPose.getY());

		hudGroup.setTouchable(Touchable.disabled);
		hudGroup.setSize(actorSize.x, actorSize.y);
		hudGroup.setOrigin(hudGroup.getWidth() / 2, hudGroup.getHeight() / 2);
		hudGroup.rotateBy(startingPose.getAngle());
		hudGroup.setPosition(startingPose.getX(), startingPose.getY());

		EntityComponent comp = new EntityComponent(actorSize, startingPose, shape, bodyType, density, friction,
				restitution, this);

		aliveHolder.addPropertyChangeListener(evt -> {
			if (!(Boolean) evt.getNewValue()) {
				onDeath();
			}
		});
		components.add(comp);
	}

	protected void onDeath() {
		// do nothing
	}

	@Override
	public void applyFriction(float deltaT) {
		PhysicalEntity.super.applyFriction(deltaT);
	}

	protected void act(float delta) {
		if (removeFromWorld) {
			_removeFromWorld();
			removeFromWorld = false;
		}
		if (disposing) {
			_dispose();
			return;
		}
		if (getBody() != null && getBody().isAwake()) {
			applyFriction(delta);
			updateActorGroup();
			updateHudGroup();
		}
	}

	public Vector2 getHeading() {
		return components.get(0).getHeading();
	}

	public void setPositionFull(float x, float y) {
		components.get(0).setPositionFull(x, y);
	}

	public void setPositionFull(float x, float y, float angle) {
		components.get(0).setPositionFull(x, y, angle);
	}

	public void setPositionFull(Vector2 pos) {
		setPositionFull(pos.x, pos.y);
	}

	protected void updateActorGroup() {
		actorGroup.setRotation(getBody().getAngle() * MathUtils.radiansToDegrees);
		actorGroup.setPosition(getBody().getPosition().x - getLeadComponent().getWidth() / 2,
				getBody().getPosition().y - getLeadComponent().getHeight() / 2);
	}

	protected void updateHudGroup() {
		hudGroup.setPosition(getBody().getPosition().x - getLeadComponent().getWidth() / 2,
				getBody().getPosition().y - getLeadComponent().getHeight() / 2);
	}

	public void setPositionFull(Pose pose) {
		setPositionFull(pose.getX(), pose.getY(), pose.getAngle());
	}

	@Override
	public void resolveDmg(Damage dmg) {
		getHpHolder().sub(dmg.getDmg());
	}

	@Override
	public Damage calculateDmg(DmgResolving target, WorldManifold worldManifold) {
		return new Damage(this, 0, worldManifold);
	}

	@Override
	public void dispose() {
		this.disposing = true;
	}

	protected void _dispose() {
		firePropertyChange(PROPERTY_DESTROY_BODY, this, null);
		components.forEach(x -> x._dispose());
		actorGroup.remove();
		actorGroup.clear();
		hudGroup.remove();
		hudGroup.clear();
		aliveHolder.dispose();
		hpHolder.dispose();
	}

	public void addToWorld() {
		components.get(0).addAction(action);
		components.forEach(x -> x.addToWorld());
		GameContext.getGameStage().addActor(actorGroup, getWorldLayer());
		GameContext.getGameStage().addActor(hudGroup, getWorldLayer());
	}

	public void removeFromWorld() {
		this.removeFromWorld = true;
	}

	public void _removeFromWorld() {
		firePropertyChange(PROPERTY_DESTROY_BODY, this, null);
		components.forEach(x -> x._removeFromWorld());
		actorGroup.remove();
		hudGroup.remove();
	}

	public void setCollisionFilter(short category, short mask, short group) {
		components.forEach(x -> x.setCollisionFilter(category, mask, group));
	}

	public void setCollisionFilter(short category, short mask) {
		setCollisionFilter(category, mask, CollisionFilters.DEFAULT_GROUP);
	}

	public void setCollisionFilter(short category) {
		setCollisionFilter(category, CollisionFilters.INACTIVE_MASK, CollisionFilters.DEFAULT_GROUP);
	}

	public EntityComponent getLeadComponent() {
		return components.get(0);
	}

	public boolean checkPosition(Vector2 targetPos) {
		return !GameContext.getGameStage().getTileLayer().getMap().getCollisionMap()
				.getTile(TPos.fromWorldPos(targetPos));
	}

	public abstract int getWorldLayer();

	@Override
	public Body getBody() {
		return components.get(0).getBody();
	}

	@Override
	public int getZIndex() {
		return components.get(0).getZIndex();
	}

	@Override
	public float getRadius() {
		return (getLeadComponent().getActorSize().x + getLeadComponent().getActorSize().y) / 2f;
	}

	@AllArgsConstructor
	public class EntityAction extends Action {

		Entity owner;

		@Override
		public boolean act(float delta) {
			owner.act(delta);
			return owner.isDisposing();
		}

	}

}
