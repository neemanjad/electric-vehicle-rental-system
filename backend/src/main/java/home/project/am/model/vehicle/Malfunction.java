package home.project.am.model.vehicle;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "MALFUNCTION")
public class Malfunction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMalfunction", nullable = false)
    private int idMalfunction;

    @Column(name = "description", length = 80)
    private String description;

    @Column(name = "repairCosts")
    private Double repairCosts;

    @OneToMany(mappedBy = "malfunction", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MalfunctionRecord> malfunctionRecords;

    public int getIdMalfunction() {
        return idMalfunction;
    }

    public void setIdMalfunction(int idMalfunction) {
        this.idMalfunction = idMalfunction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getRepairCosts() {
        return repairCosts;
    }

    public void setRepairCosts(Double repairCosts) {
        this.repairCosts = repairCosts;
    }

    public List<MalfunctionRecord> getMalfunctionRecords() {
        return malfunctionRecords;
    }

    public void setMalfunctionRecords(List<MalfunctionRecord> malfunctionRecords) {
        this.malfunctionRecords = malfunctionRecords;
    }
}
