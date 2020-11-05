package basic_blockchain;

import java.util.ArrayList;
import java.util.Random;

public class Parameters {
    public static int edge_value_range = 25;
    public static double prob = 0.2; // The probability that there is no edge between two nodes
    public static double pace = 0.5; // The more pace value would be, the faster everything is done
    public static ArrayList<Node> nodes = new ArrayList<>();
    
    public static double period_calculator(){
        return Parameters.pace * Parameters.nodes.size();
    }
    
    public static void mine() throws Exception{
        Random random = new Random();
        int randomIndx = random.nextInt(Parameters.nodes.size()); 
        Node lucky_node = Parameters.nodes.get(randomIndx);
        System.out.println("Congratulations to node " + lucky_node.id + " for being our next minner!");
        String block = RSA.sign(lucky_node.memory_pool, lucky_node.keys.getPrivate());
        System.out.println("Node " + lucky_node.id + " just signed its memory pool! ");
        lucky_node.gossip(block);
    }

}
