package student.gettysburg.engine.common;

import java.util.ArrayList;

import gettysburg.common.Coordinate;
import gettysburg.common.Direction;
import gettysburg.common.GbgGameStatus;
import gettysburg.common.GbgGameStep;
import gettysburg.common.GbgUnit;
import student.gettysburg.engine.utility.configure.UnitInitializer;

public abstract class GbgGameState {

	protected int turnNum;
	protected GbgGameStep step;
	protected Board board;
	
	
	public GbgGameState(int turnNum, GbgGameStep step, ArrayList<UnitInitializer> units) {
		this.turnNum = turnNum;
		this.step = step;
		this.board = new Board(units);
		
	}
	public GbgGameState(int turnNum, GbgGameStep step, Board board) {
		this.turnNum = turnNum;
		this.step = step;
		this.board = board;
	}
	
	/**
	 * Getter for turn num
	 * @return turnNum
	 */
	public int getTurnNum() {
		return this.turnNum;
	}
	
	/**
	 * Getter for the step.
	 * @return step
	 */
	public GbgGameStep getStep() {
		return this.step;
	}
	
	/**
	 * Getter for the board
	 * @return board
	 */
	public Board getBoard() {
		return this.board;
	}
	
	/**
	 * Setter for turn number
	 * @param turn
	 */
	public void setTurnNum(int turn) {
		this.turnNum = turn;
	}
	
	/**
	 * Setter for the game step
	 * @param step
	 */
	public void setGameStep(GbgGameStep step) {
		this.step = step;
	}
	
	/**
	 * Abstract method for ending the turn
	 */
	public abstract GbgGameState endStep();
	
	/**
	 * Abstract getter for the game status. 
	 * @return
	 */
	public abstract GbgGameStatus getStatus();
	
	/**
	 * Abstract move unit function, implemented in move state
	 * @param unit
	 * @param from
	 * @param where
	 */
	public abstract void moveUnit(GbgUnit unit, Coordinate from, Coordinate where);
	
	/**
	 * Abstract set unit facing method, implemented in move state
	 * @param unit
	 * @param to
	 */
	public abstract void setUnitFacing(GbgUnit unit, Direction to);
	
}
