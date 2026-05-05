package home.project.am.model.vehicle;

import jakarta.persistence.*;

@Entity
@Table(name = "MALFUNCTION_RECORD")
public class MalfunctionRecord {

    @EmbeddedId
    private MalfunctionRecordId id;

    @ManyToOne
    @MapsId("vehicleId") 
    @JoinColumn(name = "VEHICLE_ID", referencedColumnName = "ID", nullable = false)
    private Vehicle vehicle;

    @ManyToOne
    @MapsId("malfunctionId") 
    @JoinColumn(name = "MALFUNCTION_idMalfunction", referencedColumnName = "idMalfunction", nullable = false)
    private Malfunction malfunction;

    @Column(name = "malfunctionDate", length = 45)
    private String malfunctionDate;

    @Column(name = "repairDate", length = 45)
    private String repairDate;

    public MalfunctionRecordId getId() {
        return id;
    }

    public void setId(MalfunctionRecordId id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Malfunction getMalfunction() {
        return malfunction;
    }

    public void setMalfunction(Malfunction malfunction) {
        this.malfunction = malfunction;
    }

    public String getMalfunctionDate() {
        return malfunctionDate;
    }

    public void setMalfunctionDate(String malfunctionDate) {
        this.malfunctionDate = malfunctionDate;
    }

    public String getRepairDate() {
        return repairDate;
    }

    public void setRepairDate(String repairDate) {
        this.repairDate = repairDate;
    }
}
