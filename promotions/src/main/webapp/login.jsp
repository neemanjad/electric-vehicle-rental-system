<%@ page import="home.project.beans.UserBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:useBean id="userBean" class="home.project.beans.UserBean" scope="session"></jsp:useBean>
<jsp:useBean id="appService" class="home.project.service.AppService" scope="application"></jsp:useBean>

<jsp:setProperty property="userName" name="userBean" param="userName" />
<jsp:setProperty property="password" name="userBean" param="password" />

<!DOCTYPE html>
<% 
	if(userBean != null && userBean.isLoggedIn())
		response.sendRedirect("home.jsp");
	else
    	if(request.getParameter("submit") != null){
    		UserBean tmp = appService.loginUser(userBean);
    		if(tmp != null){    
    			userBean.setFirstName(tmp.getFirstName());
    			userBean.setLastName(tmp.getLastName());
                
    			userBean.setBlocked(false);
    			userBean.setLoggedIn(true);
    			
    			session.setAttribute("notification", "");
    			response.sendRedirect("home.jsp");          
    		} else{
    			session.setAttribute("notification", "Wrong credentials!");
    			userBean.setLoggedIn(false);
    		}       
    	} else
    		session.setAttribute("notification", "");
%>

<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>ETFBL_IP</title>
        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="css/login.css">
        <script type="text/javascript" src="js/login.js"></script>
    </head>
    
    <body>
        <div class="form-container">
            <form action="login.jsp" method="post" onsubmit="return validateLoginFormEntry()">
                <h2>ETFBL_IP</h2>
                <div class="mb-3">
                    <input type="text" name="userName" id="userName" placeholder="Enter username:" class="form-control" required />
                </div>
                <div class="mb-3">
                    <input type="password" name="password" id="password" placeholder="Enter password:" class="form-control" required />
                </div>
                <button type="submit" name="submit">Login</button>
                <h5 id="notification"><%=session.getAttribute("notification").toString()%></h5>
            </form>
        </div>
    </body>
</html>
