/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.ArrayList;
import java.util.List;

public class HeatMapContainer {
    private String status;
    private ArrayList<String> messages;
    private ArrayList<HeatMapRow> heatmap;
    
    /**
     * constructor of HeatMap Controller
     * @param status, String
     * @param messages, ArrayList
     */
    public HeatMapContainer(String status, ArrayList<String> messages) {
        this.status = status;
        this.messages = messages;
        this.heatmap = null;
    }
    
    /**
     * constructor of HeatMap Container, consisting of all the crowd density per semantic place in the form of heatmap rows
     * @param HeatMap, ArrayList
     * @param status, String
     */
    public HeatMapContainer(ArrayList<HeatMapRow> HeatMap, String status) {
        this.status = status;
        this.messages = null;
        this.heatmap = HeatMap;
    }

    /**
     * contains list of success or error messages from heatmap controller
     * @return list of return messages from heatmap controller, List
     */
    public List<String> getMessages() {
        return this.messages;
    }
    
    /**
     * shows overall status of heatmap query
     * @return status(success/fail) of the requested HeatMap container from queried floor, String
     */
    public String getStatus() {
        return this.status;
    }

    
    /**
     * obtain heatmap according to semantic place per floor
     * @return HeatMap in a form of a list of heatmap rows, per semantic place in queried row, ArrayList
     */
    public ArrayList<HeatMapRow> getHeatMap() {
        return this.heatmap;
    }
    
}