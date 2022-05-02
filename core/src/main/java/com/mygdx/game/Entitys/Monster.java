package com.mygdx.game.Entitys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.AI.EnemyState;
import com.mygdx.game.AI.MonsterState;
import com.mygdx.game.Components.*;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.game.Managers.RenderLayer;
import com.mygdx.game.Physics.CollisionCallBack;
import com.mygdx.game.Physics.CollisionInfo;
import com.mygdx.game.Physics.PhysicsBodyType;
import com.mygdx.utils.QueueFIFO;
import com.mygdx.utils.Utilities;

public class Monster extends Obstacle implements CollisionCallBack {

    public StateMachine<Monster, MonsterState> stateMachineMonster;
    private static JsonValue AISettings;
    private final QueueFIFO<Vector2> path;

    private float fireTimer = 2;


    public Monster() {
        super();
        this.obstacleName = "Monster";

        //TODO - set sprite properly
        //getComponent(Renderable.class).setTexture(obstacleName);

        getComponent(Transform.class).setPosition(2000,-100);

        getComponent(Pirate.class).setHealth(200);

        path = new QueueFIFO<>();
        if (AISettings == null) {
            AISettings = GameManager.getSettings().get("AI");
        }

        stateMachineMonster = new DefaultStateMachine<>(this, MonsterState.WANDER);
        AINavigation nav = new AINavigation();
        addComponent(nav);

        JsonValue starting = GameManager.getSettings().get("starting");

        // agro trigger
        getComponent(RigidBody.class).addTrigger(Utilities.tilesToDistance(starting.getFloat("argoRange_tiles")), "agro");
    }

    private boolean setCanFire() {
        return fireTimer >= 2f;
    }

    public void shoot() {
        if (setCanFire()) {
            Vector2 delta = getComponent(Pirate.class).getTarget().getPosition();
            delta.sub(getComponent(Transform.class).getPosition());
            delta.nor();
            //delta.y *= -1;

            getComponent(Pirate.class).shoot(delta);


        }
    }

    //Roscoe - added damage method
    public void cannonBallAffect(CollisionInfo info) {
        getComponent(Pirate.class).takeDamage(((CannonBall) info.a).getDamage());
        GameManager.getPlayer().getComponent(Pirate.class).addPlunder(100);
    }

    public boolean isAlive() {
        if (getComponent(Pirate.class).getHealth() <= 0) {
            isActive = false;
            return false;
        }
        return true;
    }

    /**
     * gets the top of targets from pirate component
     *
     * @return the top target
     */
    private Ship getTarget() {
        return getComponent(Pirate.class).getTarget();
    }


    /**
     * creates a new steering behaviour that will make the NPC beeline for the target doesn't factor in obstetrical
     */
    public void followTarget() {
        if (getTarget() == null) {
            stopMovement();
            return;
        }
        AINavigation nav = getComponent(AINavigation.class);

        Arrive<Vector2> arrives = new Arrive<>(nav,
                getTarget().getComponent(Transform.class))
                .setTimeToTarget(AISettings.getFloat("accelerationTime"))
                .setArrivalTolerance(AISettings.getFloat("arrivalTolerance"))
                .setDecelerationRadius(AISettings.getFloat("slowRadius"));

        nav.setBehavior(arrives);
    }

    /**
     * stops all movement and sets the behaviour to null
     */
    public void stopMovement() {
        AINavigation nav = getComponent(AINavigation.class);
        nav.setBehavior(null);
        nav.stop();
    }

    /**
     * Meant to cause the npc to wander
     */
    public void wander() {
    }

    public void dead() {
        //TODO - implement death of monster
    }

    /**
     * updates the state machine
     */
    @Override
    public void update() {
        super.update();
        stateMachineMonster.update();
        if (!isAlive()) {
            dead();
        }
        fireTimer += Gdx.graphics.getDeltaTime();
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
