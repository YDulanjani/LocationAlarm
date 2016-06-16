package yamuna.com.locationalarm;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.text.format.DateFormat;
import android.widget.Toast;

import java.util.Date;
import java.util.Timer;


public class LocationDetector {

    private final long timerDelay = 30 * 1000;

    private Timer timer;
    private LocationManager locationManager;
    private Location location =null;
    private Context context;
    private boolean gpsEnabled;
    private boolean networkEnabled;

    public Location getLocation() {
        return location;
    }

    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location l) {
            timer.cancel();
            location = l;
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(this);
        }

        public void onProviderDisabled(String provider) { }

        public void onProviderEnabled(String provider) { }

        public void onStatusChanged(String provider, int status, Bundle extras) { }
    };

    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location l) {
            timer.cancel();
            location = l;
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(this);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    public LocationDetector(Context context) {
        this.context = context;
        gpsEnabled = false;
        networkEnabled = false;
    }

    public boolean saveLocation() {
        if (locationManager == null)
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        try {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            Toast.makeText(context,ex.toString(), Toast.LENGTH_LONG).show();
        }
        try {
            networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            Toast.makeText(context,ex.toString(), Toast.LENGTH_LONG).show();
        }

        if (!gpsEnabled && !networkEnabled) {
            Toast.makeText(context,"Not enabled any", Toast.LENGTH_LONG).show();
            return false;
        }
        if (gpsEnabled) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context,"No permission gps", Toast.LENGTH_LONG).show();
                return false;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
            Toast.makeText(context," gps", Toast.LENGTH_LONG).show();
        }
        if (networkEnabled) {
            Toast.makeText(context,"Network enabled gps", Toast.LENGTH_LONG).show();

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);

        }
        timer = new Timer();
        SaveLastLocation handler = new SaveLastLocation();
        handler.sendEmptyMessageDelayed(0, timerDelay);
        return true;
    }

    class SaveLastLocation extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context    , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context,"Access not SAve Location", Toast.LENGTH_LONG).show();
                return;
            }

            Toast.makeText(context,"reached save location", Toast.LENGTH_LONG).show();
            locationManager.removeUpdates(locationListenerGps);
            locationManager.removeUpdates(locationListenerNetwork);

            Location netLocation = null, gpsLocation = null;
            if(gpsEnabled)
                gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(networkEnabled)
                netLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(gpsLocation != null && netLocation != null) {
                if(gpsLocation.getAccuracy() > netLocation.getAccuracy())
                    location = gpsLocation;
                else
                    location = netLocation;
            } else {
                if(gpsLocation != null) {
                    location = gpsLocation;
                } else if (netLocation != null){
                    location = netLocation;
                }
            }

            Toast.makeText(context, location.getLatitude() + " " +  location.getLongitude(), Toast.LENGTH_LONG).show();


        }
    }
}
