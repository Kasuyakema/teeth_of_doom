package com.kumascave.games.teeth_of_doom.core.entity.item;

import com.kumascave.games.teeth_of_doom.core.entity.mobs.antropomorph.Human;
import com.kumascave.games.teeth_of_doom.core.world.Duo;

public interface Equippable {

	default void onEquip(Human user) {
		// do nothing
	}

	default void onUnequip() {
		// do nothing
	}

	public EquipmentSlotType getEquipmentSlotType();

	enum EquipmentSlotType {
		HAND, BODY, LEGS, RING, AMULET;
	}

	class EquipmentSlot extends Duo<EquipmentSlotType, Integer> {
		public EquipmentSlot(EquipmentSlotType a, Integer b) {
			super(a, b);
		}
	}
}
