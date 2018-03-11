/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.List;
 
public class CompanionContainer {
    private String status;
    private List<Companion> results;
    private List<String> messages;
    
    /**
     * Creates the companion container which stores the results and status which will be success given that there is either data/no data
     * @param status this container will only return success
     * @param results either empty ArrayList or ArrayList of data
     */
    public CompanionContainer(String status, List<Companion> results) {
        this.status = status;
        this.results = results;
        this.messages = null;
    }
    
    /**
     * This container will primarily be used for error messages due to failing the input validation
     * @param message ArrayList of error messages
     * @param status this container will only return error
     */
    public CompanionContainer(List<String> message, String status) {
        this.status = status;
        this.messages = message;
        this.results = null;
    }
    
    /**
     * 
     * @return status
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * 
     * @return list of companion
     */
    
    public List<Companion> getResults() {
        return results;
    }
    
    /**
     * 
     * @return list of messages
     */
    
    public List<String> getMessages(){
        return this.messages;
    }
    
    /**
     * 
     * @param message sets message for companion container
     */
    
    public void setMessages(List<String> message){
        this.messages = message;
    }
    
    /**
     * 
     * @param results sets the results for companion container
     */

    public void setResults(List<Companion> results) {
        this.results = results;
    }
}
