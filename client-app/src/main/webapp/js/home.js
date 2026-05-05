let interval = null;
let intervalMove = null;
let secondsPassed = 0;
let matrix = [];
let priceForType = 0;
let car_type = null;
let server_url = 'http://10.99.134.87:8000/ETFBL_IP_Clients/';
let vehicle_id = null;
let currentX = null; // Trenutna X koordinata vozila
let currentY = null; // Trenutna Y koordinata vozila
let currentDot = null;
let animationInterval;
let isSelectingStart = true;
let startCell = null;
let endCell = null;

const dropdown = document.getElementById('vehicle-dropdown');
const isSmallScreen = window.innerWidth <= 768; // Provera širine ekrana
const rows = isSmallScreen ? 15 : 15;
const cols = isSmallScreen ? 13 : 35; // Postavi matrixCols pre generisanja HTML-a
const maxCols = cols - 1;

document.addEventListener('change', function (event) {
    if (event.target && event.target.id === 'vehicle-dropdown') {
        const selectedOption = event.target.selectedOptions[0];
        const picture = selectedOption.getAttribute('data-picture');
        const id = selectedOption.value;
        vehicle_id = id;
		const model = selectedOption.textContent.trim();

        const modal = document.getElementById('vehicle-modal');
        const modalImg = document.getElementById('modal-img');
        const modalDetails = document.getElementById('modal-details');
        const closeModal = document.getElementById('close-modal');
        const dropdown = document.getElementById('vehicle-dropdown');
        const overlay = document.getElementById('modal-overlay'); // Overlay element

        if (picture) {
            modalImg.src = picture;
            modalDetails.textContent = `Vehicle ID: ${id}, ${model}`;
            modal.style.display = 'block'; // Prikaži modal
            overlay.style.display = 'block'; // Prikaži overlay
            dropdown.disabled = true; // Onemogući dropdown
        }

        closeModal.addEventListener('click', () => {
            modal.style.display = 'none'; // Zatvori modal
            overlay.style.display = 'none'; // Zatvori overlay
            dropdown.disabled = false; // Ponovo omogući dropdown
        });
    }
});

function getVehicleInfo(type, vehicle){
	let result = null;
	if(type == 'scooter'){
		result = `${vehicle.manufacturer} | ${vehicle.model} | ${vehicle.purchasePrice}` + '$' + ` | ${vehicle.maxSpeed}` + 'km/h' + ` | ${vehicle.ID}`;
	} else if(type == 'car'){
		result = `${vehicle.manufacturer} | ${vehicle.model} | ${vehicle.purchasePrice}` + '$' + ` | ${vehicle.description} | ${vehicle.purchaseDate} | ${vehicle.ID}`;
	} else if(type == 'bicycle'){
		result = `${vehicle.manufacturer} | ${vehicle.model} | ${vehicle.purchasePrice}` + '$' + ` | ${vehicle.range}` + 'm'+ ` | ${vehicle.ID}`;
	} else{
		result = 'ERROR!';
	}
	return result;
}

function handlePaymentMethodChange() {
    const paymentSelect = document.getElementById('payment-select');
    const paymentDetails = document.getElementById('payment-details');
    const selectedMethod = paymentSelect.value;

    // Resetiramo prethodni sadržaj
    paymentDetails.innerHTML = '';

    if (selectedMethod === 'card') {
    	paymentDetails.innerHTML = `
        	<label for="card-number">Enter Card Number:</label>
			<input type="text" id="card-number" 
			       placeholder="1234-5678-9123-4567" 
			       maxlength="16" 
			       pattern="\d{16}" 
			       title="Card number must be exactly 16 digits">
        `;
    } else if (selectedMethod === 'paypal') {
        paymentDetails.innerHTML = `
            <label for="paypal-email">Enter PayPal Email:</label>
            <input type="email" id="paypal-email" placeholder="example@paypal.com" required>
        `;
    }
}

function loadContent(type) {
    if (type !== 'profile') {

        const content = document.getElementById('content');
        content.style.justifyContent = "flex-start";
        content.style.alignItems = "center";
		
		let documentFieldsHTML = ''; // Prazan string za unos, koji se popunjava dinamički

		if (type === 'car') {
			car_type = true;
		    documentFieldsHTML = `
		        <div id="document-fields" class="document-fields">
		            <div class="field-group">
		                <label for="document-number">Document number:</label>
		                <input type="text" id="document-number" 
							   placeholder="Enter document number"
							   maxlength="9"
							   pattern="\d{9}"
							   title="Document number must be exactly 9 digits"
							   required>
		            </div>
		            <div class="field-group">
		                <label for="license-number">Driver's license number:</label>
						<input type="text" id="license-number" 
						       placeholder="Enter driver's license number" 
						       maxlength="9" 
						       pattern="\d{9}" 
						       title="License number must be exactly 9 digits" 
						       required>
		            </div>
		        </div>
		    `;
		}

        let url = `${server_url}?action=fetch&type=${type}`;

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                return response.json(); // Pretpostavljamo da API vraća JSON podatke
            })
            .then(data => {
				priceForType = data.price;
                // Kreiranje dropdownHTML nakon dobijanja podataka
                let dropdownHTML = `
				<div id="selection-container" class="selection-container">
                    <div class="vehicle-dropdown-container">
                        <select id="vehicle-dropdown" class="vehicle-dropdown">
                            <option value="" disabled selected>choose ` + type + `</option>`;
                data.vehicles.forEach(vehicle => {
                    dropdownHTML += `
                        <option 
                            value="${vehicle.ID}" 
                            data-picture="data:image/jpeg;base64,${vehicle.pictureAsStringBase64}">` +
                        	getVehicleInfo(type, vehicle) + `
                        </option>`;
                });

                dropdownHTML += `
                    </select>
                    <div id="modal-overlay" style="display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0, 0, 0, 0.5); z-index: 9998;"></div>
                    <div id="vehicle-modal" style="display: none; position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); background: #fff; border: 1px solid #ccc; padding: 20px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2); z-index: 9999;">
                        <img id="modal-img" src="" alt="Vehicle Preview" style="width: 300px; height: 200px; object-fit: cover;">
                        <p id="modal-details" style="margin-top: 10px; font-size: 16px; color: #333;"></p>
                        <button id="close-modal" style="margin-top: 10px; padding: 5px 10px; background: #ccc; border: none; cursor: pointer;">Close</button>
                    </div>
                </div>`;

                // Kombinovanje dropdownHTML sa ostatkom sadržaja
                const combinedHTML = dropdownHTML + `
				<div id="payment-method" class="payment-method">
					<select id="payment-select" class="payment-dropdown" onchange="handlePaymentMethodChange()">
						<option value="" disabled selected>choose payment method</option>
						<option value="card">Credit/Debit Card</option>
						<option value="paypal">PayPal</option>
					</select>
									    
				<div id="payment-details" class="payment-details"></div>
				</div>
				${documentFieldsHTML} <!-- Dinamički dodan HTML ako je tip "car" -->
                    <div id="matrix-container"></div>
                    <div class="controls">
                        <div class="coord-inputs">
                            <label>Start location</label>
                            <input type="number" id="startX" min="0" max="14">
                            <input type="number" id="startY" min="0" max="${maxCols}">

                            <label>End location</label>
                            <input type="number" id="endX" min="0" max="14">
                            <input type="number" id="endY" min="0" max="${maxCols}">

                            <button id="startButton" class="rent-button" onclick="startRenting()">START RENTING</button>
                            <button id="endButton" class="rent-button cancel" onclick="endRenting()">END RENTING</button>
                        </div>
                    </div>
                    
					<div id="status">
					    <div class="status-item">
					        <p><strong>TIME:</strong> <span id="time-display">0</span> s</p>
					    </div>
					    <div class="status-item">
					        <p><strong>PRICE:</strong> <span id="price-display">0</span> KM</p>
					    </div>
					</div>
					
					<div id="pdfDiv" style="display: none; text-align: center;">
					    <a id="pdfLink" href="" target="_blank" style="text-decoration: none; color: inherit;">
					        <img src="photos/pdf.png" alt="PDF Icon" style="width: 40px; height: auto;">
					        <p id="pdfTitle"></p>
					    </a>
					</div>
					
                    <button id="back-to-main" onclick="loadMainContent()">BACK</button>
                `;
                content.innerHTML = combinedHTML; // Postavljanje kompletnog HTML sadržaja u content

                createMatrix(); // Kreiranje matrice
            })
            .catch(error => {
                console.error("Došlo je do greške:", error);
            });
    }
}

function loadMainContent() {
    const content = document.getElementById('content');
	content.style.justifyContent = "center";
	content.style.alignItems = "center";

    content.innerHTML = `
        <div class="button-container">
            <button class="content-button" onclick="loadContent('scooter')">SCOOTER RENTAL</button>
            <button class="content-button" onclick="loadContent('bicycle')">BIKE RENTAL</button>
            <button class="content-button" onclick="loadContent('car')">CAR RENTAL</button>
			<form action="?action=profile" method="POST">
				<button class="content-button" type="submit">MY PROFILE</button>
			</form>
        </div>
    `;
}

function createMatrix() {
    const container = document.getElementById('matrix-container');
    container.innerHTML = '';
    matrix = [];
	
		for (let x = 0; x < rows; x++) {
	    const row = [];
	    for (let y = 0; y < cols; y++) {
	        const cell = document.createElement('div');
	        cell.classList.add('cell');
	        cell.dataset.x = x;
	        cell.dataset.y = y;

	        cell.addEventListener('click', () => handleCellClick(cell));

	        container.appendChild(cell);
	        row.push(cell);
	    }
	    matrix.push(row);
	}
    // Reset
    startCell = null;
    endCell = null;
    isSelectingStart = true;

    document.getElementById('startX').value = '';
    document.getElementById('startY').value = '';
    document.getElementById('endX').value = '';
    document.getElementById('endY').value = '';

    document.getElementById('startButton').disabled = false;
    document.getElementById('endButton').disabled = false;
}

function handleCellClick(cell) {
    const x = parseInt(cell.dataset.x);
    const y = parseInt(cell.dataset.y);

    if (isSelectingStart) {
        if (startCell) startCell.classList.remove('start');
        startCell = cell;
        startCell.classList.add('start');
        document.getElementById('startX').value = x;
        document.getElementById('startY').value = y;
        isSelectingStart = false;
    } else {
        if (endCell) endCell.classList.remove('end');
        endCell = cell;
        endCell.classList.add('end');
        document.getElementById('endX').value = x;
        document.getElementById('endY').value = y;
        isSelectingStart = true;
    }
}

function startRenting() {
	// Provjera odabira vozila
	const selectedVehicle = document.getElementById('vehicle-dropdown').value;
	if (!selectedVehicle) {
	    alert("Please select a vehicle.");
	    return;
	}

	// Provjera načina plaćanja
	const paymentMethod = document.getElementById('payment-select').value;
	if (!paymentMethod) {
	    alert("Please choose a payment method.");
	    return;
	}
	
	let isPaymentValid = false;

	// Validacija podataka za plaćanje 
	if (paymentMethod === 'card') {
		const cardNumber = document.getElementById('card-number')?.value;
		if (!cardNumber || cardNumber.length < 16 || cardNumber.length > 19) {
			alert("Please enter a valid card number.");
	        return;
		}
	        
		isPaymentValid = true;
	    } else if (paymentMethod === 'paypal') {
			const paypalEmail = document.getElementById('paypal-email')?.value;
	        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // Validacija email-a
	        if (!paypalEmail || !emailPattern.test(paypalEmail)) {
	            alert("Please enter a valid PayPal email address.");
	            return;
	        }
	        isPaymentValid = true;
	    }

	    if (!isPaymentValid) {
	        alert("Invalid payment details. Please check and try again.");
	        return;
	    }
	
		// Provjera dodatnih podataka za vozila (broj dokumenta i vozačka dozvola)
	    if (car_type) {
	        const documentNumber = document.getElementById('document-number')?.value;
	        const licenseNumber = document.getElementById('license-number')?.value;

	        if (!documentNumber || documentNumber.trim() === "" || !licenseNumber || licenseNumber.trim() === "") {
	            alert("Please enter both document number and driver's license number.");
	            return;
	        }
	    }
		
		const x1 = parseInt(document.getElementById('startX').value);
		const y1 = parseInt(document.getElementById('startY').value);
		const x2 = parseInt(document.getElementById('endX').value);
		const y2 = parseInt(document.getElementById('endY').value);

		if (isNaN(x1) || isNaN(y1) || isNaN(x2) || isNaN(y2)) {
			alert("Please enter valid coordinates.");
		    return;
		}
			
		if(x1 < 0 || x1 >= rows || y1 < 0 || y1 > maxCols){
			alert("Please enter valid start coordinates.");
		    return;
		}
			
		if(x2 < 0 || x2 >= rows || y2 < 0 || y2 > maxCols){
			alert("Please enter valid end coordinates.");
			return;
		}
	
		fetch(server_url + '?action=rent', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({veh : vehicle_id})
		})
		.then(response => {
			if(response.ok){
				// Reset timer i status
				secondsPassed = 0;
				updateTimeDisplay();
				updatePriceDisplay();

				// Onemogući START, omogući END
				document.getElementById('startButton').disabled = true;
				document.getElementById('endButton').disabled = false;

				// Start timer
				interval = setInterval(() => {
			    	secondsPassed++;
			   	 	updateTimeDisplay();
			    	updatePriceDisplay();
				}, 1000);

				// Pokreni simulaciju kretanja
				simulateMovement(x1, y1, x2, y2);
			} else{
				alert('Vehicle bussy!');
			}
		})
		.catch(error => console.error('Error: ', error));	
}

function simulateMovement(x1, y1, x2, y2) {
    let path = [];

    for (let x = x1; x !== x2; x += x < x2 ? 1 : -1) path.push([x, y1]);
    for (let y = y1; y !== y2; y += y < y2 ? 1 : -1) path.push([x2, y]);
    path.push([x2, y2]);

    let index = 0;

    intervalMove = 	setInterval(() => {
	    if (index > 0) {
	        const [prevX, prevY] = path[index - 1];
	        matrix[prevX][prevY].classList.remove('active');
	    }

	    if (index < path.length) {
	        const [x, y] = path[index];
	        matrix[x][y].classList.add('active');
			
			// Ažuriraj trenutnu poziciju vozila
			currentX = x;
			currentY = y;
			
	        index++;
	    } else {
	       endRenting();
	    }
	}, 300);
}

function endRenting() {
    clearInterval(interval);       // zaustavi mjerenje vremena
    clearInterval(intervalMove);   // zaustavi animaciju kretanja
    document.getElementById('endButton').disabled = true; // onemogući END

	// Podaci koje želite poslati na server
	const rental = {
		idRental: null,
	    dateTime: null,
	    startX: parseInt(document.getElementById('startX').value),
	    startY: parseInt(document.getElementById('startY').value),
	    endX: currentX,
		endY: currentY,
		price: parseFloat(parseFloat(document.getElementById('price-display').textContent).toFixed(2)),
		vehicleID: vehicle_id,
		userName: null,
		licenceNumber: document.getElementById('license-number')?.value,
		documentNumber: document.getElementById('document-number')?.value,
		seconds: document.getElementById('time-display').innerText
	};

	sendRentalData(rental);	
    document.getElementById('startButton').disabled = false; // omogući ponovo START
}

async function sendRentalData(rental) {
    try {
        const response = await fetch(server_url + '?action=save', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'include',
            body: JSON.stringify(rental)
        });

        if (!response.ok) {
            throw new Error(`Server error: ${response.status}`);
        }

        const contentDisposition = response.headers.get("Content-Disposition");
        let filename = "invoice.pdf";
        const match = /filename="(.+)"/.exec(contentDisposition);
        if (match && match[1]) {
            filename = match[1];
        }

        const blob = await response.blob();
        const fileURL = URL.createObjectURL(blob);

        const pdfLink = document.getElementById("pdfLink");
        const pdfTitle = document.getElementById("pdfTitle");

        pdfLink.href = fileURL;
        pdfTitle.textContent = filename;
        document.getElementById("pdfDiv").style.display = "block";

    } catch (error) {
        console.error('Došlo je do greške pri slanju podataka:', error);
    }
}

function updateTimeDisplay() {
    document.getElementById('time-display').textContent = secondsPassed;
}

function updatePriceDisplay() {
    constprice = secondsPassed * priceForType;
    document.getElementById('price-display').textContent = constprice;
}

function fetchAnnouncements() {
    let url = server_url + '?action=announcements'; // Endpoint sa servera
    fetch(url)
        .then(response => response.json())
        .then(data => {
            const announcements = document.getElementById('announcements');
            announcements.innerHTML = ''; // Očisti trenutni sadržaj
            
            data.reverse().forEach(pb => {
                const cardHTML = `
                    <div class="col-12 mb-4">
                        <div class="card">
                            <div class="card-body">
                                <h5 class="card-title text-primary">${pb.title}</h5>
                                <p class="card-text">${pb.content}</p>
                            </div>
                        </div>
                    </div>
                `;
                announcements.innerHTML += cardHTML;
            });
        })
        .catch(error => console.error('Error fetching promotions:', error));
}

function fetchPromotions() {
    let url = server_url + '?action=promotions'; // Endpoint sa servera
    fetch(url)
        .then(response => response.json())
        .then(data => {
            const promotions = document.getElementById('promotions');
            promotions.innerHTML = ''; // Očisti trenutni sadržaj
            
            data.reverse().forEach(pb => {
                const cardHTML = `
                    <div class="col-12 mb-4">
                        <div class="card">
                            <div class="card-body">
                                <h5 class="card-title text-primary">${pb.title}</h5>
								<p class="card-text">${pb.description}</p>
								<p class="text-muted"><small>Expires on: ${pb.expirationDate}</small></p>
                            </div>
                        </div>
                    </div>
                `;
                promotions.innerHTML += cardHTML;
            });
        })
        .catch(error => console.error('Error fetching promotions:', error));
}

function refreshData() {
    fetchAnnouncements();
    fetchPromotions();
}

setInterval(refreshData, 5000);