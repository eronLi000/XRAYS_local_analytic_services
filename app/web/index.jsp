
<%@page import="dao.LocationDAO"%>
<%@page import="dao.LocationLookUpDAO"%>
<%@page import="dao.DemographicDAO"%>
<%@include file="protect.jsp" %> 

<jsp:include page="layouts/header.jsp">
    <jsp:param name="active_tab" value="1"/>
</jsp:include>

<div id="page-wrapper">
    <div class="container-fluid">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">Dashboard</h1>
                <div class="row">
                    <!-- Start Demographic Box -->
                    <div class="col-lg-4">
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
                    </div>
                    <!-- End Demographic Box -->
                    <!-- Start Location LookUp Box -->
                    <div class="col-lg-4">
                        <div class="panel panel-yellow">
                            <div class="panel-heading">
                                <div class="row">
                                    <div class="col-xs-3">
                                        <i class="fa fa-search fa-5x"></i>
                                    </div>
                                    <div class="col-xs-9 text-right">
                                        <div class="huge"><%=LocationLookUpDAO.retriveRowCount()%></div>
                                        <div>Location LookUp Table</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- End Location LookUp Box -->
                    <!-- Start Location Box -->
                    <div class="col-lg-4">
                        <div class="panel panel-primary">
                            <div class="panel-heading">
                                <div class="row">
                                    <div class="col-xs-3">
                                        <i class="fa fa-map-marker fa-5x"></i>
                                    </div>
                                    <div class="col-xs-9 text-right">
                                        <!--Retrieve row count from location DAO-->
                                        <div class="huge"><%=LocationDAO.retriveRowCount()%></div>
                                        <div>Location Table</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- End Location Box -->
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="layouts/footer.jsp" />
