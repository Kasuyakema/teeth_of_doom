package com.kumascave.games.teeth_of_doom.core.entity.mobs.antropomorph;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.kumascave.games.teeth_of_doom.core.RepeatingAction;
import com.kumascave.games.teeth_of_doom.core.physics.WorldUtil;

import lombok.Getter;
import lombok.Setter;

public class MonitoredJoint implements Disposable {

	@Setter
	Runnable onDestroy;
	RepeatingAction action;
	@Getter
	Joint joint;
	@Setter
	Body refBody;

	public MonitoredJoint(Actor actor, Runnable onDestroy) {
		super();
		this.onDestroy = onDestroy;
		this.action = new RepeatingAction(0.02f, () -> {
			if (!jointExists()) {
				action.setAwake(false);
				this.onDestroy.run();
			}
		});
		action.setAwake(false);
		actor.addAction(action);
	}

	public boolean jointExists() {
		return joint != null && WorldUtil.jointExists(joint, refBody);
	}

	public void destroyJoint() {
		action.setAwake(false);
		WorldUtil.destroyJoint(joint, refBody);
		joint = null;
	}

	@Override
	public void dispose() {
		action.setDestroy(false);
	}

	public void setJoint(Joint joint) {
		this.joint = joint;
		action.setAwake(true);
	}

}
