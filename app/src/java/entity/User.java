package entity;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class User implements Comparable<User>{
    
    private String email;
    private String mac_address;
    private String name;
    private String username;
    private String password;
    private Character gender = null;
    
    /**
     * initializes a user that takes in mac address and email
     * @param mac_address - mac address
     * @param email - email
     */
    public User(String mac_address, String email) {
        this.mac_address = mac_address;
        this.email =email;
    }
    
    /**
     *initialize a user object that takes in mac address
     * @param mac_address - mac address of user
     */
    public User(String mac_address) {
        this.mac_address = mac_address;
        this.email = "";
    }
    
    /**
     * initialize a new user object that takes in mac address, name, password, email and gender
     * @param mac_address - mac address
     * @param name - name
     * @param password - password
     * @param email - email
     * @param gender - gender
     */
    public User(String mac_address, String name, String password, String email, char gender) {
        this.mac_address = mac_address;
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
    }
    
    /**
     * checks if two user's have the same mac address
     * @param another - another user object
     * @return - true if this user's mac address is equal to the other user's mac address
     */
    public boolean isSameUserAs(User another) {
        return this.mac_address.equals(another.getMacAddress());
    }
    
    /**
     *gets the username from the email attribute by removing the year and anything after the @ sign
     */
    public void retrieveUsernameFromEmail() {
        int indexOfAt = email.indexOf('@');
        this.username = email.substring(0, indexOfAt);
    }

    /**
     * gets the username 
     * @return username of type string
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * gets password
     * @return password of type string
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * get mac address
     * @return mac address of type string
     */
    public String getMacAddress() {
        return mac_address;
    }

    /**
     * get name
     * @return name of type string
     */
    public String getName() {
        return name;
    }

    /**
     * get gender
     * @return gender of type char
     */
    public char getGender() {
        return gender;
    }

    /**
     * gets email
     * @return email of type string
     */
    public String getEmail() {
        return email;
    }

    /**
     * sets mac address using given mac address
     * @param mac_address - mac address
     */
    public void setMacAddress(String mac_address) {
        this.mac_address = mac_address;
    }
    
    /**
     * sets name using parameter name
     * @param name - name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * sets email using parameter email
     * @param email - email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * sets password using parameter password
     * @param password - password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * sets gender using parameter gender
     * @param gender - gender
     */
    public void setGender(char gender) {
        this.gender = gender;
    }

    /**
     * sets this user's username using parameter username
     * @param username - username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    /**
     * compares two user objects and returns an integer depending on whether this user is before, after or equal to another user
     * @param o - another user object
     * @return -1 if this user should be before the other user, 1 if this user is after the other user and 0 if they are same position
     */
    public int compareTo(User o) {
        if(this.email.length() == 0 && o.getEmail().length() == 0){
            return this.mac_address.compareTo(o.getMacAddress());
        }
        else if (this.email.length() == 0){
            return 1;
        }
        else if (o.email.length() == 0){
            return 1;
        }
        else{
            if(this.email.compareTo(o.getEmail()) == 0){
                return this.mac_address.compareTo(o.getMacAddress());
            }
            else{
                return this.email.compareTo(o.getEmail());
            }
        }
    }
}
