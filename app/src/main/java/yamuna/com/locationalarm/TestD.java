package yamuna.com.locationalarm;

import android.content.Context;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Yamuna on 6/15/2016.
 */
public class TestD {
    Context context;
    TestD(Context c){
        context=c;
        Location origin = new Location(6.7966, 79.9006);

        List<Location> destinations = new LinkedList<>();
        Location destination1 = new Location(6.72022, 79.93046);
        destinations.add(destination1);

        Location destination2 = new Location(6.0535,80.2209);
        destinations.add(destination2);

        DistanceManager d=new DistanceManager(context);
        d.getDistances(origin,destinations);
    }


     class Location {
        private double latitude;
        public double longitude;

        public Location() {}

        public Location(double latitude, double longitude) {
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



     static class  NetworkAccess {
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
                Log.d("Test Server response",responseStr);
            } catch (Exception e) {
                Log.d("Test ERROR ",e.toString());
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return responseStr;
        }
    }


    class DistanceManager {
        FetchDistanceTask mFetchDistanceTask;
        Context mContext;

        public DistanceManager(Context context) {
            mContext = context;
        }

        public void getDistances(Location origin, List<Location> destinations) {
            mFetchDistanceTask = new FetchDistanceTask(origin, destinations);
            mFetchDistanceTask.execute((Void) null);
        }

        public void runTasks(List<Integer> distances) {
            // TODO : Implement code for processing the locations
            for (int i = 0 ; i < distances.size() ; i++) {
                Log.d("Test", String.valueOf(distances.get(i)));
            }
        }

        public class FetchDistanceTask extends AsyncTask<Void, Void, Boolean> {
            private List<Integer> mResponse;
            private Location mOrigin;
            private List<Location> mDestinations;

            FetchDistanceTask(Location origin, List<Location> destinations) {
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
                try {
                    mResponse = new LinkedList<>();
                    JSONArray elements = new JSONObject(response).getJSONArray("rows").getJSONObject(0).getJSONArray ("elements");
                    for (int i = 0 ; i < elements.length() ; i++) {
                        Integer distance = Integer.parseInt(elements.getJSONObject(i).getJSONObject("distance").get("value").toString());
                        mResponse.add(distance);
                    }
                } catch (JSONException e) {
                    Log.d("Test ERROR JSON ",e.toString());
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
                        Log.d("Test", "Error");
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
}
