package yamuna.com.locationalarm;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Yamuna on 6/12/2016.
 */
public class LocationSearcher extends AsyncTask< Object ,Void,String >{
    private Context context;
    String result="";

    @Override
    protected String doInBackground(Object... params) {

        context=(Context) params[1];
        String inputLine = "";
       String location=(String)params[0];
        location=location.replaceAll(" ", "%20");
        String myUrl="http://maps.google.com/maps/geo?q="+location+"&output=csv";

        URL url= null;
        try {
            url = new URL(myUrl);

        URLConnection urlConnection=url.openConnection();
        BufferedReader in = new BufferedReader(new
                InputStreamReader(urlConnection.getInputStream()));
        while ((inputLine = in.readLine()) != null) {
            result=inputLine;
        }
        String lat = result.substring(6, result.lastIndexOf(","));
        String longi = result.substring(result.lastIndexOf(",") + 1);
            return lat+"Long :"+longi+ "Rresult"+result ;
        } catch (Exception e) {
            return e.getMessage();
        }


    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Toast.makeText(context,result, Toast.LENGTH_LONG).show();

    }
}
