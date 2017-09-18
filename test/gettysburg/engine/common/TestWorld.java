package gettysburg.engine.common;
import org.junit.*;
import gettysburg.common.*;
import static gettysburg.common.ArmyID.*;
import static gettysburg.common.Direction.*;
import static gettysburg.common.UnitSize.*;
import static gettysburg.common.UnitType.*;
import static org.junit.Assert.*;

public class TestWorld {
	
	@Test
	public void initialSetupPieces(){
		TestGettysburgEngine engine = new TestGettysburgEngine();
		int turnNum = engine.getTurnNumber();
		GbgGameStep step = engine.getCurrentStep();
		System.out.println(step);
		assertTrue(turnNum == 1 && step == GbgGameStep.UMOVE);
	}
}
