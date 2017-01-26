package fr.sco.staticjo.codingame.common.genetic.bestpath;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import fr.sco.staticjo.codingame.common.genetic.FitnessCalc;
import fr.sco.staticjo.codingame.common.genetic.Person;

public class WorldMap implements Person {

	private static int numberOfCities;
	private Long[] genes;
	private static FitnessCalc calc;
	private static Point[] pointList;
	
	@Override
	public void generatePerson() {
		setGenes(new Long[numberOfCities]);
		List<Integer> allNumbers = IntStream.range(0, numberOfCities).boxed().collect(Collectors.toList());
		IntStream.range(0, numberOfCities).forEach(e -> {
			int rand = ThreadLocalRandom.current().nextInt(0, allNumbers.size());
			setGene(e, Long.valueOf(allNumbers.get(rand).toString()));
			allNumbers.remove(rand);
		});
		if (geneSize() > numberOfCities){
			IntStream.range(numberOfCities, geneSize()).forEach(e ->
			setGene(e, Long.valueOf(Integer.valueOf(ThreadLocalRandom.current().nextInt(0, numberOfCities)).toString())));
		}
	}

	public static void setDefaultGeneLength(int length) {
		numberOfCities = length;
	}

	@Override
	public Long getGene(int index) {
		return getGenes()[index];
	}

	@Override
	public void setGene(int index, Long value) {
		getGenes()[index] = value;
	}

	@Override
	public int getFitness() {
		return getCalc().getFitness(this);
	}

	@Override
	public int geneSize() {
		return getGenes().length;
	}

	public static void addPoint(Point p, int index){
		pointList[index] = p;
	}
	public static Point[] getPointList() {
		return pointList;
	}

	public static void setPointList(Point[] points) {
		pointList = points;
	}

	public Long[] getGenes() {
		return genes;
	}

	public void setGenes(Long[] genes) {
		this.genes = genes;
	}

	public static FitnessCalc getCalc() {
		return calc;
	}

	public static void setCalc(FitnessCalc calc) {
		WorldMap.calc = calc;
	}

	@Override
	public String toString() {
		return "WorldMap [genes=" + Arrays.toString(genes) + ", fit=" + getFitness() + "]";
	}


}
