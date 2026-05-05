<%@ page import="java.util.ArrayList" %>
<%@ page import="home.project.beans.RentalBean" %>
<%@ page import="home.project.model.Rental" %>
<%@ page import="home.project.beans.PromotionBean" %>
<%@ page import="home.project.beans.AnnouncementBean" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:useBean id="userBean" type="home.project.beans.UserBean" scope="session"></jsp:useBean>
<jsp:useBean id="rentalBean" type="home.project.beans.RentalBean" scope="request"></jsp:useBean>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>ETFBL_IP</title>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/profile.js"></script>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/profile.css"></link>
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
	</head>
	<body>
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
        		 <div class="row">
    				<% 
    					@SuppressWarnings("unchecked")
        				ArrayList<PromotionBean> pBeans = (ArrayList<PromotionBean>) session.getAttribute("pBeans");
        				if(pBeans == null)
        					pBeans = new ArrayList<>();
    				for (PromotionBean pb : pBeans) { 
    				%>
    				<div class="col-12 mb-4">
        				<div class="card">
            				<div class="card-body">
                				<h5 class="card-title text-primary"><%= pb.getTitle() %></h5>
                				<p class="card-text"><%= pb.getDescription() %></p>
                				<p class="text-muted"><small>Expires on: <%= pb.getExpirationDate() %></small></p>
     	      				</div>
        				</div>
    				</div>
    				<% } %>
				</div>
        	</div>
        	
        	<div class="center-section">
    			<div class="container mt-4">
    				<div class="row align-items-center">
    					<!-- Lijeva strana: Fotografija, ime, prezime i username -->
    					<div class="col-12 col-md-4 text-center">
        					<img src="data:image/jpeg;base64,${userBean.user.pictureAsStringBase64}" 
             							alt="Profile Picture" class="img-thumbnail mb-3" style="max-width: 200px;">
        					<h4 class="fw-bold"><%=userBean.getUser().getFirstName() + " " + userBean.getUser().getLastName()%></h4>
        					<p class="text-muted">@<%=userBean.getUser().getUserName()%></p>
    					</div>

    					<!-- Desna strana: Osnovne informacije -->
    					<div class="col-12 col-md-8">
        					<div class="card">
            					<div class="card-header bg-primary text-white">
                					<h5 class="mb-0 text-center">Basic Information</h5>
           						 </div>
            					<div class="card-body text-center">
                					<p><strong>Name: </strong><%=userBean.getUser().getFirstName()%></p>
                					<p><strong>Last name: </strong><%=userBean.getUser().getLastName()%></p>
                					<p><strong>Username: </strong><%=userBean.getUser().getUserName()%></p>
                					<div class="d-flex justify-content-center">
                    					<!-- Dugme za promjenu lozinke -->
                    					<button type="button" class="btn btn-primary mt-3 me-2" data-bs-toggle="modal" data-bs-target="#passwordModal">
                        					Change Password
                    					</button>
                    					<!-- Dugme za deaktivaciju naloga -->
                    					<button type="button" class="btn btn-danger mt-3" data-bs-toggle="modal" data-bs-target="#deactivateModal">
                        					Deactivate Account
                    					</button>
                					</div>
            					</div>
        					</div>
    					</div>
					</div>
    
    				<!-- Modal za promjenu lozinke -->
    				<div class="modal fade" id="passwordModal" tabindex="-1" aria-labelledby="passwordModalLabel" aria-hidden="true">
        				<div class="modal-dialog modal-dialog-centered">
            				<div class="modal-content">
                				<div class="modal-header bg-primary text-white">
                    				<h5 class="modal-title" id="passwordModalLabel">Change Password</h5>
                    				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                				</div>
               					 <div class="modal-body">
                    				<form action="/changePassword" method="POST">
                        				<div class="mb-3">
                            				<label for="oldPassword" class="form-label">Old Password:</label>
                            				<input type="password" class="form-control" id="oldPassword" name="oldPassword" required>
                        				</div>
                        				<div class="mb-3">
                            				<label for="newPassword" class="form-label">New Password:</label>
                            				<input type="password" class="form-control" id="newPassword" name="newPassword" required>
                        				</div>
                        				<div class="mb-3">
                            				<label for="confirmPassword" class="form-label">New Password Again:</label>
                            				<input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                        				</div>
                        				<button class="btn btn-success w-100" type="button" onclick="changePassword()">SUBMIT</button>
                        				<div id="notification" class="mt-3" style="display: none;"></div>
                    				</form>
                				</div>
            				</div>
        				</div>
    				</div>
				</div>
				
				<div class="modal fade" id="deactivateModal" tabindex="-1" aria-labelledby="deactivateModalLabel" aria-hidden="true">
    				<div class="modal-dialog">
        				<div class="modal-content">
            				<div class="modal-header">
                				<h5 class="modal-title" id="deactivateModalLabel">Deactivate Account</h5>
                				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            				</div>
            				<div class="modal-body">
                				Are you sure you want to deactivate your account? This action cannot be undone.
            				</div>
            				<div class="modal-footer">
                				<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                				<form action="?action=deactivate" method="post">
                					<button class="btn btn-danger" type="submit">Deactivate</button>
                				</form>
            				</div>
        				</div>
    				</div>
				</div>

				<div class="container mt-4">
    				<div class="row">
        				<div class="col-12">
            				<table class="table table-bordered table-striped table-hover w-100 text-center">
                				<thead class="table-primary text-center align-middle">
                    				<tr>
                    					<th style="color: blue;">NMB</th>
        								<th style="color: blue;">DATE-TIME</th>
        								<th style="color: blue;">PATH</th>
        								<th style="color: blue;">PRICE</th>
        								<th style="color: blue;">VEHICLE ID</th>
        								<th style="color: blue;">LICENCE NUMBER</th>
        								<th style="color: blue;">DOCUMENT NUMBER</th>
        								<th style="color: blue;">DURATION</th>
                    				</tr>
                				</thead>
               		 			<tbody>
               		 				<%  
               		 					ArrayList<Rental> rentals = rentalBean.getRentals();	
               		 					for(int i=1; i<=rentals.size(); i++) {
               		 				   		Rental rental = rentals.get(i-1);
               		 				%>
                    				<tr class="<%=i % 2 == 0 ? "table-light" : "table-secondary"%>">
                    					<td><%=i%></td>
                        				<td><%=rental.getDateTime()%></td>
                        				<td><%="from (" + rental.getStartX() + "," + rental.getStartY() + ") to " + 
                        						"(" + rental.getEndX() + "," + rental.getEndY() + ")"%></td>
                        				<td><%=rental.getPrice() + " $"%></td>
                        				<td><%=rental.getVehicleID()%></td>
                        				<td><%=rental.getLicenceNumber() != null ? rental.getLicenceNumber() : "-" %></td>
                        				<td><%=rental.getDocumentNumber() != null ? rental.getDocumentNumber() : "-" %></td>
                        				<td><%=rental.getSeconds() + " s"%></td>
                    				</tr>
        							<%}%>
                				</tbody>
            				</table>
        				</div>
    				</div>
				</div>
				
				<form action="?action=back" method="POST">
    					<button id="back-to-main" type="submit">BACK</button>
				</form>
			</div>
        		
        	<div class="ann-column">
				<div class="row">
					<%
						@SuppressWarnings("unchecked")
						ArrayList<AnnouncementBean> aBeans = (ArrayList<AnnouncementBean>) session.getAttribute("aBeans");
						if (aBeans == null)
					    	aBeans = new ArrayList<>();
        				for (AnnouncementBean ab : aBeans) { 
    				%>
    				<div class="col-12 mb-4">
        				<div class="card">
            				<div class="card-body">
                				<h5 class="card-title text-primary"><%=ab.getTitle() %></h5>
                				<p class="card-text"><%=ab.getContent() %></p>
            				</div>
        				</div>
    				</div>
    				<% } %>
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