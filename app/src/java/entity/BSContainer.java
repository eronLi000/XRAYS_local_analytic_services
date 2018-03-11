/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.ArrayList;
 
public class BSContainer {

    String status;
    ArrayList<BSSuccess> num_record_loaded;
    ArrayList<BSError> error;

    /**
     * Container for successful bootstrap, showing details
     * @param status, String
     * @param num_record_loaded, int
     */
    public BSContainer(String status, ArrayList<BSSuccess> num_record_loaded) {
        this.status = status;
        this.num_record_loaded = num_record_loaded;
    }

    /**
     * Container for failed bootstrap, showing details
     * @param status, String
     * @param num_record_loaded, ArrayList of String
     * @param error, ArrayList of BSError
     */
    public BSContainer(String status, ArrayList<BSSuccess> num_record_loaded, ArrayList<BSError> error) {
        this.status = status;
        this.num_record_loaded = num_record_loaded;
        this.error = error;
    }


    /**
     * obtains status of bootstrap query
     * @return status, String
     */
    public String getStatus() {
        return status;
    }

    /**
     * obtain number of records loaded upon bootstrap
     * @return number of records loaded, ArrayList of String
     */
    public ArrayList<BSSuccess> getRecordLoaded() {
        return num_record_loaded;
    }

    /**
     * obtains error messages from bootstrap query
     * @return error messages, ArrayList of BSError
     */
    public ArrayList<BSError> getErrObj() {
        return error;
    }

}
