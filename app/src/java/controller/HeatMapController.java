/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.DemographicDAO;
import dao.HeatMapDAO;
import entity.HeatMapContainer;
import entity.HeatMapRow;
import entity.HeatMap_map;
import is203.JWTException;
import is203.JWTUtility;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;
import net.minidev.json.JSONObject;


@WebServlet(name = "HeatMapController", urlPatterns = {"/HeatMapController"})
public class HeatMapController extends HttpServlet {

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

        //Creates session for non json requests, query generated from UI jsp page 
        HttpSession session = request.getSession(false);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //Check if query is a json query
        String notJson = request.getParameter("notJson");
        //creates an arraylist for storing the message for invalid user input
        ArrayList<String> messages = new ArrayList<>();
        boolean blankOrMissingToken = false;
        boolean validToken = true;
        boolean validDate = true;
        boolean validFloor = true;

        //--------- Start Handler for token validation ----------
        String floor = request.getParameter("floor");
        String date = request.getParameter("date");
        String token = (String) request.getParameter("token");
        
         if (notJson == null) {
            //----- since notJson == null means this is a json request

             //check for missing token
            if (token == null || date == null || floor == null || floor.trim().equals("") || token.trim().equals("") || date.trim().equals("")){
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
                
                if (floor == null){
                    messages.add("missing floor");
                }
                else if (floor.trim().equals("")){
                    messages.add("blank floor");
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
                HeatMapContainer hmc = new HeatMapContainer("error", messages);
                String json = gson.toJson(hmc);
                
                //forward result back to the page which calls this controller, where query is made
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/basic-loc-report");
                view.forward(request, response);
                return;
            }
            
            //ensure that there are no white spaces
            token = token.trim();
            floor = floor.trim();
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
                HeatMapContainer hmc = new HeatMapContainer("error", messages);
                String json = gson.toJson(hmc);
                
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/basic-loc-report");
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
        
        Integer level = null;

        //checks for a valid floor number
        try {
            level = Integer.parseInt(floor);
            if (level < 0 || level > 5) {
                validFloor = false;
            }
        } catch (NumberFormatException e) {
            validFloor = false;
        }
        
        //check for missing 
        if (!validDate || !validFloor){
            //Adds the message for the json return
            if (!validDate) {
                messages.add("invalid date");
            }
            if (!validFloor) {
                messages.add("invalid floor");
            }
            
            Collections.sort(messages);
            HeatMapContainer hmc = new HeatMapContainer("error", messages);
            String json = gson.toJson(hmc);
            
            
             if (notJson == null) {
                //----- since notJson == null means this is a json request

                //Set the json return to an attribute and forward
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/heatmap");
                view.forward(request, response);
                return;
            } else {
                session.setAttribute("json", json);
                response.sendRedirect("heatmap.jsp");
                return;
            }
        }
        
        //retrieves all semantic place from selected floor of query
        TreeMap<String, HeatMapRow> blankSemanticPlaces = HeatMapDAO.retrieveAllSemanticPlaces(level);

        //returns heatmap results in Arraylist format
        ArrayList<HeatMapRow> result = HeatMapDAO.retrieveHeatMap(inputDateTime, level, blankSemanticPlaces);
        Collections.sort(result);
        
        //checks for floor input parameter
        String floorReturn;
        if(level == 0){
            floorReturn = "Basement 1";
        } else {
            floorReturn = "Level " + level;
        }

        //for successful generation of result, returned to jsp and json query accordingly
        if (!result.isEmpty()) {
            HeatMapContainer hmc = new HeatMapContainer(result, "success");
            String json = gson.toJson(hmc);
            //Start generating graphical heatmap 
            ArrayList<HeatMap_map> generated_heatmap = new ArrayList<>();

            //Based on the result of heatmaprow, assign the xy plots for heatmap
            for(HeatMapRow hmr : result){
                HeatMap_map data = new HeatMap_map(level,hmr.getCrowdDensity(),hmr.getSemanticPlace());
                generated_heatmap.add(data);
            }

            JSONObject map = new JSONObject();
            map.put("map", generated_heatmap);

            String jsonMap = gson.toJson(map);
            //End generating graphical heatmap 
            
            if (notJson == null) {
                //----- since notJson == null means this is a json request
               

                //Set the json return to an attribute and forward
                json = json.replace("semantic_place", "semantic-place");
                json = json.replace("num_people", "num-people");
                json = json.replace("crowd_density", "crowd-density");
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/heatmap");
                view.forward(request, response);
                return;

            } else {
                session.setAttribute("heatmap_graphical", jsonMap);
                session.setAttribute("heatmap_title", floorReturn);
                session.setAttribute("heatmap_floor", floor); 
                session.setAttribute("heatmap_result", json);
                response.sendRedirect("heatmap.jsp");
                return;
            }

        } else {
            messages.add("no data found for input datetime");
            Collections.sort(messages);
            HeatMapContainer hmc = new HeatMapContainer("error", messages);
            String json = gson.toJson(hmc);

            if (notJson == null) {
                //----- since notJson == null means this is a json request

                //Set the json return to an attribute and forward
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/heatmap");
                view.forward(request, response);
                return;

            } else {               
                //Set error message to be return and forward it back to jsp caller
                session.setAttribute("heatmap_result", json);
                response.sendRedirect("heatmap.jsp");
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