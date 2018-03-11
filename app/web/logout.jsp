

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<!--Invalidates this session then unbinds any objects bound to it-->
<!--Redirects back to login page-->
<%
    session.invalidate();
    response.sendRedirect("login.jsp");
%>
