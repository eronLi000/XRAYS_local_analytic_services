/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.DemographicDAO;
import dao.TopKNextPlacesDAO;
import entity.LocationLookUp;
import entity.NextPlace;
import entity.NextPlaceContainer;
import entity.UserLocation;
import is203.JWTException;
import is203.JWTUtility;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(name = "TopKNextPlacesController", urlPatterns = {"/TopKNextPlacesController"})
public class TopKNextPlacesController extends HttpServlet {

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
        boolean validOrigin = true;
        boolean validK = true;
        
        String origin = request.getParameter("origin");
        String date = request.getParameter("date");
        String token = (String) request.getParameter("token");
        String k = request.getParameter("k");

        //--------- Start Handler for token validation ----------
        if (notJson == null) {
            //----- since notJson == null means this is a json request

             //check for missing token
            if (token == null || origin == null || date == null || token.trim().equals("") || origin.trim().equals("") || date.trim().equals("") || (k != null && k.trim().equals(""))){
                //Creates the json return for status error
                if(token == null){
                    blankOrMissingToken = true;
                    messages.add("missing token");
                }
                else if (token.trim().equals("")){
                    blankOrMissingToken = true;
                    messages.add("blank token");
                }
                
                if (origin == null){
                    messages.add("missing origin");
                }
                else if (origin.trim().equals("")){
                    messages.add("blank origin");
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
                
                NextPlaceContainer nextPlaceContainer = new NextPlaceContainer(messages, "error");
                String json = gson.toJson(nextPlaceContainer);

                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/top-k-next-places");
                view.forward(request, response);
                return;
            }
            
            token = token.trim();
            origin = origin.toUpperCase().trim();
            date = date.trim();
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
                
                NextPlaceContainer nextPlaceContainer = new NextPlaceContainer(messages, "error");
                String json = gson.toJson(nextPlaceContainer);

                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/top-k-next-places");
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
        
        LocationLookUp luk = new LocationLookUp();
        if(notJson != null && (origin == null || origin.length() == 0)) {
            messages.add("no data found in input datetime");
            Collections.sort(messages);
            NextPlaceContainer toReturn = new NextPlaceContainer(messages, "success");
            String json = gson.toJson(toReturn);

            if (notJson == null) {
                //----- since notJson == null means this is a json request

                //Set the json return to an attribute and forward
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/top-k-next-places");
                view.forward(request, response);
                return;
            } else {
                session.setAttribute("top_next_places_result", json);
                response.sendRedirect("TopKNextPlacesForm.jsp");
                return;
            }
        }
        if (!luk.isValidSemanticPlace(origin) || !TopKNextPlacesDAO.verifyOrigin(origin)) {
            validOrigin = false;
            
        }

        if (!validDate || !validK || !validOrigin) {
            //Adds the message for the json return
            if (!validDate) {
                messages.add("invalid date");
            }
            if (!validK) {
                messages.add("invalid k");
            }
            
            if (!validOrigin) {
                messages.add("invalid origin");
            }

            Collections.sort(messages);
                
            NextPlaceContainer nextPlaceContainer = new NextPlaceContainer(messages, "error");
            String json = gson.toJson(nextPlaceContainer);

            if (notJson == null) {
                //----- since notJson == null means this is a json request

                //Set the json return to an attribute and forward
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/top-k-next-places");
                view.forward(request, response);
                return;
            } else {
                session.setAttribute("json", json);
                response.sendRedirect("TopKNextPlacesForm.jsp");
                return;
            }
            //--------- End Handler for Invalid Input  ----------
        }
        
        //get the last location update of a user from a specified time window                        
        Map<String, String> userLastLocation = TopKNextPlacesDAO.retrieveUserLastLocation(inputDateTime);

        //retrieve valid location IDs
        List<String> validLocationIDs = TopKNextPlacesDAO.retrieveValidLocationIDs(origin);

        if (userLastLocation.size() == 0 || validLocationIDs.isEmpty()) {
            messages.add("no data found in input datetime");
            Collections.sort(messages);
            NextPlaceContainer toReturn = new NextPlaceContainer(messages, "success");
            String json = gson.toJson(toReturn);

            if (notJson == null) {
                //----- since notJson == null means this is a json request

                //Set the json return to an attribute and forward
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/top-k-next-places");
                view.forward(request, response);
                return;
            } else {
                session.setAttribute("top_next_places_result", json);
                response.sendRedirect("TopKNextPlacesForm.jsp");
                return;
            }

        } else {

            List<String> validUsers = new ArrayList<>();

            //get all the users whose last location update matches the valid location ids of the chosen semantic place
            for (Map.Entry<String, String> entry : userLastLocation.entrySet()) {
                String macAddress = entry.getKey();
                String locationID = entry.getValue();

                if (validLocationIDs.contains(locationID)) {
                    validUsers.add(macAddress);
                }
            }

            //retrieve next location updates for valid users
            if(validUsers.isEmpty()) {
                messages.add("no user in input semantic place");
                Collections.sort(messages);
                NextPlaceContainer toReturn = new NextPlaceContainer(messages, "success");
                String json = gson.toJson(toReturn);

                if (notJson == null) {
                    //----- since notJson == null means this is a json request

                    //Set the json return to an attribute and forward
                    request.setAttribute("json", json);
                    RequestDispatcher view = request.getRequestDispatcher("json/top-k-next-places");
                    view.forward(request, response);
                    return;
                } else {
                    session.setAttribute("top_next_places_result", json);
                    response.sendRedirect("TopKNextPlacesForm.jsp");
                    return;
                }                
            }
            
            Map<String, List<UserLocation>> nextUserLocations = TopKNextPlacesDAO.retrieveNextUserLocations(validUsers, inputDateTime);

            if (nextUserLocations == null || nextUserLocations.isEmpty()) {
                messages.add("no data found for possible next place");
                Collections.sort(messages);
                NextPlaceContainer toReturn = new NextPlaceContainer(messages, "success");
                String json = gson.toJson(toReturn);

                if (notJson == null) {
                    //----- since notJson == null means this is a json request

                    //Set the json return to an attribute and forward
                    request.setAttribute("json", json);
                    RequestDispatcher view = request.getRequestDispatcher("json/top-k-next-places");
                    view.forward(request, response);
                    return;
                } else {
                    session.setAttribute("top_next_places_result", json);
                    response.sendRedirect("TopKNextPlacesForm.jsp");
                    return;
                }
            }

            Map<String, Integer> semanticPlaceCount = new HashMap<>();

            //loop through the map of nextUserlocations to check each mac address and check their location updates to see if they have a next place
            for (Map.Entry<String, List<UserLocation>> entry : nextUserLocations.entrySet()) {
                String macAddress = entry.getKey();
                List<UserLocation> userLocationList = entry.getValue();

                long time = 0;
                //get the 15 mins after of the input date time
                long maxTimeOfWindow = 900000 + inputDateTime.getTime();
                int userLocationListSize = userLocationList.size();
                int fiveMinutes = 300000;
                boolean willContinue = true;

                UserLocation ul = userLocationList.get(userLocationListSize - 1);
                long ulTime = ul.getTimeInMSeconds();
                String ulSemanticPlace = ul.getSemanticPlace();
                time = maxTimeOfWindow - ulTime;

                if (time >= fiveMinutes) {
                    if (semanticPlaceCount.get(ulSemanticPlace) == null) {
                        semanticPlaceCount.put(ulSemanticPlace, 1);
                    } else {
                        int count = semanticPlaceCount.get(ulSemanticPlace);
                        count++;
                        semanticPlaceCount.put(ulSemanticPlace, count);
                    }
                    willContinue = false;
                }

                //check if user has more than one location update
                if (userLocationListSize > 1) {
                    //get second to the last location update for user
                    int userLocationListIndex = userLocationListSize - 2;

                    while (willContinue && userLocationListIndex >= 0) {
                        UserLocation ul1 = userLocationList.get(userLocationListIndex);
                        String ul1SemanticPlace = ul1.getSemanticPlace();

                        //check if the previous semantic place is equal to the current semantic place
                        if (ulSemanticPlace.equals(ul1SemanticPlace)) {
                            long duration = ulTime - ul1.getTimeInMSeconds();
                            time += duration;

                            if (time >= fiveMinutes) {
                                if (semanticPlaceCount.get(ul1SemanticPlace) == null) {
                                    semanticPlaceCount.put(ul1SemanticPlace, 1);
                                } else {
                                    int count = semanticPlaceCount.get(ul1SemanticPlace);
                                    count++;
                                    semanticPlaceCount.put(ul1SemanticPlace, count);
                                }
                                willContinue = false;
                            } else {
                                ulTime = ul1.getTimeInMSeconds();
                                ulSemanticPlace = ul1.getSemanticPlace();
                            }
                            userLocationListIndex--;
                        } else {
                            time = ulTime - ul1.getTimeInMSeconds();

                            if (time >= fiveMinutes) {
                                if (semanticPlaceCount.get(ul1SemanticPlace) == null) {
                                    semanticPlaceCount.put(ul1SemanticPlace, 1);
                                } else {
                                    int count = semanticPlaceCount.get(ul1SemanticPlace);
                                    count++;
                                    semanticPlaceCount.put(ul1SemanticPlace, count);
                                }
                                willContinue = false;
                            } else {
                                ulTime = ul1.getTimeInMSeconds();
                                ulSemanticPlace = ul1.getSemanticPlace();
                            }
                            userLocationListIndex--;
                        }
                    }
                }
            }

            //put the hashmap of key semantic place and value count into a treemap where key is the count and value is a list of semantic places
            TreeMap<Integer, List<String>> semanticPlaceRank = new TreeMap<>();
            int totalNextPlaceUsers = 0;
            
            if(semanticPlaceCount.isEmpty()){
                NextPlaceContainer nextPlaceContainer = new NextPlaceContainer("success", validUsers.size(), totalNextPlaceUsers, new ArrayList<>());
                messages.add("no next place");
                Collections.sort(messages);
                nextPlaceContainer.setMessages(messages);
                
                String json = gson.toJson(nextPlaceContainer);

                if (notJson == null) {
                    //----- since notJson == null means this is a json request

                    //Set the json return to an attribute and forward
                    request.setAttribute("json", json);
                    RequestDispatcher view = request.getRequestDispatcher("json/top-k-next-places");
                    view.forward(request, response);
                    return;
                } else {
                    session.setAttribute("top_next_places_result", json);
                    response.sendRedirect("TopKNextPlacesForm.jsp");
                    return;
                }
            }
            
            for (Map.Entry<String, Integer> entry : semanticPlaceCount.entrySet()) {
                String place = entry.getKey();
                int count = entry.getValue();
                totalNextPlaceUsers += count;

                List<String> list = new ArrayList<>();

                if (semanticPlaceRank.containsKey(count)) {
                    list = semanticPlaceRank.get(count);
                }
                list.add(place);
                semanticPlaceRank.put(count, list);
            }
            
            Map<Integer, List<String>> sortedSemanticPlaceRank = semanticPlaceRank.descendingMap();

            List<NextPlace> nextPlaces = new ArrayList<>();
            int ranking = 0;
            if (rank >= sortedSemanticPlaceRank.size()) {
                for (Map.Entry<Integer, List<String>> entry : sortedSemanticPlaceRank.entrySet()) {
                    ranking++;
                    List<String> semanticPlaces = entry.getValue();
                    Collections.sort(semanticPlaces);
                    for(String semanticPlace : semanticPlaces){
                        nextPlaces.add(new NextPlace(ranking, entry.getKey(), semanticPlace));
                    }
                }
            } else {
                Object[] keys = sortedSemanticPlaceRank.keySet().toArray();
                for (int j = 0; j < rank; j++) {
                    ranking++;
                    int key = (int) keys[j];
                    List<String> semanticPlaces = sortedSemanticPlaceRank.get(key);
                    Collections.sort(semanticPlaces);
                    for(String semanticPlace : semanticPlaces){
                        nextPlaces.add(new NextPlace(ranking, key, semanticPlace));
                    }
                }
            }

            int totalUsers = validUsers.size();

            NextPlaceContainer toReturn = new NextPlaceContainer("success", totalUsers, totalNextPlaceUsers, nextPlaces);
            String json = gson.toJson(toReturn);

            if (notJson == null) {
                //----- since notJson == null means this is a json request

                //Set the json return to an attribute and forward
                json = json.replace("total_users", "total-users");
                json = json.replace("total_next_place_users", "total-next-place-users");
                json = json.replace("semantic_place", "semantic-place");
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/top-k-next-places");
                view.forward(request, response);
                return;
            } else {
                session.setAttribute("top_next_places_result", json);
                response.sendRedirect("TopKNextPlacesForm.jsp");
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