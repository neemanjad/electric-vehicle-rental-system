package home.project.model;

import java.util.Objects;

public class Rental {
	private int idRental;
	private String dateTime;
	private int startX, startY, endX, endY;
	private double price;
	private String vehicleID, userName, licenceNumber, documentNumber;
	private int seconds;

	public Rental() {
		super();
	}

	public Rental(int idRental, String dateTime, int startX, int startY, int endX, int endY, double price,
			String vehicleID, String userName, String licenceNumber, String documentNumber, int seconds) {
		super();
		this.idRental = idRental;
		this.dateTime = dateTime;
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.price = price;
		this.vehicleID = vehicleID;
		this.userName = userName;
		this.licenceNumber = licenceNumber;
		this.documentNumber = documentNumber;
		this.seconds = seconds;
	}

	public int getIdRental() {
		return idRental;
	}
	
	public void setIdRental(int idRental) {
		this.idRental = idRental;
	}
	
	public String getDateTime() {
		return dateTime;
	}
	
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	public int getStartX() {
		return startX;
	}
	
	public void setStartX(int startX) {
		this.startX = startX;
	}
	
	public int getStartY() {
		return startY;
	}
	
	public void setStartY(int startY) {
		this.startY = startY;
	}
	
	public int getEndX() {
		return endX;
	}
	
	public void setEndX(int endX) {
		this.endX = endX;
	}
	
	public int getEndY() {
		return endY;
	}
	
	public void setEndY(int endY) {
		this.endY = endY;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public String getVehicleID() {
		return vehicleID;
	}
	
	public void setVehicleID(String vehicleID) {
		this.vehicleID = vehicleID;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getLicenceNumber() {
		return licenceNumber;
	}
	
	public void setLicenceNumber(String licenceNumber) {
		this.licenceNumber = licenceNumber;
	}
	
	public String getDocumentNumber() {
		return documentNumber;
	}
	
	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(idRental);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rental other = (Rental) obj;
		return idRental == other.idRental;
	}

	@Override
	public String toString() {
		return "Rental [idRental=" + idRental + ", dateTime=" + dateTime + ", startX=" + startX + ", startY=" + startY
				+ ", endX=" + endX + ", endY=" + endY + ", price=" + price + ", vehicleID=" + vehicleID + ", userName="
				+ userName + ", licenceNumber=" + licenceNumber + ", documentNumber=" + documentNumber + "]";
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
}