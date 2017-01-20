package fr.sco.staticjo.chess.ia;

import java.util.List;


public interface Action{
	
	Score getScore();
	void setScore(Score score);
	List<DataForAction> getDataForNextAvailableActions();
	
	void setNextAction(Action next);
	Action getNextAction();
}
