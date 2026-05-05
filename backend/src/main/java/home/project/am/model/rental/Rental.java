package home.project.am.model.rental;

import home.project.am.model.user.Client;
import home.project.am.model.vehicle.Vehicle;
import jakarta.persistence.*;

@Entity
@Table(name = "RENTAL")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRental", nullable = false)
    private int idRental;

    @Column(name = "dateTime", nullable = false)
    private String dateTime;

    @Column(name = "startX", nullable = false)
    private int startX;

    @Column(name = "startY", nullable = false)
    private int startY;

    @Column(name = "endX", nullable = false)
    private int endX;

    @Column(name = "endY", nullable = false)
    private int endY;

    @Column(name = "price", nullable = false)
    private double price;

    @ManyToOne
    @JoinColumn(name = "VEHICLE_ID", referencedColumnName = "ID", nullable = false)
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "CLIENT_USER_userName", referencedColumnName = "USER_userName", nullable = false)
    private Client client;

    @Column(name = "licenceNumber", nullable = true)
    private String licenceNumber;

    @Column(name = "documentNumber", nullable = true)
    private String documentNumber;

    @Column(name = "seconds", nullable = true)
    private Integer seconds;

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

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }
}
