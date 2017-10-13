package student.gettysburg.engine.common;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

import gettysburg.common.ArmyID;
import gettysburg.common.BattleResolution;
import gettysburg.common.BattleResult;
import gettysburg.common.GbgUnit;

public class BattleResolutionImpl implements BattleResolution{
	
	// Units of concern
	private ArrayList<GbgUnit> activeConfederateUnits;
	private ArrayList<GbgUnit> activeUnionUnits;
	private ArrayList<GbgUnit> eliminatedConfederateUnits;
	private ArrayList<GbgUnit> eliminatedUnionUnits;
	// Army IDs
	private ArmyID attackerArmy;
	private ArmyID eliminatedArmy;
	// Battle result
	private BattleResult result;
	
	
	public BattleResolutionImpl() {
		// Init lists
		this.activeConfederateUnits = new ArrayList<GbgUnit>();
		this.activeUnionUnits = new ArrayList<GbgUnit>();
		this.eliminatedConfederateUnits = new ArrayList<GbgUnit>();
		this.eliminatedUnionUnits = new ArrayList<GbgUnit>();
		
	}
	
	/**
	 * Setter for the battle result
	 * @param result
	 */
	public void setResult(BattleResult result) {
		this.result = result;
	}
	
	/**
	 * Adds
	 * @param attackers
	 */
	public void addActiveUnits(Collection<GbgUnit> attackers) {
		// Set the army 
		this.attackerArmy = attackers.iterator().next().getArmy();
		// Add attackers to respective list
		Iterator<GbgUnit> atkrs = attackers.iterator();
		while(atkrs.hasNext()) {
			if(this.attackerArmy == ArmyID.UNION) {
				this.activeUnionUnits.add(atkrs.next());
			} else {
				this.activeConfederateUnits.add(atkrs.next());
			}
		}
	}
	public void addElimintedUnits(Collection<GbgUnit> eliminated) {
		// Set the army
		this.eliminatedArmy = eliminated.iterator().next().getArmy();
		// Add units to respective list
		Iterator<GbgUnit> elims = eliminated.iterator();
		while(elims.hasNext()) {
			if(this.eliminatedArmy == ArmyID.UNION) {
				this.eliminatedUnionUnits.add(elims.next());
			} else {
				this.eliminatedConfederateUnits.add(elims.next());
			}
		}
	}
	
	@Override
	public Collection<GbgUnit> getActiveConfederateUnits() {
		if(this.activeConfederateUnits.size() == 0) {
			return null;
		}
		return this.activeConfederateUnits;
	}

	@Override
	public Collection<GbgUnit> getActiveUnionUnits() {
		if(this.activeUnionUnits.size() == 0) {
			return null;
		}
		return this.activeUnionUnits;
	}

	@Override
	public BattleResult getBattleResult() {
		return this.result;
	}

	@Override
	public Collection<GbgUnit> getEliminatedConfederateUnits() {
		if(this.eliminatedConfederateUnits.size() == 0) {
			return null;
		}
		return this.eliminatedConfederateUnits;
	}

	@Override
	public Collection<GbgUnit> getEliminatedUnionUnits() {
		if(this.eliminatedUnionUnits.size() == 0) {
			return null;
		}
		return this.eliminatedUnionUnits;
	}

	// Overridden method for singular eliminated unit to be added
	public void addElimintedUnits(GbgUnit unit) {
		ArrayList<GbgUnit> result = new ArrayList<GbgUnit>();
		result.add(unit);
		addElimintedUnits(result);
	}

	/**
	 * Overridden function for singular unit to be added
	 * @param g
	 */
	public void addActiveUnits(GbgUnit g) {
		ArrayList<GbgUnit> unit = new ArrayList<GbgUnit>();
		unit.add(g);
		this.addActiveUnits(unit);
		
	}

}
