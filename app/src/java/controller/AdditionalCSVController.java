package controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.DemographicDAO;
import entity.BSContainer;
import entity.BSError;
import entity.BSSuccess;
import entity.DemoSuccess;
import entity.JsonContainer;
import entity.LocSuccess;
import is203.JWTException;
import is203.JWTUtility;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.zip.*;
import java.util.ArrayList;
import java.util.Collections;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;


@WebServlet(urlPatterns = {"/AdditionalCSVController"})
@MultipartConfig

public class AdditionalCSVController extends HttpServlet {

    final int BUFFER = 64;
    
    /**
     * Processes requests for both HTTP <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //If notJson is not null, means it came from UI page
        //instantiate a session object
        HttpSession session = request.getSession(false);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String notJson = request.getParameter("notJson");
        //creates an arraylist for storing the message for invalid user input
        ArrayList<String> messages = new ArrayList<>();
        boolean blankOrMissingToken = false;
        boolean validToken = true;
        boolean validFilepart = true;
        
        Part filePart = request.getPart("bootstrap-file");
        String token = (String) request.getParameter("token");

        //--------- Start Handler for token validation ----------
        if (notJson == null) {
            //----- since notJson == null means this is a json request
                        
             //check if token is missing or blank or filePart is missing or blank
            if (token == null || filePart == null || token.equals("") || filePart.equals("")){
                //Creates the json return for status error
                if(token == null){
                    blankOrMissingToken = true;
                    messages.add("missing token");
                }
                else if (token.equals("")){
                    blankOrMissingToken = true;
                    messages.add("blank token");
                }
                
                if (filePart == null){
                    messages.add("missing bootstrap-file");
                }
                else if (filePart.equals("")){
                    messages.add("blank bootstrap-file");
                }
                
                //if token is not missing or blank then check if its valid
                if(!blankOrMissingToken){
                    try {
                        token = token.trim();
                        //Verifies the token using shared secret
                        if (JWTUtility.verify(token, DemographicDAO.getSharedSecret()) == null){
                            validToken = false;
                        }            

                    } catch (JWTException e) {
                        validToken = false;
                    }
                }
                
                //token is not valid, add message
                if(!validToken){
                    messages.add("invalid token");
                }
                
                //sort mesages by ascending order
                Collections.sort(messages);
                JsonContainer jsc = new JsonContainer("error", messages);
                messages = new ArrayList<>();
                String json = gson.toJson(jsc);
                
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/bootstrap");
                view.forward(request, response);
                return;
            }
            
            //check if token is invalid after doing all the missing and blank checks
            token = token.trim();
            try {
                //Verifies the token using shared secret
                if (JWTUtility.verify(token, DemographicDAO.getSharedSecret()) == null) {
                    validToken = false;
                }

            } catch (JWTException e) {
                validToken = false;
            }
            
            //token is invalid
            if(!validToken){
                messages.add("invalid token");
                
                Collections.sort(messages);
                JsonContainer jsc = new JsonContainer("error", messages);
                messages = new ArrayList<>();
                String json = gson.toJson(jsc);

                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/bootstrap");
                view.forward(request, response);
                return;
            }
        }
        //--------- End Handler for token validation ----------
        
        //get inputstream for filePart, which is a sequence of data
        InputStream fileContentCheck = filePart.getInputStream();
        InputStream fileContent = filePart.getInputStream();

        //---------- Start creating of directory for csv ----------
        //Check if /tmp/addFiles folder exist
        String absoluteDiskPath = ("/tmp/addFiles");
        File folderCheck = new File(absoluteDiskPath);
        //if folder does not eist
        if (!folderCheck.exists()) {
            folderCheck.mkdirs();
        }

        //Check if /tmp/addFiles/final_upload exist
        String finalUploadPath = ("/tmp/addFiles/final_upload");
        File finalUploadCheck = new File(finalUploadPath);
        //if path does not exist
        if (!finalUploadCheck.exists()) {
            finalUploadCheck.mkdirs();
        }

        //---------- End creating of directory for csv ----------
        //Check if file is of type zip
        if (filePart.getContentType().equals("application/octet-stream") 
            || filePart.getContentType().equals("application/zip")
            ||filePart.getContentType().equals("application/x-zip-compressed")
            || filePart.getContentType().equals("application/x-zip")
            ||filePart.getContentType().equals("application/x-compress")
            || filePart.getContentType().equals("multipart/x-zip")
            ||filePart.getContentType().equals("application/x-compressed")) {

            //-----Start zip file checker-----
            int counter = 0;
            boolean invalidFile = false;

            try {
                //create zip input stream to iterate over all the files in zip
                ZipInputStream zisCheck = new ZipInputStream(new BufferedInputStream(fileContentCheck));
                ZipEntry fileChecker;

                //check if file contains 2 or 1 csv and they are properly named
                while ((fileChecker = zisCheck.getNextEntry()) != null) {
                    
                    //if file name equals demographics.csv
                    if (fileChecker.getName().equals("demographics.csv")) {
                        counter++;
                        //if file name equals location.csv
                    } else if (fileChecker.getName().equals("location.csv")) {
                        counter++;
                    } else {
                        invalidFile = true;
                        counter++;
                    }
                }
                // if there are more than 2 files or its not properly named
                if (counter > 2 || invalidFile) {

                    //--------- Start Handler for Invalid File ----------
                    if (notJson == null) {
                        //----- since notJson == null means this is a json request

                        //Creates the message for the json return
                        ArrayList<String> message = new ArrayList<>();
                        message.add("invalid bootstrap-file");

                        //Creates the json return for status that has error
                        Collections.sort(message);
                        JsonContainer jsc = new JsonContainer("error", message);
                        gson = new GsonBuilder().setPrettyPrinting().create();
                        String json = gson.toJson(jsc);

                        //Set the json return to an attribute and forward
                        request.setAttribute("json", json);
                        RequestDispatcher view = request.getRequestDispatcher("json/update");
                        view.forward(request, response);

                    } else {
                        session.setAttribute("fail", "It seems that the contents in the zip file are not what we expected.");
                        response.sendRedirect("additional_csv.jsp");
                    }
                    //--------- End Handler for Invalid File  ----------

                }
                zisCheck.close();

            } catch (Exception e) {

                //--------- Start Handler for Exception Handling ----------
                if (notJson == null) {
                    //----- since notJson == null means this is a json request

                    //Creates the message for the json return
                    ArrayList<String> message = new ArrayList<>();
                    message.add(e.getMessage());

                    //Creates the json return for status that has error
                    Collections.sort(message);
                    JsonContainer jsc = new JsonContainer("error", message);
                    gson = new GsonBuilder().setPrettyPrinting().create();
                    String json = gson.toJson(jsc);

                    //Set the json return to an attribute and forward
                    request.setAttribute("json", json);
                    RequestDispatcher view = request.getRequestDispatcher("json/update");
                    view.forward(request, response);

                } else {
                    session.setAttribute("fail", e.getMessage());
                    response.sendRedirect("additional_csv.jsp");
                }
                //--------- End Handler for Exception Handling ----------

            }

            //-----End zipfile check-----
            boolean demo = false;
            boolean loc = false;

            if (!invalidFile) {
                
                //-----Start saving csv to directory-----
                ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fileContent));
                ZipEntry entry;

                //check if file contains 3 csv and they are properly named
                try {
                    //get each of the files in the zip
                    while ((entry = zis.getNextEntry()) != null) {
                        int count;
                        //create an array of byte
                        byte data[] = new byte[BUFFER];

                        FileOutputStream fos = null;

                        //creates file in system directory
                        if (entry.getName().equals("demographics.csv")) {
                            fos = new FileOutputStream(absoluteDiskPath + "//demographics.csv");
                            demo = true;
                        } else if (entry.getName().equals("location.csv")) {
                            fos = new FileOutputStream(absoluteDiskPath + "//location.csv");
                            loc = true;
                        } 

                        //writes content into file
                        BufferedOutputStream dest = null;
                        dest = new BufferedOutputStream(fos, BUFFER);
                        while ((count = zis.read(data, 0, BUFFER)) != -1) {
                            dest.write(data, 0, count);
                        }
                        dest.flush();
                        dest.close();
                    }

                    zis.close();

                    DemographicsController dc = null;
                    ArrayList<BSError> demoError = null;

                    //---------- Start validation, uploading to db and returning of errors if any ----------
                    //if demo file has been uploaded, validate the demo file
                    if (demo) {
                        dc = new DemographicsController();
                        demoError = dc.validateDemo(absoluteDiskPath, true);
                    }

                    LocationController lc = null;
                    ArrayList<BSError> locError = null;
                    
                    //if loc file has been uploaded, validate the loc file
                    if (loc) {
                        lc = new LocationController();
                        locError = lc.validateLocation(absoluteDiskPath, true);
                    }
                    //---------- End validation, uploading to db and returning of errors if any ----------

                    //---------- Start adding errors from individual csv to a main container ----------
                    ArrayList<BSError> errorContainer = new ArrayList<>();
                    int demoErrorCounter = 0;
                    
                    //if demo file has been uploaded, start getting the errors if any
                    if(demo){
                        for (BSError error : demoError) {
                            demoErrorCounter++;
                            errorContainer.add(error);
                        }

                    }
                    int locErrorCounter = 0;
                    
                    //if loc file has been uploaded, start getting the errors if any
                    if(loc){
                        for (BSError error : locError) {
                            locErrorCounter++;
                            errorContainer.add(error);
                        }
                    }
                    
                    //---------- End adding errors from individual csv to a main container ----------

                    //Creates the recordLoadedContainer for BSContainer
                    ArrayList<BSSuccess> recordLoadedContainer = new ArrayList<>();
                    
                    if(demo){
                        recordLoadedContainer.add(new DemoSuccess(dc.getSuccessNum(), null, null));
                    }
                    if(loc){
                        recordLoadedContainer.add(new LocSuccess(null, null, lc.getSuccessNum()));
                    }
                    

                    //If any of the counters have error, create an error container
                    if (demoErrorCounter != 0 || locErrorCounter != 0) {
                        BSContainer bsc = new BSContainer("error", recordLoadedContainer, errorContainer);

                        gson = new GsonBuilder().setPrettyPrinting().create();
                        String json = gson.toJson(bsc);

                        if (notJson == null) {
                            //----- since notJson == null means this is a json request
                            request.setAttribute("json", json);
                        } else {
                            session.setAttribute("errorMessage", json);
                            session.setAttribute("warning", "You have successfully added more CSV files, however, some rows did not make it in!");
                        }

                    } else {
                        BSContainer bsc = new BSContainer("success", recordLoadedContainer);

                        gson = new GsonBuilder().create();
                        String json = gson.toJson(bsc);

                        if (notJson == null) {
                            //----- since notJson == null means this is a json request
                            request.setAttribute("json", json);
                        } else {
                            session.setAttribute("errorMessage", json);
                            session.setAttribute("success", "You have successfully added more CSV files!");
                        }

                    }

                    //--------- Start Handler for system that has CSV added ----------
                    if (notJson == null) {
                        //----- since notJson == null means this is a json request

                        RequestDispatcher view = request.getRequestDispatcher("json/update");
                        view.forward(request, response);

                    } else {
                        response.sendRedirect("additional_csv.jsp");
                    }
                    //--------- End Handler for system that has CSV added ----------

                } catch (Exception e) {

                    //--------- Start Handler for Exception Handling ----------
                    if (notJson == null) {

                        //----- since notJson == null means this is a json request
                        //Creates the message for the json return
                        ArrayList<String> message = new ArrayList<>();
                        message.add(e.getMessage());

                        //Creates the json return for status that has error
                        Collections.sort(message);
                        JsonContainer jsc = new JsonContainer("error", message);
                        gson = new GsonBuilder().setPrettyPrinting().create();
                        String json = gson.toJson(jsc);

                        //Set the json return to an attribute and forward
                        request.setAttribute("json", json);
                        RequestDispatcher view = request.getRequestDispatcher("json/update");
                        view.forward(request, response);

                    } else {
                        session.setAttribute("fail", e.getMessage());
                        response.sendRedirect("additional_csv.jsp");
                    }
                    //--------- End Handler for Exception Handling ----------

                }
            }
        } else {

            //--------- Start Handler for Invalid File ----------
            if (notJson == null) {

                //----- since notJson == null means this is a json request
                //Creates the message for the json return
                ArrayList<String> message = new ArrayList<>();
                message.add("invalid bootstrap-file");

                //Creates the json return for status that has error
                Collections.sort(message);
                JsonContainer jsc = new JsonContainer("error", message);
                gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(jsc);

                //Set the json return to an attribute and forward
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/update");
                view.forward(request, response);

            } else {
                session.setAttribute("fail", "It seems that you have uploaded an invalid file format. Only Zip files are accepted here!");
                response.sendRedirect("additional_csv.jsp");
            }
            //--------- End Handler for Invalid File  ----------

        }
    }
}
