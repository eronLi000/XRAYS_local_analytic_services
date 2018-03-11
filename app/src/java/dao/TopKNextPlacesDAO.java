/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.UserLocation;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

 
public class TopKNextPlacesDAO {
    
    /**
     * Returns a list of all semantic places in the entire SIS Building
     * 
     * This method connects to the database and retrieves all semantic places found in the SIS building.
     * Returns null if no semantic places are found.
     *
     * @return a list of String objects
     */
    public static List<String> retrieveAllSemanticPlaces(){
        List<String> allSemanticPlaces = new ArrayList<>();
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try{
            conn = ConnectionManager.getConnection();
            pstmt = conn.prepareStatement("select distinct semantic_place from locationlookup order by semantic_place");
            
            rs = pstmt.executeQuery();
            
            if(!rs.isBeforeFirst()){
                return null;
            }
            
            //allSemanticPlaces = new ArrayList<>();
            
            while(rs.next()){
                allSemanticPlaces.add(rs.getString(1));
            }
            
        }
        catch (SQLException e){
            return null;
        }
        finally{
            ConnectionManager.close(conn, pstmt, rs);
        }
        
        return allSemanticPlaces;
    }
    
    /**
     * Returns a map of each user's last location update found in the 15 minutes query window.
     * 
     * This method connects to the database and retrieves all location updates within the 15 minutes window, sorted by ascending timestamp. 
     * If no updates are found, an empty HashMap is returned.
     * If updates are found, new entries are added to the HashMap with mac address as the key and location_id as the value.
     * When this happens, older location updates are replaced with new ones for the same mac address
     * 
     * 
     * Returns null if an SQLException is caught.
     *
     * @param dateTime a Timestamp object of the user's query time
     * @return a Map with mac address as key and location id as value
     */
    public static Map<String, String> retrieveUserLastLocation(Timestamp dateTime){
        Map<String, String> userLastLocation = new HashMap<>();
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try{
            conn = ConnectionManager.getConnection();
            pstmt = conn.prepareStatement("select mac_address, location_id from location where timestamp < ? AND timestamp >= ? - INTERVAL 15 MINUTE order by timestamp");
            pstmt.setTimestamp(1, dateTime);
            pstmt.setTimestamp(2, dateTime);
            
            rs = pstmt.executeQuery();
            
            if(!rs.isBeforeFirst()){
                return userLastLocation;
            }
            
            while(rs.next()){
                String macAddress = rs.getString("mac_address");
                String locationID = rs.getString("location_id");
                
                userLastLocation.put(macAddress, locationID);
                
            }
            
        }
        catch(SQLException e){
            return null;
        }
        finally{
            ConnectionManager.close(conn, pstmt, rs);
        }
        
        return userLastLocation;
    }
    
    /**
     * Returns a list of all valid location ids belonging to the semantic place queried
     * 
     * This method connects to the database and retrieves all location ids found in the location lookup table that are found in the semantic place being queried.
     * If no location ids are found, an empty ArrayList is returned.
     * If location ids are found, they are all added to the ArrayList.
     * 
     * Returns null if an SQLException is caught.
     *
     * @param semanticPlace a string value of the semantic place being queried
     * @return a List with valid corresponding location ids
     */
    public static List<String> retrieveValidLocationIDs(String semanticPlace){
        List<String> validLocationIDs = new ArrayList<>();
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try{
            conn = ConnectionManager.getConnection();
            pstmt = conn.prepareStatement("select distinct location_id from locationlookup where semantic_place = ?");
            
            pstmt.setString(1, semanticPlace);
            
            rs = pstmt.executeQuery();
            
            if(!rs.isBeforeFirst()){
                return validLocationIDs;
            }

            while(rs.next()){
                validLocationIDs.add(rs.getString(1));
            }
            
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
        finally{
            ConnectionManager.close(conn, pstmt, rs);
        }
        
        return validLocationIDs;
    }
    
    /**
     * Returns a boolean, checking if the queried semantic place is valid
     * 
     * This method connects to the database and retrieves all location ids found in the location lookup table that match the semantic place being queried.
     * If no location ids are found, the result is false.
     * If location ids are found, the semantic place exists and the result is true.
     * 
     * Returns false if an SQLException is caught.
     *
     * @param origin a string value of the semantic place being queried
     * @return a List with valid corresponding location ids
     */
    public static boolean verifyOrigin(String origin){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try{
            conn = ConnectionManager.getConnection();
            pstmt = conn.prepareStatement("select location_id from locationlookup where semantic_place = ?");
            pstmt.setString(1, origin);
            rs = pstmt.executeQuery();
        
            if(!rs.isBeforeFirst()){
                return false;
            }
                        
            while(rs.next()){
                return true;
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
        finally{
            ConnectionManager.close(conn, pstmt, rs);
        }
        
        return false;
    }
    
    /**
     * Returns a map containing the last location update within the 15 minutes after the user specified time that is more than 5 minutes, for each user found in the 
     * queried semantic place for the previous 15 minutes.
     * 
     * Returns an empty Map if no valid location updates are found.
     * If location updates are found, add it to the user's userLocationList.
     * 
     * Returns null if an SQLException is caught.
     *
     * @param validUsers a list of all valid users found in previous 15 minutes window
     * @param dateTime the datetime value that user queried
     * @return a Map with user's mac address as string and the corresponding valid location updates as value
     */
    public static Map<String, List<UserLocation>> retrieveNextUserLocations(List<String> validUsers, Timestamp dateTime){        
        Map<String, List<UserLocation>> nextUserLocations = null;
        List<UserLocation> userLocationList = null;
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try{
            conn = ConnectionManager.getConnection();
            
            nextUserLocations = new HashMap<String, List<UserLocation>>();
            
            String macAdd = validUsers.get(0);
            String allMacs = "('" + macAdd;
            
            if (validUsers.size() > 1){
                for(int i = 1; i < validUsers.size(); i++){
                    allMacs += "','" + validUsers.get(i);
                }
            }
            
            allMacs += "')";
                        
            pstmt = conn.prepareStatement("select mac_address, semantic_place, l.location_id, timestamp from location l inner join locationlookup llu on l.location_id = llu.location_id where timestamp >= ? and timestamp < ? + INTERVAL 15 MINUTE and mac_address in " + allMacs + " order by timestamp");
            pstmt.setTimestamp(1, dateTime);
            pstmt.setTimestamp(2, dateTime);

            rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                return nextUserLocations;
            }

            while (rs.next()) {
                String macAddress = rs.getString("mac_address");
                String locationID = rs.getString("location_id");
                Timestamp timestamp = rs.getTimestamp("timestamp");
                String semanticPlace = rs.getString("semantic_place");


                userLocationList = nextUserLocations.get(macAddress);

                if (userLocationList == null) {
                    userLocationList = new ArrayList<>();
                    userLocationList.add(new UserLocation(timestamp, locationID, semanticPlace));
                    nextUserLocations.put(macAddress, userLocationList);
                } else {
                    userLocationList.add(new UserLocation(timestamp, locationID, semanticPlace));
                    nextUserLocations.put(macAddress, userLocationList);
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
        finally{
            ConnectionManager.close(conn, pstmt, rs);
        }
        
        return nextUserLocations;
    }
}
