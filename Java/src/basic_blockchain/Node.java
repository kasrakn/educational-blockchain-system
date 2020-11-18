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
    ArrayList<String> pre_events; // This will be used to prevents getting stuck in loops while gossiping/receiving
    final int id;
    KeyPair keys; // Includes a private key and a public key
    int coins;

    public Node(int id) throws Exception{
        this.id = id;
        keys = RSA.generateKeyPair();
        memory_pool = new String();
        this.costs = new HashMap<>();
        this.blocks = new ArrayList();
        this.pre_events = new ArrayList();
        Parameters.nodes.add(this);
        Random random = new Random();

        this.coins = 1000;

        for(int i = 0; i < Parameters.nodes.size(); i++){
            int edge_cost;
            edge_cost = 1+random.nextInt(Parameters.edge_value_range);

            if(edge_cost <= Parameters.edge_value_range*Parameters.prob){ // Make an edge between this node and node[i]
                Parameters.nodes.get(this.id).costs.put(i, edge_cost);
                Parameters.nodes.get(i).costs.put(this.id, edge_cost);
            }
            else{
                Parameters.nodes.get(this.id).costs.put(i, Integer.MAX_VALUE);
                Parameters.nodes.get(i).costs.put(this.id, Integer.MAX_VALUE);
            }
            this.costs.put(this.id, 0);
        }
        System.out.println("\033[31;1m" + "Node " + id + " has been initialized..." + "\033[0m");
    }

    public Node getNode(){
        return this;
    }

    public void gossip(String block, int mode) {
        // mode 0 : transaction
        // mode 1: mine
            for(int i = 0; i < Parameters.nodes.size(); i++){
                int time_to_receive = (int)Math.ceil(Parameters.delay * getNode().costs.get(i)); // Simulate the latency

                // Avoid the node to have a transaction with
                // itself and the edges that aren't connected to this node
                if (getNode().costs.get(i) != 0 && getNode().costs.get(i) != Integer.MAX_VALUE) {
                    if(mode == 1)
                        System.out.println("Node " + getNode().id + " is gossiping a block with node " + Parameters.nodes.get(i).id);
                    else if (mode == 0)
                        System.out.println("Node " + getNode().id + " is gossiping a transaction: { " + block + " } with node " + Parameters.nodes.get(i).id);

                    if(time_to_receive <= Parameters.delay * Parameters.edge_value_range){
                        AtomicInteger indx = new AtomicInteger(i);
                        Thread thread = new Thread(() -> {
                            try {
                                Thread.sleep(time_to_receive);
                                Parameters.nodes.get(indx.get()).receive(block, mode);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        thread.start();
                    }
                }
            }
    }

    public void receive(String block, int mode) throws InterruptedException {
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

    public void update_costs(){
        UpdateCosts.costMinimizer(this.id, Parameters.nodes);
    }
}

