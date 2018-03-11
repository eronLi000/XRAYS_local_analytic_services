/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

public class HeatMapRow implements Comparable<HeatMapRow>{
    private String semantic_place;
    private int num_people; //str to int
    private int crowd_density;
    
    /**
     * constructor of heatmap row, allocates the number of people to the respective crowd density for output
     * @param semantic_place, String
     * @param num_people, String
     */
    public HeatMapRow(String semantic_place, int num_people){
        this.semantic_place = semantic_place;
        this.num_people = num_people;
        setCrowdDensity();
    }

    /**
     * obtains semantic place of queried level
     * @return semantic place of this row in the queried level, String
     */
    public String getSemanticPlace() {
        return semantic_place;
    }

    /**
     * Obtains the number of mac address from semantic place
     * @return value of mac address count of this row in the queried level, String
     */
    public int getMacAddCount() {
        return num_people;
    }
    
    /**
     * takes in the number of people from the constructor and allocate the respective crowd density value
     */
    public void setCrowdDensity(){
        if (num_people >= 31){
            this.crowd_density = 6;
        } else if (num_people >= 21){
            this.crowd_density = 5;
        } else if (num_people >= 11){
            this.crowd_density = 4;
        } else if (num_people >= 6){
            this.crowd_density = 3;
        } else if (num_people >= 3){
            this.crowd_density = 2;
        } else if (num_people >= 1){
            this.crowd_density = 1;
        } else {
            this.crowd_density = 0;
        }
    }

    /**
     * obtains crowd density
     * @return value of crown density of this row in the queried level, int
     */
    public int getCrowdDensity() {
        return crowd_density;
    }

    
    /**
     * Compares heatmap row by semantic place
     * @param o, heatmap row object
     * @return 0 if string lexicographically equal to this string, else a number bigger than 0, int
     */
    @Override
    public int compareTo(HeatMapRow o) {
        return this.semantic_place.compareTo(o.getSemanticPlace());
    }
    
    
}
