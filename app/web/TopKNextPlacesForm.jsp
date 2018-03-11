
<%@page import="entity.NextPlace"%>
<%@page import="entity.NextPlaceContainer"%>
<%@page import="entity.PopularPlaceContainer"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="entity.UserLocation"%>
<%@page import="java.util.Map"%>
<%@page import="dao.TopKNextPlacesDAO"%>
<%@page import="java.util.List"%>
<%@page import="dao.LocationLookUpDAO"%>
<%@include file="protect.jsp" %> 

<jsp:include page="layouts/header.jsp">
    <jsp:param name="active_tab" value="9"/>
</jsp:include>

<script src="public/bootstrap/vendor/jquery/jquery.min.js"></script>
<script src="public/bootstrap/vendor/bootstrap/js/bootstrap.min.js"></script>
<script src="public/bootstrap/dist/js/moment.min.js"></script>
<link rel="stylesheet" href="public/bootstrap/dist/css/bootstrap-datetimepicker.min.css">
<script src="public/bootstrap/dist/js/bootstrap-datetimepicker.min.js"></script>

<div id="page-wrapper">
    <div class="container-fluid">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">Top K Next Places</h1>
                <div class="row">
                    <div class="col-lg-6">
                        <form action="TopKNextPlacesController" method="post" style="padding-top: 30px">
                            <div class='input-group date' id='datetimepicker1'>
                                <input type='text' name="date" class="form-control"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                            </div>
                            <br>
                            Select a place: 
                            <select name="origin">
                            <% 
                                //retrieve a list of semantic places
                                if(TopKNextPlacesDAO.retrieveAllSemanticPlaces() != null){ 
                                    List<String> semanticPlaceList = TopKNextPlacesDAO.retrieveAllSemanticPlaces();

                                    for (int i = 0; i < semanticPlaceList.size(); i++) {
                                        out.println("<option value=\"" + semanticPlaceList.get(i) + "\">" + semanticPlaceList.get(i) + "</option>");
                                    }
                                }
                                
                            %>
                            </select>
                            <br><br>
                            Select up to what rank to see: 
                            <select name="k">
                                <option value = "3"> default </option>
                            <%      
                                for (int i = 1; i <= 10; i++) {
                                    out.println("<option value=\"" + i + "\">" + i + "</option>");
                                }
                            %>
                            </select>
                            <input type="text" name="notJson" value="true" hidden="">
                            <br><br>
                            <button type="submit" class="btn btn-primary" onclick="showLoader()">Generate</button>
                        </form>
                    </div>
                </div>
                
                <!-- testing purposes REMOVE AFTER-->            
                <%
                    if (session.getAttribute("top_next_places_result") != null) {

                        String json = (String) session.getAttribute("top_next_places_result");
                        Gson gson = new Gson();
                        NextPlaceContainer nextPlaceContainer = gson.fromJson(json, NextPlaceContainer.class);
                        if(nextPlaceContainer.getStatus().equals("success")){
                            
                            if(nextPlaceContainer.getResults() == null || nextPlaceContainer.getResults().isEmpty()){
                                out.println("<div class='alert alert-warning' role='alert' style='margin-top:50px;'>"
                                        + "<strong>Uh oh!</strong>");
                                List<String> messages = nextPlaceContainer.getMessages();
                                for (String msg : messages) {
                                    out.println(msg);
                                }
                                out.println("</div>");
                            } else {
                                int totalUsers = nextPlaceContainer.getTotal_users();
                                out.println("<div style='margin-top: 30px'>");
                                out.println("Total Users: " + totalUsers + "<br>");
                                out.println("Total Next Places Users: " + nextPlaceContainer.getTotal_next_place_users()+ "</div>");

                                out.println("<table class='table table-hover' style='margin-top:30px'>");

                                out.println("<thead><tr>");
                                out.println("<td>" + "Rank" + "</td>");
                                out.println("<td>" + "No.of People" + "</td>");
                                out.println("<td>" + "Semantic Place" + "</td>");
                                out.println("<td>" + "Percentage" + "</td>");
                                out.println("</tr></thead>");

                                List<NextPlace> nextPlaceList = nextPlaceContainer.getResults();
                                int size = nextPlaceList.size();
                                int tempRank = 0;
                                for (int i = 0; i < size; i++){
                                    NextPlace nextPlace = nextPlaceList.get(i);
                                    int rank = nextPlace.getRank();
                                    int count = nextPlace.getCount();
                                    int numOfPeople = nextPlace.getCount();
                                    long percentage = Math.round(((numOfPeople * 1.0) / totalUsers) * 100);
                                        out.println("<tr>");
                                        
                                        if(rank == tempRank){
                                            out.println("<td> </td>");
                                            tempRank = rank;
                                        }
                                        else{
                                            out.println("<td>" + rank + "</td>");
                                            tempRank = rank;
                                        }
                                        out.println("<td>" + count+ "</td>");
                                        out.println("<td>"+ nextPlace.getSemanticPlace() +"</td>");
                                        out.println("<td>" + percentage + "%" + "</td>");
                                        out.println("<tr>");                            
                                }
                                out.println("</table>");
                            }
                        } else{
                            List<String> errorMessages = nextPlaceContainer.getMessages();
                            out.println(nextPlaceContainer.getStatus());
                            int num = 1;
                            for (String msg: errorMessages) {
                                out.println(num + ". " +msg + "<br>");
                                num++;
                            }
                            
                        }
                    }
                    session.setAttribute("top_next_places_result", null);
                %>         
            </div>
        </div>
    </div>
</div>

<script>
    $('#datetimepicker1').datetimepicker({
        defaultDate: '2017-02-06 10:00:17',
        format: 'YYYY-MM-DD HH:mm:ss',
        sideBySide: true    
    });
</script>

<jsp:include page="layouts/footer.jsp" />