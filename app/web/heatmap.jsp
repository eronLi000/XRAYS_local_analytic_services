
<%@page import="java.util.List"%>
<%@page import="entity.HeatMapRow"%>
<%@page import="entity.HeatMapContainer"%>
<%@page import="com.google.gson.Gson"%>
<%@include file="protect.jsp" %> 
<jsp:include page="layouts/header.jsp" >
    <jsp:param name="active_tab" value="5" />
</jsp:include>

<script src="public/bootstrap/vendor/jquery/jquery.min.js"></script>
<script src="public/bootstrap/vendor/bootstrap/js/bootstrap.min.js"></script>
<script src="public/bootstrap/dist/js/moment.min.js"></script>
<link rel="stylesheet" href="public/bootstrap/dist/css/bootstrap-datetimepicker.min.css">
<script src="public/bootstrap/dist/js/bootstrap-datetimepicker.min.js"></script>

<link rel='stylesheet prefetch' href='public/floorplan/css/yyyqdl.css'>
<!--Generates graphical heatmap-->
<script>
    var heatmap = <%= session.getAttribute("heatmap_graphical") %>;
    var floor = <%= session.getAttribute("heatmap_floor") %>;
    if(floor !== null){
        var image_path = "public/floorplan/images/level_" + <%= session.getAttribute("heatmap_floor") %> + ".jpg";
    } else {
        var image_path = "public/floorplan/images/level_0.jpg";
    }
</script>
<!-- Page Content -->
<div id="page-wrapper">
    <div class="container-fluid">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">SIS Building Heat Map</h1>
                <div class="row">
                    <div class="col-lg-6">
                        <!--Passes to HeatMap Controller to generate heatmap in table form-->
                        <form action="HeatMapController" method="post">
                            <br>
                            <!--Dropbox for floor options-->
                            Select Level: 
                            <select name="floor">
                                <option value="0">Basement</option>
                                <%
                                    for (int i = 1; i <= 5; i++) {
                                        out.println("<option value=\"" + i + "\">" + i + "</option>");
                                    }
                                %>
                            </select>

                            <div class="form-group" style="padding-top: 30px">
                                <div class='input-group date' id='datetimepicker1'>
                                    <input type='text' name="date" class="form-control"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                </div>
                            </div>
                            <!--Submits query in both UI and json format-->
                            <input type="text" name="notJson" value="true" hidden="">
                            <button type="submit" class="btn btn-primary" onclick="showLoader()">Generate Heatmap</button>
                        </form>
                    </div>
                </div>
            </div>
            <!-- /.col-lg-12 -->
        </div>
        <!-- /.row -->
        
        <!--Gets heatmap results generated from heatmap controller-->
        <%
        if (session.getAttribute("heatmap_result") != null) {
            String json = (String) session.getAttribute("heatmap_result");
            Gson gson = new Gson();
            HeatMapContainer hmc = gson.fromJson(json, HeatMapContainer.class);
            
            if(hmc.getStatus().equals("success")){
                
                if (hmc.getHeatMap()== null || hmc.getHeatMap().isEmpty()) {
                %>
                                            <div class="alert alert-warning" role="alert" style="margin-top:50px;"> 
                                                <strong>Uh oh!</strong> 
                <%
                    List<String> messages = hmc.getMessages();
                    for (String msg : messages) {
                        out.println(msg);
                    }
                %>
                                            </div>
                <%
                }
                else{
        %>  
                <!-- Start of declaration for heatmap -->
                <div class="row">
                    <div class="col-lg-12">
                        <h3 style="text-align: center;">Results for <%=session.getAttribute("heatmap_title")%></h3>
                        <div id="heatmap"></div>
                    </div>
                </div>
                <!-- End of declaration for heatmap -->

                <!-- Start of declaration for legends -->
                <div class="row">
                    <div class="col-lg-12">
                        <table class="legend">
                            <thead>
                                <td>Legend</td>
                            </thead>
                            <tr>
                                <td>0 pax</td>
                                <td>1 - 2 pax</td>
                                <td>3 - 5 pax</td>
                                <td>6 - 10 pax</td>
                                <td>11 - 20 pax</td>
                                <td>21 - 30 pax</td>
                                <td>30 pax</td>
                            </tr>
                            <tr>
                                <td bgcolor="#00FF00">&nbsp;</td>
                                <td bgcolor="#54FF00">&nbsp;</td>
                                <td bgcolor="#AAFF00">&nbsp;</td>
                                <td bgcolor="#FFFF00">&nbsp;</td>
                                <td bgcolor="#FFAA00">&nbsp;</td>
                                <td bgcolor="#FF5400">&nbsp;</td>
                                <td bgcolor="#FF0000">&nbsp;</td>
                            </tr>
                        </table>
                    </div>
                </div>
                <!-- End of declaration for legends -->
                <!-- Start of declaration for heatmap table results -->
                <div class="row">
                    <div class="col-lg-12">
                        <table class="table table-hover" style="margin-top:30px">
                            <thead>
                                <tr>
                                    <td>Semantic Place</td>
                                    <td style="text-align: center">Mac Address Count</td>
                                    <td style="text-align: center">Crowd Density</td>
                                </tr>
                            </thead>
                            <%   
                            for (HeatMapRow hmr : hmc.getHeatMap()) {
                                out.println("<tr>");

                                out.println("<td>");
                                out.println(hmr.getSemanticPlace());
                                out.println("</td>");

                                out.println("<td style='text-align: center'>");
                                out.println(hmr.getMacAddCount());
                                out.println("</td>");

                                out.println("<td style='text-align: center'>");
                                out.println(hmr.getCrowdDensity());
                                out.println("</td>");

                                out.println("</tr>");

                            }
                        out.println("</table>");
                        %>
                    </div>
                </div>
            <!-- End of declaration for heatmap table results -->
        <%  }
            } else {
            %>
            <div class="alert alert-warning" role="alert" style="margin-top:50px;">
                <strong>Uh oh!</strong> 
                <%
                    List<String> messages = hmc.getMessages();
                        for (String msg : messages) {
                            out.println(msg);
                        }
                %>
            </div>

            <%
            }
        }
        
        session.setAttribute("heatmap_graphical", null);
        session.setAttribute("heatmap_title", null);
        session.setAttribute("heatmap_level", null);
        session.setAttribute("heatmap_result", null);
        %>
        
    
    <!-- /.container-fluid -->
</div>
<!-- /#page-wrapper -->

<!--Date picker for datetime input parameter for query-->
<script>
    
    $('#datetimepicker1').datetimepicker({
        defaultDate: '2017-02-06 10:00:17',
        format: 'YYYY-MM-DD HH:mm:ss',
        sideBySide: true    
    });
    
    
</script>

<script src='http://cdnjs.cloudflare.com/ajax/libs/d3/3.4.13/d3.min.js'></script>
<script src='public/floorplan/js/yyyqdl.js'></script>
<script src='public/floorplan/js/heatmap_levels.js'></script>



<jsp:include page="layouts/footer.jsp" />