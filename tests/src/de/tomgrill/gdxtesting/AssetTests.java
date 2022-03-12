package de.tomgrill.gdxtesting;

import com.badlogic.gdx.Gdx;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class AssetTests {

    @Test
    public void testShipAssetExists() {
        assertTrue("Test passes if ship.png exists", Gdx.files.internal("ship.png").exists());
    }
}
