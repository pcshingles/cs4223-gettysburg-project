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
import gettysburg.common.*;
import gettysburg.common.exceptions.GbgInvalidMoveException;

import static gettysburg.common.ArmyID.*;
import static gettysburg.common.Direction.*;
import static gettysburg.common.UnitSize.*;
import static gettysburg.common.UnitType.*;

import java.util.*;
import student.gettysburg.engine.utility.configure.BattleOrder;
import student.gettysburg.engine.utility.configure.UnitInitializer;

/**
 * This is the game engine master class that provides the interface to the game
 * implementation. DO NOT change the name of this file and do not change the
 * name of the methods that are defined here since they must be defined to implement the
 * GbgGame interface.
 * 
 * @version Jun 9, 2017
 */
public class GettysburgEngine implements GbgGame
{
	/**
	 * private fields for turn number, step, and the units on the board
	 */
	private int turnNum;
	private GbgGameStep step;
	protected ArrayList<UnitInitializer> units;
	protected ArrayList<GbgUnit> movedUnits;
	protected ArrayList<GbgUnit> facedUnits;
	
	
	/**
	 * Constructor - add the three units to the board, init turn to 1, and step to union
	 */
	public GettysburgEngine(){
		this.turnNum = 1;
		this.step = GbgGameStep.UMOVE;
		units = new ArrayList<UnitInitializer>();
		movedUnits = new ArrayList<GbgUnit>();
		facedUnits = new ArrayList<GbgUnit>();
		units.add(new UnitInitializer(this.turnNum, 11, 11, UNION, 5, WEST, "Gamble", 3, DIVISION, CAVALRY));
		units.add(new UnitInitializer(this.turnNum, 13, 9, UNION, 5, SOUTH, "Devin", 3, DIVISION, CAVALRY));
		units.add(new UnitInitializer(this.turnNum, 8, 8, CONFEDERATE, 5, EAST, "Heth", 2, DIVISION, INFANTRY));
	}
	
	/*
	 * @see gettysburg.common.GbgGame#endBattleStep()
	 */
	@Override
	public void endBattleStep()
	{
		// TODO Auto-generated method stub

	}

	/*
	 * @see gettysburg.common.GbgGame#endMoveStep()
	 */
	@Override
	public void endMoveStep()
	{
		// TODO Auto-generated method stub

	}

	/*
	 * @see gettysburg.common.GbgGame#endStep()
	 */
	@Override
	public GbgGameStep endStep()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @see gettysburg.common.GbgGame#getBattlesToResolve()
	 */
	@Override
	public Collection<BattleDescriptor> getBattlesToResolve()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @see gettysburg.common.GbgGame#getCurrentStep()
	 */
	@Override
	public GbgGameStep getCurrentStep()
	{
		return this.step;
	}
	
	/*
	 * @see gettysburg.common.GbgGame#getGameStatus()
	 * Checks every unit, if tehre are different army ID's then it's in progress
	 */
	@Override
	public GbgGameStatus getGameStatus()
	{
		boolean isOver = true;
		ArmyID id = null;
		for(UnitInitializer u : this.units){
			if(id != u.unit.getArmy() || id == null){
				isOver = false;
				break;
			}
			id = u.unit.getArmy();
		}
		if(id == CONFEDERATE){
			return GbgGameStatus.CONFEDERATE_WINS;
		}
		if(id == UNION){
			return GbgGameStatus.UNION_WINS;
		}
		return GbgGameStatus.IN_PROGRESS;
	}
	
	/*
	 * @see gettysburg.common.GbgGame#getGameDate()
	 */
	@Override
	public Calendar getGameDate()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @see gettysburg.common.GbgGame#getSquareDescriptor(gettysburg.common.Coordinate)
	 */
	@Override
	public GbgSquareDescriptor getSquareDescriptor(Coordinate where)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @see gettysburg.common.GbgGame#getTurnNumber()
	 */
	@Override
	public int getTurnNumber()
	{
		return this.turnNum;
	}

	/*
	 * @see gettysburg.common.GbgGame#getUnitFacing(int)
	 */
	@Override
	public Direction getUnitFacing(GbgUnit unit)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @see gettysburg.common.GbgGame#getUnitsAt(gettysburg.common.Coordinate)
	 */
	@Override
	public Collection<GbgUnit> getUnitsAt(Coordinate where)
	{
		/*Collection<GbgUnit> units = new ArrayList<GbgUnit>();
		for (UnitInitializer u : this.units){
			if (u.where.equals(where)){
				units.add(u.unit);
			}
		}
		return units;*/
		return null;
	}

	/*
	 * @see gettysburg.common.GbgGame#moveUnit(gettysburg.common.GbgUnit, gettysburg.common.Coordinate, gettysburg.common.Coordinate)
	 */
	@Override
	public void moveUnit(GbgUnit unit, Coordinate from, Coordinate to)
	{	
		// Ensure that it's the right player's turn
		if(this.step == GbgGameStep.UMOVE){
			if (unit.getArmy() == CONFEDERATE){
				throw new GbgInvalidMoveException("Wrong turn");
			}
		}
		
		if(this.step == GbgGameStep.CMOVE){
			if (unit.getArmy() == UNION){
				throw new GbgInvalidMoveException("Wrong turn");
			}
		}
		
		// Ensure that unit has not yet moved
		for(GbgUnit u: this.movedUnits){
			if(u.getLeader() == unit.getLeader() && u.getArmy() == unit.getArmy()){
				throw new GbgInvalidMoveException("Unit has already moved");
			}
		}
		
		// Ensure that no other unit is in the square
		for(UnitInitializer u : this.units){
			if (u.where.equals(to)){
				throw new GbgInvalidMoveException("Tried moving into another unit");
			}
		}
		/* ensure that there is a path from the source to the
		* destination whose length does not exceed the moving unit’s movement factor */
		if(unit.getMovementFactor() < from.distanceTo(to)){
			throw new GbgInvalidMoveException("Move factor is less than distance attempted to move");
		}
		// Otherwise, move
		for(UnitInitializer u: this.units){
			if (u.unit == unit){
				u.where = to;
			}
		}
		this.movedUnits.add(unit);
	}

	/*
	 * @see gettysburg.common.GbgGame#resolveBattle(int)
	 */
	@Override
	public BattleResolution resolveBattle(BattleDescriptor battle)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @see gettysburg.common.GbgGame#setUnitFacing(gettysburg.common.GbgUnit, gettysburg.common.Direction)
	 */
	@Override
	public void setUnitFacing(GbgUnit unit, Direction direction)
	{
		
		// Ensure that it's the right player's turn
				if(this.step == GbgGameStep.UMOVE){
					if (unit.getArmy() == CONFEDERATE){
						throw new GbgInvalidMoveException("Wrong turn");
					}
				}
				
				if(this.step == GbgGameStep.CMOVE){
					if (unit.getArmy() == UNION){
						throw new GbgInvalidMoveException("Wrong turn");
					}
				}
		
		// Check to see if unit has already been faced
			for(GbgUnit u: this.facedUnits){
				if(u.getArmy() == unit.getArmy() && u.getLeader() == unit.getLeader()){
					throw new GbgInvalidMoveException("Unit has already been faced this turn");
				}
			}
		
			// move unit
			unit.setFacing(direction);
		
		this.facedUnits.add(unit);

	}

	/*
	 * @see gettysburg.common.GbgGame#whereIsUnit(gettysburg.common.GbgUnit)
	 */
	@Override
	public Coordinate whereIsUnit(GbgUnit unit)
	{
		
		return null;
	}

	/*
	 * @see gettysburg.common.GbgGame#whereIsUnit(java.lang.String, gettysburg.common.ArmyID)
	 */
	@Override
	public Coordinate whereIsUnit(String leader, ArmyID army)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
