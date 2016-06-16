package yamuna.com.locationalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Yamuna on 6/14/2016.
 */
public class AlarmFragment extends Fragment implements View.OnClickListener {
    TextView locationName;
    Button upButton;
    // ArrayAdapter<Switch> adapter;
    ArrayList<Switch> listItems=new ArrayList<Switch>();

    private ListView listView1;
    LocationAdapter adapter;
    Location ls[];



    @Override
    public void onClick(View v) {


        //do what you want to do when button is clicked
        switch (v.getId()) {
            case R.id.btnAlarmAdd:
                Toast.makeText(this.getContext(),"cometo this :"+v.getId()+"Show :"+R.id.btnShowMap, Toast.LENGTH_LONG).show();

                //Add alarm to the db
                try {

                    DBHandler dbHandler = new DBHandler(this.getContext());
                    String location = locationName.getText().toString();

                    if(!location.isEmpty()){
                        Geocoder coder=new Geocoder(this.getContext());
                        Address s=coder.getFromLocationName(location,1).get(0);
                        double lati=s.getLatitude();
                        double longi=s.getLongitude();

                        dbHandler.insertAlarm(new Alarm(location,lati,longi,true));
                        ArrayList<Alarm> locationList = dbHandler.getAllAlarmLocations();

                        String txt = "";
                        int i = 0;

                        ls =new Location[locationList.size()];
                        while(i<locationList.size()){
                            ls[i]=new Location(locationList.get(i).getLocationNmae(),"",locationList.get(i).isMode());
                            txt += locationList.get(i) + "/";
                            i++;
                        }

                        adapter = new LocationAdapter(this.getActivity(),
                                R.layout.listview_item_row, ls);
                        listView1 = (ListView) this.getActivity().findViewById(R.id.listView1);
                        listView1.setAdapter(adapter);

                        MainActivity a=(MainActivity) this.getActivity();

                        if(!a.alarmFlag){
                            Intent intent = new Intent(this.getActivity(), AlarmReceiver.class);
                            PendingIntent pintent = PendingIntent.getBroadcast(this.getActivity(), 0, intent, 0);
                            AlarmManager alarm = (AlarmManager)this.getActivity().getSystemService(Context.ALARM_SERVICE);
                            alarm.setRepeating(AlarmManager.RTC_WAKEUP,1000,30*1000,pintent);
                            a.alarmFlag=true;

                        }



                    }else{
                        Toast.makeText(this.getContext(),"Nothing to add Plz enter a location ", Toast.LENGTH_LONG).show();
                    }



                        /*
                        //to add a google map
                        Intent intent = new Intent(this.getActivity(), AddLocationActivity.class);
                        startActivity(intent);*/

                    //to get logitude latitude of type location

                        /*String inputLine = "";
                        String result = "";
                        location=location.replaceAll(" ", "%20");
                        String myUrl="http://maps.google.com/maps/geo?q="+location+"&output=csv";

                            URL url=new URL(myUrl);
                            URLConnection urlConnection=url.openConnection();
                            BufferedReader in = new BufferedReader(new
                                    InputStreamReader(urlConnection.getInputStream()));
                            while ((inputLine = in.readLine()) != null) {
                                result=inputLine;
                            }
                            String lat = result.substring(6, result.lastIndexOf(","));
                            String longi = result.substring(result.lastIndexOf(",") + 1);

*/



                }catch(IOException e){
                    Toast.makeText(this.getContext(),"Check your data connection"+e.getMessage(), Toast.LENGTH_LONG).show();
                }catch(Exception e){
                    Toast.makeText(this.getContext(),e.toString(), Toast.LENGTH_LONG).show();
                }



                break;

            case R.id.btnShowMap:
                Toast.makeText(this.getContext(),"cometo this", Toast.LENGTH_LONG).show();
                try {
                    Geocoder coder=new Geocoder(this.getContext());
                    String location=locationName.getText().toString();

                    Address s=coder.getFromLocationName(location,1).get(0);
                    double lati=s.getLatitude();
                    double longi=s.getLongitude();

                    Intent intent = new Intent(this.getActivity(),AddLocationActivity.class);
                    Bundle b = new Bundle();
                    b.putDouble("longitude", longi);
                    b.putDouble("latitude",lati);
                    intent.putExtras(b);
                    startActivity(intent);
                }catch(IOException e){
                    Toast.makeText(this.getContext(),"Check your data connection", Toast.LENGTH_LONG).show();
                }catch(Exception e){
                    Toast.makeText(this.getContext(),e.getMessage(), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alarm, container, false);
        upButton = (Button) rootView.findViewById(R.id.btnAlarmAdd);
        locationName = (TextView) rootView.findViewById(R.id.locationText);
        upButton.setOnClickListener(this);
        try {
            // if(dbHandler == null){

            DBHandler dbHandler = new DBHandler(this.getContext());


            //}

            ArrayList<Alarm> locationList = dbHandler.getAllAlarmLocations();

            ls =new Location[locationList.size()];


            String txt = "";
            int i=0;
            while(i<locationList.size()){
                ls[i]=new Location(locationList.get(i).getLocationNmae(),"",locationList.get(i).isMode());
                txt += locationList.get(i) + "/";
                i++;
            }


            adapter = new LocationAdapter(this.getActivity(),
                    R.layout.listview_item_row_alarm, ls);


            listView1 = (ListView) rootView.findViewById(R.id.listView2);
            listView1.setAdapter(adapter);
        }catch(Exception e){
            Toast.makeText(this.getContext(),e.toString(), Toast.LENGTH_LONG).show();
        }


        return rootView;
    }


    public void onStart() {
        super.onStart();
        Button buttonShow = (Button) getActivity().findViewById(R.id.btnShowMap);
        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LocationDetector locationDetector = new LocationDetector(getActivity().getBaseContext());
                locationDetector.saveLocation();
            }
        });


        Button btnSearchMap = (Button) getActivity().findViewById(R.id.btnSearchMap);
        btnSearchMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Test Beforen TestD ","No data");

            new TestD(getActivity().getBaseContext());

                Log.d("Test After TestD ","No data");

            }
        });


    }
}





