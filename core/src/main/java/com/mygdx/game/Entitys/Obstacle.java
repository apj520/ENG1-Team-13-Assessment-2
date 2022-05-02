package com.mygdx.game.Entitys;

import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.game.AI.EnemyState;
import com.mygdx.game.Components.Pirate;
import com.mygdx.game.Components.Renderable;
import com.mygdx.game.Components.RigidBody;
import com.mygdx.game.Components.Transform;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.game.Managers.RenderLayer;
import com.mygdx.game.Managers.ResourceManager;
import com.mygdx.game.Physics.CollisionCallBack;
import com.mygdx.game.Physics.CollisionInfo;
import com.mygdx.game.Physics.PhysicsBodyType;
import com.mygdx.utils.QueueFIFO;
import com.mygdx.utils.Utilities;

public class Obstacle extends Entity implements CollisionCallBack {

    public static ObjectMap<Vector2, String> obstacleDirections;

    protected String obstacleName;
    protected boolean isActive;
    protected int atlas_id = ResourceManager.getId("obstacles.txt");

    public Obstacle() {
        super(4);
        Vector2 currentDir = new Vector2();

        this.obstacleName = "Monster_MK1";

        Transform t = new Transform();
        Renderable r = new Renderable(atlas_id, obstacleName, RenderLayer.Transparent);
        RigidBody rb = new RigidBody(PhysicsBodyType.Static, r, t, true);
        rb.setCallback(this);

        Pirate p = new Pirate();

        addComponents(t, r, rb, p);

        isActive = true;
    }

    //TODO - set specific attack ranges for instances
    public static float getAttackRange() {
        return Utilities.tilesToDistance(GameManager.getSettings().get("starting").getFloat("attackRange_tiles"));
    }

    public int getHealth() {
        return getComponent(Pirate.class).getHealth();
    }

    public Vector2 getPosition() {
        return getComponent(Transform.class).getPosition().cpy();
    }

    public void remove() {
            getComponent(Renderable.class).hide();
            Transform t = getComponent(Transform.class);
            t.setPosition(10000, 10000);

            RigidBody rb = getComponent(RigidBody.class);
            rb.setPosition(t.getPosition());
            rb.setVelocity(0, 0);

            //set isActive back to true to avoid calling all the time
            isActive = true;
    }

    @Override
    public void update() {
        super.update();
        if (!isActive) {
            remove();
        }
    }


    @Override
    public void BeginContact(CollisionInfo info) {

    }

    @Override
    public void EndContact(CollisionInfo info) {

    }

    @Override
    public void EnterTrigger(CollisionInfo info) {

    }

    @Override
    public void ExitTrigger(CollisionInfo info) {

    }
}
