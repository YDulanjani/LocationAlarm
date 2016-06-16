package yamuna.com.locationalarm;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.*;
import android.location.Location;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;


public class AlarmReceiver extends WakefulBroadcastReceiver {



    private final long timerDelay = 30 * 1000;

    private Intent intent;
    private Timer timer;
    private LocationManager locationManager;
    private android.location.Location location =null;
    private Context context;
    private boolean gpsEnabled;
    private boolean networkEnabled;

    ArrayList<Alarm> alarmList=new ArrayList<Alarm>();
    ArrayList<Reminder> reminderList=new ArrayList<Reminder>();


    public Location getLocation() {
        return location;
    }

    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location l) {
            timer.cancel();
            location = l;
            Log.d("Test :","location changed null start");
            DistanceData origin = new DistanceData(location.getLatitude(), location.getLongitude());
            List<DistanceData> destinations = new LinkedList<DistanceData>();

            DistanceData destination1 = new DistanceData(6.72022, 79.93046);
            destinations.add(destination1);

            DistanceData destination2 = new DistanceData(6.0535, 80.2209);
            destinations.add(destination2);

            DistanceManager distanceManager = new DistanceManager(context);
            distanceManager.getDistances(origin, destinations);
            Log.d("Test :","location changed  end");

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
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Access not SAve Location", Toast.LENGTH_LONG).show();
                return;
            }

            Toast.makeText(context, "reached save location", Toast.LENGTH_LONG).show();
            locationManager.removeUpdates(locationListenerGps);
            locationManager.removeUpdates(locationListenerNetwork);

            Location netLocation = null, gpsLocation = null;
            if (gpsEnabled)
                gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (networkEnabled)
                netLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (gpsLocation != null && netLocation != null) {
                if (gpsLocation.getAccuracy() > netLocation.getAccuracy()) {

                }else {
                    location = netLocation;
                }
            } else {
                if (gpsLocation != null) {
                    location = gpsLocation;
                    location = gpsLocation;
                    Log.d("Test :","gps ! null start");
                    DistanceData origin = new DistanceData(location.getLatitude(), location.getLongitude());
                    List<DistanceData> destinations = new LinkedList<DistanceData>();

                    DistanceData destination1 = new DistanceData(6.72022, 79.93046);
                    destinations.add(destination1);

                    DistanceData destination2 = new DistanceData(6.0535, 80.2209);
                    destinations.add(destination2);

                    DistanceManager distanceManager = new DistanceManager(context);
                    distanceManager.getDistances(origin, destinations);
                    Log.d("Test :","gps ! null end");
                } else if (netLocation != null) {
                    location = netLocation;
                }
            }

            Toast.makeText(context, location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_LONG).show();



            android.location.Location l=getLocation();
            if(l != null){
                ArrayList<Alarm> trigAlarms=new ArrayList<Alarm>();
                ArrayList<Reminder> trigReminders=new ArrayList<Reminder>();

                double curntLati=l.getLatitude();
                double curntLong=l.getLatitude();

                DBHandler dbHandler=new DBHandler(context);
                alarmList=dbHandler.getAllAlarmLocations();
                reminderList=dbHandler.getAllReminders();
/*
                String dataUrl = "http://http://maps.googleapis.com/maps/api/distancematrix/json";
                String dataUrlParameters = "origins=6.7966%2C79.9006&destinations=6.72022%2C79.93046%7C6.0535%2C80.2209";
                URL url;
                HttpURLConnection connection = null;
                try {
// Create connection
                    url = new URL(dataUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                    connection.setRequestProperty("Content-Length","" + Integer.toString(dataUrlParameters.getBytes().length));
                    connection.setRequestProperty("Content-Language", "en-US");
                    connection.setUseCaches(false);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
// Send request
                    DataOutputStream wr = new DataOutputStream(
                            connection.getOutputStream());
                    wr.writeBytes(dataUrlParameters);
                    wr.flush();
                    wr.close();
// Get Response
                    InputStream is = connection.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                    String line;
                    StringBuffer response = new StringBuffer();
                    while ((line = rd.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                    }
                    rd.close();
                    String responseStr = response.toString();
                    Log.d("Server response",responseStr);
                } catch (Exception e) {

                    Log.d("Server response",e.toString());

                } finally {

                    if (connection != null) {
                        connection.disconnect();
                    }
                }*/


                DistanceData origin = new DistanceData(curntLati, curntLong);
                List<DistanceData> destinations = new LinkedList<DistanceData>();
               if(!alarmList.isEmpty()){
                    for (Alarm alarm :alarmList ) {
                        destinations.add(new DistanceData(alarm.getLatitude(),alarm.getLongitude()));
                    }
                }




                Log.d("Test ","sIZE OF ALARM lIST "+alarmList.size()+"Size AlarmDistance "+DistanceKeeper.AlarmDistance.size());
                if(!alarmList.isEmpty() && !DistanceKeeper.AlarmDistance.isEmpty()){
                    Log.d("Test ","Com to if");
                    for (Alarm alarm :alarmList ) {
                        Integer distance=DistanceKeeper.AlarmDistance.get(alarm.getLocationNmae());
                        Toast.makeText(context,"dis :"+alarm.getLocationNmae()+" :", Toast.LENGTH_LONG).show();
                        Log.d("Test ",alarm.getLatitude() +"Long :"+alarm.getLongitude());
                        if(distance!=null && distance.intValue() <50000 && alarm.isMode()){
                            trigAlarms.add(alarm);
                        }
                    }
                }

                if(!reminderList.isEmpty() && !DistanceKeeper.ReminderDistance.isEmpty() ){
                    for (Reminder alarm :reminderList ) {
                        double distance=DistanceCalculator.distance(curntLati,curntLong,alarm.getLatitude(),alarm.getLongitude());
                        Toast.makeText(context,"dis :"+alarm.getLocationName()+" :"+distance, Toast.LENGTH_LONG).show();
                        if(distance<500 && alarm.isMode()){
                            trigReminders.add(alarm);
                        }
                    }
                }


                if(!trigAlarms.isEmpty() | (!trigReminders.isEmpty()) ){

                    //this will sound the alarm tone
                    //this will sound the alarm once

                    Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                    if (alarmUri == null) {
                        alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    }
                    if(trigAlarms.isEmpty()){
                        alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    }
                    Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
                    ringtone.play();

                    ComponentName comp = new ComponentName(context.getPackageName(),
                            AlarmService.class.getName());
                    startWakefulService(context, (intent.setComponent(comp)));
                    setResultCode(Activity.RESULT_OK);



                }
            }
        }
    }


    @Override
    public void onReceive(final Context context, Intent intent) {
        this.context=context;
        gpsEnabled = false;
        networkEnabled = false;
        this.intent=intent;
        saveLocation();

      /*
        MediaPlayer mp=new MediaPlayer();
        mp.setLooping(true);
*/


    }



    //Class which will calculate and give distance betweek current and others
     class DistanceManager {
        FetchDistanceTask mFetchDistanceTask;
        Context mContext;

        public DistanceManager(Context context) {
            mContext = context;
        }

        public void getDistances(DistanceData origin, List<DistanceData> destinations) {
            mFetchDistanceTask = new FetchDistanceTask(origin, destinations);
            mFetchDistanceTask.execute((Void) null);
        }

        public void runTasks(List<Integer> distances) {
            // TODO : Implement code for processing the locations

            for (int i = 0 ; i < distances.size() ; i++) {
                Log.d("Test", String.valueOf(distances.get(i)));
                try {
                    if (!alarmList.isEmpty()) {

                        DistanceKeeper.AlarmDistance.put(alarmList.get(i).getLocationNmae(), distances.get(i));
                        Log.d("Test :", "Put :" + alarmList.get(i).getLocationNmae() + " and" + String.valueOf(distances.get(i)));
                    }

                }catch(IndexOutOfBoundsException e){

                }

            }
        }

        public class FetchDistanceTask extends AsyncTask<Void, Void, Boolean> {
            private List<Integer> mResponse;
            private DistanceData mOrigin;
            private List<DistanceData> mDestinations;

            FetchDistanceTask(DistanceData origin, List<DistanceData> destinations) {
                mOrigin = origin;
                mDestinations = destinations;
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                String parameters = "origins=" + mOrigin.getLatitude() + "," + mOrigin.getLongitude();
                if (mDestinations.size() > 0) {
                    parameters += "&destinations=";
                }
                for (int i = 0 ; i < mDestinations.size() ; i++) {
                    parameters += mDestinations.get(i).getLatitude() + "," + mDestinations.get(i).getLongitude();
                    if (i < mDestinations.size() - 1) {
                        parameters += "|";
                    }
                }

                String response = NetworkAccess.connect("http://maps.googleapis.com/maps/api/distancematrix/json", parameters);

                Log.d("Test Response ",response);
                try {
                    mResponse = new LinkedList<>();
                    JSONArray elements = new JSONObject(response).getJSONArray("rows").getJSONObject(0).getJSONArray ("elements");
                    for (int i = 0 ; i < elements.length() ; i++) {
                        Integer distance = Integer.parseInt(elements.getJSONObject(i).getJSONObject("distance").get("value").toString());
                        mResponse.add(distance);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(mResponse == null) {
                    return false;
                }
                // set response
                return true;
            }

            @Override
            protected void onPostExecute(final Boolean success) {
                mFetchDistanceTask = null;

                if (success) {
                    if (mResponse.size() > 0 ) {
                        runTasks(mResponse);
                    } else {
                        Log.d("Debug", "Error");
                        return;
                    }
                }

            }

            @Override
            protected void onCancelled() {
                mFetchDistanceTask = null;
            }
        }
    }


    public class DistanceData {
        private double latitude;
        public double longitude;

        public DistanceData() {}

        public DistanceData(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }



    //Class for give Network access in AlarmReciver (For get Google Matrix)

    static class NetworkAccess {
        public static String connect(String uri, String parameters) {
            String responseStr = null;
            URL url;
            HttpURLConnection connection = null;
            try {
                // Create connection
                url = new URL(uri + "?" + parameters);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length","" + Integer.toString(parameters.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                // Send request
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(parameters);
                wr.flush();
                wr.close();
                // Get Response
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                responseStr = response.toString();
                Log.d("Server response",responseStr);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return responseStr;
        }
    }


}

