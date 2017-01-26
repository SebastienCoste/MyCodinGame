package fr.sco.staticjo.codingame.common.genetic.bestpath;

import fr.sco.staticjo.codingame.graphics.DisplayPoint;

public class Point implements DisplayPoint{

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

	@Override
	public int getX() {
		return x;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}
}
