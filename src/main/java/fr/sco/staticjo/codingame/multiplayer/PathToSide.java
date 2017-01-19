package fr.sco.test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
 *  Find the shortest path to the side of the map, with obstacles
 */
public class PathToSide{


	private static Map <Coord, List<Coord>> blockedLinks = new HashMap<>();
	static boolean[][] mapSeen = new boolean[9][9];
	
	public static void main(String[] args) {
		
		LeafInter[][] map = new Point[9][9];
		IntStream.range(0, 9).forEach(i -> IntStream.range(0, 9).forEach(j -> mapSeen[i][j] = false));
		LeafInter start = new Point(new Coord(0, 0));
		LeafInter end = new Point(new Coord(9, null));
		
		blockedLinks.put(((Point) start).c, Arrays.asList(new Coord(1, 0)));
		blockedLinks.put(new Coord(0, 1), Arrays.asList(new Coord(1, 1)));
		List<LeafInter> path = null;
		try {
			PathToSide dij = new PathToSide(map, start, end);
			path = dij.buildTheTreeForThePathToTheEnd();
		} catch (DijkstraException e) {
		}
		path.stream().forEach(e -> System.out.println(e));
	}
	
	
	public static class Coord{
		

		
		public Integer x;
		public Integer y;
		
		public Coord (Integer x, Integer y){
			this.x = x;
			this.y = y;
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
		
		public Point(Coord c){
			this.c = c;
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
				res.add(new Point(left));
			}
			if (c.x <map.length && (blocks== null || !blocks.contains(right))){
//				res.add(map[right.x][right.y]);
				res.add(new Point(right));
			}
			if (c.y >0 && (blocks== null || !blocks.contains(up))){
//				res.add(map[up.x][up.y]);
				res.add(new Point(up));
			}
			if (c.y<map[0].length && (blocks== null || !blocks.contains(down))){
//				res.add(map[down.x][down.y]);
				res.add(new Point(down));
			}
			return res;
		}

		@Override
		public boolean isTheEnd(LeafInter end) {
			return c.matches(((Point)end).c);
		}

		@Override
		public void seen(boolean hasBeenSeen) {
			mapSeen[c.x][c.y] = hasBeenSeen;
		}

		@Override
		public boolean canBeTreated() {
			return !mapSeen[c.x][c.y];
		}
		
	}
	
	public class DijkstraException extends Exception {}
	
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

	public PathToSide(LeafInter[][] map, LeafInter start, LeafInter end) throws DijkstraException{
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
