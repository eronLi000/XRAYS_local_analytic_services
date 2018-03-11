
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!-- display error message -->
<%
    String username = (String) session.getAttribute("username");
    if (username != null) {
        response.sendRedirect("index.jsp");
    }
%>

<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">

        <title>XRAYS SLOCA  - Login</title>
        <link href="public/bootstrap/custom/login.css" rel="stylesheet">
        <link href='https://fonts.googleapis.com/css?family=Work+Sans:400,300,700' rel='stylesheet' type='text/css'>
        <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
    </head>

    <body>
        <div class="container">
            <div class="profile profile--open">
                <button class="profile__avatar">
                    <img src="public/images/xrays_logo.png" alt="Avatar"/>
                </button>
                <div class="profile__form">
                    <form action="AuthController" method="post">
                        <div class="profile__fields">
                            <div class="field">
                                <input type="text" id="fieldUser" class="input" name="username" required />
                                <label for="fieldUser" class="label">Username</label>
                            </div>
                            <div class="field">
                                <input type="password" id="fieldPassword" class="input" name="password" required />
                                <label for="fieldPassword" class="label">Password</label>
                            </div>
                            <input type="text" name="notJson" value="true" hidden="">
                            <button class="btn">Login</button>
                        </div>

                        <%
                            if(request.getAttribute("error") != null){
                                List<String> errorMessage = (List<String>) request.getAttribute("error");

                                if (errorMessage != null) {
                                    out.println("<div class='errorMsg'>");
                                    for (String msg: errorMessage) {
                                        out.println("*" + msg + "*<br>");
                                    }
                                    out.println("</div>");
                                }
                            }
                        %>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
