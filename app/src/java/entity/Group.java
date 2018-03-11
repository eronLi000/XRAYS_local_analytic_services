/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.*;

 
public class Group implements Comparable<Group>{

    private int size;
    private long total_time_spent;
    private ArrayList<User> members;
    private ArrayList<UserLocation> userLocations;
    private ArrayList<String> macAddressList;
    private List<GroupLocation> locations;
    
    /**
     *initialize a newly Group object with size 1
     */
    public Group() {
        this.size = 1;
    }

    /**
     *initialize a newly Group object with one user inside group
     * @param user - a user object
     * @param userLocations - a list of userLocations
     */
    public Group(User user, ArrayList<UserLocation> userLocations) {
        this.size = 1;
        this.userLocations = userLocations;
        this.members = new ArrayList<User>();
        this.macAddressList = new ArrayList<>();
        this.addMembersAndMacList(user);
    }

    /**
     *initialize a newly Group object with list of users inside group
     * @param members - a list of users
     * @param userLocations - a list of UserLocations
     */
    public Group(ArrayList<User> members, ArrayList<UserLocation> userLocations) {
        this.setTotal_time_spent(userLocations);
        this.size = members.size();
        this.members = members;
        this.userLocations = userLocations;
        this.macAddressList = new ArrayList<>();
        this.locations = null;
        addMacList(members);
    }
    
    /**
     *setting members and macAddress list
     * @param user - user to be added 
     */
    public void addMembersAndMacList(User user) {
        this.members.add(user);
        this.macAddressList.add(user.getMacAddress());
    }
    
    /**
     *setting macAddress list
     * @param members - a list of users
     */
    public void addMacList(ArrayList<User> members) {
        for(User user: members) {
            this.macAddressList.add(user.getMacAddress());
        }
    }
    
    /**
     *get macAddress list
     * @return macAddressList - a list of string macAddress
     */
    public ArrayList<String> getMacAddressList() {
        return macAddressList;
    }

    /**
     *get size of group
     * @return size - and int value
     */
    public int getSize() {
        return size;
    }

    /**
     *get time the group spend together
     * @return total_time_spent - long value
     */
    public long getTotal_time_spent() {
        return total_time_spent;
    }
    
    /**
     *get locations of group
     * @return locations - a list of userLocations
     */
    public List<GroupLocation> getLocations(){
        return this.locations;
    }
    
    /**
     *get the list of user in the group
     * @return members -a list of users
     */
    public ArrayList<User> getMembers() {
        return members;
    }

    /**
     *get the list of location the group has stayed
     * @return userLocations - a list of userLocations
     */
    public ArrayList<UserLocation> getUserLocations() {
        return userLocations;
    }

    /**
     *setting the size of group
     * @param size - a int value
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     *setting the members of group
     * @param members - a list of members
     */
    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }

    /**
     *setting the useLocations of group
     * @param userLocations- a list of userLocations
     */
    public void setUserLocations(ArrayList<UserLocation> userLocations) {
        this.userLocations = userLocations;
    }
    
    /**
     *setting th macAdd list of group
     * @param macAddressList - a list of string value 
     */
    public void setMacAddressList(ArrayList<String> macAddressList){
        this.macAddressList = macAddressList;
    }

    /**
     *check if two group have same members, return true if same, false otherwise
     * @param grp - a group object
     * @return boolean- a boolean value
     */
    public boolean isSameGroup(Group grp) {
        if(this.size == grp.getSize()) {
            ArrayList<String> anotherGrpMacList = grp.getMacAddressList();
            for (String mac: anotherGrpMacList) {
                if(!this.macAddressList.contains(mac)) {
                    return false;
                }
            }
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     *check if two groups can form a bigger group by check common locations of two group, 
     * return true if commLocations duration is more than 12 mins, false otherwise
     * @param grp - a group object
     * @return boolean - a boolean value
     */
    public boolean check_IF_GroupWith(Group grp) {
        //check for togetherTime and check for all location updates
        long togetherTime = 0;
        //find all subgroup locations with common location id
        ArrayList<UserLocation> commLocList = this.findCommLocations(grp);
        for (UserLocation commLoc : commLocList) {
            togetherTime += commLoc.getDuration();
        }
        if (togetherTime >= 12 * 60 * 1000) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     *get all the unique macAddress from both groups
     * @param grp - a group object
     * @return MacAddList - a list of MacAddress
     */
    public ArrayList<String> getUniqueMacs(Group grp) {
        ArrayList<String> anotherMacs = grp.getMacAddressList();
        ArrayList<String> commMacs = new ArrayList<>();
        commMacs.addAll(this.macAddressList);
        for (String anotherMac : anotherMacs) {
            boolean notContains = true;
            for (String thisMac : this.macAddressList) {
                if (anotherMac.equals(thisMac)) {
                    notContains = false;
                    break;
                }
            }
            if (notContains) {
                commMacs.add(anotherMac);
            } 
        }
        return commMacs;
        
    }

    /**
     *get all the unique users from both groups
     * @param grp - a group object
     * @return userList - a list of users
     */
    public ArrayList<User> getUniqueUsers(Group grp) {
        //check this group for other users who are in the parameter group
        //return unique macaddress list of this and the parameter group
        ArrayList<User> anotherMembers = grp.getMembers();
        
        ArrayList<User> commUsers = new ArrayList<>();
        commUsers.addAll(this.members);
        
        
        for (User anotherUser : anotherMembers) {
            boolean notContains = true;
            for (User thisUser : this.members) {
                if (anotherUser.isSameUserAs(thisUser)) {
                    notContains = false;
                    break;
                }
            }
            if (notContains) {
                commUsers.add(anotherUser);
            } 
        }
        return commUsers;
    }

    /**
     *get overLapped userLocation updates of two groups
     * @param grp - a group object
     * @return commonLocList - A list of userLocation
     */
    public ArrayList<UserLocation> findCommLocations(Group grp) {
        /*make another ist of common location ids with their respective timings*/
        
        //retrieves all location updates of this group
        ArrayList<UserLocation> thisLocList = getUserLocations();
        
        //retrieves all location updates of other group
        ArrayList<UserLocation> anotherLocList = grp.getUserLocations();
        
        ArrayList<UserLocation> groupCommonLocUpdates = new ArrayList<>();
        
        //loop through this group's location updates
        for (UserLocation userLocation : thisLocList) {
            //loop through other group's location updates
            for (UserLocation anotherLocation : anotherLocList) {
                //check if both updates are same location
                if (userLocation.isSameLocID(anotherLocation)) {
                    //if group i's start time is not after group j's end time AND group i's end time is not before group j's start time, they are in the same location for a certain time
                    if (!(userLocation.getDateTime().after(anotherLocation.getEndTime()) || userLocation.getEndTime().before(anotherLocation.getDateTime()))) {
                        Timestamp maxSTime = null;
                        Timestamp minETime = null;
                        //if group i's start time is after group j's start time, set companion start time to be group i's 
                        //otherwise, set companion start time to be group j's
                        if (userLocation.getDateTime().after(anotherLocation.getDateTime())) {
                            maxSTime = userLocation.getDateTime();
                        } else {
                            maxSTime = anotherLocation.getDateTime();
                        }

                        //if group i's end time is before group j's end time, set companion end time to be user i's 
                        //otherwise, set companion end time to be group j's
                        if (userLocation.getEndTime().before(anotherLocation.getEndTime())) {
                            minETime = userLocation.getEndTime();
                        } else {
                            minETime = anotherLocation.getEndTime();
                        }

                        //if companion start time and companion end time are not the same, get the duration
                        if (maxSTime.compareTo(minETime) != 0) {
                            //create a new commonLocation Update with companion start time and companion end time
                            UserLocation commonLoc = new UserLocation(maxSTime, minETime, userLocation.getLocationID());
                            groupCommonLocUpdates.add(commonLoc);
                        }
                    }
                }
            }
        }
        return groupCommonLocUpdates;
    }


    private void setTotal_time_spent(ArrayList<UserLocation> userLocations) {
        long togetherTime = 0;
        togetherTime = userLocations.stream().map((loc) -> loc.getDuration()).reduce(togetherTime, (accumulator, _item) -> accumulator + _item);
        this.total_time_spent = togetherTime/1000;
    }
    
    /**
     *sum the time spent in the same location id 
     */
    public void setTimeInLocID() {
        ArrayList<GroupLocation> locations = new ArrayList<>();
        HashMap<String, Long> locationMap = new HashMap<>();
        
        for(UserLocation userLocation: this.userLocations) {
            long timeSpend = 0;
            String locID = userLocation.getLocationID();
            if(locationMap.containsKey(locID)) {
                timeSpend = locationMap.get(locID);
            }
            timeSpend += userLocation.getDuration();
            locationMap.put(locID,timeSpend);
        }
        
        for(Map.Entry<String, Long> entry : locationMap.entrySet()){
            locations.add(new GroupLocation(entry.getKey(), entry.getValue()/1000));
        }
            
        this.locations = locations;
    }
    
    @Override
    public int compareTo(Group o) {

        if (this.size == o.getSize()){
            if (this.total_time_spent != o.getTotal_time_spent()){
                return (int) ((int) o.getTotal_time_spent() - this.total_time_spent);
            }
            else{
                for(int i = 0; i < this.members.size()-1; i++){
                    if(this.members.get(i).compareTo(o.getMembers().get(i)) != 0){
                        return this.members.get(i).compareTo(o.getMembers().get(i));
                    }
                }
                return this.members.get(this.members.size()-1).compareTo(o.getMembers().get(this.members.size()-1));
            }
        }
        else{
            return o.getSize() - this.size;
        }
    }
    
    /**
     *sort the group by members then by locations
     */
    public void sort(){
        Collections.sort(this.members);
        Collections.sort(this.locations);
    }
}
