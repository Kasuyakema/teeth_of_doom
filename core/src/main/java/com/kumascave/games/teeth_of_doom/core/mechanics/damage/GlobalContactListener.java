package com.kumascave.games.teeth_of_doom.core.mechanics.damage;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.kumascave.games.teeth_of_doom.core.world.TiledLayer;
import com.kumascave.games.teeth_of_doom.core.world.tiles.Tile;

public class GlobalContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Object dataA = contact.getFixtureA().getUserData();
		Object dataB = contact.getFixtureB().getUserData();

		DmgResolving dmgResA = castIfDmgResolving(contact, dataA);
		DmgResolving dmgResB = castIfDmgResolving(contact, dataB);

		if (dmgResA != null && dmgResB != null) {
			// System.out.println(a + " hit " + b + " at " +
			// contact.getWorldManifold().getNumberOfContactPoints());
			dmgResA.resolveDmg(dmgResB.getDmg(dmgResA, contact.getWorldManifold()));
			dmgResB.resolveDmg(dmgResA.getDmg(dmgResB, contact.getWorldManifold()));
		}

		ContactResolving resA;
		ContactResolving resB;

		if (dmgResA != null) {
			resA = dmgResA;
		} else {
			resA = castIfResolving(contact, dataA);
		}

		if (dmgResB != null) {
			resB = dmgResB;
		} else {
			resB = castIfResolving(contact, dataB);
		}

		if (resA != null && resB != null) {
			resA.resolveContact(resB);
			resB.resolveContact(resA);
		}
	}

	@Override
	public void endContact(Contact contact) {
		return;
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		return;
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		return;
	}

	private DmgResolving castIfDmgResolving(Contact contact, Object userData) {
		if (userData instanceof DmgResolving) {
			return (DmgResolving) userData;
		} else if (userData instanceof TiledLayer) {
			Tile tile = ((TiledLayer) userData).getMap().getNearestWall(contact.getWorldManifold().getPoints()[0]);
			tile.initStatus();
			return tile.getStatus();
		}
		return null;
	}

	private ContactResolving castIfResolving(Contact contact, Object userData) {
		if (userData instanceof ContactResolving) {
			return (ContactResolving) userData;
		} else if (userData instanceof TiledLayer) {
			Tile tile = ((TiledLayer) userData).getMap().getNearestWall(contact.getWorldManifold().getPoints()[0]);
			tile.initStatus();
			return tile.getStatus();
		}
		return null;
	}
}
