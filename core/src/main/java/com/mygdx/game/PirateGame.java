package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.game.Managers.PhysicsManager;
import com.mygdx.game.Managers.RenderingManager;
import com.mygdx.game.Managers.ResourceManager;
import com.mygdx.game.UI.EndScreen;
import com.mygdx.game.UI.GameScreen;
import com.mygdx.game.UI.MenuScreen;

/**
 * Contains class instances of game UI screens.
 */
public class PirateGame extends Game {
    public MenuScreen menu;
    public GameScreen game;
    public EndScreen end;
    public Stage stage;
    public Skin skin;
    private static int id_ship;
    private static int id_map;
    private static int atlas_id;
    private static int extras_id;
    private static int buildings_id;

    private static int powerups_id;
    private static int obstacles_id;

    /**
     * Create instances of game stage and UI screens.
     */
    @Override
    public void create() {
        ResourceManager.Initialize();
        loadResources(); // TODO

        // cant load any more resources after this point (just functionally I choose not to implement)
        RenderingManager.Initialize();
        stage = new Stage(new ScreenViewport());
        createSkin();
        menu = new MenuScreen(this);
        PhysicsManager.Initialize();
        GameManager.Initialize();
        game = new GameScreen(this, id_map);
        end = new EndScreen(this);
        setScreen(menu);
    }
    public static void loadResources(){
        id_ship = ResourceManager.addTexture("ship.png");
        id_map = ResourceManager.addTileMap("Map.tmx");
        atlas_id = ResourceManager.addTextureAtlas("Boats.txt");
        extras_id = ResourceManager.addTextureAtlas("UISkin/skin.atlas");
        buildings_id = ResourceManager.addTextureAtlas("Buildings.txt");

        //Roscoe - added textureAtlas for sprites
        powerups_id = ResourceManager.addTextureAtlas("powerups.txt");
        obstacles_id = ResourceManager.addTextureAtlas("obstacles.txt");


        ResourceManager.addTexture("menuBG.jpg");
        ResourceManager.addTexture("Chest.png");
        ResourceManager.loadAssets();


    }
    /**
     * Clean up prevent memory leeks
     */
    @Override
    public void dispose() {
        menu.dispose();
        game.dispose();
        stage.dispose();
        skin.dispose();
    }

    /**
     * load ui skin from assets
     */
    private void createSkin() {
        skin = new Skin(Gdx.files.internal("UISkin/skin.json"));
    }
}
