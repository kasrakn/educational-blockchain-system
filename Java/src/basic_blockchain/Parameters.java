package basic_blockchain;

import java.util.ArrayList;
import java.util.Random;

public class Parameters {
    public static int edge_value_range = 25;
    public static double prob = 0.2; // The probability that there is no edge between two nodes
    public static double delay = 1; // The more delay value would be, the slower everything is done
    public static int transaction_rate = 7; // Specifies the time gap between each transaction
    public static ArrayList<Node> nodes = new ArrayList<>(); // Holds the nodes of whole network

    public static double period_calculator(){ // Returns the time gap between each mine
        return Parameters.delay * Parameters.nodes.size();
    }

    public static void mine() throws Exception{
        Random random = new Random();
        Lock lock = new Lock();

        int randomIndx = random.nextInt(Parameters.nodes.size());
        Node lucky_node = Parameters.nodes.get(randomIndx);
        System.out.println("\033[32;1m"+ "Congratulations to node " + lucky_node.id + " for being our next miner!" + "\033[0m");

        lock.lock();
        String block = RSA.sign(lucky_node.memory_pool, lucky_node.keys.getPrivate());
        System.out.println("Node " + lucky_node.id + " just signed its memory pool! ");

        lucky_node.pre_events.add(block);
        Thread thread = new Thread(() -> {
            lucky_node.gossip(block, 1);
        });
        lock.unlock();
        thread.start();
    }

    public static void transact() throws InterruptedException{
        Random random = new Random();
        int senderIndx = random.nextInt(Parameters.nodes.size());
        int recieveIndx = random.nextInt(Parameters.nodes.size());
        Node sender = Parameters.nodes.get(senderIndx);
        Node receiver = Parameters.nodes.get(recieveIndx);
        int amount = random.nextInt(sender.coins)+1;

        if(sender.id != receiver.id){
            String transaction = "Node " + sender.id + " sent " + amount + " coins to node "+ receiver.id;
            Lock lock = new Lock();

            lock.lock();
            sender.coins -= amount;
            receiver.coins += amount;
            sender.memory_pool += transaction;
            receiver.memory_pool += transaction;
            sender.pre_events.add(transaction);
            receiver.pre_events.add(transaction);
            lock.unlock();

            System.out.println("\033[34;1m" + "A transaction has been occurred between node " + sender.id + " and node " + receiver.id + "\033[0m");
        }
        else
            System.out.println("\033[35;1m" + "Illegal Transaction" + "\033[0m");
    }

    public static void print_distances(){
        System.out.println("\033[34;1m" + "====================================================");
        for(Node node: Parameters.nodes){
            String dist;
            for(int j = 0; j < Parameters.nodes.size(); j++){
                dist = Integer.toString(node.distances.get(j));
                if(node.distances.get(j) == Integer.MAX_VALUE)
                    dist = "\u221E";

                System.out.print(dist + "\t");
            }
            System.out.print("\n");
        }
        System.out.println("====================================================" + "\033[0m");
    }
}
