package basic_blockchain;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Basic_Blockchain {
    public static void main(String[] args) throws Exception {
   
        Scanner scan = new Scanner(System.in);
        System.out.print("Please enter the initial number of nodes: ");
        int nodes_number = scan.nextInt();
        for(int id = 0; id < nodes_number; id++){
            Node node = new Node(id);
        }
        
        for (Node node : Parameters.nodes) {
            for (int i = 0; i < node.costs.size(); i++)
                System.out.println("NodeID: " + node.id + " " + i + " " + node.costs.get(i));
        }
        
        int period = (int)Math.ceil(Parameters.period_calculator());
        Runnable mine_runnable = () -> {
            try {
                Parameters.mine();
            } catch (Exception ex) {
                Logger.getLogger(Basic_Blockchain.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        exec.scheduleAtFixedRate(mine_runnable, 0, period, TimeUnit.SECONDS);
    }   
}
