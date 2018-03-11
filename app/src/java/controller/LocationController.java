/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.opencsv.CSVReader;
import dao.LocationDAO;
import entity.BSError;
import entity.Location;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class LocationController {

    int successfulRowCounter = 0;

    /**
     * Executes file and param validation
     *
     * @param path which the user stored the csv file in, String
     * @param bootstrap checks if this is a validation from a bootstrap or
     * additional file request, boolean
     * @return uploading to db and returning of errors if any, ArrayList BSError
     * @throws Exception mainly for CSVGenerator
     */
    public ArrayList<BSError> validateLocation(String path, boolean bootstrap) throws Exception {

        String csvFile = path + "//location.csv";

        CSVReader reader = null;

        //collates the validated location data in to a hashmap, that is used for the system's data base
        HashMap<String, Location> locHashMap = new HashMap<>();

        //create new HashMap to store latest correct rows and row number (helps in duplicate row error msg)
        HashMap<String, Integer> latestCorrectLocations = new HashMap<>();

        // pk and fk check
        HashSet<String> locPKCheck = LocationDAO.pkCheck();
        HashMap<String, String> fkCheckMap = LocationDAO.fkCheck();

        //Created arraylist to store all error messages
        ArrayList<BSError> errorArr = new ArrayList<>();

        try {
            reader = new CSVReader(new FileReader(csvFile));
            String[] stringArr;
            int lineCounter = 1;
            reader.readNext();

            while ((stringArr = reader.readNext()) != null) {
                lineCounter++;
                //ensure that there is 3 column per row
                if (stringArr.length == 3) {

                    boolean validRow = true;
                    ArrayList<String> errorMessageArr = new ArrayList<>();

                    for (int i = 0; i < stringArr.length; i++) {
                        stringArr[i] = stringArr[i].trim();
                        if (stringArr[i].equals("")) {
                            validRow = false;
                            if (i == 0) {
                                errorMessageArr.add("timestamp is blank");
                            }
                            if (i == 1) {
                                errorMessageArr.add("mac-address is blank");
                            }
                            if (i == 2) {
                                errorMessageArr.add("location-id is blank");
                            }
                        }
                    }

                    if (validRow) {
                        //------- Check if location ID exists in location lookup table —-------
                        if (fkCheckMap.get(stringArr[2]) == null) {
                            validRow = false;
                            errorMessageArr.add("invalid location");
                        }
                        //------- Check if mac-address is alphanumeric and 40 digits long —----------------
                        if (!(Location.isValidMacAddress(stringArr[1]))) {
                            validRow = false;
                            errorMessageArr.add("invalid mac address");
                        }
                        //------- Check if timestamp is valid forrmat —-------
                        if (!(Location.isValidTimestamp(stringArr[0]))) {
                            validRow = false;
                            errorMessageArr.add("invalid timestamp");
                        }
                    }

                    //------- if the row is valid, add it into the database —---------------------------—
                    if (validRow) {
                        String compositeKey = stringArr[0] + stringArr[1];

                        boolean existsInDB = false;

                        //check if data exists in current database
                        if (locPKCheck.contains(compositeKey)) {
                            errorMessageArr.add("duplicate row");
                            BSError errObj = new BSError("location.csv", lineCounter, errorMessageArr);
                            errorArr.add(errObj);
                            existsInDB = true;
                        }
                        //check for duplicate rows within the csv file (accounts for both additionalData & bootstrap
                        if (locHashMap.get(compositeKey) != null) {
                            errorMessageArr.add("duplicate row");

                            //get previous correct row number to print out error message
                            int previousCorrectRowNum = latestCorrectLocations.get(compositeKey);
                            BSError errObj = new BSError("location.csv", previousCorrectRowNum, errorMessageArr);
                            errorArr.add(errObj);

                            //don't double count for duplicate row
                            successfulRowCounter--;
                        }
                        if (!existsInDB) {
                            Location locEntry = new Location(stringArr[0], stringArr[1], stringArr[2]);

                            //update hashmap with latest correct row (i.e. current row being checked)
                            locHashMap.put(compositeKey, locEntry);

                            //since this current row will now be the latest correct row, update the hashmap with new row's line number
                            latestCorrectLocations.put(compositeKey, lineCounter);

                            successfulRowCounter++;
                        }

                    } else {
                        //------- if the row is invalid, add it to the error log array —------------------);
                        BSError errObj = new BSError("location.csv", lineCounter, errorMessageArr);
                        errorArr.add(errObj);
                    }

                } else {

                    ArrayList<String> notThreeFieldsMsg = new ArrayList<>();
                    notThreeFieldsMsg.add("rows contain less than or more than 3 fields");
                    BSError notThreeFields = new BSError("location.csv", lineCounter, notThreeFieldsMsg);
                    errorArr.add(notThreeFields);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //generates validated hashmap of locatiion data into a csv to be thrown into system's database
        csvGenerator(path, locHashMap);

        if (bootstrap) {
            LocationDAO.batchLocUpload(path);
        } else {
            LocationDAO.addLocUpload(path);
        }

        return errorArr;
    }

    /**
     * generates a new csv file with values from a HashMap
     *
     * @param path which the user stored the csv file in, String
     * @param input validated HashMap with correct dataset to be thrown into db,
     * HashMap
     * @throws Exception for FileWriter
     */
    public void csvGenerator(String path, HashMap input) throws Exception {
        FileWriter writer;
        writer = new FileWriter(path + "//final_upload//location_final.csv", false);
        Iterator iter = input.entrySet().iterator();
        while (iter.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) iter.next();
            Location loc = (Location) pair.getValue();
            writer.write(loc.getDateTime());
            writer.write(",");
            writer.write(loc.getMacAdd());
            writer.write(",");
            writer.write(loc.getLocId());
            writer.write("\r\n");
        }
        writer.flush();
        writer.close();

    }

    /**
     * retrieves numerical value of the number of successful row that is in the
     * db
     *
     * @return numerical value of the number of successful row that is in the
     * db, int
     */
    public int getSuccessNum() {
        return successfulRowCounter;
    }
}
