/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016-2017 Gary F. Pollice
 *******************************************************************************/
package student.gettysburg.engine.common;

import java.util.ArrayList;

import gettysburg.common.*;
import gettysburg.common.exceptions.GbgInvalidCoordinateException;

/**
 * Implementation of the gettysburg.common.Coordinate interface. Additional methods
 * used in this implementation are added to this class. Clients should <em>ONLY</em>
 * use the public Coordinate interface. Additional methods
 * are only for engine internal use.
 * 
 * @version Jun 9, 2017
 */
public class CoordinateImpl implements Coordinate
{
	private final int x, y;
	
	/**
	 * Private constructor that is called by the factory method.
	 * @param x
	 * @param y
	 */
	private CoordinateImpl(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Needed for JSON processing.
	 */
	public CoordinateImpl()
	{
		x = y = 0;
	}
	
	/**
	 * Factory method for creating Coordinates.
	 * @param x
	 * @param y
	 * @return
	 */
	public static CoordinateImpl makeCoordinate(int x, int y)
	{
		if (x < 1 || x > GbgBoard.COLUMNS || y < 1 || y > GbgBoard.ROWS) {
			throw new GbgInvalidCoordinateException(
					"Coordinates for (" + x + ", " + y + ") are out of bounds.");
		}
		return new CoordinateImpl(x, y);
	}
	
	/**
	 * Copy constructor for CoordinateImpl
	 * @param cord
	 * @return
	 */
	public static CoordinateImpl makeCoordinate(Coordinate cord) {
		if(cord == null) {
			return null;
		}
		else return CoordinateImpl.makeCoordinate(cord.getX(), cord.getY());
	}
	
	/*
	 * @see gettysburg.common.Coordinate#directionTo(gettysburg.common.Coordinate)
	 */
//	@Override
//	public Direction directionTo(Coordinate coordinate)
//	{
//		if(this.x == coordinate.getX() && this.y > coordinate.getY()){
//			return Direction.NORTH;
//		}
//		if(this.x == coordinate.getX() && this.y < coordinate.getY()){
//			return Direction.SOUTH;
//		}
//		if(this.x > coordinate.getX() && this.y == coordinate.getY()){
//			return Direction.WEST;
//		}
//		if(this.x < coordinate.getX() && this.y == coordinate.getY()){
//			return Direction.EAST;
//		}
//		if(this.x > coordinate.getX() && this.y > coordinate.getY()){
//			return Direction.NORTHWEST;
//		}
//		if(this.x < coordinate.getX() && this.y > coordinate.getY()){
//			return Direction.NORTHEAST;
//		}
//		if(this.x > coordinate.getX() && this.y < coordinate.getY()){
//			return Direction.SOUTHWEST;
//		}
//		if(this.x < coordinate.getX() && this.y < coordinate.getY()){
//			return Direction.SOUTHEAST;
//		}
//		return Direction.NONE;
//	}

	/*
	 * @see gettysburg.common.Coordinate#distanceTo(gettysburg.common.Coordinate)
	 * return straight line distance
	 */
	@Override
	public int distanceTo(Coordinate coordinate)
	{
		// Max(dx,dy) returns shortest path
		return Math.max(Math.abs(this.x-coordinate.getX()),Math.abs(this.y-coordinate.getY()));
	}

	/*
	 * @see gettysburg.common.Coordinate#getX()
	 */
	@Override
	public int getX()
	{
		return this.x;
	}

	/*
	 * @see gettysburg.common.Coordinate#getY()
	 */
	@Override
	public int getY()
	{
		return this.y;
	}
	
	/**
	 * Return a list of valid adjacent coordinates
	 * @param c1
	 * @return
	 */
	public static ArrayList<Coordinate> getAdjacentCoordinates(Coordinate c1){
		ArrayList<Coordinate> result = new ArrayList<Coordinate>();
		int x = c1.getX();
		int y = c1.getY();
		addCordToList(x,y-1, result);
		addCordToList(x,y+1, result);
		addCordToList(x+1,y, result);
		addCordToList(x+1,y-1, result);
		addCordToList(x+1,y+1, result);
		addCordToList(x-1,y, result);
		addCordToList(x-1,y+1, result);
		addCordToList(x-1,y-1, result);
		return result;
	}
	
	/**
	 * Returns a list of coordinates that are controlled by a direction
	 * @param d
	 * @param c
	 * @return
	 */
	public static ArrayList<Coordinate> getFacingCoordinates(Coordinate c, Direction d){
		
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
			addCordToList(x,y-1, result);
			addCordToList(x+1,y-1, result);
			addCordToList(x+1,y, result);
			break;
		case NORTHWEST:
			addCordToList(x,y-1, result);
			addCordToList(x-1,y-1, result);
			addCordToList(x-1,y, result);
			break;
		case SOUTHEAST:
			addCordToList(x,y+1, result);
			addCordToList(x+1,y+1, result);
			addCordToList(x+1,y, result);
			break;
		case SOUTHWEST:
			addCordToList(x,y+1, result);
			addCordToList(x-1,y+1, result);
			addCordToList(x-1,y, result);
			break;
		default:
			return null;
		}
		return result;
	}
	
	/**
	 * helper function for adding a coordinate to a list
	 * @param x
	 * @param y
	 * @param list
	 */
	private static void addCordToList(int x, int y, ArrayList<Coordinate> list) {
		try {
			CoordinateImpl attempt = CoordinateImpl.makeCoordinate(x, y);
			list.add(attempt);
		} catch(Exception e) {}
	}
	
	// Change the equals and hashCode if you need to.
	/*
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	/*
	 * We do not compare a CoordinateImpl to any object that just implements
	 * the Coordinate interface.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CoordinateImpl)) {
			return false;
		}
		CoordinateImpl other = (CoordinateImpl) obj;
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return"(" + x + ", " + y + ")";
	}
}
