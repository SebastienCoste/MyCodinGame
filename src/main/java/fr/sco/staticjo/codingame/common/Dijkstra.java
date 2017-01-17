package fr.sco.staticjo.codingame.common;

import java.util.ArrayList;
import java.util.List;

	/**
	 *  Work In Progress 
	 *  
	 * @author Static
	 *
	 * Common class used to find the lightest path in a tree 
	 */
public class Dijkstra{

	public interface LeafInter{
		
		long getWeightToTheEnd(LeafInter end);
		List<LeafInter>getAllMatchingNeighbours(LeafInter[][] map);
		
	}
	
	public static class Tree<T extends LeafInter> {
	    private Node<T> root;

	    public Tree(T rootData, T end) {
	        root = new Node<T>(rootData, end);
	        root.data = rootData;
	        root.children = new ArrayList<Node<T>>();
	    }

	    public static class Node<T extends LeafInter> {
	    	public long currentWeight;
			public long estimatedWeightLeft;
	        private T data;
	        private Node<T> parent;
	        private List<Node<T>> children;
	        
	        public Node(T data, T end){
				this.data = data;
				currentWeight = 0;
				estimatedWeightLeft = data.getWeightToTheEnd(end);
			}
			
			public Node(Node<T> parent, long weightFromparent, T data, T end){
				this.data = data;
				this.parent = parent;
				currentWeight = parent.currentWeight + weightFromparent;
				estimatedWeightLeft = data.getWeightToTheEnd(end);
			}
			
	    }
	}
	
	public LeafInter[][] map;
	public Tree<LeafInter> tree;
	public LeafInter end;
	
	public Dijkstra(LeafInter[][] map, LeafInter start, LeafInter end){
		tree = new Tree<LeafInter>(start, end);
		this.map = map;
		this.end = end;
	}
}