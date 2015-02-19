package ca.mcgill.cs.comp361.nwo.mwgs.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;



/**
 * Map class definition.
 * Generated by the TouchCORE code generator.
 */
public class Map {
    
    private final int width;
    private final int height;
    private final int totalTiles;
    private final Hashtable<Integer, Tile> tiles;
    
    public Map(List<Tile> pTiles, int pWidth, int pHeight) {
        assert(pTiles.size() == pWidth * pHeight);
        width = pWidth;
        height = pHeight;
        totalTiles = pWidth * pHeight;
        tiles = new Hashtable<Integer,Tile>();
        for (Tile t : pTiles) {
            tiles.put(t.hashCode(), t);
        }
    }
    
    public static Map setUpMap(List<Player> players, String jsonMap) {
        Map map = JsonParser.parseMap(jsonMap);
        return map;
    }
    
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Iterable<Tile> getTiles() {
        return tiles.values();
    }
    
    public List<Tile> getTilesWithTombstones() {
        /* TODO: No message view defined */
        return null;
    }

    public List<Village> getVillages() {
        /* TODO: No message view defined */
        return null;
    }

    public List<Tile> getTilesWithTrees() {
        /* TODO: No message view defined */
        return null;
    }

    public void replaceTombstonesWithTrees(Player player) {
        Tile tile;
        for (int i = 0 ; i < totalTiles; i++) {
            tile = tiles.get(i);
            if (player == tile.getControllingPlayer()) {
                tile.setStructure(null);
                tile.setTerrainType(TerrainType.TREE);
            }
        }
    }

    public void growNewTrees() {
        /* TODO: No message view defined */
    }

    public Tile getTile(int x, int y) {
        assert(x < width && x >= 0);
        assert(y < height && y >= 0);
        return tiles.get(y * width + x);
    }
}
