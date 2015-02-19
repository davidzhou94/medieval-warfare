package ca.mcgill.cs.comp361.nwo.mwgs.model;

import java.util.ArrayList;
import java.util.Iterator;



/**
 * GameEngine class definition.
 * Generated by the TouchCORE code generator.
 */
public class GameEngine {
    
    private Game gameState;
    
    public void buildRoad(Unit u) {
        Tile tile;
        UnitType unitType;
        Structure structure;
        int roundCount;
        unitType = u.getUnitType();
        tile = u.getTile();
        structure = tile.getStructure();
        if (unitType == UnitType.PEASANT && structure != StructureType.ROAD) {
            u.setCurrentAction();
            roundCount = gameState.getRoundCount();
            return roundCount;
            roundCount = roundCount 1;
            u.setImmobileUntilRound(roundCount);
            tile.setStructure(StructureType.ROAD);
        }
    }

    public void takeoverTile(Tile dest) {
        Village destVillage;
        Village uv;
        Region ur;
        Unit u;
        Region destRegion;
        int w;
        int g;
        destVillage = dest.getVillage();
        destRegion = dest.getRegion();
        u = dest.getUnit();
        uv = u.getVillage();
        ur = uv.getRegion();
        if (v!=null) {
            g = destVillage.getGold();
            return gold;
            w = destVillage.getWood();
            return wood;
            uv.transactGold(g);
            uv.transactWood(w);
            destVillage.kill();
        }
        destRegion.removeTile(dest);
        ur.addTile(dest);
        splitRegions(u);
        checkWinConditions();
    }

    private void checkWinConditions() {
        Player p;
        ArrayList<Village> v;
        ArrayList<Player> players;
        Iterator<Player> iterator;
        boolean remove;
        players = gameState.getPlayers();
        iterator = players.iterator();
        for (iterator.hasNext()) {
            p = iterator.next();
            v = p.getVillages();
            return controlledVillages;
            if (v.size() <= 0) {
                remove = players.remove(p);
            }
        }
        if (players.size() <= 1) {
            gameState.setHasWon(true);
            // Increment the number of wins for the winner and the number of losses for the losers.;
        }
    }

    public void newGame(ArrayList<Player> players, String mapChosen) {
        Map map;
        Game game;
        Player.setUpPlayers(players);
        map = Map.setUpMap(players, mapChosen);
        game = new Game();
        game.setPlayers(players);
        game.setMap(map);
        setGameState(game);
    }

    public void beginTurn(Game g, Player p) {
        Map map;
        map = g.getMap();
        phaseBuild(p);
        phaseTombstone(p, map);
        phaseIncome(p);
        phasePayment(p);
    }

    public void upgradeUnit(Unit u, UnitType newLevel) {
        int c1;
        Village v;
        int g;
        UnitType t;
        int c2;
        t = u.getUnitType();
        c1 = Unit.unitLevel(newLevel);
        c2 = Unit.unitLevel(t);
        v = u.getVillage();
        g = v.getGold();
        cost = c1 - c2;
        if (g >= cost) {
            u.setUnitType(newLevel);
            v.transactGold("-cost");
        }
    }

    public void upgradeVillage(Village v, VillageType newLevel) {
        VillageType villageType;
        int c2;
        int c1;
        int w;
        villageType = v.getVillageType();
        c1 = Village.villageCost(newLevel);
        c2 = Village.villageCost(villageType);
        w = v.getWood();
        cost = c1-c2;
        if (w >= cost) {
            v.setVillageType(newLevel);
            v.transactWood("-cost");
        }
    }

    public void buildTower(Tile t) {
        /* TODO: No message view defined */
    }

    public Game getGameState() {
        /* TODO: No message view defined */
        return null;
    }

    public void setGameState(Game gameState) {
        /* TODO: No message view defined */
    }

    private MoveType getMoveType(Unit u, Tile dest) {
        TerrainType landOnDest;
        Village villageOnDest;
        Tile origin;
        int unitLevel;
        List<Tile> adjacent;
        Structure structureOnDest;
        Unit unitOnDest;
        // Please refer to the attached pseudocode;
        // This sequence diagram does not check every case we intend to check;
        origin = u.getTile();
        adjacent = origin.getNeighbours();
        if (dest is in adjacent) {
            landOnDest = dest.getTerrainType();
            return terrainType;
            structureOnDest = dest.getStructure();
            return occupyingStructure;
            unitOnDest = dest.getUnit();
            return unit;
            villageOnDest = dest.getVillage();
            return myVillage;
            if (unitOnDest != null) {
                unitLevel = Unit.unitLevel(u);
                if (unitOnDest.getControllingPlayer() == u.getControllingPlayer()) {
                    return ;
                } else if (unitOnDestLevel >= unitLevel) {
                    return ;
                } else if (unitOnDestLevel < unitLevel) {
                    return ;
                }
            }
        } else if (dest is not in adjacent) {
            return ;
        }
    }

    private void gatherWood(Unit u) {
        Village village;
        Tile tile;
        int roundCount;
        tile = u.getTile();
        tile.setTerrainType();
        roundCount = gameState.getRoundCount();
        rountCount += 1;
        u.setImmobileUntilRound(roundCount);
        u.setCurrentAction();
        village = u.getVillage();
        village.transactWood(1);
    }

    private void clearTombstone(Unit u) {
        Tile tile;
        int roundCount;
        tile = u.getTile();
        tile.setStructure();
        roundCount = gameState.getRoundCount();
        roundCount += 1;
        u.setImmobileUntilRound(roundCount);
        u.setCurrentAction();
    }

    private void trampleMeadow(Unit u) {
        Tile tile;
        tile = u.getTile();
        tile.setTerrainType();
    }

    private void phaseTombstone(Player p, Map m) {
        m.replaceTombstonesWithTrees(p);
    }

    private void phaseBuild(Player p) {
        ArrayList<Unit> units;
        ActionType currentAction;
        Iterator<Unit> iterator;
        Tile tile;
        Unit u;
        units = p.getUnits();
        iterator = units.iterator();
        for (iterator.hasNext() == true) {
            u = iterator.next();
            currentAction = u.getCurrentAction();
            return currentAction;
            if (currentAction == ActionType.STARTCULTIVATING) {
                u.setCurrentAction();
            } else if (currentAction == ActionTypeFINISHCULTIVATING) {
                u.setCurrentAction();
                tile = u.getTile();
                return tile;
                tile.setTerrainType();
            } else if (currentAction == ActionType.BUILDINGROAD) {
                // The road structure was already placed as part of the build road operation;
                u.setCurrentAction();
            } else if (else) {
                u.setCurrentAction();
            }
        }
    }

    private void phaseIncome(Player p) {
        Village v;
        int totalIncome;
        Iterator<Village> iterator;
        ArrayList<Village> villages;
        villages = p.getVillages();
        iterator = villages.iterator();
        if (iterator.hasNext() == true) {
            v = iterator.next();
            totalIncome = v.getTotalIncome();
            v.transactGold(totalIncome);
        }
    }

    private void phasePayment(Player p) {
        Village v;
        Iterator<Unit> iterator;
        ArrayList<Village> villages;
        Unit u;
        int totalUpkeep;
        ArrayList<Unit> supportedUnits;
        Iterator<Village> villageIterator;
        villages = p.getVillages();
        villageIterator = villages.iterator();
        while (iterator.hasNext()) {
            v = villageIterator.next();
            totalUpkeep = v.getTotalUpkeep();
            if (v.getGold() < totalUpkeep) {
                supportedUnits = v.getSupportedUnits();
                return supportedUnits;
                iterator = supportedUnits.iterator();
                for (iterator.hasNext()) {
                    u = iterator.next();
                    u.kill();
                }
            } else if (v.getGold() >= totalUpkeep) {
                totalUpkeep = totalUpkeep * -1;
                v.transactGold(totalUpkeep);
            }
        }
    }

    public void moveUnit(Unit u, Tile dest) {
        int roundCount;
        Unit otherUnit;
        Tile origin;
        int roundCount;
        Unit enemyUnit;
        MoveType moveType;
        moveType = getMoveType(u, dest);
        if (moveType == MoveType.INVALIDMOVE) {
            Do nothing;
        } else if (moveType == MoveType.COMBINEUNITS) {
            otherUnit = dest.getUnit();
            return unit;
            combineUnits(otherUnit, u);
        } else if (else) {
            if (dest.getControllingPlayer() != u.getControllingPlayer() && dest.getUnit() != null) {
                //If there is an enemy unit, we kill it because we checked whether it was defeatable in getMoveType;
                enemyUnit = dest.getUnit();
                return unit;
                enemyUnit.kill();
            }
            origin = u.getTile();
            return tile;
            origin.setUnit();
            dest.setUnit(u);
            u.setTile(dest);
            if (moveType == MoveType.TRAMPLEMEADOW) {
                trampleMeadow(u);
            } else if (moveType == MoveType.CLEARTOMB) {
                clearTombstone(u);
            } else if (moveType == MoveType.GATHERWOOD) {
                gatherWood(u);
            }
            if (dest.getControllingPlayer() == null) {
                combineRegions(u);
                roundCount = gameState.getRoundCount();
                return roundCount;
                rountCount += 1;
                u.setImmobileUntilRound(roundCount);
                u.setCurrentAction();
            } else if (dest.getControllingPlayer() != u.getControllingPlayer()) {
                takeoverTile(dest);
                roundCount = gameState.getRoundCount();
                return roundCount;
                rountCount += 1;
                u.setImmobileUntilRound(roundCount);
                u.setCurrentAction();
            }
        }
    }

    private void combineUnits(Unit dest, Unit moved) {
        // This operation is a long series of if switches. Please see pseudocode.;
    }

    private void combineRegions(Unit u) {
        // This operation is two nested loops followed by two nested ifs. Please see the attached pseudocode.;
        u.setCurrentAction(null);
    }

    private void splitRegions(Unit u) {
        Region region;
        Village village;
        List<Tile> neighbours;
        Tile tile;
        tile = u.getTile();
        neighbours = tile.getNeighbours();
        // Check if the tiles around the unit belong to a region that has been split.;
        if (region has been split) {
            // Determine the set of connected tiles in each of the new regions;
            for (for each new set of tiles) {
                if (set of tiles is greater than or equal to 3) {
                    // Create a new region with the set of connected tiles.;
                    region = new Region();
                    region.createVillage();
                    village = region.getVillage();
                    return controllingVillage;
                    // Iterate through every tile, set the village of each unit found to the new village.;
                } else if (set of tiles is fewer than 3) {
                    // Remove all the tiles in the set from the region.;
                    // If the tile contains a unit, kill the unit.;
                }
            }
        }
    }
}
