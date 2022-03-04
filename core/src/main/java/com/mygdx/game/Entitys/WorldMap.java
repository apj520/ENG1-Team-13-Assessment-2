package com.mygdx.game.Entitys;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.mygdx.game.Components.TileMap;
import com.mygdx.game.Managers.PhysicsManager;
import com.mygdx.game.Managers.RenderLayer;

/**
 * The world map
 */
public class WorldMap extends Entity {
    public WorldMap() {
        super();
    }

    public WorldMap(int mapId) {
        super(1);
        setName("WorldMap");
        TileMap map = new TileMap(mapId, RenderLayer.Five);
        addComponent(map);
        PhysicsManager.createMapCollision(map);
    }

    public TiledMap getTileMap() {
        return getComponent(TileMap.class).getTileMap();
    }
}
