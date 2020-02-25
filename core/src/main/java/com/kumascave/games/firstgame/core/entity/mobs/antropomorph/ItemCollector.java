package com.kumascave.games.firstgame.core.entity.mobs.antropomorph;

import java.util.UUID;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.kumascave.games.firstgame.core.entity.item.Item;
import com.kumascave.games.firstgame.core.entity.item.ItemState;
import com.kumascave.games.firstgame.core.mechanics.damage.ContactResolving;
import com.kumascave.games.firstgame.core.physics.shapes.Circle;
import com.kumascave.games.firstgame.core.world.CollisionFilters;

public class ItemCollector implements ContactResolving {

	Human owner;

	FixtureDef fixtureDef;

	public ItemCollector(Human owner) {
		super();
		this.owner = owner;

		fixtureDef = new FixtureDef();
		fixtureDef.shape = new Circle(owner.getWidth());
		fixtureDef.isSensor = true;
		fixtureDef.filter.categoryBits = CollisionFilters.ITEM_COLLECTOR_CATEGORY;
		fixtureDef.filter.maskBits = CollisionFilters.ITEM_COLLECTOR_MASK;
	}

	@Override
	public UUID getId() {
		return owner.getId();
	}

	@Override
	public void resolveContact(ContactResolving contact) {
		if (contact instanceof Item && ((Item) contact).getState().equals(ItemState.FREE)) {
			owner.addToInventory((Item) contact);
		}
		ContactResolving.super.resolveContact(contact);
	}

	public void addSensor(Body body) {
		Fixture sensor = body.createFixture(fixtureDef);
		sensor.setUserData(this);
	}

}
