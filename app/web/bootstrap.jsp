
<%@page import="entity.BSSuccess"%>
<%@page import="entity.BSError"%>
<%@page import="entity.BSContainer"%>
<%@page import="com.google.gson.Gson"%>
<%
    String username = (String) session.getAttribute("username");
    if (username == null || !username.equals("admin")) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<jsp:include page="layouts/header.jsp" >
    <jsp:param name="active_tab" value="2" />
</jsp:include>

<!-- Page Content -->
<div id="page-wrapper">
    <div class="container-fluid">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">Bootstrap SLOCA</h1>
                <form action="BootstrapController" method="post" enctype="multipart/form-data">
                    <input type="file" name="bootstrap-file" />
                    <input type="text" name="notJson" value="true" hidden="">
                    <br>
                    <button type="submit" class="btn btn-primary" onclick="showLoader()">Bootstrap System</button>
                </form>

                <!-- checks return status and returns relevant result, with specific error message -->
                <%
                    if (session.getAttribute("success") != null) {
                %> 
                <div class="alert alert-success" role="alert" style="margin-top:50px;">
                    <strong>Well done!</strong> <%=session.getAttribute("success")%>
                </div>
                <%
                } else if (session.getAttribute("fail") != null) {
                %>
                <div class="alert alert-danger" role="alert" style="margin-top:50px;">
                    <strong>Uh oh!</strong> <%=session.getAttribute("fail")%>
                </div>
                <%
                } else if (session.getAttribute("warning") != null) {
                %>
                <div class="alert alert-warning" role="alert" style="margin-top:50px;">
                    <strong>Uh oh!</strong> <%=session.getAttribute("warning")%>
                </div>
                <%
                    }

                    if (session.getAttribute("errorMessage") != null) {
                        String json = (String) session.getAttribute("errorMessage");
                        Gson gson = new Gson();
                        BSContainer bsc = gson.fromJson(json, BSContainer.class);

                        if (bsc.getStatus().equals("Success")) {
                            out.println("Status : <span class='badge badge-success'>" + bsc.getStatus() + "</span> <br><br>");
                        } else {
                            out.println("Status : <span class='badge badge-warning'>" + bsc.getStatus() + "</span> <br><br>");
                        }
                        out.println("Number of rows successfully inserted:<br>");
                        for (BSSuccess bss : bsc.getRecordLoaded()) {
                            if (bss == null){
                                break;
                            } else {
                                if (bss.getDemographicscsv() != null){
                                    out.println("demographics.csv : " + bss.getDemographicscsv() + "<br>");
                                }
                                if (bss.getLocation_lookupcsv() != null){
                                    out.println("location-lookup.csv : " + bss.getLocation_lookupcsv() + "<br>");
                                }

                                if (bss.getLocationcsv() != null){
                                    out.println("location.csv : " + bss.getLocationcsv() + "<br>");
                                }
                            }
                        }

                        if (session.getAttribute("warning") != null) {

                            out.println("<div class='FixedHeightContainer'><div class='FixedHeightContent'>");
                            int counter =0;
                            for (BSError obj : bsc.getErrObj()) {
                                if (obj.getFile().equals("demographics.csv")) {
                                    counter++;
                                    out.println("File : " + obj.getFile() + "<br>");
                                    out.println("Line : " + obj.getLine() + "<br>");
                                    out.println("Message : " + obj.getErrMsgArr() + "<br><br>");
                                }

                            }
                            out.println("</div>");
                            if(counter > 0){
                                out.println("<p style='margin: 10px 0 0 10px;color:#cc0000'>Demographic Error Counter: " + counter + "</p>");
                            }
                            out.println("</div>");

                            out.println("<div class='FixedHeightContainer'><div class='FixedHeightContent'>");
                            counter = 0;
                            for (BSError obj : bsc.getErrObj()) {
                                if (obj.getFile().equals("location-lookup.csv")) {
                                    counter++;
                                    out.println("File : " + obj.getFile() + "<br>");
                                    out.println("Line : " + obj.getLine() + "<br>");
                                    out.println("Message : " + obj.getErrMsgArr() + "<br><br>");
                                }

                            }
                            out.println("</div>");
                            if(counter > 0){
                                out.println("<p style='margin: 10px 0 0 10px;color:#cc0000'>Location LookUp Error Counter: " + counter + "</p>");
                            }
                            out.println("</div>");
                            
                            counter = 0;
                            out.println("<div class='FixedHeightContainer'><div class='FixedHeightContent'>");
                            for (BSError obj : bsc.getErrObj()) {
                                if (obj.getFile().equals("location.csv")) {
                                    counter++;
                                    out.println("File : " + obj.getFile() + "<br>");
                                    out.println("Line : " + obj.getLine() + "<br>");
                                    out.println("Message : " + obj.getErrMsgArr() + "<br><br>");
                                }

                            }
                            out.println("</div>");
                            if(counter > 0){
                                out.println("<p style='margin: 10px 0 0 10px;color:#cc0000'>Location Error Counter: " + counter + "</p>");
                            }
                            out.println("</div>");
                        }
                    }

                    session.setAttribute("success", null);
                    session.setAttribute("fail", null);
                    session.setAttribute("warning", null);
                    session.setAttribute("errorMessage", null);
                %>
            </div>
            <!-- /.col-lg-12 -->
        </div>
        <!-- /.row -->
    </div>
    <!-- /.container-fluid -->
</div>
<!-- /#page-wrapper -->


<jsp:include page="layouts/footer.jsp" />