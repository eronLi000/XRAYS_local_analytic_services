/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

public class Companion{
    private int rank;
    private String companion;
    private String mac_address;
    private long time_together;
    
    /**
     * Creates the companion object to be thrown within the companion container
     * @param rank rank based on order 
     * @param time_together calculated time spent together
     * @param mac_address mac address of the companion
     * @param companion name of the companion
     */
    public Companion(int rank , long time_together, String mac_address, String companion){
        this.rank = rank;
        this.time_together = time_together;
        this.companion = companion;
        this.mac_address = mac_address;
        getTimeInSeconds();
    }
    
    /**
     * 
     * @return the rank
     */

    public int getRank() {
        return rank;
    }
    
    /**
     * 
     * @return the time spent together
     */
    
    public long getTimeTogether() {
        return time_together;
    }
    
    /**
     * 
     * @return the companion
     */

    public String getCompanion() {
        return companion;
    }
    
    /**
     * 
     * @param companion set companion
     */

    public void setCompanion(String companion) {
        this.companion = companion;
    }

    /**
     * 
     * @return the mac address
     */
    public String getMac_address() {
        return mac_address;
    }

    /**
     * 
     * @param mac_address set the mac address
     */
    public void setMac_address(String mac_address) {
        this.mac_address = mac_address;
    }
    
    /**
     * Calculates time in seconds
     */
    
    public void getTimeInSeconds(){
        this.time_together = this.time_together/1000;
    }
}