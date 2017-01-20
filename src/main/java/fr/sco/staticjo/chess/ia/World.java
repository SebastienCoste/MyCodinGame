package fr.sco.staticjo.chess.ia;

import java.util.List;
import java.util.Map;

public interface World{
	Object getInitIterationValue();
	Object getLimitIterationValue();
	Limit getTypeIteration();
	Score getInitActionScore(PlayerProfile player);
	List<DataForAction> getAvailableInitDataActions(PlayerProfile player);
	
	Action getAction(PlayerProfile player, DataForAction dataForAction);
	boolean isMatchFinished(Map<PlayerProfile, Score> scoreMap);
}
