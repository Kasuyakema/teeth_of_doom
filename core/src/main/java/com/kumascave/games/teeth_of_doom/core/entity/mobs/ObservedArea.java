package com.kumascave.games.teeth_of_doom.core.entity.mobs;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.kumascave.games.teeth_of_doom.core.mechanics.damage.ContactResolving;

import lombok.Getter;

public abstract class ObservedArea implements ContactResolving {

	protected Mob owner;

	protected FixtureDef fixtureDef;

	@Getter
	protected Set<ContactResolving> observed = new HashSet<>();

	public ObservedArea(Mob mob, Shape shape) {
		super();
		this.owner = mob;
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.isSensor = true;
	}

	@Override
	public UUID getId() {
		return owner.getId();
	}

	@Override
	public void resolveContact(ContactResolving contact) {
		observed.add(contact);
	}

	@Override
	public void resolveContactEnd(ContactResolving contact) {
		observed.remove(contact);
	}

	public void addSensor(Body body) {
		Fixture sensor = body.createFixture(fixtureDef);
		sensor.setUserData(this);
	}

	public boolean contains(ContactResolving object) {
		return observed.contains(object);
	}

}
