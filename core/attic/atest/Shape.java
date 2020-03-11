/*******************************************************
 * Copyright (C) 2015 Mirco Timmermann - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Mirco Timmermann <mtimmermann@gmx.de>, December 2016
 *
 *******************************************************/
package com.kumascave.games.firstgame.atest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;


/*
 * renders shapes (uses ShapeRenderer. Notice: Too many Shapes can have influence on performance)
 */
public class Shape extends Actor {
	private Graphics _graphics = null;

	private ShapeRenderer _shapeRenderer = null;

	public boolean _enableBlend = false;

	public Shape() {
		this(new ShapeRenderer(), new Graphics());
	}

	public Shape(ShapeRenderer shapeRenderer) {
		this(shapeRenderer, new Graphics());
	}

	public Shape(ShapeRenderer shapeRenderer, Graphics graphics) {
		_shapeRenderer = shapeRenderer;
		_graphics = graphics;
	}

	public Graphics graphics() {
		return _graphics;
	}

	//	Entity e;

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.end();

		if(_enableBlend) {
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		}

		Color tmpColor = _shapeRenderer.getColor();
		_shapeRenderer.setColor(this.getColor());

		_shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		_shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

		_shapeRenderer.translate(getX() + getOriginX(), getY() + getOriginY(), 0);
		_shapeRenderer.rotate(0, 0, 1, getRotation());
		_shapeRenderer.scale(getScaleX(), getScaleY(), 1);
		_shapeRenderer.translate(-getOriginX(), -getOriginY(), 0);

		_graphics.execute(_shapeRenderer);
		//		BodyBuilder
		_shapeRenderer.setColor(tmpColor);

		if(_enableBlend) {
			Gdx.gl.glDisable(GL20.GL_BLEND);
		}

		batch.begin();
	}

	public ShapeRenderer getShapeRenderer() {
		return _shapeRenderer;
	}
}
