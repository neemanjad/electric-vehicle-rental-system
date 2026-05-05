<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="UTF-8">
		<title>ETFBL_IP</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login.css">
		<script src="${pageContext.request.contextPath}/js/login.js"></script>
	</head>
	<body>
		<div class="form-container">
			<form action="?action=login" method="post" onsubmit="return validateLoginFormEntry()">
				<h2>ETFBL_IP</h2>
                <div class="mb-3">
                    <input type="text" name="userName" id="userName" placeholder="Enter username:" class="form-control" required />
                </div>
                <div class="mb-3">
                    <input type="password" name="password" id="password" placeholder="Enter password:" class="form-control" required />
                </div>
                <button type="submit" name="submit">Login</button>
                <h5 id="notification"><%=session.getAttribute("notification")!=null?session.getAttribute("notification").toString():""%></h5>
			</form>
			<div class="text-center mt-3">
            	<form action="?action=create" method="post">
            		<button class="btn btn-secondary w-100" type="submit">Create new profile</button>
            	</form>
        	</div>
		</div>
	</body>
</html>