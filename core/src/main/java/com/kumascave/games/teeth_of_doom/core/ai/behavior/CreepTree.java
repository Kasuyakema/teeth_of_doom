package com.kumascave.games.teeth_of_doom.core.ai.behavior;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.branch.Selector;
import com.kumascave.games.teeth_of_doom.core.Constants;
import com.kumascave.games.teeth_of_doom.core.entity.mobs.creeps.Creep;

public class CreepTree extends BehaviorTree<Creep> {

	public CreepTree(Creep object) {
		super(buildTree(object), object);
	}

	private static Task<Creep> buildTree(Creep object) {
		ChooseTarget<Creep> chooseTarget = new ChooseTarget<>();

		MoveTo<Creep> moveTo = new MoveTo<>();
		@SuppressWarnings("unchecked")
		Selector<Creep> root = new Selector<>(new Attack<Creep>(), chooseTarget,
				new ChooseAttackPosition<>(object.getAttackRange() - Constants.PLANNINGGRID_SIZE), moveTo);
		return root;
	}

}
