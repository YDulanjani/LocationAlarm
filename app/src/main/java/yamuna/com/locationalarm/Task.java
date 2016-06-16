package yamuna.com.locationalarm;

/**
 * Created by Yamuna on 6/13/2016.
 */
public class Task {


    private String locationName;
    private String description;
    private boolean status;


    public Task(String locationName, boolean status, String description) {
        this.locationName = locationName;
        this.status = status;
        this.description = description;
    }

    public Task() {
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
