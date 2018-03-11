/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;
 
public class Demographic {

    private String macAdd;
    private String name;
    private String password;
    private String email;
    private String gender;
    private String school;
    private String year;

    public Demographic() {

    }

    /**
     * Creates a demographic object
     * @param macAdd macaddress
     * @param name name
     * @param password password
     * @param email email
     * @param gender gender
     */
    public Demographic(String macAdd, String name, String password, String email, String gender) {
        this.macAdd = macAdd;
        this.name = name;
        this.password = password;
        this.email = email;
        this.gender = gender;
    }
    
    /**
     * Creates a demographic object that only has email and gender
     * @param email email
     * @param gender gender
     */
    public Demographic(String email, String gender){
        this.email = email;
        this.gender = gender;
        extractSchool();
        extractYear();
    }

    /**
     * get mac address of user
     * @return mac address, String
     */
    public String getMacAdd() {
        return macAdd;
    }

    /**
     * get name of user
     * @return name, String
     */
    public String getName() {
        return name;
    }

    /**
     * gets password of user 
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * gets email of user
     * @return email, String
     */
    public String getEmail() {
        return email;
    }

    /**
     * get gender of user
     * @return gender, char
     */
    public String getGender() {
        return gender;
    }
    
    /**
     * get school of user
     * @return school, String
     */
    public String getSchool(){
        return this.school;
    }
    
    /**
     * get year of user
     * @return year, String
     */
    public String getYear(){
        return this.year;
    }
    
    /**
     * extracts the school of the user from email
     */
    public void extractSchool(){
        String[] parts = email.split("@");
        String part1 = parts[1];
        String skul = part1.replaceAll(".smu.edu.sg", "");
        this.school = skul;
    }
    
    /**
     * extracts the year of the user from email
     */
    public void extractYear(){
        String[] parts = email.split("@");
        String part = parts[0];
        String extractedYear = part.substring(part.length() - 4);
        this.year = extractedYear;
    }

    //------------------- Checks for Valid SHA1 (AlphaNumeric, 40 char limit) --------------------

    /**
     * check if SHA1 is AlphaNumeric, 40 char limit
     * @param s SHA1 input, String
     * @return if valid 
     */
    public static boolean isValidSHA1(String s) {
        return s.matches("[a-fA-F0-9]{40}");
    }

    //************************************ Start Email Checker ***********************************
    //------------------- Checks for Valid Email (first check if theres [@] [.] fist) ------------

    /**
     *  Checks for Valid Email (first check if theres [@] [.] fist) 
     * @param email, String
     * @return if valid
     */
    public static boolean isValidEmail(String email) {
        if (!email.contains(".")) {
            return false;
        } else if (!email.contains("@")) {
            return false;
        }

        //--------------- Split email string into 2 parts before the @ and after the @ -----------
        String[] parts = email.split("@");
        String part1 = parts[0];
        String part2 = parts[1];

        //--------------- Checks the 1st part for AlphaNumeric and [.] ---------------------------
        if (!part1.matches("[0-9A-Za-z.]+") || !part1.contains(".")) {
            return false;
        }
        
       //---------------- Checks if there is a dot right before the year -------------------------
        char checkDot = part1.charAt(part1.length() - 5);
        if(checkDot != '.'){
            return false;
        }
     

        //--------------- Checks the 1st part for valid year by using substring ------------------
        int yearChecker;
        try{
            yearChecker = Integer.parseInt(part1.substring(part1.length() - 4));
        } catch (Exception e){
            return false;
        }
        

        if (yearChecker < 2013 || yearChecker > 2017) {
            return false;
        }

        //--------------- Checks the 2nd part for valid email ------------------------------------
        if (part2.equals("sis.smu.edu.sg")
                || part2.equals("business.smu.edu.sg")
                || part2.equals("accountancy.smu.edu.sg")
                || part2.equals("law.smu.edu.sg")
                || part2.equals("economics.smu.edu.sg")
                || part2.equals("socsc.smu.edu.sg")) {
            return true;
        }
        return false;
    }

    //------- Check if password is >= 8 and if there are any white space ------------------

    /**
     * Check if password is at least 8 chars long and if there are any white space
     * @param pwd, String
     * @return if valid
     */
    public static boolean isValidPassword(String pwd) {
        return (pwd.length() >= 8 && pwd.indexOf(' ') == -1);
    }

    /**
     * Check if gender is valid, either m or f (casing ignored)
     * @param gender, char
     * @return if valid
     */
    public static boolean isValidGender(String gender) {
        return (gender.equalsIgnoreCase("M") || gender.equalsIgnoreCase("F"));
    }
}
