package de.tomgrill.gdxtesting;

import com.mygdx.game.Entitys.Building;
import com.mygdx.game.Entitys.Player;
import com.mygdx.game.Entitys.Ship;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.game.Managers.PhysicsManager;
import com.mygdx.game.Managers.ResourceManager;
import com.mygdx.game.PirateGame;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.mygdx.game.Components.Pirate;
import com.mygdx.game.Entitys.College;


@RunWith(GdxTestRunner.class)

public class CollegeTests {

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
    public void testShip(){
        Player player = new Player();
        College college = new College();
        ResourceManager.cleanUp();
        ResourceManager.Initialize();

    }
}
