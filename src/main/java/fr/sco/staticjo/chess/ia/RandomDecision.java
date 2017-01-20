package fr.sco.staticjo.chess.ia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class RandomDecision {

	public World world;
	public List<PlayerProfile> players;

	public RandomDecision(World world, List<PlayerProfile> players){
		this.world = world;
		this.players = players;

	}
	public AtomicInteger tot = new AtomicInteger(0);

	
	public Action getBestNextAction(int depth, PlayerProfile player){

		
		Map<PlayerProfile, List<DataForAction>> dataForActionInitMap = new HashMap<>();
		Map<PlayerProfile, Score> scoreInitMap = new HashMap<>();
		players.stream().forEach(p -> {dataForActionInitMap.put(p, world.getAvailableInitDataActions(player));
		scoreInitMap.put(p, world.getInitActionScore(player));
		});

		return getBestNextAction(depth, player, players, dataForActionInitMap, scoreInitMap, new HashMap<>());
	}

	public Action getBestNextAction(int depth, PlayerProfile player, List<PlayerProfile> players, Map<PlayerProfile, List<DataForAction>> dataForActionInitMap, 
			Map<PlayerProfile, Score> scoreInitMap, Map<PlayerProfile, Action> doneThisTurn){
		Action bestAction = null;
		Score bestScore = null;
		int pos = players.indexOf(player);
		Condition cond = new Condition(world.getTypeIteration(), world.getInitIterationValue(), world.getLimitIterationValue());
		List<DataForAction> dataForActionInit = dataForActionInitMap.get(player);
		List<DataForAction> dataForActions = new ArrayList<>(dataForActionInit);

		while (cond.doContinue() && dataForActions != null && dataForActions.size() >0){
			
			tot.incrementAndGet();
			System.out.println(depth + " - " + player.aff() + " " + cond.initValue);
			//Choose an action
			int total = dataForActions.size();
			int randomNum = ThreadLocalRandom.current().nextInt(0, total);
			DataForAction dataForAction = dataForActions.get(randomNum);
			dataForActions.remove(randomNum);

			Map<PlayerProfile, Action> maybeDoneThisTurn = new HashMap<>(doneThisTurn);
			Map<PlayerProfile, Score> maybeScoreInitMap = new HashMap<>(scoreInitMap);
			Map<PlayerProfile, List<DataForAction>> maybeDataForActionInitMap = new HashMap<>(dataForActionInitMap);

			//Play it
			dataForAction.updateWithEstimatedDoneByOtherPlayersThisTurn(doneThisTurn);
			Action currentAction = world.getAction(player, dataForAction);
			maybeDoneThisTurn.put(player, currentAction);
			maybeScoreInitMap.get(player).addOrUpdateScore(currentAction.getScore());
			maybeDataForActionInitMap.put(player, currentAction.getDataForNextAvailableActions());

			for (int p = pos+1; p < players.size(); p++){
				PlayerProfile next = players.get(p);
				Action act = getBestNextAction(depth, next, players, maybeDataForActionInitMap, maybeScoreInitMap, maybeDoneThisTurn);
				maybeDoneThisTurn.put(next, act); 
				maybeDataForActionInitMap.put(next, act.getDataForNextAvailableActions());
				maybeScoreInitMap.get(next).addOrUpdateScore(act.getScore());
			}

			if (depth > 1){
				for (int p = 0; p < pos; p++){
					PlayerProfile next = players.get(p);
					Action act = getBestNextAction(depth -1, next, players, maybeDataForActionInitMap, maybeScoreInitMap, maybeDoneThisTurn);
					maybeDoneThisTurn.put(next, act); 
					maybeScoreInitMap.get(next).addOrUpdateScore(act.getScore());
				}
				if (!world.isMatchFinished(maybeScoreInitMap)){
					Action action = getBestNextAction(depth -1, player, players, maybeDataForActionInitMap, maybeScoreInitMap, new HashMap<>());
					currentAction.getScore().addOrUpdateScore(action.getScore());
					maybeScoreInitMap.put(player, currentAction.getScore());
					currentAction.setNextAction(action);
				}

			}
			if (bestScore == null || !bestScore.isBetterThan(maybeScoreInitMap, player) ){
				bestAction = currentAction;
				bestScore = currentAction.getScore();
			}



		}
		return bestAction;


	}

}
