package dao;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DemographicDAO {

    /**
     * to match random 16 character, and generate a token for Json input
     * @return random 16 character
     */
    public static String getSharedSecret(){
        return "nBGhl2XbhBRBpDFj";
    }
    
    /**
     * retrieves number of rows input into database from demographics csv
     * @return String of count
     */
    public static String retriveRowCount() {
        try{
           Connection conn = null;
            conn = ConnectionManager.getConnection();
            PreparedStatement truncateStmt = conn.prepareStatement("select count(*) from demographics");
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
     * retrieveUser Returns a user object of a given username
     * @param username - String value to be searched in database
     * @return User - return null if username is not in the database
     */
    public static User retrieveUser(String username) {

        User user = null;

        /*check if username equals to "admin",if true, return admin user*/
        if (username.equals("admin")) {

            user = new User("sdf", "admin", "pxraysw", "admin@smu.edu.sg", 'M');
            user.retrieveUsernameFromEmail();
            return user;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            //prepared statement to retrieve user from email
            conn = ConnectionManager.getConnection();
            pstmt = conn.prepareStatement("select * from demographics where email like  ? ");
            pstmt.setString(1, username + "%");

            rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                user = null;
            }
            while (rs.next()) {
                user = new User(rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5).charAt(0));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // otherwise, a new user
        } finally {
            ConnectionManager.close(conn, pstmt, rs);
        }
        if (user != null) {
            user.retrieveUsernameFromEmail();
        }
        return user;
    }

    /**
     * insert demographic data into db
     * @param stringArr, String[]
     * @throws Exception for db connection
     */
    public void insertDemo(String[] stringArr) throws Exception {
        //------------------- Creates the Database Connection to mysql -------------------------------
        Connection conn = null;
        conn = ConnectionManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement("insert into demographics values (?, ?, ?, ?, ?)");
        PreparedStatement prikeyCheckStmt = conn.prepareStatement("select email from demographics where email = ? and mac_address = ? limit 1");

        for (int i = 0; i < stringArr.length; i++) {
            stmt.setString(i + 1, stringArr[i]);
        }
        stmt.executeUpdate();
        conn.close();
    }

    /**
     * clears all data from demographics table
     * @throws Exception for db connection
     */
    public static void truncateDemo() throws Exception {
        Connection conn = null;
        conn = ConnectionManager.getConnection();
        PreparedStatement truncateStmt = conn.prepareStatement("truncate table demographics");
        truncateStmt.execute();
        conn.close();
    }

    /**
     * uses batch upload method to insert demographics data into db for bootstrap query
     * @param path where the user stores the file, String
     * @throws Exception for db connection
     */
    public static void batchDemoUpload(String path) throws Exception {

        String actualPath = path + "\\final_upload\\demographics_final.csv";
        String replacedPath = actualPath.replace("\\", "//");

        Connection conn = null;
        conn = ConnectionManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement("LOAD DATA LOCAL INFILE '" + replacedPath + "' INTO TABLE demographics\n"
                + "FIELDS TERMINATED BY ',' \n"
                + "ENCLOSED BY '\"' \n"
                + "LINES TERMINATED BY '\\r\\n'\n");
        stmt.execute();
        conn.close();
    }
    
    /**
     * uses batch upload method to insert demographics data into db for additional query
     * @param path where the user stores the file, String
     * @throws Exception for db connection
     */
    public static void addDemoUpload(String path) throws Exception {

        String actualPath = path + "\\final_upload\\demographics_final.csv";
        String replacedPath = actualPath.replace("\\", "//");

        Connection conn = null;
        conn = ConnectionManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement("LOAD DATA LOCAL INFILE '" + replacedPath + "' REPLACE INTO TABLE demographics\n"
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
        PreparedStatement stmt = conn.prepareStatement("select * from demographics");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String compositeKey = rs.getString(1) + rs.getString(4);
            result.add(compositeKey);
        }
        conn.close();
        return result;
    }
}
