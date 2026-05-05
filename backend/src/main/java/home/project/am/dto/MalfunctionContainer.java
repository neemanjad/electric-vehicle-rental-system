package home.project.am.dto;

public class MalfunctionContainer {
    private int idMalfunction;
    private String description;
    private Double repairCosts;
    private String malfunctionDate;
    private String repairDate;
    private String vehicleId;

    public MalfunctionContainer(int idMalfunction, String description, Double repairCosts, String malfunctionDate, String repairDate) {
        this.idMalfunction = idMalfunction;
        this.description = description;
        this.repairCosts = repairCosts;
        this.malfunctionDate = malfunctionDate;
        this.repairDate = repairDate;
    }

    // Getteri i setteri
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

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}
}
