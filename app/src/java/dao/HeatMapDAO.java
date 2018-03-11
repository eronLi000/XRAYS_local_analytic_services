/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.HeatMapRow;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;


public class HeatMapDAO {

    /**
     * retrieves all semantic place from the selected floor from with in 15min of query(exclusive)
     * @param level, int
     * @return all semantic place of queried floor, TreeMap
     */
    public static TreeMap<String, HeatMapRow> retrieveAllSemanticPlaces(int level) {
        //connecting to sql database
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        //return result of this method to be stored in a tree map
        TreeMap<String, HeatMapRow> semanticPlaceHashMap = new TreeMap<>();

        try {
            conn = ConnectionManager.getConnection();
            if (level == 0) {
                //prepared statement to retrieve all semantic place only in basement 
                pstmt = conn.prepareStatement("SELECT DISTINCT semantic_place "
                        + "FROM locationlookup "
                        + "WHERE semantic_place LIKE '%B1%'"
                        + "ORDER BY semantic_place;");
            } else {
                //prepared statement to retrieve all semantic place only in queried floor
                pstmt = conn.prepareStatement("SELECT DISTINCT semantic_place "
                        + "FROM locationlookup "
                        + "WHERE semantic_place LIKE ?"
                        + "ORDER BY semantic_place;");
                pstmt.setString(1, "%L" + level + "%");
            }

            rs = pstmt.executeQuery();
            
            //put all sementic place found in queried level into a hash map
            while (rs.next()) {
                semanticPlaceHashMap.put(rs.getString(1), new HeatMapRow(rs.getString(1), 0));
            }

            return semanticPlaceHashMap;

        } catch (SQLException e) {
            //e.printStackTrace();
        } finally {
            ConnectionManager.close(conn, pstmt, rs);
        }
        
        return semanticPlaceHashMap;
    }

    /**
     * Generates heatmap results within 15min before the queried timing(exclusive), in table form
     * @param inputDateTime, java.sql.Timestamp
     * @param level, int
     * @param blankSemanticPlaces, TreeMap
     * @return heatmap results, ArrayList
     */
    public static ArrayList<HeatMapRow> retrieveHeatMap(java.sql.Timestamp inputDateTime, int level, TreeMap<String, HeatMapRow> blankSemanticPlaces) {
        //connecting to sql database
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        ArrayList<HeatMapRow> result = new ArrayList<>();
        TreeMap<String, HeatMapRow> editedSemanticPlaces = blankSemanticPlaces;

        try {
            conn = ConnectionManager.getConnection();
            //check if level is B1
            if (level == 0) {
                //retrieves number of mac address within the queried timeframe and all semantic place in B1
                pstmt = conn.prepareStatement("SELECT semantic_place, count(l.mac_address) "
                        + "FROM location l, (SELECT mac_address, max(timestamp) as maxTimeStamp "
                        + "					FROM location "
                        + "					WHERE timestamp >= ? - interval 15 minute and timestamp < ? "
                        + "					GROUP BY mac_address) as temp, locationlookup llu "
                        + "WHERE l.mac_address = temp.mac_address "
                        + "AND l.location_id = llu.location_id "
                        + "AND l.timestamp = temp.maxTimeStamp "
                        + "AND semantic_place LIKE '%B1%' "
                        + "GROUP BY semantic_place;");

                pstmt.setTimestamp(1, inputDateTime);
                pstmt.setTimestamp(2, inputDateTime);

            } else {
                //level 1-5
                //retrieves number of mac address within the queried timeframe and all semantic place in level 1-5
                pstmt = conn.prepareStatement("SELECT semantic_place, count(l.mac_address) "
                        + "FROM location l, (SELECT mac_address, max(timestamp) as maxTimeStamp "
                        + "					FROM location "
                        + "					WHERE timestamp >= ? - interval 15 minute and timestamp < ? "
                        + "					GROUP BY mac_address) as temp, locationlookup llu "
                        + "WHERE l.mac_address = temp.mac_address "
                        + "AND l.location_id = llu.location_id "
                        + "AND l.timestamp = temp.maxTimeStamp "
                        + "AND semantic_place LIKE ? "
                        + "GROUP BY semantic_place;");

                pstmt.setTimestamp(1, inputDateTime);
                pstmt.setTimestamp(2, inputDateTime);
                pstmt.setString(3, "%L" + level + "%");
            }

            rs = pstmt.executeQuery();

            if(!rs.isBeforeFirst()){
                return new ArrayList<>(blankSemanticPlaces.values());
            }
            
            //add semantic place with results from query
            while (rs.next()) {
                editedSemanticPlaces.put(rs.getString(1),new HeatMapRow(rs.getString(1), rs.getInt(2)));
            }

            result = new ArrayList<>(editedSemanticPlaces.values());
            
            return result;

        } catch (SQLException e) {
            System.out.println("sql error encountered" + e);
            //e.printStackTrace();
        } finally {
            ConnectionManager.close(conn, pstmt, rs);
        }
        return result;
    }
    
    /**
     * Retrieves the Loc ID for heatmap_map to compare with
     * @param semanticPlace - a string value
     * @return heatmap results, ArrayList
     */
    public static List<Integer> retrieveLocID(String semanticPlace) {
        //connecting to sql database
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        List<Integer> result = new ArrayList<>();

        try {
            conn = ConnectionManager.getConnection();
            //check if level is B1

                //retrieves location id belonging to semanticplace
                pstmt = conn.prepareStatement("SELECT location_id from locationlookup where semantic_place = ?;");

                pstmt.setString(1, semanticPlace);

            rs = pstmt.executeQuery();

            if(!rs.isBeforeFirst()){
                return new ArrayList<Integer>();
            }
            
            //add semantic place with results from query
            while (rs.next()) {
                result.add(rs.getInt(1));
            }

            
            return result;

        } catch (SQLException e) {
            System.out.println("sql error encountered" + e);
            //e.printStackTrace();
        } finally {
            ConnectionManager.close(conn, pstmt, rs);
        }
        return result;
    }

}