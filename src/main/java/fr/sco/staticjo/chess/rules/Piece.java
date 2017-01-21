package fr.sco.staticjo.chess.rules;

import java.util.ArrayList;
import java.util.List;

import fr.sco.staticjo.chess.world.DataForMove;

public abstract class Piece {
	
//	public abstract List<Point> getAvailablePoints(Board board);
	
	public List<DataForMove> getAllMoves(Board board, Point position, List<Point> destination){
		
		List<DataForMove> res = new ArrayList<>();
		
		if (destination.size() == 0 || !Validator.INSTANCE.canLeaveThisPosition(board, position)){
			return res;
		}
		
		return res;
	}
}
