package com.mygdx.game.Components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.Entitys.Ship;
import com.mygdx.game.Faction;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.utils.QueueFIFO;

/**
 * Gives the concepts of health plunder, etc. Allows for firing of cannonballs, factions, death, targets
 */
public class Pirate extends Component {
    private int factionId;
    private int plunder;
    protected boolean isAlive;
    private int health;
    private int ammo;
    private final int attackDmg;

    //Roscoe - added flags for immunity and double fire power-ups, added double plunder flag
    private boolean isImmune;
    private boolean doubleFire;
    private boolean dpFlag;

    /**
     * The enemy that is being targeted by the AI.
     */
    private final QueueFIFO<Ship> targets;

    public Pirate() {
        super();
        targets = new QueueFIFO<>();
        type = ComponentType.Pirate;
        plunder = GameManager.getSettings().get("starting").getInt("plunder");
        factionId = 1;
        isAlive = true;
        JsonValue starting = GameManager.getSettings().get("starting");
        health = starting.getInt("health");
        attackDmg = starting.getInt("damage");
        ammo = starting.getInt("ammo");

        //Roscoe - initialised power-up flags
        isImmune = false;
        doubleFire = false;
        dpFlag = false;
    }

    public void addTarget(Ship target) {
        targets.add(target);
    }

    public int getPlunder() {
        return plunder;
    }

    public void addPlunder(int money) {
        if (dpFlag) {
            plunder += money*2;
        } else {
            plunder += money;
        }
    }

    //AYMAN ADD FUNCTIONALITY FOR ARMOR + AMMO UPDRADE:
    public void addArmor(int armor) {
        health += armor;
    }
    public void addAmmo(int bullets) {
        ammo += bullets;
    }
    //CHANGE END

    //AYMAN RESTART CHANGE SETTERS:
    public void resetHealth(int newHealth) {this.health = newHealth;}
    public void resetPlunder(int newPlunder) {this.plunder = newPlunder;}




    public void switchDP() {
        if (dpFlag) {
            dpFlag = false;
        } else {
            dpFlag = true;
        }
    }

    public Faction getFaction() {
        return GameManager.getFaction(factionId);
    }

    public void setFactionId(int factionId) {
        this.factionId = factionId;
    }

    //Roscoe - modified to include immunity possibility
    public void takeDamage(float dmg) {
        if (!isImmune) {
            health -= dmg;
            if (health <= 0) {
                health = 0;
                isAlive = false;
            }
        }
    }

    //Roscoe - method to switch immunity on/off
    public void switchImmune() {
        if (isImmune) {
            isImmune = false;
        } else {isImmune = true;}
    }

    public void switchDF() {
        if (doubleFire) {
            doubleFire = false;
        } else {doubleFire = true;}
    }

    //Roscoe - modified shoot function to include power-up functionality (x2 fire)
    /**
     * Will shoot a cannonball assigning this.parent as the cannonball's parent (must be Ship atm)
     *
     * @param dir the direction to shoot in
     */
    public void shoot(Vector2 dir) {
        if (ammo == 0) {
            return;
        }
        else if (!doubleFire) {
            ammo--;
            GameManager.shoot((Ship) parent, dir);
        } else {
            if (ammo < 2) {
                GameManager.shoot((Ship) parent, dir);
            } else {
                GameManager.shoot((Ship) parent, dir.rotateDeg(358));
                GameManager.shoot((Ship) parent, dir.rotateDeg(2));
            }
        }
    }

    /**
     * Adds ammo
     *
     * @param ammo amount to add
     */
    public void reload(int ammo) {
        this.ammo += ammo;
    }

    public int getHealth() {
        return health;
    }

    //Roscoe - added setHealth and resetHealth methods for power-up functionality
    public void setHealth(int val) {
        health = val;
    }

    public void resetHealth() {
        health = GameManager.getSettings().get("starting").getInt("health");
    }

    /**
     * if dst to target is less than attack range
     * target will be null if not in agro range
     */
    public boolean canAttack() {
        if (targets.peek() != null) {
            final Ship p = (Ship) parent;
            final Vector2 pos = p.getPosition();
            final float dst = pos.dst(targets.peek().getPosition());
            // withing attack range
            return dst < Ship.getAttackRange();
        }
        return false;
    }

    /**
     * if dst to target is >= attack range
     * target will be null if not in agro range
     */
    public boolean isAgro() {
        if (targets.peek() != null) {
            final Ship p = (Ship) parent;
            final Vector2 pos = p.getPosition();
            final float dst = pos.dst(targets.peek().getPosition());
            // out of attack range but in agro range
            return dst >= Ship.getAttackRange();
        }
        return false;
    }

    public Ship getTarget() {
        return targets.peek();
    }

    public void removeTarget() {
        targets.pop();
    }

    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Kill its self
     */
    public void kill() {
        health = 0;
        isAlive = false;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public int getAmmo() {
        return ammo;
    }

    public int targetCount() {
        return targets.size();
    }

    public QueueFIFO<Ship> getTargets() {
        return targets;
    }
}
