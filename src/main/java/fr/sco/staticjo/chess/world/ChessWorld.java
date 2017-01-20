package fr.sco.staticjo.chess.world;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import fr.sco.staticjo.chess.ia.Action;
import fr.sco.staticjo.chess.ia.DataForAction;
import fr.sco.staticjo.chess.ia.Limit;
import fr.sco.staticjo.chess.ia.PlayerProfile;
import fr.sco.staticjo.chess.ia.Score;
import fr.sco.staticjo.chess.ia.World;

public class ChessWorld implements World {

	@Override
	public Object getInitIterationValue() {
		return 0;
	}

	@Override
	public Object getLimitIterationValue() {
		return 11;
	}

	@Override
	public Limit getTypeIteration() {
		return Limit.ITERATIONS;
	}

	@Override
	public Score getInitActionScore(PlayerProfile player) {
		return new ChessScore();
	}

	@Override
	public List<DataForAction> getAvailableInitDataActions(PlayerProfile player) {
		return IntStream.range(0, 20).mapToObj(i -> new DataForMove()).collect(Collectors.toList());
	}

	@Override
	public Action getAction(PlayerProfile player, DataForAction dataForAction) {
		return new Move();
	}

	@Override
	public boolean isMatchFinished(Map<PlayerProfile, Score> scoreMap) {
		return false;
	}

}
