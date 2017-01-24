package fr.sco.staticjo.codingame.common.genetic;

public interface FitnessCalc {

	public int getFitness(Person person);
	public int getMaxFitness();
}
