package fr.sco.staticjo.codingame.clashofcode;

import java.util.Scanner;

	/**
	 * 
	 * @author Static
	 *
	 * Clash Of Code are 5min clash between coders on one topic
	 * 
	 */
public class NumerOfCandiesInMouth {

	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		int N = in.nextInt();
		int K = in.nextInt();

		System.err.println(N + "-" + K);
		StringBuilder res = new StringBuilder();
		boolean first = true;
		for (int i = 0; i <= N; i = i + K){
			int suite = N - i; 

			System.err.println(suite);
			int d = Math.min(K,suite);
			if (d >0){
				if (!first){

					res.append(" "); 
				}
				first = false;
				System.err.println("min:" + Math.min(K,suite));
				res.append(Math.min(K,suite));
			}
		}


		System.out.println(res);

	}
}
