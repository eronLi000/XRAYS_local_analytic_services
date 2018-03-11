
<%@page import="entity.GroupLocation"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.TreeMap"%>
<%@page import="entity.UserLocation"%>
<%@page import="entity.User"%>
<%@page import="java.util.List"%>
<%@page import="entity.Group"%>
<%@page import="java.util.ArrayList"%>
<%@page import="entity.GroupContainer"%>
<%@page import="com.google.gson.Gson"%>
<%@include file="protect.jsp" %> 
<jsp:include page="layouts/header.jsp" >
    <jsp:param name="active_tab" value="10" />
</jsp:include>

<script src="public/bootstrap/vendor/jquery/jquery.min.js"></script>
<script src="public/bootstrap/vendor/bootstrap/js/bootstrap.min.js"></script>
<script src="public/bootstrap/dist/js/moment.min.js"></script>
<link rel="stylesheet" href="public/bootstrap/dist/css/bootstrap-datetimepicker.min.css">
<script src="public/bootstrap/dist/js/bootstrap-datetimepicker.min.js"></script>

<!-- Page Content -->
<div id="page-wrapper">
    <div class="container-fluid">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">Auto Group Detection</h1>
                <div class="row">
                    <div class="col-lg-6">
                        <form action="AutoGroupController" method="post">
                            <div class="form-group">
                                <div class='input-group date' id='datetimepicker1' style="padding-top: 30px">
                                    <input type='text' name="date" class="form-control"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                </div>
                            </div>
                            <br><br>
                             <input type="text" name="notJson" value="true" hidden="">
                            <button type="submit" class="btn btn-primary" onclick="showLoader()">Detect Groups</button>
                        </form>
                    </div>
                </div>
                <div class="row" style="padding-top: 50px">
                    <div class="col-lg-12">
                        <%                                         
                            if (session.getAttribute("AutoGroupResult") != null) {

                                String json = (String) session.getAttribute("AutoGroupResult");
                                Gson gson = new Gson();
                                GroupContainer groupContainer = gson.fromJson(json, GroupContainer.class);
                                if(groupContainer.getStatus().equals("success")){

                                    out.println("Status: "+ groupContainer.getStatus() + "<br>");
                                    if (groupContainer.getGroups()== null || groupContainer.getGroups().isEmpty()) {
                                    %>
                                    <div class="alert alert-warning" role="alert" style="margin-top:50px;"> 
                                        <strong>Uh oh!</strong> 
                                        <% 
                                            List<String> messages = groupContainer.getMessages();
                                            for (String msg : messages){
                                                out.println(msg);
                                            }
                                        %>
                                    </div>
                                    <%
                                    }
                                    else {
                                        out.println("Total Users: "+ groupContainer.getTotalUsers() + "<br>");
                                        out.println("Total Groups: "+ groupContainer.getTotalGroups() + "<br>");
                                       

                                        ArrayList<Group> groupList = groupContainer.getGroups();
                                        int size = groupList.size();

                                        for (int i = 0; i < size; i++){
                                            Group group = groupList.get(i);
                                            ArrayList<User> members = group.getMembers();
                                            List<GroupLocation> groupLocations = group.getLocations();
                                             %>
                                            <div class="row" style="margin: 15px 0px 15px">
                                                <div class="col-lg-12" style="border-radius: 10px; background-color: #E3E8F8;">
                                                   
                                                        <div class="col-lg-3">
                                                            <div style="padding-top:50px;font-size:25px">
                                                        <%  
                                                            out.println("Size: " + group.getSize() +"<br>");
                                                            out.println("Total Time Spent: " + group.getTotal_time_spent() +"<br>");
                                                        %>
                                                            </div>
                                                        </div>
                                                        <div class="col-lg-9">
                                                            <div class="row" style="margin: 15px 0px 15px">
                                                                <div class="col-lg-12" style="border-radius: 10px; background-color: #FFF;">
                                                                    <div class="row" style="margin: 15px 0px 15px; background-color: #C0C5CD;border-radius: 10px; ">
                                                                        <%  

                                                                            out.println("<table class='table table'>");
                                                                            out.println("<tr style='font-weight:bold'><td style='width: 40%'>Email</td><td style='width: 60%'>Mac Address</td></tr>");    
                                                                            for(User member: members) {
                                                                                out.println("<tr>");
                                                                                out.println("<td>" + member.getEmail() +"</td>");
                                                                                out.println("<td>" + member.getMacAddress() +"</td></tr>");

                                                                            }
                                                                            out.println("</table>");
                                                                        %>
                                                                        
                                                                        
                                                                    </div>
                                                                    <div class="row" style="margin: 15px 0px 15px; background-color: #3E588F;border-radius: 10px;color:#fff ">
                                                                        <%
                                                                            out.println("<table class='table table'>");
                                                                            out.println("<tr style='font-weight:bold'><td style='width:40%'>Location</td><td style='width:60%'>Time Spent</td></tr>");    
                                                                            for(GroupLocation groupLocation : groupLocations) {
                                                                                out.println("<tr>");
                                                                                out.println("<td>" + groupLocation.getLocation() +"</td>");
                                                                                out.println("<td>" + groupLocation.getTime_spent() +"</td></tr>");

                                                                            }
                                                                            out.println("</table>");
                                                                        %>
                                                                        
                                                         
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            
                                                        </div>
                                                        
                                                </div>
                                            </div>
                                            <%
                                        }

                                    }       
                                }
                                else {
                                    List<String> errorMessages = groupContainer.getMessages();
                                    out.println(groupContainer.getStatus());
                                    int num = 1;
                                    for (String msg: errorMessages) {
                                        out.println(num + ". " +msg + "<br>");
                                        num++;
                                    }
                                }
                            }
                            session.setAttribute("AutoGroupResult", null);
                        %>     
                    </div>
                </div>
            </div>
            <!-- /.col-lg-12 -->
        </div>
        <!-- /.row -->
    </div>
    <!-- /.container-fluid -->
</div>
<!-- /#page-wrapper -->
<script>
    $('#datetimepicker1').datetimepicker({
        defaultDate: '2015-11-28 20:35:00',
        format: 'YYYY-MM-DD HH:mm:ss',
        sideBySide: true    
    });
</script>

<jsp:include page="layouts/footer.jsp" />