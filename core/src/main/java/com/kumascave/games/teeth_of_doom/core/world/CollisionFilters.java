package com.kumascave.games.teeth_of_doom.core.world;

import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;

//A method explaining collision filtering:
//public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
//    Filter filterA = fixtureA.getFilterData();
//    Filter filterB = fixtureB.getFilterData();
//
//    if (filterA.groupIndex == filterB.groupIndex && filterA.groupIndex != 0) {
//        return filterA.groupIndex > 0;
//    }
//
//    boolean collide = (filterA.maskBits & filterB.categoryBits) != 0
//            && (filterA.categoryBits & filterB.maskBits) != 0;
//    return collide;
//}
public abstract class CollisionFilters {

	// everything
	public static final short BASE_CATEGORY = 1;
	// Big things. Boulders, Walls
	public static final short BIG_CATEGORY_BASE = 2;
	// Small Items, Things flat on the ground
	public static final short GROUND_CATEGORY_BASE = 4;
	// Living things (probably bigger than mice)
	public static final short MOB_CATEGORY_BASE = 8;
	// Active Weapons, swinging swords, etc
	public static final short WEAPON_CATEGORY_BASE = 16;
	// Needs to be in every non Sensor Mask
	public static final short SENSOR_CATEGORY_BASE = 32;

	public static final short SENSOR_CATEGORY = BASE_CATEGORY | SENSOR_CATEGORY_BASE;
	public static final short BIG_CATEGORY = BASE_CATEGORY | BIG_CATEGORY_BASE;
	public static final short GROUND_CATEGORY = BASE_CATEGORY | GROUND_CATEGORY_BASE;
	public static final short MOB_CATEGORY = BASE_CATEGORY | MOB_CATEGORY_BASE;
	public static final short ALL_CATEGORY = BASE_CATEGORY | BIG_CATEGORY_BASE | GROUND_CATEGORY_BASE
			| MOB_CATEGORY_BASE | WEAPON_CATEGORY_BASE;

	// public static final short PROJECTILE_CATEGORY = BASE_CATEGORY |
	// PROJECTILE_CATEGORY_BASE;

	// -----------------Mask bits---------------------

	public static final short INACTIVE_MASK = -1;

	public static final short COLLISION_DEACTIVATED_MASK = 0;

	public static final short MOB_MASK = SENSOR_CATEGORY_BASE | BIG_CATEGORY_BASE | MOB_CATEGORY_BASE
			| WEAPON_CATEGORY_BASE;

	public static final short SMALL_ITEM_MASK = SENSOR_CATEGORY_BASE | BIG_CATEGORY_BASE;

	public static final short ITEM_COLLECTOR_MASK = GROUND_CATEGORY_BASE;

	public static final short WEAPON_MASK = SENSOR_CATEGORY_BASE | BIG_CATEGORY_BASE | MOB_CATEGORY_BASE
			| WEAPON_CATEGORY_BASE;

	// -----------------Group bits---------------------

	public static final short DEFAULT_GROUP = 0;

	public static final short PROJECTILE_GROUP = -1;

	public static final short BALLGAME_GROUP = 10;

	public static final short FRIENDLY_GROUP = -10;

	public static boolean collides(Fixture fixtureA, Fixture fixtureB) {
		Filter filterA = fixtureA.getFilterData();
		Filter filterB = fixtureB.getFilterData();

		if (filterA.groupIndex == filterB.groupIndex && filterA.groupIndex != 0) {
			return filterA.groupIndex > 0;
		}

		boolean collide = (filterA.maskBits & filterB.categoryBits) != 0
				&& (filterA.categoryBits & filterB.maskBits) != 0;
		return collide;
	}

}
