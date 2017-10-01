package student.gettysburg.engine.common;
import gettysburg.common.Coordinate;
import gettysburg.common.Direction;
import gettysburg.common.GbgGameStatus;
import gettysburg.common.GbgGameStep;
import gettysburg.common.GbgUnit;
import gettysburg.common.exceptions.GbgInvalidMoveException;

public class GbgGameStateBattle extends GbgGameState {
	
	/**
	 * Constructor, should always get a board
	 * @param turnNum
	 * @param step
	 * @param board
	 */
	public GbgGameStateBattle(int turnNum, GbgGameStep step, Board board) {
		super(turnNum, step, board);
	}

	@Override
	public GbgGameState endStep() {
		if(this.getStep() == GbgGameStep.CBATTLE) {
			return new GbgGameStateMove(this.getTurnNum() + 1, GbgGameStep.UMOVE, this.getBoard());
		} else if(this.getStep() == GbgGameStep.UBATTLE) {
			return new GbgGameStateMove(this.getTurnNum(), GbgGameStep.CMOVE, this.getBoard());
		} else return null;
	}

	@Override
	public GbgGameStatus getStatus() {
		return GbgGameStatus.IN_PROGRESS;
	}

	@Override
	public void moveUnit(GbgUnit unit, Coordinate from, Coordinate where) {
		throw new GbgInvalidMoveException("On battle step, cannot move.");
	}

	@Override
	public void setUnitFacing(GbgUnit unit, Direction to) {
		throw new GbgInvalidMoveException("On battle step, cannot turn unit.");
	}

}
