/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;
 
public class NextPlace {
    private int rank;
    private String semantic_place;
    private int count;
    
    /**
     * Creates a new NextPlace Object
     * 
     * This method creates a new NextPlace Object to be used in the Top K Next Places query.
     *
     * @param rank rank of current entry
     * @param count count of users who visited the next place
     * @param semanticPlace semantic place value of current entry of next place visited
     */
    public NextPlace(int rank , int count, String semanticPlace){

        this.rank = rank;
        this.count = count;
        this.semantic_place = semanticPlace;
    }

    /**
     * Returns the ranking of the current entry. 
     * 
     * This method returns a number with the ranking of the current entry
     *
     * @return a rank number of current next place entry
     */
    public int getRank() {
        return rank;
    }

    /**
     * Returns the count of the number of user's for the current entry. 
     * 
     * This method returns a number with the number of users who visited the current next Place
     *
     * @return a count of all the users in the current entry.
     */
    public int getCount() {
        return count;
    }
    
    /**
     * Returns the semantic place value of the current entry. 
     * 
     * This method returns a semantic place name of the current entry
     *
     * @return the semantic place of the current next place entry
     */
    public String getSemanticPlace(){
        return semantic_place;
    }
}