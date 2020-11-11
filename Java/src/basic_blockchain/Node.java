package basic_blockchain;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Node {
    String memory_pool;
    Map<Integer, Integer> costs;
    ArrayList<String> blocks; 
    ArrayList<String> pre_events;
    final int id;
    KeyPair keys;
    int coins;
    Random random;
    
    public Node(int id) throws Exception{
        System.out.println("Node " + id + " is being initialized...");
        this.id = id;
        keys = RSA.generateKeyPair();
        memory_pool = new String();
        this.costs = new HashMap<>(); 
        this.blocks = new ArrayList();
        this.pre_events = new ArrayList();
        Parameters.nodes.add(this);
        random = new Random();
        this.coins = 1000;
        
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

    public Node getNode(){
        return this;
    }
    
    public void gossip(String block, int mode) { //DONE
        if(mode == 1){
            for(int i = 0; i < Parameters.nodes.size(); i++){
                int time_to_recieve = (int)Math.ceil((1-Parameters.pace) * getNode().costs.get(i));
                
                // Avoid the node to have a transaction with 
                //itself and the edges that aren't connected to this node
                if (getNode().costs.get(i) != 0 && getNode().costs.get(i) != Integer.MAX_VALUE) {
                    System.out.println("Node " + getNode().id + " is gossiping a block with node " + Parameters.nodes.get(i).id);
                    if(time_to_recieve <= Parameters.edge_value_range){
                        AtomicInteger indx = new AtomicInteger(i);
                        Thread thread = new Thread(() -> {
                            try {
                                Thread.sleep(time_to_recieve);
                                Parameters.nodes.get(indx.get()).recieve(block, mode);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        thread.start();
                    }
                }
            }
        }
        else if (mode == 0){
            for(int i = 0; i < Parameters.nodes.size(); i++){
                int time_to_recieve = (int)Math.ceil((1-Parameters.pace) * getNode().costs.get(i));
                
                // Avoid the node to have a transaction with 
                //itself and the edges that aren't connected to this node
                if (getNode().costs.get(i) != 0 && getNode().costs.get(i) != Integer.MAX_VALUE) {
                    System.out.println("Node " + getNode().id + " is gossiping a transaction with node " + Parameters.nodes.get(i).id);
                    if(time_to_recieve <= Parameters.edge_value_range){
                        AtomicInteger indx = new AtomicInteger(i);
                        Thread thread = new Thread(() -> {
                            try {
                                Thread.sleep(time_to_recieve);
                                Parameters.nodes.get(indx.get()).recieve(block, mode);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        thread.start();
                    }
                }
            }
            
        }
    }
    
    public void recieve(String block, int mode) throws InterruptedException { //DONE
        if(!this.pre_events.contains(block)){
            this.pre_events.add(block);
            if(mode == 1){
                this.blocks.add(block);
                this.memory_pool = "";
            }
            else
                this.memory_pool += block;
            Thread.sleep(1000);
            Thread thread = new Thread(() -> {
                this.gossip(block, mode);
            });
            thread.start();
        }
    }
    
    public void update_distances(){
        // Time to code: It is your turn to complete this method
        // This method is doing some stuff in order to update the costs
        
        // Update the distances in a way to minimize the sum of the 
        // distances of the whole network
    }
}

