package dao;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class LocationDAO {
    
    /**
     * retrieves number of rows input into database from location csv
     * @return String of row count
     */
    public static String retriveRowCount() {
        try{
           Connection conn = null;
            conn = ConnectionManager.getConnection();
            PreparedStatement truncateStmt = conn.prepareStatement("select count(*) from location");
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
     * insert location data into db
     * @param stringArr, String[]
     * @throws Exception when performing a date format or db connection
     */
    public void insertLoc(String[] stringArr) throws Exception {
        //------------------- Creates the Database Connection to mysql -------------------------------
        Connection conn = null;
        conn = ConnectionManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement("insert into location values (?, ?, ?)");

        //------- Check if duplicate row exists based on timestamp and mac-address ------------------
        PreparedStatement priKeyCheckStmt = conn.prepareStatement("select timestamp, mac_address from location where timestamp = ? and mac_address = ? limit 1");

        //------- Check if locationID exists in locationlookup table ------------------
        PreparedStatement foreignKeyCheckStmt = conn.prepareStatement("select * from locationlookup where location_id = ? limit 1");

        String input_format = "dd/MM/yyyy HH:mm";
        DateFormat df = new SimpleDateFormat(input_format);
        //Prevents simpleDateFormat from accepting invalid dates i.e 2017-13-12T24:00:00
        df.setLenient(false);

        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        df.setLenient(false);
        String formattedDate = targetFormat.format(df.parse(stringArr[0]));

        stmt.setString(1, formattedDate);
        stmt.setString(2, stringArr[1]);
        stmt.setString(3, stringArr[2]);

        stmt.executeUpdate();
        conn.close();
    }

    /**
     * does fk check and ensures that every row is unique
     * @return Hashset of unique rows to be complimented with current row, HashSet
     * @throws Exception for db connection
     */
    public static HashMap<String, String> fkCheck() throws Exception {
        HashMap<String, String> result = new HashMap<>();
        Connection conn = null;
        conn = ConnectionManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement("select * from locationlookup");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            result.put(rs.getString(1), rs.getString(2));
        }
        conn.close();
        return result;
    }

    /**
     * clears all data from location table
     * @throws Exception for db connection
     */
    public static void truncateLoc() throws Exception {
        Connection conn = null;
        conn = ConnectionManager.getConnection();
        PreparedStatement truncateStmt = conn.prepareStatement("truncate table location");
        truncateStmt.execute();
        conn.close();
    }

    /**
     * uses batch upload method to insert location data into db for bootstrap query
     * @param path where the user stores the file, String
     * @throws Exception for db connection
     */
    public static void batchLocUpload(String path) throws Exception {

        String actualPath = path + "\\final_upload\\location_final.csv";
        String replacedPath = actualPath.replace("\\", "//");

        Connection conn = null;
        conn = ConnectionManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement("LOAD DATA LOCAL INFILE '" + replacedPath + "' INTO TABLE location\n"
                + "FIELDS TERMINATED BY ',' \n"
                + "ENCLOSED BY '\"' \n"
                + "LINES TERMINATED BY '\\r\\n'\n");
        stmt.execute();
        conn.close();
    }

    /**
     * uses batch upload method to insert location data into db for additional query
     * @param path where the user stores the file, String
     * @throws Exception for db connection
     */
    public static void addLocUpload(String path) throws Exception {

        String actualPath = path + "\\final_upload\\location_final.csv";
        String replacedPath = actualPath.replace("\\", "//");

        Connection conn = null;
        conn = ConnectionManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement("LOAD DATA LOCAL INFILE '" + replacedPath + "' REPLACE INTO TABLE location\n"
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
        PreparedStatement stmt = conn.prepareStatement("select * from location");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String time = rs.getString(1);
            time = time.substring(0, 19);
            String compositeKey = time + rs.getString(2);
           
            result.add(compositeKey);
        }
        conn.close();
        return result;
    }
}
