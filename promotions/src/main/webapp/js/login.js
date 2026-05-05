function validateLoginFormEntry() {
    const sqlInjectionPattern = /('|--|;|\/\*|\*\/)/;
    if (sqlInjectionPattern.test(document.getElementById("userName").value) || 
		sqlInjectionPattern.test(document.getElementById("password").value)) {
			
			document.getElementById("notification").innerHTML = "The entry contains potential SQL Injection characters!";
		   	document.forms[0].reset();
			return false;
    }

    const xssPattern = /<script.*?>.*?<\/script>|<.*?>/;
    if (xssPattern.test(document.getElementById("userName").value) ||
		xssPattern.test(document.getElementById("password").value)) {
			
			document.getElementById("notification").innerHTML = "The entry contains a potential XSS attack!";
			document.forms[0].reset();
			return false;
    }

    const maxLength = 50;
    if (document.getElementById("userName").value.length > maxLength ||
		document.getElementById("password").value.length > maxLength) {
			
			document.getElementById("notification").innerHTML = "The input is too long and may cause a buffer overflow!";
			document.forms[0].reset();
			return false;
    }
	
	return true;
}