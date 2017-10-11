package student.gettysburg.engine.common;

import java.util.Collection;
import java.util.ArrayList;

import gettysburg.common.ArmyID;
import gettysburg.common.BattleResolution;
import gettysburg.common.BattleResult;
import gettysburg.common.GbgUnit;

public class BattleResolutionImpl implements BattleResolution{
	
	private ArrayList<GbgUnit> activeConfederateUnits;
	private ArrayList<GbgUnit> activeUnionUnits;
	private ArrayList<GbgUnit> eliminatedConfederateUnits;
	private ArrayList<GbgUnit> eliminatedUnionUnits;
	private ArmyID attackerArmy;
	private ArmyID eliminatedArmy;
	private BattleResult result;
	
	public BattleResolutionImpl() {
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
		// Set the army if not already done so
		if(this.attackerArmy == null) {
			this.attackerArmy = attackers.iterator().next().getArmy();
		}
		// Add attackers to respective list
		while(attackers.iterator().hasNext()) {
			if(this.attackerArmy == ArmyID.UNION) {
				this.activeUnionUnits.add(attackers.iterator().next());
			} else {
				this.activeConfederateUnits.add(attackers.iterator().next());
			}
		}
	}
	public void addElimintedUnits(Collection<GbgUnit> eliminated) {
		// Set the army if not already done so
		if(this.eliminatedArmy == null) {
			this.eliminatedArmy = eliminated.iterator().next().getArmy();
		}
		// Add attackers to respective list
		while(eliminated.iterator().hasNext()) {
			if(this.eliminatedArmy == ArmyID.UNION) {
				this.eliminatedUnionUnits.add(eliminated.iterator().next());
			} else {
				this.eliminatedConfederateUnits.add(eliminated.iterator().next());
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

}
