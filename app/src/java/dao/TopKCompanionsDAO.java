/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.UserLocation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class TopKCompanionsDAO {
    
    /**
     *this method will retrieve all distinct mac address from location table ordered by mac address
     * @return List of mac addresses
     */
    public static List<String> retrieveAllMacAddress(){
        List<String> allMacAddress = new ArrayList<>();
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try{
            conn = ConnectionManager.getConnection();
            pstmt = conn.prepareStatement("select distinct mac_address from location order by mac_address");
            
            rs = pstmt.executeQuery();
            
            if(!rs.isBeforeFirst()){
                return null;
            }
            
            //allMacAddress = new ArrayList<>();
            
            while(rs.next()){
                allMacAddress.add(rs.getString(1));
            }
        }
        catch (SQLException e){
            return null;
        }
        finally{
            ConnectionManager.close(conn, pstmt, rs);
        }
        
        return allMacAddress;
    }
    /*retrieve all the locations of the specified mac address in the query window*/

    /**
     *this method will verify if the mac address exists in the location table
     * @param macAddress the locations of the user to be retrieved
     * @return boolean valid of true or false if mac address exists
     */
    
    public static boolean verifyMacAddress(String macAddress){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try{
            conn = ConnectionManager.getConnection();
            pstmt = conn.prepareStatement("select distinct timestamp from location where mac_address = ?");
            pstmt.setString(1, macAddress);
            rs = pstmt.executeQuery();
            
            if(!rs.isBeforeFirst()){
                return false;
            }
                        
            while(rs.next()){
                return true;
            }
        }
        catch (SQLException e){
            return false;
        }
        finally{
            ConnectionManager.close(conn, pstmt, rs);
        }
        
        return false;
    }
    
     /**
     *this method retrieve a list of specified user's locations within the query window
     * will return an empty arrayList of userlcoation if there is no result
     * @param macAddress the locations of the user to be retrieved
     * @param inputDateTime the locations with the inputDateTime-15mins
     * @return List list of specified user's locations within the query window; return empty list if there is result
     */
    
    
    public static List<UserLocation> retrieveUserLocationList(String macAddress, Timestamp inputDateTime){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        List<UserLocation> userLocationList = null;    
        
        try{
            conn = ConnectionManager.getConnection();
            userLocationList = new ArrayList<>();           
            pstmt = conn.prepareStatement("select location_id, timestamp from location where mac_address = ? "
                    + "and timestamp < ? and timestamp >= ? - Interval 15 minute order by timestamp");
            
            pstmt.setString(1, macAddress);
            pstmt.setTimestamp(2, inputDateTime); 
            pstmt.setTimestamp(3, inputDateTime);     
            rs = pstmt.executeQuery();

            // if there is no result, return empty list 
            if(!rs.isBeforeFirst()){
                return new ArrayList<>();
            }
            
            // store the result into list
            while(rs.next()){
                String locationID = rs.getString(1);
                Timestamp timestamp = rs.getTimestamp(2);
                userLocationList.add(new UserLocation(timestamp, locationID));
            }
                       
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
        finally{
            ConnectionManager.close(conn, pstmt, rs);
        }
        return userLocationList;
    }   
    
    /**
     *  this method retrieve a list of specified user's companions' locations within the query window
     * will return an empty map if there is no result
     * @param macAddress the locations of the specified macAddress's potential companions to be retrieved
     * @param datetime  the locations within the inputDateTime-15mins
     * @return TreeMap list of specified user's potential companions' locations within the query window; return empty list if there is result
     */
    public static TreeMap<String, List<UserLocation>> retrieveCompanionsLocations (String macAddress,Timestamp datetime) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        TreeMap<String, List<UserLocation>> toReturn = new TreeMap<>();
 
        try {
            conn = ConnectionManager.getConnection();
            //inner select statement retrieves unique location_ids for all location updates by specified user within 15 mins timeframe
            // != is to make sure that companions doesn't include yourself
            //left outer join is for retrival of all mac address and emails of the respective mac address in demographics
            pstmt = conn.prepareStatement("select loc.mac_address,loc.location_id, loc.timestamp,demo.email "
                    + "from location loc left outer join demographics demo on loc.mac_address = demo.mac_address"
                    + " where timestamp < ? and timestamp >= ? - Interval 15 minute "
                    + "and loc.mac_address != ? order by loc.timestamp");

            pstmt.setTimestamp(1,datetime);
            pstmt.setTimestamp(2,datetime);  
            pstmt.setString(3,macAddress);
   
            rs = pstmt.executeQuery();
            
            //returns empty map
            if(!rs.isBeforeFirst()){
                return toReturn;
            }    
            
            toReturn = new TreeMap<>();
            
            //store the result(key mac add, value location list of mac address) into map
            while (rs.next()) {
                String macAdd = rs.getString(1);
                String locationID = rs.getString(2);
                Timestamp timestamp = rs.getTimestamp(3);
                String email = rs.getString(4);
                List<UserLocation> locations = new ArrayList<>();
                //check if it has current location list of that mac address, can add if not create new list to add
                if (toReturn.containsKey(macAdd)) {
                    locations = toReturn.get(macAdd);
                    locations.add(new UserLocation(email,timestamp,locationID));
                    toReturn.put(macAdd, locations);
                }
                else {
                    locations.add(new UserLocation(email,timestamp,locationID));
                    toReturn.put(macAdd, locations);                    
                }
            }  
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            ConnectionManager.close(conn, pstmt, rs);
        }
        return toReturn;
    }
}