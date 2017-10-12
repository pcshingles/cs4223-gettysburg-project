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
import gettysburg.common.exceptions.GbgInvalidActionException;

import static gettysburg.common.ArmyID.*;
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
	
	// Game state holds data of the state of the game.
	protected GbgGameState gameState;
	
	/**
	 * Constructor - initialize game state to move
	 */
	public GettysburgEngine(){
		int turnNum = 1;
		GbgGameStep step = GbgGameStep.UMOVE;
		ArrayList<UnitInitializer> units = new ArrayList<UnitInitializer>();
		units.addAll(BattleOrder.getBattleOrder(UNION, 0));
		units.addAll(BattleOrder.getBattleOrder(CONFEDERATE, 0));
		this.gameState = new GbgGameStateMove(turnNum, step, units);
	}

	/*
	 * @see gettysburg.common.GbgGame#endStep()
	 */
	@Override
	public GbgGameStep endStep()
	{
		this.setState(this.gameState.endStep());
		return this.gameState.getStep();
	}

	/*
	 * @see gettysburg.common.GbgGame#getBattlesToResolve()
	 */
	@Override
	public Collection<BattleDescriptor> getBattlesToResolve()
	{
		try {
		return ((GbgGameStateBattle) this.gameState).getBattlesToResolve();
		}catch(Exception e) {
			return new ArrayList<BattleDescriptor>();
		}
	}

	/*
	 * @see gettysburg.common.GbgGame#getCurrentStep()
	 */
	@Override
	public GbgGameStep getCurrentStep()
	{
		return this.gameState.getStep();
	}
	
	/*
	 * @see gettysburg.common.GbgGame#getGameStatus()
	 * Checks every unit, if there are different army ID's then it's in progress
	 */
	@Override
	public GbgGameStatus getGameStatus()
	{
		return this.gameState.getStatus();
	}

	/*
	 * @see gettysburg.common.GbgGame#getTurnNumber()
	 */
	@Override
	public int getTurnNumber()
	{
		return this.gameState.getTurnNum();
	}

	/*
	 * @see gettysburg.common.GbgGame#getUnitFacing(int)
	 */
	@Override
	public Direction getUnitFacing(GbgUnit unit)
	{
		return this.gameState.getBoard().getUnitFacing(unit);
	}

	/*
	 * @see gettysburg.common.GbgGame#getUnitsAt(gettysburg.common.Coordinate)
	 */
	@Override
	public Collection<GbgUnit> getUnitsAt(Coordinate where)
	{
		return this.gameState.getBoard().getUnitsAt(where);
	}

	/*
	 * @see gettysburg.common.GbgGame#moveUnit(gettysburg.common.GbgUnit, gettysburg.common.Coordinate, gettysburg.common.Coordinate)
	 */
	@Override
	public void moveUnit(GbgUnit unit, Coordinate from, Coordinate to)
	{	
		this.gameState.moveUnit(unit, from, to);
	}

	/*
	 * @see gettysburg.common.GbgGame#resolveBattle(int)
	 */
	@Override
	public BattleResolution resolveBattle(BattleDescriptor battle)
	{
		try {
			return ((GbgGameStateBattle) this.gameState).resolveBattle(battle);
			}catch(Exception e) {
				throw new GbgInvalidActionException("Cannot resolve battle in this state");
			}
	}

	/*
	 * @see gettysburg.common.GbgGame#setUnitFacing(gettysburg.common.GbgUnit, gettysburg.common.Direction)
	 */
	@Override
	public void setUnitFacing(GbgUnit unit, Direction direction)
	{
		this.gameState.setUnitFacing(unit, direction);
	}

	/*
	 * @see gettysburg.common.GbgGame#whereIsUnit(gettysburg.common.GbgUnit)
	 */
	@Override
	public Coordinate whereIsUnit(GbgUnit unit)
	{
		return this.gameState.getBoard().whereIsUnit(unit);
	}

	/*
	 * @see gettysburg.common.GbgGame#whereIsUnit(java.lang.String, gettysburg.common.ArmyID)
	 */
	@Override
	public Coordinate whereIsUnit(String leader, ArmyID army)
	{
		return this.gameState.getBoard().whereIsUnit(leader, army);
	}
	
	/**
	 * Getter for a unit by leader and army ID.
	 */
	public GbgUnit getUnit(String leader, ArmyID army) {
		return this.gameState.getBoard().getUnit(leader, army);
	}
	
	/**
	 * Setter for the state, set by the states
	 * @param state
	 */
	public void setState(GbgGameState state) {
		this.gameState = state;
	}

}
