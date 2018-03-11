/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

public class BSSuccess {
    private Integer demographicscsv = null;
    private Integer location_lookupcsv = null;
    private Integer locationcsv = null;        
    
    /**
     * constructor for successful rows from bootstrap
     * @param demo - a String value
     * @param loclookup - a String value
     * @param loc - a String value
     */
    public BSSuccess(Integer demo, Integer loclookup, Integer loc){
        this.demographicscsv = demo;
        this.location_lookupcsv = loclookup;
        this.locationcsv = loc;       
    }
        
    /**
     *get num of successful rows added from demographics, Integer
     * @return num of successful rows added from demographics, Integer
     */
    public Integer getDemographicscsv() {
        return demographicscsv;
    }

    /**
     * set num of successful rows added from demographics
     * @param demographicscsv - a String value
     */
    public void setDemographicscsv(Integer demographicscsv) {
        this.demographicscsv = demographicscsv;
    }

    /**
     *get num of successful rows added from location lookuo, Integer
     * @return num of successful rows added from location lookuo, Integer
     */
    public Integer getLocation_lookupcsv() {
        
        return location_lookupcsv;
    }

    /**
     * set num of successful rows added from location lookup
     * @param location_lookupcsv - a String value
     */
    public void setLocation_lookupcsv(Integer location_lookupcsv) {
        this.location_lookupcsv = location_lookupcsv;
    }

    /**
     *get num of successful rows added from location, Integer
     * @return num of successful rows added from location, Integer
     */
    public Integer getLocationcsv() {
        return locationcsv;
    }

    /**
     * set num of successful rows added from location
     * @param locationcsv - a String value
     */
    public void setLocationcsv(Integer locationcsv) {
        this.locationcsv = locationcsv;
    }
    
    
}
