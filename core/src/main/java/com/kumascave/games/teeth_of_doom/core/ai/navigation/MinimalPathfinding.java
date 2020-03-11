package com.kumascave.games.teeth_of_doom.core.ai.navigation;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class MinimalPathfinding {

	public static Vector2 node0 = new Vector2(0, 0);
	public static Vector2 node1 = new Vector2(1, 1);
	public static Connection<Vector2> connection = new DefaultConnection<Vector2>(node0, node1);

	public static void main(String[] args) {
		IndexedAStarPathFinder<Vector2> pathFinder = new IndexedAStarPathFinder<Vector2>(createGraph());
		boolean found = pathFinder.searchNodePath(node0, node1, (node, endNode) -> node.dst2(endNode), createOut());
		System.out.println(found);
	}

	private static GraphPath<Vector2> createOut() {
		return new DefaultGraphPath<Vector2>();
	}

	private static IndexedGraph<Vector2> createGraph() {
		return new IndexedGraph<Vector2>() {

			@Override
			public Array<Connection<Vector2>> getConnections(Vector2 fromNode) {
				Array<Connection<Vector2>> result = new Array<>();
				if (fromNode == node0) {
					result.add(connection);
				}
				return result;
			}

			@Override
			public int getIndex(Vector2 node) {
				if (node == node0) {
					return 0;
				}
				if (node == node1) {
					return 1;
				}
				return -1;
			}

			@Override
			public int getNodeCount() {
				return 2;
			}
		};
	}

}
