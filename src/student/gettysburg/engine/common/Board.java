package student.gettysburg.engine.common;

import java.util.ArrayList;
import java.util.HashMap;

import gettysburg.common.Coordinate;
import gettysburg.common.Direction;
import gettysburg.common.GbgUnit;
import student.gettysburg.engine.utility.configure.UnitInitializer;

public class Board {
	
	// Data structure is hashmap
	private HashMap<Coordinate, ArrayList<GbgUnit>> board;
	
	/**
	 * Constructor adds all units to board
	 */
	public Board(ArrayList<UnitInitializer> units) {
		// Init board
		board = new HashMap<Coordinate, ArrayList<GbgUnit>>();
		// For each unit, add to board
		for(UnitInitializer u : units) {
			this.addToBoard(u.unit, u.where);
		}
	}
	
	/**
	 * Get the units at a given coordinate
	 * @param where
	 * @return GbgUnit or null
	 */
	public ArrayList<GbgUnit> getUnitsAt(Coordinate where) {
		return this.board.get(CoordinateImpl.makeCoordinate(where));
	}
	
	/**
	 * Add a given unit to the board.
	 * @param unit
	 * @param where
	 */
	public void addToBoard(GbgUnit unit, Coordinate where) {
		if(this.board.containsKey(where)) {
			this.board.get(where).add(unit);
		} else {
			ArrayList<GbgUnit> newList = new ArrayList<GbgUnit>();
			newList.add(unit);
			this.board.put(where, newList);
		}
	}
	
	/**
	 * Getter for all units on the board.
	 * @return
	 */
	public ArrayList<GbgUnit> getAllUnits(){
		ArrayList<GbgUnit> result = new ArrayList<GbgUnit>();
		for(ArrayList<GbgUnit> unitsAt : this.board.values()) {
			result.addAll(unitsAt);
		}
		return result;
	}
	
	/**
	 * Getter for the facing of a given unit
	 * @param unit
	 * @return
	 */
	public Direction getUnitFacing(GbgUnit unit) {
		for(GbgUnit u: getAllUnits()) {
			if(u == unit) {
				return u.getFacing();
			}
		}
		return null;
	}
	
	/**
	 * Getter for the coordinate of a given unit
	 * @param unit
	 * @return
	 */
	public Coordinate whereIsUnit(GbgUnit unit) {
		ArrayList<GbgUnit> units = this.getAllUnits();
		for(Coordinate c: this.board.keySet()) {
			if(units.contains(unit)) {
				return c;
			}
		}
		return null;
	}
	
	/**
	 * Moves unit in board to given coordinate
	 * @param unit
	 * @param to
	 */
	public void moveUnit(GbgUnit unit, Coordinate to) {
		Coordinate location = this.whereIsUnit(unit);
		// Remove from list, delete Hashmap entry if now empty
		this.board.get(location).remove(unit);
		if(this.board.get(location).isEmpty()) {
			this.board.remove(location);
		}
	}
}
