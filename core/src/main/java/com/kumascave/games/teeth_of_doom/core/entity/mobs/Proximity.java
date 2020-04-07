package com.kumascave.games.teeth_of_doom.core.entity.mobs;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import com.google.common.primitives.Floats;
import com.kumascave.games.teeth_of_doom.core.mechanics.alignment.Aligned;
import com.kumascave.games.teeth_of_doom.core.mechanics.alignment.Alignment;
import com.kumascave.games.teeth_of_doom.core.mechanics.damage.ContactResolving;
import com.kumascave.games.teeth_of_doom.core.physics.PhysicalEntity;
import com.kumascave.games.teeth_of_doom.core.physics.shapes.Circle;
import com.kumascave.games.teeth_of_doom.core.world.CollisionFilters;

public class Proximity extends ObservedArea {

	public Proximity(Mob mob) {
		super(mob, new Circle(3.0f));

		fixtureDef.filter.categoryBits = CollisionFilters.SENSOR_CATEGORY;
		fixtureDef.filter.maskBits = CollisionFilters.MOB_CATEGORY_BASE;
	}

	public Set<ContactResolving> getHostiles() {
		Set<Alignment> targetAlignments = Sets.newHashSet(Alignment.values());
		targetAlignments.remove(owner.getAlignment());
		targetAlignments.remove(Alignment.NEUTRAL);

		return observed.stream()
				.filter(x -> x instanceof Aligned && targetAlignments.contains(((Aligned) x).getAlignment()))
				.collect(Collectors.toSet());
	}

	public Optional<PhysicalEntity> getClosestHostilePhysicalEntity() {
		return getHostiles().stream().filter(x -> x instanceof PhysicalEntity).map(x -> ((PhysicalEntity) x))
				.sorted((x, y) -> Floats.compare(x.distanceTo(owner), y.distanceTo(owner))).findFirst();
	}

}
