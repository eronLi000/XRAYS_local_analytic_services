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
import java.util.TreeMap;
 
public class AutoGroupDAO {
    
    /**
     * method retrieve userLocation list map within the given time window
     * @param datetime - a datetime in timestamp
     * @return toReturn - a map of userLocation updates within the 15 minutes window
     */
    public static TreeMap<String, ArrayList<UserLocation>> retrieveLocations (Timestamp datetime) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        TreeMap<String, ArrayList<UserLocation>> toReturn = new TreeMap<>();
 
        try {
            conn = ConnectionManager.getConnection();
            //arraylist
            pstmt = conn.prepareStatement("select loc.mac_address, demo.email,loc.timestamp, loc.location_id "
                    + "from location loc left outer join demographics demo on loc.mac_address = demo.mac_address "
                    + "where timestamp < ? and timestamp >= ? - Interval 15 minute order by loc.timestamp");
            pstmt.setTimestamp(1,datetime);
            pstmt.setTimestamp(2,datetime);
            rs = pstmt.executeQuery();
            
            
            if(!rs.isBeforeFirst()){
                return toReturn;
            }    
    
            while (rs.next()) {
                //test
                String macAdd = rs.getString(1);
                ArrayList<UserLocation> locations = new ArrayList<>();
                UserLocation newUpdate = new UserLocation(rs.getString(2),rs.getTimestamp(3),rs.getString(4));
                if (toReturn.containsKey(macAdd)) {
                    locations = toReturn.get(macAdd);
                    locations.add(newUpdate);
                    toReturn.put(macAdd, locations);
                }
                else{
                    locations.add(newUpdate);
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
