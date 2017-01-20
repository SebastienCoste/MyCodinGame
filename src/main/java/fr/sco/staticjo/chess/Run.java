package fr.sco.staticjo.chess;

import java.util.Arrays;
import java.util.List;

import fr.sco.staticjo.chess.ia.PlayerProfile;
import fr.sco.staticjo.chess.ia.RandomDecision;
import fr.sco.staticjo.chess.ia.World;
import fr.sco.staticjo.chess.world.BlackPlayer;
import fr.sco.staticjo.chess.world.ChessWorld;
import fr.sco.staticjo.chess.world.WhitePlayer;

public class Run {

	public static void main(String args[]) {
		
		World world = new ChessWorld();
		WhitePlayer whitePlayer = new WhitePlayer();
		BlackPlayer blackPlayer = new BlackPlayer();
		List<PlayerProfile> players = Arrays.asList(whitePlayer, blackPlayer);
		RandomDecision ia = new RandomDecision(world, players);
		
		ia.getBestNextAction(10, whitePlayer);
		System.err.println(ia.tot);
	}
	
	
}
