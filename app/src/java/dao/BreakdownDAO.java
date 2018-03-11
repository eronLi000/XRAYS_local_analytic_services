/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Demographic;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class BreakdownDAO {
    
    /**
     * Returns a list of @link Demographic objects. 
     * 
     * This method connects to the database and retrieves all users who had a location update
     * within the 15 minutes window that was queried.
     *
     * @param datetime a Timestamp object of the user's query time
     * @return a list of Demographic objects
     */
    public static List<Demographic> retrieveDemographicList(Timestamp datetime){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Demographic> demographicList = null;
        
        try {
            conn = ConnectionManager.getConnection();
            //retrieve all the users who had a location update in the 15 min window from location table but is also inside demographic table. this is done by inner joining demographics and
            //a temp table that selects all the distinct mac address from location table where time is within the 15 min window
            pstmt = conn.prepareStatement("SELECT D.email, D.gender FROM demographics D inner join (select distinct mac_address from location where timestamp < ? and timestamp >= ? - interval 15 minute) as temp ON temp.mac_address = D.mac_address");
            pstmt.setTimestamp(1, datetime);
            pstmt.setTimestamp(2, datetime);
            
            rs = pstmt.executeQuery();
            
            //return null to controller if no results were obtained from the query
            if(!rs.isBeforeFirst()){
                return null;
            }
            
            demographicList = new ArrayList<>();
            
            //create new demographic objects for each row returned and add to demographicList
            while (rs.next()) {
                String email = rs.getString(1);
                String gender = rs.getString(2);
                demographicList.add(new Demographic(email, gender));
            }  
                        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            ConnectionManager.close(conn, pstmt, rs);
        }
        return demographicList;
    }
}
