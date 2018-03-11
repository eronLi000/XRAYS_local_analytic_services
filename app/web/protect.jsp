
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<!--Ensure that a valid session is currently running-->
<!--Included at the start of every page to ensure that there is a valid user using the app-->
<!--If user is not valid, redirects back to login page-->
<%
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>