/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class TopKPopularPlacesDAO {

    /**
     * retrieve the popular places within a specified query window from database.
     * This method returns TreeMap where "Integer" is the number of people inside the list of place
     * 
     * @param  datetime  datetime taken from user side, in Timestamp type
     * @return TreeMap key is the number of people inside the the semantic place, List store a list of semantic place has same number of people inside, return null if there is no result
     *
     */
    
    public static TreeMap<Integer,List<String>> retrievePopularPlace(Timestamp datetime){
        Connection conn = null;
        PreparedStatement retrieveStmt = null;
        ResultSet rs = null;
        TreeMap<Integer,List<String>> toReturn = null;
 
        try {
            conn = ConnectionManager.getConnection();
            //arraylist
            retrieveStmt = conn.prepareStatement("select count(*) as CountMacAddress, semantic_place from locationlookup luk inner join (SELECT l.timestamp, l.mac_address, l.location_id FROM location l, (SELECT mac_address, max(timestamp) as maxTimeStamp FROM location WHERE timestamp >= ? - interval 15 minute and timestamp <? GROUP BY mac_address) as temp WHERE l.mac_address = temp.mac_address AND l.timestamp = temp.maxTimeStamp) as temp2 on luk.location_id = temp2.location_id group by luk.semantic_place order by CountMacAddress desc");
            retrieveStmt.setTimestamp(1,datetime);
            retrieveStmt.setTimestamp(2,datetime);
            rs = retrieveStmt.executeQuery();
            
            if(!rs.isBeforeFirst()){
                return toReturn;
            }    
            
            toReturn = new TreeMap<>();

            while (rs.next()) {
                Integer count = (Integer) (rs.getInt(1));
                List<String> places = new ArrayList<>();
                if (toReturn.containsKey(count)) {
                    places = toReturn.get(count);
                }
                places.add(rs.getString(2));
                toReturn.put(count, places);
            }        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            ConnectionManager.close(conn, retrieveStmt, rs);
        }
        return toReturn;
    }
}