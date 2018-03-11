/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.List;
 
public class NextPlaceContainer {
    private String status;
    private Integer total_users;
    private Integer total_next_place_users;
    private List<NextPlace> results;
    private List<String> messages;

    /**
     * Creates a new NextPlaceContainer Object for when results are returned
     * 
     * This method creates a new NextPlaceContainer Object.
     *
     * @param status the status of the next place query
     * @param totalUser the total count of users
     * @param totalNextPlaceUser the count of total next place users 
     * @param results the list of NextPlace objects retrieved
     */
    public NextPlaceContainer(String status, int totalUser, int totalNextPlaceUser, List<NextPlace> results) {
        this.status = status;
        this.total_users = totalUser;
        this.total_next_place_users = totalNextPlaceUser;
        this.results = results;
        this.messages = null;
    }
    
    /**
     * Creates a new NextPlaceContainer Object for when there are error messages returned
     * 
     * This method creates a new NextPlaceContainer Object to be used in the breakdown list.
     * 
     * @param message the list of error messages retrieved
     * @param status -the status of the next places query
     */
    public NextPlaceContainer(List<String> message, String status) {
        this.status = status;
        this.messages = message;
        this.total_next_place_users = null;
        this.total_users = null;
    }

    /**
     * Returns the status of the NextPlaces Container object. 
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
     * @param status -a string value with either "success" or "error"
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns the total number of users 
     * 
     * @return the total number of users
     */
    public int getTotal_users() {
        return total_users;
    }

    /**
     * Sets total_users attribute to parameter input 
     * 
     * @param total_users -a integer value with the count of total users
     */
    public void setTotal_users(int total_users) {
        this.total_users = total_users;
    }
    
    /**
     * Returns the total number of next place users 
     * 
     * @return the total number of next place users 
     */
    public int getTotal_next_place_users() {
        return total_next_place_users;
    }

    /**
     * Sets total_next_place_users attribute to parameter input 
     * 
     * @param total_next_place_users - an integer value with the count of total users
     */
    public void setTotal_next_place_users(int total_next_place_users) {
        this.total_next_place_users = total_next_place_users;
    }

    /**
     * Returns the list of next places of users 
     * 
     * @return results -the list of valid next places
     */
    public List<NextPlace> getResults() {
        return results;
    }

    /**
     * sets the list of valid next place for user
     * @param results -a list of NextPlace
     */
    public void setResults(List<NextPlace> results) {
        this.results = results;
    }

    /**
     * get a list of messages(success or error)
     * @return List
     */
    public List<String> getMessages() {
        return messages;
    }

    /**
     * sets a list of message to be returned to user
     * @param messages -a list of string
     */
    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
    
}