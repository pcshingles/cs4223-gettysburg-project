/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Copyright Â©2016-2017 Gary F. Pollice
 *******************************************************************************/

package gettysburg.engine.common;
import static student.gettysburg.engine.GettysburgFactory.*;
import static gettysburg.common.GbgGameStep.*;
import static gettysburg.common.ArmyID.*;
import static gettysburg.common.Direction.*;
import static gettysburg.common.GbgGameStatus.*;
import static org.junit.Assert.*;
import org.junit.*;
import gettysburg.common.*;
import gettysburg.common.exceptions.*;

/**
 * JUnit tests to be run against student projects.
 * @version Sep 18, 2017
 */
public class Version1MasterTests
{
	private GbgGame game;
	private GbgUnit gamble, devin, heth;
	private static boolean getUnit = false, 
			endMoveStep = false,
			endBattleStep = false;

	@Before
	public void setup()
	{
		game = makeGame();
		try {
			gamble = game.getUnit("Gamble", UNION);
			devin = game.getUnit("Devin", UNION);
			heth = game.getUnit("Heth", CONFEDERATE);
		} catch (Exception e) {
			if (!getUnit) {
				getUnit = true;
			}
			gamble = game.getUnitsAt(makeCoordinate(11, 11)).iterator().next();
			devin = game.getUnitsAt(makeCoordinate(13, 9)).iterator().next();
			heth = game.getUnitsAt(makeCoordinate(8, 8)).iterator().next();
		}
		gamble.setFacing(WEST);
		devin.setFacing(SOUTH);
		heth.setFacing(EAST);
	}

	@Test
	public void factoryMakesGame()
	{
		assertNotNull(game);
	}

	// Initial setup tests
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
	public void gambleFacesWest()
	{
		assertEquals(WEST, game.getUnitFacing(gamble));
	}

	@Test
	public void devinFacesSouth()
	{
		assertEquals(SOUTH, game.getUnitFacing(devin));
	}

	@Test
	public void hethFacesEast()
	{
		assertEquals(EAST, heth.getFacing());
	}

	// Game step and turn tests
	@Test
	public void unionBattleFollowsUnionMove()
	{
		doEndMoveStep();
		assertEquals(UBATTLE, game.getCurrentStep());
	}

	@Test
	public void confederateMoveFollowsUnionBattle()
	{
		doEndMoveStep();
		doEndBattleStep();
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
	public void endOfGameUnionWins()
	{
		game.endStep();
		game.endStep();
		game.endStep();
		game.endStep();
		assertEquals(UNION_WINS, game.getGameStatus());
	}

	// Movement tests
	@Test
	public void gambleMovesNorth()
	{
		game.moveUnit(gamble, makeCoordinate(11, 11), makeCoordinate(11, 10));
		assertEquals(makeCoordinate(11, 10), game.whereIsUnit(gamble));
		assertNull(">> Documentation says this should be null, not an empty array", game.getUnitsAt(makeCoordinate(11, 11)));
	}

	@Test
	public void devinMovesSouth()
	{
		game.moveUnit(devin,  makeCoordinate(13, 9), makeCoordinate(13, 11));
		assertEquals(makeCoordinate(13, 11), game.whereIsUnit(devin));
	}

	@Test
	public void hethMovesEast()
	{
		game.endStep();
		game.endStep();
		game.moveUnit(heth,  makeCoordinate(8, 8), makeCoordinate(10, 8));
		assertEquals(heth, game.getUnitsAt(makeCoordinate(10, 8)).iterator().next());
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
	
	@Test
	public void gameOverAfterFirstFullTurn()
	{
		game.endStep();
		game.endStep();
		game.endStep();
		assertEquals(">> Do not take points off if this fails", GAME_OVER, game.endStep());
	}

	@Test(expected=Exception.class)
	public void endMoveStepWhenNotInUnionMove()
	{
		game.endStep();		// to UBATTLE
		doEndMoveStep();
	}

	@Test(expected=Exception.class)
	public void endBattleStepWhenNotInConfederateBattle()
	{
		game.endStep();
		game.endStep();		// to CMOVE
		doEndBattleStep();
	}

	@Test(expected=GbgInvalidMoveException.class)
	public void moveToStartingSquare()
	{
		game.moveUnit(gamble, makeCoordinate(11, 11), makeCoordinate(11, 11));
	}
	
	@Test
	public void gameOverTest()
	{
		game.endStep();
		game.endStep();
		game.endStep();
		game.endStep();
		assertEquals(GAME_OVER, game.getCurrentStep());
	}

	//////////////// Helper methods /////////////////////////
	private void doEndMoveStep()
	{
		try {
			game.endMoveStep();
		} catch (GbgNotImplementedException e) {
			game.endStep();
			if (!endMoveStep) {
				System.out.println(">>> Did not implement endMoveStep(), Take off points as if a test failed");
				endMoveStep = true;
			}
		}
	}

	private void doEndBattleStep()
	{
		try {
			game.endBattleStep();
		} catch (GbgNotImplementedException e) {
			game.endStep();
			if (!endBattleStep) {
				System.out.println(">>> Did not implement endBattleStep(), Take off points as if a test failed");
				endBattleStep = true;
			}
		}
	}
}