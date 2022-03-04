package com.mygdx.game.Physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.mygdx.game.Entitys.Entity;

/**
 * Contains collision info consisting of entities, box2d bodies, and fixtures involved.
 */
public class CollisionInfo {
    public Fixture fA, fB;
    public Body bA, bB;
    public Entity a, b;
}
