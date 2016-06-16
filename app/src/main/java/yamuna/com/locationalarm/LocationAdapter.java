package yamuna.com.locationalarm;

import android.app.Activity;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Yamuna on 5/30/2016.
 */
public class LocationAdapter extends ArrayAdapter<Location> {

/*
    Context context;
    int layoutResourceId;
    Location data[] = null;

    public LocationAdapter(Context context, int layoutResourceId, Location[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LocationHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new LocationHolder();
            holder.sw = (Switch)row.findViewById(R.id.switch1);


            row.setTag(holder);
        }
        else
        {
            holder = (LocationHolder) row.getTag();
        }

        Location loc = data[position];
        holder.sw.setText(loc.title);


        return row;
    }

    static class LocationHolder
    {
        Switch sw;

    }*/

    Context context;
    int layoutResourceId;
    Location data[] = null;

    public LocationAdapter(Context context, int layoutResourceId, Location[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LocationHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new LocationHolder();
            holder.imgIcon = (Switch) row.findViewById(R.id.imgIcon2);

            holder.imgIcon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView , boolean isChecked) {
                    String locationName=buttonView.getText().toString();
                    boolean mode=isChecked;
                    Alarm alarm=new Alarm(locationName,0,0,mode);
                    new DBHandler(context).updateAlarm(alarm);

                    Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                    if (alarmUri == null) {
                        alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    }
                    if(alarmUri == null ){
                        alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    }
                    Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
                    ringtone.stop();

                    /*
                    if(isChecked){
                        Toast.makeText(context,"click on switch"+buttonView.getText(), Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(context,"click off switch", Toast.LENGTH_LONG).show();
                    }
                    */

                }
            });


            row.setTag(holder);
        }
        else
        {
            holder = (LocationHolder) row.getTag();
        }

        Location weather = data[position];

        holder.imgIcon.setText(weather.icon);
        holder.imgIcon.setChecked(weather.status);

        return row;
    }

    static class LocationHolder
    {
        Switch imgIcon;
        TextView txtTitle;
    }

}

