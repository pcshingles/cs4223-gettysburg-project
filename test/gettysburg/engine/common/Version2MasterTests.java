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
import static gettysburg.common.BattleResult.*;
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
	}
	@Test
	public void factoryMakesGame()
	{
		assertNotNull(game);
	}

	@Test
	public void correctSquareForDevinUsingWhereIsUnit()
	{
		assertEquals(makeCoordinate(13, 9), game.whereIsUnit(devin));
	}

	@Test
	public void correctSquareForDevinUsingGetUnitsAt()
	{
		GbgUnit unit = game.getUnitsAt(makeCoordinate(13, 9)).iterator().next();
		assertNotNull(unit);
		assertEquals("Devin", unit.getLeader());
	}

	@Test
	public void correctSquareForHethUsingWhereIsUnit()
	{
		assertEquals(makeCoordinate(8, 8), game.whereIsUnit("Heth", CONFEDERATE));
	}

	@Test
	public void correctSquareForHethUsingGetUnitsAt()
	{
		GbgUnit unit = game.getUnitsAt(makeCoordinate(8, 8)).iterator().next();
		assertNotNull(unit);
		assertEquals("Heth", unit.getLeader());
	}

	@Test
	public void hethMovesWest()
	{
		game.endStep();
		game.endStep();
		game.moveUnit(heth,  makeCoordinate(8, 8), makeCoordinate(9, 8));
		assertEquals(heth, game.getUnitsAt(makeCoordinate(9, 8)).iterator().next());
	}

	@Test
	public void devinMovesNorthEast()
	{
		game.moveUnit(devin,  makeCoordinate(13, 9), makeCoordinate(16, 6));
		assertEquals(makeCoordinate(16, 6), game.whereIsUnit(devin));
	}

	@Test
	public void devinMovesSouthWest()
	{
		game.moveUnit(devin,  makeCoordinate(13, 9), makeCoordinate(9, 13));
		assertEquals(makeCoordinate(9, 13), game.whereIsUnit(devin));
	}

	@Test
	public void gambleMovesSouthEast()
	{
		game.moveUnit(gamble, makeCoordinate(11, 11), makeCoordinate(13, 12));
		assertEquals(makeCoordinate(13, 12), game.whereIsUnit(gamble));
	}

	@Test
	public void gambleMovesNorthWest()
	{
		game.moveUnit(gamble, makeCoordinate(11, 11), makeCoordinate(12, 10));
		assertEquals(makeCoordinate(12, 10), game.whereIsUnit(gamble));
	}
	
	@Test
	public void hethMovesAnL()
	{
		game.endStep();
		game.endStep();
		game.moveUnit(heth,  makeCoordinate(8, 8), makeCoordinate(9, 6));
		assertEquals(heth, game.getUnitsAt(makeCoordinate(9, 6)).iterator().next());
	}

	@Test(expected=GbgInvalidMoveException.class)
	public void attemptToMoveTooFar()
	{
		game.moveUnit(devin, makeCoordinate(13, 9), makeCoordinate(18, 9));
	}

	@Test(expected=GbgInvalidMoveException.class)
	public void attemptToMoveOntoAnotherUnit()
	{
		game.moveUnit(gamble, makeCoordinate(11, 11), makeCoordinate(13, 9));
	}

	@Test(expected=GbgInvalidMoveException.class)
	public void attemptToMoveFromEmptySquare()
	{
		game.moveUnit(gamble,  makeCoordinate(10, 10), makeCoordinate(10, 9));
	}

	@Test(expected=GbgInvalidMoveException.class)
	public void attemptToMoveWrongArmyUnit()
	{
		game.moveUnit(heth, makeCoordinate(8, 8), makeCoordinate(9, 6));
	}

	@Test(expected=GbgInvalidMoveException.class)
	public void attemptToMoveUnitTwiceInOneTurn()
	{
		game.moveUnit(gamble, makeCoordinate(11, 11), makeCoordinate(12, 10));
		game.moveUnit(gamble, makeCoordinate(12, 10), makeCoordinate(12, 11));
	}
	
	// Facing tests
	@Test
	public void gambleFacesNorth()
	{
		game.setUnitFacing(gamble, NORTH);
		assertEquals(NORTH, game.getUnitFacing(gamble));
	}

	@Test
	public void devinFacesSoutheastAfterMoving()
	{
		game.moveUnit(devin, makeCoordinate(13, 9), makeCoordinate(13, 10));
		game.setUnitFacing(devin, SOUTHEAST);
		assertEquals(SOUTHEAST, game.getUnitFacing(devin));
	}

	@Test(expected=GbgInvalidMoveException.class)
	public void hethAttemptsFacingChangeAtWrongTime()
	{
		game.setUnitFacing(heth, WEST);
	}

	@Test(expected=GbgInvalidMoveException.class)
	public void devinTriesToSetFacingTwice()
	{
		game.setUnitFacing(devin, NORTHWEST);
		game.moveUnit(devin, makeCoordinate(13, 9), makeCoordinate(13, 10));
		game.setUnitFacing(devin, SOUTHEAST);
	}
	
	// Other tests
	@Test(expected=Exception.class)
	public void queryInvalidSquare()
	{
		game.getUnitsAt(makeCoordinate(30, 30));
	}

	@Test(expected=GbgInvalidMoveException.class)
	public void moveToStartingSquare()
	{
		game.moveUnit(gamble, makeCoordinate(11, 11), makeCoordinate(11, 11));
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
		game.moveUnit(game.getUnitsAt(makeCoordinate(7, 28)).iterator().next(), makeCoordinate(7, 28), makeCoordinate(5, 28));
		game.moveUnit(game.getUnitsAt(makeCoordinate(7, 28)).iterator().next(), makeCoordinate(7, 28), makeCoordinate(6, 28));
		game.moveUnit(game.getUnitsAt(makeCoordinate(7, 28)).iterator().next(), makeCoordinate(7, 28), makeCoordinate(8, 28));
		game.moveUnit(game.getUnitsAt(makeCoordinate(7, 28)).iterator().next(), makeCoordinate(7, 28), makeCoordinate(9, 28));
		Collection<GbgUnit> remaining = game.getUnitsAt(makeCoordinate(7, 28));
		assertTrue(remaining == null || remaining.isEmpty());
	}
	
	@Test
	public void someEntryUnitsRemainAndAreRemoved()
	{
		game.moveUnit(game.getUnitsAt(makeCoordinate(7, 28)).iterator().next(), makeCoordinate(7, 28), makeCoordinate(5, 28));
		game.moveUnit(game.getUnitsAt(makeCoordinate(7, 28)).iterator().next(), makeCoordinate(7, 28), makeCoordinate(6, 28));
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
	
// My tests start here

	@Test(expected = GbgInvalidMoveException.class)
	public void cannotMoveThroughEnemyZones() {
		testGame.setGameTurn(1);
		testGame.clearBoard();
		testGame.putUnitAt(gamble, 1, 3, NORTH);
		testGame.putUnitAt(devin, 3, 2, WEST);
		testGame.putUnitAt(heth, 1, 1, EAST);
		testGame.setGameStep(CMOVE);
		game.moveUnit(heth, makeCoordinate(1,1), makeCoordinate(3, 1));
	}
	
	@Test
	public void canMoveAroundEnemy() {
		testGame.setGameTurn(1);
		testGame.clearBoard();
		testGame.putUnitAt(gamble, 2, 3, NORTHWEST);
		testGame.putUnitAt(devin, 4, 2, NORTH);
		testGame.putUnitAt(heth, 1, 1, EAST);
		testGame.setGameStep(CMOVE);
		game.moveUnit(heth, makeCoordinate(1,1), makeCoordinate(3, 2));
		assertTrue(game.whereIsUnit(heth).equals(makeCoordinate(3,2)));
	}
	
	@Test
	public void canMoveIntoEnemyZone() {
		testGame.setGameTurn(1);
		testGame.clearBoard();
		testGame.putUnitAt(gamble, 1, 3, NORTH);
		testGame.putUnitAt(devin, 3, 2, WEST);
		testGame.putUnitAt(heth, 1, 1, EAST);
		testGame.setGameStep(CMOVE);
		game.moveUnit(heth, makeCoordinate(1,1), makeCoordinate(2, 1));
		assertTrue(game.whereIsUnit(heth).equals(makeCoordinate(2,1)));
	}
	
	@Test(expected = GbgInvalidActionException.class)
	public void cantEndBattleStepWithUnresolvedBattles() {
		testGame.setGameTurn(2);
		testGame.clearBoard();
		testGame.putUnitAt(heth, 5, 5, SOUTH);
		testGame.putUnitAt(devin, 5, 7, SOUTH);
		testGame.setGameStep(CMOVE);
		game.moveUnit(heth, makeCoordinate(5,5), makeCoordinate(5, 6));
		game.endStep();		// CBATTLE
		game.endStep();		// Throws exception
	}
	
	@Test(expected = GbgInvalidActionException.class)
	public void cantResolveBattleThatHasUnitsThatAlreadyFought() {
		testGame.setGameTurn(2);
		testGame.clearBoard();
		testGame.putUnitAt(heth, 5, 5, SOUTH);
		testGame.putUnitAt(devin, 5, 7, SOUTH);
		testGame.setGameStep(CMOVE);
		game.moveUnit(heth, makeCoordinate(5,5), makeCoordinate(5, 6));
		game.endStep();		// CBATTLE
		BattleDescriptor battle = game.getBattlesToResolve().iterator().next();	// Only 1
		game.resolveBattle(battle);
		game.resolveBattle(battle); 	// Throws exception
	}
	
	@Test
	public void canMoveUnitsZOCIntoEnemyForBattle() {
		testGame.setGameTurn(2);
		testGame.clearBoard();
		testGame.putUnitAt(heth, 3, 2, SOUTH);
		testGame.putUnitAt(devin, 1, 1, SOUTH);
		testGame.setGameStep(UMOVE);
		testGame.moveUnit(devin, makeCoordinate(1,1), makeCoordinate(2, 1));
		assertEquals(game.whereIsUnit(devin), makeCoordinate(2,1));
		testGame.endStep();
		assertTrue(testGame.getBattlesToResolve().iterator().next().getAttackers().iterator().next().equals(devin));
		
	}
	
	@Test(expected = GbgInvalidMoveException.class)
	public void cantMoveUnitsZOCThroughEnemyForBattle() {
		testGame.setGameTurn(2);
		testGame.clearBoard();
		testGame.putUnitAt(heth, 3, 2, SOUTH);
		testGame.putUnitAt(hampton, 2, 3, NORTH);
		testGame.putUnitAt(devin, 1, 1, SOUTH);
		testGame.setGameStep(UMOVE);
		game.moveUnit(devin, makeCoordinate(1,1), makeCoordinate(3, 1));
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
		List<BattleResult> br = new ArrayList<BattleResult>();
		br.add(AELIM);
		testGame.setBattleResults(br);
		assertEquals(BattleResult.AELIM, game.resolveBattle(battle).getBattleResult());
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
		List<BattleResult> br = new ArrayList<BattleResult>();
		br.add(EXCHANGE);
		br.add(EXCHANGE);
		testGame.setBattleResults(br);
		assertEquals(BattleResult.EXCHANGE, game.resolveBattle(bd).getBattleResult());
		assertEquals(BattleResult.EXCHANGE, game.resolveBattle(bd1).getBattleResult());
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
		List<BattleResult> br = new ArrayList<BattleResult>();
		br.add(DELIM);
		testGame.setBattleResults(br);
		assertEquals(BattleResult.DELIM, game.resolveBattle(battle).getBattleResult());
	}
	
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
		BattleDescriptor battle = game.getBattlesToResolve().iterator().next();	
		List<BattleResult> br = new ArrayList<BattleResult>();
		br.add(DELIM);
		testGame.setBattleResults(br);
		assertEquals(BattleResult.DELIM, game.resolveBattle(battle).getBattleResult());
	}
	
	@Test
	public void noEscapeDuringRetreat() {
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 1, 1, SOUTH);
		testGame.putUnitAt(devin, 1, 2, NORTH);
		testGame.putUnitAt(gamble, 2, 2, NORTH);
		testGame.setGameStep(CMOVE);
		testGame.endStep();
		BattleDescriptor battle = game.getBattlesToResolve().iterator().next();	
		List<BattleResult> br = new ArrayList<BattleResult>();
		br.add(ABACK);
		testGame.setBattleResults(br);
		assertEquals(game.resolveBattle(battle).getEliminatedConfederateUnits().iterator().next(), heth);
	}
	
	@Test
	public void exchangeWithAttackersAtAdvantage() {
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 1, 1, SOUTH);
		testGame.putUnitAt(devin, 1, 2, NORTH);
		testGame.putUnitAt(gamble, 2, 2, NORTH);
		testGame.setGameStep(CMOVE);
		testGame.endStep();
		List<BattleResult> br = new ArrayList<BattleResult>();
		br.add(EXCHANGE);
		testGame.setBattleResults(br);
		TestBattleDescriptor bd1 = new TestBattleDescriptor();
		bd1.addAttacker(heth);
		bd1.addDefender(devin);
		bd1.addDefender(gamble);
		BattleResolution brf = game.resolveBattle(bd1);
		assertEquals(brf.getEliminatedConfederateUnits().iterator().next(), heth);
		assertEquals(brf.getActiveConfederateUnits(), null);
		assertEquals(brf.getActiveUnionUnits(), null);
		assertTrue(brf.getEliminatedUnionUnits().contains(devin) && brf.getEliminatedUnionUnits().contains(gamble));
		
	} 
	
	@Test
	public void successfulRetreat() {
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 1, 1, SOUTH);
		testGame.putUnitAt(devin, 1, 2, NORTH);
		testGame.setGameStep(CMOVE);
		testGame.endStep();
		BattleDescriptor battle = game.getBattlesToResolve().iterator().next();	
		List<BattleResult> br = new ArrayList<BattleResult>();
		br.add(ABACK);
		testGame.setBattleResults(br);
		assertEquals(game.resolveBattle(battle).getActiveConfederateUnits().iterator().next(), heth); 
	}
	
	@Test
	public void onlyOneOutOfTwoEscapes() {
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(gamble, 2, 1, SOUTH);
		testGame.putUnitAt(devin, 1, 1, SOUTH);
		testGame.putUnitAt(heth, 1, 2, NORTH);
		testGame.putUnitAt(hampton, 2, 2, NORTH);
		testGame.setGameStep(UMOVE);
		testGame.endStep();
		List<BattleResult> br = new ArrayList<BattleResult>();
		br.add(ABACK);
		testGame.setBattleResults(br);
		TestBattleDescriptor bd1 = new TestBattleDescriptor();
		bd1.addAttacker(gamble);
		bd1.addAttacker(devin);
		bd1.addDefender(heth);
		bd1.addDefender(hampton);
		BattleResolution res = game.resolveBattle(bd1);
		assertEquals(res.getActiveUnionUnits().iterator().next(), gamble); // gamble still lives
		assertEquals(res.getEliminatedUnionUnits().iterator().next(), devin);
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

