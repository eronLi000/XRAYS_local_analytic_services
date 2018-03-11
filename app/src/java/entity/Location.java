/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Location {

    private String dateTime;
    private String macAdd;
    private String locID;

    /**
     *initialize a new Location object without any parameters
     */
    public Location() {

    }

    /**
     *initialize a new location object with 3 parameters
     * @param dateTime - timestamp of this object
     * @param macAdd - mac address of location object
     * @param locID - location id of location object
     */
    public Location(String dateTime, String macAdd, String locID) {
        this.dateTime = dateTime;
        this.macAdd = macAdd;
        this.locID = locID;
    }

    /**
     *get the datetime of this object
     * @return timestamp pertaining to the datetime of this object
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     *returns mac address of the object
     * @return mac address 
     */
    public String getMacAdd() {
        return macAdd;
    }

    /**
     *returns location id
     * @return location ID
     */
    public String getLocId() {
        return locID;
    }

    //------------------- Checks for Valid Timestamp (yyyy-MM-dd HH:mm:ss) --------------------

    /**
     *checks if timestamp is valid if it follows yyyy-MM-dd HH:mm:ss format
     * @param dateTime - dateTime that has to be validated
     * @return true if valid timestamp
     */
    public static boolean isValidTimestamp(String dateTime) {
        
        if (dateTime.length()!=19){
            return false;
        }
        
        //String input_format = "dd/MM/yyyy HH:mm:ss";
        String input_format = "yyyy-MM-dd HH:mm:ss";

        DateFormat df = new SimpleDateFormat(input_format);

        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            df.setLenient(false);
            String formattedDate = targetFormat.format(df.parse(dateTime));
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    //************************************ Start Email Checker ***********************************
    //------------------- Checks for SHA1 (AlphaNumeric, 40 char limit) ------------

    /**
     *check if mac address is hexadecimal and 40 characters long
     * @param s - the mac address to be validated
     * @return true if valid mac address
     */
    public static boolean isValidMacAddress(String s) {

        return s.matches("[a-fA-F0-9]{40}");

    }

}
