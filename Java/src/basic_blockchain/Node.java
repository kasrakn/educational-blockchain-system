package basic_blockchain;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Node {
    String memory_pool;
    Map<Integer, Integer> costs;
    ArrayList<String> blocks; 
    final int id;
    KeyPair keys;
    
    Random random = new Random();
    
    public Node getNode(){
        return this;
    }
    
    public Node(int id) throws Exception{
        System.out.println("Node " + id + " is being initialized...");
        this.id = id;
        keys = RSA.generateKeyPair();
        memory_pool = new String();
        this.costs = new HashMap<>(); 
        this.blocks = new ArrayList();
        Parameters.nodes.add(this);
        
        for(int i = 0; i < Parameters.nodes.size(); i++){
            int edge_dist;
            edge_dist = 1+random.nextInt(Parameters.edge_value_range);
            
            if(edge_dist <= Parameters.edge_value_range*Parameters.prob){
                Parameters.nodes.get(this.id).costs.put(i, edge_dist);
                Parameters.nodes.get(i).costs.put(this.id, edge_dist);
            }
            else{
                Parameters.nodes.get(this.id).costs.put(i, Integer.MAX_VALUE);
                Parameters.nodes.get(i).costs.put(this.id, Integer.MAX_VALUE);
            }
            this.costs.put(this.id, 0);
        }
    }

    public void gossip(String block) { // Done 
        Thread thread = new Thread(() -> 
        {
            for(int i = 0; i < Parameters.nodes.size(); i++){
                int time_to_recieve = (int)Math.ceil((1-Parameters.pace) * getNode().costs.get(i));
                
                // Avoid the node to have a transaction with 
                //itself and the edges that aren't connected to this node
                if (getNode().costs.get(i) != 0 && getNode().costs.get(i) != Integer.MAX_VALUE) {
                    System.out.println("Node " + getNode().id + " sent message to " + Parameters.nodes.get(i).id);
                    try {
                        Thread.sleep(time_to_recieve);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Parameters.nodes.get(i).recieve(block);
                }
            }
        });
        thread.start();
    }
    
    public void recieve(String block) { //Done
        this.blocks.add(block);
    }
    
    public void update_distances(){
        // Time to code: It is your turn to complete this method
        // This method is doing some stuff in order to update the costs
        
        // Update the distances in a way to minimize the sum of the 
        // distances of the whole network
    }
}

