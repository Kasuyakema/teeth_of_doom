package com.kumascave.games.firstgame.core.entity.mobs.antropomorph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import com.jgoodies.binding.beans.Model;
import com.kumascave.games.firstgame.core.entity.item.Equippable;
import com.kumascave.games.firstgame.core.entity.item.Equippable.EquipmentSlot;

public class EquipmentHolder extends Model {

	private static final long serialVersionUID = 5771746001390173883L;
	Map<EquipmentSlot, Equippable> equipmentMap;
	private Human owner;

	public EquipmentHolder(Human owner, List<EquipmentSlot> slots) {
		equipmentMap = new HashMap<>();
		slots.forEach(x -> equipmentMap.put(x, null));
		this.owner = owner;
	}

	public Equippable equip(EquipmentSlot slot, Equippable equipment) {
		Equippable oldEquipment = equipmentMap.put(slot, equipment);
		if (equipment != oldEquipment) {
			if (equipment != null) {
				equipment.onEquip(owner);
			}
			if (oldEquipment != null) {
				oldEquipment.onUnequip();
			}
			firePropertyChange(slot.toString(), oldEquipment, equipment);
		}
		return oldEquipment;
	}

	public Equippable unequip(EquipmentSlot slot) {
		return equip(slot, null);
	}

	public EquipmentSlot getSlot(Equippable candidate) {
		Optional<Entry<EquipmentSlot, Equippable>> entry = equipmentMap.entrySet().stream()
				.filter(x -> x.getValue() == candidate).findFirst();
		if (entry.isPresent()) {
			return entry.get().getKey();
		} else {
			return null;
		}
	}
}
