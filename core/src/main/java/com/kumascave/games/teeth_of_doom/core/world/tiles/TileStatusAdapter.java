package com.kumascave.games.teeth_of_doom.core.world.tiles;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.kumascave.games.teeth_of_doom.core.mechanics.damage.Damage;
import com.kumascave.games.teeth_of_doom.core.mechanics.damage.DmgResolving;
import com.kumascave.games.teeth_of_doom.core.mechanics.damage.HasHitpoints;
import com.kumascave.games.teeth_of_doom.core.mechanics.damage.HpHolder;
import com.kumascave.games.teeth_of_doom.util.jgoodies.VHolder;

import lombok.Getter;
import lombok.Setter;

public class TileStatusAdapter implements HasHitpoints {

	private static final Integer hpMaxBase = 100;

	@Getter
	private UUID id = UUID.randomUUID();

	@Getter
	private VHolder<Boolean> aliveHolder = new VHolder<>(true);

	@Getter
	private VHolder<Integer> hpMaxHolder = new VHolder<>(hpMaxBase);
	@Getter
	protected HpHolder hpHolder = new HpHolder(this);

	@Getter
	private Map<UUID, Long> collisionCooldowns = new HashMap<>();

	@Setter
	@Getter
	private Long lastCollisionTime = null;

	Tile tile;

	@Override
	public void resolveDmg(Damage dmg) {
		getHpHolder().sub(dmg.getDmg());
		if (this.hpHolder.getValue() == 0) {
			tile.destroy();
		}
	}

	@Override
	public Damage calculateDmg(DmgResolving target, WorldManifold worldManifold) {
		return new Damage(this, 0, worldManifold);
	}

	public TileStatusAdapter(Tile tile) {
		super();
		this.tile = tile;
	}

}
