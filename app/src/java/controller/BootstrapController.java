package controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.DemographicDAO;
import dao.LocationDAO;
import dao.LocationLookUpDAO;
import entity.BSContainer;
import entity.BSError;
import entity.BSSuccess;
import entity.DemoSuccess;
import entity.JsonContainer;
import entity.LocLookupSuccess;
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

 
@WebServlet(urlPatterns = {"/BootstrapController"})
@MultipartConfig

public class BootstrapController extends HttpServlet {

    //BUFFER set for BufferedOutputStream, writing of data to CSV
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

        //If there is data from notJson, means it came from UI page
        //instantiate a session object
        HttpSession session = request.getSession(false);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
	//Check if query is a json query
        String notJson = request.getParameter("notJson");
        //creates an arraylist for storing the message for invalid user input
        ArrayList<String> messages = new ArrayList<>();
        boolean blankOrMissingToken = false;
        boolean validToken = true;
        boolean validFilepart = true;
        
        //retrieve input parameters from user
        Part filePart = request.getPart("bootstrap-file");
        String token = (String) request.getParameter("token");

        //--------- Start Handler for token validation ----------
        if (notJson == null) {
            //----- since notJson == null means this is a json request
                        
             //check for missing token
            if (token == null || filePart == null || token.trim().equals("") || filePart.equals("")){
                //Creates the json return for status error
                if(token == null){
                    blankOrMissingToken = true;
                    messages.add("missing token");
                }
                else if (token.trim().equals("")){
                    blankOrMissingToken = true;
                    messages.add("blank token");
                }
                
                if (filePart == null){
                    messages.add("missing bootstrap-file");
                }
                else if (filePart.equals("")){
                    messages.add("blank bootstrap-file");
                }
                
                
                
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
                
                if(!validToken){
                    messages.add("invalid token");
                }
                
                //sorts error messages in order of expected output
                Collections.sort(messages);
                JsonContainer jsc = new JsonContainer("error", messages);
                messages = new ArrayList<>();
                String json = gson.toJson(jsc);
                
                //forward result back to the page which calls this controller, where query is made
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/bootstrap");
                view.forward(request, response);
                return;
            }
            
            token = token.trim();
            try {
                //Verifies the token using shared secret
                if (JWTUtility.verify(token, DemographicDAO.getSharedSecret()) == null) {
                    validToken = false;
                }

            } catch (JWTException e) {
                validToken = false;
            }
            
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
        
        InputStream fileContentCheck = filePart.getInputStream();
        InputStream fileContent = filePart.getInputStream();

        //---------- Start creating of directory for csv ----------
        //Check if bootstrapFiles folder exist
        String absoluteDiskPath = ("/tmp/bootstrapFiles");
        File folderCheck = new File(absoluteDiskPath);
        if (!folderCheck.exists()) {
            folderCheck.mkdirs();
        }

        //Check if bootstrapFiles/final_upload folder exist
        String finalUploadPath = ("/tmp/bootstrapFiles/final_upload");
        File finalUploadCheck = new File(finalUploadPath);
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
            ||filePart.getContentType().equals("application/x-compressed")
            ) {
            //-----Start zip file checker-----
            int counter = 0;
            boolean invalidFile = false;

            try {
                ZipInputStream zisCheck = new ZipInputStream(new BufferedInputStream(fileContentCheck));
                ZipEntry fileChecker;

                //check if file contains 3 csv and they are properly named
                while ((fileChecker = zisCheck.getNextEntry()) != null) {

                    if (fileChecker.getName().equals("demographics.csv")) {
                        counter++;
                    } else if (fileChecker.getName().equals("location.csv")) {
                        counter++;
                    } else if (fileChecker.getName().equals("location-lookup.csv")) {
                        counter++;
                    } else {
                        invalidFile = true;
                        counter++;
                    }
                }
                if (counter != 3 || invalidFile) {

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
                        RequestDispatcher view = request.getRequestDispatcher("json/bootstrap");
                        view.forward(request, response);

                    } else {
                        session.setAttribute("fail", "It seems that the contents in the zip file are not what we expected.");
                        response.sendRedirect("bootstrap.jsp");
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
                    RequestDispatcher view = request.getRequestDispatcher("json/bootstrap");
                    view.forward(request, response);

                } else {
                    session.setAttribute("fail", e.getMessage());
                    response.sendRedirect("bootstrap.jsp");
                }
                //--------- End Handler for Exception Handling ----------

            }

            //-----End zipfile check-----
            if (counter == 3 && !invalidFile) {

                //-----Start saving csv to directory-----
                ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fileContent));
                ZipEntry entry;

                //check if file contains 3 csv and they are properly named
                try {
                    while ((entry = zis.getNextEntry()) != null) {
                        int count;
                        byte data[] = new byte[BUFFER];

                        FileOutputStream fos = null;

                        //creates file in system directory
                        if (entry.getName().equals("demographics.csv")) {
                            fos = new FileOutputStream(absoluteDiskPath + "//demographics.csv");
                        } else if (entry.getName().equals("location.csv")) {
                            fos = new FileOutputStream(absoluteDiskPath + "//location.csv");
                        } else if (entry.getName().equals("location-lookup.csv")) {
                            fos = new FileOutputStream(absoluteDiskPath + "//location-lookup.csv");
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

                    //Truncate all the Tables 
                    DemographicDAO.truncateDemo();
                    LocationDAO.truncateLoc();
                    LocationLookUpDAO.truncateLluc();

                    //---------- Start validation, uploading to db and returning of errors if any ----------
                    DemographicsController dc = new DemographicsController();
                    ArrayList<BSError> demoError = dc.validateDemo(absoluteDiskPath, true);

                    LocationLookUpController lluc = new LocationLookUpController();
                    ArrayList<BSError> locLookUpError = lluc.validateLocationLookUp(absoluteDiskPath, true);

                    LocationController lc = new LocationController();
                    ArrayList<BSError> locError = lc.validateLocation(absoluteDiskPath, true);
                    //---------- End validation, uploading to db and returning of errors if any ----------

                    //---------- Start adding errors from individual csv to a main container ----------
                    ArrayList<BSError> errorContainer = new ArrayList<>();

                    int demoErrorCounter = 0;
                    for (BSError error : demoError) {
                        demoErrorCounter++;
                        errorContainer.add(error);
                    }

                    int locLookUpErrorCounter = 0;
                    for (BSError error : locLookUpError) {
                        locLookUpErrorCounter++;
                        errorContainer.add(error);
                    }

                    int locErrorCounter = 0;
                    for (BSError error : locError) {
                        locErrorCounter++;
                        errorContainer.add(error);
                    }
                    //---------- End adding errors from individual csv to a main container ----------

                    //Creates the recordLoadedContainer for BSContainer
                    ArrayList<BSSuccess> recordLoadedContainer = new ArrayList<>();
                    recordLoadedContainer.add(new DemoSuccess(dc.getSuccessNum(), null, null));
                    recordLoadedContainer.add(new LocLookupSuccess(null, lluc.getSuccessNum(), null));
                    recordLoadedContainer.add(new LocSuccess(null, null, lc.getSuccessNum()));

                    //If any of the counters have error, create an error container
                    if (demoErrorCounter != 0 || locLookUpErrorCounter != 0 || locErrorCounter != 0) {
                        BSContainer bsc = new BSContainer("error", recordLoadedContainer, errorContainer);

                        gson = new GsonBuilder().setPrettyPrinting().create();
                        String json = gson.toJson(bsc);

                        if (notJson == null) {
                            json = json.replace("num_record_loaded", "num-record-loaded");
                            json = json.replace("demographicscsv", "demographics.csv");
                            json = json.replace("location_lookupcsv", "location-lookup.csv");
                            json = json.replace("locationcsv", "location.csv");
                            //----- since notJson == null means this is a json request
                            request.setAttribute("json", json);
                        } else {
                            session.setAttribute("errorMessage", json);
                            session.setAttribute("warning", "You have successfully bootstrap the system, however, some rows did not make it in!");
                        }
                    } else {
                        //Since there are no errors, create a success container
                        BSContainer bsc = new BSContainer("success", recordLoadedContainer);

                        gson = new GsonBuilder().create();
                        String json = gson.toJson(bsc);
                        
                        if (notJson == null) {
                            json = json.replace("num_record_loaded", "num-record-loaded");
                            json = json.replace("demographicscsv", "demographics.csv");
                            json = json.replace("location_lookupcsv", "location-lookup.csv");
                            json = json.replace("locationcsv", "location.csv");
                            //----- since notJson == null means this is a json request
                            request.setAttribute("json", json);
                        } else {
                            session.setAttribute("errorMessage", json);
                            session.setAttribute("success", "You have successfully bootstrap the system!");
                        }

                    }

                    //--------- Start Handler for system that has been Bootstraped ----------
                    if (notJson == null) {
                        //----- since notJson == null means this is a json request

                        RequestDispatcher view = request.getRequestDispatcher("json/bootstrap");
                        view.forward(request, response);

                    } else {
                        response.sendRedirect("bootstrap.jsp");
                    }
                    //--------- End Handler for system that has been Bootstraped ----------

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
                        RequestDispatcher view = request.getRequestDispatcher("json/bootstrap");
                        view.forward(request, response);

                    } else {
                        session.setAttribute("fail", e.getMessage());
                        response.sendRedirect("bootstrap.jsp");
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
                RequestDispatcher view = request.getRequestDispatcher("json/bootstrap");
                view.forward(request, response);

            } else {
		//Set error message to be return and forwards back to jsp
                session.setAttribute("fail", "It seems that you have uploaded an invalid file format. Only Zip files are accepted here!");
                response.sendRedirect("bootstrap.jsp");
            }
            //--------- End Handler for Invalid File  ----------

        }
    }
}
