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

public class Version2StudentTests {
	
	private GbgGame game;
	private TestGbgGame testGame;
	private GbgUnit gamble, devin, heth;

	@Before
	public void setup()
	{
		game = testGame = makeTestGame();
		gamble = game.getUnit("Gamble", UNION);
		devin = game.getUnit("Devin", UNION);
		heth = game.getUnit("Heth", CONFEDERATE);
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
	public void hethFacesEast()
	{
		assertEquals(EAST, heth.getFacing());
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
	
	// Tests requiring the test double
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
	    assertTrue(game.getUnitsAt(makeCoordinate(22,22)) == null);
	}
	
	@Test
	public void hethDefeatsDevin()
	{
		testGame.setGameTurn(2);
		testGame.clearBoard();
		testGame.putUnitAt(heth, 5, 5, SOUTH);
		testGame.putUnitAt(devin, 5, 7, SOUTH);
		testGame.setGameStep(CMOVE);
		game.moveUnit(heth, makeCoordinate(5,5), makeCoordinate(5, 6));
		game.endStep();		// CBATTLE
		BattleDescriptor battle = game.getBattlesToResolve().iterator().next();	// Only 1
		assertEquals(BattleResult.DELIM, game.resolveBattle(battle));
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
	
}

