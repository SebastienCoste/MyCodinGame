package fr.sco.staticjo.codingame.clashofcode;

import java.util.Scanner;

	/**
	 * 
	 * @author Static
	 *
	 * Clash Of Code are 5min clash between coders on one topic
	 * 
	 */
public class SimonSays {
	public static void zmain(String args[]) {
		Scanner in = new Scanner(System.in);
		String sentence = in.nextLine();
		System.err.println(sentence);
		if (sentence.startsWith("Simon")){

			String[] split = sentence.split("write ");
			split[0] = "";
			// Write an action using System.out.println()
			// To debug: System.err.println("Debug messages...");
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i<split.length; i++){
				sb.append(split[i]);
			}
			String res = sb.toString();
			if (res.isEmpty()){
				res = "...";
			}
			System.out.println(res);
		} else {
			System.out.println("...");
		}
		
	}
	
    
}
