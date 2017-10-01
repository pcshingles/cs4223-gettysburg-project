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
package gettysburg.engine.common;
import java.util.ArrayList;
import gettysburg.common.*;
import student.gettysburg.engine.common.CoordinateImpl;
import student.gettysburg.engine.common.GbgGameState;
import student.gettysburg.engine.common.GbgGameStateBattle;
import student.gettysburg.engine.common.GbgGameStateMove;
import student.gettysburg.engine.common.GettysburgEngine;

/**
 * Test implementation of the Gettysburg game.
 * @version Jul 31, 2017
 */
public class TestGettysburgEngine extends GettysburgEngine implements TestGbgGame
{
	/*
	 * @see gettysburg.common.TestGbgGame#clearBoard()
	 */
	@Override
	public void clearBoard()
	{
		this.gameState.getBoard().clearBoard();
	}
	/*
	 * Used to see if we initialized the units correctly
	 * 
	 */
	public ArrayList<GbgUnit> getUnits(){
		return super.gameState.getBoard().getAllUnits();
	}

	/*
	 * @see gettysburg.common.TestGbgGame#putUnitAt(gettysburg.common.GbgUnit, int, int, gettysburg.common.Direction)
	 */
	@Override
	public void putUnitAt(GbgUnit arg0, int arg1, int arg2, Direction arg3)
	{
		arg0.setFacing(arg3);
		this.gameState.getBoard().putUnit(arg0, CoordinateImpl.makeCoordinate(arg1,arg2));
	}

	/*
	 * @see gettysburg.common.TestGbgGame#setBattleResult(gettysburg.common.BattleDescriptor, gettysburg.common.BattleResult)
	 */
	@Override
	public void setBattleResult(BattleDescriptor arg0, BattleResult arg1)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * @see gettysburg.common.TestGbgGame#setGameStep(gettysburg.common.GbgGameStep)
	 */
	@Override
	public void setGameStep(GbgGameStep arg0)
	{
		GbgGameState state = this.gameState;
		switch(arg0) {
		case CBATTLE:
			this.gameState = new GbgGameStateBattle(state.getTurnNum(), GbgGameStep.CBATTLE, state.getBoard());
			break;
		case CMOVE:
			this.gameState = new GbgGameStateMove(state.getTurnNum(), GbgGameStep.CMOVE, state.getBoard());
			break;
		case UBATTLE:
			this.gameState = new GbgGameStateBattle(state.getTurnNum(), GbgGameStep.UBATTLE, state.getBoard());
			break;
		case UMOVE:
			this.gameState = new GbgGameStateMove(state.getTurnNum(), GbgGameStep.UMOVE, state.getBoard());
			break;
		default:
			break;
		}
	}

	/*
	 * @see gettysburg.common.TestGbgGame#setGameTurn(int)
	 */
	@Override
	public void setGameTurn(int arg0)
	{
		this.gameState.setTurnNum(arg0);
	}

}
