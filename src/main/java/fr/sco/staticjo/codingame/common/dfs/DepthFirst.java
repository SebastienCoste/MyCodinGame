package fr.sco.staticjo.codingame.common.dfs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.sco.staticjo.codingame.common.dfs.DepthFirst.Tree.Node;

public class DepthFirst {
	
	public interface LeafInter {

		LeafInter getNextMatchingChild(LeafInter currentChild);
		boolean isTheEnd(LeafInter end);
		void seen(boolean hasBeenSeen);
		boolean canBeTreated(); //For example if you don't want to treat an already seen leaf
	}

	protected static class Tree<T extends LeafInter> {
		private Node<T> root;

		public Tree(T rootData, T end) {
			root = new Node<T>(rootData);
		}

		protected static class Node<T extends LeafInter> {
			@Override
			public String toString() {
				return "Node [data=" + data + "]";
			}

			private T data;
			private Node<T> parent;

			public Node(T data){
				this.data = data;
			}

			public Node(Node<T> parent, T data){
				this.data = data;
				this.parent = parent;
			}

		}
	}

	public Tree<LeafInter> tree;
	public LeafInter end;
	public Set<LeafInter> leavesSeen;

	public DepthFirst(LeafInter[][] map, LeafInter start, LeafInter end) {
		tree = new Tree<LeafInter>(start, end);
		this.end = end;
		leavesSeen = new HashSet<>();
	}

	public List<LeafInter> buildTheTreeForThePathToTheEnd(){
		
		List<LeafInter> path = new ArrayList<>();
		
		Node<LeafInter> rightLeaf = getTheRightPath(tree.root);
		if (rightLeaf == null){
			return path;
		}
		while (rightLeaf.parent != null){
			path.add(rightLeaf.data);
			rightLeaf = rightLeaf.parent;
		}
		path.add(tree.root.data);
		Collections.reverse(path);
		return path;
	}
	
	private Node<LeafInter> getTheRightPath(Node<LeafInter> root){
		if (!root.data.canBeTreated()){
			return null;
		}
		if (root.data.isTheEnd(end)){
			return root;
		}
		
		Node<LeafInter> child = getNextChild(root, null);
		while (child != null){
			if (child.data.canBeTreated() && !leavesSeen.contains(child.data) && !root.data.equals(child.parent.data)){
				Node<LeafInter> childOnRightPath = getTheRightPath(child);
				if (childOnRightPath != null){
					return childOnRightPath;
				}
				leavesSeen.add(child.data);
			}
			child = getNextChild(root, child.data);
		}
		
		return null;
	}
	
	
	private Node<LeafInter> getNextChild(Node<LeafInter> leaf, LeafInter currentChild) {
		LeafInter dataChild = leaf.data.getNextMatchingChild(currentChild);
		if (dataChild == null){
			return null;
		}
		return new Node<LeafInter>(leaf, dataChild);
	}

}
