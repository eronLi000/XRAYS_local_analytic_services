/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.ArrayList;
import java.util.List;

public class GroupContainer {

    private String status;
    private Integer total_users;
    private Integer total_groups;
    private ArrayList<Group> groups;
    private List<String> messages;

    /**
     *initialize a new GroupContainer object
     * @param status - a string status
     * @param totalUsers - a int of totalUsers
     * @param groups - a list of groups
     */
    public GroupContainer(String status, int totalUsers, ArrayList<Group> groups) {
        this.status = status;
        this.total_users = totalUsers;
        this.total_groups = groups.size();
        this.groups = groups;
        this.messages = null;
    }

    /**
     *initialize a new GroupContainer object
     * @param status - a string status
     * @param messages - a list of string messages
     */
    public GroupContainer(String status, List<String> messages) {
        this.status = status;
        this.messages = messages;
        this.groups = null;
        this.total_users = null;
        this.total_groups = null;
    }
    
    /**
     *get status of container
     * @return status - a string value
     */
    public String getStatus() {
        return status;
    }

    /**
     *get totalUsers
     * @return total_users - an int value
     */
    public int getTotalUsers() {
        return total_users;
    }

    /**
     *get size of group list
     * @return total_groups - the int size of groups
     */
    public int getTotalGroups() {
        return total_groups;
    }

    /**
     *get group list
     * @return groups - a list of groups
     */
    public ArrayList<Group> getGroups() {
        return groups;
    }

    /**
     *get a list of messages
     * @return messages - a list of string message
     */
    public List<String> getMessages(){
        return this.messages;
    }
    
    /**
     * this method set messages of GroupContainer
     * @param messages - a list of string value
     */
    public void setMessages(List<String> messages){
        this.messages = messages;
    }
}

