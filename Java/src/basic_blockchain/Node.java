package basic_blockchain;
import basic_blockchain.Parameters;
import java.awt.List;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class Node {
    List memory_pool;
    Map<Integer, Integer> costs;
    ArrayList blocks; //TODO: Add type of blocks
    final int id;
    final String private_key;
    final String public_key;
    
    Random random = new Random();
    
    public Node(int id){
        this.id = id;
        this.private_key = Parameters.randomGenerate();
        this.public_key = Parameters.randomGenerate();
        this.costs = new HashMap<>(); 
        this.blocks = new ArrayList();
        
        Parameters.nodes.add(this);
        
        for(int i = 0; i < Parameters.nodes.size(); i++){
            int edge_dist;
            
            System.out.println(this.id + " " + i);
          
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
    
//    public void gossip(block)
}

