package fr.sco.staticjo.codingame.common.decision;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomDecison {


	public enum Limit{
		TIME,
		ITERATIONS;
	}

	public interface Action{
		Double getScore();
		List<DataForAction> getDataForNextAvailableActions();

	}

	public interface DataForAction{

	}

	public interface World{
		Object getInitIterationValue();
		Object getLimitIterationValue();
		Limit getTypeIteration();
		Double getInitActionScore();
		List<DataForAction> getAvailableInitDataActions();
		
		Action getAction(DataForAction dataForAction);
	}

	private class Condition{

		public Limit limit;
		public Object limitValue;
		public Object initValue;

		public Condition (Limit limit, Object initValue, Object limitValue){
			this.limit = limit;
			this.limitValue = limitValue;
			this.initValue = initValue;
		}


		public boolean doContinue(){
			switch (limit) {
			case TIME:
				return new Date().getTime() < (long)limitValue;
			case ITERATIONS:
			default:
				initValue = (int)initValue+1;
				return (int)initValue < (int)limitValue;
			}
		}
	}

	public World world;

	public RandomDecison(World world){
		this.world = world;

	}

	public List<Action> getBestNextAction(int depth){
		
		List<Action> bestActions = null;
		Double bestScore = world.getInitActionScore();
		
		Condition cond = new Condition(world.getTypeIteration(), world.getInitIterationValue(), world.getLimitIterationValue());
		List<DataForAction> dataForActionInit = world.getAvailableInitDataActions();

		while (cond.doContinue()){
			List<DataForAction> dataForActions = dataForActionInit;
			List<Action> actionList = new ArrayList<>(depth);
			Double score = world.getInitActionScore();
			for (int i = 0; i < depth && dataForActions != null && dataForActions.size() > 0; i++){
				int total = dataForActions.size();
				int randomNum = ThreadLocalRandom.current().nextInt(0, total);
				Action action = world.getAction(dataForActions.get(randomNum));
				actionList.add(action);
				dataForActions = action.getDataForNextAvailableActions();
				score += action.getScore();
			}
			if (bestScore < score){
				bestActions = actionList;
				bestScore = score;
			}
			
		}

		return bestActions;
	}

}
