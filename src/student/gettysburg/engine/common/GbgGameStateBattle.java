package student.gettysburg.engine.common;
import java.util.ArrayList;
import java.util.Collection;

import gettysburg.common.ArmyID;
import gettysburg.common.BattleDescriptor;
import gettysburg.common.BattleResolution;
import gettysburg.common.Coordinate;
import gettysburg.common.Direction;
import gettysburg.common.GbgGameStatus;
import gettysburg.common.GbgGameStep;
import gettysburg.common.GbgUnit;
import gettysburg.common.exceptions.GbgInvalidActionException;
import gettysburg.common.exceptions.GbgInvalidMoveException;

public class GbgGameStateBattle extends GbgGameState {
	
	private ArrayList<GbgUnit> battledUnits;
	
	/**
	 * Constructor, should always get a board
	 * @param turnNum
	 * @param step
	 * @param board
	 */
	public GbgGameStateBattle(int turnNum, GbgGameStep step, Board board) {
		super(turnNum, step, board);
		battledUnits = new ArrayList<GbgUnit>();
	}

	@Override
	public GbgGameState endStep() {
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
		// Identify our army
		ArmyID friendly = this.getStep() == GbgGameStep.UMOVE ? ArmyID.UNION : ArmyID.CONFEDERATE;
		// Result list
		ArrayList<BattleDescriptor> battles = new ArrayList<BattleDescriptor>();
		
		// For each friendly piece, if their zone of control contains enemies, add them to battle descriptor.
		for(Coordinate c : this.getBoard().getCoordinates()) {
			GbgUnit unit = this.getBoard().getUnitsAt(c).iterator().next();
			if(unit.getArmy() == friendly) {
				// Loop through the ZOC, get the enemies
				ArrayList<GbgUnit> enemies = new ArrayList<GbgUnit>();
				for(Coordinate zoc : CoordinateImpl.getFacingCoordinates(c, unit.getFacing())) {
					if(!(this.getBoard().getUnitsAt(zoc) == null)) {
						GbgUnit potentialEnemyUnit = this.getBoard().getUnitsAt(zoc).iterator().next();
						if(potentialEnemyUnit.getArmy() != friendly) {
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
		return battles;
	}
	
	/**
	 * Battle resolution method resolves a battle and returns a descriptor of the results
	 * @param battle
	 * @return
	 */
	public BattleResolution resolveBattle(BattleDescriptor battle) {
		
		BattleResolution resolution;
		Collection<GbgUnit> attackers = battle.getAttackers();
		Collection<GbgUnit> defenders = battle.getDefenders();
		
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
		
		resolution = calculateResult(attackers, defenders);
		
		// Add attackers and defenders to list of battled units
		this.battledUnits.addAll(attackers);
		this.battledUnits.addAll(defenders);
		return new BattleResolutionImpl();
	}

	private BattleResolution calculateResult(Collection<GbgUnit> attackers, Collection<GbgUnit> defenders) {
		
		int attackerFactor = 0;
		int defenderFactor = 0;
		int combatRatio = 0;
		
		// Calculate attacker and defender scores
		for(GbgUnit a : attackers) {
			attackerFactor += a.getCombatFactor();
		}
		for(GbgUnit d : defenders) {
			defenderFactor += d.getCombatFactor();
		}
		combatRatio = attackerFactor/defenderFactor;
	}

}
