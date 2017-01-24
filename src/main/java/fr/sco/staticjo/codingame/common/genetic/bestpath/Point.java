package fr.sco.staticjo.codingame.common.genetic.bestpath;

public class Point {

	private int x;
	private int y;
	private int id;
	
	public Point(int id, int x, int y){
		this.id = id;
		this.x = x;
		this.y = y;
	}
	
	public double getDistanceTo(Point test){
		return Math.sqrt((x - test.x)*(x - test.x) + (y - test.y)*(y - test.y));
	}
	
	@Override
	public String toString() {
		return "P[x=" + x + ", y=" + y + ", id=" + id + "]";
	}
}
