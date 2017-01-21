package fr.sco.staticjo.codingame.common.dijkstra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import fr.sco.staticjo.codingame.common.dijkstra.Dijkstra.Tree.Node;

/**
 *  Work In Progress 
 *  
 * @author Static
 *
 * Common class used to find the lightest path in a tree 
 */
public class Dijkstra{

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
	}

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
				if (childrenAsLeaves != null && bestLeaf.parent != null){
					removeLeaf(bestLeaf, childrenAsLeaves);
				}
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
		path.add(bestLeaf.data);
		Collections.reverse(path);
		return path;
	}

	private void removeLeaf(Node<LeafInter> bestLeaf, List<Node<LeafInter>> childrenAsLeaves) {
		Iterator<Node<LeafInter>> itChild = childrenAsLeaves.iterator();
		while(itChild.hasNext()){
			if (itChild.next().data.equals(bestLeaf.parent.data)){
				itChild.remove();
			}
		}
	}

	private Node<LeafInter> cutTheTree(Node<LeafInter> bestLeaf){
		while (bestLeaf.parent.children.size() == 1){
			bestLeaf = bestLeaf.parent;
		}
		removeLeaf(bestLeaf, bestLeaf.parent.children);
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


























