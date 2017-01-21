package fr.sco.staticjo.codingame.medium;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;



public class Teads {

	static List<Point> extreme;
	static int total;

	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		total = in.nextInt(); // the number of adjacency relations
		Point[][] link = new Point[2][total];
		Map<Point, Integer> encounterMap = new HashMap<>();
		for (int i = 0; i < total; i++) {
			Point xi =  new Point(in.nextInt()); // the ID of a person which is adjacent to yi
			Point yi = new Point(in.nextInt()); // the ID of a person which is adjacent to xi
			link[0][i] = xi;
			link[1][i] = yi;
			incEncoutnerNumber(encounterMap, xi);
			incEncoutnerNumber(encounterMap, yi);
		}
		extreme = encounterMap.keySet().stream().filter(k -> encounterMap.get(k) == 1).collect(Collectors.toList());

		
		int longestShortPath = IntStream.range(0, extreme.size())
			.map(start -> 
						IntStream.range(start, extreme.size())
						.map(end -> 
								new Dijkstra(link, extreme.get(start), extreme.get(end)).buildTheTreeForThePathToTheEnd().size()
							)
						.reduce((x,y) -> x > y ? x : y)
						.getAsInt()
					)
			.reduce((x,y) -> x > y ? x : y)
			.getAsInt();
		


		System.out.println(longestShortPath%2 == 0 ? longestShortPath/2 : longestShortPath/2 +1);
	}


	private static void incEncoutnerNumber(Map<Point, Integer> encounterMap, Point number){

		Integer total = encounterMap.get(number);
		if (total == null){
			total = new Integer(1);
			encounterMap.put(number, total);
		} else {
			total++;
		}

	}



	public static class Point implements LeafInter{

		int number;

		public Point (int number){
			this.number = number;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + number;
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
			Point other = (Point) obj;
			if (number != other.number)
				return false;
			return true;
		}

		@Override
		public long getWeightToTheNextStep() {
			return 1;
		}

		@Override
		public long getEstimatedWeightToTheEnd(LeafInter end) {
			return 0;
		}

		@Override
		public List<LeafInter> getAllMatchingNeighbours(LeafInter[][] map) {
			return IntStream.range(0, total)
			.mapToObj(i -> getPoint(map, i))
			.filter(p -> p != null)
			.collect(Collectors.toList());
		}
		private LeafInter getPoint(LeafInter[][] map, int i) {
			if (this.equals(map[1][i])){
				return map[2][i];
			}else if (this.equals(map[2][i])){
				return map[1][i];
			} else {
				return null;
			}
		}

		@Override
		public boolean isTheEnd(LeafInter end) {
			return this.equals(end);
		}

		@Override
		public void seen(boolean hasBeenSeen) {
			//Useless because no cycling 
		}

		@Override
		public boolean canBeTreated() {
			return true;
		}


	}













	//Below my generic script

	public class DijkstraException extends Exception {}

	public interface LeafInter {

		long getWeightToTheNextStep();
		long getEstimatedWeightToTheEnd(LeafInter end);
		List<LeafInter>getAllMatchingNeighbours(LeafInter[][] map);
		boolean isTheEnd(LeafInter end);
		void seen(boolean hasBeenSeen);
		boolean canBeTreated(); //For example if you don't want to treat an alrealy seen leaf
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

		public Dijkstra(LeafInter[][] map, LeafInter start, LeafInter end) {
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
				while(it.hasNext() && it.next().data.canBeTreated()){
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
						.map(e -> new Node<LeafInter>(bestLeaf, bestLeaf.currentWeight, e, end))
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
}
