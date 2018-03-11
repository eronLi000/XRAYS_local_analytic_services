/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import dao.HeatMapDAO;
import java.util.ArrayList;
import java.util.List;

public class HeatMap_map {
    int value;
    ArrayList<HeatMap_points> points; 
    
    /**
     * To generate graphical display of heatmap
     * @param level specific floor to be converted to graphical form, int
     * @param value assigned value for respective colour to show density, int
     * @param loc specific semantic place to be converted to graphical form, String
     */
    public HeatMap_map(int level, int value, String loc){
        this.value = value;
        this.points = generatePoints(level,loc);
    }
    
    /**
     * Generate points on the graphical heatmap display
     * @param level specific floor to be plotted, int
     * @param loc specific semantic place to be plotted, String
     * @return  Arraylist that is thrown to generate heatmap display, ArrayList
     */
    public static ArrayList<HeatMap_points> generatePoints(int level, String loc){
        ArrayList<HeatMap_points> result = new ArrayList<>();
        
        List<Integer> compareLocID = HeatMapDAO.retrieveLocID(loc);
        
        //--------------- Start of XY plots for Basement 1 ----------------------
        if(level == 0){
            //----------- Start of XY plots for SMUSISB1NearLiftLobby
            List<Integer> sisb1nearliftlobby = new ArrayList<>();
            sisb1nearliftlobby.add(1010110001);
            sisb1nearliftlobby.add(1010110002);
            sisb1nearliftlobby.add(1010110003);
            sisb1nearliftlobby.add(1010110004);
            sisb1nearliftlobby.add(1010110006);
            sisb1nearliftlobby.add(1010110008);
            sisb1nearliftlobby.add(1010110009);
            sisb1nearliftlobby.add(1010110010);
            sisb1nearliftlobby.add(1010110011);
            sisb1nearliftlobby.add(1010110012);
            sisb1nearliftlobby.add(1010110013);
            sisb1nearliftlobby.add(1010110014);
            sisb1nearliftlobby.add(1010110015);
            sisb1nearliftlobby.add(1010110016);
            sisb1nearliftlobby.add(1010110017);
            sisb1nearliftlobby.add(1010110018);
            sisb1nearliftlobby.add(1010110020);
            
            if(compareLocID.equals(sisb1nearliftlobby)){
                
                HeatMap_points[] pa = new HeatMap_points[18];
                pa[0] = new HeatMap_points(31.77,10.35);
                pa[1] = new HeatMap_points(31.77,9.05);
                pa[2] = new HeatMap_points(30.22,9.05);
                pa[3] = new HeatMap_points(30.22,8.06);
                pa[4] = new HeatMap_points(30.06,8.06);
                pa[5] = new HeatMap_points(30.06,6.61);
                pa[6] = new HeatMap_points(27.57,6.61);
                pa[7] = new HeatMap_points(27.57,3.86);
                pa[8] = new HeatMap_points(31.15,3.86);
                pa[9] = new HeatMap_points(31.15,4);
                pa[10] = new HeatMap_points(33.4,4);
                pa[11] = new HeatMap_points(33.4,5.1);
                pa[12] = new HeatMap_points(34.4,5.1);
                pa[13] = new HeatMap_points(34.4,5.59);
                pa[14] = new HeatMap_points(33.71,5.59);
                pa[15] = new HeatMap_points(33.71,7.78);
                pa[16] = new HeatMap_points(35.51,7.78);
                pa[17] = new HeatMap_points(35.51,10.35);
                
                for(int i = 0 ; i < 18; i++){
                    result.add(pa[i]);
                }                
            }
            //----------- End of XY plots for SMUSISB1NearLiftLobby
           
            
            //----------- Start of XY plots for SMUSISB1NearCSRAndTowardsMRT
            List<Integer> sisb1nearcsr = new ArrayList<>();
            sisb1nearcsr.add(1010110040);
            sisb1nearcsr.add(1010110041);
            sisb1nearcsr.add(1010110043);
            sisb1nearcsr.add(1010110044);
            sisb1nearcsr.add(1010110045);
            sisb1nearcsr.add(1010110046);
            sisb1nearcsr.add(1010110047);
            sisb1nearcsr.add(1010110048);
            sisb1nearcsr.add(1010110052);
            sisb1nearcsr.add(1010110053);
            sisb1nearcsr.add(1010110054);
            sisb1nearcsr.add(1010110055);
            sisb1nearcsr.add(1010110056);
            sisb1nearcsr.add(1010110057);
            sisb1nearcsr.add(1010110058);
            sisb1nearcsr.add(1010110059);
            sisb1nearcsr.add(1010110061);
            sisb1nearcsr.add(1010110062);
            sisb1nearcsr.add(1010110063);
            sisb1nearcsr.add(1010110064);
            sisb1nearcsr.add(1010110065);
            sisb1nearcsr.add(1010110066);
            sisb1nearcsr.add(1010110068);
            sisb1nearcsr.add(1010110069);
            sisb1nearcsr.add(1010110071);
            
            if(compareLocID.equals(sisb1nearcsr)){
                HeatMap_points[] pa = new HeatMap_points[4];
                pa[0] = new HeatMap_points(28.75,29.67);
                pa[1] = new HeatMap_points(28.75,18.2);
                pa[2] = new HeatMap_points(35.5,18.2);
                pa[3] = new HeatMap_points(35.5,29.67);
                
                for(int i = 0 ; i < 4; i++){
                    result.add(pa[i]);
                }
            }
            //----------- End of XY plots for SMUSISB1NearCSRAndTowardsMRT

            //----------- Start of XY plots for SMUSISB1NearATM
            List<Integer> sisb1nearatm = new ArrayList<>();
            sisb1nearatm.add(1010110021);
            sisb1nearatm.add(1010110022);
            sisb1nearatm.add(1010110023);
            sisb1nearatm.add(1010110024);
            sisb1nearatm.add(1010110025);
            sisb1nearatm.add(1010110026);
            sisb1nearatm.add(1010110027);
            sisb1nearatm.add(1010110029);
            sisb1nearatm.add(1010110032);
            sisb1nearatm.add(1010110035);
            sisb1nearatm.add(1010110036);
            sisb1nearatm.add(1010110037);
            sisb1nearatm.add(1010110038);
            
            if(compareLocID.equals(sisb1nearatm)){
                HeatMap_points[] pa = new HeatMap_points[16];
                pa[0] = new HeatMap_points(27.18,18.19);
                pa[1] = new HeatMap_points(27.18,16.92);
                pa[2] = new HeatMap_points(31.77,16.92);
                pa[3] = new HeatMap_points(31.77,10.35);
                pa[4] = new HeatMap_points(35.44,10.35);
                pa[5] = new HeatMap_points(35.44,11);
                pa[6] = new HeatMap_points(36.4,11);
                pa[7] = new HeatMap_points(36.4,13.46);
                pa[8] = new HeatMap_points(35.47,13.46);
                pa[9] = new HeatMap_points(35.47,14.55);
                pa[10] = new HeatMap_points(37.02,14.55);
                pa[11] = new HeatMap_points(37.02,16.85);
                pa[12] = new HeatMap_points(38.35,16.85);
                pa[13] = new HeatMap_points(38.35,17.66);
                pa[14] = new HeatMap_points(35.47,17.6);
                pa[15] = new HeatMap_points(35.47,18.19);
                
                for(int i = 0 ; i < 16; i++){
                    result.add(pa[i]);
                }
            }
            //----------- End of XY plots for SMUSISB1NearATM
        }
        //--------------- End of XY plots for Basement 1 ----------------------
        
        //--------------- Start of XY plots for Level 1 ----------------------
        if(level == 1){
            
            //----------- Start of XY plots for SMUSISL1ReceptionAndLobby
            List<Integer> sisl1reception = new ArrayList<>();
            sisl1reception.add(1010100009);
            sisl1reception.add(1010100013);
            sisl1reception.add(1010100015);
            sisl1reception.add(1010100016);
            sisl1reception.add(1010100017);
            sisl1reception.add(1010100021);
            sisl1reception.add(1010100023);
            sisl1reception.add(1010100025);
            sisl1reception.add(1010100026);
            sisl1reception.add(1010100027);
            sisl1reception.add(1010100028);
            sisl1reception.add(1010100029);
            sisl1reception.add(1010100033);
            sisl1reception.add(1010100035);
            sisl1reception.add(1010100037); 
            
            if(compareLocID.equals(sisl1reception)){
                HeatMap_points[] pa = new HeatMap_points[16];
                pa[0] = new HeatMap_points(12.6,21.47);
                pa[1] = new HeatMap_points(12.6,20.69);
                pa[2] = new HeatMap_points(11.55,20.69);
                pa[3] = new HeatMap_points(11.55,19.86);
                pa[4] = new HeatMap_points(11.87,19.79);
                pa[5] = new HeatMap_points(11.87,19);
                pa[6] = new HeatMap_points(12.61,19);
                pa[7] = new HeatMap_points(12.61,13.35);
                pa[8] = new HeatMap_points(15.47,13.35);
                pa[9] = new HeatMap_points(15.47,12.96);
                pa[10] = new HeatMap_points(21.47,12.96);
                pa[11] = new HeatMap_points(21.47,12.47);
                pa[12] = new HeatMap_points(23.16,12.47);
                pa[13] = new HeatMap_points(23.16,18.33);
                pa[14] = new HeatMap_points(17.6,18.33);
                pa[15] = new HeatMap_points(16.23,21.47);
                
                for(int i = 0 ; i < 16; i++){
                    result.add(pa[i]);
                }                
            }
            //----------- End of XY plots for SMUSISL1ReceptionAndLobby
            
            //----------- Start of XY plots for SMUSISL1NearSubway
            List<Integer> sisl1nearsubway = new ArrayList<>();
            sisl1nearsubway.add(1010100050);
            sisl1nearsubway.add(1010100051);
            sisl1nearsubway.add(1010100053);
            sisl1nearsubway.add(1010100058);
            sisl1nearsubway.add(1010100059);
            sisl1nearsubway.add(1010100060);
            sisl1nearsubway.add(1010100061);
            sisl1nearsubway.add(1010100062);
            sisl1nearsubway.add(1010100063);
            sisl1nearsubway.add(1010100064);
            sisl1nearsubway.add(1010100065);
            sisl1nearsubway.add(1010100066);
            sisl1nearsubway.add(1010100067);
            
            if(compareLocID.equals(sisl1nearsubway)){
                HeatMap_points[] pa = new HeatMap_points[6];
                pa[0] = new HeatMap_points(23.2,12.93);
                pa[1] = new HeatMap_points(23.2,7.36);
                pa[2] = new HeatMap_points(31.13,7.36);
                pa[3] = new HeatMap_points(31.13,9.68);
                pa[4] = new HeatMap_points(36.81,9.68);
                pa[5] = new HeatMap_points(36.81,12.93);

                
                for(int i = 0 ; i < 6; i++){
                    result.add(pa[i]);
                }
            }
            //----------- End of XY plots for SMUSISL1NearSubway
            
            //----------- Start of XY plots for SMUSISL1FilteringArea
            List<Integer> sisl1nearfilterarea = new ArrayList<>();
            sisl1nearfilterarea.add(1010100041);
            sisl1nearfilterarea.add(1010100042);
            sisl1nearfilterarea.add(1010100043);
            sisl1nearfilterarea.add(1010100044);
            sisl1nearfilterarea.add(1010100045);
            sisl1nearfilterarea.add(1010100046);
            sisl1nearfilterarea.add(1010100047);
            sisl1nearfilterarea.add(1010100048);
            sisl1nearfilterarea.add(1010100049);
            sisl1nearfilterarea.add(1010100052);
            sisl1nearfilterarea.add(1010100054);
            sisl1nearfilterarea.add(1010100055);
            sisl1nearfilterarea.add(1010100056);
            sisl1nearfilterarea.add(1010100057);
            
            if(compareLocID.equals(sisl1nearfilterarea)){
                HeatMap_points[] pa = new HeatMap_points[15];
                pa[0] = new HeatMap_points(20.87,19.91);
                pa[1] = new HeatMap_points(20.87,18.47);
                pa[2] = new HeatMap_points(22.14,18.47);
                pa[3] = new HeatMap_points(22.14,18.96);
                pa[4] = new HeatMap_points(23.9,18.96);
                pa[5] = new HeatMap_points(23.9,18.49);
                pa[6] = new HeatMap_points(25.84,18.47);
                pa[7] = new HeatMap_points(25.84,17.09);
                pa[8] = new HeatMap_points(23.79,17.09);
                pa[9] = new HeatMap_points(23.79,16.42);
                pa[10] = new HeatMap_points(25.14,16.42);
                pa[11] = new HeatMap_points(25.14,13.28);
                pa[12] = new HeatMap_points(36.79,13.28);
                pa[13] = new HeatMap_points(36.79,16.72);
                pa[14] = new HeatMap_points(32.03,19.91);

                
                for(int i = 0 ; i < 15; i++){
                    result.add(pa[i]);
                }
            }
            //----------- End of XY plots for SMUSISL1FilteringArea

        }
        //--------------- End of XY plots for Level 1 ----------------------
        
        //--------------- Start of XY plots for Level 2 ----------------------
        if(level == 2){
            
            //----------- Start of XY plots for SMUSISL2LiftLobby
            List<Integer> sisl2liftlobby = new ArrayList<>();
            sisl2liftlobby.add(1010200001);
            sisl2liftlobby.add(1010200003);
            sisl2liftlobby.add(1010200010);
            sisl2liftlobby.add(1010200011);
            sisl2liftlobby.add(1010200012);
            sisl2liftlobby.add(1010200014);
            sisl2liftlobby.add(1010200016);
            sisl2liftlobby.add(1010200018);
            sisl2liftlobby.add(1010200019);
            sisl2liftlobby.add(1010200025);
            sisl2liftlobby.add(1010200027);
            sisl2liftlobby.add(1010200029);
            sisl2liftlobby.add(1010200031);
            
            if(compareLocID.equals(sisl2liftlobby)){
                HeatMap_points[] pa = new HeatMap_points[18];
                pa[0] = new HeatMap_points(14.73,18.68);
                pa[1] = new HeatMap_points(14.73,12.29);
                pa[2] = new HeatMap_points(16.74,12.29);
                pa[3] = new HeatMap_points(16.74,11.92);
                pa[4] = new HeatMap_points(17.66,11.92);
                pa[5] = new HeatMap_points(17.66,12.33);
                pa[6] = new HeatMap_points(25.06,12.33);
                pa[7] = new HeatMap_points(25.06,15.72);
                pa[8] = new HeatMap_points(24.82,15.72);
                pa[9] = new HeatMap_points(24.82,17.41);
                pa[10] = new HeatMap_points(21.96,17.41);
                pa[11] = new HeatMap_points(21.96,15.86);
                pa[12] = new HeatMap_points(19.81,15.86);
                pa[13] = new HeatMap_points(19.81,16.49);
                pa[14] = new HeatMap_points(18.68,16.49);
                pa[15] = new HeatMap_points(18.68,15.86);
                pa[16] = new HeatMap_points(16.03,15.86);
                pa[17] = new HeatMap_points(16.03,18.68);
                
                for(int i = 0 ; i < 18; i++){
                    result.add(pa[i]);
                }                
            }
            //----------- End of XY plots for SMUSISL2LiftLobby
            
            //----------- Start of XY plots for SMUSISL2StudyArea1
            List<Integer> sisl2studyarea1 = new ArrayList<>();
            sisl2studyarea1.add(1010200021);
            sisl2studyarea1.add(1010200023);
            sisl2studyarea1.add(1010200034);
            sisl2studyarea1.add(1010200035);
            sisl2studyarea1.add(1010200037);
            sisl2studyarea1.add(1010200042);
            sisl2studyarea1.add(1010200127);
            sisl2studyarea1.add(1010200132);
            sisl2studyarea1.add(1010200135);
            sisl2studyarea1.add(1010200137);
            sisl2studyarea1.add(1010200141);
            
            if(compareLocID.equals(sisl2studyarea1)){
                HeatMap_points[] pa = new HeatMap_points[26];
                pa[0] = new HeatMap_points(12.89,17.37);
                pa[1] = new HeatMap_points(12.89,16.7);
                pa[2] = new HeatMap_points(11.52,16.7);
                pa[3] = new HeatMap_points(11.52,15.72);
                pa[4] = new HeatMap_points(12.44,15.72);
                pa[5] = new HeatMap_points(12.44,13.26);
                pa[6] = new HeatMap_points(9.57,13.83);
                pa[7] = new HeatMap_points(8.35,10.82);
                pa[8] = new HeatMap_points(9.13,10.56);
                pa[9] = new HeatMap_points(9.38,10.78);
                pa[10] = new HeatMap_points(14.59,10.78);
                pa[11] = new HeatMap_points(14.59,8.87);
                pa[12] = new HeatMap_points(18.01,8.87);
                pa[13] = new HeatMap_points(18.01,8.55);
                pa[14] = new HeatMap_points(18.33,8.55);
                pa[15] = new HeatMap_points(18.33,7.88);
                pa[16] = new HeatMap_points(19.77,7.88);
                pa[17] = new HeatMap_points(19.77,8.55);
                pa[18] = new HeatMap_points(19.98,8.55);
                pa[19] = new HeatMap_points(19.98,9.12);
                pa[20] = new HeatMap_points(22.21,9.12);
                pa[21] = new HeatMap_points(22.21,10.46);
                pa[22] = new HeatMap_points(16,10.46);
                pa[23] = new HeatMap_points(16,12.33);
                pa[24] = new HeatMap_points(14.8,12.33);
                pa[25] = new HeatMap_points(14.8,17.37);
                
                for(int i = 0 ; i < 26; i++){
                    result.add(pa[i]);
                }
            }
            //----------- End of XY plots for SMUSISL2StudyArea1
            
            //----------- Start of XY plots for SMUSISL2StudyArea2
            List<Integer> sisl2studyarea2 = new ArrayList<>();
            sisl2studyarea2.add(1010200044);
            sisl2studyarea2.add(1010200045);
            sisl2studyarea2.add(1010200046);
            sisl2studyarea2.add(1010200048);
            sisl2studyarea2.add(1010200050);
            sisl2studyarea2.add(1010200052);
            sisl2studyarea2.add(1010200054);
            sisl2studyarea2.add(1010200057);
            sisl2studyarea2.add(1010200059);
            sisl2studyarea2.add(1010200061);
            sisl2studyarea2.add(1010200063);
            sisl2studyarea2.add(1010200066);
            sisl2studyarea2.add(1010200084);
            sisl2studyarea2.add(1010200086);
            sisl2studyarea2.add(1010200088);
            
            if(compareLocID.equals(sisl2studyarea2)){
                HeatMap_points[] pa = new HeatMap_points[18];
                pa[0] = new HeatMap_points(23.79,12.4);
                pa[1] = new HeatMap_points(23.79,10.55);
                pa[2] = new HeatMap_points(22.21,10.55);
                pa[3] = new HeatMap_points(22.21,9.15);
                pa[4] = new HeatMap_points(31.38,9.15);
                pa[5] = new HeatMap_points(31.38,8.5);
                pa[6] = new HeatMap_points(31.7,8.5);
                pa[7] = new HeatMap_points(31.7,7.88);
                pa[8] = new HeatMap_points(33.14,7.88);
                pa[9] = new HeatMap_points(33.14,8.52);
                pa[10] = new HeatMap_points(33.46,8.52);
                pa[11] = new HeatMap_points(33.46,8.8);
                pa[12] = new HeatMap_points(36.81,8.8);
                pa[13] = new HeatMap_points(36.81,9.7);
                pa[14] = new HeatMap_points(37.4,9.7);
                pa[15] = new HeatMap_points(37.4,11.13);
                pa[16] = new HeatMap_points(38.51,11.13);
                pa[17] = new HeatMap_points(38.51,12.4);
                
                for(int i = 0 ; i < 18; i++){
                    result.add(pa[i]);
                }
            }
            //----------- End of XY plots for SMUSISL2StudyArea2
            
            //----------- Start of XY plots for SMUSISL2SR2.1
            List<Integer> sisl2sr21 = new ArrayList<>();
            sisl2sr21.add(1010200116);
            sisl2sr21.add(1010200120);
            sisl2sr21.add(1010200123);
            
            if(compareLocID.equals(sisl2sr21)){
                HeatMap_points[] pa = new HeatMap_points[23];
                pa[0] = new HeatMap_points(12.89,9.37);
                pa[1] = new HeatMap_points(12.89,8.52);
                pa[2] = new HeatMap_points(10.64,8.52);
                pa[3] = new HeatMap_points(10.64,7.8);
                pa[4] = new HeatMap_points(10.87,7);
                pa[5] = new HeatMap_points(11.39,6.37);
                pa[6] = new HeatMap_points(12.17,5.93);
                pa[7] = new HeatMap_points(12.52,5.79);
                pa[8] = new HeatMap_points(12.45,5.66);
                pa[9] = new HeatMap_points(13.56,5.36);
                pa[10] = new HeatMap_points(14.76,5.27);
                pa[11] = new HeatMap_points(15.93,5.43);
                pa[12] = new HeatMap_points(16.58,5.66);
                pa[13] = new HeatMap_points(16.53,5.79);
                pa[14] = new HeatMap_points(17.36,6.23);
                pa[15] = new HeatMap_points(17.9,6.83);
                pa[16] = new HeatMap_points(18.21,7.39);
                pa[17] = new HeatMap_points(18.29,7.88);
                pa[18] = new HeatMap_points(18.29,8.55);
                pa[19] = new HeatMap_points(17.94,8.55);
                pa[20] = new HeatMap_points(17.94,8.91);
                pa[21] = new HeatMap_points(14.55,8.91);
                pa[22] = new HeatMap_points(14.55,9.37);    
                
                for(int i = 0 ; i < 23; i++){
                    result.add(pa[i]);
                }
            }
            //----------- End of XY plots for SMUSISL2SR2.1
            
            //----------- Start of XY plots for SMUSISL2SR2.2
            List<Integer> sisl2sr22 = new ArrayList<>();
            sisl2sr22.add(1010200105);
            sisl2sr22.add(1010200109);
            sisl2sr22.add(1010200110);
            
            if(compareLocID.equals(sisl2sr22)){
                HeatMap_points[] pa = new HeatMap_points[13];
                pa[0] = new HeatMap_points(19.98,9.19);
                pa[1] = new HeatMap_points(19.98,8.59);
                pa[2] = new HeatMap_points(19.63,8.59);
                pa[3] = new HeatMap_points(19.63,7.94);
                pa[4] = new HeatMap_points(19.81,7.13);
                pa[5] = new HeatMap_points(20.28,6.49);
                pa[6] = new HeatMap_points(21.18,5.86);
                pa[7] = new HeatMap_points(22.43,5.5);
                pa[8] = new HeatMap_points(22.37,5.36);
                pa[9] = new HeatMap_points(23.69,5.2);
                pa[10] = new HeatMap_points(24.89,5.1);
                pa[11] = new HeatMap_points(25.7,5.1);
                pa[12] = new HeatMap_points(25.7,9.19);
                
                for(int i = 0 ; i < 13; i++){
                    result.add(pa[i]);
                }
            }
            //----------- End of XY plots for SMUSISL2SR2.2
            
            //----------- Start of XY plots for SMUSISL2SR2.3
            List<Integer> sisl2sr23 = new ArrayList<>();
            sisl2sr23.add(1010200092);
            sisl2sr23.add(1010200095);
            sisl2sr23.add(1010200099);
            
            if(compareLocID.equals(sisl2sr23)){
                HeatMap_points[] pa = new HeatMap_points[13];
                pa[0] = new HeatMap_points(25.7,9.12);
                pa[1] = new HeatMap_points(25.7,5.1);
                pa[2] = new HeatMap_points(26.5,5.1);
                pa[3] = new HeatMap_points(27.7,5.2);
                pa[4] = new HeatMap_points(29.01,5.36);
                pa[5] = new HeatMap_points(28.95,5.5);
                pa[6] = new HeatMap_points(30.19,5.84);
                pa[7] = new HeatMap_points(31.08,6.47);
                pa[8] = new HeatMap_points(31.56,7.09);
                pa[9] = new HeatMap_points(31.8,7.89);
                pa[10] = new HeatMap_points(31.8,8.52);
                pa[11] = new HeatMap_points(31.5,8.52);
                pa[12] = new HeatMap_points(31.5,9.12);
                
                for(int i = 0 ; i < 13; i++){
                    result.add(pa[i]);
                }
            }
            //----------- End of XY plots for SMUSISL2SR2.3
            
            //----------- Start of XY plots for SMUSISL2SR2.4
            List<Integer> sisl2sr24 = new ArrayList<>();
            sisl2sr24.add(1010200070);
            sisl2sr24.add(1010200074);
            sisl2sr24.add(1010200080);
            
            if(compareLocID.equals(sisl2sr24)){
                HeatMap_points[] pa = new HeatMap_points[23];
                pa[0] = new HeatMap_points(33.43,8.91);
                pa[1] = new HeatMap_points(33.43,8.59);
                pa[2] = new HeatMap_points(33.11,8.58);
                pa[3] = new HeatMap_points(33.11,7.94);
                pa[4] = new HeatMap_points(33.23,7.1);
                pa[5] = new HeatMap_points(33.87,6.37);
                pa[6] = new HeatMap_points(34.5,5.97);
                pa[7] = new HeatMap_points(34.9,5.79);
                pa[8] = new HeatMap_points(34.77,5.64);
                pa[9] = new HeatMap_points(35.6,5.44);
                pa[10] = new HeatMap_points(36.47,5.31);
                pa[11] = new HeatMap_points(37.5,5.31);
                pa[12] = new HeatMap_points(38.3,5.42);
                pa[13] = new HeatMap_points(38.92,5.64);
                pa[14] = new HeatMap_points(38.86,5.77);
                pa[15] = new HeatMap_points(39.83,6.26);
                pa[16] = new HeatMap_points(40.43,6.87);
                pa[17] = new HeatMap_points(40.78,7.61);
                pa[18] = new HeatMap_points(40.83,8.34);
                pa[19] = new HeatMap_points(38.58,8.34);
                pa[20] = new HeatMap_points(38.58,9.9);
                pa[21] = new HeatMap_points(36.81,9.9);
                pa[22] = new HeatMap_points(36.81,8.91);
                
                for(int i = 0 ; i < 23; i++){
                    result.add(pa[i]);
                }
            }
            //----------- End of XY plots for SMUSISL2SR2.4

        }
        //--------------- End of XY plots for Level 2 ----------------------
        
        //--------------- Start of XY plots for Level 3 ----------------------
        if(level == 3){
            
            //----------- Start of XY plots for SMUSISL3LiftLobby
            List<Integer> sisl3liftlobby = new ArrayList<>();
            sisl3liftlobby.add(1010300001);
            sisl3liftlobby.add(1010300002);
            sisl3liftlobby.add(1010300003);
            sisl3liftlobby.add(1010300005);
            sisl3liftlobby.add(1010300007);
            sisl3liftlobby.add(1010300008);
            sisl3liftlobby.add(1010300010);
            sisl3liftlobby.add(1010300033);
            sisl3liftlobby.add(1010300036);
            sisl3liftlobby.add(1010300038);
            sisl3liftlobby.add(1010300040);
            sisl3liftlobby.add(1010300041);
            sisl3liftlobby.add(1010300043);
            
            if(compareLocID.equals(sisl3liftlobby)){
                HeatMap_points[] pa = new HeatMap_points[21];
                pa[0] = new HeatMap_points(14.64,18.1);
                pa[1] = new HeatMap_points(14.64,15.45);
                pa[2] = new HeatMap_points(13.93,15.45);
                pa[3] = new HeatMap_points(13.93,12.92);
                pa[4] = new HeatMap_points(15.84,11.92);
                pa[5] = new HeatMap_points(17.04,11.92);
                pa[6] = new HeatMap_points(17.04,11.47);
                pa[7] = new HeatMap_points(18.03,11.47);
                pa[8] = new HeatMap_points(18.03,11.89);
                pa[9] = new HeatMap_points(24.45,11.89);
                pa[10] = new HeatMap_points(24.45,15.25);
                pa[11] = new HeatMap_points(22.93,15.25);
                pa[12] = new HeatMap_points(22.93,15.89);
                pa[13] = new HeatMap_points(21.48,15.89);
                pa[14] = new HeatMap_points(21.48,15.2);
                pa[15] = new HeatMap_points(20.07,15.2);
                pa[16] = new HeatMap_points(20.07,15.63);
                pa[17] = new HeatMap_points(19.05,15.63);
                pa[18] = new HeatMap_points(19.05,15.2);
                pa[19] = new HeatMap_points(15.88,15.2);
                pa[20] = new HeatMap_points(15.88,18.1);
                
                for(int i = 0 ; i < 21; i++){
                    result.add(pa[i]);
                }                
            }
            //----------- End of XY plots for SMUSISL3LiftLobby
            
            //----------- Start of XY plots for SMUSISL3StudyArea1
            List<Integer> sisl3studyarea1 = new ArrayList<>();
            sisl3studyarea1.add(1010300026);
            sisl3studyarea1.add(1010300028);
            sisl3studyarea1.add(1010300029);
            sisl3studyarea1.add(1010300032);
            sisl3studyarea1.add(1010300046);
            sisl3studyarea1.add(1010300138);
            sisl3studyarea1.add(1010300141);
            sisl3studyarea1.add(1010300147);
            sisl3studyarea1.add(1010300148);
            sisl3studyarea1.add(1010300149);
            sisl3studyarea1.add(1010300150);
            
            if(compareLocID.equals(sisl3studyarea1)){
                HeatMap_points[] pa = new HeatMap_points[17];
                pa[0] = new HeatMap_points(11.78,16.65);
                pa[1] = new HeatMap_points(11.78,15.52);
                pa[2] = new HeatMap_points(10.44,15.52);
                pa[3] = new HeatMap_points(10.44,14.68);
                pa[4] = new HeatMap_points(8.84,10.41);
                pa[5] = new HeatMap_points(9.53,10.26);
                pa[6] = new HeatMap_points(9.7,10.41);
                pa[7] = new HeatMap_points(11.22,10.41);
                pa[8] = new HeatMap_points(11.22,11.32);
                pa[9] = new HeatMap_points(12.7,11.32);
                pa[10] = new HeatMap_points(12.7,10.41);
                pa[11] = new HeatMap_points(15.87,10.41);
                pa[12] = new HeatMap_points(15.87,11.92);
                pa[13] = new HeatMap_points(13.99,12.92);
                pa[14] = new HeatMap_points(13.99,15.45);
                pa[15] = new HeatMap_points(14.61,15.45);
                pa[16] = new HeatMap_points(14.61,16.65);
                
                for(int i = 0 ; i < 17; i++){
                    result.add(pa[i]);
                }
            }
            //----------- End of XY plots for SMUSISL3StudyArea1
            
            //----------- Start of XY plots for SMUSISL3StudyArea2
            List<Integer> sisl3studyarea2 = new ArrayList<>();
            sisl3studyarea2.add(1010300048);
            sisl3studyarea2.add(1010300049);
            sisl3studyarea2.add(1010300051);
            sisl3studyarea2.add(1010300052);
            sisl3studyarea2.add(1010300054);
            sisl3studyarea2.add(1010300056);
            
            if(compareLocID.equals(sisl3studyarea2)){
                HeatMap_points[] pa = new HeatMap_points[8];
                pa[0] = new HeatMap_points(13.05,10.41);
                pa[1] = new HeatMap_points(13.06,8.36);
                pa[2] = new HeatMap_points(27.02,8.36);
                pa[3] = new HeatMap_points(27.02,9.67);
                pa[4] = new HeatMap_points(22.47,9.67);
                pa[5] = new HeatMap_points(22.47,10.12);
                pa[6] = new HeatMap_points(15.91,10.12);
                pa[7] = new HeatMap_points(15.88,10.41);
                
                for(int i = 0 ; i < 8; i++){
                    result.add(pa[i]);
                }
            }
            //----------- End of XY plots for SMUSISL3StudyArea2
            
            //----------- Start of XY plots for SMUSISL3SR3.1
            List<Integer> sisl3sr31 = new ArrayList<>();
            sisl3sr31.add(1010300132);
            sisl3sr31.add(1010300135);
            sisl3sr31.add(1010300136);
            
            if(compareLocID.equals(sisl3sr31)){
                HeatMap_points[] pa = new HeatMap_points[16];
                pa[0] = new HeatMap_points(13.09,8.36);
                pa[1] = new HeatMap_points(13.09,8.18);
                pa[2] = new HeatMap_points(11.01,8.18);
                pa[3] = new HeatMap_points(10.9,7.58);
                pa[4] = new HeatMap_points(11.15,6.88);
                pa[5] = new HeatMap_points(11.67,6.3);
                pa[6] = new HeatMap_points(12.3,5.8);
                pa[7] = new HeatMap_points(12.82,5.57);
                pa[8] = new HeatMap_points(12.69,5.39);
                pa[9] = new HeatMap_points(13.72,5.17);
                pa[10] = new HeatMap_points(14.95,5.1);
                pa[11] = new HeatMap_points(16.37,5.41);
                pa[12] = new HeatMap_points(16.25,5.55);
                pa[13] = new HeatMap_points(16.83 ,5.86);
                pa[14] = new HeatMap_points(17.57,6.07);
                pa[15] = new HeatMap_points(17.57,8.36);    
                
                for(int i = 0 ; i < 16; i++){
                    result.add(pa[i]);
                }
            }
            //----------- End of XY plots for SMUSISL3SR3.1
            
            //----------- Start of XY plots for SMUSISL3SR3.2
            List<Integer> sisl3sr32 = new ArrayList<>();
            sisl3sr32.add(1010300116);
            sisl3sr32.add(1010300120);
            sisl3sr32.add(1010300122);
            
            if(compareLocID.equals(sisl3sr32)){
                HeatMap_points[] pa = new HeatMap_points[8];
                pa[0] = new HeatMap_points(19.86,8.36);
                pa[1] = new HeatMap_points(19.86,6.1);
                pa[2] = new HeatMap_points(21.21,5.59);
                pa[3] = new HeatMap_points(22.12,5.35);
                pa[4] = new HeatMap_points(22.03,5.18);
                pa[5] = new HeatMap_points(23.73,4.9);
                pa[6] = new HeatMap_points(25.01,4.9);
                pa[7] = new HeatMap_points(25.01,8.36);
                
                for(int i = 0 ; i < 8; i++){
                    result.add(pa[i]);
                }
            }
            //----------- End of XY plots for SMUSISL3SR3.2
            
            //----------- Start of XY plots for SMUSISL3SR3.3
            List<Integer> sisl3sr33 = new ArrayList<>();
            sisl3sr33.add(1010300105);
            sisl3sr33.add(1010300109);
            sisl3sr33.add(1010300111);
            
            if(compareLocID.equals(sisl3sr33)){
                HeatMap_points[] pa = new HeatMap_points[8];
                pa[0] = new HeatMap_points(25.01,8.33);
                pa[1] = new HeatMap_points(25.01,4.87);
                pa[2] = new HeatMap_points(26.58,4.97);
                pa[3] = new HeatMap_points(28.01,5.22);
                pa[4] = new HeatMap_points(27.9,5.34);
                pa[5] = new HeatMap_points(28.93,5.59);
                pa[6] = new HeatMap_points(30.13,6.15);
                pa[7] = new HeatMap_points(30.13,8.33);
                
                for(int i = 0 ; i < 8; i++){
                    result.add(pa[i]);
                }
            }
            //----------- End of XY plots for SMUSISL3SR3.3
            
            //----------- Start of XY plots for SMUSISL3SR3.4
            List<Integer> sisl3sr34 = new ArrayList<>();
            sisl3sr34.add(1010300075);
            sisl3sr34.add(1010300076);
            sisl3sr34.add(1010300079);
            sisl3sr34.add(1010300083);
            
            if(compareLocID.equals(sisl3sr34)){
                HeatMap_points[] pa = new HeatMap_points[18];
                pa[0] = new HeatMap_points(32.46,8.36);
                pa[1] = new HeatMap_points(32.46,6.07);
                pa[2] = new HeatMap_points(32.84,6.07);
                pa[3] = new HeatMap_points(33.34,5.74);
                pa[4] = new HeatMap_points(33.75,5.56);
                pa[5] = new HeatMap_points(33.6,5.39);
                pa[6] = new HeatMap_points(35.14,5.08);
                pa[7] = new HeatMap_points(36.75,5.19);
                pa[8] = new HeatMap_points(37.41,5.41);
                pa[9] = new HeatMap_points(37.3,5.57);
                pa[10] = new HeatMap_points(38.39,6.29);
                pa[11] = new HeatMap_points(38.88,6.99);
                pa[12] = new HeatMap_points(39.13,7.91);
                pa[13] = new HeatMap_points(39.05,8.22);
                pa[14] = new HeatMap_points(37.01,8.22);
                pa[15] = new HeatMap_points(37.01,9.03);
                pa[16] = new HeatMap_points(35.42,9.03);
                pa[17] = new HeatMap_points(35.42,8.36);

                for(int i = 0 ; i < 18; i++){
                    result.add(pa[i]);
                }
            }
            //----------- End of XY plots for SMUSISL3SR3.4
            
            //----------- Start of XY plots for SMUSISL3CLSRM
            List<Integer> sisl3clsrm = new ArrayList<>();
            sisl3clsrm.add(1010300090);
            sisl3clsrm.add(1010300093);
            sisl3clsrm.add(1010300096);
            sisl3clsrm.add(1010300099);
            
            if(compareLocID.equals(sisl3clsrm)){
                HeatMap_points[] pa = new HeatMap_points[4];
                pa[0] = new HeatMap_points(22.47,11.89);
                pa[1] = new HeatMap_points(22.47,9.7);
                pa[2] = new HeatMap_points(31.36,9.7);
                pa[3] = new HeatMap_points(31.36,11.89);

                for(int i = 0 ; i < 4; i++){
                    result.add(pa[i]);
                }
            }
            //----------- End of XY plots for SMUSISL3CLSRM
            
            //----------- Start of XY plots for SMUSISL3StudyArea3
            List<Integer> sisl3studyarea3 = new ArrayList<>();
            sisl3studyarea3.add(1010300058);
            sisl3studyarea3.add(1010300060);
            sisl3studyarea3.add(1010300061);
            sisl3studyarea3.add(1010300063);
            sisl3studyarea3.add(1010300064);
            sisl3studyarea3.add(1010300066);
            sisl3studyarea3.add(1010300067);
            sisl3studyarea3.add(1010300069);
            sisl3studyarea3.add(1010300071);
            sisl3studyarea3.add(1010300072);
            
            if(compareLocID.equals(sisl3studyarea3)){
                HeatMap_points[] pa = new HeatMap_points[10];
                pa[0] = new HeatMap_points(31.22,11.89);
                pa[1] = new HeatMap_points(31.22,9.67);
                pa[2] = new HeatMap_points(26.99,9.67);
                pa[3] = new HeatMap_points(26.99,8.3);
                pa[4] = new HeatMap_points(35.42,8.33);
                pa[5] = new HeatMap_points(35.42,9.03);
                pa[6] = new HeatMap_points(35.74,9.03);
                pa[7] = new HeatMap_points(35.74,10.72);
                pa[8] = new HeatMap_points(36.97,10.72);
                pa[9] = new HeatMap_points(36.97,11.89);

                for(int i = 0 ; i < 10; i++){
                    result.add(pa[i]);
                }
            }
            //----------- End of XY plots for SMUSISL3StudyArea3

        }
        //--------------- End of XY plots for Level 3 ----------------------
        
        //--------------- Start of XY plots for Level 4 ----------------------
        if(level == 4){
            
            //----------- Start of XY plots for SMUSISL4LiftLobby
            List<Integer> sisl4liftlobby = new ArrayList<>();
            sisl4liftlobby.add(1010400001);
            sisl4liftlobby.add(1010400003);
            sisl4liftlobby.add(1010400005);
            
            if(compareLocID.equals(sisl4liftlobby)){
                HeatMap_points[] pa = new HeatMap_points[12];
                pa[0] = new HeatMap_points(17.27,12.36);
                pa[1] = new HeatMap_points(17.27,10.88);
                pa[2] = new HeatMap_points(18.68,10.88);
                pa[3] = new HeatMap_points(18.68,9.72);
                pa[4] = new HeatMap_points(20,9.72);
                pa[5] = new HeatMap_points(20,10.88);
                pa[6] = new HeatMap_points(20.76,10.88);
                pa[7] = new HeatMap_points(20.76,9.75);
                pa[8] = new HeatMap_points(24.11,9.75);
                pa[9] = new HeatMap_points(24.11,13.56);
                pa[10] = new HeatMap_points(20.76,13.56);
                pa[11] = new HeatMap_points(20.76,12.36);
                
                for(int i = 0 ; i < 12; i++){
                    result.add(pa[i]);
                }                
            }
            //----------- End of XY plots for SMUSISL4LiftLobby
            
            //----------- Start of XY plots for SMUSISL4MRCorridor
            List<Integer> sisl4mrcorridor = new ArrayList<>();
            sisl4mrcorridor.add(1010400013);
            sisl4mrcorridor.add(1010400015);
            sisl4mrcorridor.add(1010400017);
            sisl4mrcorridor.add(1010400019);
            sisl4mrcorridor.add(1010400021);
            sisl4mrcorridor.add(1010400022);
            sisl4mrcorridor.add(1010400024);
            
            if(compareLocID.equals(sisl4mrcorridor)){
                HeatMap_points[] pa = new HeatMap_points[24];
                pa[0] = new HeatMap_points(13.09,16.46);
                pa[1] = new HeatMap_points(13.09,15.15);
                pa[2] = new HeatMap_points(14.66,15.15);
                pa[3] = new HeatMap_points(14.66,9.12);
                pa[4] = new HeatMap_points(12.96,9.12);
                pa[5] = new HeatMap_points(12.96,9.47);
                pa[6] = new HeatMap_points(12.19,9.47);
                pa[7] = new HeatMap_points(12.19,8.03);
                pa[8] = new HeatMap_points(13.14,8.03);
                pa[9] = new HeatMap_points(13.14,6.51);
                pa[10] = new HeatMap_points(16.03,6.51);
                pa[11] = new HeatMap_points(16.03,8.59);
                pa[12] = new HeatMap_points(16.53,8.59);
                pa[13] = new HeatMap_points(16.53,9.33);
                pa[14] = new HeatMap_points(16.25,9.33);
                pa[15] = new HeatMap_points(16.25,10.85);
                pa[16] = new HeatMap_points(17.23,10.85);
                pa[17] = new HeatMap_points(17.23,12.4);
                pa[18] = new HeatMap_points(16.42,12.4);
                pa[19] = new HeatMap_points(16.42,12.89);
                pa[20] = new HeatMap_points(16,12.89);
                pa[21] = new HeatMap_points(16,16.74);
                pa[22] = new HeatMap_points(14.69,16.74);
                pa[23] = new HeatMap_points(14.69,16.46);

                for(int i = 0 ; i < 24; i++){
                    result.add(pa[i]);
                }
            }
            //----------- End of XY plots for SMUSISL4MRCorridor
            
            //----------- Start of XY plots for SMUSISL4OusideAcadOffice1
            List<Integer> sisl4outsideacadoffice1 = new ArrayList<>();
            sisl4outsideacadoffice1.add(1010400027);
            sisl4outsideacadoffice1.add(1010400029);
            sisl4outsideacadoffice1.add(1010400030);
            sisl4outsideacadoffice1.add(1010400032);
            sisl4outsideacadoffice1.add(1010400035);
            sisl4outsideacadoffice1.add(1010400038);
            sisl4outsideacadoffice1.add(1010400040);
            sisl4outsideacadoffice1.add(1010400042);
            sisl4outsideacadoffice1.add(1010400044);
            sisl4outsideacadoffice1.add(1010400046);
            sisl4outsideacadoffice1.add(1010400048);
            sisl4outsideacadoffice1.add(1010400050);
            sisl4outsideacadoffice1.add(1010400052);
            
            if(compareLocID.equals(sisl4outsideacadoffice1)){
                HeatMap_points[] pa = new HeatMap_points[30];
                pa[0] = new HeatMap_points(13.1,6.46);
                pa[1] = new HeatMap_points(13.1,5.63);
                pa[2] = new HeatMap_points(12.82,5.63);
                pa[3] = new HeatMap_points(12.82,3.65);
                pa[4] = new HeatMap_points(11.94,3.65);
                pa[5] = new HeatMap_points(11.94,2.8);
                pa[6] = new HeatMap_points(15.26,2.8);
                pa[7] = new HeatMap_points(15.26,4.99);
                pa[8] = new HeatMap_points(18.08,4.99);
                pa[9] = new HeatMap_points(18.08,2.84);
                pa[10] = new HeatMap_points(19.28,2.84);
                pa[11] = new HeatMap_points(19.28,4.96);
                pa[12] = new HeatMap_points(22.14,4.96);
                pa[13] = new HeatMap_points(22.14,2.84);
                pa[14] = new HeatMap_points(23.3,2.84);
                pa[15] = new HeatMap_points(23.3,4.99);
                pa[16] = new HeatMap_points(24.75,4.99);
                pa[17] = new HeatMap_points(24.75,6.44);
                pa[18] = new HeatMap_points(23.37,6.44);
                pa[19] = new HeatMap_points(23.37,7.6);
                pa[20] = new HeatMap_points(24.82,7.6);
                pa[21] = new HeatMap_points(24.82,9.75);
                pa[22] = new HeatMap_points(22.28,9.75);
                pa[23] = new HeatMap_points(22.28,7.74);
                pa[24] = new HeatMap_points(22.07,7.74);
                pa[25] = new HeatMap_points(22.07,6.44);
                pa[26] = new HeatMap_points(19.31,6.44);
                pa[27] = new HeatMap_points(19.31,7.74);
                pa[28] = new HeatMap_points(17.97,7.74);
                pa[29] = new HeatMap_points(17.97,6.47);
                
                for(int i = 0 ; i < 30; i++){
                    result.add(pa[i]);
                }
            }
            //----------- End of XY plots for SMUSISL4OusideAcadOffice1
            
            //----------- Start of XY plots for SMUSISL4OusideAcadOffice2
            List<Integer> sisl4outsideacadoffice2 = new ArrayList<>();
            sisl4outsideacadoffice2.add(1010400054);
            sisl4outsideacadoffice2.add(1010400056);
            sisl4outsideacadoffice2.add(1010400058);
            sisl4outsideacadoffice2.add(1010400060);
            sisl4outsideacadoffice2.add(1010400062);
            sisl4outsideacadoffice2.add(1010400064);
            sisl4outsideacadoffice2.add(1010400067);
            sisl4outsideacadoffice2.add(1010400068);
            sisl4outsideacadoffice2.add(1010400070);
            sisl4outsideacadoffice2.add(1010400072);
            sisl4outsideacadoffice2.add(1010400075);
            
            if(compareLocID.equals(sisl4outsideacadoffice2)){
                HeatMap_points[] pa = new HeatMap_points[30];
                pa[0] = new HeatMap_points(24.78,6.44);
                pa[1] = new HeatMap_points(24.78,5.03);
                pa[2] = new HeatMap_points(26.16,5.03);
                pa[3] = new HeatMap_points(26.16,2.77);
                pa[4] = new HeatMap_points(27.39,2.77);
                pa[5] = new HeatMap_points(27.39,5.03);
                pa[6] = new HeatMap_points(30.22,5.03);
                pa[7] = new HeatMap_points(30.22,2.8);
                pa[8] = new HeatMap_points(31.45,2.8);
                pa[9] = new HeatMap_points(31.45,4.99);
                pa[10] = new HeatMap_points(34.34,4.99);
                pa[11] = new HeatMap_points(34.34,2.8);
                pa[12] = new HeatMap_points(37.64,2.8);
                pa[13] = new HeatMap_points(37.64,3.65);
                pa[14] = new HeatMap_points(35.31,3.65);
                pa[15] = new HeatMap_points(35.31,5.42);
                pa[16] = new HeatMap_points(36.29,5.42);
                pa[17] = new HeatMap_points(36.29,8.34);
                pa[18] = new HeatMap_points(35.3,8.34);
                pa[19] = new HeatMap_points(35.3,8.17);
                pa[20] = new HeatMap_points(34.24,8.17);
                pa[21] = new HeatMap_points(34.24,6.44);
                pa[22] = new HeatMap_points(31.45,6.44);
                pa[23] = new HeatMap_points(31.45,8.2);
                pa[24] = new HeatMap_points(30.18,8.2);
                pa[25] = new HeatMap_points(30.18,6.44);
                pa[26] = new HeatMap_points(27.43,6.44);
                pa[27] = new HeatMap_points(27.43,7.32);
                pa[28] = new HeatMap_points(26.12,7.32);
                pa[29] = new HeatMap_points(26.12,6.44);
                
                for(int i = 0 ; i < 30; i++){
                    result.add(pa[i]);
                }
            }
            //----------- End of XY plots for SMUSISL4OusideAcadOffice2
        }
        //--------------- End of XY plots for Level 4 ----------------------
        
        //--------------- Start of XY plots for Level 5 ----------------------
        if(level == 5){
            
            //----------- Start of XY plots for SMUSISL5LiftLobby
            List<Integer> sisl5liftlobby = new ArrayList<>();
            sisl5liftlobby.add(1010500002);
            sisl5liftlobby.add(1010500004);
            sisl5liftlobby.add(1010500006);
            
            if(compareLocID.equals(sisl5liftlobby)){
                HeatMap_points[] pa = new HeatMap_points[8];
                pa[0] = new HeatMap_points(18.9,14.9);
                pa[1] = new HeatMap_points(18.9,13.3);
                pa[2] = new HeatMap_points(19.9,13.3);
                pa[3] = new HeatMap_points(19.9,12.3);
                pa[4] = new HeatMap_points(23.6,12.3);
                pa[5] = new HeatMap_points(23.6,15.8);
                pa[6] = new HeatMap_points(20,15.8);
                pa[7] = new HeatMap_points(20,14.9);
                
                for(int i = 0 ; i < 8; i++){
                    result.add(pa[i]);
                }                
            }
            //----------- End of XY plots for SMUSISL5LiftLobby
            
            //----------- Start of XY plots for SMUSISL5StudyArea1
            List<Integer> sisl5studyarea1 = new ArrayList<>();
            sisl5studyarea1.add(1010500008);
            sisl5studyarea1.add(1010500011);
            sisl5studyarea1.add(1010500013);
            sisl5studyarea1.add(1010500014);
            sisl5studyarea1.add(1010500016);
            sisl5studyarea1.add(1010500017);
            sisl5studyarea1.add(1010500019);
            
            if(compareLocID.equals(sisl5studyarea1)){
                HeatMap_points[] pa = new HeatMap_points[20];
                pa[0] = new HeatMap_points(13.5,18.7);
                pa[1] = new HeatMap_points(13.5,17.45);
                pa[2] = new HeatMap_points(12.9,17.45);
                pa[3] = new HeatMap_points(12.8,11.2);
                pa[4] = new HeatMap_points(13.5,11.2);
                pa[5] = new HeatMap_points(13.5,10.3);
                pa[6] = new HeatMap_points(14.8,10.3);
                pa[7] = new HeatMap_points(14.8,11.2);
                pa[8] = new HeatMap_points(15.4,11.2);
                pa[9] = new HeatMap_points(15.4,11.9);
                pa[10] = new HeatMap_points(14.8,11.9);
                pa[11] = new HeatMap_points(14.8,12.7);
                pa[12] = new HeatMap_points(15.4,12.7);
                pa[13] = new HeatMap_points(15.4,13.4);
                pa[14] = new HeatMap_points(17.5,13.4);
                pa[15] = new HeatMap_points(17.5,12.3);
                pa[16] = new HeatMap_points(19,12.3);
                pa[17] = new HeatMap_points(18.9,14.8);
                pa[18] = new HeatMap_points(14.8,14.8);
                pa[19] = new HeatMap_points(14.8,18.7);

                for(int i = 0 ; i < 20; i++){
                    result.add(pa[i]);
                }
            }
            //----------- End of XY plots for SMUSISL5StudyArea1
            
            //----------- Start of XY plots for SMUSISL5OutsideDeansOffice
            List<Integer> sisl5outsidedean = new ArrayList<>();
            sisl5outsidedean.add(1010500021);
            sisl5outsidedean.add(1010500023);
            sisl5outsidedean.add(1010500025);
            sisl5outsidedean.add(1010500028);
            sisl5outsidedean.add(1010500031);
            sisl5outsidedean.add(1010500033);
            sisl5outsidedean.add(1010500036);
            
            if(compareLocID.equals(sisl5outsidedean)){
                HeatMap_points[] pa = new HeatMap_points[22];
                pa[0] = new HeatMap_points(13.5,10.3);
                pa[1] = new HeatMap_points(13.5,9.5);
                pa[2] = new HeatMap_points(13.1,9.5);
                pa[3] = new HeatMap_points(13.1,5.3);
                pa[4] = new HeatMap_points(13.1,7.4);
                pa[5] = new HeatMap_points(17,7.4);
                pa[6] = new HeatMap_points(17,5.3);
                pa[7] = new HeatMap_points(18.4,5.3);
                pa[8] = new HeatMap_points(18.4,7.4);
                pa[9] = new HeatMap_points(21.4,7.4);
                pa[10] = new HeatMap_points(21.4,5.3);
                pa[11] = new HeatMap_points(22.8,5.3);
                pa[12] = new HeatMap_points(22.8,7.4);
                pa[13] = new HeatMap_points(25.8,7.4);
                pa[14] = new HeatMap_points(25.8,7.4);
                pa[15] = new HeatMap_points(25.8,9.2);
                pa[16] = new HeatMap_points(22.8,9.2);
                pa[17] = new HeatMap_points(22.8,10.4);
                pa[18] = new HeatMap_points(21.4,10.4);
                pa[19] = new HeatMap_points(21.4,9.2);
                pa[20] = new HeatMap_points(14.8,9.18);
                pa[21] = new HeatMap_points(14.8,10.3);
                
                for(int i = 0 ; i < 22; i++){
                    result.add(pa[i]);
                }
            }
            //----------- Start of XY plots for SMUSISL5OutsideDeansOffice
            
            //----------- Start of XY plots for SMUSISL5AcadOffice
            List<Integer> sisl5acadoffice = new ArrayList<>();
            sisl5acadoffice.add(1010500037);
            sisl5acadoffice.add(1010500039);
            sisl5acadoffice.add(1010500041);
            sisl5acadoffice.add(1010500042);
            sisl5acadoffice.add(1010500044);
            sisl5acadoffice.add(1010500046);
            sisl5acadoffice.add(1010500049);
            sisl5acadoffice.add(1010500051);
            sisl5acadoffice.add(1010500053);
            sisl5acadoffice.add(1010500055);
            sisl5acadoffice.add(1010500057);
            sisl5acadoffice.add(1010500058);
            sisl5acadoffice.add(1010500059);
            sisl5acadoffice.add(1010500061);
            
            if(compareLocID.equals(sisl5acadoffice)){
                HeatMap_points[] pa = new HeatMap_points[26];
                pa[0] = new HeatMap_points(25.76,9.97);
                pa[1] = new HeatMap_points(25.76,5.31);
                pa[2] = new HeatMap_points(27.1,5.31);
                pa[3] = new HeatMap_points(27.1,7.42);
                pa[4] = new HeatMap_points(30.09,7.42);
                pa[5] = new HeatMap_points(30.09,5.31);
                pa[6] = new HeatMap_points(31.43,5.31);
                pa[7] = new HeatMap_points(31.43,7.44);
                pa[8] = new HeatMap_points(34.36,7.44);
                pa[9] = new HeatMap_points(34.36,5.31); 
                pa[10] = new HeatMap_points(37.94,5.31); 
                pa[11] = new HeatMap_points(37.94,6.39); 
                pa[12] = new HeatMap_points(35.62,6.39); 
                pa[13] = new HeatMap_points(35.62,8.23); 
                pa[14] = new HeatMap_points(36.62,8.23); 
                pa[15] = new HeatMap_points(36.62,10.95); 
                pa[16] = new HeatMap_points(35.52,10.95); 
                pa[17] = new HeatMap_points(35.52,10.74); 
                pa[18] = new HeatMap_points(34.38,10.74); 
                pa[19] = new HeatMap_points(34.38,9.13); 
                pa[20] = new HeatMap_points(31.38,9.13); 
                pa[21] = new HeatMap_points(31.38,10.82); 
                pa[22] = new HeatMap_points(30.03,10.82); 
                pa[23] = new HeatMap_points(30.03,9.16); 
                pa[24] = new HeatMap_points(27.1,9.16); 
                pa[25] = new HeatMap_points(27.03,9.94);

                for(int i = 0 ; i < 26; i++){
                    result.add(pa[i]);
                }
            }
            //----------- End of XY plots for SMUSISL5AcadOffice
        }
        //--------------- End of XY plots for Level 5 ----------------------
        
        
        return result;
    }
}
