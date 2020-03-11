package com.kumascave.games.teeth_of_doom.core.entity.mobs.antropomorph;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.google.common.collect.Lists;
import com.kumascave.games.teeth_of_doom.core.entity.item.Equippable;
import com.kumascave.games.teeth_of_doom.core.entity.item.Item;
import com.kumascave.games.teeth_of_doom.core.entity.item.ItemState;
import com.kumascave.games.teeth_of_doom.core.entity.item.Equippable.EquipmentSlot;
import com.kumascave.games.teeth_of_doom.core.entity.item.Equippable.EquipmentSlotType;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.Mob;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.antropomorph.Hand.HandType;
import com.kumascave.games.teeth_of_doom.core.physics.Friction;
import com.kumascave.games.teeth_of_doom.core.physics.Pose;
import com.kumascave.games.teeth_of_doom.core.physics.shapes.Circle;

import lombok.Builder;
import lombok.Getter;

public class Human extends Mob {

	private static final List<EquipmentSlot> EQUIPMENT_SLOTS = Lists.newArrayList(
			new EquipmentSlot(EquipmentSlotType.HAND, 0), new EquipmentSlot(EquipmentSlotType.HAND, 1),
			new EquipmentSlot(EquipmentSlotType.BODY, 0));

	private float armLength = 0.6f;

	@Getter
	private final Inventory inventory;

	@Getter
	private final EquipmentHolder equipmentHolder;

	@Getter
	private final Hand leftHand;
	@Getter
	private final Hand rightHand;

	@Builder
	public Human(float diam, Pose pose, float density, Friction friction) {
		super(new Vector2(diam, diam), pose, new Circle(diam), density, friction, 0.0f);
		this.equipmentHolder = new EquipmentHolder(this, EQUIPMENT_SLOTS);
		this.leftHand = new Hand(this, armLength, HandType.LEFT);
		this.rightHand = new Hand(this, armLength, HandType.RIGHT);
		this.inventory = new Inventory(this);
		this.setOrigin(Align.center);
	}

	public Equippable equip(EquipmentSlot slot, Equippable equipment) {
		return equipmentHolder.equip(slot, equipment);
	}

	public Equippable unequip(EquipmentSlot slot) {
		return equip(slot, null);
	}

	public void addToInventory(Item item) {
		item.requireSwitchState(ItemState.INVENTORY);
		inventory.addItem(item);
	}

}
