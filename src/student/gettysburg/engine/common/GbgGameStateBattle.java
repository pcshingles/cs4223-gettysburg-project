package student.gettysburg.engine.common;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
	private ArrayList<GbgUnit> unitsNeedToBattle;
	private List<BattleResult> results;
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
		unitsNeedToBattle = new ArrayList<GbgUnit>();
		this.army = step == GbgGameStep.UBATTLE ? ArmyID.UNION : ArmyID.CONFEDERATE;
		updateUnitsWhoNeedToBattle(getBattlesToResolve()); // sets the units need to battle list
	}

	@Override
	public GbgGameState endStep() {
		// Make sure there's not any battles left
		if(!this.getBattlesToResolve().isEmpty()) {
			throw new GbgInvalidActionException("Cannot end battle step when there are unresolved battles");
		}
		if(!this.unitsNeedToBattle.isEmpty()) {
			throw new GbgInvalidActionException("Cannot end battle when not all units have participated in battle");
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
	 * Our set of battle descriptors will have exactly one attacker and 1..* defenders.
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
		updateUnitsWhoNeedToBattle(battles);
		return battles;
	}
	
	/**
	 * Helper function that adds units that need to battle this turn to our local variable
	 * @param battles
	 */
	private void updateUnitsWhoNeedToBattle(Collection<BattleDescriptor> battles) {
		for(BattleDescriptor b : battles) {
			ArrayList<GbgUnit> attackers = new ArrayList<GbgUnit>(b.getAttackers());
			ArrayList<GbgUnit> defenders = new ArrayList<GbgUnit>(b.getDefenders());
			attackers.forEach((GbgUnit) -> {
				if(!this.unitsNeedToBattle.contains(GbgUnit)) {
					this.unitsNeedToBattle.add(GbgUnit);}});
			defenders.forEach((GbgUnit) -> {
				if(!this.unitsNeedToBattle.contains(GbgUnit)) {
					this.unitsNeedToBattle.add(GbgUnit);}});
			}
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
		
		// Get our battle result from the table, check if we were given any first
		if(this.results == null) {
			ResolutionTable resTable = new ResolutionTable();
			result = resTable.getRow(resTable.resolutionTable, combatRatio)[ResolutionTable.getRandomInt()];
		} else {
			result = results.remove(0);
		}
		
		// Switch the result to handle appropriately
		switch(result) {
		case ABACK:
			this.back(attackers, defenders, resolution);
			break;
		case AELIM:
			// Defenders remain, attackers eliminated
			resolution.addActiveUnits(defenders);
			resolution.addElimintedUnits(attackers);
			this.getBoard().removeAllFromBoard(attackers);
			break;
		case DBACK:
			this.back(defenders, attackers, resolution);
			break;
		case DELIM:
			// Attackers remain, defenders eliminated
			resolution.addActiveUnits(attackers);
			resolution.addElimintedUnits(defenders);
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
				while(defenderFactor > cumSum && iter.hasNext()) {
					GbgUnit dfndr = iter.next();
					cumSum += dfndr.getCombatFactor();
					resolution.addElimintedUnits(dfndr);
				}
			}
			break;
		default:
			break;
		}
		// update the units that still need to battle
		this.unitsNeedToBattle.removeAll(attackers);
		this.unitsNeedToBattle.removeAll(defenders);
		// Update the result
		resolution.setResult(result);
		return resolution;
	}
	
	/**
	 * Helper for processing a backing move
	 * @param attackers
	 * @param defenders
	 */
	private void back(Collection<GbgUnit> attackers, Collection<GbgUnit> defenders, BattleResolutionImpl resolution) {
		// Try to retreat the attackers
		ArrayList<GbgUnit> stuckUnits = this.getBoard().retreatUnits(attackers);
		// Case when retreating was successfull
		if(stuckUnits == null) {
			resolution.addActiveUnits(attackers);
			resolution.addActiveUnits(defenders);
		} else { // Case when couldn't escape : Loop through remaining units and remove from game
			// Add active units that aren't stuck
			resolution.addActiveUnits(defenders);
			for(GbgUnit g : attackers) {
				if(!stuckUnits.contains(g)) {
					resolution.addActiveUnits(g); 
				}
			}
			// Add eliminated units
			for(GbgUnit unit : stuckUnits) {
				resolution.addElimintedUnits(unit);
			}
			this.getBoard().removeAllFromBoard(stuckUnits);
		}
	}
	
	public void setResults(List<BattleResult> results) {
		this.results = results;
	}
	
}
