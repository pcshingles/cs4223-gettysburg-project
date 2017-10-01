package student.gettysburg.engine.common;
import java.util.ArrayList;
import gettysburg.common.Coordinate;
import gettysburg.common.Direction;
import gettysburg.common.GbgGameStatus;
import gettysburg.common.GbgGameStep;
import gettysburg.common.GbgUnit;
import gettysburg.common.ArmyID;
import gettysburg.common.exceptions.GbgInvalidMoveException;
import student.gettysburg.engine.utility.configure.BattleOrder;
import student.gettysburg.engine.utility.configure.UnitInitializer;

public class GbgGameStateMove extends GbgGameState {
	
	private ArrayList<GbgUnit> movedUnits;
	private ArrayList<GbgUnit> facedUnits;

	/**
	 * First-time constructor
	 * @param turnNum
	 * @param step
	 * @param units
	 */
	public GbgGameStateMove(int turnNum, GbgGameStep step, ArrayList<UnitInitializer> units) {
		super(turnNum, step, units);
		this.movedUnits = new ArrayList<GbgUnit>();
		this.facedUnits = new ArrayList<GbgUnit>();
	}
	
	/**
	 * Constructor for already initialized board
	 * @param turnNum
	 * @param step
	 * @param board
	 */
	public GbgGameStateMove(int turnNum, GbgGameStep step, Board board) {
		super(turnNum, step, board);
		this.movedUnits = new ArrayList<GbgUnit>();
		this.facedUnits = new ArrayList<GbgUnit>();
		// Add units from battle order
		if(this.getTurnNum() > 1) {
			this.addReinforcements();
		}
	}

	/**
	 * Method for calling battle order to add reinforcements to the board.
	 */
	private void addReinforcements() {
		
		ArmyID army = this.getStep() == GbgGameStep.CMOVE ? ArmyID.CONFEDERATE : ArmyID.UNION;
		
		// Keep track of distinct coords so we don't place on occupied game tiles
		ArrayList<Coordinate> distinctDropZones = new ArrayList<Coordinate>();
		// Get the troops to add to the board
		ArrayList<UnitInitializer> troops = new ArrayList<UnitInitializer>();
		
		// Get the troops and coordinates
		for(UnitInitializer u : BattleOrder.getBattleOrder(army, this.getTurnNum())) {
			if(!distinctDropZones.contains(u.where)) {
				distinctDropZones.add(u.where);
			}
			troops.add(u);	
		}
		
		// Remove coordinates from distinct list to ensure we don't add troops to that location
		for(int i = 0; i < distinctDropZones.size(); i ++) {
			if(!(this.getBoard().getUnitsAt(distinctDropZones.get(i)) == null)) {
				distinctDropZones.remove(distinctDropZones.get(i));
			}
		}
		
		// Add the troops to the empty locations
		for(UnitInitializer u : troops) {
			if(distinctDropZones.contains(u.where)) {
				this.getBoard().addToBoard(u.unit, u.where);
			}
		}
	}

	@Override
	public GbgGameState endStep() {
		
		// Ensure that there are no stacked units
		this.getBoard().resolveStackedUnits();
		
		// End the turn by changing the game state in the engine
		if(this.getStep() == GbgGameStep.CMOVE) {
			return new GbgGameStateBattle(this.getTurnNum(), GbgGameStep.CBATTLE, this.getBoard());
		} else if(this.getStep() == GbgGameStep.UMOVE) {
			return new GbgGameStateBattle(this.getTurnNum(), GbgGameStep.UBATTLE, this.getBoard());
		} else return null;
		
	}

	@Override
	public GbgGameStatus getStatus() {
		return GbgGameStatus.IN_PROGRESS;
	}

	@Override
	public void moveUnit(GbgUnit unit, Coordinate from, Coordinate to) {
		from = CoordinateImpl.makeCoordinate(from);
		to = CoordinateImpl.makeCoordinate(to);
		// Coordinate from is unit's coordinate
		if(!(this.getBoard().whereIsUnit(unit).equals(CoordinateImpl.makeCoordinate(from)))) {
			throw new GbgInvalidMoveException("Coordinate is empty");
		}
		
		// It's the correct Army unit
		if(!((unit.getArmy() == ArmyID.CONFEDERATE && this.getStep() == GbgGameStep.CMOVE) || (unit.getArmy() == ArmyID.UNION && this.getStep() == GbgGameStep.UMOVE))){
			throw new GbgInvalidMoveException("Wrong turn");
		}

		// This unit has not already moved
		if (this.movedUnits.contains(unit)) {
			throw new GbgInvalidMoveException("Unit has already moved");
		}

		// There is no unit in the square already
		if(this.getBoard().getUnitsAt(to) != null) {
			if(this.getBoard().getUnitsAt(to).contains(unit)) {
				throw new GbgInvalidMoveException("Tried moving into another unit");
			}
		}
		
		// We are not exceeding the movement factor
		if(unit.getMovementFactor() < from.distanceTo(to)){
			throw new GbgInvalidMoveException("Move factor is less than distance attempted to move");
		}
		
		// Move unit
		this.getBoard().moveUnit(unit, to);
		
		// Add unit to moved list
		this.movedUnits.add(unit);
		
	}

	@Override
	public void setUnitFacing(GbgUnit unit, Direction to) {
		
		// It's the correct Army unit
		if(!((unit.getArmy() == ArmyID.CONFEDERATE && this.getStep() == GbgGameStep.CMOVE) || (unit.getArmy() == ArmyID.UNION && this.getStep() == GbgGameStep.UMOVE))){
			throw new GbgInvalidMoveException("Wrong turn");
			}

		// Check to see if unit has already been faced
		for(GbgUnit u: this.facedUnits){
			if(u == unit){
				throw new GbgInvalidMoveException("Unit has already been faced this turn");
			}
		}
		
		// move unit
		unit.setFacing(to);
		
		// add to visited list
		this.facedUnits.add(unit);
		
	}

}
