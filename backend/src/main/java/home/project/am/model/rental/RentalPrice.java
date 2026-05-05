package home.project.am.model.rental;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Entity
@Table(name = "RENTAL_PRICE")
public class RentalPrice {

    @Id
    @Column(name = "type", nullable = false, unique = true, length = 8)
    private String type;

    @Column(name = "price")
    private Double price;

    public RentalPrice() {}

    public RentalPrice(String type, Double price) {
        this.type = type;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
