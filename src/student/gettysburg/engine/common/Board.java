package student.gettysburg.engine.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import gettysburg.common.ArmyID;
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
		// Loop through all the keys-value pairs and return the correct key
		for(Coordinate c: this.board.keySet()) {
			if(this.board.get(c).contains(unit)) {
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
		// Put into new location
		putUnit(unit, to);
	}
	
	/**
	 * Puts unit on the board
	 * @param unit
	 * @param to
	 */
	public void putUnit(GbgUnit unit, Coordinate to) {
		if(this.board.containsKey(to)) {
			this.board.get(to).add(unit);
		} else {
			ArrayList<GbgUnit> newUnit = new ArrayList<GbgUnit>();
			newUnit.add(unit);
			this.board.put(to, newUnit);
		}
	}
	
	/**
	 * Clear board, inits a new hashmap.
	 */
	public void clearBoard() {
		this.board = new HashMap<Coordinate, ArrayList<GbgUnit>>();
	}

	/**
	 * Getter for the unit location by the weak relationship of leader, army
	 * @param leader
	 * @param army
	 * @return Coordinate location of unit
	 */
	public Coordinate whereIsUnit(String leader, ArmyID army) {
		for(GbgUnit u : this.getAllUnits()) {
			if(u.getLeader() == leader && u.getArmy() == army) {
				return this.whereIsUnit(u);
			}
		}
		return null;
	}
	
	/**
	 * Getter for a unit by the weak relationship of leader, army
	 * @param leader
	 * @param id
	 * @return
	 */
	public GbgUnit getUnit(String leader, ArmyID id) {
		for(GbgUnit u : this.getAllUnits()) {
			if(u.getLeader() == leader && u.getArmy() == id) {
				return u;
			}
		}
		return null;
	}

	/**
	 * Method for resolving any stacked units from reinforcements.
	 */
	public void resolveStackedUnits() {
		// Keep list of coordinates that have multiple units on them
		ArrayList<Coordinate> locationsToPrune = new ArrayList<Coordinate>();
		// Add locations that have stacked units to list of locations
		this.board.forEach( (key, value) -> {
			if(value.size() > 1) {
				locationsToPrune.add(key);
			}
		});
		// For each stacked unit, delete from the hashmap.
		for(Coordinate c : locationsToPrune) {
			this.board.remove(c);
		}
		
	}
	
	/**
	 * Function that calculates shortest path. -1 if impossible.
	 * @param c1
	 * @param c2
	 * @return length of path, -1 if impossible
	 */
	public int shortestPath(Coordinate c1, Coordinate c2) {
		
		// Queue holding the paths
		Queue<Queue<Coordinate>> queue = new LinkedList<Queue<Coordinate>>();
		// Starting coordinate
		Queue<Coordinate> start = new LinkedList<Coordinate>();
		start.add(c1);
		queue.add(start);
		// Visited list
		ArrayList<Coordinate> visited = new ArrayList<Coordinate>();
		visited.add(c1);
		// List of successfull paths
		ArrayList<Queue<Coordinate>> successfullPaths = new ArrayList<Queue<Coordinate>>();
		
		/*
		 * Loop:
		 * if empty, return -1.
		 */
		while(!queue.isEmpty()) {
			// Current path in queue
			Queue<Coordinate> currentPath = queue.poll();
			// Current coordinate in path
			Coordinate cuurentCord = currentPath.poll();
			// For each adjacent cord, add new paths to queue if valid
		}
	}
	
	/**
	 * Gets all coordinates that are controlled by enemies
	 * @return
	 */
	public ArrayList<Coordinate> getEnemyZOC(){
		
	}
	
	/**
	 * Returns a list of coordinates that are controlled by a direction
	 * @param d
	 * @param c
	 * @return
	 */
	public ArrayList<Coordinate> getFacingCoordinates(Coordinate c, Direction d){
		
		int x = c.getX();
		int y = c.getY();
		
		ArrayList<Coordinate> result = new ArrayList<Coordinate>();
		
		switch(d) {
		case NORTH:
			addCordToList(x,y-1, result);
			addCordToList(x-1,y-1, result);
			addCordToList(x+1,y-1, result);
			break;
		case SOUTH:
			addCordToList(x,y+1, result);
			addCordToList(x-1,y+1, result);
			addCordToList(x+1,y+1, result);
			break;
		case WEST:
			addCordToList(x-1,y, result);
			addCordToList(x-1,y-1, result);
			addCordToList(x-1,y+1, result);
			break;
		case EAST:
			addCordToList(x+1,y, result);
			addCordToList(x+1,y+1, result);
			addCordToList(x+1,y-1, result);
			break;
		case NORTHEAST:
			addCordToList(x,y, result);
			addCordToList(x,y, result);
			addCordToList(x,y, result);
			break;
		case NORTHWEST:
			addCordToList(x,y, result);
			addCordToList(x,y, result);
			addCordToList(x,y, result);
			break;
		case SOUTHEAST:
			addCordToList(x,y, result);
			addCordToList(x,y, result);
			addCordToList(x,y, result);
			break;
		case SOUTHWEST:
			addCordToList(x,y, result);
			addCordToList(x,y, result);
			addCordToList(x,y, result);
			break;
		default:
			return null;
		}
	}
	
	/**
	 * helper function for adding a coordinate to a list
	 * @param x
	 * @param y
	 * @param list
	 */
	private void addCordToList(int x, int y, ArrayList<Coordinate> list) {
		try {
			CoordinateImpl attempt = CoordinateImpl.makeCoordinate(x, y);
			list.add(attempt);
		} catch(Exception e) {}
	}
}
