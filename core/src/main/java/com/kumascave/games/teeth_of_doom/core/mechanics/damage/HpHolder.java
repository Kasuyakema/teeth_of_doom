package com.kumascave.games.teeth_of_doom.core.mechanics.damage;

import java.beans.PropertyChangeEvent;

import com.kumascave.games.teeth_of_doom.util.jgoodies.VHolder;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class HpHolder extends VHolder<Integer> {
	private static final long serialVersionUID = -5933821472424923603L;
	HasHitpoints parent;

	public HpHolder(HasHitpoints parent) {
		super(parent.getHpMaxHolder().getValue());
		this.parent = parent;
		this.parent.getHpMaxHolder().addValueChangeListener(evt -> updateHp(evt));
	}

	private void updateHp(PropertyChangeEvent evt) {
		int oldMax = (int) evt.getOldValue();
		int newMax = (int) evt.getNewValue();

		if (newMax < getValue()) {
			setValue(newMax);
		}
		if (newMax > oldMax) {
			add(newMax - oldMax);
		}
	}

	public int sub(int dmg) {
		int newValue = Math.max(getValue() - dmg, 0);
		if (newValue == 0) {
			parent.getAliveHolder().setValue(false);
		}
		setValue(newValue);
		return newValue;
	}

	public int add(int heal) {
		int newValue = Math.min(getValue() + heal, parent.getHpMaxHolder().getValue());
		if (newValue > 0) {
			parent.getAliveHolder().setValue(true);
		}
		setValue(newValue);
		return newValue;
	}

	public float getPercentage() {
		return (float) getValue() / (float) parent.getHpMaxHolder().getValue();
	}
}
