package fr.sco.staticjo.codingame.common.dijkstra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.ComparatorUtils;

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
		int compareTo(LeafInter arg0);
		boolean isTheEnd(LeafInter end);
	}
	
	public static class Tree<T extends LeafInter> {
	    private Node<T> root;

	    public Tree(T rootData, T end) {
	        root = new Node<T>(rootData, end);
	        root.data = rootData;
	        root.children = new ArrayList<Node<T>>();
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
			public int compareTo(Object arg0) {
				return this.data.compareTo(((Node<T>)arg0).data);
			}

	    }
	}
	
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
			insertInLeavesToAnalyse(getChildrenAsLeaves(bestLeaf));
			bestLeaf = leavesToAnalyse.get(0);
			leavesToAnalyse.remove(0);
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


























