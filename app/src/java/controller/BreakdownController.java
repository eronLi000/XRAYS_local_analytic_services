package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.BreakdownDAO;
import dao.DemographicDAO;
import entity.Breakdown;
import entity.BreakdownContainer;
import entity.Demographic;
import is203.JWTException;
import is203.JWTUtility;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Set;
import javax.servlet.RequestDispatcher;

@WebServlet(name = "BreakdownController", urlPatterns = {"/BreakdownController"})
public class BreakdownController extends HttpServlet {

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
        //If there is data from notJson, means it came from UI page
                //instantiate a session object
        HttpSession session = request.getSession(false);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String notJson = request.getParameter("notJson");
        //creates an arraylist for storing the message for invalid user input
        ArrayList<String> messages = new ArrayList<>();
        boolean blankOrMissingToken = false;
        boolean validToken = true;
        boolean validDate = true;
        boolean validOrder = true;
        
        String order = request.getParameter("order");
        String date = request.getParameter("date");
        String token = (String) request.getParameter("token");

        //--------- Start Handler for token validation ----------
        if (notJson == null) {
            //----- since notJson == null means this is a json request
                        
             //check for missing token
            if (token == null || order == null || date == null || token.trim().equals("") || order.trim().equals("") || date.trim().equals("")){
                //Creates the json return for status error
                if(token == null){
                    blankOrMissingToken = true;
                    messages.add("missing token");
                }
                else if (token.trim().equals("")){
                    blankOrMissingToken = true;
                    messages.add("blank token");
                }
                
                if (order == null){
                    messages.add("missing order");
                }
                else if (order.trim().equals("")){
                    messages.add("blank order");
                }
                
                if (date == null){
                    messages.add("missing date");
                }
                else if (date.trim().equals("")){
                    messages.add("blank date");
                }
                
                if(!blankOrMissingToken){
                    try {
                        token = token.trim();
                        //Verifies the token using shared secret
                        String username = JWTUtility.verify(token, DemographicDAO.getSharedSecret());
                        System.out.println(username);
                        if (username == null) {
                            validToken = false;
                        }
                        else{
                            System.out.println(username);
                        }         

                    } catch (JWTException e) {
                        validToken = false;
                    }
                }
                
                if(!validToken){
                    messages.add("invalid token");
                }
                
                Collections.sort(messages);
                BreakdownContainer breakdownContainer = new BreakdownContainer(messages, "error");
                String json = gson.toJson(breakdownContainer);
                
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/basic-loc-report");
                view.forward(request, response);
                return;
            }
            
            token = token.trim();
            date = date.trim();
            order = order.trim();
            
            try {
                //Verifies the token using shared secret
                if (JWTUtility.verify(token, DemographicDAO.getSharedSecret()) == null) {
                    validToken = false;
                }
            } catch (JWTException e) {
                System.out.println("Throws exception outside the blank and missing shitload");
                validToken = false;
            }
            
            if(!validToken){
                messages.add("invalid token");
                
                Collections.sort(messages);
                BreakdownContainer breakdownContainer = new BreakdownContainer(messages, "error");
                String json = gson.toJson(breakdownContainer);

                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/basic-loc-report");
                view.forward(request, response);
                return;
            }
        }
        //--------- End Handler for token validation ----------
        
        //check for missing 
        
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
        
        List<String> orders = null;

        //check if the user inputted multiple orders. Order can be year only or year,gender or year,gender,school.
        if (order.indexOf(',') == -1) {
            //check if the word is either "year" "school" or "gender"
            if (!isValidOrder(order)) {
                validOrder = false;
            } else {
                orders = new ArrayList<>();
                orders.add(order);
            }
        } //there is a comma in the input of user for order
        else {
            //split the user input and store each word in a List<String>
            orders = Arrays.asList(order.split(","));
            
            Set<String> set = new HashSet<>();
            for(String o : orders){
                String ord = o.toLowerCase();
                ord = ord.trim();
                if(!set.add(ord)){
                    validOrder=false;
                }
            }
                    
            //check if each of the word is a valid order
            for (String type : orders) {
                if (!isValidOrder(type.trim())) {
                    validOrder = false;
                }
            }
        }

        //check if there is something invalid so we can add to error message list
        if (!validDate || !validOrder) {

            if (!validDate) {
                messages.add("invalid date");
            }

            if (!validOrder) {
                messages.add("invalid order");
            }
            
            BreakdownContainer toReturn = null;
            
            if (notJson == null) {
                //----- since notJson == null means this is a json request             
                Collections.sort(messages);
                toReturn = new BreakdownContainer(messages, "error");
                //convert the breakdowncontainer to a json object
                String json = gson.toJson(toReturn);
                //Set the json return to an attribute and forward
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/basic-loc-report");
                view.forward(request, response);
                return;

            } else {
                Collections.sort(messages);
                toReturn = new BreakdownContainer(messages, "error");
                String json = gson.toJson(toReturn);
                session.setAttribute("json", json);
                response.sendRedirect("BreakdownForm.jsp");
                return;
            }
            //--------- End Handler for Invalid Input  ----------
        }

        //retrieve all the users who had a location update in the 15 min window from location table but is also inside demographic table
        List<Demographic> demoList = BreakdownDAO.retrieveDemographicList(inputDateTime);
        

        TreeMap<String, List<Demographic>> firstMap = null;
        TreeMap<String, List<Demographic>> secondMap = null;
        TreeMap<String, List<Demographic>> thirdMap = null;

        //create a list that will store breakdown objects. this list will be assigned to one of the attributes of breakdown container
        List<Breakdown> breakdowns = new ArrayList<Breakdown>();

        //create a variable that will store at which index of breakdowns variable will a breakdown object be stored 
        int firstLayer = 0;

        //check if demoList is null, returns an empty breakdown list to jsp if it is null
        if (demoList == null) {
            messages.add("no data found for input datetime");
            Collections.sort(messages);
            BreakdownContainer breakdownContainer = new BreakdownContainer(messages, "success");
            String json = gson.toJson(breakdownContainer);

            if (notJson == null) {
                //----- since notJson == null means this is a json request

                //Set the json return to an attribute and forward
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/basic-loc-report");
                view.forward(request, response);
                return;
            } else {
                session.setAttribute("breakdownResult", json);
                response.sendRedirect("BreakdownForm.jsp");
                return;
            }

        } //if demoList is not null
        else {
            //get the first word from the input order of the user
                //if the first word in the variable orders is "year"
                if(orders.get(0).equalsIgnoreCase("year")){
                    //get a TreeMap<String, List<Demographic>> where the key is the type of order, in this case it's year
                    //and a value of List of Demographic objects. The TreeMap is passed by the categoryByYear method
                    //while it takes in a list of demographic objects
                    //demolist is the list of all users we got from the query in the DAO
                    firstMap = categoryByYear(demoList);
                    //set a list of breakdown objects to breakdowns which we declared above
                    breakdowns = generateBreakdownList(firstMap, orders.get(0));
                    //loop through the list of breakdown objects from breakdowns
                    for (Breakdown breakdown : breakdowns) {
                        //orders.size checks if the user has input more than one type of order,
                        //and checks if the order is "school" 
                        if (orders.size() > 1 && orders.get(1).equalsIgnoreCase("school")) {
                            int secondLayer = 0;
                            //get a TreeMap<String, List<Demographic>> where the key is the type of order, in this case it's school
                            //and a value of List of Demographic objects. The TreeMap is passed by the categoryBySchool method
                            //while it takes in a list of demographic objects
                            //demolist is the list of all users we got from the query in the DAO
                            //categoryBySchool takes in a list of demographic
                            //firstMap.get returns a list of demographics
                            //key of firstMap is the specific type of that list of demographics as in is it 2013? is it M?
                            secondMap = categoryBySchool(firstMap.get(breakdown.getYear()));
                            List<Breakdown> breakdownList = generateBreakdownList(secondMap, orders.get(1));
                            for (Breakdown breakdown2 : breakdownList) {
                                if (orders.size() > 2 && orders.get(2).equalsIgnoreCase("gender")) {
                                    thirdMap = categoryByGender(secondMap.get(breakdown2.getSchool()));
                                    List<Breakdown> breakdownList2 = generateBreakdownList(thirdMap, orders.get(2));
                                    breakdownList.get(secondLayer).setBreakdown(breakdownList2);
                                    secondLayer++;
                                }
                            }
                            //so the breakdown object has an attribute that is a list of breakdown objects also
                            //in here i get one breakdown object and set his list of breakdowns to breakdownList as can be seen
                            //by the setList Method
                            breakdowns.get(firstLayer).setBreakdown(breakdownList);
                            firstLayer++;
                        } else if (orders.size() > 1 && orders.get(1).equalsIgnoreCase("gender")) {
                            int secondLayer = 0;
                            secondMap = categoryByGender(firstMap.get(breakdown.getYear()));
                            List<Breakdown> breakdownList = generateBreakdownList(secondMap, orders.get(1));
                            for (Breakdown breakdown2 : breakdownList) {
                                if (orders.size() > 2 && orders.get(2).equalsIgnoreCase("school")) {
                                    thirdMap = categoryBySchool(secondMap.get(breakdown2.getGender()));
                                    List<Breakdown> breakdownList2 = generateBreakdownList(thirdMap, orders.get(2));
                                    breakdownList.get(secondLayer).setBreakdown(breakdownList2);
                                    secondLayer++;
                                }
                            }
                            breakdowns.get(firstLayer).setBreakdown(breakdownList);
                            firstLayer++;
                        }
                    }
                }

                else if (orders.get(0).equalsIgnoreCase("school")){
                    firstMap = categoryBySchool(demoList);
                    breakdowns = generateBreakdownList(firstMap, orders.get(0));
                    for (Breakdown breakdown : breakdowns) {
                        if (orders.size() > 1 && orders.get(1).equalsIgnoreCase("year")) {
                            int secondLayer = 0;
                            secondMap = categoryByYear(firstMap.get(breakdown.getSchool()));
                            List<Breakdown> breakdownList = generateBreakdownList(secondMap, orders.get(1));
                            for (Breakdown breakdown2 : breakdownList) {
                                if (orders.size() > 2 && orders.get(2).equalsIgnoreCase("gender")) {
                                    thirdMap = categoryByGender(secondMap.get(breakdown2.getYear()));
                                    List<Breakdown> breakdownList2 = generateBreakdownList(thirdMap, orders.get(2));
                                    breakdownList.get(secondLayer).setBreakdown(breakdownList2);
                                    secondLayer++;
                                }
                            }
                            breakdowns.get(firstLayer).setBreakdown(breakdownList);
                            firstLayer++;
                        } else if (orders.size() > 1 && orders.get(1).equalsIgnoreCase("gender")) {
                            int secondLayer = 0;
                            secondMap = categoryByGender(firstMap.get(breakdown.getSchool()));
                            List<Breakdown> breakdownList = generateBreakdownList(secondMap, orders.get(1));
                            for (Breakdown breakdown2 : breakdownList) {
                                if (orders.size() > 2 && orders.get(2).equalsIgnoreCase("year")) {
                                    thirdMap = categoryByYear(secondMap.get(breakdown2.getGender()));
                                    List<Breakdown> breakdownList2 = generateBreakdownList(thirdMap, orders.get(2));
                                    breakdownList.get(secondLayer).setBreakdown(breakdownList2);
                                    secondLayer++;
                                }
                            }
                            breakdowns.get(firstLayer).setBreakdown(breakdownList);
                            firstLayer++;
                        }
                    }
                }

                else if (orders.get(0).equalsIgnoreCase("gender")){
                    firstMap = categoryByGender(demoList);
                    breakdowns = generateBreakdownList(firstMap, orders.get(0));
                    for (Breakdown breakdown : breakdowns) {
                        if (orders.size() > 1 && orders.get(1).equalsIgnoreCase("school")) {
                            int secondLayer = 0;
                            secondMap = categoryBySchool(firstMap.get(breakdown.getGender()));
                            List<Breakdown> breakdownList = generateBreakdownList(secondMap, orders.get(1));
                            for (Breakdown breakdown2 : breakdownList) {
                                if (orders.size() > 2 && orders.get(2).equalsIgnoreCase("year")) {
                                    thirdMap = categoryByYear(secondMap.get(breakdown2.getSchool()));
                                    List<Breakdown> breakdownList2 = generateBreakdownList(thirdMap, orders.get(2));
                                    breakdownList.get(secondLayer).setBreakdown(breakdownList2);
                                    secondLayer++;
                                }
                            }
                            breakdowns.get(firstLayer).setBreakdown(breakdownList);
                            firstLayer++;
                        } else if (orders.size() > 1 && orders.get(1).equalsIgnoreCase("year")) {
                            int secondLayer = 0;
                            secondMap = categoryByYear(firstMap.get(breakdown.getGender()));
                            List<Breakdown> breakdownList = generateBreakdownList(secondMap, orders.get(1));
                            for (Breakdown breakdown2 : breakdownList) {
                                if (orders.size() > 2 && orders.get(2).equalsIgnoreCase("school")) {
                                    thirdMap = categoryBySchool(secondMap.get(breakdown2.getYear()));
                                    List<Breakdown> breakdownList2 = generateBreakdownList(thirdMap, orders.get(2));
                                    breakdownList.get(secondLayer).setBreakdown(breakdownList2);
                                    secondLayer++;
                                }
                            }
                            breakdowns.get(firstLayer).setBreakdown(breakdownList);
                            firstLayer++;
                        }
                    }
                }
        }

        //returns the breakdowncontainer
        BreakdownContainer breakdownContainer = new BreakdownContainer("success", breakdowns);
        if (notJson == null) {
            //----- since notJson == null means this is a json request
            String json = gson.toJson(breakdownContainer);
            //Set the json return to an attribute and forward
            if(json.indexOf("2015") != -1) {
                json = json.replace("\"2013\"", "2013");
                json = json.replace("\"2014\"", "2014");
                json = json.replace("\"2015\"", "2015");
                json = json.replace("\"2016\"", "2016");                
                json = json.replace("\"2017\"", "2017");                
            }

            request.setAttribute("json", json);
            RequestDispatcher view = request.getRequestDispatcher("json/basic-loc-report");
            view.forward(request, response);
            return;
        } else {
            breakdownContainer.setTotalCount(demoList.size());
            String json = gson.toJson(breakdownContainer);
            session.setAttribute("breakdownResult", json);
            response.sendRedirect("BreakdownForm.jsp");
            return;
        }

    }
    
    /**
     * Returns a Treemap object. 
     * 
     * This method takes in a list of demographic objects and returns a Treemap.
     * The Treemap key is the specific school and the value is list of demographic objects.
     *
     * @param demographicList a List object of type Demographic.
     * @return the Treemap Object
     */
    protected TreeMap<String, List<Demographic>> categoryBySchool(List<Demographic> demographicList) {
        TreeMap<String, List<Demographic>> toReturn = new TreeMap<>();
        toReturn.put("accountancy", new ArrayList<>());
        toReturn.put("business", new ArrayList<>());
        toReturn.put("economics", new ArrayList<>());
        toReturn.put("law", new ArrayList<>());
        toReturn.put("sis", new ArrayList<>());
        toReturn.put("socsc", new ArrayList<>());
        
        List<Demographic> demoList = new ArrayList<>();

        for (Demographic demo : demographicList) {
            String school = demo.getSchool();

            if (toReturn.containsKey(school)) {
                demoList = toReturn.get(school);
                demoList.add(demo);
                toReturn.put(school, demoList);
            } else {
                demoList = new ArrayList<>();
                demoList.add(demo);
                toReturn.put(school, demoList);
            }
        }

        return toReturn;
    }

    /**
     * Returns a Treemap object. 
     * 
     * This method takes in a list of demographic objects and returns a Treemap.
     * The Treemap key is the specific year and the value is list of demographic objects.
     *
     * @param demographicList a List object of type Demographic.
     * @return the Treemap Object
     */
    protected TreeMap<String, List<Demographic>> categoryByYear(List<Demographic> demographicList) {
        TreeMap<String, List<Demographic>> toReturn = new TreeMap<>();
        toReturn.put("2013", new ArrayList<>());
        toReturn.put("2014", new ArrayList<>());
        toReturn.put("2015", new ArrayList<>());
        toReturn.put("2016", new ArrayList<>());
        toReturn.put("2017", new ArrayList<>());
        
        List<Demographic> demoList = new ArrayList<>();

        //for each demographic object from parameter list
        for (Demographic demo : demographicList) {
            //year will contain either 2014/2015/2016/2017
            String year = demo.getYear();

            //check if the key already exists meaning the year already has a list of demographic
            //if map already has the key, just append to the list of demographic objects
            if (toReturn.containsKey(year)) {
                demoList = toReturn.get(year);
                demoList.add(demo);
                toReturn.put(year, demoList);
            } //create new arraylist and add demo to list
            else {
                demoList = new ArrayList<>();
                demoList.add(demo);
                toReturn.put(year, demoList);
            }
        }

        return toReturn;
    }
    
    /**
     * Returns a boolean with value true/false. 
     * 
     * This method checks if the input parameter is a valid order type. 
     * Valid order types include gender, year and school. 
     * If the order is valid, the method will return true. 
     * Otherwise, this method will return false.
     *
     * @param order order type being queried.
     * @return boolean value
     */
    protected boolean isValidOrder(String order) {
        return order.equalsIgnoreCase("gender") || order.equalsIgnoreCase("year") || order.equalsIgnoreCase("school");
    }

    /**
     * Returns a Treemap object. 
     * 
     * This method takes in a list of demographic objects and returns a Treemap.
     * The Treemap key is the specific gender and the value is list of demographic objects.
     *
     * @param demographicList a List object of type Demographic.
     * @return the Treemap Object
     */
    protected TreeMap<String, List<Demographic>> categoryByGender(List<Demographic> demographicList) {
        TreeMap<String, List<Demographic>> toReturn = new TreeMap<>();
        toReturn.put("M", new ArrayList<>());
        toReturn.put("F", new ArrayList<>());
        
        List<Demographic> demoList = null;

        for (Demographic demo : demographicList) {
            String gender = demo.getGender();

            if (toReturn.containsKey(gender)) {
                demoList = toReturn.get(gender);
                demoList.add(demo);
                toReturn.put(gender, demoList);
            } else {
                demoList = new ArrayList<>();
                demoList.add(demo);
                toReturn.put(gender, demoList);
            }
        }

        return toReturn;
    }
    
    
    /**
     * Returns an ArrayList of Breakdown objects. 
     * 
     * This method takes in a Treemap of String and List of Demographics.
     * In the Treemap, the key is the specific gender, year or school
     * while the value is list of corresponding users for that year gender or school. 
     * <p>
     * The method also takes in a String value order which is either "year", "school" or "gender. 
     * @param map List object of type Demographic.
     * @param order school/year/gender
     * @return ArrayList of Breakdown objects
     */
    protected ArrayList<Breakdown> generateBreakdownList(TreeMap<String, List<Demographic>> map, String order) {
        ArrayList<Breakdown> toReturn = new ArrayList<Breakdown>();
        //iterate through the TreeMap
        //point of iterating is that each string in the treemap corresponds to a certain school/year/gender
        //and the value is the list of demographic with this type of school/year/gender
        //so for example the key, string can be the values 2013, 2014, 2015, 2016 and the 
        //value is the list of demographics whose year is 2013/2014/2015/2016
        //the key here is dependent on what kind of map you pass this methods so it can be M/F or the schools also
        for (Map.Entry<String, List<Demographic>> entry : map.entrySet()) {
            String type = entry.getKey();
            List<Demographic> demographicList = entry.getValue();
            //add a new breakdown object with type(2013? M? sis? (only one of the three) as first parameter, count of how many users correspond
            //to the type and order is was it sorting by school? year? gender?
            //this breakdown object is what we are interested in
            //the breakdown object tells us that this year has this many people. More specifically for example, the type will be 2016 and size will be 30 and order is year
            //so now we know that there are 30 users who are 2016.
            Breakdown breakdown = null;
            if (order.equalsIgnoreCase("school")) {
                breakdown = new Breakdown(type, null, demographicList.size());
            }

            if (order.equalsIgnoreCase("year")) {
                breakdown = new Breakdown(demographicList.size(), null, type);
            }

            if (order.equalsIgnoreCase("gender")) {
                breakdown = new Breakdown(demographicList.size(), type, null);
            }

            toReturn.add(breakdown);
        }
        Collections.sort(toReturn);
        return toReturn;
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