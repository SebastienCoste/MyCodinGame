package fr.sco.staticjo.codingame.medium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * 
 * @author Static
 *
 * My solution to https://www.codingame.com/training/medium/the-gift
 */
public class TheGift {



	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
//				int N = in.nextInt();
//				int C = in.nextInt();
//				List<Integer> allCash = new ArrayList<>(N);
//				for (int i = 0; i < N; i++) {
//					int B = in.nextInt();
//					allCash.add(B);
//				}

		int N = 3;
		int C = 100;
		List<Integer> allCash = Arrays.asList(20, 20, 40);

		Collections.sort(allCash);
		int remainingCash = C;
		List<Integer> contrib = new ArrayList<>(N);
		for (int i = 0; i < N; i++) {
			if (i == 0 || allCash.get(i) > allCash.get(i-1)){
				Integer cash = allCash.get(i);
				if (i > 0){
					cash = cash - allCash.get(i-1);
				}
				Result res = shareIt(N-i, remainingCash, cash);
				if (!res.overflow){
					for (int j = i; j < N; j++) {
						int share = res.share;
						if (j > N -1 - res.leave){
							share++;
						}
						if (i == 0){
							contrib.add(j, share);
						} else {
							contrib.add(j, contrib.get(i-1) + share);
						}
					}
					remainingCash = 0;
					break;
				} else {
					remainingCash = remainingCash - (N-i)*cash;
					if (i == 0){
						contrib.add(i, allCash.get(i));
					} else {
						contrib.add(i, contrib.get(i-1) + allCash.get(i));
					}
				}
			} else if (i > 0){
				contrib.add(i, contrib.get(i-1) + allCash.get(i));
			}
		}

		// Write an action using System.out.println()
		// To debug: System.err.println("Debug messages...");
		if (remainingCash > 0){
			System.out.println("IMPOSSIBLE");
		} else {
			for (int i = 0; i < N; i++) {
				System.out.println(contrib.get(i));
			}
		}
	}


	private static Result shareIt(int members, int total, int cash){

		Result res = new Result();
		res.overflow = total > members * cash;
		if (res.overflow){
			res.share = cash;
			res.leave = 0;
		} else {
			res.leave = total%members;
			res.share = total/members;
		}
		return res;
	}

	public static class Result{

		public boolean overflow;
		public int share;
		public int leave;
	}
}
