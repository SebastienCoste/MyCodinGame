package fr.sco.staticjo.codingame.common.genetic.bestpath;

import java.util.stream.IntStream;

import fr.sco.staticjo.codingame.common.genetic.FitnessCalc;
import fr.sco.staticjo.codingame.common.genetic.Person;

public class PathCalculator implements FitnessCalc {

	@Override
	public int getFitness(Person person) {
		WorldMap map = (WorldMap) person;
		Point[] points = WorldMap.getPointList();
		double sum = IntStream.range(0, map.geneSize()-1).mapToDouble(e -> points[map.getGene(e)].getDistanceTo(points[map.getGene(e+1)])).sum();
		return Double.valueOf(sum + points[map.getGene(0)].getDistanceTo(points[map.getGene(map.geneSize()-1)])).intValue();
	}

	@Override
	public int getMaxFitness() {
		return 40;
	}

}
