package gettysburg.engine.common;
import org.junit.*;
import gettysburg.common.*;
import gettysburg.common.exceptions.GbgInvalidMoveException;
import student.gettysburg.engine.common.CoordinateImpl;
import student.gettysburg.engine.common.GettysburgEngine;
import student.gettysburg.engine.utility.configure.UnitInitializer;

import static gettysburg.common.ArmyID.*;
import static gettysburg.common.Direction.*;
import static gettysburg.common.UnitSize.*;
import static gettysburg.common.UnitType.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class TestWorld {
	
	@Test
	public void initialSetupPieces(){
		GettysburgEngine engine = new GettysburgEngine();
		int turnNum = engine.getTurnNumber();
		GbgGameStep step = engine.getCurrentStep();
		System.out.println(step);
		assertTrue(turnNum == 1 && step == GbgGameStep.UMOVE);
	}
	
	@Test(expected = GbgInvalidMoveException.class)
	public void testMovementLengthNotExceeded(){
		TestGettysburgEngine engine = new TestGettysburgEngine();
		engine.moveUnit(engine.getUnits().get(0).unit, engine.getUnits().get(0).where, CoordinateImpl.makeCoordinate(11,20));
	}
	
	@Test
	public void testMovementLengthGood(){
		TestGettysburgEngine engine = new TestGettysburgEngine();
		engine.moveUnit(engine.getUnits().get(0).unit, engine.getUnits().get(0).where, CoordinateImpl.makeCoordinate(14,14));
		assertTrue(true);
	}
	
	@Test(expected = GbgInvalidMoveException.class)
	public void unitHasAlreadyBeenMoved(){
		TestGettysburgEngine engine = new TestGettysburgEngine();
		engine.moveUnit(engine.getUnits().get(0).unit, engine.getUnits().get(0).where, CoordinateImpl.makeCoordinate(14,14));
		engine.moveUnit(engine.getUnits().get(0).unit, engine.getUnits().get(0).where, CoordinateImpl.makeCoordinate(14,14));
	}
	
	@Test
	public void setFacingWorks(){
		TestGettysburgEngine engine = new TestGettysburgEngine();
		engine.setUnitFacing(engine.getUnits().get(0).unit, Direction.NORTHEAST);
		assertTrue(engine.getUnits().get(0).unit.getFacing() == Direction.NORTHEAST);
	}
}
