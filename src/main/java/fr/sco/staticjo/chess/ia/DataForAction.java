package fr.sco.staticjo.chess.ia;

import java.util.Map;

public interface DataForAction{

	void updateWithEstimatedDoneByOtherPlayersThisTurn(Map<PlayerProfile, Action> actionMap);
}
