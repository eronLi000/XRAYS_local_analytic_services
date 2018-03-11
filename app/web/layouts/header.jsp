<%-- 
    Created on : 22 Sep, 2017, 3:19:23 PM
    Author     : Amos Lee, Samantha Nonis
--%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>XRAYS SLOCA APP</title>

    <!-- Bootstrap Core CSS -->
    <link href="public/bootstrap/vendor/bootstrap/css/bootstrap.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="public/bootstrap/vendor/metisMenu/metisMenu.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="public/bootstrap/dist/css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="public/bootstrap/vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">

</head>

<body>
    <div id="overlay">
        <div class="overlay_container">
            <p class="noselect">Crunching the numbers...</p>
            <div class="sk-folding-cube">
                <div class="sk-cube1 sk-cube"></div>
                <div class="sk-cube2 sk-cube"></div>
                <div class="sk-cube4 sk-cube"></div>
                <div class="sk-cube3 sk-cube"></div>
            </div>
        </div>
    </div>
    <div id="wrapper">
        <%
            String username = (String) session.getAttribute("username");
        %>
        <!-- Navigation -->
        <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="index.jsp">XRAYS SLOCA</a>
            </div>
            <!-- /.navbar-header -->

            <ul class="nav navbar-top-links navbar-right">
                <span style="color:#fff">Welcome back <%=username%></span>
                <li><a href="logout.jsp"><i class="fa fa-sign-out fa-fw"></i> Logout</a>
                
                <!-- /.dropdown -->
                <!--
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <i class="fa fa-user fa-fw"></i> <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-user">
                        <li><a href="#"><i class="fa fa-user fa-fw"></i> User Profile</a>
                        </li>
                        <li><a href="#"><i class="fa fa-gear fa-fw"></i> Settings</a>
                        </li>
                        <li class="divider"></li>
                        <li><a href="logout.jsp"><i class="fa fa-sign-out fa-fw"></i> Logout</a>
                        </li>
                    </ul>
                    <!-- /.dropdown-user -->
                </li>
                <!-- /.dropdown -->
            </ul>
            <!-- /.navbar-top-links -->

            <div class="navbar-default sidebar" role="navigation">
                <div class="sidebar-nav navbar-collapse">
                    <ul class="nav" id="side-menu">
                        
                        <li>
                            <a href="index.jsp" class="${(param.active_tab eq 1 or empty param.active_tab) ? 'active' : ''}"><i class="fa fa-dashboard fa-fw"></i> Dashboard</a>
                        </li>
                <%
                    
                    if(username.equals("admin")){
                        out.println("<li>");
                            out.println("<a href=\"#\"><i class=\"fa fa-bar-chart-o fa-fw\"></i> Admin Panel<span class=\"fa arrow\"></span></a>");
                            
                            out.println("<ul class=\"nav nav-second-level\">");
                                out.println("<li>");
                                    out.println("<a href=\"bootstrap.jsp\">Bootstrap SLOCA</a>");
                                out.println("</li>");
                                out.println("<li>");
                                    out.println("<a href=\"additional_csv.jsp\">Add Data</a>");
                                out.println("</li>");
                                out.println("<li>");
                                    out.println("<a href=\"reset.jsp\">Reset Data</a>");
                                out.println("</li>");
                            out.println("</ul>");
                            out.println("<!-- /.nav-second-level -->");
                            
                        out.println("</li>");
                
                    }
                %>
                        <li>
                            <a href="heatmap.jsp"><i class="fa fa-map-o fa-fw"></i> Heatmap</a>
                        </li>
                        <li>
                            <a href="#" class=""><i class="fa fa-line-chart fa-fw"></i> Basic Reports<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a href="BreakdownForm.jsp" class="">Breakdown Report</a>
                                </li>
                                <li>
                                    <a href="TopKPopularPlacesForm.jsp">Top-K Popular places</a>
                                </li>
                                <li>
                                    <a href="TopKCompanionsForm.jsp">Top-K Companions</a>
                                </li>
                                <li>
                                    <a href="TopKNextPlacesForm.jsp">Top-K Next Places</a>
                                </li>
                            </ul>
                            <!-- /.nav-second-level -->
                        </li>
                        <li>
                            <a href="auto_group.jsp"><i class="fa fa-group fa-fw"></i> Auto Group Detection</a>
                        </li>
                    </ul>
                </div>
                <!-- /.sidebar-collapse -->
            </div>
            <!-- /.navbar-static-side -->
        </nav>

        

    
