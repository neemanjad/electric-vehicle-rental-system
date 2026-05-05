function changePassword() {
    const notification = document.getElementById("notification");
    
    // Validacija ili drugi procesi
    const oldPassword = document.getElementById("oldPassword").value;
    const newPassword = document.getElementById("newPassword").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

	// Resetovanje notifikacije prije validacije
	notification.style.display = "none";
	notification.className = ""; // Uklanja prethodni stil
	
    if (newPassword !== confirmPassword) {
        notification.style.display = "block";
        notification.className = "alert alert-danger"; // Bootstrap klasa za stilizovanje
        notification.innerHTML = "New passwords do not match!";
    } else {
		if(validateEntry()){
			let container = {
				oldPassword: oldPassword,
				newPassword: newPassword
			};
			sendPasswordData(container);	
		}	
    }
}

async function sendPasswordData(container){
	try{
		const response = await fetch('http://192.168.0.19:8000/ETFBL_IP_Clients/?action=pass', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			credentials: 'include',
			body: JSON.stringify(container)
		});
		
		if(!response.ok){
			throw new Error(`Server error: ${response.status}`);
		}
		
		const notification = document.getElementById("notification");
		notification.style.display = "block";
		notification.className = "alert alert-success"; // Bootstrap klasa
		notification.innerHTML = "Password successfully changed!";
		
	} catch(error){
		console.log("Error trying to change password");
	}
}

function validateEntry(){
	const notification = document.getElementById("notification");
	
	const sqlInjectionPattern = /('|--|;|\/\*|\*\/)/;
	if (sqlInjectionPattern.test(document.getElementById("confirmPassword").value) || sqlInjectionPattern.test(document.getElementById("oldPassword").value)) {
		notification.style.display = "block";
		notification.className = "alert alert-danger"; // Bootstrap klasa
		notification.innerHTML = "The entry contains potential SQL Injection characters!";
		document.forms[0].reset();
		return false;
	}

	const xssPattern = /<script.*?>.*?<\/script>|<.*?>/;
	if (xssPattern.test(document.getElementById("confirmPassword").value) || xssPattern.test(document.getElementById("oldPassword").value)) {
		notification.style.display = "block";
		notification.className = "alert alert-danger"; // Bootstrap klasa
		notification.innerHTML = "The entry contains a potential XSS attack!";
		document.forms[0].reset();
		return false;	
	}

	const maxLength = 50;
	if (document.getElementById("confirmPassword").value.length > maxLength || document.getElementById("oldPassword").value.length > maxLength) {
		notification.style.display = "block";
		notification.className = "alert alert-danger"; // Bootstrap klasa
		notification.innerHTML = "The input is too long and may cause a buffer overflow!";
		document.forms[0].reset();
		return false;
	}
	return true;
}