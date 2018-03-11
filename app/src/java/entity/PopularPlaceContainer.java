/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.ArrayList;

 
public class PopularPlaceContainer {
    private String status;
    private ArrayList<PopularPlace> results;
    private ArrayList<String> messages;
    
    /**
     * Creates the PopularPlace container which stores the results and status which will be success given that there is either data/no data
     * @param status this container will only return success
     * @param popularPlaces either empty ArrayList or ArrayList of data
     */
    public PopularPlaceContainer(String status, ArrayList<PopularPlace> popularPlaces) {
        this.status = status;
        this.results = popularPlaces;
        this.messages = null;
    }
    
    /**
     * This container will primarily be used for error messages due to failing the input validation
     * @param message ArrayList of error messages
     * @param status this container will only return error
     */
    public PopularPlaceContainer(ArrayList<String> message, String status) {
        this.status = status;
        this.messages = message;
        this.results = null;
    }
    
    /**
     * gets the status of the query
     * @return String
     */
    public String getStatus() {
        return status;
    }

    /**
     * gets a list of popular place
     * @return ArrayList
     */
    public ArrayList<PopularPlace> getPopularPlaces() {
        return results;
    }
    
    /**
     * get a list of messages(success or error)
     * @return List
     */
    public ArrayList<String> getMessages(){
        return this.messages;
    }
    
    /**
     * sets a list of message to be returned to user
     * @param message for errors
     */
    public void setMessages(ArrayList<String> message){
        this.messages = message;
    }
}