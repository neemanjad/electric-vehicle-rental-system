<%@page import="home.project.beans.AnnouncementBean"%>
<%@page import="home.project.beans.PromotionBean"%>
<%@page import="java.util.ArrayList"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:useBean id="userBean" type="home.project.beans.UserBean" scope="session"></jsp:useBean>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>ETFBL_IP</title>
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
		<script src="${pageContext.request.contextPath}/js/home.js"></script>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/home.css"></link>
	</head>
	<body onload="refreshData()">
		<header>
    		<div class="d-flex align-items-center justify-content-center w-100">
        		<!-- Centrirana slika i korisnički podaci -->
        		<img src="data:image/jpeg;base64,${userBean.user.pictureAsStringBase64}" 
           		  		alt="Profile Picture" class="img-thumbnail rounded-circle me-3" style="width: 40px; height: 40px;">
        		<div>
            		<span class="fw-bold"><%= userBean.getUser().getFirstName() %> <%= userBean.getUser().getLastName() %></span><br>
            		<span class="text-muted">@<%= userBean.getUser().getUserName() %></span>
        		</div>
    		</div>
    
    		<!-- Logout dugme desno -->
    		<form method="post" action="?action=logout" class="position-absolute" style="right: 20px; top: 50%; transform: translateY(-50%);">
        		<button class="logout-btn" type="submit" id="logoutButton">Logout</button>
    		</form>
		</header>
		
    	<div class="main-container">
        	<div class="ann-column">
        		 <div class="row" id="promotions">
    				
				</div>
        	</div>
        	
        	<div id="content">
        		<div class="button-container">
        			<button class="content-button" onclick="loadContent('scooter')">SCOOTER RENTAL</button>
        			<button class="content-button" onclick="loadContent('bicycle')">BICYCLE RENTAL</button>
        			<button class="content-button" onclick="loadContent('car')">CAR RENTAL</button>
        			<form action="?action=profile" method="POST">
    					<button class="content-button" type="submit">MY PROFILE</button>
					</form>
    			</div>
        	</div>
        		
        	<div class="ann-column">
				<div class="row" id="announcements">
				
				</div>
			</div>
    	</div>
    
    	<footer>
    		<p>
    			2025 &copy; ETFBL_IP <br/>
    			Patre 5, 78 000 Banja Luka, Republic of Srpska
    		</p>
  		</footer>
	</body>
</html>