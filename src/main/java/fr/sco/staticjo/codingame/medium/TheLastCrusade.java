package fr.sco.staticjo.codingame.medium;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

	/**
	 * 
	 * @author Static
	 *
	 * My answer to https://www.codingame.com/training/medium/the-last-crusade-episode-1
	 */
public class TheLastCrusade {

	public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int W = in.nextInt(); // number of columns.
        int H = in.nextInt(); // number of rows.
        in.nextLine();
        Point[][] map = new Point[W][H];
        
        for (int i = 0; i < H; i++) {
            String LINE = in.nextLine(); // represents a line in the grid and contains W integers. Each integer represents one room of a given type.
            String[] alltype = LINE.split(" ");
            for (int j = 0; j < W; j++){
            	map[j][i] = getPointByType(alltype[j]);
            }
        }
        int EX = in.nextInt(); // the coordinate along the X axis of the exit (not useful for this first mission, but must be read).

        // game loop
        while (true) {
            int XI = in.nextInt();
            int YI = in.nextInt();
            String POS = in.next();
            Position p = Position.valueOf(POS);
            Point pt = map[XI][YI];
            Coord c = new Coord(XI, YI);
            c.updateWithExit(pt.getExitFrom(p));
            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");


            // One line containing the X Y coordinates of the room in which you believe Indy will be on the next turn.
            System.out.println(c.w + " " + c.h);
        }
    }
	
	
	
	
	public static Point getPointByType(String type){
		
		switch (type) {
		case "1":
			return new Type1();
		case "2":
			return new Type2();
		case "3":
			return new Type3();
		case "4":
			return new Type4();
		case "5":
			return new Type5();
		case "6":
			return new Type6();
		case "7":
			return new Type7();
		case "8":
			return new Type8();
		case "9":
			return new Type9();
		case "10":
			return new Type10();
		case "11":
			return new Type11();
		case "12":
			return new Type12();
		case "13":
			return new Type13();

		default:
			return null;
		}
	}
	
	
	public static class Coord{
		
		public int w;
		public int h;
		public Coord (int ww, int hh){
			w = ww;
			h = hh;
		}
		public void updateWithExit(Position exit){
			switch (exit) {
			case TOP:
				//Never 
				h--;
				break;
			case BOTTOM:
				h++;
				break;
			case LEFT:
				w--;
				break;
			case RIGHT:
				w++;
				break;

			default:
				break;
			}
		}
	}
	
	public static abstract class Point{
		
		Map<Position, Position> entranceExit = new HashMap<>();
		
		public Coord coord;
		
		Point withCoords(int w, int h){
			coord = new Coord(w, h);
			return this;
		}
		Position getExitFrom(Position entrance){
			return entranceExit.get(entrance);
		}
		
		Point addPath(Position entrance, Position exit){
			entranceExit.put(entrance, exit);
			return this;
		}
		
	}
	public static class Type1 extends Point{
		public Type1(){
			this.addPath(Position.TOP, Position.BOTTOM)
			.addPath(Position.LEFT, Position.BOTTOM)
			.addPath(Position.RIGHT, Position.BOTTOM);
		}
	}
	public static class Type2 extends Point{
		public Type2(){
			this.addPath(Position.LEFT, Position.RIGHT)
			.addPath(Position.RIGHT, Position.LEFT);
		}
	}
	public static class Type3 extends Point{
		public Type3(){
			this.addPath(Position.TOP, Position.BOTTOM);
		}
	}
	public static class Type4 extends Point{
		public Type4(){
			this.addPath(Position.TOP, Position.LEFT)
			.addPath(Position.RIGHT, Position.BOTTOM);
		}
	}
	public static class Type5 extends Point{
		public Type5(){
			this.addPath(Position.TOP, Position.RIGHT)
			.addPath(Position.LEFT, Position.BOTTOM);
		}
	}
	public static class Type6 extends Point{
		public Type6(){
			this.addPath(Position.LEFT, Position.RIGHT)
			.addPath(Position.RIGHT, Position.LEFT);
		}
	}
	public static class Type7 extends Point{
		public Type7(){
			this.addPath(Position.TOP, Position.BOTTOM)
			.addPath(Position.RIGHT, Position.BOTTOM);
		}
	}
	public static class Type8 extends Point{
		public Type8(){
			this.addPath(Position.LEFT, Position.BOTTOM)
			.addPath(Position.RIGHT, Position.BOTTOM);
		}
	}
	public static class Type9 extends Point{
		public Type9(){
			this.addPath(Position.TOP, Position.BOTTOM)
			.addPath(Position.LEFT, Position.BOTTOM);
		}
	}
	public static class Type10 extends Point{
		public Type10(){
			this.addPath(Position.TOP, Position.LEFT);
		}
	}
	public static class Type11 extends Point{
		public Type11(){
			this.addPath(Position.TOP, Position.RIGHT);
		}
	}
	public static class Type12 extends Point{
		public Type12(){
			this.addPath(Position.RIGHT, Position.BOTTOM);
		}
	}
	public static class Type13 extends Point{
		public Type13(){
			this.addPath(Position.LEFT, Position.BOTTOM);
		}
	}
	public enum Position{
		TOP,
		BOTTOM,
		LEFT,
		RIGHT;
		
		Position invert(){
			switch (this) {
			case TOP: return BOTTOM;
			case BOTTOM: return TOP;
			case LEFT: return RIGHT;
			case RIGHT: return LEFT;
			default: return null;
			}
		}
	}
	
	
}
