package com.kumascave.games.firstgame.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.kumascave.games.firstgame.core.physics.PhysicalEntity;
import com.kumascave.games.firstgame.util.jgoodies.TagOnPropertyChangeSupport;

import lombok.Getter;
import lombok.Setter;

public class ManagedJointReference extends Action implements TagOnPropertyChangeSupport {

	public static final String PROPERTY_JOINT = "joint";
	public static final String PROPERTY_DESTROY_JOINT = "destroy_joint";

	@Getter
	@Setter
	protected PropertyChangeSupport changeSupport;

	private PropertyChangeListener listenerA;
	private PropertyChangeListener listenerB;

	PhysicalEntity a;
	PhysicalEntity b;

	@Getter
	private Joint joint;
	private boolean destroy = false;

	public ManagedJointReference(Joint joint) {
		super();
		setJoint(joint);
		GameContext.inst().getGameStage().addAction(this);
	}

	public void setJoint(Joint joint) {
		if (getJoint() != null) {
			removeListeners();
		}
		if (joint != null) {
			if (joint.getBodyA().getUserData() instanceof PhysicalEntity) {
				a = (PhysicalEntity) joint.getBodyA().getUserData();
				listenerA = (evt) -> onJointDestruction();
				a.addDestructionListener(listenerA);
			}
			if (joint.getBodyB().getUserData() instanceof PhysicalEntity) {
				b = (PhysicalEntity) joint.getBodyB().getUserData();
				listenerB = (evt) -> onJointDestruction();
				b.addDestructionListener(listenerB);
			}
		}
		firePropertyChange(PROPERTY_JOINT, getJoint(), joint);
		this.joint = joint;
	}

	private void removeListeners() {
		if (a != null) {
			a.removePropertyChangeListener(listenerA);
			a = null;
		}
		if (b != null) {
			b.removePropertyChangeListener(listenerB);
			b = null;
		}
	}

	public void destroyJoint() {
		if (joint == null) {
			return;
		}
		destroy = true;
	}

	private void _destroy() {
		Joint oldJoint = getJoint();
		onJointDestruction();
		GameContext.inst().getWorld().destroyJoint(oldJoint);
	}

	private void onJointDestruction() {
		removeListeners();
		joint = null;
		firePropertyChange(PROPERTY_DESTROY_JOINT, this, null);
		firePropertyChange(PROPERTY_JOINT, this, null);
	}

	public void addDestructionListener(PropertyChangeListener listener) {
		addPropertyChangeListener(PROPERTY_DESTROY_JOINT, listener);
	}

	@Override
	public boolean act(float delta) {
		if (destroy) {
			_destroy();
			destroy = false;
		}
		return false;
	}

}
