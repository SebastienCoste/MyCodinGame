package fr.sco.staticjo.codingame.graphics.example;

public class DrawLine {

	private DrawPoint source;
	private DrawPoint dest;
	
	public DrawLine(DrawPoint start, DrawPoint end){
		source = start;
		dest = end;
	}
	
	public DrawPoint getSource() {
		return source;
	}
	public void setSource(DrawPoint source) {
		this.source = source;
	}
	public DrawPoint getDest() {
		return dest;
	}
	public void setDest(DrawPoint dest) {
		this.dest = dest;
	}
	
}
