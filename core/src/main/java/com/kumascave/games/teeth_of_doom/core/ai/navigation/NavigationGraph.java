package com.kumascave.games.teeth_of_doom.core.ai.navigation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.kumascave.games.teeth_of_doom.core.Constants;
import com.kumascave.games.teeth_of_doom.core.GameContext;
import com.kumascave.games.teeth_of_doom.core.ai.navigation.Neighbourhood.Position;
import com.kumascave.games.teeth_of_doom.core.world.PPos;
import com.kumascave.games.teeth_of_doom.core.world.TPos;

import lombok.Getter;
import lombok.Setter;

public class NavigationGraph implements IndexedGraph<Vector2> {

	@Getter
	private List<Vector2> nodes;

	@Getter
	@Setter
	float actorRadius;

	private static int nodesPerLine = Constants.PLANNINGGRID_RESOLUTION;
	private static float offset = Constants.PLANNINGGRID_SIZE;
	private static float toBorder = offset / 2f;

	public NavigationGraph() {
		nodes = new ArrayList<>();
		GameContext.getCollisionMap().iterate((pos, b) -> {
			if (!b) {
				// BL node on this tile
				Vector2 botLeft = pos.botLeftWorldPos().add(toBorder, toBorder);

				for (int x = 0; x < nodesPerLine; x++) {
					for (int y = 0; y < nodesPerLine; y++) {
						nodes.add(botLeft.cpy().add(x * offset, y * offset));
					}
				}
			}
		});
	}

	private NavigationGraph(List<Vector2> nodes, float actorRadius) {
		this.nodes = nodes;
		this.actorRadius = actorRadius;
	}

	@Override
	public Array<Connection<Vector2>> getConnections(Vector2 fromNode) {
		Array<Connection<Vector2>> result = new Array<>();
		Neighbourhood<Vector2> neigbours = new Neighbourhood<>();
		neigbours.put(Position.TOP_RIGHT, mapToNearestNode(fromNode.cpy().add(offset, offset)));
		neigbours.put(Position.TOP_LEFT, mapToNearestNode(fromNode.cpy().add(-offset, offset)));
		neigbours.put(Position.BOT_LEFT, mapToNearestNode(fromNode.cpy().add(-offset, -offset)));
		neigbours.put(Position.BOT_RIGHT, mapToNearestNode(fromNode.cpy().add(offset, -offset)));
		neigbours.put(Position.TOP_MID, mapToNearestNode(fromNode.cpy().add(0, offset)));
		neigbours.put(Position.BOT_MID, mapToNearestNode(fromNode.cpy().add(0, -offset)));
		neigbours.put(Position.MID_LEFT, mapToNearestNode(fromNode.cpy().add(-offset, 0)));
		neigbours.put(Position.MID_RIGHT, mapToNearestNode(fromNode.cpy().add(offset, 0)));
		for (Entry<Position, Vector2> e : neigbours.entrySet()) {
			if (checkNode(e.getValue())) {
				switch (e.getKey()) {
				case BOT_LEFT:
					if (checkNode(neigbours.get(Position.BOT_MID)) && checkNode(neigbours.get(Position.MID_LEFT))) {
						result.add(new DefaultConnection<Vector2>(fromNode, e.getValue()));
					}
					break;
				case BOT_RIGHT:
					if (checkNode(neigbours.get(Position.BOT_MID)) && checkNode(neigbours.get(Position.MID_RIGHT))) {
						result.add(new DefaultConnection<Vector2>(fromNode, e.getValue()));
					}
					break;
				case TOP_LEFT:
					if (checkNode(neigbours.get(Position.TOP_MID)) && checkNode(neigbours.get(Position.MID_LEFT))) {
						result.add(new DefaultConnection<Vector2>(fromNode, e.getValue()));
					}
					break;
				case TOP_RIGHT:
					if (checkNode(neigbours.get(Position.TOP_MID)) && checkNode(neigbours.get(Position.MID_RIGHT))) {
						result.add(new DefaultConnection<Vector2>(fromNode, e.getValue()));
					}
					break;
				case BOT_MID:
				case MID_LEFT:
				case MID_RIGHT:
				case TOP_MID:
					result.add(new DefaultConnection<Vector2>(fromNode, e.getValue()));
					break;
				default:
					throw new RuntimeException();
				}
			}
		}
		return result;
	}

	private boolean checkNode(Vector2 node) {
		if (node == null) {
			return false;
		}

		return actorRadius + Constants.PATHFINDING_BUFFER < GameContext.getGameStage().getTileLayer().getMap()
				.getDistanceMap().getTile(PPos.fromWorldPos(node));
	}

	@Override
	public int getIndex(Vector2 node) {
		return nodes.indexOf(node);
	}

	@Override
	public int getNodeCount() {
		return nodes.size();
	}

	public Vector2 getNode(Vector2 pos) {
		int index = nodes.indexOf(pos);
		if (index < 0) {
			return null;
		} else {
			return nodes.get(index);
		}
	}

	public Vector2 mapToNearestNode(Vector2 pos) {
		Vector2 botLeft = TPos.fromWorldPos(pos).botLeftWorldPos();
		Vector2 diff = pos.cpy().sub(botLeft);
		diff.x -= diff.x % offset;
		diff.y -= diff.y % offset;
		Vector2 node = botLeft.cpy().add(toBorder, toBorder).add(diff);
		return getNode(node);
	}

	public NavigationGraph withRadius(float radius) {
		return new NavigationGraph(nodes, radius);
	}

}
