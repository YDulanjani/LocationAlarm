package yamuna.com.locationalarm;

/**
 * Created by Yamuna on 5/30/2016.
 */
public class Location {

    public String icon;
    public String title;
    public boolean status;
    public Location(){
        super();
    }

    public Location(String icon, String title,boolean status) {
        super();
        this.icon = icon;
        this.title = title;
        this.status=status;
    }
}
