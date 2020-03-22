package com.kumascave.games.teeth_of_doom.core.ui.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.kumascave.games.teeth_of_doom.AppContext;
import com.kumascave.games.teeth_of_doom.core.entity.Entity;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.antropomorph.Player;
import com.kumascave.games.teeth_of_doom.core.mechanics.alignment.Aligned;

public class HPBar<P extends Entity> extends Image implements Disposable {
	private P parent;

	private String textureNeutral = "hpBarNeutral.png";
	private String textureAlly = "hpBarAlly.png";
	private String textureHostile = "hpBarHostile.png";
	private String texturePlayer = "hpBarPlayer.png";

	private boolean dispose = false;

	public HPBar(P parent) {
		super();
		String texture = textureNeutral;
		if (parent instanceof Player) {
			texture = texturePlayer;
		} else if (parent instanceof Aligned) {
			((Aligned) parent).getAlignmentHolder().addValueChangeListener(evt -> updateTexture());
			switch (((Aligned) parent).getAlignment()) {
			case ALLY:
				texture = textureAlly;
				break;
			case HOSTILE:
				texture = textureHostile;
				break;
			case NEUTRAL:
				texture = textureNeutral;
				break;
			default:
				break;
			}
		}
		setDrawable(new TextureRegionDrawable(
				new TextureRegion(AppContext.inst().getAssetManager().get(texture, Texture.class))));

		this.parent = parent;
		this.setSize(parent.getActorGroup().getWidth(), 0.02f);
		this.setOrigin(Align.center);
		parent.getHudGroup().addActor(this);

		setPosition(parent.getHudGroup().getWidth() - getWidth(), parent.getHudGroup().getHeight() - getHeight() / 2f);

		parent.getHpHolder().addValueChangeListener(evt -> updateHp());
		parent.getHpMaxHolder().addValueChangeListener(evt -> updateHp());
	}

	private void updateTexture() {
		String texture = textureNeutral;
		switch (((Aligned) parent).getAlignment()) {
		case ALLY:
			texture = textureAlly;
			break;
		case HOSTILE:
			texture = textureHostile;
			break;
		case NEUTRAL:
			texture = textureNeutral;
			break;
		default:
			break;
		}
		setDrawable(new TextureRegionDrawable(
				new TextureRegion(AppContext.inst().getAssetManager().get(texture, Texture.class))));
	}

	@Override
	public void act(float delta) {
		if (dispose) {
			_dispose();
		}

		super.act(delta);
	}

	private void updateHp() {
		this.setSize(parent.getComponents().get(0).getWidth() * parent.getHpHolder().getPercentage(), 0.02f);
	}

	@Override
	public void dispose() {
		this.dispose = true;
	}

	private void _dispose() {
		Group parent = this.getParent();
		if (parent != null) {
			parent.removeActor(this);
		}
	}
}
