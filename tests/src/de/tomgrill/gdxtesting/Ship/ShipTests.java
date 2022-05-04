package de.tomgrill.gdxtesting.Ship;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.mygdx.game.Components.ComponentType;
import com.mygdx.game.Components.Pirate;
import com.mygdx.game.Components.PlayerController;
import com.mygdx.game.Components.Transform;
import com.mygdx.game.Entitys.*;
import com.mygdx.game.Managers.*;
import com.mygdx.game.Physics.CollisionInfo;
import com.mygdx.game.PirateGame;
import com.mygdx.game.Quests.LocateQuest;
import com.mygdx.game.Quests.Quest;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Matchers;
import org.mockito.Mockito.*;

import javax.sound.sampled.Line;


@RunWith(GdxTestRunner.class)
public class ShipTests {

    @Before
    public void init(){
        ResourceManager.uninitialize();

        ResourceManager.Initialize();

        PirateGame.loadResources();
        PhysicsManager.Initialize();
        GameManager.Initialize("GameSettingsEasy.json");
    }
    @After
    public void dispose() {
        ResourceManager.cleanUp();
        ResourceManager.Initialize();
    }

    @Test
    public void AmmoGoesDown() {
        Player player = new Player();
        int initial = player.getAmmo();
        player.shoot();
        int after = player.getAmmo();
        Assert.assertEquals(initial - 1, after);

    }


    @Test
    public void TestNoAmmo() {
        Player player = new Player();
        player.getComponent(Pirate.class).setAmmo(0);
        player.shoot();
        int afterPressed = player.getAmmo();
        Assert.assertEquals(0, afterPressed);
    }

    @Test
    public void QuestComplete() {
        Player player = new Player();
        Vector2 chestLocation = new Vector2(805,805);
        LocateQuest chest = new LocateQuest(chestLocation,10);
        Vector2 location = (chest.getLocation().cpy());

        Assert.assertTrue(chest.checkCompleted(player));
    }




    @Test
    public void damageWithDying() {
        Player player = new Player();
        player.getComponent(Pirate.class).setHealth(1);
        player.getComponent(Pirate.class).takeDamage(1);
        Assert.assertFalse(player.getComponent(Pirate.class).isAlive());
    }

    @Test
    public void damageWithoutDying() {
        Player player = new Player();
        player.getComponent(Pirate.class).setHealth(2);
        player.getComponent(Pirate.class).takeDamage(1);
        Assert.assertTrue(player.getComponent(Pirate.class).isAlive());
    }
    @Test
    public void damageToNegative() {
        Player player = new Player();
        player.getComponent(Pirate.class).setHealth(1);
        player.getComponent(Pirate.class).takeDamage(2);
        Assert.assertFalse(player.getComponent(Pirate.class).isAlive());
    }
    @Test
    public void immunityDamage(){
        Player player = new Player();
        player.getComponent(Pirate.class).switchImmune();
        player.getComponent(Pirate.class).setHealth(1);
        player.getComponent(Pirate.class).takeDamage(1);
        Assert.assertTrue(player.getHealth()==1);
    }
    @Test
    public void DoublePlunder(){
        Player player = new Player();
        player.getComponent(Pirate.class).switchDP();
        player.getComponent(Pirate.class).addPlunder(100);
        Assert.assertTrue(player.getPlunder()==200);
    }


}