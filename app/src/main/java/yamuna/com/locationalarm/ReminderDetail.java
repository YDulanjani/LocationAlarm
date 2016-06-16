package yamuna.com.locationalarm;

import android.widget.ArrayAdapter;

/**
 * Created by Yamuna on 6/16/2016.
 */
public class ReminderDetail {

    public String icon;
    public String title;
    public boolean status;
    public ReminderDetail(){
        super();
    }

    public ReminderDetail(String icon, String title,boolean status) {
        super();
        this.icon = icon;
        this.title = title;
        this.status=status;
    }
}
