<%@page import="dao.LocationDAO"%>
<%@page import="dao.LocationLookUpDAO"%>
<%@page import="dao.DemographicDAO"%>
<%
    String username = (String) session.getAttribute("username");
    if (username == null || !username.equals("admin")) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<jsp:include page="layouts/header.jsp">
    <jsp:param name="active_tab" value="4"/>
</jsp:include>

<div id="page-wrapper">
    <div class="container-fluid">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">Reset SLOCA</h1>
                <div class="row">
                    <!-- Start Demographic Box -->
                    <div class="col-lg-4" style="padding-bottom: 30px;">
                        <div class="panel panel-green">
                            <div class="panel-heading">
                                <div class="row">
                                    <div class="col-xs-3">
                                        <i class="fa fa-group fa-5x"></i>
                                    </div>
                                    <div class="col-xs-9 text-right">
                                        <!--Retrieve row count from Demographics DAO-->
                                        <div class="huge"><%=DemographicDAO.retriveRowCount()%></div>
                                        <div>Demographics Table</div>
                                    </div>
                                </div>
                            </div>
                            
                        </div>
                        <!--Passes to Reset Controller to perform truncate function-->
                        <form action="ResetController" method="post">
                            <input value="true" name="demo" hidden="true">
                            <button type="submit" class="btn btn-danger btn-lg btn-block">Truncate Demographics</button>
                        </form>
                    </div>
                    <!-- End Demographic Box -->
                    <!-- Start Location LookUp Box -->
                    <div class="col-lg-4" style="padding-bottom: 30px;">
                        <div class="panel panel-yellow">
                            <div class="panel-heading">
                                <div class="row">
                                    <div class="col-xs-3">
                                        <i class="fa fa-search fa-5x"></i>
                                    </div>
                                    <div class="col-xs-9 text-right">
                                        <!--Retrieve row count from location Lookup DAO-->
                                        <div class="huge"><%=LocationLookUpDAO.retriveRowCount()%></div>
                                        <div>Location LookUp Table</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!--Passes to Reset Controller to perform truncate function-->
                        <form action="ResetController" method="post">
                            <input value="true" name="llu" hidden="true">
                            <button type="submit" class="btn btn-danger btn-lg btn-block">Truncate Location LookUp</button>
                        </form>
                    </div>
                    <!-- End Location LookUp Box -->
                    <!-- Start Location Box -->
                    <div class="col-lg-4" style="padding-bottom: 30px;">
                        <div class="panel panel-primary">
                            <div class="panel-heading">
                                <div class="row">
                                    <div class="col-xs-3">
                                        <i class="fa fa-map-marker fa-5x"></i>
                                    </div>
                                    <div class="col-xs-9 text-right">
                                        <!--Retrieve row count from Location DAO-->
                                        <div class="huge"><%=LocationDAO.retriveRowCount()%></div>
                                        <div>Location Table</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!--Passes to Reset Controller to perform truncate function-->
                        <form action="ResetController" method="post">
                            <input value="true" name="loc" hidden="true">
                            <button type="submit" class="btn btn-danger btn-lg btn-block">Truncate Location</button>
                        </form>
                    </div>
                    <!-- End Location Box -->
                </div>
                <!--Returns success or fail message from the action performed-->
                <%
                if (session.getAttribute("success") != null) {
                %> 
                    <div class="alert alert-success" role="alert" style="margin-top:50px;">
                        <strong>Woohoo!</strong> <%=session.getAttribute("success")%>
                    </div>
                <%
                } else if (session.getAttribute("fail") != null) {
                %>
                    <div class="alert alert-danger" role="alert" style="margin-top:50px;">
                        <strong>Uh oh!</strong> <%=session.getAttribute("fail")%>
                    </div>
                <%
                }    
                //<!--Resets session to handle new query-->
                session.setAttribute("success", null);
                session.setAttribute("fail", null);
                %>
            </div>
        </div>
    </div>
</div>

<jsp:include page="layouts/footer.jsp" />
