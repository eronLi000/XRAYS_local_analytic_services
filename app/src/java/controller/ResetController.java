package controller;

import dao.DemographicDAO;
import dao.LocationDAO;
import dao.LocationLookUpDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


 

    
@WebServlet(urlPatterns = {"/ResetController"})

public class ResetController extends HttpServlet {
    
    /**
     * Processes requests for both HTTP <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        ServletOutputStream out = response.getOutputStream();
        
        //--------- Get specific request for whihc data set to be truncated ----------
        boolean demo = false;
        if(request.getParameter("demo") != null){
            demo = true;
            out.println("demo: " + demo);
        }
        
        boolean llu = false;
        if(request.getParameter("llu") != null){
            llu = true;
            out.println("llu: " + llu);
        }
        
        boolean loc = false;
        if(request.getParameter("loc") != null){
            loc = true;
            out.println("loc: " + loc);
        }
        

        //--------- Generates specific error messages to be returned to reset jsp page----------
        try{
            
            //Set attribute to the current session with the corresponding error message and redirect back to the jsp page to user
            if(demo){
                DemographicDAO.truncateDemo();
                session.setAttribute("success", "You have successfully truncated Demographics Table!");
                response.sendRedirect("reset.jsp");
            } else if(llu){
                LocationLookUpDAO.truncateLluc();
                session.setAttribute("success", "You have successfully truncated Location LookUp Table!");
                response.sendRedirect("reset.jsp");
            } else if(loc){
                LocationDAO.truncateLoc();
                session.setAttribute("success", "You have successfully truncated Location Table!");
                response.sendRedirect("reset.jsp");
            }
                
        } catch(Exception e){
            
            //Set error message to be return and forward back to jsp caller
            session.setAttribute("fail", "An unexpected error has occured");
            response.sendRedirect("reset.jsp");
        }
              
    }

}
