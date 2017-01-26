package fr.sco.staticjo.codingame.common.genetic.bestpath;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import fr.sco.staticjo.codingame.common.genetic.GeneticAlgo;
import fr.sco.staticjo.codingame.common.genetic.Population;

public class Runner {

	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {

        // Set a candidate solution
		int size = 100;
		int sizeMap= 100;
		WorldMap.setDefaultGeneLength(size);
		WorldMap.setPointList(new Point[size]);
		IntStream.range(0, size).forEach(e -> WorldMap.addPoint(p(e, sizeMap), e));
		
		
		WorldMap.setCalc(new PathCalculator());
		
        // Create an initial population
        Population<WorldMap> myPop = new Population<WorldMap>(size, true, WorldMap.class);
        
        // Evolve our population until we reach an optimum solution
        int generationCount = 0;
        GeneticAlgo<WorldMap> algorithm = new GeneticPathAlgo();
        int fit = Integer.MAX_VALUE;
        int geneFit = 0;
        long init = new Date().getTime();
		while (myPop.getFittest().getFitness() >= WorldMap.getCalc().getMaxFitness() && generationCount <300000) {
            generationCount++;
            int fittest = myPop.getFittest().getFitness();
            if (fit > fittest){
            	fit = fittest;
            	geneFit = generationCount;
            	System.out.println("Generation: " + generationCount + " Fittest: " + fittest + " time: " + ((new Date().getTime() - init)/1000));
            }
            myPop = algorithm.evolvePopulation(myPop);
        }
        System.out.println("Solution found!");
        System.out.println("Generation: " + geneFit);
        System.out.println("Genes:");
        System.out.println(myPop.getFittest());

    }

	private static Point p(int e, int max) {
		return new Point(e, ThreadLocalRandom.current().nextInt(0, max), ThreadLocalRandom.current().nextInt(0, max));
	}
}
