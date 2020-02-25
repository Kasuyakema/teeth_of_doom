package com.kumascave.games.firstgame.core.entity.mobs.antropomorph;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kumascave.games.firstgame.core.GameContext;
import com.kumascave.games.firstgame.core.ManagedJointReference;
import com.kumascave.games.firstgame.core.entity.StateMachine;
import com.kumascave.games.firstgame.core.entity.Transition;
import com.kumascave.games.firstgame.core.entity.WithStateMachine;
import com.kumascave.games.firstgame.core.entity.item.Equippable.EquipmentSlot;
import com.kumascave.games.firstgame.core.entity.item.Equippable.EquipmentSlotType;
import com.kumascave.games.firstgame.core.entity.item.Handheld;
import com.kumascave.games.firstgame.core.entity.item.HandheldState;
import com.kumascave.games.firstgame.core.entity.item.Item;
import com.kumascave.games.firstgame.core.physics.PhysicalEntity;

import lombok.Getter;

public class Hand implements WithStateMachine {
	@Getter
	private HandType type;
	private Human owner;
	private float armLength;
	protected ManagedJointReference grabbedObject = new ManagedJointReference(null);
	@Getter
	protected Handheld equippedObject;

	@Getter
	protected HandStateMachine stateMachine = new HandStateMachine();

	public Hand(Human owner, float armLength, HandType type) {
		super();
		this.owner = owner;
		this.armLength = armLength;
		this.type = type;
		owner.getEquipmentHolder().addPropertyChangeListener(equipmentSlotFromHandType(type).toString(),
				(evt) -> handleEquipEvent(evt));
		grabbedObject.addDestructionListener((evt) -> onGrabbedObjectDestruction());
	}

	private void handleEquipEvent(PropertyChangeEvent evt) {
		if (evt.getOldValue() != null) {
			requireSwitchState(HandState.EMPTY);
		}
		if (evt.getNewValue() != null) {
			equip((Handheld) evt.getNewValue());
		}
	}

	private void equip(Handheld equippable) {
		if (getState().equals(HandState.EQUIPPED)) {
			requireSwitchState(HandState.EMPTY);
		}
		requireSwitchState(HandState.EQUIPPED, equippable);
	}

	protected void throwAway(float impulseAbs) {
		Body target;
		if (isInState(HandState.GRABBED)) {
			target = grabbedObject.getJoint().getBodyB();
		} else {
			target = equippedObject.getBody();
			equippedObject.requireSwitchState(HandheldState.THROWN);
		}
		release();
		Vector2 impulse = new Vector2(target.getWorldCenter()).sub(owner.getBody().getWorldCenter());
		impulse.setLength(impulseAbs);
		target.applyLinearImpulse(impulse, target.getWorldCenter(), true);
	}

	public void release() {
		grabbedObject.destroyJoint();
		owner.unequip(equipmentSlotFromHandType(type));
		// equippedObject = null via listener
	}

	public void grab(Vector2 pos) {
		// Dont use owner Center but "shoulder"?
		if (new Vector2(pos).sub(owner.getBody().getWorldCenter()).len() > armLength) {
			// out of reach
			return;
		}
		Actor target = GameContext.inst().getGameStage().hit(pos.x, pos.y, false);
		if (owner.equals(target)) {
			// Dont touch yourself ;)
			return;
		}
		if (target instanceof Handheld) {
			owner.equip(equipmentSlotFromHandType(type), (Handheld) target);
			// stateSwitch -> equipped via listener
			return;
		}
		if (target instanceof Item) {
			owner.addToInventory((Item) target);
			return;
		}
		if (target instanceof PhysicalEntity) {
			switchState(HandState.GRABBED, pos, target);
			return;
		}
		return;
	}

	protected void grabObject(Vector2 pos, PhysicalEntity target) {
		DistanceJointDef def = new DistanceJointDef();
		Body targetBod = target.getBody();
		def.initialize(owner.getBody(), targetBod, owner.getBody().getPosition(), pos);
		def.frequencyHz = 0;
		def.dampingRatio = 1;
		def.collideConnected = true;
		grabbedObject.setJoint(GameContext.inst().getWorld().createJoint(def));
	}

	private void onGrabbedObjectDestruction() {
		stateMachine.requireTransition(HandState.EMPTY);
	}

	public static EquipmentSlot equipmentSlotFromHandType(HandType type) {
		switch (type) {
		case LEFT:
			return new EquipmentSlot(EquipmentSlotType.HAND, 1);
		case RIGHT:
			return new EquipmentSlot(EquipmentSlotType.HAND, 0);
		default:
			throw new IllegalArgumentException("Cant determine EquipmentSlot for HandType " + type);
		}
	}

	public static HandType handTypeFromEquipmentSlot(EquipmentSlot slot) {
		Integer pos = slot.getB();
		if (slot.getA() != EquipmentSlotType.HAND || pos > 1) {
			throw new IllegalArgumentException("Cant determine Hand Type for slot " + slot);
		}
		if (pos == 0) {
			return HandType.RIGHT;
		}
		return HandType.LEFT;
	}

	public enum HandType {
		LEFT, RIGHT;
	}

	class HandStateMachine extends StateMachine<Hand> {

		HandStateMachine() {
			this.setSubject(Hand.this);

			this.setState(HandState.EMPTY);

			List<Transition> transitionz = new ArrayList<>();

			transitionz
					.add(new Transition(HandState.EMPTY, HandState.GRABBED, () -> getSubject().equippedObject == null,
							args -> grabObject((Vector2) args[0], (PhysicalEntity) args[1])));
			transitionz.add(new Transition(HandState.GRABBED, HandState.EMPTY,
					() -> getSubject().equippedObject == null, args -> release()));

			transitionz.add(new Transition(HandState.EMPTY, HandState.EQUIPPED,
					args -> getSubject().equippedObject = (Handheld) args[0]));
			transitionz.add(
					new Transition(HandState.EQUIPPED, HandState.EMPTY, args -> getSubject().equippedObject = null));

			transitionz.add(new Transition(HandState.GRABBED, HandState.THROW,
					() -> getSubject().equippedObject == null, args -> {
						throwAway((float) args[0]);
						requireTransition(HandState.EMPTY);
					}));
			Transition throwToEmpty = new Transition(HandState.THROW, HandState.EMPTY,
					() -> getSubject().equippedObject == null);

			transitionz.add(new Transition(HandState.EQUIPPED, HandState.THROW, args -> throwAway((float) args[0]),
					throwToEmpty));

			transitionz.add(throwToEmpty);

			this.addTransitions(transitionz);
		}

	}
}
