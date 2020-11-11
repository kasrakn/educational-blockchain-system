/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic_blockchain;

public class Lock{

    private boolean isLocked = false;

    public synchronized void lock()
    throws InterruptedException{
      while(isLocked){
        wait();
      }
      isLocked = true;
    }

    public synchronized void unlock(){
      isLocked = false;
      notify();
    }
}