package com.kumascave.games.firstgame.atest;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kumascave.games.firstgame.AppContext;

/**
 * Created by julienvillegas on 01/02/2017.
 */

public class Floor extends Image {

	private Body body;
	private World world;

	public Floor(World aWorld, float pos_x, float pos_y, float aWidth, float aHeight, float angle) {
		super(AppContext.inst().getAssetManager().get("naturalstone.jpg", Texture.class));
		this.setSize(aWidth, aHeight);
		this.setOrigin(this.getWidth() / 2, this.getHeight() / 2);
		this.rotateBy(angle);
		this.setPosition(pos_x, pos_y);
		world = aWorld;
		BodyDef groundBodyDef = new BodyDef();
		// Set its world position
		groundBodyDef.position.set(new Vector2(pos_x, pos_y));

		// Create a body from the defintion and add it to the world
		body = world.createBody(groundBodyDef);

		// Create a polygon shape
		PolygonShape groundBox = new PolygonShape();

		// (setAsBox takes half-width and half-height as arguments)
		groundBox.setAsBox(this.getWidth() / 2, this.getHeight() / 2);
		body.setTransform(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2,
				(float) Math.toRadians(angle));

		// Create a fixture from our polygon shape and add it to our ground body
		body.createFixture(groundBox, 0.0f);
		// Clean up after ourselves
		groundBox.dispose();
	}

	private float t = 2;
	private float n = 0;

	@Override
	public void act(float delta) {
		super.act(delta);
		// Ball dispenser XD
		// if (n <= 0) {
		// Ball ball = new Ball(world, 0.05f, getX(), getY() + 1);
		// ball.getBody().applyLinearImpulse(new Vector2(0, 0.5f),
		// ball.getBody().getWorldCenter(), true);
		// n = t;
		// } else {
		// n -= delta;
		// }
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}
}
