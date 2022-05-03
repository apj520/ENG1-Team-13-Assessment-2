package com.mygdx.game.Entitys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;;
import com.mygdx.game.Components.*;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.game.Managers.RenderLayer;
import com.mygdx.game.Managers.ResourceManager;
import com.mygdx.game.Physics.CollisionCallBack;
import com.mygdx.game.Physics.CollisionInfo;
import com.mygdx.game.Physics.PhysicsBodyType;

public class PowerUp extends Entity implements CollisionCallBack {

    private static int atlas_id;
    private boolean isFlag;

    private String powerUpName;
    private boolean inEffect;
    private float timer;

    public PowerUp(String name) {
        super();
        this.powerUpName = name;
        Transform t = new Transform();
        t.setPosition(-100, 100);
        inEffect = false;
        atlas_id = ResourceManager.getId("powerups.txt");
        Renderable r = new Renderable(atlas_id, powerUpName, RenderLayer.Transparent);
        Sprite s = ResourceManager.getSprite(atlas_id, name);
        r.setTexture(s);
        RigidBody rb = new RigidBody(PhysicsBodyType.Dynamic,r,t);
        rb.setCallback(this);
        addComponents(t,r,rb);
    }

    public void powerUpAffect(String name) {
        timer = 0;
        inEffect = true;

        if (name == "double_plunder") {
            GameManager.getPlayer().getComponent(Pirate.class).switchDP();
        } else if (name == "FFR_bubble") {
            GameManager.getPlayer().getComponent(Pirate.class).switchDF();
        } else if (name == "health_regen") {
            GameManager.getPlayer().getComponent(Pirate.class).resetHealth();
        } else if (name == "immunity") {
            GameManager.getPlayer().getComponent(Pirate.class).switchImmune();
        } else if (name == "speed") {
            GameManager.getPlayer().updateSpeed(1000f);
        }
    }

    public void restore(String name) {
        inEffect = false;
        if (name == "double_plunder") {
            GameManager.getPlayer().getComponent(Pirate.class).switchDP();
        } else if (name == "FFR_bubble") {
            GameManager.getPlayer().getComponent(Pirate.class).switchDF();
        } else if (name == "health_regen") {

        } else if (name == "immunity") {
            GameManager.getPlayer().getComponent(Pirate.class).switchImmune();
        } else if (name == "speed") {
            GameManager.getPlayer().restoreSpeed();
        }
    }

    /**
     * Gets the name of the power up as a String
     *
     * @return name of power up
     */

    public String getPowerUpName() {
        return this.powerUpName;
    }

    /**
     * Removes the power-up offscreen once touched, call only when collision with player ship.
     */
    private void removeOnCollision() {
        if (isFlag) {
            getComponent(Renderable.class).hide();
            Transform t = getComponent(Transform.class);
            t.setPosition(10000, 10000);

            RigidBody rb = getComponent(RigidBody.class);
            rb.setPosition(t.getPosition());

            isFlag = false;
        }
    }

    public void kill() { isFlag = true; }

    @Override
    public void update() {
        super.update();
        removeOnCollision();

        if (inEffect) {
            timer += Gdx.graphics.getDeltaTime();
            if (timer >= 30f) {
                restore(powerUpName);
            }
        }
    }

    @Override
    public void BeginContact(CollisionInfo info) {
        if (info.a instanceof Player) {
            // the ball if from the same faction
            /*if(Objects.equals(b.getShooter().getComponent(Pirate.class).getFaction().getName(),
                    getComponent(Pirate.class).getFaction().getName())) {
                return;
            }*/

            kill();
            powerUpAffect(powerUpName);
        }
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
