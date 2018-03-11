
<%@page import="entity.BreakdownContainer"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="entity.Breakdown"%>
<%@page import="java.util.List"%>
<%@include file="protect.jsp" %> 
<jsp:include page="layouts/header.jsp">
    <jsp:param name="active_tab" value="6"/>
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
                <h1 class="page-header">Breakdown by Year/Gender/School</h1>
                <div class="row">
                    <div class="col-lg-6">
                        <form action="BreakdownController" method="post">
                            <div class="form-group" style="padding-top: 30px">
                                <!-- DateTime input -->
                                <div class='input-group date' id='datetimepicker1'>
                                    <input type='text' name="date" class="form-control"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                </div>
                            </div>
                            <br>
                            <!-- Order input -->
                            Select a type of breakdown:
                            <select name="order">
                                <option value ="gender"> Gender </option>
                                <option value ="year"> Year </option>
                                <option value ="school"> School </option>
                                <option value ="gender,year"> Gender / Year </option>
                                <option value ="gender,school"> Gender / School </option>
                                <option value ="year,gender"> Year / Gender </option>
                                <option value ="year,school"> Year / School </option>
                                <option value ="school,gender"> School / Gender </option>
                                <option value ="school,year"> School / Year </option>
                                <option value ="gender,year,school"> Gender / Year / School </option>
                                <option value ="gender,school,year"> Gender / School / Year </option>
                                <option value ="year,gender,school"> Year / Gender / School </option>
                                <option value ="year,school,gender"> Year / School / Gender </option>
                                <option value ="school,gender,year"> School / Gender / Year </option>
                                <option value ="school,year,gender"> School / Year / Gender </option>
                            </select>
                            <input type="text" name="notJson" value="true" hidden="">
                            <br><br>
                            <button type="submit" class="btn btn-primary" onclick="showLoader()">Generate</button>

                        </form>
                        
                    </div>
                </div>
                
                <%
                if (session.getAttribute("breakdownResult") != null) {

                    String json = (String) session.getAttribute("breakdownResult");
                    Gson gson = new Gson();
                    BreakdownContainer breakdownContainer = gson.fromJson(json, BreakdownContainer.class);
                    if (breakdownContainer.getStatus().equals("success")) {

                        if (breakdownContainer.getBreakdown() == null || breakdownContainer.getBreakdown().isEmpty()) {
                            %>
                            <div class="alert alert-warning" role="alert" style="margin-top:50px;"> 
                                <strong>Uh oh!</strong> 
                                <% 
                                    List<String> messages = breakdownContainer.getMessages();
                                    for (String msg : messages){
                                        out.println(msg);
                                    }
                                %>
                            </div>
                            <%
                        } else {
                            List<Breakdown> breakdownList1 = breakdownContainer.getBreakdown();
                            int totalCount = breakdownContainer.getTotalCount();
                            //Start of for loop for Breakdown 1
                            for (Breakdown br1 : breakdownList1) {
                                int count = br1.getCount();
                                long percentage = Math.round(((count * 1.0) / totalCount) * 100);
                            %>
                            <div class="row" style="margin: 15px 0px 15px">
                                <div class="col-lg-12" style="border-radius: 10px; background-color: #E3E8F8;">
                                    <table class='table table'>
                                    <tr>
                                    <td style="font-weight: bold;width:20%">
                                        <%
                                            //br1.getOrder().substring(0, 1).toUpperCase() + br1.getOrder().substring(1)
                                            if(br1.getYear() != null){
                                                out.println(br1.getYear());
                                            }
                                            
                                            else if (br1.getSchool() != null){
                                                out.println(br1.getSchool());
                                            }
                                            
                                            else if(br1.getGender() != null){
                                                out.println(br1.getGender());
                                            }                                           
                                        %>
                                    :</td>
                                    <td><%=br1.getCount()%></td>
                                    </tr>
                                    <td style="font-weight: bold">Percentage</td>
                                    <td><%=percentage%>%</td>
                                    </tr>
                                    </table>
                                    <%
                                    //Start check if Breakdown 2 exits
                                    if (br1.getBreakdown() != null) {
                                        List<Breakdown> breakdownList2 = br1.getBreakdown();
                                        //Start of for loop for Breakdown 2
                                        for (Breakdown br2 : breakdownList2) {
                                            int count2 = br2.getCount();
                                            percentage = Math.round(((count2 * 1.0) / totalCount) * 100);
                                    %>  
                                    <div class="row" style="background-color: #C0C5CD; border-radius: 10px; margin: 10px">
                                        <div class="col-lg-4">
                                            <table class='table table'>
                                            <tr>
                                            <td style="font-weight: bold;width:20%">
                                                <%
                                                    //br1.getOrder().substring(0, 1).toUpperCase() + br1.getOrder().substring(1)
                                                    if(br2.getYear() != null){
                                                        out.println(br2.getYear());
                                                    }

                                                    else if (br2.getSchool() != null){
                                                        out.println(br2.getSchool());
                                                    }

                                                    else if(br2.getGender() != null){
                                                        out.println(br2.getGender());
                                                    }                                           
                                                %>
                                            :</td>
                                            <td><%=br2.getCount()%></td>
                                            </tr>
                                            <td style="font-weight: bold;">Percentage:</td>
                                            <td><%=percentage%>%</td>
                                            </tr>
                                            </table>
                                        </div>
                                        <%
                                        //Start check if Breakdown 2 exits
                                        if (br2.getBreakdown() != null) {
                                            out.println("<div class='col-lg-8'>");
                                            List<Breakdown> breakdownList3 = br2.getBreakdown();
                                            //Start of for loop for Breakdown 2
                                            for (Breakdown br3 : breakdownList3) {
                                                int count3 = br3.getCount();
                                                percentage = Math.round(((count3 * 1.0) / totalCount) * 100);
                                        %>  
                                        <div class="row" style="background-color: #3E588F; color: white;border-radius: 10px; margin: 10px">
                                            <div class="col-lg-12">
                                                <table class='table table'>
                                                <tr>
                                                <td style="font-weight: bold;width:20%">
                                                <%
                                                    //br1.getOrder().substring(0, 1).toUpperCase() + br1.getOrder().substring(1)
                                                    if(br3.getYear() != null){
                                                        out.println(br3.getYear());
                                                    }

                                                    else if (br3.getSchool() != null){
                                                        out.println(br3.getSchool());
                                                    }

                                                    else if(br3.getGender() != null){
                                                        out.println(br3.getGender());
                                                    }                                           
                                                %>
                                                :</td>
                                                <td><%=br3.getCount()%></td>
                                                </tr>
                                                <td style="font-weight: bold;">Percentage:</td>
                                                <td><%=percentage%>%</td>
                                                </tr>
                                                </table>
                                            </div>
                                        </div>
                                            <% 
                                            }
                                            out.println("</div>");
                                        }
                                        %>
                                    </div>
                                    <% 
                                    }
                                }
                                %>
                                </div>
                            </div>
                            <% 
                            }
                        }

                    }else {
                        List<String> errorMessages = breakdownContainer.getMessages();
                        out.println(breakdownContainer.getStatus());
                        int num = 1;
                        for (String msg: errorMessages) {
                            out.println(num + ". " +msg + "<br>");
                            num++;
                        }
                    }
                }
                session.setAttribute("breakdownResult", null);
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