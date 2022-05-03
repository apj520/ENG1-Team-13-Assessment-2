package com.mygdx.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.AI.TileMapGraph;
import com.mygdx.game.Components.Pirate;
import com.mygdx.game.Components.Transform;
import com.mygdx.game.Entitys.*;
import com.mygdx.game.Faction;
import com.mygdx.utils.QueueFIFO;
import com.mygdx.utils.Utilities;
import static com.mygdx.game.PirateGame.prefs;
import com.mygdx.game.Components.Pirate;
import java.util.ArrayList;//
import java.util.List;
//TODO tryinit has been commented out
/**
 * Responsible for creating most entity's associated with the game. Also the cached chest and cannonballs
 */
public final class GameManager {
    private static boolean initialized = false;
    private static ArrayList<Faction> factions;
    private static ArrayList<Ship> ships;
    private static ArrayList<College> colleges;

    //Roscoe - instantiates list of power-ups;
    private static ArrayList<PowerUp>  powerUps;
    private static final int powerUpCacheSize = 5;

    private static final int cacheSize = 100;
    private static ArrayList<CannonBall> ballCache;
    private static int currentElement;

    private static JsonValue settings;

    private static TileMapGraph mapGraph;

    //Roscoe - added monster ball cache
    private static ArrayList<Obstacle> obstacles;
    private static ArrayList<MonsterBall> monsterBallCache;
    private static final int monsterCacheSize = 20;
    private static int currentElementM;

    /**
     * facilitates creation of the game
     */
    public static void Initialize(String difficulty) {
        initialized = true;
        currentElement = 0;
        //Roscoe added current Element for monsterBall
        currentElementM = 0;
        settings = new JsonReader().
                parse(Gdx.files.internal(difficulty));

        factions = new ArrayList<>();
        ships = new ArrayList<>();
        ballCache = new ArrayList<>(cacheSize);
        colleges = new ArrayList<>();

        for (int i = 0; i < cacheSize; i++) {
            ballCache.add(new CannonBall());
        }

        //Roscoe - initialises list of power-ups
        powerUps = new ArrayList<>(powerUpCacheSize);

        //Roscoe - added monsterBall cache
        obstacles = new ArrayList<>();
        monsterBallCache = new ArrayList<>(20);
        for (int i = 0; i < monsterCacheSize; i++) {
            monsterBallCache.add(new MonsterBall());
        }


        for (JsonValue v : settings.get("factions")) {
            String name = v.getString("name");
            String col = v.getString("colour");
            Vector2 pos = new Vector2(v.get("position").getFloat("x"), v.get("position").getFloat("y"));
            pos = Utilities.tilesToDistance(pos);
            Vector2 spawn = new Vector2(v.get("shipSpawn").getFloat("x"), v.get("shipSpawn").getFloat("y"));
            spawn = Utilities.tilesToDistance(spawn);
            factions.add(new Faction(name, col, pos, spawn, factions.size() + 1));
        }
    }

    //Ayman - getters for saving
    public static Ship getShips(int n) {return  ships.get(n);}

    public static ArrayList getShip() {
        return  ships;
    }

    //Ayman - added restartGame function which resets game when called
    public static void restartGame() {
        JsonValue starting = getSettings().get("starting");
        JsonValue factions = getSettings().get("factions");
        //reset player starting stats
        getPlayer().getComponent(Pirate.class).setAmmo(starting.getInt("ammo"));
        getPlayer().getComponent(Pirate.class).resetHealth(starting.getInt("health"));
        getPlayer().getComponent(Pirate.class).resetPlunder(0);
        getPlayer().getComponent(Pirate.class).setPoints(0);
        getPlayer().getComponent(Pirate.class).setTime(0);
        //forloop to reset ship spawn pos and health
        //NEED TO RESET SHIP STATUS AFTER ROSCOE ADDS CODE
      for (int i = 0; i < (GameManager.getShip()).size(); i++) {
            GameManager.getShips(i).getComponent(Pirate.class).setHealth(100);
            if (i >=0 && i <=2) {GameManager.getShips(i).getComponent(Transform.class).setPosition(getFaction(1).getSpawnPos());}
            if (i >=3 && i <=5) {GameManager.getShips(i).getComponent(Transform.class).setPosition(getFaction(2).getSpawnPos());
                GameManager.getShips(i).setFaction(2);}
            if (i >=6 && i <=8) {GameManager.getShips(i).getComponent(Transform.class).setPosition(getFaction(3).getSpawnPos());
                GameManager.getShips(i).setFaction(3);}
            if (i >=9 && i <=11) {GameManager.getShips(i).getComponent(Transform.class).setPosition(getFaction(4).getSpawnPos());
                GameManager.getShips(i).setFaction(4);}
            if (i >=12 && i <=14) {GameManager.getShips(i).getComponent(Transform.class).setPosition(getFaction(5).getSpawnPos());
                GameManager.getShips(i).setFaction(5);}
        }
        getPlayer().getComponent(Transform.class).setPosition(800, 800); //player has to be repositioned after all the ships are spawned as unsure which ship index player is
        //Ayman - destroy cannons
        //works but player ends up woth infinite ammo (maintains functionality tho)
        //for (CannonBall canon: ballCache) {
        //    canon.kill();
        //}
    }

    /**
     * called every fram checks id the quests are completed
     */
    public static void update() {
        QuestManager.checkCompleted();
    }

    /**
     * Player is always in ships at index 0
     *
     * @return the ship
     */
    public static Player getPlayer() {
        return (Player) ships.get(0);
    }

    /**
     * Creates the game with player maps, NPCs, colleges
     *
     * @param mapId the resource id of the tilemap
     */
    public static void SpawnGame(int mapId) {
        CreateWorldMap(mapId);
        CreatePlayer();
        CreatePowerUps();
        CreateMonsters();
        CreateHurricane();
        final int cnt = settings.get("factionDefaults").getInt("shipCount");
        for (int i = 0; i < factions.size(); i++) {
            CreateCollege(i + 1);
            for (int j = 0; j < cnt; j++) {
                // prevents halifax from having shipcount + player
                if (i == 0 && j > cnt - 2) {
                    break;
                }
                if (i >= 0) {
                    NPCShip s = CreateNPCShip(i + 1);
                    s.getComponent(Transform.class).setPosition(getFaction(i + 1).getSpawnPos());
                }
            }
        }

        //Ayman - kill marked colleges in save file
        for (College c: colleges) {
            int id = c.getComponent(Pirate.class).getFaction().id;
            String fac = Integer.toString(id);
            if (settings.get("killed").getString(fac).isEmpty() && id!=1) {
                getCollege(id).getComponent(Pirate.class).kill(); //isAlive is false and health is 0
                for (int i = 0; i < getCollege(id).getAllBuilding().size()-1 ; i++){
                    getCollege(id).getBuilding(i).destroy();
                }
                System.out.println("College killed"+fac);
            } else {
                System.out.println("College is alive"+fac);
            }
        }
    }

    //Roscoe - added hurricane creation method
    private static void CreateHurricane() {
        Hurricane h = new Hurricane();
        obstacles.add(h);
    }

    /**
     * Creates player that belongs the faction with id 1
     */
    public static void CreatePlayer() {
       // tryInit();
        Player p = new Player();
        p.setFaction(1);
        ships.add(p);
    }

    /**
     * Creates an NPC ship with the given faction
     *
     * @param factionId desired faction
     * @return the created ship
     */
    public static NPCShip CreateNPCShip(int factionId) {
       // tryInit();
        NPCShip e = new NPCShip();
        e.setFaction(factionId);
        ships.add(e);

        //Roscoe - added condition that if npcship is halifax, health is only 50 for game balance
        if (factionId == 1) {
            e.getComponent(Pirate.class).setHealth(50);
        }

        return e;
    }

    /**
     * Creates the world map
     *
     * @param mapId resource id
     */
    public static void CreateWorldMap(int mapId) {
       // tryInit();
        WorldMap map = new WorldMap(mapId);
        mapGraph = new TileMapGraph(map.getTileMap());
    }

    /**
     * Creates the college with it's building for the desired college
     *
     * @param factionId desired faction
     */
    public static void CreateCollege(int factionId) {
        //tryInit();
        College c = new College(factionId);
        colleges.add(c);
    }

    //Roscoe: create power-ups method
    /**
     * Creates 5 unique powerups
     */
    public static void CreatePowerUps() {
        String[] powerUpNames = {"double_plunder","FFR_bubble","health_regen","immunity","speed"};
        for (int i = 0; i < powerUpCacheSize; i++) {
            powerUps.add(new PowerUp(powerUpNames[i]));
            powerUps.get(i).getComponent(Transform.class).setPosition(900+(i*60),600);
        }
    }

    //Roscoe - added getShips method
    public static ArrayList<Ship> getShipsList() {
        return ships;
    }

    //Roscoe - added monster creation method
    public static void CreateMonsters() {
        Monster m = new Monster();
        obstacles.add(m);
    }

    private static void tryInit() {
        if (!initialized) {
            Initialize(prefs.getString("difficulty"));
        }
    }

    public static Faction getFaction(int factionId) {
      //  tryInit();
        return factions.get(factionId - 1);
    }

    /**
     * Gets the setting object from the GameSetting.json
     *
     * @return the JSON representation fo settings
     */
    public static JsonValue getSettings() {
        //do not comment out tryInit() here:
        tryInit();
        return settings;
    }

    public static College getCollege(int factionId) {
      //  tryInit();
        return colleges.get(factionId - 1);
    }

    /**
     * Utilises the cached cannonballs to fire one
     *
     * @param p   parent
     * @param dir shoot direction
     */
    public static void shoot(Entity p, Vector2 dir) {
        Vector2 pos = p.getComponent(Transform.class).getPosition().cpy();
        //pos.add(dir.x * TILE_SIZE * 0.5f, dir.y * TILE_SIZE * 0.5f);
        if (p instanceof Ship) {
            ballCache.get(currentElement++).fire(pos, dir, (Ship) p);
            currentElement %= cacheSize;
        }
        else if (p instanceof Monster) {
            monsterBallCache.get(currentElementM++).fire(pos, dir, (Monster) p);
            currentElementM %= monsterCacheSize;
        }
    }

    /**
     * uses a* not sure if it works but i think it does
     *
     * @param loc src
     * @param dst dst
     * @return queue of delta postions
     */
    public static QueueFIFO<Vector2> getPath(Vector2 loc, Vector2 dst) {
        return mapGraph.findOptimisedPath(loc, dst);
    }
}
