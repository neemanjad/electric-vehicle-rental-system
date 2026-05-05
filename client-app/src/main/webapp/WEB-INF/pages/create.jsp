<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>ETFBL_IP</title>
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/create.css"></link>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/create.js"></script>
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
	</head>
	<body>
 		<div class="container">
        	<!-- Opis kompanije -->
        	<div class="mb-4 text-center">
            	<h2>Welcome to ETFBL_IP</h2>
            	<p>ETFBL_IP is a modern system for renting cars, bicycles, and scooters. Join us and enjoy fast and reliable transport options tailored to your needs!</p>
        	</div>

        	<!-- Forma za kreiranje profila -->
        	<form action="?action=newProfile" method="post" enctype="multipart/form-data" onsubmit="return validateFormEntries()">
            	<h3 class="text-center mb-4">Create Your Profile</h3>
            
            	<!-- Polje za ime i prezime -->
            	<div class="row mb-3">
                	<div class="col-md-6">
                    	<label for="firstName" class="form-label">First Name:</label>
                    	<input type="text" name="firstName" id="firstName" class="form-control" placeholder="John" required />
                	</div>
                	<div class="col-md-6">
                    	<label for="lastName" class="form-label">Last Name:</label>
                    	<input type="text" name="lastName" id="lastName" class="form-control" placeholder="Doe" required />
                	</div>
            	</div>
            
            	<!-- Username i Password -->
            	<div class="row mb-3">
                	<div class="col-md-6">
                    	<label for="userName" class="form-label">Username:</label>
                    	<input type="text" name="userName" id="userName" class="form-control" placeholder="johndoe123" required />
                	</div>
                	<div class="col-md-6">
                    	<label for="password" class="form-label">Password:</label>
                    	<input type="password" name="password" id="password" class="form-control" placeholder="********" required />
                	</div>
            	</div>
            
            	<!-- ID Card Number i Avatar Image -->
            	<div class="row mb-3">
                	<div class="col-md-6">
                    	<label for="idCard" class="form-label">ID Card Number:</label>
                    	<input type="text" name="idCard" id="idCard" class="form-control" placeholder="e.g., 12345678" required />
                	</div>
                	<div class="col-md-6">
                    	<label for="avatar" class="form-label">Avatar Image:</label>
                    	<input type="file" name="avatar" id="avatar" class="form-control" accept="image/*" />
                	</div>
            	</div>
            
            	<!-- Email i Phone Number -->
            	<div class="row mb-3">
                	<div class="col-md-6">
                    	<label for="email" class="form-label">Email:</label>
                    	<input type="email" name="email" id="email" class="form-control" placeholder="john.doe@example.com" required />
                	</div>
                	<div class="col-md-6">
                    	<label for="phoneNumber" class="form-label">Phone Number:</label>
                    	<input type="tel" name="phoneNumber" id="phoneNumber" class="form-control" placeholder="+123456789" required />
                	</div>
            	</div>
            
            	<!-- Dugme za kreiranje profila -->
            	<button type="submit" class="btn btn-primary w-100">Create Profile</button>
            
            	<!-- Div za notifikacije -->
            	<div id="notification" class="notification alert alert-danger">
                	<%=request.getAttribute("create_notification") != null ? request.getAttribute("create_notification") : ""%>
            	</div>
            	<script>
            		const notificationContent = "<%=request.getAttribute("create_notification") != null ? request.getAttribute("create_notification") : ""%>";
            		const notificationDiv = document.getElementById("notification");

            		if (notificationContent.trim() !== "") {
            	    	notificationDiv.style.display = "block"; // Prikaži div
            	    	if (notificationContent.trim().startsWith("Profile created")) {
            	            notificationDiv.className = "notification alert alert-success"; // Izmjena klase za uspjeh
            	        } else
            	            notificationDiv.className = "notification alert alert-danger";
            	}
            	</script>
        	</form>
        
        	<!-- Dugme za prijavu -->
        	<div class="text-center mt-3">
            	<form action="?action=login" method="post">
            		<button class="btn btn-secondary w-100" type="submit">Login</button>
            	</form>
        	</div>
    	</div>
	</body>
</html>