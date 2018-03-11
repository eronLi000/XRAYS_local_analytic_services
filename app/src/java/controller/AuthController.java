package controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.User;
import dao.DemographicDAO;
import entity.JsonContainer;
import is203.JWTUtility;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;


 
@WebServlet(urlPatterns = {"/AuthController"})
public class AuthController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        //crreate session object
        HttpSession session = request.getSession(false);
        //create gson object
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //get notJson request parameter
        String notJson = request.getParameter("notJson");
        //creates an arraylist for storing the messages
        ArrayList<String> messages = new ArrayList<>();
        boolean validPassword = true;
        boolean validUsername = true;
        
        //get username and password request parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        
        //--------- Start Handler for token validation ----------
        if (notJson == null) {
            //----- since notJson == null means this is a json request
                        
             //check if username or password is null or blank if not null
            if (username == null || password == null || username.trim().equals("") || password.trim().equals("")){
                //Creates the json return for status error
                
                //no username in the url
                if (username == null){
                    messages.add("missing username");
                }
                //have username in the url but does not equal to anything, means it's blank
                else if (username.trim().equals("")){
                    messages.add("blank username");
                }
                
                if (password == null){
                    messages.add("missing password");
                }
                else if (password.trim().equals("")){
                    messages.add("blank password");
                }
                
                //sort the messages in ascending order
                Collections.sort(messages);
                JsonContainer jsc = new JsonContainer("error", messages);
                String json = gson.toJson(jsc);
                
                //to send back the result to json/authenticate page
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/authenticate");
                view.forward(request, response);
                return;
            }
        }
        
        boolean validUser = false;
        
        //check if username entered includes @, it is invalid
        if (username.indexOf('@') != -1) {
            messages.add("invalid username/password");
            Collections.sort(messages);

            //If notJson == null means this is a json request
            if(notJson == null){
                
                JsonContainer jsc = new JsonContainer("error", messages);
                String json = gson.toJson(jsc);
            
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/authenticate");
                view.forward(request, response);
            } else {
                request.setAttribute("error", messages);
                RequestDispatcher view = request.getRequestDispatcher("login.jsp");
                view.forward(request, response);
                return;
            }

        }

        //initilize the demographicDAO to retrieve the user object
        User user = DemographicDAO.retrieveUser(username);

        //is user object is not null and user obect's password from the database is equal to input password then it is a valid user
        if (user != null && user.getPassword().equals(password)) {
            validUser = true;
        }

        //it is a valid user
        if(validUser){
            
            //--------- Start Handler for Valid User ----------
            if(notJson == null){
                //----- since notJson == null means this is a json request

                //Token Generator
                String token = JWTUtility.sign(DemographicDAO.getSharedSecret(), username); 
                
                //Creates the json return for status success
                JsonContainer jsc = new JsonContainer("success", token);
                gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(jsc);

                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/authenticate");
                view.forward(request, response);
            } else {
                session.setAttribute("username", username);
                response.sendRedirect("index.jsp");
            }
            //--------- End Handler for Valid User ----------

        } else {
            
            //--------- Start Handler for Invalid User ----------
            
            if(notJson == null){
                messages.add("invalid username/password");
                //----- since notJson == null means this is a json request
                Collections.sort(messages);
                JsonContainer jsc = new JsonContainer("error", messages);
                messages = new ArrayList<>();
                String json = gson.toJson(jsc);
                
                request.setAttribute("json", json);
                RequestDispatcher view = request.getRequestDispatcher("json/authenticate");
                view.forward(request, response);
            } else {
                messages.add("invalid username/password");
                Collections.sort(messages);
                request.setAttribute("error", messages);
                RequestDispatcher view = request.getRequestDispatcher("login.jsp");
                view.forward(request, response);
            }
            //--------- End Handler for Invalid User ----------

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
