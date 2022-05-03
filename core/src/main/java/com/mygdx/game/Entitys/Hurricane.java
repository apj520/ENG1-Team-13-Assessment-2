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

public class Hurricane extends Obstacle implements CollisionCallBack {

    private static JsonValue AISettings;
    private final QueueFIFO<Vector2> path;
    private final float damage = GameManager.getSettings().get("obstacle").getFloat("damage");
    private float damageRate = 0;


    public Hurricane() {
        super();
        this.obstacleName = "Tornado";



        JsonValue starting = GameManager.getSettings().get("starting");
        JsonValue obstacleSettings = GameManager.getSettings().get("obstacle");

        getComponent(Renderable.class).setTexture(ResourceManager.getSprite(ResourceManager.getId("obstacles.txt"), obstacleName));

        getComponent(Transform.class).setPosition(1000,1650);

        getComponent(Pirate.class).setHealth(obstacleSettings.getInt("health"));

        path = new QueueFIFO<>();
        if (AISettings == null) {
            AISettings = GameManager.getSettings().get("AI");
        }

        /**AINavigation nav = new AINavigation();
         addComponent(nav);**/

        // agro trigger
        getComponent(RigidBody.class).addTrigger(Utilities.tilesToDistance(obstacleSettings.getFloat("argoRange_tiles")), "agro");
    }
    /**
     * updates the state machine
     */
    @Override
    public void update() {
        super.update();
        if (damageRate >= 2.5f) {
            damageShips();
        }
        damageRate += Gdx.graphics.getDeltaTime();
        //TODO - random placement of hurricane on map every 30/60 seconds
    }

    public void damageShips() {
        for (Ship ship : GameManager.getShipsList()) {
            if (isAgro(ship)) {
                ship.getComponent(Pirate.class).takeDamage(getDamage());
                //Ayman - implemented point increment when encountering player
                GameManager.getPlayer().getComponent(Pirate.class).addPoints(500);
                damageRate = 0;
            }
        }
    }

    public boolean isAgro(Ship p) {
        final Vector2 pos = getComponent(Transform.class).getPosition();
        final float dist = pos.dst(p.getPosition());
        return dist <= getAgroRange()/2;
    }

    public float getDamage() {
        return this.damage;
    }

    public void setPosition(Vector2 pos) {
        getComponent(Transform.class).setPosition(pos);
    }


    public static float getAttackRange() {
        return Utilities.tilesToDistance(GameManager.getSettings().get("starting").getFloat("attackRange_tiles"));
    }

    public static float getAgroRange() {
        return Utilities.tilesToDistance(GameManager.getSettings().get("starting").getFloat("argoRange_tiles"));
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