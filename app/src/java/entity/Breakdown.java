/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.List;
 
public class Breakdown implements Comparable<Breakdown>{
    private String gender;
    private String year;
    private String school;
    private int count;
    private List<Breakdown> breakdown = null;
/**
     * Creates a new Breakdown Object for gender breakdown
     * 
     * This method creates a new Breakdown Object to be used in the breakdown list.
     * This method is called when a gender breakdown is requested.
     *
     * @param count the number of Demographics objects being analyzed
     * @param gender the specific gender being queried
     * @param breakdown the list of Breakdown objects retrieved
     */
    public Breakdown(int count, String gender, List<Breakdown> breakdown){
        this.count = count;
        this.breakdown = breakdown;
        this.gender = gender;
        this.year = null;
        this.school = null;
    }
    
    /**
     * Creates a new Breakdown Object for school breakdown
     * 
     * This method creates a new Breakdown Object to be used in the breakdown list.
     * This method is called when a school breakdown is requested.
     *
     * 
     * @param school the specific school being queried
     * @param breakdown the list of Breakdown objects retrieved
     * @param count the number of Demographics objects being analyzed
     */
    public Breakdown(String school, List<Breakdown> breakdown, int count){
        this.count = count;
        this.breakdown = breakdown;
        this.gender = null;
        this.year = null;
        this.school = school;
    }
    
    /**
     * Creates a new Breakdown Object for year breakdown
     * 
     * This method creates a new Breakdown Object to be used in the breakdown list.
     * This method is called when a year breakdown is requested.
     *
     * @param count the number of Demographics objects being analyzed
     * @param breakdown the list of Breakdown objects retrieved
     * @param year the specific year being queried
     */
    public Breakdown(int count, List<Breakdown> breakdown, String year){
        this.count = count;
        this.breakdown = breakdown;
        this.gender = null;
        this.year = year;
        this.school = null;
    }
    
    /**
     * Sets breakdown attribute to parameter input 
     * 
     * @param breakdown a list of Breakdown objects
     */
    public void setBreakdown(List<Breakdown> breakdown){
        this.breakdown = breakdown;
    }
    
    /**
     * Returns a list of @link Breakdown objects. 
     * 
     * This method returns a list of breakdown objects 
     *
     * @return a list of Breakdown objects
     */
    public List<Breakdown> getBreakdown(){
        return this.breakdown;
    }
    
    /**
     * Returns a count of @link Breakdown objects. 
     * 
     * This method returns a list of breakdown objects 
     *
     * @return a list of Breakdown objects
     */
    public int getCount(){
        return this.count;
    }
    /**
     * Sets count attribute to parameter input 
     * 
     * @param count the new count value
     */
    public void setCount(int count){
        this.count = count;
    }

    public String getGender() {
        return gender;
    }
    
    /**
     * Sets gender attribute to parameter input 
     * 
     * @param gender the new gender value
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getYear() {
        return year;
    }
    
    /**
     * Sets year attribute to parameter input 
     * 
     * @param year the new year value
     */
    public void setYear(String year) {
        this.year = year;
    }

    public String getSchool() {
        return school;
    }
    
    /**
     * Sets school attribute to parameter input 
     * 
     * @param school the new school value
     */
    public void setSchool(String school) {
        this.school = school;
    }

    @Override
    public int compareTo(Breakdown o) {
        if(this.gender != null && o.getGender() != null){
            if (this.gender.equals(o.getGender())) {
                return 0;
            } else if (this.gender.equalsIgnoreCase("M")) {
                return -1;
            } else {
                return 1;
            }
        }
        return 0;
    }
}

