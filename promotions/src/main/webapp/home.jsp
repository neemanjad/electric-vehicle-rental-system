<%@ page import="home.project.beans.PromBean"%>
<%@ page import="home.project.beans.AnnounBean"%>
<%@ page import="java.util.ArrayList"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:useBean id="userBean" class="home.project.beans.UserBean" scope="session"></jsp:useBean>
<jsp:useBean id="promBean" class="home.project.beans.PromBean" scope="session"></jsp:useBean>
<jsp:useBean id="announBean" class="home.project.beans.AnnounBean" scope="session"></jsp:useBean>

<jsp:useBean id="appService" class="home.project.service.AppService" scope="application"></jsp:useBean>

<jsp:setProperty property="ann_title" name="announBean" param="ann_title" />
<jsp:setProperty property="ann_content" name="announBean" param="ann_content" />

<jsp:setProperty property="prom_title" name="promBean" param="prom_title" />
<jsp:setProperty property="prom_description" name="promBean" param="prom_description" />

<!DOCTYPE html>

<%
	if(userBean == null || !userBean.isLoggedIn())
		response.sendRedirect("login.jsp");
	else{		
		if(request.getParameter("prom_submit") != null){
			promBean.setProm_expirationDate(appService.convertStringToSqlDate(request.getParameter("prom_expirationDate")));
			if(appService.insertProm(promBean))
				session.setAttribute("prom_notification", "Promotion successfully added!");
			else
				session.setAttribute("prom_notification", "Promotion not added!");
			
		} else if(request.getParameter("ann_submit") != null){
			if(appService.insertAnnoun(announBean))
				session.setAttribute("ann_notification", "Announcement successfully added!");
			else
				session.setAttribute("ann_notification", "Announcement not added!");
		} else {
			session.setAttribute("prom_notification", "");
			session.setAttribute("ann_notification", "");
		}
	}
%>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0" >
		<title>ETFBL_IP</title>
		<link rel="stylesheet" href="css/home.css?v=1.1">
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
		<script type="text/javascript" src="js/home.js"></script>
	</head>
	
	<body>
		<header>
			<h1><%=userBean.getFirstName()%>, welcome!</h1>
			<form method="post" action="logout.jsp">
    			<button class="logout-btn" type="submit" id="logoutButton">Logout</button>
			</form>
		</header>

		<main>
   	 		<div class="content">
 				<div class="box">
        			<div class="box-title">PROMOTIONS</div>
        			<div class="form-container">
          				<p>Add promotion</p>
          				<form method="post" action="home.jsp" onsubmit="return validatePromFormEntry()">
            				<div class="row">
              					<div class="col-md-6 mb-3">
                					<label for="prom_title" class="form-label">Title</label>
                					<input type="text" class="form-control" name="prom_title" id="prom_title" required="required">
              					</div>
              					<div class="col-md-6 mb-3">
                					<label for="prom_content" class="form-label">Description</label>
                					<input type="text" class="form-control" name="prom_description" id="prom_content" required="required">
              					</div>
              					<div class="col-md-6 mb-3">
                					<label for="prom_expirationDate" class="form-label">Expiration date</label>
               	 					<input type="date" class="form-control" name="prom_expirationDate" id="prom_expirationDate" min="<%= java.time.LocalDate.now() %>" required="required">
              					</div>
            				</div>
            				<button type="submit" name="prom_submit" class="btn btn-primary w-100" id="prom_button">Add promotion</button>
          				</form>
        			</div>
        			
        			<p class="notification-cont" id="prom_notification"><%=session.getAttribute("prom_notification")%></p>
        			
        			<hr class="form-border">
        			<p class="section-title">Added promotions:</p>
        			
        			<div class="row">
    					<% for(PromBean promotion : appService.getPromotionPosts()) { %>
        				<div class="col-md-4 mb-4">
            				<div class="card">
                				<div class="card-body">
                    				<h5 class="card-title text-primary"><%= promotion.getProm_title() %></h5>
                    				<p class="card-text"><%= promotion.getProm_description() %></p>
                    				<p class="text-muted"><small>Expires on: <%= promotion.getProm_expirationDate() %></small></p>
                				</div>
            				</div>
        				</div>
    					<% } %>
					</div>
      			</div>

      			<div class="box">
        			<div class="box-title">ANNOUNCEMENT</div>
        			<div class="form-container">
          				<p>Add announcement</p>
          				<form method="post" action="home.jsp" onsubmit="return validateAnnounFormEntry()">
            				<div class="row">
              					<div class="col-md-6 mb-3">
                					<label for="ann_title" class="form-label">Title</label>
                					<input type="text" class="form-control" name="ann_title" id="ann_title" required="required">
              					</div>
              					<div class="col-md-6 mb-3">
                					<label for="ann_content" class="form-label">Content</label>
                					<input type="text" class="form-control" name="ann_content" id="ann_content" required="required">
              					</div>
            				</div>
            				<button type="submit" name="ann_submit" class="btn btn-primary w-100" id="ann_button">Add announcement</button>
          				 </form>
        			</div>
        			
        			<p class="notification-cont" id="ann_notification"><%=session.getAttribute("ann_notification")%></p>
			
					<hr class="form-border">
					
					<div class="search-box mx-auto">
						<form action="home.jsp" method="post">
							<div class="input-group">
        						<input type="text" class="form-control" placeholder="Enter text for search..." name="content_for_search" id="content_for_search">
        						<button class="btn btn-primary" type="submit" id="searchButton">Search</button>
      						</div>
						</form>
    				</div>
					
        			<p class="section-title">Added announcements:</p>
			
			        <div class="row">
    					<% for(AnnounBean announ : appService.getAnnounPosts(request.getParameter("content_for_search"))) { %>
        				<div class="col-md-4 mb-4">
            				<div class="card">
                				<div class="card-body">
                    				<h5 class="card-title text-primary"><%= announ.getAnn_title() %></h5>
                    				<p class="card-text"><%= announ.getAnn_content() %></p>
                				</div>
            				</div>
        				</div>
    					<% } %>
					</div>
      			</div>
    		</div>
  		</main>

		<footer>
    		<p>2025 &copy; ETFBL_IP</p>
  		</footer>
	</body>
</html>