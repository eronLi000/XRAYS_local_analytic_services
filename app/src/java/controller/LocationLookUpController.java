/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.opencsv.CSVReader;
import dao.LocationLookUpDAO;
import entity.BSError;
import entity.LocationLookUp;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

 
public class LocationLookUpController {

    int successfulRowCounter = 0;

    /**
     * Executes file and param validation
     * @param path which the user stored the csv file in, String
     * @param bootstrap checks if this is a validation from a bootstrap or additional file request, boolean
     * @return uploading to db and returning of errors if any, ArrayList of BSError
     * @throws Exception mainly for CSVGenerator
     */
    public ArrayList<BSError> validateLocationLookUp(String path, boolean bootstrap) throws Exception {
        String csvFile = path + "//location-lookup.csv";
        CSVReader reader = null;
        
        //collates the validated llu data in to a hashmap, that is used for the system's data base
        HashMap<String, LocationLookUp> llucHashMap = new HashMap<>();
        //pk check
        HashSet<String> lluPKCheck = LocationLookUpDAO.pkCheck();

        //Created arraylist to store all error messages
        ArrayList<BSError> errorArr = new ArrayList<>();
        
        try{
            reader = new CSVReader(new FileReader(csvFile));
            String[] stringArr;
            int lineCounter = 1;
            reader.readNext();
            
            while((stringArr = reader.readNext()) != null){
                lineCounter++;
                
                //ensure that there is 2 column per row
                if (stringArr.length == 2) {

                    boolean validRow = true;
                    ArrayList<String> errorMessageArr = new ArrayList<>();

                    for (int i = 0; i < stringArr.length; i++) {
                        stringArr[i] = stringArr[i].trim();
                        if (stringArr[i].equals("") || stringArr[i] == null) {
                            validRow = false;
                            if(i == 0){
                                errorMessageArr.add("location-id is blank"); 
                            }
                            if(i == 1){
                                errorMessageArr.add("semantic-place is blank"); 
                            }
                        }
                    }

                    LocationLookUp locLookUp = new LocationLookUp();
                    if(validRow){
                        //------- Check if location ID is valid for length 10 and using isValidLocId checker ---------
                        if (!(locLookUp.isValidLocId(stringArr[0]))) {
                            validRow = false;
                            errorMessageArr.add("invalid location id");

                            //------- Check if semantic place is valid and using isValidSemanticPlace checker ------------------
                        } 
                        if (!(locLookUp.isValidSemanticPlace(stringArr[1]))) {
                            validRow = false;
                            errorMessageArr.add("invalid semantic place");
                        }
                    }
                    //------- if the row is valid, add it into the database -------------------------------
                    if (validRow) {
                        String primaryKey = stringArr[0];
                        if (llucHashMap.get(primaryKey) != null || lluPKCheck.contains(primaryKey)) {
                            errorMessageArr.add("duplicate row");
                            BSError errObj = new BSError("location-lookup.csv", lineCounter, errorMessageArr);
                            errorArr.add(errObj);
                        } else {
                            LocationLookUp locEntry = new LocationLookUp(stringArr[0], stringArr[1]);
                            llucHashMap.put(primaryKey, locEntry);
                            successfulRowCounter++;
                        }

                    } else {
                        //------- if the row is invalid, add it to the error log array --------------------
                        BSError errObj = new BSError("location-lookup.csv", lineCounter, errorMessageArr);
                        errorArr.add(errObj);
                    }

                } else {

                    ArrayList<String> notTwoFieldsMsg = new ArrayList<>();
                    notTwoFieldsMsg.add("rows contain less than or more than 2 fields");
                    BSError notTwoFields = new BSError("location-lookup.csv", lineCounter, notTwoFieldsMsg);
                    errorArr.add(notTwoFields);
                }
                
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        
        //generates validated hashmap of llu data into a csv to be thrown into system's database
        csvGenerator(path, llucHashMap);

        if (bootstrap) {
            LocationLookUpDAO.batchLluUpload(path);
        } else {
            LocationLookUpDAO.addLluUpload(path);
        }

        return errorArr;
    }

    
    /**
     * generates a new csv file with values from a HashMap
     * @param path which the user stored the csv file in, String
     * @param input validated HashMap with correct dataset to be thrown into db, HashMap
     * @throws Exception for FileWriter
     */
    public void csvGenerator(String path, HashMap input) throws Exception {
        FileWriter writer;
        writer = new FileWriter(path + "//final_upload//location-lookup_final.csv", false);
        Iterator iter = input.entrySet().iterator();
        while (iter.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) iter.next();
            LocationLookUp lluc = (LocationLookUp) pair.getValue();
            writer.write(lluc.getLocId());
            writer.write(",");
            writer.write(lluc.getSemanticPlace());
            writer.write("\r\n");
        }
        writer.flush();
        writer.close();

    }

    /**
     * retrieves numerical value of the number of successful row that is in the db
     * @return numerical value of the number of successful row that is in the db, int
     */
    public int getSuccessNum() {
        return successfulRowCounter;
    }
}
