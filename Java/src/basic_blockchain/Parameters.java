package basic_blockchain;

import java.util.LinkedList;
import java.util.Random;

public class Parameters {
    public static int edge_value_range = 50;
    public static double prob = 0.5; // The probability that there is no edge between two nodes
    public static double pace = 0.5;
    public static LinkedList<Node> nodes = new LinkedList();
    
    
    public static String randomGenerate(){
        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = 32;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) 
              (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }
    
    public static double period_calculator(){
        return Parameters.pace * Parameters.nodes.size();
    }
    
    public static void mine(){
        Random random = new Random();
        int randomIndx = random.nextInt(Parameters.nodes.size()); 
        Node lucky_node = Parameters.nodes.get(randomIndx);
        System.out.println("Congratulations to node " + lucky_node.id + " for being our next minner");
    }

}
