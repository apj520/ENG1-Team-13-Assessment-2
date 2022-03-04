package com.mygdx.game.AI;

import com.badlogic.gdx.ai.pfa.Connection;

/**
 * The path that exists between 2 nodes not bidirectional
 */
public class Path implements Connection<Node> {
    Node from;
    Node to;

    public Path(Node f, Node t) {
        from = f;
        to = t;
    }

    @Override
    public float getCost() {
        return to.cost;
    }

    @Override
    public Node getFromNode() {
        return from;
    }

    @Override
    public Node getToNode() {
        return to;
    }
}
