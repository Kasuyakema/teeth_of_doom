package com.kumascave.games.teeth_of_doom.core.ui.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;

import lombok.Getter;
import lombok.Setter;

public class BlendingImage extends Image {

	@Getter
	@Setter
	float alpha;

	public BlendingImage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BlendingImage(Drawable drawable, Scaling scaling, int align) {
		super(drawable, scaling, align);
		// TODO Auto-generated constructor stub
	}

	public BlendingImage(Drawable drawable, Scaling scaling) {
		super(drawable, scaling);
		// TODO Auto-generated constructor stub
	}

	public BlendingImage(Drawable drawable) {
		super(drawable);
		// TODO Auto-generated constructor stub
	}

	public BlendingImage(NinePatch patch) {
		super(patch);
		// TODO Auto-generated constructor stub
	}

	public BlendingImage(Skin skin, String drawableName) {
		super(skin, drawableName);
		// TODO Auto-generated constructor stub
	}

	public BlendingImage(Texture texture) {
		super(texture);
		// TODO Auto-generated constructor stub
	}

	public BlendingImage(TextureRegion region) {
		super(region);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, alpha);
	}
}
