package fr.sco.staticjo.codingame.multiplayer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import fr.sco.test.PathToSide.Tree.Node;


/**
 *  Work In Progress 
 *  
 * @author Static
 *
 * Common class used to find the lightest path in a tree 
 */
public class PathToSide{


	private static Map <Coord, List<Coord>> blockedLinks = new HashMap<>();
	static Map<Integer, boolean[][]> mapSeen = new HashMap<>();

	public static void main(String[] args) {

		LeafInter[][] map = new Point[9][9];
		int nbrPlayer = 3;
		int myNumber = 0;
		int myRemainingWalls = 1;

		long init = new Date().getTime();
		IntStream.range(0, nbrPlayer).forEach(p -> mapSeen.put(p, new boolean[9][9]));

		Map<Integer, List<LeafInter>> pathPerPlayer = new HashMap<>();


		blockedLinks.put(new Coord(0, 0), Arrays.asList(new Coord(1, 0)));
		blockedLinks.put(new Coord(0, 1), Arrays.asList(new Coord(1, 1)));
		blockedLinks.put(new Coord(1, 2), Arrays.asList(new Coord(2, 2)));
		blockedLinks.put(new Coord(1, 3), Arrays.asList(new Coord(2, 3)));
		blockedLinks.put(new Coord(2, 5), Arrays.asList(new Coord(3, 5)));
		blockedLinks.put(new Coord(2, 6), Arrays.asList(new Coord(3, 6)));
		blockedLinks.put(new Coord(3, 4), Arrays.asList(new Coord(4, 4)));
		blockedLinks.put(new Coord(3, 5), Arrays.asList(new Coord(4, 5)));

		//		List<Coord> allStart = new ArrayList<>(nbrPlayer);
		//		List<Coord> allEnd = new ArrayList<>(nbrPlayer);

		//Test
		List<Coord> allStart = Arrays.asList(new Coord(0, 0), new Coord(0, 1), new Coord(0, 2));
		List<Coord> allEnd = Arrays.asList(new Coord(8, null), new Coord(8, null), new Coord(8, 8));
		//Fin test

		IntStream.range(0, nbrPlayer)
		.forEach(p -> pathPerPlayer.put(p,
				new PathToSide(map, new Point(allStart.get(p), p), new Point(allEnd.get(p), p))
				.buildTheTreeForThePathToTheEnd()));



		pathPerPlayer.keySet()
		.forEach(k -> {
			System.err.println("player: " + k + " size " + pathPerPlayer.get(k).size());
			pathPerPlayer.get(k)
			.stream()
			.forEach(e -> System.err.println(e));
		}
				);

		int fastest = myNumber;
		
		if (myRemainingWalls >0){
			Comparator<Integer> compOnPath = getPlayerComparator(pathPerPlayer);
			List<Integer> orderedPlayers = IntStream.range(0, nbrPlayer).boxed().collect(Collectors.toList());
			orderedPlayers.sort(compOnPath);
			System.err.println(orderedPlayers.toString());
			fastest = orderedPlayers.get(0);
		}
		if (fastest == myNumber){
			System.out.println(allStart.get(myNumber).getDirectionTo(((Point)pathPerPlayer.get(myNumber).get(0)).c));
		} else {
			List<LeafInter> fastpath = pathPerPlayer.get(fastest);
			if (fastpath.size() <2){
				System.out.println(allStart.get(myNumber).getDirectionTo(((Point)pathPerPlayer.get(myNumber).get(0)).c));
			} else {
				Coord source = ((Point)pathPerPlayer.get(fastest).get(1)).c;
				Direction dir = source.getDirectionTo(((Point)pathPerPlayer.get(fastest).get(2)).c);
				Coord wall = getCoordWall(source, dir, allEnd.get(fastest));
				System.out.println(wall.x + " " + wall.y + " " + (dir == Direction.UP || dir == Direction.DOWN ? "H" : "V"));
			}
		}
		System.err.println("TOTAL: " + (new Date().getTime() - init));
	}

	private static Coord getCoordWall(Coord source, Direction dir, Coord end) {
		int x = source.x;
		int y = source.y;
		switch (dir) {
		case UP:
			y--;
			if (x == 0 || (end.x != null && end.x < x)){
				x--;
			}
			return new Coord(x, y);
		case DOWN:
			y++;
			if (x == 0 || (end.x != null && end.x < x)){
				x--;
			}
			return new Coord(x, y);
		case LEFT:
			x--;
			if (y == 0 || (end.y != null && end.y > y)){
				y++;
			}
			return new Coord(x, y);
		case RIGHT:
			x++;
			if (y == 0 || (end.y != null && end.y > y)){
				y++;
			}
			return new Coord(x, y);
		default:
			return null;
		}
	}

	private static Comparator<Integer> getPlayerComparator(Map<Integer, List<LeafInter>> pathPerPlayer) {
		Comparator<Integer> compOnPath = new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return Integer.valueOf(pathPerPlayer.get(o1).size()).compareTo(pathPerPlayer.get(o2).size());
			}
		};
		return compOnPath;
	}


	public static enum Direction {
		UP,
		DOWN,
		LEFT,
		RIGHT;
	}

	public static class Coord{



		public Integer x;
		public Integer y;

		public Coord (Integer x, Integer y){
			this.x = x;
			this.y = y;
		}

		public Direction getDirectionTo(Coord dest){

			if (x == dest.x){
				if (y > dest.y){
					return Direction.UP;
				} else {
					return Direction.DOWN;
				}
			} else if (x < dest.x){
				return Direction.RIGHT;
			} else {
				return Direction.LEFT;
			}
		}

		public boolean matches (Coord test){
			if (test == null){
				return false;
			}
			if (this.equals(test)){
				return true;
			}
			return (test.x == null && y == test.y)
					|| (test.y == null && x == test.x);
		}

		@Override
		public String toString() {
			return "Coord [x=" + x + ", y=" + y + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Coord other = (Coord) obj;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}
	}


	public static class Point implements LeafInter{

		public Coord c;
		public Integer user;

		public Point(Coord c, Integer user){
			this.c = c;
			this.user = user;
		}
		@Override
		public long getWeightToTheNextStep() {
			return 1;
		}

		@Override
		public long getEstimatedWeightToTheEnd(LeafInter end) {
			Point e = (Point) end;
			Integer endX = e.c.x;
			if (endX == null){
				endX = c.x;
			}
			Integer endY = e.c.y;
			if (endY == null){
				endY = c.y;
			}
			return Double.valueOf(Math.sqrt((c.x-endX)*(c.x-endX) + (c.y-endY)*(c.y-endY))).longValue();
		}

		@Override
		public String toString() {
			return "Point [c=" + c + "]";
		}

		@Override
		public List<LeafInter> getAllMatchingNeighbours(LeafInter[][] map) {

			List<LeafInter> res = new ArrayList<>();
			List<Coord> blocks = blockedLinks.get(c);
			Coord left = new Coord(c.x-1, c.y);
			Coord right = new Coord(c.x+1, c.y);
			Coord up = new Coord(c.x, c.y-1);
			Coord down = new Coord(c.x, c.y+1);
			if (c.x >0 && (blocks== null || !blocks.contains(left))){
				//				res.add(map[left.x][left.y]);
				res.add(new Point(left, user));
			}
			if (c.x <map.length && (blocks== null || !blocks.contains(right))){
				//				res.add(map[right.x][right.y]);
				res.add(new Point(right, user));
			}
			if (c.y >0 && (blocks== null || !blocks.contains(up))){
				//				res.add(map[up.x][up.y]);
				res.add(new Point(up, user));
			}
			if (c.y<map[0].length && (blocks== null || !blocks.contains(down))){
				//				res.add(map[down.x][down.y]);
				res.add(new Point(down, user));
			}
			return res;
		}

		@Override
		public boolean isTheEnd(LeafInter end) {
			return c.matches(((Point)end).c);
		}

		@Override
		public void seen(boolean hasBeenSeen) {
			mapSeen.get(user)[c.x][c.y] = hasBeenSeen;
		}

		@Override
		public boolean canBeTreated() {
			return !mapSeen.get(user)[c.x][c.y];
		}

	}


	public interface LeafInter {

		long getWeightToTheNextStep();
		long getEstimatedWeightToTheEnd(LeafInter end);
		List<LeafInter>getAllMatchingNeighbours(LeafInter[][] map);
		boolean isTheEnd(LeafInter end);
		void seen(boolean hasBeenSeen);
		boolean canBeTreated(); //For example if you don't want to treat an already seen leaf
	}

	protected static class Tree<T extends LeafInter> {
		private Node<T> root;

		public Tree(T rootData, T end) {
			root = new Node<T>(rootData, end);
			root.children = new ArrayList<Node<T>>();
		}

		protected static class Node<T extends LeafInter> implements Comparable {
			@Override
			public String toString() {
				return "Node [data=" + data + "]";
			}

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

			public Node(Node<T> parent, T data, T end){
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
	}

	public LeafInter[][] map;
	public Tree<LeafInter> tree;
	public LeafInter end;
	public List<Node<LeafInter>> leavesToAnalyse;

	public PathToSide(LeafInter[][] map, LeafInter start, LeafInter end) {
		if (!end.isTheEnd(end)){
			return ;
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

		while (!bestLeaf.data.isTheEnd(end)){
			Iterator<Node<LeafInter>> it = leavesToAnalyse.iterator();
			while(it.hasNext() && !it.next().data.canBeTreated()){
				it.remove();
			}
			if (leavesToAnalyse.size() == 0){
				bestLeaf = cutTheTree(bestLeaf);
			} else {
				bestLeaf = leavesToAnalyse.get(0);
			}
			if(!bestLeaf.data.isTheEnd(end)){
				leavesToAnalyse.remove(0);
				bestLeaf.data.seen(true);
				List<Node<LeafInter>> childrenAsLeaves = getChildrenAsLeaves(bestLeaf);
				if (childrenAsLeaves != null && childrenAsLeaves.size() > 0){
					insertInLeavesToAnalyse(childrenAsLeaves);
					bestLeaf = leavesToAnalyse.get(0);
				} else {
					bestLeaf = cutTheTree(bestLeaf);
				}
			}
		}

		while (bestLeaf.parent != null){
			path.add(bestLeaf.data);
			bestLeaf = bestLeaf.parent;
		}
		Collections.reverse(path);
		return path;
	}

	private Node<LeafInter> cutTheTree(Node<LeafInter> bestLeaf){
		if (bestLeaf.parent == null){
			return null;
		}
		while (bestLeaf.parent.children.size() == 1){
			bestLeaf = bestLeaf.parent;
		}
		bestLeaf.parent.children.remove(bestLeaf);
		bestLeaf = bestLeaf.parent;
		bestLeaf.data.seen(false);
		insertInLeavesToAnalyse(bestLeaf);
		return leavesToAnalyse.get(0);
	}

	private List<Node<LeafInter>> getChildrenAsLeaves(Node<LeafInter> bestLeaf) {
		if (bestLeaf.children == null || bestLeaf.children.size() == 0){
			bestLeaf.children = bestLeaf.data.getAllMatchingNeighbours(map)
					.stream()
					.map(e -> new Node<LeafInter>(bestLeaf, e, end))
					.collect(Collectors.toList());
		}
		return bestLeaf.children;
	}

	private void insertInLeavesToAnalyse(List<Node<LeafInter>> nodes){
		leavesToAnalyse.addAll(nodes);
		Collections.sort(leavesToAnalyse);
	}
	private void insertInLeavesToAnalyse(Node<LeafInter> node){
		leavesToAnalyse.add(node);
		Collections.sort(leavesToAnalyse);
	}

}
