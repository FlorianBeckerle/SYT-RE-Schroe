/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package producerconsumertest;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author robreder
 */
public class MessagePool {

    private String[] pool; 
    private int indexP;
    private int indexC;
    private final Semaphore availibleElementsForConsumption; //prevents underflow //n
    private final Semaphore availibleSpaceForProduction;//prevents overflow  //e
    /***
     * Constructor MessagePool with size of store as parameter
     * @param size 
     */
    public MessagePool(int size) {
        pool = new String[size];
        indexP = 0;
        indexC=0;
        availibleElementsForConsumption = new Semaphore(0, false); //Semaphore für verfügbare "Produkte"
        availibleSpaceForProduction = new Semaphore(size, false); //Semaphore für verfügbaren Platz für Produkte
    }

    

    /***
     * get() is used from the consumer
     * @return 
     */
    public String get() {
        String value = "";
        try {
            availibleElementsForConsumption.acquire(); //verfügbare Produkte um 1 verringern
            synchronized(this){ //sichergehen, dass diese Aktion nicht unterbrochen wird
                value = pool[indexC]; //neues "Produkt" in den Pool packen
                indexC= (indexC+1)%pool.length;
            }
            availibleSpaceForProduction.release(); //Platz für weitere Produkte freigeben
            return value;
        } catch (InterruptedException ex) {
            Logger.getLogger(MessagePool.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }

    /***
     * put is used from the producer to put an object to the data pool
     * @param value 
     */
    public void put(String value) {
        try {
            availibleSpaceForProduction.acquire(); //verfügbaren Platz um 1 verringern
            synchronized(this){ //sichergehen, dass diese Aktion nicht unterbrochen wird
                pool[indexP] = value; //pool mit dem Value füllen
                indexP= (indexP+1)%pool.length;
            }
            availibleElementsForConsumption.release(); //verfügbare Produkte um 1 erhöhen //freigabe zur verarbeitung eines weiteren "Produkts"
        } catch (InterruptedException ex) {
            Logger.getLogger(MessagePool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
