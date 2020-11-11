package basic_blockchain;

import java.util.ArrayList;
import java.util.Random;

public class Parameters {
    public static int edge_value_range = 25;
    public static double prob = 0.2; // The probability that there is no edge between two nodes
    public static double pace = 0.5; // The more pace value would be, the faster everything is done
    public static int transaction_rate = 5;
    public static ArrayList<Node> nodes = new ArrayList<>();
    
    public static double period_calculator(){
        return Parameters.pace * Parameters.nodes.size();
    }
    
    public static void mine() throws Exception{
        Random random = new Random();
        Lock lock = new Lock();
        
        int randomIndx = random.nextInt(Parameters.nodes.size()); 
        Node lucky_node = Parameters.nodes.get(randomIndx);
        System.out.println("Congratulations to node " + lucky_node.id + " for being our next minner!");
        
        lock.lock();
        String block = RSA.sign(lucky_node.memory_pool, lucky_node.keys.getPrivate());
        System.out.println("Node " + lucky_node.id + " just signed its memory pool! ");
        
        lucky_node.pre_events.add(block);
        Thread thread = new Thread(() -> {
            lucky_node.gossip(block, 1);
        });
        thread.start();
        lock.unlock();
    }
    
    public static void transact() throws InterruptedException{
        Random random = new Random();
        int senderIndx = random.nextInt(Parameters.nodes.size());
        int recieveIndx = random.nextInt(Parameters.nodes.size());
        Node sender = Parameters.nodes.get(senderIndx);
        Node reciever = Parameters.nodes.get(recieveIndx);
        int amount = random.nextInt(sender.coins)+1;
        
        if(sender.id != reciever.id){
            String transaction = "Node " + sender.id + " sent " + amount + " coins to node "+ reciever.id;
            Lock lock = new Lock();
            
            lock.lock();
            sender.coins -= amount;
            reciever.coins += amount;
            sender.memory_pool += transaction;
            reciever.memory_pool += transaction;
            lock.unlock();
            
            System.out.println("A transaction has been occurred between node " + sender.id + " and node " + reciever.id);
            
            sender.pre_events.add(transaction);
            reciever.pre_events.add(transaction);
        }
        else
            System.out.println("Illegal Transaction");
    }

}
