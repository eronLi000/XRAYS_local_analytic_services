/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

public class HeatMap_points {
    double x;
    double y;

    /**
     * constructor for the plotting of coordinates for graphical display of heatmap
     * @param x horizontal coordinate, double
     * @param y vertical coordinate, double
     */
    public HeatMap_points(double x, double y){
        this.x = x;
        this.y = y;
    }
}
