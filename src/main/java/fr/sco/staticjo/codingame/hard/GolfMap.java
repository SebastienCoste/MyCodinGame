package fr.sco.staticjo.codingame.hard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GolfMap {

	public static List<Board> allBoardsTested = new ArrayList<>();
	
	public static enum Direction{
		UP, 
		RIGHT,
		DOWN,
		LEFT;
		
		public static Direction getNext(Direction dir){
			if (dir == null){
				return UP;
			}
			switch (dir){
			case UP: return RIGHT;
			case RIGHT: return DOWN;
			case DOWN: return LEFT;
			case LEFT: return null;
			default: return UP;
			}
				
		}
	}
	
	public static class Move {
		
		public int x;
		public int y; 
		public Direction direction;
		
		public Move MoveToAndGetCopy(Direction dir, int gap){
			Move move = new Move();
			move.direction = dir;
			direction = dir;
			move.x = x;
			move.y = y;
			switch (dir) {
			case UP: move.y -= gap;
			y -= gap;
			break;
			case RIGHT: move.x += gap;
			x += gap;
			break;
			case DOWN: move.y += gap;
			y += gap;
			break;
			case LEFT: move.x -= gap;
			x -= gap;
			break;
			}
			
			return move;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((direction == null) ? 0 : direction.hashCode());
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
			Move other = (Move) obj;
			if (direction != other.direction)
				return false;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}
		
	}
	
	public static class BallTry{
		

		public int order;
		public int jumpsLeft;
		public Move lastTry;
		
		public BallTry copyOfThis(){
			BallTry ball = new BallTry();
			ball.jumpsLeft = this.jumpsLeft;
			ball.lastTry = this.lastTry;
			ball.order = this.order;
			
			return ball;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + jumpsLeft;
			result = prime * result + ((lastTry == null) ? 0 : lastTry.hashCode());
			result = prime * result + order;
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
			BallTry other = (BallTry) obj;
			if (jumpsLeft != other.jumpsLeft)
				return false;
			if (lastTry == null) {
				if (other.lastTry != null)
					return false;
			} else if (!lastTry.equals(other.lastTry))
				return false;
			if (order != other.order)
				return false;
			return true;
		}
	}
	
	
	public static class Board implements LeafInter{
	
		
		List<Move> allMovesDone;
		List<BallTry> allTriesLeft;
		
		
		private boolean validateMove(BallTry ballTry){
			//TODO
			
			return false;
		}
		
		private Board copyOfThis(){
			Board next = new Board();
			next.allMovesDone = new ArrayList<>(this.allMovesDone);
			next.allTriesLeft = new ArrayList<>(this.allTriesLeft);
			
			return next;
		}

		@Override
		public LeafInter getNextMatchingChild(LeafInter currentChild) {
			if (allTriesLeft == null || allTriesLeft.size() == 0){
				return null;
			}
			BallTry nextTry = allTriesLeft.get(0).copyOfThis();
					
			Direction nextDirection = Direction.getNext(nextTry.lastTry.direction);
			
			while (nextDirection != null && !validateMove(nextTry)){
				nextDirection = Direction.getNext(nextDirection);
			}
			if ( nextDirection == null){
				//All tries failed
				return null;
			}
			
			Board next = copyOfThis();
			Move move = nextTry.lastTry.MoveToAndGetCopy(nextDirection, nextTry.jumpsLeft);
			next.allMovesDone.add(move);
			nextTry.jumpsLeft--;
			if (nextTry.jumpsLeft == 0){
				next.allTriesLeft.remove(0);
			} else {
				next.allTriesLeft.add(0, nextTry);
			}
			
			
			return next;
		}

		@Override
		public boolean isTheEnd(LeafInter end) {
			return allTriesLeft == null || allTriesLeft.isEmpty();
		}

		@Override
		public void seen(boolean hasBeenSeen) {
			allBoardsTested.add(this);
			
		}

		@Override
		public boolean canBeTreated() {
			return !allBoardsTested.contains(this);
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((allMovesDone == null) ? 0 : allMovesDone.hashCode());
			result = prime * result + ((allTriesLeft == null) ? 0 : allTriesLeft.hashCode());
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
			Board other = (Board) obj;
			if (allMovesDone == null) {
				if (other.allMovesDone != null)
					return false;
			} else if (!allMovesDone.containsAll(other.allMovesDone) || !other.allMovesDone.containsAll(allMovesDone))
				return false;
			if (allTriesLeft == null) {
				if (other.allTriesLeft != null)
					return false;
			} else if (!allTriesLeft.containsAll(other.allTriesLeft) || !other.allTriesLeft.containsAll(allTriesLeft))
				return false;
			return true;
		}
	}
	
	
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
	
	public class DepthFirst {
		
		

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
}
