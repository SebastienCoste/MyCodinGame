package fr.sco.staticjo.codingame.common.genetic;

public interface Person{

	void generatePerson();

	public byte getGene(int index);

	public void setGene(int index, byte value);

	public int getFitness();
	
	 public int geneSize();

}
