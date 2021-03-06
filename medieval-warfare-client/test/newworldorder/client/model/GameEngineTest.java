package newworldorder.client.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import newworldorder.client.shared.StructureType;
import newworldorder.client.shared.TerrainType;
import newworldorder.client.shared.UnitType;
import newworldorder.client.shared.VillageType;

import org.junit.Before;
import org.junit.Test;

public class GameEngineTest {
	private Map aMap;
	private Village village1, village2;
	private Player p1, p2;
	private GameEngine gameEngine;

	@Before
	public void setUp() {
		p1 = new Player("Yung Lean");
		p2 = new Player("2 Chainz");
		List<Player> playerList = new ArrayList<Player>();
		playerList.add(p1);
		playerList.add(p2);
		aMap = new Map(10, 10);
		Game aGame = new Game(playerList, aMap);
		gameEngine = new GameEngine();
		gameEngine.setGameState(aGame);
		aGame.setTurnOf(p1);
		Set<Tile> reg1 = new HashSet<Tile>();
		reg1.add(aMap.getTile(0, 0));
		reg1.add(aMap.getTile(1, 0));
		reg1.add(aMap.getTile(0, 1));
		village1 = new Village(aMap.getTile(0, 0), p1, reg1);

		Set<Tile> reg2 = new HashSet<Tile>();
		reg2.add(aMap.getTile(1, 1));
		reg2.add(aMap.getTile(2, 1));
		reg2.add(aMap.getTile(1, 2));
		reg2.add(aMap.getTile(2, 2));
		village2 = new Village(aMap.getTile(2, 2), p2, reg2);
	}

	@Test
	public void testBuildUnit() {
		village1.transactGold(10);
		assertEquals(aMap.getTile(0, 1).getUnit(), null);
		gameEngine.buildUnit(village1, aMap.getTile(0, 1), UnitType.PEASANT);
		assertEquals(aMap.getTile(0, 1).getUnit().getUnitType(), UnitType.PEASANT);
		// Now try making a unit without enough money
		gameEngine.buildUnit(village1, aMap.getTile(1, 0), UnitType.PEASANT);
		assertEquals(aMap.getTile(1, 0).getUnit(), null);
		// Try to make a unit that is too strong
		village1.transactGold(50);
		gameEngine.buildUnit(village1, aMap.getTile(1, 0), UnitType.SOLDIER);
		assertEquals(aMap.getTile(1, 0).getUnit(), null);
	}

	@Test
	public void testBuildRoad() {
		new Unit(UnitType.PEASANT, village1, aMap.getTile(1, 0));
		assertFalse(aMap.getTile(1, 0).getStructure() == StructureType.ROAD);
		gameEngine.buildRoad(1, 0);
		assertTrue(aMap.getTile(1, 0).getStructure() == StructureType.ROAD);

		new Unit(UnitType.KNIGHT, village1, aMap.getTile(0, 1));
		assertFalse(aMap.getTile(0, 1).getStructure() == StructureType.ROAD);
		gameEngine.buildRoad(0, 1);
		assertFalse(aMap.getTile(0, 1).getStructure() == StructureType.ROAD);
	}

	@Test
	public void testNewGame() {
		// TODO fail("Not yet implemented");
	}

	@Test
	public void testEndTurn() {
		gameEngine.endTurn();
		assertEquals(gameEngine.getGameState().getCurrentTurnPlayer(), p2);
		gameEngine.endTurn();
		assertEquals(gameEngine.getGameState().getCurrentTurnPlayer(), p1);
	}

	@Test
	public void testUpgradeUnit() {
		Unit aUnit = new Unit(UnitType.PEASANT, village1, aMap.getTile(1, 0));
		village1.setVillageType(VillageType.FORT);
		assertEquals(aUnit.getVillage().getWood(), 0);
		gameEngine.upgradeUnit(aUnit, UnitType.KNIGHT);
		assertEquals(aUnit.getUnitType(), UnitType.PEASANT);
		aUnit.getVillage().transactGold(20);
		gameEngine.upgradeUnit(aUnit, UnitType.KNIGHT);
		assertEquals(aUnit.getUnitType(), UnitType.KNIGHT);
	}

	@Test
	public void testUpgradeVillage() {
		// not enough wood
		gameEngine.upgradeVillage(village1, VillageType.FORT);
		assertEquals(village1.getVillageType(), VillageType.HOVEL);
		// proper upgrade
		village1.transactWood(16);
		gameEngine.upgradeVillage(village1, VillageType.FORT);
		assertEquals(village1.getVillageType(), VillageType.FORT);
		assertEquals(village1.getWood(), 0);
	}

	@Test
	public void testBuildTower() {
		// not enough wood
		Tile buildOn = aMap.getTile(0, 1);
		gameEngine.buildTower(0 ,1);
		assertEquals(buildOn.getStructure(), null);
		// proper build
		village1.transactWood(5);
		gameEngine.buildTower(0, 1);
		assertEquals(buildOn.getStructure(), StructureType.WATCHTOWER);
		assertEquals(village1.getWood(), 0);
	}

	@Test
	public void testMoveUnit_InvalidMove() {
		// Case where enemy unit is stronger
		Unit ally = new Unit(UnitType.INFANTRY, village1, aMap.getTile(1, 0));
		Unit enemy = new Unit(UnitType.KNIGHT, village2, aMap.getTile(1, 1));
		gameEngine.moveUnit(ally, aMap.getTile(1, 1));
		assertEquals(aMap.getTile(1, 0).getUnit(), ally);
		assertEquals(aMap.getTile(1, 1).getUnit(), enemy);
		// Case where dest tile is not adjacent.
		gameEngine.moveUnit(ally, aMap.getTile(2, 2));
		assertEquals(aMap.getTile(1, 0).getUnit(), ally);
		// Case where enemy unit is weaker, but on a watchtower that is stronger
		// than attacker
		ally.setUnitType(UnitType.INFANTRY);
		enemy.setUnitType(UnitType.PEASANT);
		enemy.getTile().setStructure(StructureType.WATCHTOWER);
		gameEngine.moveUnit(ally, enemy.getTile());
		assertEquals(aMap.getTile(1, 0).getUnit(), ally);
		// Case where there is just a watchtower
		enemy.getVillage().removeUnit(enemy);
		enemy.getTile().setUnit(null);
		enemy.getTile().setStructure(StructureType.TOMBSTONE);
		aMap.getTile(1, 1).setStructure(StructureType.WATCHTOWER);
		gameEngine.moveUnit(ally, aMap.getTile(1, 1));
		assertEquals(aMap.getTile(1, 0).getUnit(), ally);
		// Case where weak unit tries to take village
		aMap.getTile(1, 1).setRegion(aMap.getTile(1, 0).getRegion());
		aMap.getTile(1, 1).setTerrainType(TerrainType.GRASS);
		gameEngine.moveUnit(ally, aMap.getTile(1, 1));
		assertEquals(aMap.getTile(1, 0).getRegion(), aMap.getTile(1, 1).getRegion());
		assertEquals(aMap.getTile(1, 1).getUnit(), ally);
		gameEngine.moveUnit(ally, aMap.getTile(2, 2));
		assertEquals(aMap.getTile(1, 1).getUnit(), ally);
		// Knight cant walk onto trees or tombstones
		ally.setUnitType(UnitType.KNIGHT);
		aMap.getTile(1, 0).setTerrainType(TerrainType.TREE);
		aMap.getTile(0, 1).setStructure(StructureType.TOMBSTONE);
		gameEngine.moveUnit(ally, aMap.getTile(1, 0));
		assertEquals(aMap.getTile(1, 1).getUnit(), ally);
		gameEngine.moveUnit(ally, aMap.getTile(0, 1));
		assertEquals(aMap.getTile(1, 1).getUnit(), ally);
	}

	@Test
	public void testMoveUnit_FreeMove() {
		Unit ally = new Unit(UnitType.KNIGHT, village1, aMap.getTile(1, 0));
		new Unit(UnitType.SOLDIER, village2, aMap.getTile(1, 1));
		// Kill enemy unit
		gameEngine.moveUnit(ally, aMap.getTile(1, 1));
		assertEquals(aMap.getTile(1, 1).getUnit(), ally);
		assertEquals(aMap.getTile(1, 1).getRegion(), aMap.getTile(1, 0).getRegion());
		// Move onto grass in own territory
		aMap.getTile(0, 1).setTerrainType(TerrainType.GRASS);
		ally.setImmobileUntilRound(0);
		gameEngine.moveUnit(ally, aMap.getTile(0, 1));
		assertEquals(aMap.getTile(0, 1).getUnit(), ally);
		ally.setImmobileUntilRound(0);
		aMap.getTile(1, 2).setTerrainType(TerrainType.GRASS);
		gameEngine.moveUnit(ally, aMap.getTile(1, 2));
		assertEquals(aMap.getTile(1, 2).getUnit(), ally);
		// Take over village
		ally.setImmobileUntilRound(0);
		aMap.getTile(2, 2).setTerrainType(TerrainType.GRASS);
		gameEngine.moveUnit(ally, aMap.getTile(2, 2));
		assertEquals(aMap.getTile(2, 2).getUnit(), ally);
		// Move onto meadow as weak unit
		ally.setUnitType(UnitType.PEASANT);
		aMap.getTile(1, 2).setTerrainType(TerrainType.MEADOW);
		ally.setImmobileUntilRound(0);
		gameEngine.moveUnit(ally, aMap.getTile(1, 2));
		assertEquals(aMap.getTile(1, 2).getTerrainType(), TerrainType.MEADOW);
		assertEquals(aMap.getTile(1, 2).getUnit(), ally);

		// Walk into neutral territory
		ally.setImmobileUntilRound(0);
		aMap.getTile(1, 3).setTerrainType(TerrainType.GRASS);
		gameEngine.moveUnit(ally, aMap.getTile(1, 3));
		assertEquals(aMap.getTile(1, 3).getUnit(), ally);
	}

	@Test
	public void testMoveUnit_CombineUnits() {
		Unit u1 = new Unit(UnitType.INFANTRY, village1, aMap.getTile(1, 1));
		new Unit(UnitType.INFANTRY, village1, aMap.getTile(1, 0));
		village1.setVillageType(VillageType.FORT);
		gameEngine.moveUnit(u1, aMap.getTile(1, 0));
		assertEquals(aMap.getTile(1, 1).getUnit(), null);
		assertEquals(aMap.getTile(1, 0).getUnit().getUnitType(), UnitType.KNIGHT);
	}

	@Test
	public void testMoveUnit_TrampleMeadow() {
		Unit u1 = new Unit(UnitType.KNIGHT, village1, aMap.getTile(1, 1));
		aMap.getTile(1, 0).setTerrainType(TerrainType.MEADOW);
		gameEngine.moveUnit(u1, aMap.getTile(1, 0));
		assertEquals(aMap.getTile(1, 0).getUnit(), u1);
		assertEquals(aMap.getTile(1, 0).getTerrainType(), TerrainType.GRASS);
	}

	@Test
	public void testMoveUnit_ClearTomb() {
		Unit u1 = new Unit(UnitType.PEASANT, village1, aMap.getTile(1, 1));
		Tile dest = aMap.getTile(1, 0);
		dest.setStructure(StructureType.TOMBSTONE);
		gameEngine.moveUnit(u1, dest);
		assertEquals(dest.getStructure(), null);
		assertEquals(dest.getUnit(), u1);
	}

	@Test
	public void testMoveUnit_GatherWood() {
		Unit u1 = new Unit(UnitType.PEASANT, village1, aMap.getTile(0, 0));
		Tile dest = aMap.getTile(1, 0);
		dest.setTerrainType(TerrainType.TREE);
		gameEngine.moveUnit(u1, dest);
		assertEquals(dest.getTerrainType(), TerrainType.GRASS);
		assertEquals(dest.getUnit(), u1);
	}

	@Test
	public void testCultivateMeadow() {
		// Strong unit cant cultivate
		village1.transactGold(10);

		Tile t1 = aMap.getTile(0, 1);
		Unit u1 = new Unit(UnitType.KNIGHT, village1, t1);
		t1.setTerrainType(TerrainType.GRASS);
		gameEngine.cultivateMeadow(0, 1);
		assertEquals(t1.getTerrainType(), TerrainType.GRASS);
		// Peasant can
		u1.setUnitType(UnitType.PEASANT);
		gameEngine.cultivateMeadow(0, 1);
		assertEquals(u1.getCurrentAction(), ActionType.STARTCULTIVATING);
		gameEngine.endTurn();
		gameEngine.endTurn();
		assertEquals(u1.getCurrentAction(), ActionType.FINISHCULTIVATING);
		gameEngine.endTurn();
		gameEngine.endTurn();
		assertEquals(u1.getCurrentAction(), ActionType.READYFORORDERS);
		assertEquals(u1.getTile(), t1);
		assertEquals(t1.getTerrainType(), TerrainType.MEADOW);
	}

	@Test
	public void testCombineRegions() {
		Set<Tile> reg3 = new HashSet<Tile>();
		reg3.add(aMap.getTile(0, 3));
		reg3.add(aMap.getTile(0, 4));
		reg3.add(aMap.getTile(1, 3));
		Village village3 = new Village(aMap.getTile(1, 3), p1, reg3);
		village3.setVillageType(VillageType.FORT);
		assertTrue(village3.getRegion().getTiles().contains(aMap.getTile(0, 3)));
		assertEquals(village3.getRegion(), aMap.getTile(0, 3).getRegion());
		Unit u1 = new Unit(UnitType.SOLDIER, village1, aMap.getTile(0, 1));
		Unit u2 = new Unit(UnitType.PEASANT, village1, aMap.getTile(1, 0));
		aMap.getTile(0, 2).setTerrainType(TerrainType.GRASS);
		gameEngine.moveUnit(u1, aMap.getTile(0, 2));
		assertTrue(village3.getRegion().getTiles().contains(aMap.getTile(0, 3)));
		assertEquals(village3.getRegion(), aMap.getTile(0, 3).getRegion());
		assertEquals(village3.getRegion(), aMap.getTile(0, 2).getRegion());
		assertEquals(village3.getRegion(), aMap.getTile(0, 1).getRegion());
		assertEquals(aMap.getTile(1, 0).getRegion().getVillage(), village3);
		assertEquals(u1.getVillage(), village3);
		assertEquals(u2.getVillage(), village3);
	}

}
