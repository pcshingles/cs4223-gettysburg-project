package student.gettysburg.engine.common;

import static gettysburg.common.BattleResult.ABACK;
import static gettysburg.common.BattleResult.AELIM;
import static gettysburg.common.BattleResult.DBACK;
import static gettysburg.common.BattleResult.DELIM;
import static gettysburg.common.BattleResult.EXCHANGE;

import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Map.Entry;

import gettysburg.common.BattleResult;

public final class ResolutionTable {

	// Data structure for our lookup table
	protected TreeMap<Double, BattleResult[]> resolutionTable = new TreeMap<Double, BattleResult[]>();
	// Init the lookup table, set boundaries
	private final BattleResult[] row1 = {DELIM, DBACK, DELIM, DELIM, DELIM, DELIM};
	private final BattleResult[] row2 = {DELIM, DBACK, DELIM, DBACK, DELIM, DELIM};
	private final BattleResult[] row3 = {DELIM, EXCHANGE, DELIM, DBACK, DBACK, DELIM};
	private final BattleResult[] row4 = {DELIM, EXCHANGE, DBACK, DBACK, EXCHANGE, DELIM};
	private final BattleResult[] row5 = {DELIM, EXCHANGE, DBACK, ABACK, EXCHANGE, AELIM};
	private final BattleResult[] row6 = {DELIM, ABACK, DBACK, ABACK, EXCHANGE, AELIM};
	private final BattleResult[] row7 = {DELIM, EXCHANGE, DBACK, ABACK, AELIM, AELIM};
	private final BattleResult[] row8 = {DBACK, EXCHANGE, ABACK, ABACK, AELIM, AELIM};
	private final BattleResult[] row9 = {ABACK, ABACK, ABACK, ABACK, AELIM, AELIM};
	private final BattleResult[] row10 = {ABACK, AELIM, ABACK, ABACK, AELIM, AELIM};
	private final BattleResult[] row11 = {AELIM, AELIM, ABACK, AELIM, AELIM, AELIM};
	private final BattleResult[] row12 = {AELIM, AELIM, AELIM, AELIM, AELIM, AELIM};
	
	/**
	 * Constructor - Inits the ranges of the key values
	 */
	public ResolutionTable() {
		// Create the ranges
		resolutionTable.put(0.0, row12);
		resolutionTable.put(0.167, row11);
		resolutionTable.put(0.20, row10);
		resolutionTable.put(0.25, row9);
		resolutionTable.put(0.333, row8);
		resolutionTable.put(0.5, row7);
		resolutionTable.put(1.0, row6);
		resolutionTable.put(2.0, row5);
		resolutionTable.put(3.0, row4);
		resolutionTable.put(4.0, row3);
		resolutionTable.put(5.0, row2);
		resolutionTable.put(6.0, row1);
	}
	
	/**
	 * Dark Magic - helper for looking up the table, allows ability to use a range of values for a key
	 * @param map
	 * @param key
	 * @return
	 */
	protected <K, V> V getRow(TreeMap<K, V> map, K key) {
	    Entry<K, V> e = map.floorEntry(key);
	    if (e != null && e.getValue() == null) {
	        e = map.lowerEntry(key);
	    }
	    return e == null ? null : e.getValue();
	}
	
	/**
	 * 
	 * @return int Random integer [0,5]
	 */
	protected static int getRandomInt() {
		return ThreadLocalRandom.current().nextInt(0, 5 + 1);
	}
	
}
