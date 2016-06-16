package yamuna.com.locationalarm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ReminderActivity extends AppCompatActivity {

    private ListView listView1;
    ReminderAdapter adapter;
    Location ls[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        DBHandler dbHandler = new DBHandler(this);


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


        adapter = new ReminderAdapter(this,R.layout.listview_item_row, ls);


        listView1 = (ListView)findViewById(R.id.listView1);
        listView1.setAdapter(adapter);


    }
}
