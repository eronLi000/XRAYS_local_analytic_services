/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

public class GroupLocation implements Comparable<GroupLocation> {
    private String location;
    private long time_spent;
    
    /**
     * Creates the GroupLocation constructor 
     * @param location the location id
     * @param time_spent time spent together
     */
    public GroupLocation(String location, long time_spent){
        this.location = location;
        this.time_spent = time_spent;
    }
    
    /**
     * 
     * @return location
     */

    public String getLocation() {
        return location;
    }
    
    /**
     * 
     * @param location sets the location
     */

    public void setLocation(String location) {
        this.location = location;
    }
    
    /**
     * 
     * @return time spent together
     */

    public long getTime_spent() {
        return time_spent;
    }
    
    /**
     * Calculate time spent together in seconds
     * @param time_spent - a long value
     */
    public void setTime_spent(long time_spent) {
        this.time_spent = time_spent/1000;
    }    

    /**
     * Compares one group location with another
     * @param o Group Location
     * @return position
     */
    @Override
    public int compareTo(GroupLocation o) {
        return this.location.compareTo(o.getLocation());        
    }
}
