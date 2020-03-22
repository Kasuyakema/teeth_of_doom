package com.kumascave.games.teeth_of_doom.core.physics;

import java.beans.PropertyChangeListener;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.kumascave.games.teeth_of_doom.util.jgoodies.TagOnPropertyChangeSupport;

public interface PhysicalEntity extends TagOnPropertyChangeSupport {

	public static final String PROPERTY_DESTROY_BODY = "destroy_body";

	Body getBody();

	Friction getFriction();

	default void applyFriction(float deltaT) {

		applyLinearFriction(deltaT);
		applyAngularFriction(deltaT);

	}

	default void applyAngularFriction(float deltaT) {
		// Angular Bohrreibung
		float ballparkRadius = getBody().getFixtureList().get(0).getShape().getRadius();
		float fBohr = getFriction().getAngularFriction() * getBody().getMass() * ballparkRadius;
		float curVel = getBody().getAngularVelocity();
		float wBohr;

		float fMax;
		// apply friction to full velocity

		// assuming everything is a Disk. If it does not work try 7/12 instead of 1/4
		fMax = curVel * getBody().getMass() * ballparkRadius * ballparkRadius / (deltaT * 4);

		if (fBohr > fMax) {
			wBohr = -fMax;
		} else {
			wBohr = -fBohr;
		}
		getBody().applyTorque(wBohr, true);
	}

	// Linar Gleitreibung
	default void applyLinearFriction(float deltaT) {
		float fGleit = getFriction().getLinearFriction() * getBody().getMass();
		Vector2 curVel = getBody().getLinearVelocity();
		Vector2 wGleit;
		float fMax;
		// apply friction to full velocity
		wGleit = new Vector2(-curVel.x, -curVel.y);
		fMax = curVel.len() * getBody().getMass() / deltaT;

		if (fGleit > fMax) {
			wGleit.setLength(fMax);
		} else {
			wGleit.setLength(fGleit);
		}
		getBody().applyForceToCenter(wGleit, true);
	}

	default public Pose getPose() {
		return new Pose(getBody().getPosition(), getBody().getAngle());
	}

	default public float distanceToCenter(Vector2 other) {
		return getPose().getPos().cpy().sub(other).len();
	}

	default public float distanceTo(Vector2 other) {
		return distanceToCenter(other) - getRadius();
	}

	default public float distanceBetweenCenters(PhysicalEntity other) {
		return getPose().getPos().cpy().sub(other.getPose().getPos()).len();
	}

	default public float distanceTo(PhysicalEntity other) {
		return distanceBetweenCenters(other) - getRadius() - other.getRadius();
	}

	default public void addDestructionListener(PropertyChangeListener listener) {
		addPropertyChangeListener(PROPERTY_DESTROY_BODY, listener);
	}

	public int getZIndex();

	float getRadius();
}
