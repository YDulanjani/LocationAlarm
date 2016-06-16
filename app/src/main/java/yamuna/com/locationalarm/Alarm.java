package yamuna.com.locationalarm;

/**
 * Created by Yamuna on 5/23/2016.
 */
public class Alarm {



    private String locationNmae;
    private double longitude;
    private double latitude;
    private  boolean mode;

    public Alarm(String locationNmae) {
        this.locationNmae = locationNmae;
    }

    public Alarm(String locationNmae,double latitude,  double longitude, boolean mode) {
        this.latitude = latitude;
        this.locationNmae = locationNmae;
        this.longitude = longitude;
        this.mode = mode;
    }

    public Alarm() {
    }

    public String getLocationNmae() {
        return locationNmae;
    }

    public void setLocationNmae(String locationNmae) {
        this.locationNmae = locationNmae;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isMode() {
        return mode;
    }

    public void setMode(boolean mode) {
        this.mode = mode;
    }
}
