package home.project.am.model.vehicle;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MalfunctionRecordId implements Serializable {
	private static final long serialVersionUID = 1L;

	private int malfunctionId;
    private String vehicleId;

    public int getMalfunctionId() {
        return malfunctionId;
    }

    public void setMalfunctionId(int malfunctionId) {
        this.malfunctionId = malfunctionId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MalfunctionRecordId that = (MalfunctionRecordId) o;
        return malfunctionId == that.malfunctionId && Objects.equals(vehicleId, that.vehicleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(malfunctionId, vehicleId);
    }
}
