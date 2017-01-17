package fr.sco.staticjo.codingame.hard;

import java.util.*;
import java.io.*;
import java.math.*;

import java.util.stream.Collectors;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class TheLastCrusade2 {

	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		int W = in.nextInt(); // number of columns.
		int H = in.nextInt(); // number of rows.
		in.nextLine();
		Point[][] map = new Point[W][H];

		for (int i = 0; i < H; i++) {
			String LINE = in.nextLine(); // represents a line in the grid and contains W integers. Each integer represents one room of a given type.
			System.err.println(LINE);
			String[] alltype = LINE.split(" ");
			for (int j = 0; j < W; j++){
				Point point = getPointByType(alltype[j]);
				point.coord = new Coord(j, i);
				map[j][i] = point;
			}
		}
		int EX = in.nextInt(); // the coordinate along the X axis of the exit (not useful for this first mission, but must be read).
		Point end = new Flipped();
		end.coord = new Coord(EX, H-1);

		int XI = in.nextInt();
		int YI = in.nextInt();
		String POS = in.next();

		Point start = map[XI][YI];
		List<LeafInter> path = null;
		System.err.println("start: " + start.toString());
		System.err.println("end: " + end.toString());
		try{
			Dijkstra pathFinder = new Dijkstra(map, start, end);
			path = pathFinder.buildTheTreeForThePathToTheEnd();
		} catch (DijkstraException de){
			//Has not to happen (or codingame will fail)
		}
		System.err.println(path);
		int next = 1;
		Point leaf = (Point) path.get(0);
		printStatus(leaf);

		// game loop
		while (true) {
			XI = in.nextInt();
			YI = in.nextInt();
			POS = in.next();
			next++;
			if (next >= path.size()){
				printStatus(null);
			} else {

				leaf = (Point) path.get(next);
				Coord coordXY = new Coord(XI, YI);
				if (leaf.coord.getSquareDistanceTo(coordXY) == 0){
					printStatus(leaf);
				} else {
					leaf = null;
					for (int i = 0; i < path.size(); i++){
						Point testLeaf = (Point) path.get(i);
						if (testLeaf.coord.getSquareDistanceTo(coordXY) == 0){
							leaf = testLeaf;
							i = path.size();
						}
					}
					if (leaf == null){
						try{
							Dijkstra pathFinder = new Dijkstra(map, map[XI][YI], end);
							path = pathFinder.buildTheTreeForThePathToTheEnd();
						} catch (DijkstraException de){
							//Has not to happen (or codingame will fail)
						}
						next = 1;
						leaf = (Point) path.get(next);
						printStatus(leaf);
					}
				}

				//			Position p = Position.valueOf(POS);
				//			Point pt = map[XI][YI];
				//			Coord c = coordXY;
				//			c.updateWithExit(pt.getExitFrom(p));
				// Write an action using System.out.println()
				// To debug: System.err.println("Debug messages...");


				// One line containing the X Y coordinates of the room in which you believe Indy will be on the next turn.
				//			System.out.println(c.w + " " + c.h);
			}
		}
	}




	private static void printStatus(Point leaf) {
		if (leaf == null || leaf.flip == null){
			System.out.println("WAIT");
			return;
		} else {
			StringBuilder sb = new StringBuilder(leaf.coord.w + " " + leaf.coord.h + " ");
			switch (leaf.flip) {
			case COUNTERCLOCKWISE:
				sb.append("LEFT");
				break;
			case CLOCKWISE:
				sb.append("RIGHT");
				break;
			default:
				System.out.println("WAIT");
				return;
			}
			System.out.println(sb.toString());
		}
	}

	public static Point getPointByType(String type){

		boolean locked = false;
		if (Integer.valueOf(type) <0){
			locked = true;
			type = type.substring(1);
		}
		Point res = getPointWithoutLock(type);
		if (res != null){
			return res.withLock(locked);
		}
		return res;
	}




	private static Point getPointWithoutLock(String type) {
		switch (type) {
		case "0":
			return new Type0();
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

		@Override
		public String toString() {
			return "Coord [w=" + w + ", h=" + h + "]";
		}


		public int w;
		public int h;


		public Coord (int ww, int hh){
			w = ww;
			h = hh;
		}

		public long getSquareDistanceTo(Coord test){
			return (w-test.w)*(w-test.w) + (h-test.h)*(h-test.h);
		}

		Position getPositionOfNeighbour(Coord test){
			if (w == test.w){
				if (h > test.h){
					return Position.TOP;
				} else {
					return Position.BOTTOM;
				}
			} else if (w > test.w){
				return Position.LEFT;
			} else {
				return Position.RIGHT;
			}

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

	public static abstract class Point implements LeafInter{

		@Override
		public String toString() {
			return "Point [entranceExit=" + entranceExit + ", coord=" + coord + ", locked=" + locked + ", flip=" + flip
					+ "]";
		}

		Map<Position, Position> entranceExit = new HashMap<>();

		public Coord coord;
		public boolean locked;
		public Switch flip;


		Point withCoords(int w, int h){
			coord = new Coord(w, h);
			return this;
		}
		Point withLock(boolean l){
			locked = l;
			return this;
		}
		Position getExitFrom(Position entrance){
			return entranceExit.get(entrance);
		}

		@Override
		public long getWeightToTheNextStep() {
			return 1;
		}

		@Override
		public long getEstimatedWeightToTheEnd(LeafInter end) {
			Point p = (Point) end;
			return p.coord.getSquareDistanceTo(this.coord);
		}

		@Override
		public boolean isTheEnd(LeafInter end) {
			Point p = (Point) end;
			return p.coord.getSquareDistanceTo(coord) == 0;
		}


		@Override
		public List<LeafInter> getAllMatchingNeighbours(LeafInter[][] map){
			List<LeafInter> res = new ArrayList<>();
			if (this instanceof Type0){
				System.err.println("CASE PLEINE");
				return res;
			}
			for (Position exit : entranceExit.keySet()) {
				Coord coordneighbour = getExitCoordFrom(exit);
				System.err.println("voisin " + exit + " de " + coord.toString() + " vers " + coordneighbour);
				if (coordneighbour != null){

					Point neighbour = (Point)map[coordneighbour.w][coordneighbour.h];
					System.err.println("voisin trouve " + neighbour);
					if (this.matchesWith(neighbour)){
						res.add(neighbour);
					}
					if (!neighbour.locked){
						Point neighbourClock = neighbour.getFlippedPoint(Switch.CLOCKWISE);
						System.err.println("voisin CLOCKWISE " + neighbourClock);
						if (this.matchesWith(neighbourClock)){
							res.add(neighbourClock);
						}
						Point neighbourCounterC = neighbour.getFlippedPoint(Switch.COUNTERCLOCKWISE);
						System.err.println("voisin COUNTERCLOCKWISE " + neighbourCounterC);
						if (this.matchesWith(neighbourCounterC)){
							res.add(neighbourCounterC);
						}
					}
				}
			}

			return res;
		}

		public void flipTo(Switch f){
			flip = f;
		}

		Coord getExitCoordFrom(Position entrance){
			if (this instanceof Type0){
				return null;
			}
			Position exit = entranceExit.get(entrance);
			if (exit == null){
				return null;
			}
			switch (exit) {
			case TOP: return new Coord(coord.w, coord.h-1);
			case BOTTOM: return new Coord(coord.w, coord.h+1);
			case LEFT: return new Coord(coord.w-1, coord.h);
			case RIGHT: return new Coord(coord.w+1, coord.h);
			default: return null;
			}
		}

		Point addPath(Position entrance, Position exit){
			entranceExit.put(entrance, exit);
			return this;
		}

		public Point getFlippedPoint(Switch flip){
			Flipped res = new Flipped();
			Map<Position, Position> newEntranceExit = new HashMap<>();

			for (Position entrance : entranceExit.keySet()) {
				Position newEntrance = entrance.flip(flip);
				Position newExit = entranceExit.get(entrance).flip(flip);
				if (!newExit.isStrictlyUpperThan(newEntrance)){
					newEntranceExit.put(newEntrance, newExit);
				}
			}
			res.entranceExit = newEntranceExit;
			res.flip = flip;
			res.locked = locked;
			res.coord = coord;

			return res;
		}

		public boolean matchesWith(Point test){

			Position pos = this.coord.getPositionOfNeighbour(test.coord);
			System.err.println("neighbour at " + pos);
			switch (pos) {
			case TOP: return false;
			case BOTTOM: return test.entranceExit.get(Position.TOP) != null && entranceExit.containsValue(Position.BOTTOM);
			case LEFT: return test.entranceExit.get(Position.RIGHT) != null && entranceExit.containsValue(Position.LEFT);
			case RIGHT: return test.entranceExit.get(Position.LEFT) != null && entranceExit.containsValue(Position.RIGHT);
			default: return false;
			}
		}

	}
	public static class Type0 extends Point{
		public Type0(){
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
	public static class Flipped extends Point{
		public Flipped(){
		}
	}
	public enum Switch{
		CLOCKWISE,
		COUNTERCLOCKWISE;
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

		public boolean isStrictlyUpperThan(Position test){

			switch (this) {
			case TOP: return true;
			case BOTTOM: return false;
			case LEFT: return test != TOP;
			case RIGHT: return test != TOP;
			default: return false;
			}
		}

		Position flip(Switch flip){
			if (flip == Switch.CLOCKWISE){
				switch (this) {
				case TOP: return RIGHT;
				case BOTTOM: return LEFT;
				case LEFT: return TOP;
				case RIGHT: return BOTTOM;
				default: return null;
				}
			} else if (flip == Switch.COUNTERCLOCKWISE){
				switch (this) {
				case TOP: return LEFT;
				case BOTTOM: return RIGHT;
				case LEFT: return BOTTOM;
				case RIGHT: return TOP;
				default: return null;
				}
			}
			return this;
		}
	}

	public static class DijkstraException extends Exception {}

	public interface LeafInter {

		long getWeightToTheNextStep();
		long getEstimatedWeightToTheEnd(LeafInter end);
		List<LeafInter>getAllMatchingNeighbours(LeafInter[][] map);
		boolean isTheEnd(LeafInter end);
	}

	public static class Tree<T extends LeafInter> {
		private Node<T> root;

		public Tree(T rootData, T end) {
			root = new Node<T>(rootData, end);
			root.data = rootData;
			root.children = new ArrayList<Node<T>>();
		}

	}
	public static class Node<T extends LeafInter> implements Comparable {
		public long currentWeight;
		public long estimatedWeightLeft;
		private T data;
		private Node<T> parent;
		private List<Node<T>> children;

		public Node(T data, T end){
			this.data = data;
			currentWeight = 0;
			estimatedWeightLeft = data.getEstimatedWeightToTheEnd(end);
		}

		public Node(Node<T> parent, long weightFromparent, T data, T end){
			this.data = data;
			this.parent = parent;
			currentWeight = parent.currentWeight + parent.data.getWeightToTheNextStep();
			estimatedWeightLeft = data.getEstimatedWeightToTheEnd(end);
		}

		@Override
		public int compareTo(Object o) {
			Node<T> test = (Node<T>) o;
			return Long.compare(currentWeight + estimatedWeightLeft, test.currentWeight + test.estimatedWeightLeft);
		}

	}

	public static class Dijkstra{
		public LeafInter[][] map;
		public Tree<LeafInter> tree;
		public LeafInter end;
		public List<Node<LeafInter>> leavesToAnalyse;

		public Dijkstra(LeafInter[][] map, LeafInter start, LeafInter end) throws DijkstraException{
			if (!end.isTheEnd(end)){
				throw new DijkstraException();
			}
			tree = new Tree<LeafInter>(start, end);
			this.map = map;
			this.end = end;
			leavesToAnalyse = new ArrayList<>();
			leavesToAnalyse.add(tree.root);
		}

		public List<LeafInter> buildTheTreeForThePathToTheEnd(){

			List<LeafInter> path = new ArrayList<>();
			Node<LeafInter> bestLeaf = tree.root;

			while (! bestLeaf.data.isTheEnd(end)){
				leavesToAnalyse.remove(0);
				System.err.println("bestLeaf: " + bestLeaf.data.toString());
				insertInLeavesToAnalyse(getChildrenAsLeaves(bestLeaf));
				System.err.println("#children: " + leavesToAnalyse.size());
				bestLeaf = leavesToAnalyse.get(0);
			}
			while (bestLeaf.parent != null){
				path.add(bestLeaf.data);
				bestLeaf = bestLeaf.parent;
			}
			Collections.reverse(path);
			return path;
		}

		private List<Node<LeafInter>> getChildrenAsLeaves(Node<LeafInter> bestLeaf) {
			return bestLeaf.data.getAllMatchingNeighbours(map)
					.stream()
					.map(e -> new Node<LeafInter>(bestLeaf, bestLeaf.currentWeight, e, end))
					.collect(Collectors.toList());
		}

		private void insertInLeavesToAnalyse(List<Node<LeafInter>> nodes){
			leavesToAnalyse.addAll(nodes);
			Collections.sort(leavesToAnalyse);
		}
	}
}
