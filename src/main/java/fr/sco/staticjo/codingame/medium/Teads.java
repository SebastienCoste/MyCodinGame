package fr.sco.staticjo.codingame.medium;

import java.util.*;
import java.io.*;
import java.math.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


	/**
	 * 
	 * @author Static
	 *
	 * My solution to https://www.codingame.com/ide/6703989cde3227741b2916e7a203964375ed289
	 */
class Teads {

	static List<Point> extreme;
	static int total;

	public static void main(String args[]) {

		//DataTest
		total = 21;
		List<Integer> alLX = Arrays.asList(0,0,0,1,1,2,2,5,5,8,8,9,9,12,12,15,15,16,16,19,19);
		List<Integer> alLY = Arrays.asList(1,8,15,2,5,3,4,6,7,9,12,10,11,13,14,16,19,17,18,20,21);
		StringBuilder inx = new StringBuilder();
		StringBuilder iny = new StringBuilder();
		//End test

		Scanner in = new Scanner(System.in);
		//				total = in.nextInt(); // the number of adjacency relations
		System.err.println(total);
		Point[][] link = new Point[2][total];
		Map<Point, Integer> encounterMap = new HashMap<>();

		
		for (int i = 0; i < total; i++) {
			//						Point xi =  new Point(in.nextInt()); // the ID of a person which is adjacent to yi
			//						Point yi = new Point(in.nextInt()); // the ID of a person which is adjacent to xi
			Point xi =  new Point(alLX.get(i)); // the ID of a person which is adjacent to yi
			Point yi = new Point(alLY.get(i)); // the ID of a person which is adjacent to xi
			inx.append(xi.number).append(",");
			inx.append(yi.number).append(",");
			link[0][i] = xi;
			link[1][i] = yi;
			incEncoutnerNumber(encounterMap, xi);
			incEncoutnerNumber(encounterMap, yi);
		}
		System.err.println(inx);
		System.err.println(iny);
		
		extreme = encounterMap.keySet().stream().filter(k -> encounterMap.get(k) == 1).collect(Collectors.toList());



		int longestShortPath = IntStream.range(0, extreme.size())
				.mapToObj(start -> 
				IntStream.range(start+1, extreme.size())
				.map(end -> 
				new Dijkstra(link, extreme.get(start), extreme.get(end)).buildTheTreeForThePathToTheEnd().size()
						)
				.max()
						)
				.filter(op -> op.isPresent())
				.mapToInt(o -> o.getAsInt())
				.max().getAsInt() -1;


		System.out.println(longestShortPath%2 == 0 ? longestShortPath/2 : longestShortPath/2 +1);
	}


	private static void incEncoutnerNumber(Map<Point, Integer> encounterMap, Point number){

		Integer total = encounterMap.get(number);
		if (total == null){
			total = new Integer(0);
		}
		total++;
		encounterMap.put(number, total);

	}



	public static class Point implements LeafInter{

		@Override
		public String toString() {
			return "P[" + number + "]";
		}

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
				return map[0][i];
			}else if (this.equals(map[0][i])){
				return map[1][i];
			} else {
				return null;
			}
		}

		@Override
		public boolean isTheEnd(LeafInter end) {
			return this.equals(end);
		}
	}



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
		public Set<LeafInter> leafSeen;

		public Dijkstra(LeafInter[][] map, LeafInter start, LeafInter end) {
			tree = new Tree<LeafInter>(start, end);
			this.map = map;
			this.end = end;
			leavesToAnalyse = new ArrayList<>();
			leavesToAnalyse.add(tree.root);
			leafSeen = new HashSet<>();
		}

		public List<LeafInter> buildTheTreeForThePathToTheEnd(){

			List<LeafInter> path = new ArrayList<>();
			Node<LeafInter> bestLeaf = tree.root;

			while (!bestLeaf.data.isTheEnd(end)){
				keepUnseenOnly(leavesToAnalyse);
				if (leavesToAnalyse.size() == 0){
//					bestLeaf = cutTheTree(bestLeaf); //Can't find a path
					return path;
				} else {
					bestLeaf = leavesToAnalyse.get(0);
				}
				if(!bestLeaf.data.isTheEnd(end)){
					leavesToAnalyse.remove(0);
					leafSeen(bestLeaf.data, true);
					List<Node<LeafInter>> childrenAsLeaves = getChildrenAsLeaves(bestLeaf);
					if (childrenAsLeaves != null && bestLeaf.parent != null){
						removeLeaf(bestLeaf.parent, childrenAsLeaves);
					}
					if (childrenAsLeaves != null && childrenAsLeaves.size() > 0){
						insertInLeavesToAnalyse(childrenAsLeaves);
//						bestLeaf = leavesToAnalyse.get(0);
//					} else {
//						bestLeaf = cutTheTree(bestLeaf);
					}
				}
			}

			while (bestLeaf.parent != null){
				path.add(bestLeaf.data);
				bestLeaf = bestLeaf.parent;
			}
			path.add(bestLeaf.data);
			//			Collections.reverse(path);
			return path;
		}

		private void removeLeaf(Node<LeafInter> bestLeaf, List<Node<LeafInter>> childrenAsLeaves) {
			Iterator<Node<LeafInter>> itChild = childrenAsLeaves.iterator();
			while(itChild.hasNext()){
				if (itChild.next().data.equals(bestLeaf.data)){
					itChild.remove();
				}
			}
		}

		private void keepUnseenOnly(List<Node<LeafInter>> list){
			if (list != null && list.size() >0){
				Iterator<Node<LeafInter>> itChild = list.iterator();
				while(itChild.hasNext()){
					if (!canBeTreated(itChild.next().data)){
						itChild.remove();
					}
				}
			}
		}

		private Node<LeafInter> cutTheTree(Node<LeafInter> bestLeaf){
			if (bestLeaf.parent == null){
				return null; 
			}
				keepUnseenOnly(bestLeaf.parent.children);
			while (bestLeaf.parent.children.size() == 0){
				bestLeaf = bestLeaf.parent;
				if (bestLeaf.parent != null){
					keepUnseenOnly(bestLeaf.parent.children);
				}
				if (bestLeaf.parent == null){
					return null; 
				}
			}
			removeLeaf(bestLeaf, bestLeaf.parent.children);
			bestLeaf = bestLeaf.parent;
			leafSeen(bestLeaf.data, false);
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
			keepUnseenOnly(bestLeaf.children);
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
		void leafSeen(LeafInter leaf, boolean hasBeenSeen){
			
			if (hasBeenSeen){
				leafSeen.add(leaf);
			} else {
				leafSeen.remove(leaf);
			}
		}
		boolean canBeTreated(LeafInter leaf){
			return !leafSeen.contains(leaf);
		}


	}
}