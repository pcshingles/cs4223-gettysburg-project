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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	/*
	 * @see gettysburg.common.TestGbgGame#setGameTurn(int)
	 */
	@Override
	public void setGameTurn(int arg0)
	{
		// TODO Auto-generated method stub

	}

}
