package student.gettysburg.engine.common;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import gettysburg.common.ArmyID;
import gettysburg.common.BattleDescriptor;
import gettysburg.common.BattleResolution;
import gettysburg.common.BattleResult;
import gettysburg.common.Coordinate;
import gettysburg.common.Direction;
import gettysburg.common.GbgGameStatus;
import gettysburg.common.GbgGameStep;
import gettysburg.common.GbgUnit;
import gettysburg.common.exceptions.GbgInvalidActionException;
import gettysburg.common.exceptions.GbgInvalidMoveException;

public class GbgGameStateBattle extends GbgGameState {
	
	private ArrayList<GbgUnit> battledUnits;
	private ArmyID army;
	
	/**
	 * Constructor, should always get a board
	 * @param turnNum
	 * @param step
	 * @param board
	 */
	public GbgGameStateBattle(int turnNum, GbgGameStep step, Board board) {
		super(turnNum, step, board);
		battledUnits = new ArrayList<GbgUnit>();
		this.army = step == GbgGameStep.UBATTLE ? ArmyID.UNION : ArmyID.CONFEDERATE;
	}

	@Override
	public GbgGameState endStep() {
		// Make sure there's not any battles left
		if(!this.getBattlesToResolve().isEmpty()) {
			throw new GbgInvalidActionException("Cannot end battle step when there are unresolved battles");
		}
		if(this.getStep() == GbgGameStep.CBATTLE) {
			return new GbgGameStateMove(this.getTurnNum() + 1, GbgGameStep.UMOVE, this.getBoard());
		} else if(this.getStep() == GbgGameStep.UBATTLE) {
			return new GbgGameStateMove(this.getTurnNum(), GbgGameStep.CMOVE, this.getBoard());
		} else return null;
	}

	@Override
	public GbgGameStatus getStatus() {
		return GbgGameStatus.IN_PROGRESS;
	}

	@Override
	public void moveUnit(GbgUnit unit, Coordinate from, Coordinate where) {
		throw new GbgInvalidMoveException("On battle step, cannot move.");
	}

	@Override
	public void setUnitFacing(GbgUnit unit, Direction to) {
		throw new GbgInvalidMoveException("On battle step, cannot turn unit.");
	}

	/**
	 * getter for the battles to resolve
	 * @return
	 */
	public Collection<BattleDescriptor> getBattlesToResolve() {
		
		// Result list
		ArrayList<BattleDescriptor> battles = new ArrayList<BattleDescriptor>();
		
		// For each friendly piece, if their zone of control contains enemies, add them to battle descriptor.
		for(Coordinate c : this.getBoard().getCoordinates()) {
			if(this.getBoard().getUnitsAt(c) != null) {
				GbgUnit unit = this.getBoard().getUnitsAt(c).iterator().next();
				if(unit.getArmy() == this.army) {
					// Loop through the ZOC, get the enemies
					ArrayList<GbgUnit> enemies = new ArrayList<GbgUnit>();
					for(Coordinate zoc : CoordinateImpl.getFacingCoordinates(c, unit.getFacing())) {
						if(!(this.getBoard().getUnitsAt(zoc) == null)) {
							GbgUnit potentialEnemyUnit = this.getBoard().getUnitsAt(zoc).iterator().next();
							if(potentialEnemyUnit.getArmy() != this.army) {
								enemies.add(potentialEnemyUnit);
							}
						}
					}
					// If we have enemies, add a battle descriptor.
					if(enemies.size() > 0) {
						BattleDescriptorImpl bd = new BattleDescriptorImpl();
						bd.addAttacker(unit);
						for(GbgUnit enemy : enemies) {
							bd.addDefender(enemy);
						}
						battles.add(bd);
					}
				}
			}
		}
		return battles;
	}
	
	/**
	 * Battle resolution method resolves a battle and returns a descriptor of the results
	 * @param battle 	 * @return
	 */
	public BattleResolution resolveBattle(BattleDescriptor battle) {
		
		// Our attackers and defenders from the battle, and all possible attackers and defenders
		Collection<GbgUnit> attackers = battle.getAttackers();
		Collection<GbgUnit> defenders = battle.getDefenders();
		
		
		// Determine if battle is valid
		// All defenders must be in the zone of control of all attackers
		ArrayList<Coordinate> enemyZOC = this.getBoard().getEnemyZOC(battle.getAttackers());
		for(GbgUnit d : defenders) {
			if(!enemyZOC.contains(CoordinateImpl.makeCoordinate(this.getBoard().whereIsUnit(d)))) {
				throw new GbgInvalidActionException("Battle is invalid. All defenders must be in the zone of control of all attackers.");
			}
		}
		
		// Loop through the units of given battle to determine if already fought
		
		// Attackers
		for(GbgUnit a : attackers) {
			if(this.battledUnits.contains(a)) {
				throw new GbgInvalidActionException("Unit has already been in a battle");
			}
		}
		// Defenders
		for(GbgUnit d : defenders) {
			if(this.battledUnits.contains(d)) {
				throw new GbgInvalidActionException("Unit has already been in a battle");
			}
		}
		
		// Add attackers and defenders to list of battled units
		this.battledUnits.addAll(attackers);
		this.battledUnits.addAll(defenders);
		
		return calculateResult(attackers, defenders);
	}

	private BattleResolution calculateResult(Collection<GbgUnit> attackers, Collection<GbgUnit> defenders) {
		
		double attackerFactor = 0;
		double defenderFactor = 0;
		double combatRatio = 0;
		BattleResult result;
		BattleResolutionImpl resolution = new BattleResolutionImpl();
		
		// Calculate attacker, defender, and ratio factors
		for(GbgUnit a : attackers) {
			attackerFactor += a.getCombatFactor();
		}
		for(GbgUnit d : defenders) {
			defenderFactor += d.getCombatFactor();
		}
		combatRatio = attackerFactor/defenderFactor;
		
		// Get our battle result from the table
		ResolutionTable resTable = new ResolutionTable();
		result = resTable.getRow(resTable.resolutionTable, combatRatio)[ResolutionTable.getRandomInt()];
		
		// Switch the result to handle appropriately
		switch(result) {
		case ABACK:
			break;
		case AELIM:
			resolution.addActiveUnits(defenders);
			resolution.addElimintedUnits(attackers);
			resolution.setResult(result);
			this.getBoard().removeAllFromBoard(attackers);
			break;
		case DBACK:
			break;
		case DELIM:
			resolution.addActiveUnits(attackers);
			resolution.addElimintedUnits(defenders);
			resolution.setResult(result);
			this.getBoard().removeAllFromBoard(defenders);
			break;
		case EXCHANGE:
			// Case for attackers win
			if(combatRatio > 1) {
				resolution.addElimintedUnits(defenders);
				// Int for cumulative sum removed so far
				int cumSum = 0;
				// Keep adding attackers until we have removed enough
				Iterator<GbgUnit> iter = attackers.iterator();
				while(defenderFactor > cumSum) {
					GbgUnit atkr = iter.next();
					cumSum += atkr.getCombatFactor();
					resolution.addElimintedUnits(atkr);
				}
			}
			// Case for defenders win
			else if(combatRatio < 1) {
				resolution.addElimintedUnits(attackers);
				// Int for cumulative sum removed so far
				int cumSum = 0;
				// Keep adding attackers until we have removed enough
				Iterator<GbgUnit> iter = defenders.iterator();
				while(defenderFactor > cumSum) {
					GbgUnit dfndr = iter.next();
					cumSum += dfndr.getCombatFactor();
					resolution.addElimintedUnits(dfndr);
				}
			}
			break;
		default:
			break;
		}
		return resolution;
	}
	
}
