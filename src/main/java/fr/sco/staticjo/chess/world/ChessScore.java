package fr.sco.staticjo.chess.world;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import fr.sco.staticjo.chess.ia.PlayerProfile;
import fr.sco.staticjo.chess.ia.Score;

public class ChessScore implements Score {

	@Override
	public boolean isBetterThan(Map<PlayerProfile, Score> scoreMap, PlayerProfile player) {
		return ThreadLocalRandom.current().nextInt(0, 2)%2 == 0;
	}

	@Override
	public void addOrUpdateScore(Score add) {
	}

}
