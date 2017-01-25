package fr.sco.staticjo.codingame.common.genetic.bestpath;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import fr.sco.staticjo.codingame.common.genetic.GeneticAlgo;
import fr.sco.staticjo.codingame.common.genetic.Person;
import fr.sco.staticjo.codingame.common.genetic.Population;

public class GeneticPathAlgo extends GeneticAlgo<WorldMap> {


	@Override
	protected Person crossover(Population<WorldMap> pop, Person indiv1, Person indiv2) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		WorldMap newborn = new WorldMap();
		WorldMap parent1 = (WorldMap) indiv1;
		WorldMap parent2 = (WorldMap) indiv2;
		boolean useParent1 = ThreadLocalRandom.current().nextInt(0,1) == 0;
		WorldMap parentref = parent1;
		WorldMap parentbis = parent2;
		if (useParent1){
			newborn.setGenes(new byte[parent1.geneSize()]);
		} else {
			newborn.setGenes(new byte[parent2.geneSize()]);
			parentref = parent2;
			parentbis = parent1;
		}

		int length = ThreadLocalRandom.current().nextInt(0,Double.valueOf(uniformRate*parentref.geneSize()).intValue());

		int start = ThreadLocalRandom.current().nextInt(0, parentref.geneSize() - length);
		byte[] parentRefGenotype = Arrays.copyOfRange(parentref.getGenes(), start, start + length);
		List<Byte> parentRefGenotypeList = IntStream.range(0, parentRefGenotype.length).mapToObj(e -> parentRefGenotype[e]).collect(Collectors.toList());
		int range = 0;
		int rangeParOff = 0;
		while (range < start){
			byte testGene = parentbis.getGene(rangeParOff);
			if (!parentRefGenotypeList.contains(testGene)){
				newborn.setGene(range, testGene);
				range++;
			}
			rangeParOff++;
		}
		while (range < start+length){
			newborn.setGene(range, parentref.getGene(range));
			range++;
		}
		while (rangeParOff < parentbis.geneSize()){
			byte testGene = parentbis.getGene(rangeParOff);
			if (!parentRefGenotypeList.contains(testGene)){
				newborn.setGene(range, testGene);
				range++;
			}
			rangeParOff++;
		}
		return newborn;
	}

	@Override
	protected void mutate(Person person) {
		WorldMap indiv = (WorldMap) person;
		Map<Byte, Integer> timesSeen = new HashMap<>();
		for (int i = 0; i < indiv.geneSize(); i++) {
			byte gene = indiv.getGene(i);
			Integer seen = timesSeen.get(gene);
			if (seen == null){
				seen = Integer.valueOf(1);
			} else {
				seen = seen +1;
			}
			timesSeen.put(gene, seen);
			if (Math.random() <= mutationRate) {
				int end = ThreadLocalRandom.current().nextInt(0, indiv.geneSize());
				indiv.setGene(i, indiv.getGene(end));
				indiv.setGene(end, gene);
			}
		}
		if (Math.random() <= mutationRate && false){
			List<Byte> multiple = timesSeen.keySet().stream().filter(e -> timesSeen.get(e) >1).collect(Collectors.toList());
			if (multiple.size() > 0){
				int delete = ThreadLocalRandom.current().nextInt(0, multiple.size());
				byte genDelete = multiple.get(delete);
				int num = ThreadLocalRandom.current().nextInt(0, timesSeen.get(genDelete));
				byte[] genes = new byte[indiv.geneSize()-1];
				int offSet = 0;
				for (int i = 0; i<genes.length; i++){
					byte gene = indiv.getGene(i);
					if (gene == genDelete){
						if (num > 0){
							num--;
							genes[i] = gene;
						}  else {
							offSet = -1;
						}
					} else {
						genes[i + offSet] = gene;
					}
				}
				indiv.setGenes(genes);
			}
		}
		if (Math.random() <= mutationRate && false){
			int add = ThreadLocalRandom.current().nextInt(0, indiv.geneSize());
			int posAdd = ThreadLocalRandom.current().nextInt(0, indiv.geneSize());
			byte[] genes = new byte[indiv.geneSize()+1];
			for (int i = 0; i<indiv.geneSize()+1; i++){
				if (i < posAdd){
					genes[i] = indiv.getGene(i);
				} else if (i == posAdd){
					genes[i] = indiv.getGene(add);
				} else {
					genes[i] = indiv.getGene(i-1);
				}
			}
			indiv.setGenes(genes);
		}
	}
}
