function validatePromFormEntry() {
	const sqlInjectionPattern = /(--|;|\/\*|\*\/|'|"|=|OR\s+\d+=\d+|DROP|SELECT|INSERT|DELETE|UPDATE)/i;
    if (sqlInjectionPattern.test(document.getElementById("prom_title").value) || 
		sqlInjectionPattern.test(document.getElementById("prom_content").value)) {
			
			document.getElementById("prom_notification").innerHTML = "The entry contains potential SQL Injection characters!";
		   	document.forms[0].reset();
			return false;
    }

    const xssPattern = /<script.*?>.*?<\/script>|<.*?>/;
    if (xssPattern.test(document.getElementById("prom_title").value) ||
		xssPattern.test(document.getElementById("prom_content").value)) {
			
			document.getElementById("prom_notification").innerHTML = "The entry contains a potential XSS attack!";
			document.forms[0].reset();
			return false;
    }

    const maxLength = 150;
	const contentLength = 800;
    if (document.getElementById("prom_title").value.length > maxLength ||
		document.getElementById("prom_content").value.length > contentLength) {
			
			document.getElementById("prom_notification").innerHTML = "The input is too long and may cause a buffer overflow!";
			document.forms[0].reset();
			return false;
    }
	
	return true;
}

function validateAnnounFormEntry() {
	const sqlInjectionPattern = /(--|;|\/\*|\*\/|'|"|=|OR\s+\d+=\d+|DROP|SELECT|INSERT|DELETE|UPDATE)/i;
    if (sqlInjectionPattern.test(document.getElementById("ann_title").value) || 
		sqlInjectionPattern.test(document.getElementById("ann_content").value)) {
			
			document.getElementById("ann_notification").innerHTML = "The entry contains potential SQL Injection characters!";
		   	document.forms[1].reset();
			return false;
    }

    const xssPattern = /<script.*?>.*?<\/script>|<.*?>/;
    if (xssPattern.test(document.getElementById("ann_title").value) ||
		xssPattern.test(document.getElementById("ann_content").value)) {
			
			document.getElementById("ann_notification").innerHTML = "The entry contains a potential XSS attack!";
			document.forms[1].reset();
			return false;
    }

    const maxLength = 150;
	const contentLength = 800;
    if (document.getElementById("ann_title").value.length > maxLength ||
		document.getElementById("ann_content").value.length > contentLength) {
			
			document.getElementById("ann_notification").innerHTML = "The input is too long and may cause a buffer overflow!";
			document.forms[1].reset();
			return false;
    }
	
	return true;
}