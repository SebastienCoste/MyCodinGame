package fr.sco.staticjo.codingame.hard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class GolfMap {

	public static List<Board> allBoardsTested = new ArrayList<>();
	public static List<Move> dontLandOn = new ArrayList<>();
	public static List<Move> dontFlyOver = new ArrayList<>(); //To use for optimisation

	 public static void main(String args[]) {
	        Scanner in = new Scanner(System.in);
//	        int width = in.nextInt();
//	        int height = in.nextInt();
	        
	        String[] test = new String[]{"2.X","X.H",".H1"};
	        int width = 3;
	        int height = 3;
	        
	        
	        Board root = new Board(width, height);
	        
	        for (int i = 0; i < height; i++) {
//	            String row = in.next();
	        	String row = test[i];
	        	
	            for (int j = 0; j < width; j++) {
	            	char currentChar = row.charAt(j);
					if ('X' == currentChar){
	            		withNoLandOn(j, i);
	            	} else if ('H' == currentChar){
	            		Move hole = withNoFlyOver(j, i);
	            		root.allHoles.add(hole);
	            	}  else if (!('.' == currentChar)){
	            		Integer jump = Integer.valueOf(String.valueOf(currentChar));
	            		BallTry ball = new BallTry();
	            		ball.jumpsLeft = jump;
	            		ball.order = jump;
	            		Move move = new Move();
	            		move.x = j;
	            		move.y = i;
						ball.lastTry = move;
						root.allTriesLeft.add(ball);
	            	}
	            }
	        }
	        
	        Collections.sort(root.allTriesLeft);
	        
	        DepthFirst search = new DepthFirst(root);
	        List<LeafInter> result = search.buildTheTreeForThePathToTheEnd();
	        System.out.println(result);
	    }
	 
	public static Move withNoFlyOver(int x, int y){
		Move m = new Move();
		m.x = x;
		m.y = y;
		dontFlyOver.add(m);
		return m;
	}

	public static Move withNoLandOn(int x, int y){
		Move m = new Move();
		m.x = x;
		m.y = y;
		dontLandOn.add(m);
		return m;
	}

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
	

	
	public static class Segment{
		
		public int xmin, xmax, ymin, ymax; //*min <= *max
		public Direction orientation; //Direction from (xmin, ymin) to (xmax, ymax). Only DOWN and Right then
		
		public boolean cross (Segment seg){
			if (orientation == Direction.DOWN && seg.orientation == Direction.DOWN){
				 return this.xmin == seg.xmin && 
						 (
						 (this.ymin >= seg.ymin && this.ymin <= seg.ymax)
						 || (this.ymax >= seg.ymin && this.ymax <= seg.ymax)
						 || (seg.ymin >= this.ymin && seg.ymin <= this.ymax)
						 || (seg.ymax >= this.ymin && seg.ymax <= this.ymax)
						 ); 
			}
			if (orientation == Direction.RIGHT && seg.orientation == Direction.RIGHT){
				 return this.ymin == seg.ymin && 
						 (
						 (this.xmin >= seg.xmin && this.xmin <= seg.xmax)
						 || (this.xmax >= seg.xmin && this.xmax <= seg.xmax)
						 || (seg.xmin >= this.xmin && seg.xmin <= this.xmax)
						 || (seg.xmax >= this.xmin && seg.xmax <= this.xmax)
						 ); 
			}
			if (this.orientation == Direction.DOWN && seg.orientation == Direction.RIGHT){
				return this.xmin >= seg.xmin && this.xmin <= seg.xmax && seg.ymin >= this.ymin && seg.ymin <= this.ymax;
			} else {
				return seg.cross(this);
			}
		
		}
	}

	public static class Move {

		@Override
		public String toString() {
			return "Move [x=" + x + ", y=" + y + ", " + direction + "]";
		}
		public int x;
		public int y; 
		public int gap;
		public Direction direction;

		public Move copyOfThis(){
			Move move = copyOfCoordsOnly();
			move.gap = gap;
			move.direction = direction;
			return move;
		}
		
		public Move copyOfCoordsOnly(){
			Move move = new Move();
			move.x = x;
			move.y = y;
			return move;
		}
		public Segment getSegment(){
			Segment seg = new Segment();
			seg.xmin = x;
			seg.xmax = x;
			seg.ymin = y;
			seg.ymax = y;
			
			switch (direction) {
			case UP: seg.ymin -= gap;
			seg.orientation = Direction.DOWN;
			break;
			case RIGHT: seg.xmax += gap;
			seg.orientation = Direction.RIGHT;
			break;
			case DOWN: seg.ymax += gap;
			seg.orientation = Direction.DOWN;
			break;
			case LEFT: seg.xmin -= gap;
			seg.orientation = Direction.RIGHT;
			break;
			}
			
			return seg;
		}
		
		public Move getDestinationPoint(Direction dir, int gap){
			Move move = new Move();
			move.direction = dir;
			move.x = x;
			move.y = y;
			switch (dir) {
			case UP: move.y -= gap;
			break;
			case RIGHT: move.x += gap;
			break;
			case DOWN: move.y += gap;
			break;
			case LEFT: move.x -= gap;
			break;
			}
			move.gap = gap;
			return move;
		}
		
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
			this.gap = gap;
			move.gap = gap;
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

	public static class BallTry implements Comparable<BallTry>{


		@Override
		public String toString() {
			return "Ball [N°" + order + ", jumpsLeft=" + jumpsLeft + ", " + lastTry + "]";
		}


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

		
		@Override
		public int compareTo(BallTry o) {
			return Integer.compare(order, ((BallTry) o).order);
		}
	}


	public static class Board implements LeafInter{


		@Override
		public String toString() {
			return "Board [X=" + sizeX + ", Y=" + sizeY + ", allMovesDone=" + allMovesDone + ", allHoles="
					+ allHoles + ", allTriesLeft=" + allTriesLeft + "]";
		}

		List<Move> allMovesDone;
		List<Move> allHoles;
		List<BallTry> allTriesLeft;
		int sizeX;
		int sizeY;

		public Board(int sizeX, int sizeY){
			allMovesDone = new ArrayList<>();
			allTriesLeft = new ArrayList<>();
			allHoles = new ArrayList<>();
			this.sizeX = sizeX;
			this.sizeY = sizeY;
		}


		private boolean validateBallTry(BallTry ballTry){
			if (validateNoCross(ballTry)){
				Move dest = ballTry.lastTry.getDestinationPoint(ballTry.lastTry.direction, ballTry.jumpsLeft);
				dest.direction= null;
				if (ballTry.jumpsLeft == 1){
					return allHoles.contains(dest);
				} else {
					return !dontLandOn.contains(dest);
				}
			}
			return false;
		}


		private boolean validateNoCross(BallTry ballTry) {
			Move move = ballTry.lastTry;
			int gap = ballTry.jumpsLeft;
			Segment seg = move.getSegment();
			switch (move.direction){
			case UP: return move.y - gap >= 0 && validateMove(seg, gap);
			case LEFT: return move.x - gap >= 0 && validateMove(seg, gap);
			case DOWN: return move.y + gap < this.sizeY && validateMove(seg, gap);
			case RIGHT: return move.x + gap < this.sizeX && validateMove(seg, gap);
			}
			return false;
		}

		private boolean validateMove(Segment seg, int gap){
			return allMovesDone == null || allMovesDone.size() == 0 || !allMovesDone.stream().anyMatch(m -> seg.cross(m.getSegment()));
		}


		private Board copyOfThis(){
			Board next = new Board(this.sizeX, this.sizeY);
			next.allMovesDone = new ArrayList<>(this.allMovesDone);
			next.allTriesLeft = new ArrayList<>(this.allTriesLeft);
			next.allHoles = new ArrayList<>(this.allHoles);
			next.sizeX = this.sizeX;
			next.sizeY = this.sizeY;
			
			return next;
		}

		@Override
		public LeafInter getNextMatchingChild(LeafInter currentChild) {
			if (allTriesLeft == null || allTriesLeft.size() == 0){
				return null;
			}
			BallTry nextTry = allTriesLeft.get(0).copyOfThis();

			Direction nextDirection = Direction.getNext(nextTry.lastTry.direction);
			nextTry.lastTry.gap = nextTry.jumpsLeft;
			nextTry.lastTry.direction = nextDirection;
			while (nextDirection != null && !validateBallTry(nextTry)){
				nextDirection = Direction.getNext(nextDirection);
				nextTry.lastTry.direction = nextDirection;
			}
			if ( nextDirection == null){
				//All tries failed
				return null;
			}

			Board next = copyOfThis();
			Move move = nextTry.lastTry.copyOfThis();
			nextTry.lastTry.MoveToAndGetCopy(nextDirection, nextTry.jumpsLeft);
			
			Move dest = nextTry.lastTry.copyOfCoordsOnly();
			if (next.allHoles.contains(dest)){
				next.allHoles.remove(dest);
			}
			next.allMovesDone.add(move);
			nextTry.jumpsLeft--;
			nextTry.lastTry.direction = null;
			next.allTriesLeft.remove(0);
			if (nextTry.jumpsLeft > 0){
				next.allTriesLeft.add(0, nextTry);
			}


			return next;
		}

		@Override
		public boolean isTheEnd() {
			return (allTriesLeft == null || allTriesLeft.isEmpty()) && (allHoles == null || allHoles.isEmpty());
		}

		@Override
		public void seen(boolean hasBeenSeen) {
			if (hasBeenSeen){
				allBoardsTested.add(this);
			} else {
				allBoardsTested.remove(this);
			}

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
		boolean isTheEnd();
		void seen(boolean hasBeenSeen);
		boolean canBeTreated(); //For example if you don't want to treat an already seen leaf
	}

	protected static class Tree<T extends LeafInter> {
		private Node<T> root;

		public Tree(T rootData) {
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

	public static class DepthFirst {



		public Tree<LeafInter> tree;
		public Set<LeafInter> leavesSeen;

		public DepthFirst(LeafInter start) {
			tree = new Tree<LeafInter>(start);
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
			if (root.data.isTheEnd()){
				return root;
			}

			Node<LeafInter> child = getNextChild(root, null);
			while (child != null){
				if (child.data.canBeTreated() && !leavesSeen.contains(child.data) && !root.data.equals(child.data)){
					Node<LeafInter> childOnRightPath = getTheRightPath(child);
					if (childOnRightPath != null){
						return childOnRightPath;
					}
					child.data.seen(true);
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
