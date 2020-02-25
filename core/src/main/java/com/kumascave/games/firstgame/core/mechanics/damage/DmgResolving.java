package com.kumascave.games.firstgame.core.mechanics.damage;

import java.util.Map;
import java.util.UUID;

import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.kumascave.games.firstgame.core.Constants;

public interface DmgResolving extends ContactResolving {

	public void resolveDmg(Damage dmg);

	public default Damage getDmg(DmgResolving target, WorldManifold worldManifold) {
		Long t = System.currentTimeMillis();
		if (collisionOnCooldown(target.getId(), t)) {
			return new Damage(this, 0, worldManifold);
		} else {
			getCollisionCooldowns().put(target.getId(), System.currentTimeMillis());
			setLastCollisionTime(t);
			return calculateDmg(target, worldManifold);
		}
	}

	public Damage calculateDmg(DmgResolving target, WorldManifold worldManifold);

	public Map<UUID, Long> getCollisionCooldowns();

	public default boolean collisionOnCooldown(UUID id, Long t) {
		if (getLastCollisionTime() == null || (t - getLastCollisionTime()) / 1000.0f > Constants.CONTACT_SLEEP) {
			getCollisionCooldowns().clear();
			setLastCollisionTime(null);
			return false;
		}
		Long collisionTime = getCollisionCooldowns().get(id);
		if (collisionTime == null) {
			return false;
		}
		if ((t - collisionTime) / 1000.0f > Constants.CONTACT_SLEEP) {
			return false;
		}
		return true;
	}

	public void setLastCollisionTime(Long t);

	public Long getLastCollisionTime();
}
