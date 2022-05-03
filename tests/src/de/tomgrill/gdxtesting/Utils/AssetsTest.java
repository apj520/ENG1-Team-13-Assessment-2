package de.tomgrill.gdxtesting.Utils;

import com.badlogic.gdx.Gdx;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class AssetsTest {

    @Test
    public void ArialFontAsset() {
        assertTrue("Arial.ttf is Missing", Gdx.files.internal("arial.ttf").exists());
    }

    @Test
    public void BeachTilesetPNGAsset() {
        assertTrue("Beach Tileset.png is Missing", Gdx.files.internal("Beach Tileset.png").exists());
    }

    @Test
    public void BeachTilesetTSXAsset() {
        assertTrue("Beach Tileset.tsx is Missing", Gdx.files.internal("Beach Tileset.tsx").exists());
    }

    @Test
    public void BoatPNGAsset() {

        assertTrue("boats.png is Missing", Gdx.files.internal("boats.png").exists());
    }

    @Test
    public void BoatTXTAsset() {
        assertTrue("Boats.txt is Missing", Gdx.files.internal("Boats.txt").exists());
    }

    @Test
    public void BuildingsTXTAsset() {

        assertTrue("Buildings.txt is Missing", Gdx.files.internal("Buildings.txt").exists());
    }

    @Test
    public void ChestPNGAsset() {

        assertTrue("Chest.png is Missing", Gdx.files.internal("Chest.png").exists());
    }

    @Test
    public void GameSettingsJSONAsset() {
        assertTrue("GameSettingsEasy.json is Missing", Gdx.files.internal("GameSettingsEasy.json").exists());
        assertTrue("GameSettingsMedium.json is Missing", Gdx.files.internal("GameSettingsMedium.json").exists());
        assertTrue("GameSettingsHard.json is Missing", Gdx.files.internal("GameSettingsHard.json").exists());
        assertTrue("GameSettingsHard.json is Missing", Gdx.files.internal("GameSettingsSaved.json").exists());


    }

    @Test
    public void MapTMXAsset() {

        assertTrue("map.tmx is Missing", Gdx.files.internal("map.tmx").exists());
    }

    @Test
    public void MenuBGJPGAsset() {

        assertTrue("menuBG.jpg is Missing", Gdx.files.internal("menuBG.jpg").exists());
    }

    @Test
    public void OtherPNGAsset() {

        assertTrue("other.png is Missing", Gdx.files.internal("other.png").exists());
    }

    @Test
    public void ShipPNGAsset() {

        assertTrue("Test passes if ship.png exists", Gdx.files.internal("ship.png").exists());
    }

    @Test
    public void DefaultFNTUISkin() {

        assertTrue("default.fnt is Missing", Gdx.files.internal("UISkin/default.fnt").exists());
    }

    @Test
    public void DefaultPNGUISkin() {
        assertTrue("default.png is Missing", Gdx.files.internal("UISkin/default.png").exists());
    }

    @Test
    public void SkinATLASUISkin() {

        assertTrue("skin.atlas is Missing", Gdx.files.internal("UISkin/skin.atlas").exists());
    }

    @Test
    public void SkinJSONUISkin() {

        assertTrue("skin.json is Missing", Gdx.files.internal("UISkin/skin.json").exists());
    }

    @Test
    public void UISkinPNGUISkin() {

        assertTrue("uiskin.png is Missing", Gdx.files.internal("UISkin/uiskin.png").exists());
    }

}