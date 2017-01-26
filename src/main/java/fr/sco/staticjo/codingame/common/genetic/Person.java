package fr.sco.staticjo.codingame.common.genetic;

public interface Person{

	void generatePerson();

	public Long getGene(int index);

	public void setGene(int index, Long value);

	public int getFitness();
	
	 public int geneSize();

}
