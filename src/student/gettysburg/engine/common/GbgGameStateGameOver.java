package student.gettysburg.engine.common;
import gettysburg.common.Coordinate;
import gettysburg.common.Direction;
import gettysburg.common.GbgGameStatus;
import gettysburg.common.GbgGameStep;
import gettysburg.common.GbgUnit;
import gettysburg.common.exceptions.GbgInvalidMoveException;

public class GbgGameStateGameOver extends GbgGameState {

	/**
	 * Constructor, should always get a board
	 * @param turnNum
	 * @param step
	 * @param board
	 */
	public GbgGameStateGameOver(int turnNum, GbgGameStep step, Board board) {
		super(turnNum, step, board);
		// TODO Auto-generated constructor stub
	}

	@Override
	public GbgGameState endStep() {
		throw new GbgInvalidMoveException("Game has ended, cannot end step.");
	}

	@Override
	public GbgGameStatus getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void moveUnit(GbgUnit unit, Coordinate from, Coordinate where) {
		//throw new GbgInvalidMoveException("Game has ended, cannot move.");
	}

	@Override
	public void setUnitFacing(GbgUnit unit, Direction to) {
		//throw new GbgInvalidMoveException("Game has ended, cannot turn unit.");
	}


}
