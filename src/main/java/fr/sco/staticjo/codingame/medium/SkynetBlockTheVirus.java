package fr.sco.staticjo.codingame.medium;

import java.util.*;
import java.io.*;
import java.math.*;

	/**
	 * 
	 * @author Static
	 *
	 * My answer to https://www.codingame.com/training/medium/skynet-revolution-episode-1
	 */
class SkynetBlockTheVirus {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt(); // the total number of nodes in the level, including the gateways
        int L = in.nextInt(); // the number of links
        int E = in.nextInt(); // the number of exit gateways
        
        Map<Integer, List<Integer>> mapLink = new HashMap<>();
        List<Integer> lstExit = new ArrayList<Integer>();
        
        for (int i = 0; i < L; i++) {
            int N1 = in.nextInt(); // N1 and N2 defines a link between these nodes
            int N2 = in.nextInt();
            addToMap(mapLink, N1, N2);
            addToMap(mapLink, N2, N1);
        }
        for (int i = 0; i < E; i++) {
            int EI = in.nextInt(); // the index of a gateway node
            lstExit.add(EI);
        }
        

        // game loop
        while (true) {
            int SI = in.nextInt(); // The index of the node on which the Skynet agent is positioned this turn
            boolean removed = false;
            Optional<Integer> optExit = mapLink.get(SI).stream()
                .filter( e -> lstExit.contains(e)).findFirst();

             if (optExit.isPresent()){
                Integer exit = optExit.get();
                System.out.println(SI + " " + exit);
                removeFromMap(mapLink, SI, exit);
                removeFromMap(mapLink, exit, SI);
                removed = true;
             }
            if (!removed){
                boolean found = false;
                int i = 0;
                while(!found){
                    Integer exit = lstExit.get(i);
                    
                   List<Integer> voisinLst = mapLink.get(exit);
                    if (voisinLst != null){
                        found = true;
                        Integer voisin = voisinLst.get(0);
                        System.out.println(voisin + " " + exit);
                        removeFromMap(mapLink, voisin, exit);
                        removeFromMap(mapLink, exit, voisin);
                    } else {
                        lstExit.remove(exit);
                    }
                }
                 
            
            }
        }
    }
    
    
    private static void addToMap(Map<Integer, List<Integer>> map, Integer key, Integer value){
     
         List<Integer> lst = map.get(key);  
         if (lst == null){
             lst = new ArrayList<Integer>();
             map.put(key, lst);
        }
        
        lst.add(value);
    }
    
    private static void removeFromMap(Map<Integer, List<Integer>> map, Integer key, Integer value){
        
         List<Integer> lst = map.get(key); 
         if (lst != null){
             lst.remove(value);
             if (lst.size() == 0){
                 map.remove(key);
             }
                 
         }
    }
        
}