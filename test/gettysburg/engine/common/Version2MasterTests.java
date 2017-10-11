/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016 Gary F. Pollice
 *******************************************************************************/

package gettysburg.engine.common;

import static gettysburg.common.ArmyID.*;
import static gettysburg.common.Direction.*;
import static gettysburg.common.UnitSize.*;
import static gettysburg.common.UnitType.*;
import static gettysburg.common.GbgGameStatus.IN_PROGRESS;
import static gettysburg.common.GbgGameStep.*;
import static student.gettysburg.engine.GettysburgFactory.*;
import java.util.*;
import static org.junit.Assert.*;
import org.junit.*;
import gettysburg.common.*;
import gettysburg.common.exceptions.*;
import student.gettysburg.engine.common.GbgUnitImpl;

/**
 * Test cases for release 2.
 * @version Sep 30, 2017
 */
public class Version2MasterTests
{
	private GbgGame game;
	private TestGbgGame testGame;
	private GbgUnit gamble, rowley, schurz, devin, heth, rodes, dance, hampton;

	@Before
	public void setup()
	{
		game = testGame = makeTestGame();
		gamble = game.getUnitsAt(makeCoordinate(11, 11)).iterator().next();
		devin = game.getUnitsAt(makeCoordinate(13, 9)).iterator().next();
		heth = game.getUnitsAt(makeCoordinate(8, 8)).iterator().next();
		// These work if the student kept that GbgUnitImpl.makeUnit method
		rowley = GbgUnitImpl.makeUnit(UNION, 3, NORTHEAST, "Rowley", 2, DIVISION, INFANTRY);
		schurz = GbgUnitImpl.makeUnit(UNION, 2, NORTH, "Schurz", 2, DIVISION, INFANTRY);
		rodes = GbgUnitImpl.makeUnit(CONFEDERATE, 4, SOUTH, "Rodes", 2, DIVISION, INFANTRY);
		dance = GbgUnitImpl.makeUnit(CONFEDERATE, 2, EAST, "Dance", 4, BATTALION, ARTILLERY);
		hampton = GbgUnitImpl.makeUnit(CONFEDERATE, 1, SOUTH, "Hampton", 4, BRIGADE, CAVALRY);
		// If the previous statements fail, comment them out and try these
//		rowley = TestUnit.makeUnit(UNION, 3, NORTHEAST, "Rowley", 2);
//		schurz = TestUnit.makeUnit(UNION,  2,  NORTH, "Shurz", 2);
//		rodes = TestUnit.makeUnit(CONFEDERATE, 4, SOUTH, "Rodes", 2);
//		dance = TestUnit.makeUnit(CONFEDERATE, 2, EAST, "Dance", 4);
//		hampton = TestUnit.makeUnit(CONFEDERATE, 1, SOUTH, "Hampton", 4);
//		devin.setFacing(SOUTH);
//		gamble.setFacing(WEST);
//		heth.setFacing(EAST);
	}
	
	// Initial setup tests taken as is from Version 1 tests
	@Test
	public void gameTurnIsOneOnInitializedGame()
	{
		assertEquals(1, game.getTurnNumber());
	}

	@Test
	public void initialGameStatusIsInProgress()
	{
		assertEquals(IN_PROGRESS, game.getGameStatus());
	}

	@Test
	public void gameStepOnInitializedGameIsUMOVE()
	{
		assertEquals(UMOVE, game.getCurrentStep());
	}

	@Test
	public void correctSquareForGambleUsingWhereIsUnit()
	{
		assertEquals(makeCoordinate(11, 11), game.whereIsUnit("Gamble", UNION));
	}

	@Test
	public void correctSquareForGambleUsingGetUnitsAt()
	{
		GbgUnit unit = game.getUnitsAt(makeCoordinate(11, 11)).iterator().next();
		assertNotNull(unit);
		assertEquals("Gamble", unit.getLeader());
	}

	@Test
	public void devinFacesSouth()
	{
		assertEquals(SOUTH, game.getUnitFacing(devin));
	}
	
	// Game step and turn tests
	@Test
	public void unionBattleFollowsUnionMove()
	{
		game.endStep();
		assertEquals(UBATTLE, game.getCurrentStep());
	}

	@Test
	public void confederateMoveFollowsUnionBattle()
	{
		game.endStep();
		game.endStep();
		assertEquals(CMOVE, game.getCurrentStep());
	}

	@Test
	public void confederateBattleFollowsConfederateMove()
	{
		game.endStep();
		game.endStep();
		assertEquals(CBATTLE, game.endStep());
	}

	@Test
	public void turnOneDuringConfederateBattle()
	{
		game.endStep();
		game.endStep();
		game.endStep();
		assertEquals(1, game.getTurnNumber());
	}

	@Test
	public void goToTurn2()
	{
		game.endStep();
		game.endStep();
		game.endStep();
		game.endStep();
		assertEquals(2, game.getTurnNumber());
	}

	@Test
	public void startOfTurn2IsUMOVEStep()
	{
		game.endStep();
		game.endStep();
		game.endStep();
		game.endStep();
		assertEquals(UMOVE, game.getCurrentStep());
	}

	// Movement tests
	@Test
	public void gambleMovesNorth()
	{
		game.moveUnit(gamble, makeCoordinate(11, 11), makeCoordinate(11, 10));
		assertEquals(makeCoordinate(11, 10), game.whereIsUnit(gamble));
		assertNull(">> Documentation says this should be null, not an empty array",
				game.getUnitsAt(makeCoordinate(11, 11)));
	}

	@Test
	public void devinMovesSouth()
	{
		game.moveUnit(devin, makeCoordinate(13, 9), makeCoordinate(13, 11));
		assertEquals(makeCoordinate(13, 11), game.whereIsUnit(devin));
	}

	@Test
	public void hethMovesEast()
	{
		game.endStep();
		game.endStep();
		game.moveUnit(heth, makeCoordinate(8, 8), makeCoordinate(10, 8));
		assertEquals(heth, game.getUnitsAt(makeCoordinate(10, 8)).iterator().next());
	}
	
	@Test
	public void devinMovesSouthUsingANonStandardCoordinate()
	{
		game.moveUnit(devin, new TestCoordinate(13, 9), makeCoordinate(13, 11));
		assertEquals(makeCoordinate(13, 11), game.whereIsUnit(devin));
	}
	
	// Tests requiring the test double
	// Movement tests
	@Test
	public void stackedEntryUnitIsAsCorrectLocation()
	{
	    testGame.setGameTurn(8);
	    testGame.setGameStep(CBATTLE);
	    game.endStep();  // step -> UMOVE, turn -> 9
	    assertEquals(makeCoordinate(22, 22), game.whereIsUnit("Geary", UNION));
	}

	@Test
	public void stackedEntryUnitsAreNotMoved()
	{
	    testGame.setGameTurn(8);
	    testGame.setGameStep(CBATTLE);
	    game.endStep();  // step -> UMOVE, turn -> 9
	    game.endStep();  // step -> UBATTLE
	    assertNull(game.getUnitsAt(makeCoordinate(22, 22)));
	}
	
	@Test
	public void unitsStackedProperlyAtStartOfGame()
	{
		// Move units at (7, 28) during UMOVE, turn 1
		Collection<GbgUnit> units = game.getUnitsAt(makeCoordinate(7, 28));
		assertEquals(4, units.size());
	}
	
	@Test
	public void allStackedUnitsAtStartOfGameMove()
	{
		Iterator<GbgUnit> units = game.getUnitsAt(makeCoordinate(7, 28)).iterator();
		game.moveUnit(units.next(), makeCoordinate(7, 28), makeCoordinate(5, 28));
		game.moveUnit(units.next(), makeCoordinate(7, 28), makeCoordinate(6, 28));
		game.moveUnit(units.next(), makeCoordinate(7, 28), makeCoordinate(8, 28));
		game.moveUnit(units.next(), makeCoordinate(7, 28), makeCoordinate(9, 28));
		Collection<GbgUnit> remaining = game.getUnitsAt(makeCoordinate(7, 28));
		assertTrue(remaining == null || remaining.isEmpty());
	}
	
	@Test
	public void someEntryUnitsRemainAndAreRemoved()
	{
		Iterator<GbgUnit> units = game.getUnitsAt(makeCoordinate(7, 28)).iterator();
		game.moveUnit(units.next(), makeCoordinate(7, 28), makeCoordinate(5, 28));
		game.moveUnit(units.next(), makeCoordinate(7, 28), makeCoordinate(6, 28));
		Collection<GbgUnit> remaining = game.getUnitsAt(makeCoordinate(7, 28));
		assertEquals(2, remaining.size());
		game.endStep();
		remaining = game.getUnitsAt(makeCoordinate(7, 28));
		assertTrue(remaining == null || remaining.isEmpty());
	}
	
	@Test(expected=GbgInvalidMoveException.class)
	public void tryToMoveThroughEnemyZOC()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 10, 10, SOUTH);		// ZOC = [(9, 11), (10, 11), (11, 11)]
		testGame.putUnitAt(hampton, 13, 10, SOUTH);	// ZOC = [(12, 11), (13, 11), (14, 11)]
		testGame.putUnitAt(devin, 11, 12, SOUTH);
		testGame.setGameStep(UMOVE);
		game.moveUnit(devin, makeCoordinate(11, 12), makeCoordinate(11, 9));
	}
	
	// Battle tests
	
	@Test
	public void hethDefeatsDevin()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 5, 5, SOUTH);
		testGame.putUnitAt(devin, 5, 7, SOUTH);
		testGame.setGameStep(CMOVE);
		game.moveUnit(heth, makeCoordinate(5,5), makeCoordinate(5, 6));
		game.endStep();		// CBATTLE
		BattleDescriptor battle = game.getBattlesToResolve().iterator().next();	// Only 1
		assertEquals(BattleResult.DELIM, game.resolveBattle(battle).getBattleResult());
	}
	
	@Test
	public void hethDefeatsDevinAndGamble()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 5, 5, SOUTH);
		testGame.putUnitAt(devin, 5, 7, SOUTH);
		testGame.putUnitAt(gamble, 6, 7, SOUTH);
		testGame.setGameStep(CMOVE);
		game.moveUnit(heth, makeCoordinate(5,5), makeCoordinate(5, 6));
		game.endStep();		// CBATTLE
		BattleDescriptor battle = game.getBattlesToResolve().iterator().next();	// Only 1
		assertEquals(BattleResult.DELIM, game.resolveBattle(battle).getBattleResult());
	}
	
	@Test
	public void twoBattles()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 5, 5, SOUTH);
		testGame.putUnitAt(devin, 5, 7, SOUTH);
		testGame.putUnitAt(rodes, 20, 20, NORTH);
		testGame.putUnitAt(schurz, 20, 18, SOUTHWEST);
		testGame.setGameStep(CMOVE);
		game.moveUnit(heth, makeCoordinate(5,5), makeCoordinate(5, 6));
		game.moveUnit(rodes, makeCoordinate(20, 20), makeCoordinate(20, 19));
		game.endStep();		// CBATTLE
		Collection<BattleDescriptor> battles = game.getBattlesToResolve();
		BattleDescriptor battle = battles.iterator().next();
		if (battles.size() == 1) {
			assertTrue(battle.getAttackers().contains(heth)); 
		} else {
			assertTrue(battle.getAttackers().contains(heth) || battle.getAttackers().contains(rodes));
		}
	}
	
	@Test
	public void fightTwoBattles()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.setGameStep(UMOVE);
		testGame.putUnitAt(heth, 5, 5, SOUTH);
		testGame.putUnitAt(rowley, 5, 7, NORTH);
		testGame.putUnitAt(hampton, 18, 18, EAST);
		testGame.putUnitAt(gamble, 20, 18, WEST);
		TestBattleDescriptor bd = new TestBattleDescriptor();
		bd.addAttacker(rowley);
		bd.addDefender(heth);
		TestBattleDescriptor bd1 = new TestBattleDescriptor();
		bd1.addAttacker(gamble);
		bd1.addDefender(hampton);
		game.moveUnit(rowley, makeCoordinate(5, 7), makeCoordinate(5, 6));
		game.moveUnit(gamble, makeCoordinate(20, 18), makeCoordinate(19, 18));
		game.endStep();	// CBATTLE
		assertEquals(BattleResult.EXCHANGE, game.resolveBattle(bd).getBattleResult());
		assertEquals(BattleResult.EXCHANGE, game.resolveBattle(bd1).getBattleResult());
	}
	
	@Test
	public void attackerGetsEliminated()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 5, 5, SOUTH);
		testGame.putUnitAt(devin, 5, 7, NORTH);
		testGame.setGameStep(UMOVE);
		assertEquals(heth, game.getUnitsAt(makeCoordinate(5, 5)).iterator().next());
		game.moveUnit(devin, makeCoordinate(5,7), makeCoordinate(5, 6));
		game.endStep();		// UBATTLE
		BattleDescriptor battle = game.getBattlesToResolve().iterator().next();	// Only 1
		assertEquals(BattleResult.AELIM, game.resolveBattle(battle).getBattleResult());
	}
	
	@Test(expected=GbgInvalidActionException.class)
	public void unitTriesToFightTwoBattesInSameTurn()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.setGameStep(UMOVE);
		testGame.putUnitAt(heth, 5, 5, SOUTH);
		testGame.putUnitAt(hampton, 7, 5, SOUTH);
		testGame.putUnitAt(schurz, 6, 7, NORTH);
		game.moveUnit(schurz, makeCoordinate(6, 7), makeCoordinate(6, 6));
		game.endStep();	// UBATTLE
		TestBattleDescriptor bd = new TestBattleDescriptor();
		bd.addAttacker(schurz);
		bd.addDefender(heth);
		TestBattleDescriptor bd1 = new TestBattleDescriptor();
		bd1.addAttacker(schurz);
		bd1.addDefender(hampton);
		game.resolveBattle(bd);
		game.resolveBattle(bd1);
	}
	
	@Test(expected=Exception.class)
	public void notAllUnitsHaveFoughtAtEndOfBattleStep()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.setGameStep(UMOVE);
		testGame.putUnitAt(heth, 5, 5, SOUTH);
		testGame.putUnitAt(hampton, 7, 5, SOUTH);
		testGame.putUnitAt(schurz, 6, 7, NORTH);
		game.moveUnit(schurz, makeCoordinate(6, 7), makeCoordinate(6, 6));
		game.endStep();	// UBATTLE
		TestBattleDescriptor bd = new TestBattleDescriptor();
		bd.addAttacker(schurz);
		bd.addDefender(heth);
		game.resolveBattle(bd);
		game.endStep(); 	// CMOVE: hampton did not engage
	}
	
	@Test(expected=Exception.class)
	public void improperBatteleDescriptor()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.setGameStep(UMOVE);
		testGame.putUnitAt(heth, 22, 5, SOUTH);
		testGame.putUnitAt(hampton, 7, 5, SOUTH);
		testGame.putUnitAt(schurz, 6, 7, NORTH);
		game.moveUnit(schurz, makeCoordinate(6, 7), makeCoordinate(6, 6));
		game.endStep();	// UBATTLE
		TestBattleDescriptor bd = new TestBattleDescriptor();
		bd.addAttacker(schurz);
		bd.addDefender(heth);
		game.resolveBattle(bd);		// heth is not in schurz' ZOC
	}
}

class TestCoordinate implements Coordinate
{
	private int x, y;
	
	/**
	 * @return the x
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY()
	{
		return y;
	}

	public TestCoordinate(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
}

