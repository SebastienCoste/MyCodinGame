package fr.sco.staticjo.codingame.common.genetic.bestpath;

import fr.sco.staticjo.codingame.common.genetic.GeneticAlgo;
import fr.sco.staticjo.codingame.common.genetic.Population;

public class Runner {

	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {

        // Set a candidate solution
		int size = 4;
		WorldMap.setDefaultGeneLength(size);
		WorldMap.setPointList(new Point[size]);
		WorldMap.addPoint(new Point(0, 0, 0), 0);
		WorldMap.addPoint(new Point(1, 10, 0), 1);
		WorldMap.addPoint(new Point(2, 10, 10), 2);
		WorldMap.addPoint(new Point(3, 0, 10), 3);
		
		WorldMap.setCalc(new PathCalculator());
		
        // Create an initial population
        Population<WorldMap> myPop = new Population<WorldMap>(size, true, WorldMap.class);
        
        // Evolve our population until we reach an optimum solution
        int generationCount = 0;
        GeneticAlgo<WorldMap> algorithm = new GeneticPathAlgo();
        
        while (myPop.getFittest().getFitness() >= WorldMap.getCalc().getMaxFitness()) {
            generationCount++;
            System.out.println("Generation: " + generationCount + " Fittest: " + myPop.getFittest().getFitness());
            myPop = algorithm.evolvePopulation(myPop);
        }
        System.out.println("Solution found!");
        System.out.println("Generation: " + generationCount);
        System.out.println("Genes:");
        System.out.println(myPop.getFittest());

    }
}
