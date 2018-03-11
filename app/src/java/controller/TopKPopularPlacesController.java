/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.DemographicDAO;
import dao.TopKPopularPlacesDAO;
import entity.PopularPlace;
import entity.PopularPlaceContainer;
import is203.JWTException;
import is203.JWTUtility;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "TopKPopularPlacesController", urlPatterns = {"/TopKPopularPlacesController"})
public class TopKPopularPlacesController extends HttpServlet {

    /**
     * Returns the top-k popular places in the whole SIS building at a specified time within a 15 minute processing window
     * Uses a view.forward to return results back to json page and a response.sendRedirect to return results to UI view
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
        boolean validK = true;

        String k = request.getParameter("k");
        String date = request.getParameter("date");
        String token = (String) request.getParameter("token");
        
        //______________________________________________ Start Handler for Overall Validation ______________________________________________
        
        if (notJson == null) {
            //----- since notJson == null means this is a json request

            //--------- Start Handler for Missing/Blank Token ----------
            if (token == null || date == null || token.trim().equals("") || date.trim().equals("") || (k != null && k.trim().equals(""))){
                //Creates the json return for status error
                if(token == null){
                    blankOrMissingToken = true;
                    messages.add("missing token");
                }
                else if (token.trim().equals("")){
                    blankOrMissingToken = true;
                    messages.add("blank token");
                }
                
                if (date == null){
                    messages.add("missing date");
                }
                else if (date.trim().equals("")){
                    messages.add("blank date");
                }
                
                if(k != null){
                    if(k.trim().equals("")){
                        messages.add("blank k");
                    }
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
                PopularPlaceContainer popularPlaceContainer = new PopularPlaceContainer(messages, "error");
                String json = gson.toJson(popularPlaceContainer);

                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/top-k-popular-places");
                view.forward(request, response);
                return;
            }
            //--------- End Handler for Missing/Blank Token ----------
            
            date = date.trim();
            token = token.trim();
            
            //--------- Start Handler for token validation ----------
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
                
                PopularPlaceContainer popularPlaceContainer = new PopularPlaceContainer(messages, "error");
                String json = gson.toJson(popularPlaceContainer);

                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/top-k-popular-places");
                view.forward(request, response);
                return;
            }
            //--------- End Handler for token validation ----------
        }
        //--------- End Handler for token validation ----------
        
        //--------- Start Handler for Timestamp Validation ----------
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
        //--------- End Handler for Timestamp Validation ----------
        
        //--------- Start Handler for K Validation ----------
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
        //--------- End Handler for K Validation ----------
        
        //--------- Start Handler for Invalid Input Validation ----------
        if (!validDate || !validK) {
            //--------- Start Handler for Invalid Input ----------

            //Adds the message for the json return
            if (!validDate) {
                messages.add("invalid date");
            }
            if (!validK) {
                messages.add("invalid k");
            }
            
            //Creates the json return for status that has error
            PopularPlaceContainer toReturn = null;

            if (notJson == null) {
                //----- since notJson == null means this is a json request           
                Collections.sort(messages);
                toReturn = new PopularPlaceContainer(messages, "error");
                //convert the breakdowncontainer to a json object
                String json = gson.toJson(toReturn);
                //Set the json return to an attribute and forward
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/top-k-popular-places");
                view.forward(request, response);
                return;

            } else {
                Collections.sort(messages);
                toReturn = new PopularPlaceContainer(messages, "error");
                String json = gson.toJson(toReturn);
                session.setAttribute("json", json);
                response.sendRedirect("BreakdownForm.jsp");
                return;
            }
           
        }
        //--------- End Handler for Invalid Input  ----------
        
        //______________________________________________ End Handler for Overall Validation ______________________________________________
        
        TreeMap<Integer, List<String>> result = TopKPopularPlacesDAO.retrievePopularPlace(inputDateTime);

        ArrayList<PopularPlace> popularPlaceList = new ArrayList<>();
        
        //Returns an empty container since all input are valid and no result is found
        if (result == null) {
            messages.add("no data found for input datetime");
            Collections.sort(messages);
            PopularPlaceContainer toReturn = new PopularPlaceContainer(messages, "success");
            String json = gson.toJson(toReturn);

            if (notJson == null) {
                //----- since notJson == null means this is a json request

                //Set the json return to an attribute and forward
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/top-k-popular-places");
                view.forward(request, response);
                return;
            } else {
                session.setAttribute("popularPlaceResult", json);
                response.sendRedirect("TopKPopularPlacesForm.jsp");
                return;
            }

        } else {
            int size = result.size();
            
            //Based on how many the user wants to retrieve [K], return the results accordingly 
            if (result.size() < rank) {
                for (int i = 1; i <= size; i++) {
                    int count = result.lastKey();
                    List<String> list = result.get(count);
                    Collections.sort(list);
                    for(String place : list){
                        popularPlaceList.add(new PopularPlace(i, place, count));
                    }
                    result.remove(count);
                }
            } else {
                for (int i = 1; i <= rank; i++) {
                    int count = result.lastKey();
                    List<String> list = result.get(count);
                    Collections.sort(list);
                    for(String place : list){
                        popularPlaceList.add(new PopularPlace(i, place, count));
                    }
                    result.remove(count);
                }
            }
            
            //Since there is data found, throw it to a container and return that
            PopularPlaceContainer toReturn = new PopularPlaceContainer("success", popularPlaceList);
            String json = gson.toJson(toReturn);

            if (notJson == null) {
                //----- since notJson == null means this is a json request

                //Set the json return to an attribute and forward
                json = json.replace("semantic_place", "semantic-place");
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/top-k-popular-places");
                view.forward(request, response);
                return;
            } else {
                session.setAttribute("popularPlaceResult", json);
                response.sendRedirect("TopKPopularPlacesForm.jsp");
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
