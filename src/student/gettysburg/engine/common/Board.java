package student.gettysburg.engine.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import gettysburg.common.ArmyID;
import gettysburg.common.Coordinate;
import gettysburg.common.Direction;
import gettysburg.common.GbgUnit;
import gettysburg.common.UnitType;
import static gettysburg.common.ArmyID.*;
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
	 * Depth first search, adapted from Russel and Norvig
	 * Uses Queue of Queues of Coordinates, representing a queue of paths to consider
	 * @param c1
	 * @param c2
	 * @param enemy
	 * @return length of path, -1 if impossible
	 */
	public int shortestPath(Coordinate c1, Coordinate c2, GbgUnit unit) {
		
		ArmyID enemy = unit.getArmy() == ArmyID.CONFEDERATE ? ArmyID.UNION : ArmyID.CONFEDERATE;
		
		// Queue holding the paths
		Queue<Queue<Coordinate>> queue = new LinkedList<Queue<Coordinate>>();
		
		// Starting coordinate
		Queue<Coordinate> start = new LinkedList<Coordinate>();
		start.add(c1);
		queue.add(start);
		
		// Visited list
		ArrayList<Coordinate> visited = new ArrayList<Coordinate>();
		visited.add(c1);
		
		// Create obstacles = enemy ZOC
		ArrayList<Coordinate> obstacles = getEnemyZOC(enemy);
		obstacles.addAll(this.getEnemyPositions(enemy));
		
		// Remove destination from set of obstacles
		while(obstacles.remove(c2));
		
		// List of successfull paths
		ArrayList<Queue<Coordinate>> successfullPaths = new ArrayList<Queue<Coordinate>>();
		
		/*
		 * Loop: loop through all possible paths
		 */
		while(!queue.isEmpty()) {
			// Current path in queue
			Queue<Coordinate> currentPath = queue.poll();
			// Current coordinate in path
			Coordinate currentCord = currentPath.peek();
			// For each adjacent cord, add new paths to queue if valid
			for(Coordinate option : CoordinateImpl.getAdjacentCoordinates(currentCord)) {
				if(!visited.contains(option)) {
					if(!obstacles.contains(option)) {
						// Terminal test
						if(CoordinateImpl.makeCoordinate(option).equals(CoordinateImpl.makeCoordinate(c2))) {
							Queue<Coordinate> newPath = new LinkedList<Coordinate>();
							newPath.add(option);
							newPath.addAll(currentPath);
							successfullPaths.add(newPath);
						} else {
							// Check to see if current location places us in enemyZOC
							if(this.valdiatePosition(unit, option) || CoordinateImpl.makeCoordinate(option).equals(CoordinateImpl.makeCoordinate(c2))) {
								// Add a new path to the queue
								Queue<Coordinate> newPath = new LinkedList<Coordinate>();
								newPath.add(option);
								newPath.addAll(currentPath);
								queue.add(newPath);
							}
							// Add to visited
							visited.add(option);
						}
					}
				}
			}
		}
		
		// If there's no path, indicate failure
		if(successfullPaths.size() == 0) {
			return -1;
		}
		
		// Loop through the successfull paths and return the best one.
		int bestPath = successfullPaths.iterator().next().size();
		for(Queue<Coordinate> path : successfullPaths) {
			if(path.size() < bestPath) {
				bestPath = path.size();
			}
		}
		return bestPath-1;
	}
	
	/**
	 * Function indicated if a unit at given position would place any enemies in its ZOC
	 * @param unit
	 * @param option
	 * @return
	 */
	private boolean valdiatePosition(GbgUnit unit, Coordinate option) {
		ArrayList<Coordinate> zoc = CoordinateImpl.getFacingCoordinates(option, unit.getFacing());
		// See if the board has enemies on that position
		for(Coordinate c : zoc) {
			ArrayList<GbgUnit> enemies = this.board.get(c);
			if(!(enemies == null)) {
				if (!enemies.iterator().next().getArmy().equals(unit.getArmy())) {
					// Found enemy, position is invalid
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Gets all coordinates that are controlled by enemies
	 * @param enemy
	 * @return
	 */
	public ArrayList<Coordinate> getEnemyZOC(ArmyID enemy){
		
		ArrayList<Coordinate> result = new ArrayList<Coordinate>();
		// for each enemy unit on the board, add to list of coordinates
		for(Coordinate c : this.board.keySet()) {
			// for this enemy, add his ZOC to list if not HQ
			GbgUnit currentUnit = this.board.get(c).iterator().next();
			if(currentUnit.getArmy() == enemy && currentUnit.getType() != UnitType.HQ) {
				result.addAll(CoordinateImpl.getFacingCoordinates(c, currentUnit.getFacing()));
			}
		}
		return result;
	}
	
	/**
	 * Overloaded method Gets all coordinates that are controlled by given enemies
	 * @param enemy
	 * @return
	 */
	public ArrayList<Coordinate> getEnemyZOC(Collection<GbgUnit> enemies){
		
		ArrayList<Coordinate> result = new ArrayList<Coordinate>();
		ArrayList<Coordinate> enemyCoords = new ArrayList<Coordinate>();
		enemies.forEach((GbgUnit) -> enemyCoords.add(this.whereIsUnit(GbgUnit)));
		
		// for each enemy unit given, add to list of coordinates
		for(Coordinate c : enemyCoords) {
			// for this enemy, add his ZOC to list
			GbgUnit currentUnit = this.board.get(c).iterator().next();
			result.addAll(CoordinateImpl.getFacingCoordinates(c, currentUnit.getFacing()));
		}
		return result;
	}
	
	/**
	 * Get all enemy positions
	 * @param enemy
	 * @return
	 */
	public ArrayList<Coordinate> getEnemyPositions(ArmyID enemy){
		
		ArrayList<Coordinate> result = new ArrayList<Coordinate>();
		// add each enemy's location
		for(Coordinate c : this.board.keySet()) {
			GbgUnit enemyUnit = board.get(c).iterator().next();
			if(enemyUnit.getArmy() == enemy) {
				result.add(c);
			}
		}
		return result;
	}
	
	/**
	 * Removes all units from the board
	 * @param units
	 */
	public void removeAllFromBoard(Collection<GbgUnit> units) {
		Iterator<GbgUnit> iter = units.iterator();
		ArrayList<Coordinate> positions = new ArrayList<Coordinate>();
		while(iter.hasNext()) {
			positions.add(this.whereIsUnit(iter.next()));
		}
		for(Coordinate c : positions) {
			this.board.remove(c);
		}
	}
	
	/**
	 * getter for all of the coordinates
	 * @return
	 */
	public Set<Coordinate> getCoordinates(){
		return this.board.keySet();
	}

	/**
	 * Function that attempts to retreat all units given. If it doesn't work, returns the ones that got stuck.
	 * @param attackers
	 * @return
	 */
	public ArrayList<GbgUnit> retreatUnits(Collection<GbgUnit> units) {
		ArrayList<GbgUnit> stuckUnits = new ArrayList<GbgUnit>();
		stuckUnits.addAll(units);
		// For each unit, attempt to move
		for(GbgUnit u : units) {
			// Calculate obstacles
			ArrayList<Coordinate> obstacles = new ArrayList<Coordinate>();
			// add enemy positions
			obstacles.addAll(this.getEnemyPositions(units.iterator().next().getArmy() == UNION ? CONFEDERATE : UNION));
			// add friendly positions
			obstacles.addAll(this.getEnemyPositions(units.iterator().next().getArmy() == UNION ? UNION : CONFEDERATE));
			// add enemy ZOC
			obstacles.addAll(this.getEnemyZOC(units.iterator().next().getArmy() == UNION ? CONFEDERATE : UNION));
			// Get possible locations
			ArrayList<Coordinate> adj = CoordinateImpl.getAdjacentCoordinates(this.whereIsUnit(u));
			// For each location, tryo to move the unit
			for(Coordinate c : adj) {
				// Check to see if it's valid, move if so
				if(!obstacles.contains(c)) {
					this.moveUnit(u, c);
					stuckUnits.remove(u);
				}
			}
		}
		return stuckUnits.isEmpty() ? null : stuckUnits;
	}
	
}
