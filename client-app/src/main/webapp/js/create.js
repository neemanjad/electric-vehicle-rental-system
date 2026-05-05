function validateFormEntries() {
    const fieldsToValidate = [
        { id: "firstName", name: "First Name" },
        { id: "lastName", name: "Last Name" },
        { id: "userName", name: "Username" },
        { id: "password", name: "Password" },
        { id: "idCard", name: "ID Card Number" },
        { id: "email", name: "Email" },
        { id: "phoneNumber", name: "Phone Number" }
    ];

    for (let field of fieldsToValidate) {
        const fieldElement = document.getElementById(field.id);

        // Provjera da li postoji polje
        if (!fieldElement) {
            console.error(`Field with ID ${field.id} not found!`);
            continue;
        }

        // Validacija unosa koristeći validateEntry
        if (!validateEntry(fieldElement.value)) {
            const notification = document.getElementById("notification");
            notification.style.display = "block";
            notification.innerHTML = `Invalid input in field: ${field.name}`;
            return false; // Ako je bilo koja validacija neuspješna, prekida se proces
        }
    }
    return true;
}

function validateEntry(entry){
	const sqlInjectionPattern = /('|--|;|\/\*|\*\/)/;
	if (sqlInjectionPattern.test(entry))
		return false;

	const xssPattern = /<script.*?>.*?<\/script>|<.*?>/;
	if (xssPattern.test(entry))
		return false;	

	const maxLength = 50;
	if (entry.length > maxLength)
		return false;
	return true;
}