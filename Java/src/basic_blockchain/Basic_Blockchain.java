package basic_blockchain;

import java.util.Scanner;


public class Basic_Blockchain {
    void mine(){
        
    }
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int nodes_number = scan.nextInt();
        for(int id = 0; id < nodes_number; id++){
            Node node = new Node(id);
        }
        
        for (Node node : Parameters.nodes) {
            for (int i = 0; i < node.costs.size(); i++)
                System.out.println("NodeID: " + node.id + " " + i + " " + node.costs.get(i));
                
        }
        
    }
    
}
