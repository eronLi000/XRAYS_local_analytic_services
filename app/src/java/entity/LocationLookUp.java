/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

public class LocationLookUp {

    private String locId;
    private String semantic_place;
    
    /**
     *initialize a new locationlookup object without parameters
     */
    public LocationLookUp() {

    }

    /**
     *initialize new object with two parameters
     * @param locId - location id
     * @param semantic_place - semantic place
     */
    public LocationLookUp(String locId, String semantic_place) {
        this.locId = locId;
        this.semantic_place = semantic_place;
    }

    /**
     *returns string of location id
     * @return location id of this object
     */
    public String getLocId() {
        return locId;
    }

    /**
     *return string that refers to semantic place
     * @return semantic place
     */
    public String getSemanticPlace() {
        return semantic_place;
    }

    //------------------- Checks for Valid Loc ID (Numeric, 10 char limit) --------------------

    /**
     *checks if location id is a valid positive integer
     * @param s - location id
     * @return true if valid location id
     */
    public boolean isValidLocId(String s) {
        return s.matches("[0-9]+");
    }

    /**
     *checks if semantic place of format SMUSISL"level number""specific location" or SMUSISB"level number""specific location" and returns true if valid
     * @param s - semantic place
     * @return true if valid semantic place
     */
    public boolean isValidSemanticPlace(String s) {
        if (s.length() > 7) {
            if (s.substring(0, 7).equals("SMUSISL")) {
                try {
                    String charAtSeven = s.substring(7, 8);
                    
                    int level = Integer.parseInt(charAtSeven);
                    if (level >= 1 && level <= 5) {
                        return true;
                    }
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            if (s.substring(0, 7).equals("SMUSISB")) {
                try {
                    String charAtSeven = s.substring(7, 8);
                    int level = Integer.parseInt(charAtSeven);
                    if (level == 1) {
                        return true;
                    }
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            return false;
        }
        return false;
    }
}
