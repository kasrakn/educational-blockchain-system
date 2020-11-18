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
            new Node(id);
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

        Runnable transact_runnable = () -> {
            try {
                Parameters.transact();
            } catch (InterruptedException ex) {
                Logger.getLogger(Basic_Blockchain.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        ScheduledExecutorService transact_runner = Executors.newScheduledThreadPool(1);
        transact_runner.scheduleAtFixedRate(transact_runnable, 0, Parameters.transaction_rate, TimeUnit.SECONDS);

        Lock lock = new Lock();
        while(true){
            Thread.sleep(12000);
            System.out.println("Next round!!!");
            lock.lock();
            new Node((Parameters.nodes.size()));
            for(Node node: Parameters.nodes)
                node.update_costs();

            Parameters.print_costs();
            lock.unlock();
        }
    }
}
