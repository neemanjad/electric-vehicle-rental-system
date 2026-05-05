package home.project.am.model.vehicle;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "MANUFACTURER")
public class Manufacturer implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "name", length = 40, nullable = false)
    private String name;

    @Column(name = "country", length = 20)
    private String country;

    @Column(name = "address", length = 45)
    private String address;

    @Column(name = "telephone", length = 20)
    private String telephone;

    @Column(name = "fax", length = 20)
    private String fax;

    @Column(name = "email", length = 25)
    private String email;

    // Constructors
    public Manufacturer() {}

    public Manufacturer(String name, String country, String address, String telephone, String fax, String email) {
        this.name = name;
        this.country = country;
        this.address = address;
        this.telephone = telephone;
        this.fax = fax;
        this.email = email;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}