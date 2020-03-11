package com.kumascave.games.teeth_of_doom.core.mechanics.damage;

import com.badlogic.gdx.physics.box2d.WorldManifold;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Damage {

	DmgResolving source;
	int dmg;
	WorldManifold worldManifold;

	// public Damage(DmgResolving source, int dmg) {
	// this(source, dmg, null);
	// }

	public void addDmg(int additionalDmg) {
		setDmg(getDmg() + additionalDmg);
	}

}
