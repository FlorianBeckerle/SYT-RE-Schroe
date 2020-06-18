/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package producerconsumertest;

/**
 *
 * @author robreder
 */
class Consumer extends Thread {

    private final MessagePool _messagePool;
    private String _consumerName;
    private int _consumeAmount;

    //Constructor
    public Consumer(MessagePool pool, String name, int consumption) {
        _messagePool = pool;
        _consumerName = name;
        _consumeAmount = consumption;
    }

    @Override
    public void run() {
        String value = "";
        for (int i = 0; i < _consumeAmount; i++) {
            // fetch message from the pool
            value = _messagePool.get(); //Value/Produkt aus dem Pool holen
            System.out.println("Consumer " + this._consumerName + " got: " + value); //Produkt in der Console ausgeben
            
            try {
                // Consumption of something from the pool needs some time
                sleep((int) (Math.random() * 200));
            } catch (InterruptedException e) {
            }
            
        }
    }
}
