package com.kumascave.games.firstgame.core.entity.item;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Shape;
import com.kumascave.games.firstgame.core.entity.Entity;
import com.kumascave.games.firstgame.core.entity.StateMachine;
import com.kumascave.games.firstgame.core.entity.Transition;
import com.kumascave.games.firstgame.core.entity.WithStateMachine;
import com.kumascave.games.firstgame.core.physics.Friction;
import com.kumascave.games.firstgame.core.physics.Pose;
import com.kumascave.games.firstgame.core.world.CollisionFilters;
import com.kumascave.games.firstgame.core.world.LayeredStage;

import lombok.Getter;
import lombok.Setter;

public abstract class Item extends Entity implements WithStateMachine {

	@SuppressWarnings("rawtypes")
	@Getter
	@Setter
	protected StateMachine stateMachine = new ItemStateMachine<Item>(this);

	protected Item(Vector2 actorSize, Pose startingOdom, Shape shape, BodyType bodyType, float density,
			Friction friction, float restitution) {
		super(actorSize, startingOdom, shape, bodyType, density, friction, restitution);
		fixtureDef.filter.categoryBits = CollisionFilters.GROUND_CATEGORY;
		fixtureDef.filter.maskBits = CollisionFilters.SMALL_ITEM_MASK;
	}

	@Override
	public int getWorldLayer() {
		return LayeredStage.OBJECT_LAYER;
	}

	protected void prepareForInventory() {
		removeFromWorld();
		setPosition(0, 0);
		setRotation(0);
	}

	public class ItemStateMachine<T extends Item> extends StateMachine<T> {

		ItemStateMachine(T subject) {
			this.setSubject(subject);

			this.setState(ItemState.FREE);

			List<Transition> transitionz = new ArrayList<>();

			transitionz.add(
					new Transition(ItemState.FREE, ItemState.INVENTORY, args -> getSubject().prepareForInventory()));
			transitionz.add(new Transition(ItemState.INVENTORY, ItemState.FREE));

			// transitionz.add(new Transition(ItemState.FREE, ItemState.THROWN));
			// transitionz.add(new Transition(ItemState.THROWN, ItemState.FREE));

			this.addTransitions(transitionz);
		}

	}
}
