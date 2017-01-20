package fr.sco.staticjo.chess.ia;

import java.util.Map;

public interface Score {
	boolean isBetterThan(Map<PlayerProfile, Score> scoreMap, PlayerProfile player);
	void addOrUpdateScore(Score add); //Sometimes only the ending result matters
	
}
