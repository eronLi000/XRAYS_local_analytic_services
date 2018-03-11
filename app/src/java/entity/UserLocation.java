/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UserLocation {

    private Timestamp dateTime;
    private String locationID;
    private long timeInMSeconds;
    private String semanticPlace;
    private Timestamp endTime;
    private long endTimeInMSeconds;
    private String email;

    /**
     *Initializes a newly UserLocation object so that it represents an location updates
     * constructor used for AGD individuals
     * @param email - a string email
     * @param dateTime - a datetime in Timestamp type
     * @param locationID - a datetime in Timestamp type
     */
    public UserLocation(String email, Timestamp dateTime, String locationID) {
        this.dateTime = dateTime;
        this.locationID = locationID;
        this.email = email;
    }

    /**
     *Initializes a newly UserLocation object so that it represents an location updates
     * @param dateTime - a datetime in Timestamp type
     * @param locationID - a datetime in Timestamp type
     * @param semanticPlace -a string semanticPlace
     */
    public UserLocation(Timestamp dateTime, String locationID, String semanticPlace) {
        this.dateTime = dateTime;
        this.semanticPlace = semanticPlace;
        this.locationID = locationID;
        iWantMyMSeconds();
    }

    /**
     *Initializes a newly UserLocation object so that it represents an location updates
     * constructor used for AGD pairs and groups
     * @param dateTime - a start time in Timestamp type
     * @param endTime - end time in timestamp type
     * @param locationID - a string locationID
     */
    public UserLocation(Timestamp dateTime, Timestamp endTime, String locationID) {
        this.dateTime = dateTime;
        this.endTime = endTime;
        this.locationID = locationID;
    }

    /**
     *Initializes a newly UserLocation object so that it represents an location updates
     * @param dateTime - a datetime in Timestamp type
     * @param locationID - a string locationID
     */
    public UserLocation(Timestamp dateTime, String locationID) {
        this.dateTime = dateTime;
        this.locationID = locationID;
        iWantMyMSeconds();
    }

    /**
     *get starttime of location
     * @return dateTime - a datetime in Timestamp type
     */
    public Timestamp getDateTime() {
        return dateTime;
    }


    /**
     *get the email of user in the location
     * @return email- a string email
     */
    public String getEmail() {
        return this.email;
    }
    
    /**
     *get endtime of location updates
     * @return endTime - a timestamp 
     */
    public Timestamp getEndTime() {
        return endTime;
    }

    /**
     *get semantic place of location
     * @return semanticPlace - a string semantic place
     */
    public String getSemanticPlace() {
        return semanticPlace;
    }

    /**
     *get location id of location update
     * @return locationID - a string locationID
     */
    public String getLocationID() {
        return locationID;
    }

    /**
     *get the duration user stays in the location
     * @return duration - a long value
     */
    public long getDuration() {
        return (this.endTime.getTime() - this.dateTime.getTime());
    }

    /**
     *get timeInMSeconds
     * @return timeInMSeconds - a long value
     */
    public long getTimeInMSeconds() {
        return timeInMSeconds;
    }

    /**
     *get endTimeInMSeconds
     * @return endTimeInMSeconds - a long value
     */
    public long getEndTimeInMSeconds() {
        return endTimeInMSeconds;
    }

    /**
     *setting the start time of location update
     * @param dateTime - a datetime in Timestamp
     */
    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    /**
     *setting the end time of location update
     * @param endTime - a datetime in Timestamp
     */
    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    /**
     *setting semantic place of location update
     * @param semanticPlace - a string semantic place
     */
    public void setSemanticPlace(String semanticPlace) {
        this.semanticPlace = semanticPlace;
    }

    /**
     *setting email of location update
     * @param email - a string email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *setting location id of the location update
     * @param locationID- a string locationId
     */
    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    /**
     *setting the start time in MSeconds
     * @param timeInMSeconds - a long value
     */
    public void setTimeInMSeconds(long timeInMSeconds) {
        this.timeInMSeconds = timeInMSeconds;
    }

    /**
     *setting the end time in MSeconds
     * @param endTimeInMSeconds -a long value
     */
    public void setEndTimeInMSeconds(long endTimeInMSeconds) {
        this.endTimeInMSeconds = endTimeInMSeconds;
    }

    /**
     *set start time in MSeconds
     */
    public void iWantMyMSeconds() {
        this.timeInMSeconds = this.dateTime.getTime();
    }

    /**
     *moethod setting end time in MSeconds
     */
    public void iWantMyEndMSeconds() {
        this.endTimeInMSeconds = this.endTime.getTime();
    }

    /**
     *check if two locations have the same locationID,return true if two location has the same location id, false otherwise
     * @param userLoc - a UserLocation object
     * @return boolean- a boolean value
     */
    public boolean isSameLocID(UserLocation userLoc) {
        return this.locationID.equals(userLoc.getLocationID());
    }

    /**
     *method takes in inputDateTime and a list of userlocations, methods will combine consecutive locations at the same location id
     * and set end time of each location id,
     * when this meothod is used in companion, the userLocation has no email
     * when this method is called from AGD, the userLocation may have email
     * @param userLocList - a list of userlOcation
     * @param inputDateTime - a datetime in Timestamp type
     * @return newUserLocList  - a list of new location
     */
    public static List<UserLocation> setEndTimeForAll(List<UserLocation> userLocList, Timestamp inputDateTime) {
        List<UserLocation> newUserLocList = new ArrayList<>();
        int userLocListSize = userLocList.size();
        UserLocation currentUserLoc = userLocList.get(userLocListSize - 1);
        Timestamp endTime = inputDateTime;
        Timestamp startTime = currentUserLoc.getDateTime();
        String currentLocID = currentUserLoc.getLocationID();
        String email = currentUserLoc.getEmail();
        System.out.println("========current Email======:" + email);
        long timeDifference = endTime.getTime() - startTime.getTime();

        if (timeDifference > 300000) {
            endTime = new java.sql.Timestamp(startTime.getTime() + 300000);
        }

        if (userLocListSize == 1) {
            UserLocation newLoc = new UserLocation(startTime, endTime, currentLocID); 
            if(email == null) {
                newLoc.setEmail("");
            }
            else {
                newLoc.setEmail(email);
            }
            newUserLocList.add(newLoc);
        } else {
            UserLocation nextUserLoc = null;
            String nextLocID = "";
            Timestamp nextUserLocTime = null;
            for (int i = userLocListSize - 2; i >= 0; i--) {
                nextUserLoc = userLocList.get(i);
                nextLocID = nextUserLoc.getLocationID();
                nextUserLocTime = nextUserLoc.getDateTime();

                if (currentLocID.equals(nextLocID) && startTime.getTime() - nextUserLocTime.getTime() <= 300000) {
                    startTime = nextUserLocTime;
                } else {
                    UserLocation newLoc = new UserLocation(startTime, endTime, currentLocID); 
                    if(email == null) {
                        newLoc.setEmail("");
                    }
                    else {
                        newLoc.setEmail(email);
                    }
                    newUserLocList.add(newLoc);
                    currentLocID = nextLocID;

                    if (startTime.getTime() - nextUserLocTime.getTime() > 300000) {
                        startTime = nextUserLocTime;
                        endTime = new java.sql.Timestamp(startTime.getTime() + 300000);
                        currentLocID = nextLocID;
                    } else {
                        endTime = startTime;
                        startTime = nextUserLocTime;
                        currentLocID = nextLocID;
                    }
                }
            }
            UserLocation newLoc = new UserLocation(startTime, endTime, currentLocID); 
            if(email == null) {
                newLoc.setEmail("");
            }
            else {
                newLoc.setEmail(email);
            }
            newUserLocList.add(newLoc);
        }

        return newUserLocList;
    }
}
