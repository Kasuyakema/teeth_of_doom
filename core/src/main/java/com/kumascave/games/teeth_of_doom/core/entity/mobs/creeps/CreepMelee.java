package com.kumascave.games.teeth_of_doom.core.entity.mobs.creeps;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.kumascave.games.teeth_of_doom.FirstGame;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.Mob;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.ObservedArea;
import com.kumascave.games.teeth_of_doom.core.mechanics.alignment.Aligned;
import com.kumascave.games.teeth_of_doom.core.mechanics.alignment.Alignment;
import com.kumascave.games.teeth_of_doom.core.mechanics.damage.ContactResolving;
import com.kumascave.games.teeth_of_doom.core.mechanics.damage.DmgResolving;
import com.kumascave.games.teeth_of_doom.core.physics.shapes.Rectangle;
import com.kumascave.games.teeth_of_doom.core.ui.actors.BlendingImage;
import com.kumascave.games.teeth_of_doom.core.world.CollisionFilters;

public class CreepMelee extends ObservedArea {

	BlendingImage image;

	public CreepMelee(Mob mob) {
		super(mob, new Rectangle(Creep.meleeRange, Creep.meleeRange,
				new Vector2(Creep.radius + Creep.meleeRange / 2f, 0f), 0f));
		fixtureDef.filter.categoryBits = CollisionFilters.BASE_CATEGORY;
		fixtureDef.filter.maskBits = CollisionFilters.MOB_CATEGORY_BASE;
		image = new BlendingImage(FirstGame.gameSkin, "red");
		image.setAlign(Align.center);
		image.setScaling(Scaling.fit);
		image.setSize(Creep.meleeRange, Creep.meleeRange);
		image.setOrigin(Creep.meleeRange / 2, Creep.meleeRange / 2);
		mob.getActorGroup().addActor(image);
		image.setPosition(Creep.radius * 2f, Creep.radius - Creep.meleeRange / 2f);
		image.setAlpha(0.5f);
		image.setVisible(false);
	}

	@Override
	public void resolveContact(ContactResolving contact) {
		if (contact instanceof DmgResolving) {
			if (contact instanceof Aligned) {
				if (((Aligned) contact).getAlignment().equals(Alignment.ALLY)) {
					observed.add(contact);
				}
			} else {
				observed.add(contact);
			}
		}
	}

	public void setVisible(boolean visible) {
		image.setVisible(visible);
	}

	public void playAnimation() {
		// TODO
	}

}
