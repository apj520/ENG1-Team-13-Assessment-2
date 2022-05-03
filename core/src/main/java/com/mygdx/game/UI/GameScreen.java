package com.mygdx.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Components.ComponentEvent;
import com.mygdx.game.Components.Pirate;
import com.mygdx.game.Entitys.Player;
import com.mygdx.game.Managers.*;
import com.mygdx.game.PirateGame;
import com.mygdx.game.Quests.Quest;
import static com.mygdx.game.PirateGame.prefs;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import static com.mygdx.utils.Constants.*;

public class GameScreen extends Page {
    private Label healthLabel;
    private Label dosh;
    private Label ammo;
    private final Label questDesc;
    private final Label questName;
    //AYMAN CHANGE: UI LABELS:
    private final Label shopArmor;
    private final Label shopWeapon;
    private final TextButton buyArmor, saveBtn;
    private final TextButton buyWeapon;
    //CHANGE END
    /*private final Label questComplete;
    private float showTimer = 0;
    // in seconds
    private static final float showDuration = 1;*/

    /**
     * Boots up the actual game: starts PhysicsManager, GameManager, EntityManager,
     * loads texture atlases into ResourceManager. Draws quest and control info.
     *
     * @param parent PirateGame UI screen container
     * @param id_map the resource id of the tile map
     */
    public GameScreen(PirateGame parent, int id_map) {
        super(parent);
        INIT_CONSTANTS();
        PhysicsManager.Initialize(false);

        /*int id_ship = ResourceManager.addTexture("ship.png");
        int id_map = ResourceManager.addTileMap("Map.tmx");
        int atlas_id = ResourceManager.addTextureAtlas("Boats.txt");
        int extras_id = ResourceManager.addTextureAtlas("UISkin/skin.atlas");
        int buildings_id = ResourceManager.addTextureAtlas("Buildings.txt");
        ResourceManager.loadAssets();*/

        //Ayman - initialize with difficulty setting:
        GameManager.Initialize(prefs.getString("difficulty"));
        GameManager.SpawnGame(id_map);
        //QuestManager.addQuest(new KillQuest(c));

        EntityManager.raiseEvents(ComponentEvent.Awake, ComponentEvent.Start);

        Window questWindow = new Window("Current Quest", parent.skin);

        Quest q = QuestManager.currentQuest();
        Table t = new Table();
        questName = new Label("NAME", parent.skin);
        t.add(questName);
        t.row();
        questDesc = new Label("DESCRIPTION", parent.skin);
        if (q != null) {
            questName.setText(q.getName());
            questDesc.setText(q.getDescription());
        }
        /*questComplete = new Label("", parent.skin);
        actors.add(questComplete);*/

        t.add(questDesc).left();
        questWindow.add(t);
        actors.add(questWindow);

        Table t1 = new Table();
        t1.top().right();
        t1.setFillParent(true);
        actors.add(t1);

        Window tutWindow = new Window("Controls", parent.skin);
        Table table = new Table();
        tutWindow.add(table);
        t1.add(tutWindow);

        table.add(new Label("Move with", parent.skin)).top().left();
        table.add(new Image(parent.skin, "key-w"));
        table.add(new Image(parent.skin, "key-s"));
        table.add(new Image(parent.skin, "key-a"));
        table.add(new Image(parent.skin, "key-d"));
        table.row();
        table.add(new Label("Shoot in direction of mouse", parent.skin)).left();
        //table.add(new Image(parent.skin, "space"));
        table.add(new Image(parent.skin, "mouse"));
        table.row();
        table.add(new Label("Shoot in direction of ship", parent.skin)).left();
        table.add(new Image(parent.skin, "space"));
        table.row();
        table.add(new Label("Destroy all houses to kill colleges!", parent.skin)).left();
        table.row();
        table.add(new Label("Quit", parent.skin)).left();
        table.add(new Image(parent.skin, "key-esc"));
        //Ayman - added save button
        float space = VIEWPORT_HEIGHT * 0.05f;
        saveBtn = new TextButton("SAVE", parent.skin);
        saveBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Game Saved!");
                //Ayman - write to save file
                FileHandle saveFile = Gdx.files.local("GameSettingsSaved.json");
                saveFile.writeString(harcodedLIBGDXJSON(), false);
            }
        });
        table.add(saveBtn).bottom().right().size(100, 25).spaceBottom(space);


        //AYMAN CHANGE: ADD UI FOR PLUNDER-SHOP:
        Table t2 = new Table();
        t2.bottom().right();
        t2.setFillParent(true);
        actors.add(t2);

        Window shopWindow = new Window("Shop", parent.skin);
        Table shopTable = new Table();
        shopWindow.add(shopTable);
        t2.add(shopWindow);

        //ARMOR:
        //if statement before adding buttons/when clicking buttons:
        //add labels
        shopArmor = new Label("Armor $100", parent.skin);
        shopTable.add(shopArmor).top().left();
        //add buttons to table
        //add functions to add shop items to player??
        buyArmor = new TextButton("Locked", parent.skin);
        buyArmor.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Bought Armor!");
                //buyArmor.setChecked(false);
                buyArmor.setDisabled(true);
                //buyArmor.setLabel(new Label("Bought Armor", parent.skin));
                //buyArmor.
            }
        });
        shopTable.add(buyArmor).top().size(100, 25).spaceBottom(space);
        shopTable.row();

        //WEAPON:
        //add labels
        shopWeapon = new Label("Ammo $100", parent.skin);
        shopTable.add(shopWeapon).top().left();
        //add buttons to table
        //add functions to add shop items to player??
        buyWeapon = new TextButton("Locked", parent.skin);
        buyWeapon.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Bought Weapon!");
                //buyWeapon.setChecked(false);
                buyWeapon.setDisabled(true);
            }
        });
        shopTable.add(buyWeapon).top().size(100, 25).spaceBottom(space);
        shopTable.row();
        //CHANGE END

    }

    private float accumulator;

    /**
     * Called every frame calls all other functions that need to be called every frame by rasing events and update methods
     *
     * @param delta delta time
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(BACKGROUND_COLOUR.x, BACKGROUND_COLOUR.y, BACKGROUND_COLOUR.z, 1);

        EntityManager.raiseEvents(ComponentEvent.Update, ComponentEvent.Render);

        accumulator += EntityManager.getDeltaTime();

        // fixed update loop so that physics manager is called regally rather than somewhat randomly
        while (accumulator >= PHYSICS_TIME_STEP) {
            PhysicsManager.update();
            accumulator -= PHYSICS_TIME_STEP;
        }

        GameManager.update();
        // show end screen if esc is pressed
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            parent.setScreen(parent.end);
        }

        //Ayman - increment timer
        GameManager.getPlayer().getComponent(Pirate.class).addTime(Gdx.graphics.getDeltaTime());

        //Ayman - implement lose scenario
        if ((GameManager.getPlayer().getHealth() <= 0) || (GameManager.getPlayer().getAmmo() <= 0)) {
            //parent.end.win();
            parent.setScreen(parent.end);
        }

        super.render(delta);
    }

    /**
     * disposed of all stuff it something is missing from this method you will get memory leaks
     */
    @Override
    public void dispose() {
        super.dispose();
        ResourceManager.cleanUp();
        EntityManager.cleanUp();
        RenderingManager.cleanUp();
        PhysicsManager.cleanUp();
    }

    /**
     * Resize camera, effectively setting the viewport to display game assets
     * at pixel ratios other than 1:1.
     *
     * @param width  of camera viewport
     * @param height of camera viewport
     */
    @Override
    public void resize(int width, int height) {
        //((Table) actors.get(0)).setFillParent(false);
        super.resize(width, height);
        OrthographicCamera cam = RenderingManager.getCamera();
        cam.viewportWidth = width / ZOOM;
        cam.viewportHeight = height / ZOOM;
        cam.update();

        // ((Table) actors.get(0)).setFillParent(true);
    }

    /**
     * Update the UI with new values for health, quest status, etc.
     * also called once per frame but used for actors by my own convention
     */
    //private String prevQuest = "";
    @Override
    protected void update() {
        super.update();
        Player p = GameManager.getPlayer();

        healthLabel.setText(String.valueOf(p.getHealth()));
        dosh.setText(String.valueOf(p.getPlunder()));
        ammo.setText(String.valueOf(p.getAmmo()));
        if (!QuestManager.anyQuests()) {
            parent.end.win();
            parent.setScreen(parent.end);

        } else {
            Quest q = QuestManager.currentQuest();
            /*if(Objects.equals(prevQuest, "")) {
                prevQuest = q.getDescription();
            }
            if(!Objects.equals(prevQuest, q.getDescription())) {
                questComplete.setText("Quest Competed");
                prevQuest = "";
            }*/
            questName.setText(q.getName());
            questDesc.setText(q.getDescription());
        }
        /*if(!questComplete.getText().equals("")) {
            showTimer += EntityManager.getDeltaTime();
        }
        if(showTimer >= showDuration) {
            showTimer = 0;
            questComplete.setText("");
        }*/


        //AYMAN CHANGE: SHOP UI UPDATE:
        //UNLOCK SHOP ITEMS BUTTONS:
        if ((p.getComponent(Pirate.class).getPlunder() >= 100)) {
            buyArmor.setDisabled(false);
            buyWeapon.setDisabled(false);
            buyArmor.setText("Buy");
            buyWeapon.setText("Buy");
            //System.out.println("unlocked");

            //UPDATE PLUNDER AFTER BUYING UPGRADE AND DISABLE BUTTONS (ONE TIME USE)
            //STILL NEED TO IMPLEMENT ACTUAL FUNCTIONALITY OF UPGRADES:

            //UPGRADE FOR MORE HEALTH (DON'T WANT TO TINKER WITH NEW ARMOR CLASS SO JUST INCREASING HEALTH)?
            if (buyArmor.getClickListener().getTapCount() > 0 && !buyArmor.isDisabled()){
                p.getComponent(Pirate.class).addPlunder(-100);
                p.getComponent(Pirate.class).addArmor(200);
                //increase health
                //buyArmor.setText("Bought");
                //System.out.println("update armor");
                buyArmor.getClickListener().setTapCount(0);
                buyArmor.setDisabled(true);
                //buyArmor.removeListener(buyArmor.getClickListener());
            }
            //UPGRADE FOR MORE AMMO: ?
            if (buyWeapon.getClickListener().getTapCount() > 0 && !buyWeapon.isDisabled()){
                p.getComponent(Pirate.class).addPlunder(-100);
                p.getComponent(Pirate.class).addAmmo(100);
                //p.getComponent(PlayerController.class).incSpeed(500);
                //increase ammo
                //buyWeapon.setText("Bought");
                //System.out.println("update weapon");
                buyWeapon.getClickListener().setTapCount(0);
                buyWeapon.setDisabled(true);
                //buyWeapon.removeListener(buyWeapon.getClickListener());
            }
        }

        //AYMAN CHANGE: DISABLE BUTTONS:
        if (buyArmor.isDisabled()) {
            buyArmor.getClickListener().setTapCount(0);
        }
        if (buyWeapon.isDisabled()) {
            buyWeapon.getClickListener().setTapCount(0);
        }
        //CHANGE END


    }

    /**
     * Draw UI elements showing player health, plunder, and ammo.
     */
    @Override
    protected void CreateActors() {
        Table table = new Table();
        table.setFillParent(true);
        actors.add(table);

        table.add(new Image(parent.skin.getDrawable("heart"))).top().left().size(1.25f * TILE_SIZE);
        healthLabel = new Label("N/A", parent.skin);
        table.add(healthLabel).top().left().size(50);

        table.row();
        table.setDebug(false);

        table.add(new Image(parent.skin.getDrawable("coin"))).top().left().size(1.25f * TILE_SIZE);
        dosh = new Label("N/A", parent.skin);
        table.add(dosh).top().left().size(50);

        table.row();

        table.add(new Image(parent.skin.getDrawable("ball"))).top().left().size(1.25f * TILE_SIZE);
        ammo = new Label("N/A", parent.skin);
        table.add(ammo).top().left().size(50);

        table.top().left();
    }

    //Ayman - create new Json String object for save file
    public String harcodedLIBGDXJSON(){
        Json jsonObject = new Json();
        StringWriter jsonText = new StringWriter();
        JsonWriter writer = new JsonWriter(jsonText);
        jsonObject.setOutputType(JsonWriter.OutputType.json);
        jsonObject.setWriter(writer);
        jsonObject.writeObjectStart();
        Json starting = new Json();
        starting.setOutputType(JsonWriter.OutputType.json);
        starting.setWriter(writer);
        starting.writeObjectStart("starting");
        starting.writeValue("X",GameManager.getShips(0).getPosition().x / 32);
        starting.writeValue("Y",GameManager.getShips(0).getPosition().y / 32);
        starting.writeValue("health", GameManager.getPlayer().getHealth());
        starting.writeValue("damage",10.0);
        starting.writeValue("plunder", GameManager.getPlayer().getPlunder());
        starting.writeValue("playerSpeed",100.0); //need getter
        starting.writeValue("cannonSpeed", 10000.0);
        starting.writeValue("argoRange_tiles",9.0);
        starting.writeValue("attackRange_tiles", 4.0);
        starting.writeValue("ammo",GameManager.getPlayer().getAmmo());
        starting.writeValue("points",GameManager.getPlayer().getComponent(Pirate.class).getPoints());
        starting.writeValue("time",GameManager.getPlayer().getComponent(Pirate.class).getTime());
        starting.writeObjectEnd();

        //OBSTACLE/MONSTER
        Json obstacle = new Json();
        obstacle.setOutputType(JsonWriter.OutputType.json);
        obstacle.setWriter(writer);
        obstacle.writeObjectStart("obstacle");
        obstacle.writeValue("health", 200);
        obstacle.writeValue("damage",10.0);
        obstacle.writeValue("argoRange_tiles", 18.0);
        obstacle.writeValue("attackRange_tiles", 8.0);
        obstacle.writeValue("cannonSpeed",10000000.0);
        obstacle.writeValue("monsterSpeed",2000.0);
        obstacle.writeObjectEnd();

        //AI
        Json AI = new Json();
        AI.setOutputType(JsonWriter.OutputType.json);
        AI.setWriter(writer);
        AI.writeObjectStart("AI");
        AI.writeValue("maxSpeed", 500.0);
        AI.writeValue("accelerationTime",0.01);
        AI.writeValue("arrivalTolerance", 32.0);
        AI.writeValue("slowRadius",64.0);
        AI.writeObjectEnd();

        Json killed = new Json();
        killed.setOutputType(JsonWriter.OutputType.json);
        killed.setWriter(writer);
        killed.writeObjectStart("killed");
        killed.writeValue("1", getCollegeStatus(1)); //getCollegeStatus(1)
        killed.writeValue("2",getCollegeStatus(2));
        killed.writeValue("3", getCollegeStatus(3));
        killed.writeValue("4",getCollegeStatus(4));
        killed.writeValue("5",getCollegeStatus(5));
        killed.writeObjectEnd();

        jsonObject.writeArrayStart("factions");
        {
            //HALIFAX
            Json college = new Json();
            college.setOutputType(JsonWriter.OutputType.json);
            college.setWriter(writer);
            college.writeObjectStart();
            college.writeValue("name", "Halifax");
            college.writeValue("colour", "light-blue");
            //position
            Json position = new Json();
            position.setOutputType(JsonWriter.OutputType.json);
            position.setWriter(writer);
            position.writeObjectStart("position");
            position.writeValue("x", 33);
            position.writeValue("y",35);
            position.writeObjectEnd();
            //shipspawn
            Json shipSpawn = new Json();
            shipSpawn.setOutputType(JsonWriter.OutputType.json);
            shipSpawn.setWriter(writer);
            shipSpawn.writeObjectStart("shipSpawn");
            //shipSpawn.writeValue("x",19); //x increases ship goes left
            //shipSpawn.writeValue("y",33); //y increases ships go up
            shipSpawn.writeValue("x", GameManager.getShips(1).getPosition().x / 32);
            shipSpawn.writeValue("y",GameManager.getShips(1).getPosition().y / 32);
            shipSpawn.writeObjectEnd();

            college.writeObjectEnd();

            //CONSTANTINE
            Json collegeA = new Json();
            collegeA.setOutputType(JsonWriter.OutputType.json);
            collegeA.setWriter(writer);
            collegeA.writeObjectStart();
            collegeA.writeValue("name", "Constantine");
            collegeA.writeValue("colour", "pink");

            //position
            Json positionA = new Json();
            positionA.setOutputType(JsonWriter.OutputType.json);
            positionA.setWriter(writer);
            positionA.writeObjectStart("position");
            positionA.writeValue("x", 51);
            positionA.writeValue("y",51);
            positionA.writeObjectEnd();
            //shipspawn
            Json shipSpawnA = new Json();
            shipSpawnA.setOutputType(JsonWriter.OutputType.json);
            shipSpawnA.setWriter(writer);
            shipSpawnA.writeObjectStart("shipSpawn");
            shipSpawnA.writeValue("x", GameManager.getShips(4).getPosition().x / 32);
            shipSpawnA.writeValue("y",GameManager.getShips(4).getPosition().y / 32);
            shipSpawnA.writeObjectEnd();

            collegeA.writeObjectEnd();

            //LANGWIDTH
            Json collegeB = new Json();
            collegeB.setOutputType(JsonWriter.OutputType.json);
            collegeB.setWriter(writer);
            collegeB.writeObjectStart();
            collegeB.writeValue("name", "Langwidth");
            collegeB.writeValue("colour", "yellow");

            //position
            Json positionB = new Json();
            positionB.setOutputType(JsonWriter.OutputType.json);
            positionB.setWriter(writer);
            positionB.writeObjectStart("position");
            positionB.writeValue("x", 34);
            positionB.writeValue("y",72);
            positionB.writeObjectEnd();
            //shipspawn
            Json shipSpawnB = new Json();
            shipSpawnB.setOutputType(JsonWriter.OutputType.json);
            shipSpawnB.setWriter(writer);
            shipSpawnB.writeObjectStart("shipSpawn");
            shipSpawnB.writeValue("x", GameManager.getShips(7).getPosition().x / 32);
            shipSpawnB.writeValue("y",GameManager.getShips(7).getPosition().y / 32);
            shipSpawnB.writeObjectEnd();

            collegeB.writeObjectEnd();

            //GOODRICKE:
            //LANGWIDTH
            Json collegeC = new Json();
            collegeC.setOutputType(JsonWriter.OutputType.json);
            collegeC.setWriter(writer);
            collegeC.writeObjectStart();
            collegeC.writeValue("name", "Goodricke");
            collegeC.writeValue("colour", "green");

            //position
            Json positionC = new Json();
            positionC.setOutputType(JsonWriter.OutputType.json);
            positionC.setWriter(writer);
            positionC.writeObjectStart("position");
            positionC.writeValue("x", 75);
            positionC.writeValue("y",77);
            positionC.writeObjectEnd();
            //shipspawn
            Json shipSpawnC = new Json();
            shipSpawnC.setOutputType(JsonWriter.OutputType.json);
            shipSpawnC.setWriter(writer);
            shipSpawnC.writeObjectStart("shipSpawn");
            shipSpawnC.writeValue("x", GameManager.getShips(10).getPosition().x / 32);
            shipSpawnC.writeValue("y",GameManager.getShips(10).getPosition().y / 32);
            shipSpawnC.writeObjectEnd();

            collegeC.writeObjectEnd();

            //DERWENT:
            //LANGWIDTH
            Json collegeD = new Json();
            collegeD.setOutputType(JsonWriter.OutputType.json);
            collegeD.setWriter(writer);
            collegeD.writeObjectStart();
            collegeD.writeValue("name", "Derwent");
            collegeD.writeValue("colour", "dark-blue");

            //position
            Json positionD = new Json();
            positionD.setOutputType(JsonWriter.OutputType.json);
            positionD.setWriter(writer);
            positionD.writeObjectStart("position");
            positionD.writeValue("x", 76);
            positionD.writeValue("y",23);
            positionD.writeObjectEnd();
            //shipspawn
            Json shipSpawnD = new Json();
            shipSpawnD.setOutputType(JsonWriter.OutputType.json);
            shipSpawnD.setWriter(writer);
            shipSpawnD.writeObjectStart("shipSpawn");
            shipSpawnD.writeValue("x", GameManager.getShips(13).getPosition().x / 32);
            shipSpawnD.writeValue("y",GameManager.getShips(13).getPosition().y / 32);
            shipSpawnD.writeObjectEnd();

            collegeD.writeObjectEnd();
        }

        jsonObject.writeArrayEnd();

        //faction defaults:
        //to add new stuff set append to true
        Json facDefaults = new Json();
        facDefaults.setOutputType(JsonWriter.OutputType.json);
        facDefaults.setWriter(writer);
        facDefaults.writeObjectStart("factionDefaults");
        facDefaults.writeValue("shipCount", 3);
        facDefaults.writeValue("buildingHealth",1000);
        facDefaults.writeValue("shipSpawnRadius",15);
        facDefaults.writeObjectEnd();

        //college:
        Json collegeStat = new Json();
        collegeStat.setOutputType(JsonWriter.OutputType.json);
        collegeStat.setWriter(writer);
        collegeStat.writeObjectStart("college");
        collegeStat.writeValue("spawnRadius", 3);
        collegeStat.writeValue("numBuildings",12);
        collegeStat.writeObjectEnd();

        //quests:
        Json quests = new Json();
        quests.setOutputType(JsonWriter.OutputType.json);
        quests.setWriter(writer);
        quests.writeObjectStart("quests");
        quests.writeValue("count", 6);
        quests.writeArrayStart("locations");
        quests.writeValue(20);
        quests.writeValue(20);
        quests.writeValue(25);
        quests.writeValue(50);
        quests.writeValue(20);
        quests.writeValue(85);
        quests.writeValue(53);
        quests.writeValue(80);
        quests.writeValue(58);
        quests.writeValue(30);
        quests.writeValue(77);
        quests.writeValue(50);
        quests.writeArrayEnd();
        quests.writeObjectEnd();

        jsonObject.writeObjectEnd();
        return jsonObject.prettyPrint(jsonObject.getWriter().getWriter().toString());
        //return  jsonObject;
    }

    //Ayman - getter to facilitate saving college states
    public String getCollegeStatus(int fac) {
        if (GameManager.getCollege(fac).getComponent(Pirate.class).getHealth()==0) {
            return "";
        } else {return "false";}
    }
}
