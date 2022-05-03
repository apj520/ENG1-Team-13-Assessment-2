package de.tomgrill.gdxtesting.Ship;

import com.mygdx.game.Entitys.College;
import com.mygdx.game.Entitys.Player;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.game.Managers.PhysicsManager;
import com.mygdx.game.Managers.ResourceManager;
import com.mygdx.game.PirateGame;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;



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
    public void dispose(){
        ResourceManager.cleanUp();
        ResourceManager.Initialize();
    }
    @Test
    public void testShip2(){
        Player player = new Player();
        ResourceManager.cleanUp();
        ResourceManager.Initialize();

    }
}
