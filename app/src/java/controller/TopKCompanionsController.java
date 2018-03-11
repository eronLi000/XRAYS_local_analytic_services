/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.DemographicDAO;
import dao.TopKCompanionsDAO;
import entity.Companion;
import entity.CompanionContainer;
import entity.Location;
import entity.UserLocation;
import is203.JWTException;
import is203.JWTUtility;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(name = "TopKCompanionsController", urlPatterns = {"/TopKCompanionsController"})
public class TopKCompanionsController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String notJson = request.getParameter("notJson");
        //creates an arraylist for storing the message for invalid user input
        ArrayList<String> messages = new ArrayList<>();
        boolean blankOrMissingToken = false;
        boolean validToken = true;
        boolean validDate = true;
        boolean validMacAddress = true;
        boolean validK = true;
        
        String macAddress = request.getParameter("mac-address");
        String date = request.getParameter("date");
        String token = (String) request.getParameter("token");
        String k = request.getParameter("k");

        //--------- Start Handler for token validation ----------
        if (notJson == null) {
            //----- since notJson == null means this is a json request

             //check for missing token
            if (token == null || macAddress == null || date == null || token.trim().equals("") || macAddress.trim().equals("") || date.trim().equals("") || (k != null && k.trim().equals(""))){
                //Creates the json return for status error
                if(token == null){
                    blankOrMissingToken = true;
                    messages.add("missing token");
                }
                else if (token.trim().equals("")){
                    blankOrMissingToken = true;
                    messages.add("blank token");
                }
                
                if (macAddress == null){
                    messages.add("missing mac address");
                }
                else if (macAddress.trim().equals("")){
                    messages.add("blank mac address");
                }
                
                if (date == null){
                    messages.add("missing date");
                }
                else if (date.trim().equals("")){
                    messages.add("blank date");
                }
                
                if(k != null && k.trim().equals("")) {
                    messages.add("blank k");
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
                
                Collections.sort(messages);
                
                CompanionContainer companionContainer = new CompanionContainer(messages, "error");
                String json = gson.toJson(companionContainer);

                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/top-k-companions");
                view.forward(request, response);
                return;
            }
            
            token = token.trim();
            date = date.trim();
            macAddress = macAddress.trim();
            
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
                
                CompanionContainer companionContainer = new CompanionContainer(messages, "error");
                String json = gson.toJson(companionContainer);

                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/top-k-companions");
                view.forward(request, response);
                return;
            }
        }
                
        java.sql.Timestamp inputDateTime = null;

        //convert the input datetime of the user into a java.sql.Timestamp object
        try {
            //for json requests, datetime will have a T in between the date and time so have to replace it
            date = date.replace('T', ' ');
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //Prevents simpleDateFormat from accepting invalid dates i.e 2017-13-12T24:00:00
            dateFormat.setLenient(false);
            java.util.Date parsedDate = dateFormat.parse(date);
            inputDateTime = new java.sql.Timestamp(parsedDate.getTime());
        } catch (Exception e) { //this generic but you can control another types of exception
            // look the origin of excption 
            //not a valid date time
            validDate = false;
        }
        
       Integer rank = null;

        if (k == null) {
            rank = 3;
        } else {
            k = k.trim();
            try {
                rank = Integer.parseInt(k);
                if (rank < 0 || rank > 10) {
                    validK = false;
                }
            } catch (NumberFormatException e) {
                validK = false;
            }
        }
        
        if(notJson != null && (macAddress == null || macAddress.length() == 0 )) {
             messages.add("no data found for input datetime");
            
            Collections.sort(messages);
                
            CompanionContainer companionContainer = new CompanionContainer(messages, "success");
            String json = gson.toJson(companionContainer);

            if (notJson == null) {
                //----- since notJson == null means this is a json request

                //Set the json return to an attribute and forward
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/top-k-companions");
                view.forward(request, response);
                return;
            } else {
                session.setAttribute("top_companions_result", json);
                response.sendRedirect("TopKCompanionsForm.jsp");
                return;
            }
        }
        
        // valid macAddress
        if (!Location.isValidMacAddress(macAddress) || !TopKCompanionsDAO.verifyMacAddress(macAddress)) {
            validMacAddress = false;
        }

        if (!validDate || !validK || !validMacAddress) {
            //Adds the message for the json return
            if (!validDate) {
                messages.add("invalid date");
            }
            if (!validK) {
                messages.add("invalid k");
            }
            
            if (!validMacAddress) {
                messages.add("invalid mac address");
            }

            Collections.sort(messages);
                
            CompanionContainer companionContainer = new CompanionContainer(messages, "error");
            String json = gson.toJson(companionContainer);

            if (notJson == null) {
                //----- since notJson == null means this is a json request

                //Set the json return to an attribute and forward
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/top-k-companions");
                view.forward(request, response);
                return;
            } else {
                session.setAttribute("json", json);
                response.sendRedirect("TopKCompanionsForm.jsp");
                return;
            }
            //--------- End Handler for Invalid Input  ----------                              
        }

        //retrieve the locationList of specified mac address
        List<UserLocation> userLocationList = TopKCompanionsDAO.retrieveUserLocationList(macAddress, inputDateTime);

        /*if the size of user location list is null or empty*/
        if (userLocationList == null || userLocationList.isEmpty()) {
            messages.add("no data found for input datetime");
            
            Collections.sort(messages);
                
            CompanionContainer companionContainer = new CompanionContainer(messages, "success");
            String json = gson.toJson(companionContainer);

            if (notJson == null) {
                //----- since notJson == null means this is a json request

                //Set the json return to an attribute and forward
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/top-k-companions");
                view.forward(request, response);
                return;
            } else {
                session.setAttribute("top_companions_result", json);
                response.sendRedirect("TopKCompanionsForm.jsp");
                return;
            }

        } else {

            /*----------------------------set end time for user location------------------------------------*/
            
            userLocationList = UserLocation.setEndTimeForAll(userLocationList, inputDateTime);

            //get locationList of potential companion list
            TreeMap<String, List<UserLocation>> companionUserLocations = TopKCompanionsDAO.retrieveCompanionsLocations(macAddress, inputDateTime);

            /*check if there is no potential companion*/
            if (companionUserLocations.size() == 0) {
                messages.add("no other mac address / potential companions found in input datetime");
                Collections.sort(messages);
                CompanionContainer toReturn = new CompanionContainer(messages, "success");
                toReturn.setResults(new ArrayList<>());
                String json = gson.toJson(toReturn);
                if (notJson == null) {
                    //----- since notJson == null means this is a json request

                    //Set the json return to an attribute and forward
                    request.setAttribute("json", json);
                    RequestDispatcher view = request.getRequestDispatcher("json/top-k-companions");
                    view.forward(request, response);
                    return;
                } else {
                    session.setAttribute("top_companions_result", json);
                    response.sendRedirect("TopKCompanionsForm.jsp");
                    return;
                }
            }

            //set end time of potential companions location list
            TreeMap<String, List<UserLocation>> newCompanionUserLocations = new TreeMap<>();
                    
            for (Map.Entry<String, List<UserLocation>> entry : companionUserLocations.entrySet()) {
                List<UserLocation> companionLocList = entry.getValue();
                
                companionLocList = UserLocation.setEndTimeForAll(companionLocList, inputDateTime);
                
                newCompanionUserLocations.put(entry.getKey(), companionLocList);
            }

            TreeMap<Long, TreeMap<String, String>> companionCount = new TreeMap<>();

            /*calculate the together time with the companion*/
            /*get companioncount*/
            for (Map.Entry<String, List<UserLocation>> entry : newCompanionUserLocations.entrySet()) {
                String macAdd = entry.getKey();
                List<UserLocation> companionUserLocationList = entry.getValue();
                long togetherTime = 0;
                String companionEmail = "";
                for (UserLocation userLocation : userLocationList) {
                    for (UserLocation companionLocation : companionUserLocationList) {
                        companionEmail = companionLocation.getEmail();
                        //only if same location_id, will continue
                        if (userLocation.isSameLocID(companionLocation)) {
                            //to account for no overlaps of time
                            //                  ------------      (User)
                            //          -------              --------         (Companion)
                            if (!(userLocation.getDateTime().after(companionLocation.getEndTime())
                                    || userLocation.getEndTime().before(companionLocation.getDateTime()))) {
                                Timestamp maxSTime = null;
                                Timestamp minETime = null;
                                //set maxSTime
                                if (userLocation.getDateTime().after(companionLocation.getDateTime())) {
                                    maxSTime = userLocation.getDateTime();
                                } else {
                                    maxSTime = companionLocation.getDateTime();
                                }
                                //set min end time
                                if (userLocation.getEndTime().before(companionLocation.getEndTime())) {
                                    minETime = userLocation.getEndTime();
                                } else {
                                    minETime = companionLocation.getEndTime();
                                }
                                togetherTime += (minETime.getTime() - maxSTime.getTime());
                            }
                        }
                    }
                }

                /*put togetherTime with confirmed companion into map*/
                //companion list is to include companion's email and macadd
                TreeMap<String, String> companionInfoMap = new TreeMap<>();
                if(togetherTime != 0) {
                    if (companionCount.containsKey(togetherTime)) {
                        companionInfoMap = companionCount.get(togetherTime);
                        if(companionEmail == null) {
                            companionInfoMap.put(macAdd,"");
                        }
                        else {
                            companionInfoMap.put(macAdd,companionEmail);
                        }
                        companionCount.put(togetherTime, companionInfoMap);
                    }
                    else {
                        if(companionEmail == null) {
                            companionInfoMap.put(macAdd,"");
                        }
                        else {
                            companionInfoMap.put(macAdd,companionEmail);
                        }
                        companionCount.put(togetherTime, companionInfoMap);
                    }                    
                }
            }
            
            if (companionCount.size() == 0) {
                messages.add("no companions found in input datetime");
                Collections.sort(messages);
                CompanionContainer toReturn = new CompanionContainer(messages, "success");
                toReturn.setResults(new ArrayList<>());
                String json = gson.toJson(toReturn);
                if (notJson == null) {
                    //---— since notJson == null means this is a json request

                    //Set the json return to an attribute and forward
                    request.setAttribute("json", json);
                    RequestDispatcher view = request.getRequestDispatcher("json/top-k-companions");
                    view.forward(request, response);
                    return;
                } else {
                    session.setAttribute("top_companions_result", json);
                    response.sendRedirect("TopKCompanionsForm.jsp");
                    return;
                }
            }

            //sort the map in desc order by together time
            Map<Long, TreeMap<String,String>> sortedCompanionCount = companionCount.descendingMap();

            List<Companion> companions = new ArrayList<>();

            int count = 0;
            if (rank >= sortedCompanionCount.size()) {
                for (Map.Entry<Long, TreeMap<String,String>> entry : sortedCompanionCount.entrySet()) {
                    count++;
                    long key = entry.getKey();
                    TreeMap<String,String> companionInfo = entry.getValue();
                    for(Map.Entry<String, String> entryTwo : companionInfo.entrySet()){
                        companions.add(new Companion(count, key, entryTwo.getKey(), entryTwo.getValue()));
                    }
                }
            } else {
                Object[] keys = sortedCompanionCount.keySet().toArray();
                for (int j = 0; j < rank; j++) {
                    count++;
                    long key = (long) keys[j];
                    TreeMap<String,String> companionInfo = sortedCompanionCount.get(key);
                    for(Map.Entry<String, String> entryTwo : companionInfo.entrySet()){
                        companions.add(new Companion(count, key, entryTwo.getKey(), entryTwo.getValue()));
                    }
                }
            }

            CompanionContainer toReturn = new CompanionContainer("success", companions);
            String json = gson.toJson(toReturn);
            if (notJson == null) {
                //----- since notJson == null means this is a json request

                //Set the json return to an attribute and forward
                json = json.replace("mac_address", "mac-address");
                json = json.replace("time_together", "time-together");
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/top-k-companions");
                view.forward(request, response);
                return;
            } else {
                session.setAttribute("top_companions_result", json);
                response.sendRedirect("TopKCompanionsForm.jsp");
                return;
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}