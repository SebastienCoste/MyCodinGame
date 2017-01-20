package fr.sco.staticjo.chess.world;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import fr.sco.staticjo.chess.ia.Action;
import fr.sco.staticjo.chess.ia.DataForAction;
import fr.sco.staticjo.chess.ia.Score;

public class Move implements Action{

	public Action next;
	
	@Override
	public Score getScore() {
		return new ChessScore();
	}

	@Override
	public void setScore(Score score) {
		
	}

	@Override
	public List<DataForAction> getDataForNextAvailableActions() {
		return IntStream.range(0, 20).mapToObj(i -> new DataForMove()).collect(Collectors.toList());
	}

	@Override
	public void setNextAction(Action next) {
		this.next = next;
	}

	@Override
	public Action getNextAction() {
		return next;
	}

}
