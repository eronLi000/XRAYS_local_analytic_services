/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

 
public class PopularPlace {
    private int rank;
    private String semantic_place;
    private Integer count;
    
    /**
     * Creates the PopularPlace object to be thrown within the PopularPlace container
     * @param rank rank based on order 
     * @param semanticPlace name of the semantic place
     * @param count occurrence
     */
    
    public PopularPlace(int rank , String semanticPlace, int count){
        this.rank = rank;
        this.semantic_place = semanticPlace;
        this.count = count;
    }
    
    /**
     * 
     * @return the rank
     */
    public int getRank() {
        return rank;
    }
    
    /**
     * 
     * @return the count
     */
    public int getCount() {
        return count;
    }
    
    /**
     * 
     * @return the semantic place
     */
    
    public String getSemanticPlace(){
        return semantic_place;
    }
}
