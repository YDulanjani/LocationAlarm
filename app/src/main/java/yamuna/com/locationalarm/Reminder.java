package yamuna.com.locationalarm;

import java.util.ArrayList;

/**
 * Created by Yamuna on 6/13/2016.
 */
public class Reminder {

    private String locationName;
    private double longitude;
    private double latitude;
    private  boolean mode;
    private ArrayList<Task> taskList;


    public Reminder(String locationName, double longitude, double latitude, boolean mode) {
        this.locationName = locationName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.mode = mode;
    }

    public Reminder(String locationName, ArrayList<Task> taskList, boolean mode, double latitude, double longitude) {
        this.locationName = locationName;
        this.taskList = taskList;
        this.mode = mode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Reminder() {
    }


    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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

    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }
}
