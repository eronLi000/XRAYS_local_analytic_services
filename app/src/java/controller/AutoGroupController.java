/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.AutoGroupDAO;
import dao.DemographicDAO;
import entity.Group;
import entity.GroupContainer;
import entity.User;
import entity.UserLocation;
import is203.JWTException;
import is203.JWTUtility;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(name = "AutoGroupController", urlPatterns = {"/AutoGroupController"})
public class AutoGroupController extends HttpServlet {

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
        
        //create an instance of session object
        HttpSession session = request.getSession(false);
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        //creates an arraylist for storing the message for invalid user input
        ArrayList<String> messages = new ArrayList<>();

        //initialize 3 boolean value for validation
        boolean blankOrMissingToken = false;
        boolean validToken = true;
        boolean validDate = true;

        //get input from user side
        String notJson = request.getParameter("notJson");
        String date = request.getParameter("date");
        String token = (String) request.getParameter("token");
        
        //--------- Start Handler for token validation ----------
        if (notJson == null) {
            //----- since notJson == null means this is a json request

             //check for missing/blank token or datetime
            if (token == null || date == null || token.trim().equals("") || date.trim().equals("")){
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
                
               
                
                //check if token is valid
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
                
                // sort the messages in ascending order
                Collections.sort(messages);

                //create GroupContainer to store messages
                GroupContainer groupContainer = new GroupContainer("error", messages);
                String json = gson.toJson(groupContainer);

                //send messages back to json page
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/group_detect");
                view.forward(request, response);
                return;
            }
            
            
            //when tokena and date are blank or missing, check if it is valid 
            token = token.trim();
            date = date.trim();
            
            //validate token
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
                GroupContainer groupContainer = new GroupContainer("error", messages);
                String json = gson.toJson(groupContainer);

                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/group_detect");
                view.forward(request, response);
                return;
            }
        }

        //validate the inputDateTime
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
        
        if (!validDate) {
            //Adds the message for the json return
            messages.add("invalid date");

            Collections.sort(messages);
                
            GroupContainer groupContainer = new GroupContainer("error", messages);
            String json = gson.toJson(groupContainer);

            if (notJson == null) {
                //----- since notJson == null means this is a json request

                //Set the json return to an attribute and forward
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/group_detect");
                view.forward(request, response);
                return;
            } else {
                session.setAttribute("AutoGroupResult", json);
                response.sendRedirect("auto_group.jsp");
                return;
            }
        }
        
        // retrieve all user location within query window
        TreeMap<String, ArrayList<UserLocation>> locationMap = AutoGroupDAO.retrieveLocations(inputDateTime);
        int totalUsers = locationMap.size();
        
        //if locationMap is empty, will send messge to user
        if (locationMap.isEmpty()) {
            messages.add("no data for input date time");
            Collections.sort(messages);
            GroupContainer toReturn = new GroupContainer("success", messages);            
            String json = gson.toJson(toReturn);

            if (notJson == null) {
                //----- since notJson == null means this is a json request

                //Set the json return to an attribute and forward
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/group_detect");
                view.forward(request, response);
                return;
            } else {
                session.setAttribute("AutoGroupResult", json);
                response.sendRedirect("auto_group.jsp");
                return;
            }
        }
        
        //if totalUser, there will be no group
        if(totalUsers == 1){
            messages.add("no group formed");
            GroupContainer toReturn = new GroupContainer("success", messages);
            String json = gson.toJson(toReturn);
            if (notJson == null) {
                //----- since notJson == null means this is a json request

                //Set the json return to an attribute and forward
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/group_detect");
                view.forward(request, response);
                return;
            } else {
                session.setAttribute("AutoGroupResult", json);
                response.sendRedirect("auto_group.jsp");
                return;
            }
        }
        
        //to store the new formed userlocation
        TreeMap<String, List<UserLocation>> newLocationMap = new TreeMap<>();
        for (Map.Entry<String, ArrayList<UserLocation>> entry : locationMap.entrySet()) {
            List<UserLocation> userLocList = entry.getValue();
            String macAddress = entry.getKey();
            //combine the consecutive location updates from same location id, then set end time for each location    
            List<UserLocation> newUserLocList = UserLocation.setEndTimeForAll(userLocList, inputDateTime);
            for(UserLocation loc: newUserLocList) {
               System.out.println(loc.getEmail()); 
            }
            newLocationMap.put(macAddress, newUserLocList);
        }

        // remove all user with total staying time less than 12 mins
        Iterator<String> iter = newLocationMap.keySet().iterator();
        while(iter.hasNext()) {
            List<UserLocation> value = newLocationMap.get(iter.next());

            if (!moreThan12Mins(value)) {
                iter.remove();
            }
        }
        
        /* start checking for pairs*/
        ArrayList<Group> pairs = new ArrayList<>();

        //get all valid mac addresses within 15 minute window that spends at least 12 minutes in building
        ArrayList<String> validMac = new ArrayList<String>(newLocationMap.keySet());

        //loop through valid mac addresses
        for (int i = 0; i < validMac.size() - 1; i++) {
            //get all location updates of current user (i)
            List<UserLocation> locListI = newLocationMap.get(validMac.get(i));
            String emailI = locListI.get(0).getEmail();

            String macI = validMac.get(i);
            //loop through all valid mac address with position after current user (j)
            for (int j = i + 1; j < validMac.size(); j++) {
                List<UserLocation> locListJ = newLocationMap.get(validMac.get(j));
                String emailJ = locListJ.get(0).getEmail();
                String macJ = validMac.get(j);

                ArrayList<UserLocation> pairLocUpdates = new ArrayList<>();
                long togetherTime = 0;

                //loop through user i's location updates
                for (UserLocation userLocation : locListI) {
                    //loop through user j's location updates
                    for (UserLocation anotherLocation : locListJ) {
                        //check if both location updates have same location ID
                        if (userLocation.isSameLocID(anotherLocation)) {
                            //if user i's start time is not after user j's end time AND user i's end time is not before user j's start time, they are in the same location for a certain time
                            if (!(userLocation.getDateTime().after(anotherLocation.getEndTime())|| userLocation.getEndTime().before(anotherLocation.getDateTime()))) {
                                Timestamp maxSTime = null;
                                Timestamp minETime = null;
                                //if user i's start time is after user j's start time, set companion start time to be user i's 
                                //otherwise, set companion start time to be user j's
                                if (userLocation.getDateTime().after(anotherLocation.getDateTime())) {
                                    maxSTime = userLocation.getDateTime();
                                } else {
                                    maxSTime = anotherLocation.getDateTime();
                                }

                                //if user i's end time is before user j's end time, set companion end time to be user i's 
                                //otherwise, set companion end time to be user j's
                                if (userLocation.getEndTime().before(anotherLocation.getEndTime())) {
                                    minETime = userLocation.getEndTime();
                                } else {
                                    minETime = anotherLocation.getEndTime();
                                }

                                //if companion start time and companion end time are not the same, get the duration
                                if (maxSTime.compareTo(minETime) != 0) {
                                    togetherTime += minETime.getTime() - maxSTime.getTime();
                                    //create a new commonLocation Update with companion start time and companion end time
                                    UserLocation commonLoc = new UserLocation(maxSTime, minETime, userLocation.getLocationID());

                                    pairLocUpdates.add(commonLoc);
                                }
                            }
                        }
                    }    
                }
                
                //end loops comparing location updates for both users
                //still in loop comparing i and j
                //check if pair is together for 12 minutes or more
                if(togetherTime >= 12*60*1000) {
                    ArrayList<User> users = new ArrayList<>();
                    User userThis;
                    User userAnother;
                    //make user i
                    if (emailI == null) {
                      userThis = new User(macI);
                   } else {
                      userThis = new User(macI, emailI);
                   }
                    //make user j
                    if (emailJ == null) {
                       userAnother = new User(macJ);
                    } else {
                       userAnother = new User(macJ, emailJ);
                    }

                    //add both users to new user array list
                    users.add(userThis);
                    users.add(userAnother);
                                        
                    //make new group with the user array list of both users
                    Group pair = new Group(users, pairLocUpdates);
                                        
                    //add new pair group to arraylist of groups
                    pairs.add(pair);
                }
                //continue looping through more user j
            }
            //continue looping through more user i
        }
        //finished looping through all users
        //all pairs have been found
        if (pairs.size() == 0) {
            messages.add("no group formed");
            GroupContainer toReturn = new GroupContainer("success", messages);
            String json = gson.toJson(toReturn);
            if (notJson == null) {
                //----- since notJson == null means this is a json request

                //Set the json return to an attribute and forward
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/group_detect");
                view.forward(request, response);
                return;
            } else {
                session.setAttribute("AutoGroupResult", json);
                response.sendRedirect("auto_group.jsp");
                return;
            }
        }
        if (pairs.size() == 1) {
            //stop and return the only pair to user
            for (Group grp : pairs) {
                grp.setTimeInLocID();
                grp.sort();
                grp.setMacAddressList(null);
                grp.setUserLocations(null);
            }
            GroupContainer toReturn = new GroupContainer("success", totalUsers, pairs);
            String json = gson.toJson(toReturn);
            if (notJson == null) {
                //----- since notJson == null means this is a json request

                //Set the json return to an attribute and forward
                json = json.replace("total_users", "total-users");
                json = json.replace("total_groups", "total-groups");
                json = json.replace("total_time_spent", "total-time-spent");
                json = json.replace("mac_address", "mac-address");
                json = json.replace("time_spent", "time-spent");
                
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/group_detect");
                view.forward(request, response);
                return;
            } else {
                session.setAttribute("AutoGroupResult", json);
                response.sendRedirect("auto_group.jsp");
                return;
            }
        }            
        
        //takes in all valid pairs in order to find bigger groups
        ArrayList<Group> finalGrpList = findGroup(pairs);
        for (Group grp : finalGrpList) {
            grp.setTimeInLocID();
            grp.sort();
            grp.setMacAddressList(null);
            grp.setUserLocations(null);
        }

        Collections.sort(finalGrpList);

        GroupContainer groupContainer = new GroupContainer("success", totalUsers, finalGrpList);
        String json = gson.toJson(groupContainer);
        if (notJson == null) {
            
            json = json.replace("total_users", "total-users");
            json = json.replace("total_groups", "total-groups");
            json = json.replace("total_time_spent", "total-time-spent");
            json = json.replace("mac_address", "mac-address");
            json = json.replace("time_spent", "time-spent");
            
            request.setAttribute("json", json);
            RequestDispatcher view = request.getRequestDispatcher("json/group_detect");
            view.forward(request, response);
            return;
        } else {
            session.setAttribute("AutoGroupResult", json);
            response.sendRedirect("auto_group.jsp");
            return;
        }
    }

    /**
     *check if the total duration of all location is more than 12 mins, return true if more than 12 mins, false otherwise
     * @param userLocList - a list of UserLocations
     * @return boolean - boolean value
     */
    public boolean moreThan12Mins(List<UserLocation> userLocList) {
        long stayingTime = 0;
        for (UserLocation loc : userLocList) {
            stayingTime += loc.getDuration();
        }
        return stayingTime >= 12 * 60 * 1000;
    }

    /**
     *this method will find all the bigger group can be formed from minigroups, return a list of formed groups
     * @param miniGroups- a list of small groups(pairs) 
     * @return groupList - a list of formed group
     */
    public ArrayList<Group> findGroup(ArrayList<Group> miniGroups) {
        
        int count_Num_Of_Big_Group_Of_Current_Group_List = 0; //if its zero then we all cry  
        int length = 0; //add new groups here
        
        // declare unique_Mac_ADD_list
        ArrayList<User> uniqueUserList = null;
        //declare commLocationList  set both as new in loop
        ArrayList<UserLocation> commLocationList = null;
        //a list of index that will be removed from minigroups if the group can form a bigger group
        List<Integer> toBeDropped = null;
        //sample group with size 1
        Group sample = new Group();
        
        //toReturn store the newly formed groups
        ArrayList<Group>  toReturn = null;
        //tempToReturn store the group which can not form any bigger group, will be added to toReturn at the end
        ArrayList<Group> tempToReturn = new ArrayList<>();
        
        // find Big size group
        do {
            toBeDropped = new ArrayList<>(); // initialize the toBeDropped
            count_Num_Of_Big_Group_Of_Current_Group_List = 0;
            length = miniGroups.size();
            toReturn = new ArrayList<>();
            for (int i = 0; i <= length - 2; i++) {
                for (int j = i + 1; j <= length - 1; j++) {
                    uniqueUserList = new ArrayList<>();
                    commLocationList = new ArrayList<>();
                    // check_IF_Group(miniGroup.get(i),miniGroup.get(j))----this method is the same as the one from topKCompanion
                    if (miniGroups.get(i).check_IF_GroupWith(miniGroups.get(j))) {
                        uniqueUserList = miniGroups.get(i).getUniqueUsers(miniGroups.get(j)); //get all uniqueUsers from both groups
                        commLocationList = miniGroups.get(i).findCommLocations(miniGroups.get(j));// get the overlapped location of both groups
                        Group biggerGROUP = new Group(uniqueUserList, commLocationList);  // create a group object
                        
                        // check if biggerGROUP exists in toReturn
                        boolean containsGroup = false; 
                        for(Group grp: toReturn) {
                            if(grp.isSameGroup(biggerGROUP)) {
                              containsGroup = true;
                              break;
                            }
                        }
                        if(!containsGroup) {
                        //add biggerGROUP to toReturn
                            toReturn.add(biggerGROUP);
                        }
                        
                        toBeDropped.add(i);// add the index of group which can form new group
                        toBeDropped.add(j);
                        count_Num_Of_Big_Group_Of_Current_Group_List++;
                    }
                }
            }
            
            //romove duplicate integer
            List<Integer> deduped = toBeDropped.stream().distinct().collect(Collectors.toList());
          
            //set group at indexs in toBeDropped as sample group                  
            for (Integer num : deduped) {
                miniGroups.set(num, sample);
            }

            //remove group with size 1
            Iterator iter = miniGroups.iterator();
            while (iter.hasNext()) {
                Group currentGroup = (Group)iter.next();
                if (currentGroup.getSize() < 2) {
                    iter.remove();
                }
            }
            
            //add left groups in minigroups to tempToReturn;                
            for(Group grp: miniGroups) {
                tempToReturn.add(grp);
            }
            
            //set toReturn to minigroups 
            miniGroups = toReturn;
        } while (count_Num_Of_Big_Group_Of_Current_Group_List != 0 && miniGroups.size() != 1);//stop when cant formed bigger group
        
        toReturn.addAll(tempToReturn);//add tempToReturn to toReturn
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
