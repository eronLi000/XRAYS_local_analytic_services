/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;

 
public class LocationLookUpDAO {
    
    /**
     * retrieves number of rows input into database from llu csv
     * @return String of count
     */
    public static String retriveRowCount() {
        try{
           Connection conn = null;
            conn = ConnectionManager.getConnection();
            PreparedStatement truncateStmt = conn.prepareStatement("select count(*) from locationlookup");
            ResultSet rs = truncateStmt.executeQuery();
            String result = "0";
            if(rs.next()){
                result = rs.getString(1);
            }
            conn.close(); 
            return result;
        } catch(Exception e){
            return "0";
        }
    }

    /**
     * insert llu data into db
     * @param stringArr, String[]
     * @throws Exception for db connection
     */
    public void insertLocLookUp(String[] stringArr) throws Exception {
        //------------------- Creates the Database Connection to mysql -------------------------------
        Connection conn = null;
        conn = ConnectionManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement("insert into locationlookup values (?, ?)");
        //PreparedStatement prikeyCheckStmt = conn.prepareStatement("select  from demographics where email = ? and mac_address = ? limit 1");

        for (int i = 0; i < stringArr.length; i++) {
            stmt.setString(i + 1, stringArr[i]);
        }
        stmt.executeUpdate();
        conn.close();
    }

    /**
     * clears all data from llu table
     * @throws Exception for db connection
     */
    public static void truncateLluc() throws Exception {
        Connection conn = null;
        conn = ConnectionManager.getConnection();
        PreparedStatement truncateStmt = conn.prepareStatement("truncate table locationlookup");
        truncateStmt.execute();
        conn.close();
    }

    /**
     * uses batch upload method to insert llu data into db for bootstrap query
     * @param path where the user stores the file, String
     * @throws Exception for db connection
     */
    public static void batchLluUpload(String path) throws Exception {

        String actualPath = path + "\\final_upload\\location-lookup_final.csv";
        String replacedPath = actualPath.replace("\\", "//");

        Connection conn = null;
        conn = ConnectionManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement("LOAD DATA LOCAL INFILE '" + replacedPath + "' INTO TABLE locationlookup\n"
                + "FIELDS TERMINATED BY ',' \n"
                + "ENCLOSED BY '\"' \n"
                + "LINES TERMINATED BY '\\r\\n'\n");
        stmt.execute();
        conn.close();
    }

    /**
     * uses batch upload method to insert llu data into db for additional query
     * @param path where the user stores the file, String
     * @throws Exception for db connection
     */
    public static void addLluUpload(String path) throws Exception {

        String actualPath = path + "\\final_upload\\location-lookup_final.csv";
        String replacedPath = actualPath.replace("\\", "//");

        Connection conn = null;
        conn = ConnectionManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement("LOAD DATA LOCAL INFILE '" + replacedPath + "' REPLACE INTO TABLE locationlookup\n"
                + "FIELDS TERMINATED BY ',' \n"
                + "ENCLOSED BY '\"' \n"
                + "LINES TERMINATED BY '\\r\\n'\n");
        stmt.execute();
        conn.close();
    }

    /**
     * does pk check and ensures that every row is unique
     * @return Hashset of unique rows to be compared with current row, HashSet
     * @throws Exception for db connection
     */
    public static HashSet<String> pkCheck() throws Exception {
        HashSet<String> result = new HashSet<>();
        Connection conn = null;
        conn = ConnectionManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement("select * from locationlookup");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            result.add(rs.getString(1));
        }
        conn.close();
        return result;
    }
}
