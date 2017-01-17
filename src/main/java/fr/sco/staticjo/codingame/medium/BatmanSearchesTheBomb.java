package fr.sco.staticjo.codingame.medium;

import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
 
class P {
    public int w;
    public int h;
    public P(int ww, int hh){
        w = ww;
        h = hh;
    }
}

	/**
	 * 
	 * @author Static
	 *
	 * My answer to https://www.codingame.com/training/medium/shadows-of-the-knight-episode-1
	 */
class BatmanSearchesTheBomb{

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int W = in.nextInt(); // width of the building.
        int H = in.nextInt(); // height of the building.
        int N = in.nextInt(); // maximum number of turns before game over.
        int X = in.nextInt();
        int Y = in.nextInt();
       
        int left = 0;
        int right = W;
        int down = H;
        int up = 0;

        // game loop
        while (true) {
            String bombDir = in.next(); // the direction of the bombs from batman's current location (U, UR, R, DR, D, DL, L or UL)
            System.err.println(X + " "  + Y + " " + bombDir);
            switch (bombDir) {
			case "U":
				left=X;
				right=X;
				down=Y;
				break;
			case "UR":
				left=X;
				down=Y;
				break;
			case "R":
				up=Y;
				down=Y;
				left=X;
				break;
			case "DR":
				left=X;
				up=Y;
				break;
			case "D":
				left=X;
				right=X;
				up=Y;
				break;
			case "DL":
				right=X;
				up=Y;
				break;
			case "L":
				up=Y;
				down=Y;
				right=X;
				break;
			case "UL":
				right=X;
				down=Y;
				break;
			default:
				break;
			}
            X = (right-left)/2;
            Y = (up - down)/2;
            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");
            System.err.println("l:"+left + " " + "r:"+right + "|" + "u:"+up + " " + "d:"+down);
            // the location of the next window Batman should jump to.
            System.out.println("X" + " " + Y);
        }
    }
}