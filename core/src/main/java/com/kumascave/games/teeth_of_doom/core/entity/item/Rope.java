package com.kumascave.games.teeth_of_doom.core.entity.item;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.kumascave.games.teeth_of_doom.core.GameContext;
import com.kumascave.games.teeth_of_doom.core.physics.Pose;
import com.kumascave.games.teeth_of_doom.core.physics.WorldUtil;
import com.kumascave.games.teeth_of_doom.util.Util;

import lombok.Getter;

public class Rope {

	private static Vector2 elementSize = new Vector2(0.01f, 0.05f);
	private static float length = 2.0f;
	private static float weightPerM = 1.0f;
	private static float elementWeight = weightPerM * elementSize.y * 2f;

	@Getter
	List<RopeElement> elements = new ArrayList<>();
	private Body start;
	private Body end;
	private Vector2 direction;
	private Vector2 anchorStart;
	private Vector2 anchorEnd;

	public Rope(Body start, Body end) {
		this(start, end, new Vector2(0, 0), new Vector2(0, 0));
	}

	public Rope(Body start, Body end, Vector2 anchorStart, Vector2 anchorEnd) {
		this.start = start;
		this.end = end;
		this.anchorStart = anchorStart;
		this.anchorEnd = anchorEnd;
		this.direction = end.getPosition().cpy().sub(start.getPosition());
		createRope();
	}

	private void createRope() {
		int elementCount = (int) (length / (elementSize.y * 2.0f)) + 1;
		addFirstElement();
		Util.callXTimes(elementCount - 2, () -> addElement());
		addLastElement();
	}

	public void appendElement() {
		int index = elements.size() - 1;
		RopeElement last = getLastElement();
		last.instantDispose();
		elements.remove(index);

		addElement();
		addLastElement();
	}

	public void addFirstElement() {
		Pose pose = new Pose(start.getPosition().cpy().add(0, -2f * elementSize.y), direction.angleRad());

		RopeElement element = new RopeElement(pose, elementSize, elementWeight);
		element.addToWorld();
		elements.add(element);

		RopeJointDef jointDef = new RopeJointDef();
		jointDef.collideConnected = false;
		jointDef.bodyA = start;
		jointDef.localAnchorA.set(anchorStart);
		jointDef.bodyB = element.getBody();
		jointDef.localAnchorB.set(new Vector2(0, +elementSize.y / 2.0f));
		jointDef.maxLength = elementSize.y;
		GameContext.inst();
		WorldUtil.createJoint(jointDef);
	}

	public void addElement() {
		int index = elements.size();

		Pose pose = new Pose(getLastElement().getBody().getPosition().cpy().add(0, -2f * elementSize.y),
				getLastElement().getBody().getAngle());

		RopeElement element = new RopeElement(pose, elementSize, elementWeight);
		element.addToWorld();
		elements.add(element);

		RopeJointDef jointDef = new RopeJointDef();
		jointDef.collideConnected = true;
		jointDef.bodyA = elements.get(index - 1).getBody();
		jointDef.localAnchorA.set(new Vector2(0, -elementSize.y / 2.0f));
		jointDef.bodyB = element.getBody();
		jointDef.localAnchorB.set(new Vector2(0, +elementSize.y / 2.0f));
		jointDef.maxLength = elementSize.y;
		GameContext.inst();
		WorldUtil.createJoint(jointDef);
	}

	public void addLastElement() {
		int index = elements.size();

		Pose pose = new Pose(getLastElement().getBody().getPosition().cpy().add(0, -2f * elementSize.y),
				getLastElement().getBody().getAngle());

		RopeElement element = new RopeElement(pose, elementSize, elementWeight);
		element.addToWorld();
		elements.add(element);

		RopeJointDef jointDef = new RopeJointDef();
		jointDef.collideConnected = true;
		jointDef.bodyA = elements.get(index - 1).getBody();
		jointDef.localAnchorA.set(new Vector2(0, -elementSize.y / 2.0f));
		jointDef.bodyB = element.getBody();
		jointDef.localAnchorB.set(new Vector2(0, +elementSize.y / 2.0f));
		jointDef.maxLength = elementSize.y;
		GameContext.inst();
		WorldUtil.createJoint(jointDef);

		RopeJointDef endJointDef = new RopeJointDef();
		endJointDef.collideConnected = false;
		endJointDef.bodyA = element.getBody();
		endJointDef.localAnchorA.set(new Vector2(0, -elementSize.y / 2.0f));
		endJointDef.bodyB = end;
		endJointDef.localAnchorB.set(anchorEnd);
		endJointDef.maxLength = elementSize.y;
		GameContext.inst();
		WorldUtil.createJoint(endJointDef);
	}

	public RopeElement getLastElement() {
		return elements.get(elements.size() - 1);
	}

	public void destroyRope() {
		elements.forEach(x -> x.dispose());
	}
}
