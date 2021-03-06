package newworldorder.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import newworldorder.client.shared.TerrainType;
import newworldorder.client.shared.VillageType;

/**
 * Village class is clean and complete.
 */
class Village implements Serializable {
    
	private static final long serialVersionUID = -3748359571009668032L;
	private VillageType villageType;
    private int gold;
    private int wood;
    private int health;
    private final Player controlledBy;
    private final Tile tile;
    private final Region controlledRegion;
    private Set<Unit> supportedUnits;
    
    /**
     * Creating a Village with the list of 
     * @param tile The tile the Village is on. This object must be in the pControlledTiles list of tiles.
     * @param player The Player who controls this village.
     * @param region The Tiles this village controls.
     */    
    public Village(Tile tile, Player player, Region region) {
        this.tile = tile;
        tile.setVillage(this);
        controlledBy = player;
        controlledRegion = region;
        controlledRegion.setVillage(this);
        supportedUnits = new HashSet<Unit>();
        villageType = VillageType.HOVEL;
        gold = 0;
        wood = 0;
        health = 1;
        controlledBy.addVillage(this);
        
		for (Tile t : region.getTiles()) {
			if (t.getUnit() != null) {
				this.addUnit(t.getUnit());
				t.getUnit().setVillage(this);
			}
		}
    }
    
    public static int villageLevel(VillageType pVillageType) {
        if (pVillageType == VillageType.HOVEL) {
            return 0;
        } else if (pVillageType == VillageType.TOWN) {
            return 1;
        } else if (pVillageType == VillageType.FORT) {
            return 2;
        } else if (pVillageType == VillageType.CASTLE) {
        	return 3;
        }
        return -1;
    }

    public static int villageCost(VillageType pVillageType) {
        if (pVillageType == VillageType.HOVEL) {
            return 0;
        } else if (pVillageType == VillageType.TOWN) {
            return 8;
        } else if (pVillageType == VillageType.FORT) {
            return 16;
        } else if (pVillageType == VillageType.CASTLE) {
        	return 28;
        }
        return -1;
    }
    
    public static int villageMaxHealth(VillageType pVillageType){
        if (pVillageType == VillageType.HOVEL) {
            return 1;
        } else if (pVillageType == VillageType.TOWN) {
            return 2;
        } else if (pVillageType == VillageType.FORT) {
            return 3;
        } else if (pVillageType == VillageType.CASTLE) {
        	return 10;
        }
        return -1;
    }
    
    public VillageType getVillageType() {
        return villageType;
    }

    public void setVillageType(VillageType pVillageType) {
        villageType = pVillageType;
        tile.setVillage(this);
    }

    public int getGold() {
        return gold;
    }

    public int getWood() {
        return wood;
    }
    
    public int getHealth(){
    	return health;
    }
    
    public void setHealth(int newHealth){
    	health = newHealth;
    }
    public Player getControlledBy() {
        return controlledBy;
    }
    
    public Tile getTile()
    {
        return tile;
    }

    public Region getRegion() {
        return controlledRegion;
    }

    public List<Unit> getSupportedUnits() {
        return new ArrayList<Unit>(supportedUnits);
    }
    
    public void transactGold(int g) {
        gold = gold + g;
    }

    public void transactWood(int w) {
        wood = wood + w;
    }
    
    public void addUnit(Unit u) {
        supportedUnits.add(u);
    }

    public void removeUnit(Unit u) {
        supportedUnits.remove(u);
    }

    public int getTotalUpkeep() {
        int total = 0;
        for (Unit u : supportedUnits) {
            total += u.getUpkeep();
        }
        return total;
    }

    public int getTotalIncome() {
        int grass;
        int meadow;
        int total = 0;
        grass = controlledRegion.getTileCount(TerrainType.GRASS);
        meadow = controlledRegion.getTileCount(TerrainType.MEADOW);
        total = grass + 2 * meadow;
        return total;
    }
}
