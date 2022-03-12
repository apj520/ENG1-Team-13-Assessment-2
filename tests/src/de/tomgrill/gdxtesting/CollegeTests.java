package de.tomgrill.gdxtesting;

import com.mygdx.game.Entitys.Building;
import com.mygdx.game.PirateGame;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.mygdx.game.Components.Pirate;
import com.mygdx.game.Entitys.College;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

//TEST IS BROKEN, CANT GET IT TO WORK BOTH WITH AND WITHOUT MOCKITO

@RunWith(GdxTestRunner.class)
//@RunWith(MockitoJUnitRunner.class)
public class CollegeTests {

    @Mock
    private College collegeMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCollegeDead() {
        doCallRealMethod().when(collegeMock).spawn("red");
        collegeMock.spawn("red");
        for (Building b : collegeMock.buildings) {
            b.destroy();
        }
        collegeMock.getComponent(Pirate.class).kill();
        assertFalse(collegeMock.getComponent(Pirate.class).isAlive());
    }
}
