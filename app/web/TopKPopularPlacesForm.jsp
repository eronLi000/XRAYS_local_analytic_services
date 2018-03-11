
<%@page import="entity.PopularPlace"%>
<%@page import="java.util.ArrayList"%>
<%@page import="entity.PopularPlaceContainer"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="java.util.TreeMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.List"%>
<%@include file="protect.jsp" %> 
<%@page import="dao.TopKPopularPlacesDAO"%>

<jsp:include page="layouts/header.jsp">
    <jsp:param name="active_tab" value="7"/>
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
                <h1 class="page-header">Top K Popular Places</h1>
                <div class="row">
                    <div class="col-lg-6">
                        <form action="TopKPopularPlacesController" method="post">
                            <div class="form-group" style="padding-top: 30px">
                                <div class='input-group date' id='datetimepicker1'>
                                    <input type='text' name="date" class="form-control" required="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                </div>
                            </div>
                            <br>
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
                    
                                
                <%                     
                   
                    
                    if (session.getAttribute("popularPlaceResult") != null) {
                        
                        String json = (String) session.getAttribute("popularPlaceResult");
                        Gson gson = new Gson();
                        PopularPlaceContainer popularPlaceContainer = gson.fromJson(json, PopularPlaceContainer.class);
                        if(popularPlaceContainer.getStatus().equals("success")){

                            if(popularPlaceContainer.getPopularPlaces() == null|| popularPlaceContainer.getPopularPlaces().isEmpty()){
                                out.println("<div class='alert alert-warning' role='alert' style='margin-top:50px;'>"
                                        + "<strong>Uh oh!</strong>");
                                List<String> messages = popularPlaceContainer.getMessages();
                                for (String msg : messages) {
                                    out.println(msg);
                                }
                                out.println("</div>");
                            } else {
                                out.println("<table class='table table-hover' style='margin-top:30px'>");
                                out.println("<thead><tr>");
                                out.println("<td>" + "Rank" + "</td>");
                                out.println("<td>" + "Semantic-Place" + "</td>");
                                out.println("<td>" + "Count" + "</td>");
                                out.println("</tr></thead>");

                                ArrayList<PopularPlace> popularPlaceList = popularPlaceContainer.getPopularPlaces();
                                int size = popularPlaceList.size();
                                int tempRank = 0;
                                for (int i = 0; i < size; i++){
                                    PopularPlace popularPlace = popularPlaceList.get(i);
                                    out.println("<tr>");
                                    if(tempRank == popularPlace.getRank()){
                                        out.println("<td> </td>");
                                        tempRank = popularPlace.getRank();
                                    }
                                    else{
                                        out.println("<td>" + popularPlace.getRank() + "</td>");
                                        tempRank = popularPlace.getRank();
                                    }
                                    out.println("<td>");
                                    out.println(popularPlace.getSemanticPlace());
                                    out.println("</td>");
                                    out.println("<td>" + popularPlace.getCount() + "</td>");
                                    out.println("</tr>");
                                }

                                out.println("</table>");
                            }
                            
                            
                        }
                        else {
                            List<String> errorMessages = popularPlaceContainer.getMessages();
                            out.println(popularPlaceContainer.getStatus());
                            int num = 1;
                            for (String msg: errorMessages) {
                                out.println(num + ". " +msg + "<br>");
                                num++;
                            }
                        }
                  
                    }
                    session.setAttribute("popularPlaceResult", null);
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