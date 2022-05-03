package com.mygdx.game.Entitys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.AI.EnemyState;
import com.mygdx.game.AI.MonsterState;
import com.mygdx.game.Components.*;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.game.Managers.RenderLayer;
import com.mygdx.game.Managers.ResourceManager;
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
        this.obstacleName = "Monster_MK1";



        JsonValue starting = GameManager.getSettings().get("starting");
        JsonValue obstacleSettings = GameManager.getSettings().get("obstacle");

        getComponent(Renderable.class).setTexture(ResourceManager.getSprite(ResourceManager.getId("obstacles.txt"), obstacleName));

        getComponent(Transform.class).setPosition(2100,1200);

        getComponent(Pirate.class).setHealth(obstacleSettings.getInt("health"));

        path = new QueueFIFO<>();
        if (AISettings == null) {
            AISettings = GameManager.getSettings().get("AI");
        }

        stateMachineMonster = new DefaultStateMachine<>(this, MonsterState.WANDER);
        /**AINavigation nav = new AINavigation();
        addComponent(nav);**/

        // agro trigger
        getComponent(RigidBody.class).addTrigger(Utilities.tilesToDistance(obstacleSettings.getFloat("argoRange_tiles")), "agro");
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
            fireTimer = 0;

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

    public static float getAttackRange() {
        return Utilities.tilesToDistance(GameManager.getSettings().get("obstacle").getFloat("attackRange_tiles"));
    }

    @Override
    public void BeginContact(CollisionInfo info) {

    }

    @Override
    public void EndContact(CollisionInfo info) {

    }

    @Override
    public void EnterTrigger(CollisionInfo info) {
        Ship other = (Ship) info.a;
        if (other instanceof Player) {
            Pirate pirate = getComponent(Pirate.class);
            pirate.addTarget(other);
        }
    }

    @Override
    public void ExitTrigger(CollisionInfo info) {
        Pirate pirate = getComponent(Pirate.class);
        Ship o = (Ship) info.a;
        // remove the object from the targets list
        for (Ship targ : pirate.getTargets()) {
            if (targ == o) {
                pirate.getTargets().remove(targ);
                break;
            }
        }
    }
}
