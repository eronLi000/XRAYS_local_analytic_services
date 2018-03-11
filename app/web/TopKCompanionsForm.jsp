<%@page import="java.util.TreeMap"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="entity.Companion"%>
<%@page import="entity.CompanionContainer"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="dao.TopKCompanionsDAO"%>
<%@page import="java.util.List"%>
<%@include file="protect.jsp" %> 

<jsp:include page="layouts/header.jsp">
    <jsp:param name="active_tab" value="8"/>
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
                <h1 class="page-header">Top K Companions</h1>
                <div class="row">
                    <div class="col-lg-6">
                        <form action="TopKCompanionsController" method="post">
                            <div class='input-group date' id='datetimepicker1' style="padding-top: 30px">
                                <input type='text' name="date" class="form-control"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                            </div>
                            <br>
                            Select a mac address: 
                            <select name="mac-address">
                                <% 
                                    List<String> macAddList = TopKCompanionsDAO.retrieveAllMacAddress();
                                    if(macAddList != null){
                                        for (int i = 0; i < macAddList.size(); i++) {
                                            out.println("<option value=\"" + macAddList.get(i) + "\">" + macAddList.get(i) + "</option>");
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
                <%
                    if (session.getAttribute("top_companions_result") != null) {
                        String json = (String) session.getAttribute("top_companions_result");
                        Gson gson = new Gson();
                        CompanionContainer companionContainer = gson.fromJson(json, CompanionContainer.class);
                        if (companionContainer.getStatus().equals("success")) {
                            //out.println(companionContainer.getStatus());
                                
                            if (companionContainer.getResults() == null || companionContainer.getResults().isEmpty()) {
                                       out.println("<div class='alert alert-warning' role='alert' style='margin-top:50px;'>"
                                        + "<strong>Uh oh!</strong>");
                                List<String> messages = companionContainer.getMessages();
                                for (String msg : messages) {
                                    out.println(msg);
                                }
                                out.println("</div>");
                            }
                            else {
                                out.println("<table class='table table-hover' style='margin-top:30px'>");
                                out.println("<thead><tr>");
                                out.println("<td>" + "Rank" + "</td>");
                                out.println("<td>" + "Time Spent Together" + "</td>");
                                out.println("<td>" + "Companion" + "</td>");
                                out.println("<td>" + "Mac Address" + "</td>");
                                out.println("</tr></thead>");

                                List<Companion> results = companionContainer.getResults();
                                int tempRank = 0;
                                for (Companion c : results) {
                                        out.println("<tr>");
                                        int rank = c.getRank();
                                        if(tempRank == c.getRank()){
                                            out.println("<td> </td>");
                                            tempRank = rank;
                                        }
                                        else{
                                            out.println("<td>" + c.getRank() + "</td>");
                                            tempRank = rank;
                                        }
                                        out.println("<td>" + c.getTimeTogether() + "</td>");

                                        out.println("<td>");
                                        /*************remove if else(check if length is 0)****************/
                                        out.println(c.getCompanion());

                                        out.println("</td>");

                                        out.println("<td>");
                                        out.println(c.getMac_address());
                                        out.println("</td>");

                                        out.println("</tr>");                                        
                                }
                                out.println("</table>");
                            }
                        } else {
                            List<String> errorMessages = companionContainer.getMessages();
                            out.println(companionContainer.getStatus());
                            int num = 1;
                            for (String msg: errorMessages) {
                                out.println(num + ". " +msg + "<br>");
                                num++;
                            }
                        }
                    }
                    session.setAttribute("top_companions_result", null);
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