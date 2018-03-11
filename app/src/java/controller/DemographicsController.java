/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DemographicDAO;
import entity.BSError;
import entity.Demographic;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import com.opencsv.CSVReader;

public class DemographicsController {

    int successfulRowCounter = 0;

    /**
     * Executes file and param validation
     * @param path which the user stored the csv file in, String
     * @param bootstrap checks if this is a validation from a bootstrap or additional file request, boolean
     * @return uploading to db and returning of errors if any, ArrayList
     * @throws Exception if any, mainly coming from csvGenerator
     */
    public ArrayList<BSError> validateDemo(String path, boolean bootstrap) throws Exception {
        
        String csvFile = path + "//demographics.csv";
        
        CSVReader reader = null;
        
        //collates the validated demograhics data in to a hashmap, that is used for the system's data base
        HashMap<String, Demographic> demoHashMap = new HashMap<>();
        //does pkcheck
        HashSet<String> demoPKCheck = DemographicDAO.pkCheck();
        
        //Created arraylist to store all error messages
        ArrayList<BSError> errorArr = new ArrayList<>();
        
        try{
            reader = new CSVReader(new FileReader(csvFile));
            String[] line;
            int lineCounter = 1;
            reader.readNext();

            while ((line = reader.readNext()) != null) {
                lineCounter++;
                
                //ensure that there is 5 column per row
                if (line.length == 5) {

                    boolean validRow = true;
                    ArrayList<String> errorMessageArr = new ArrayList<>();

                    for (int i = 0; i < line.length; i++) {
                        line[i] = line[i].trim();
                        if (line[i] == null || line[i].equals("")) {
                            validRow = false;

                            if(i == 0){
                                errorMessageArr.add("mac-address is blank"); 
                            }
                            if(i == 1){
                                errorMessageArr.add("name is blank"); 
                            }
                            if(i == 2){
                                errorMessageArr.add("password is blank"); 
                            }
                            if(i == 3){
                                errorMessageArr.add("email is blank"); 
                            }
                            if(i == 4){
                                errorMessageArr.add("gender is blank"); 
                            }

                        }
                    }

                    if(validRow){
                        //------- Check if SHA1 is valid for length 40 and using isValidSHA1 checker ---------
                        if (!(Demographic.isValidSHA1(line[0]))) {
                            validRow = false;
                            errorMessageArr.add("invalid mac address");

                            //------- Check if password is > 8 and if there are any white space ------------------
                        }
                        if (!(Demographic.isValidPassword(line[2]))) {
                            validRow = false;
                            errorMessageArr.add("invalid password");

                            //------- Use Email Checker to check if valid email ----------------------------------
                        } 
                        if (!Demographic.isValidEmail(line[3])) {
                            validRow = false;
                            errorMessageArr.add("invalid email");

                            //------- Check if gender is either m/f ignoring case --------------------------------
                        }
                        if (!(Demographic.isValidGender(line[4]))) {
                            validRow = false;
                            errorMessageArr.add("invalid gender");
                        }
                    }



                    //------- if the row is valid, add it into the database -------------------------------
                    if (validRow) {

                        boolean existsInDB = false;

                        String compositeKey = line[0] + line[3];

                        //check if data exists in current database
                        if (demoPKCheck.contains(compositeKey)) {
                            errorMessageArr.add("duplicate row");
                            BSError errObj = new BSError("demographics.csv", lineCounter, errorMessageArr);
                            errorArr.add(errObj);
                            existsInDB = true;
                        }
                        //even though can assume additional file has no duplicates,
                        //we still need to check for duplicate rows within the csv file (for bootstrap)
                        if (demoHashMap.get(compositeKey) != null) {
                            errorMessageArr.add("duplicate row");
                            BSError errObj = new BSError("demographics.csv", lineCounter, errorMessageArr);
                            errorArr.add(errObj);

                            //don't double count for duplicate row
                            successfulRowCounter--;
                        }
                        if (!existsInDB){
                            Demographic demoEntry = new Demographic(line[0], line[1], line[2], line[3], line[4]);
                            demoHashMap.put(compositeKey, demoEntry);

                            successfulRowCounter++;
                        }

                    } else {
                        //------- if the row is invalid, add it to the error log array --------------------
                        BSError errObj = new BSError("demographics.csv", lineCounter, errorMessageArr);
                        errorArr.add(errObj);
                    }

                } else {

                    ArrayList<String> notFiveFieldsMsg = new ArrayList<>();
                    notFiveFieldsMsg.add("rows contain less than or more than 5 fields");
                    BSError notFiveFields = new BSError("demographics.csv", lineCounter, notFiveFieldsMsg);
                    errorArr.add(notFiveFields);
                }
            } 
        }
        catch(IOException e){
            e.printStackTrace();
        }
        
        //generates validated hashmap of demographics data into a csv to be thrown into system's database
        csvGenerator(path, demoHashMap);

        if (bootstrap) {
            DemographicDAO.batchDemoUpload(path);
        } else {
            DemographicDAO.addDemoUpload(path);
        }

        return errorArr;
    }

    /**
     * generates a new csv file with values from a HashMap
     * @param path which the user stored the csv file in, String
     * @param input validated HashMap with correct dataset to be thrown into db, HashMap
     * @throws Exception when using FileWriter
     */
    public void csvGenerator(String path, HashMap input) throws Exception {
        FileWriter writer;
        writer = new FileWriter(path + "//final_upload//demographics_final.csv", false);
        Iterator iter = input.entrySet().iterator();
        while (iter.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) iter.next();
            Demographic demo = (Demographic) pair.getValue();
            writer.write(demo.getMacAdd());
            writer.write(",");
            writer.write(demo.getName());
            writer.write(",");
            writer.write(demo.getPassword());
            writer.write(",");
            writer.write(demo.getEmail());
            writer.write(",");
            writer.write(demo.getGender());
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
