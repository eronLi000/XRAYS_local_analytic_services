/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.ArrayList;


public class BSError {

    private String file;
    private int line;
    private ArrayList<String> messages;

    public BSError(String file, int line, ArrayList<String> errMsgArr) {
        this.file = file;
        this.line = line;
        this.messages = errMsgArr;
    }

    /**
     * @return the file which error occured (demo, location, llu), String
     */
    public String getFile() {
        return file;
    }

    /**
     * @return line which error occurs, int
     */
    public int getLine() {
        return line;
    }

    /**
     * @return the errMsgArr, ArrayList of String
     */
    public ArrayList<String> getErrMsgArr() {
        return messages;
    }
}
