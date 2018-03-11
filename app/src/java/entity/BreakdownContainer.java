/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.List;

public class BreakdownContainer {
    private String status;
    private List<String> messages;
    private List<Breakdown> breakdown;
    Integer totalCount = null;
    
    /**
     * Creates a new BreakdownContainer Object for when results are returned
     * 
     * This method creates a new Breakdown Object to be used in the breakdown list.
     *
     * @param status the status of the breakdown query
     * @param breakdownList the list of Breakdown objects retrieved
     */
    public BreakdownContainer(String status, List<Breakdown> breakdownList){
        this.status = status;
        this.breakdown = breakdownList;
        this.messages = null;
    }
    
    /**
     * Creates a new BreakdownContainer Object for when there are error messages returned
     * 
     * This method creates a new Breakdown Object to be used in the breakdown list.
     * 
     * @param messages the list of error messages retrieved
     * @param status the status of the breakdown query
     */
    public BreakdownContainer(List<String> messages, String status){
        this.status = status;
        this.messages = messages;
        this.breakdown = null;
    }
    
    /**
     * Returns the status of the Breakdown Container object. 
     * 
     * This method returns string value with either "success" or "error"
     *
     * @return the status of the current BreakdownContainer
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * Sets status attribute to parameter input 
     * 
     * @param status a string value with either "success" or "error"
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns the messages of the Breakdown Container object. 
     * 
     * This method returns all the error messages for this object
     *
     * @return a List object of String messages
     */
    public List<String> getMessages() {
        return messages;
    }

    /**
     * Sets message attribute to parameter input 
     * 
     * @param message a list of String objects
     */
    public void setMessages(List<String> message) {
        this.messages = message;
    }
    
    /**
     * Returns the breakdowns of the Breakdown Container object. 
     * 
     * This method returns all the breakdowns for this object
     *
     * @return a List object of Breakdown objects
     */
    public List<Breakdown> getBreakdown() {
        return this.breakdown;
    }
    
    /**
     * Sets breakdownList attribute to parameter input 
     * 
     * @param breakdownList a list of Breakdown objects
     */
    public void setBreakdown(List<Breakdown> breakdownList) {
        this.breakdown = breakdownList;
    }
    
    /**
     * Returns the total count value of the Breakdown Container object. 
     * 
     * This method an integer value of the total count.
     *
     * @return the current total count of this BreakdownContainer
     */
    public int getTotalCount(){
        return this.totalCount;
    }
    
    /**
     * Sets totalCount attribute to parameter input 
     * 
     * @param totalCount a number to be the new totalCount
     */
    public void setTotalCount(int totalCount){
        this.totalCount = totalCount;
    }  
}
