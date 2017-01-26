package fr.sco.staticjo.codingame.common.genetic;

public class GeneticAlgo<P extends Person> {


	protected static final double uniformRate = 0.5;
	protected static final double mutationRate = 0.015;
	protected static final int tournamentSize = 5;
	protected static final boolean elitism = true;
	private Class<P> classPerson;
	
	
	public GeneticAlgo(){
	}
	
	public GeneticAlgo(Class<P> clazz){
		setClassPerson(clazz);
	}
	
	public Population<P> evolvePopulation(Population<P> pop) throws InstantiationException, IllegalAccessException, ClassNotFoundException  {
		Population<P> newPopulation = new Population<P>(pop.size(), false, getClassPerson());

		if (elitism) {
			newPopulation.savePerson(0, pop.getFittest());
		}

		for (int i = (elitism?1:0); i < pop.size(); i++) {
			Person indiv1 = tournamentSelection(pop);
			Person indiv2 = tournamentSelection(pop);
			Person newIndiv = crossover(pop, indiv1, indiv2);
			newPopulation.savePerson(i, newIndiv);
		}

		for (int i = (elitism?1:0); i < newPopulation.size(); i++) {
			mutate(newPopulation.getPerson(i));
		}

		return newPopulation;
	}

	protected Person crossover(Population<P> pop, Person indiv1, Person indiv2) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Person newSol = pop.instanciatePerson(getClassPerson());
		// Loop through genes
		for (int i = 0; i < indiv1.geneSize(); i++) {
			// Crossover
			if (Math.random() <= uniformRate) {
				newSol.setGene(i, indiv1.getGene(i));
			} else {
				newSol.setGene(i, indiv2.getGene(i));
			}
		}
		return newSol;
	}
	
	protected void mutate(Person indiv) {
	        // Loop through genes
	        for (int i = 0; i < indiv.geneSize(); i++) {
	            if (Math.random() <= mutationRate) {
	                // Create random gene
	                Long gene = (Long) Math.round(Math.random());
	                indiv.setGene(i, gene);
	            }
	        }
	    }
	 
	protected Person tournamentSelection(Population<P> pop) {
	        // Create a tournament population
	        Population<P> tournament = new Population<P>(tournamentSize, false, getClassPerson());
	        // For each place in the tournament get a random individual
	        for (int i = 0; i < tournamentSize; i++) {
	            int randomId = (int) (Math.random() * pop.size());
	            tournament.savePerson(i, pop.getPerson(randomId));
	        }
	        // Get the fittest
	        Person fittest = tournament.getFittest();
	        return fittest;
	    }



	 public Class<P> getClassPerson(){
			return this.classPerson;
		}
	 public void setClassPerson(Class<P> clazz){
			this.classPerson = clazz;
		}



	

	

	

}

