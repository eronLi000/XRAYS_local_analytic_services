/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.ArrayList;

public class JsonContainer {

    String status;
    ArrayList<String> messages;
    String token;

    /**
     * constructor for json container which stores json error messages
     * @param status, String
     * @param messages, ArrayList
     */
    public JsonContainer(String status, ArrayList<String> messages) {
        this.status = status;
        this.messages = messages;
    }
    
    /**
     * constructor for json container which stores json error mesage for token validation
     * @param status, a string value
     * @param token, a String value
     */
    public JsonContainer(String status, String token) {
        this.status = status;
        this.token = token;
    }


}