package yamuna.com.locationalarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


//class for handle DB
public class DBHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "LocationAlarmDB.db";

    //table names in the database
    public static final String TABLE_ALARM = "alarm";
    public static final String TABLE_REMINDER = "reminder";
    public static final String TABLE_USER = "user";
    public static final String TABLE_TASK = "task";

    //attributes for entities(tables)
    public static final String COLUMN_LOCATION_NAME = "location_name";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_MODE = "mode";
    public static final String COLUMN_REMINDER_ID = "reminder_id";
    public static final String COLUMN_TASK_ID = "task_id";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_USERNAME = "username";


    DBHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override

    //create tables
    public void onCreate(SQLiteDatabase db) {

        //Table for store alarm data
        db.execSQL(
                "create table " + TABLE_ALARM + " ("
                        + COLUMN_LOCATION_NAME + " text primary key,"
                        + COLUMN_LONGITUDE + " double,"
                        + COLUMN_LATITUDE + " double,"
                        + COLUMN_MODE + " boolean)"
        );

        //table for store user details
        db.execSQL(
                "create table " + TABLE_USER + " ("
                        + COLUMN_USERNAME + " text primary key,"
                        + COLUMN_PASSWORD + " text )"

        );

        //table for store remiders
        db.execSQL(
                "create table " + TABLE_REMINDER + " ("
                        + COLUMN_REMINDER_ID + " text primary key,"
                        + COLUMN_LOCATION_NAME + " text unique,"
                        + COLUMN_USERNAME + " text ,"
                        + COLUMN_LONGITUDE + " double,"
                        + COLUMN_LATITUDE + " double,"
                        + COLUMN_MODE + " boolean,"
                        + "FOREIGN KEY(" + COLUMN_USERNAME + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USERNAME + ") )"
        );

        //table for store task details
        db.execSQL(
                "create table " + TABLE_TASK + " ("
                        + COLUMN_TASK_ID + " text primary key,"
                        + COLUMN_LOCATION_NAME + " text ,"
                        + COLUMN_DESCRIPTION + " text ,"
                        + COLUMN_STATUS + " boolean ,"
                        + "FOREIGN KEY(" + COLUMN_LOCATION_NAME + ") REFERENCES " + TABLE_REMINDER + "(" + COLUMN_LOCATION_NAME + ") )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARM + "");
        onCreate(db);
    }

    //Add new alarm to the DB
   /* public boolean insertAlarm  (Alarm alarm)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_LOCATION_NAME, alarm.getLocationNmae());
        contentValues.put(COLUMN_LONGITUDE,alarm.getLongitude());
        contentValues.put(COLUMN_LATITUDE, alarm.getLatitude());
        contentValues.put(COLUMN_MODE, alarm.isMode());

        db.insert(TABLE_ALARM, null, contentValues);
        return true;
    }*/

    public boolean insertAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_LOCATION_NAME, alarm.getLocationNmae());
        contentValues.put(COLUMN_LONGITUDE, alarm.getLongitude());
        contentValues.put(COLUMN_LATITUDE, alarm.getLatitude());
        contentValues.put(COLUMN_MODE, alarm.isMode());

        db.insert(TABLE_ALARM, null, contentValues);
        return true;
    }


    //get all alarm locations in  the database
    public ArrayList<Alarm> getAllAlarmLocations() {
        ArrayList<Alarm> array_list = new ArrayList<Alarm>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_ALARM + "", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(new Alarm(
                    res.getString(res.getColumnIndex(COLUMN_LOCATION_NAME)),
                    res.getDouble(res.getColumnIndex(COLUMN_LATITUDE)),
                    res.getDouble(res.getColumnIndex(COLUMN_LONGITUDE)),
                    res.getInt(res.getColumnIndex(COLUMN_MODE)) > 0));
            res.moveToNext();
        }
        return array_list;
    }

    //get alarm data to relevant locationName
    public Alarm getAlarmData(String locationName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_ALARM + " where " + COLUMN_LOCATION_NAME + "= ? ", new String[]{locationName});
        while (res.isFirst()) {
            Alarm r= new Alarm(
                    res.getString(res.getColumnIndex(COLUMN_LOCATION_NAME)),
                    res.getDouble(res.getColumnIndex(COLUMN_LATITUDE)),
                    res.getDouble(res.getColumnIndex(COLUMN_LONGITUDE)),
                    res.getInt(res.getColumnIndex(COLUMN_STATUS)) > 0

            );
            res.moveToNext();
            return r;
        }

        return null;
    }

    //update alarm details
    public int updateAlarm(Alarm alarm){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MODE, alarm.isMode());
        // updating row
        int res= db.update(TABLE_ALARM, values, COLUMN_LOCATION_NAME + " = ?",
                new String[] { alarm.getLocationNmae() });
        db.close();
        return  res;
    }

    //remove alarm
    public int  deleteAlarm(String locationName) {

        SQLiteDatabase db = this.getWritableDatabase();

         int res = db.delete(TABLE_ALARM, COLUMN_LOCATION_NAME + "= ?  ", new String[]{locationName});
        return res;
    }


    //CURD operations for Reminder

    //insert reminder
    public boolean insertReminder  (Reminder reminder)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_LOCATION_NAME, reminder.getLocationName());
        contentValues.put(COLUMN_LONGITUDE,reminder.getLongitude());
        contentValues.put(COLUMN_LATITUDE, reminder.getLatitude());
        contentValues.put(COLUMN_MODE, reminder.isMode());

        db.insert(TABLE_REMINDER, null, contentValues);
        return true;
    }


    //get all reminders in  the database
    public ArrayList<Reminder> getAllReminders() {
        ArrayList<Reminder> array_list = new ArrayList<Reminder>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_REMINDER + "", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(new Reminder(
                    res.getString(res.getColumnIndex(COLUMN_LOCATION_NAME)),
                    res.getDouble(res.getColumnIndex(COLUMN_LONGITUDE)),
                    res.getDouble(res.getColumnIndex(COLUMN_LATITUDE)),
                    res.getInt(res.getColumnIndex(COLUMN_MODE)) > 0));
            res.moveToNext();
        }
        return array_list;
    }


    //get reminder data to relevant locationName
    public Reminder getReminderData(String locationName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_REMINDER + " where " + COLUMN_LOCATION_NAME + "= ? ", new String[]{locationName});

        res.moveToFirst();

        while (res.isFirst()) {
            Reminder r= new Reminder(
                    res.getString(res.getColumnIndex(COLUMN_LOCATION_NAME)),
                    res.getDouble(res.getColumnIndex(COLUMN_LONGITUDE)),
                    res.getDouble(res.getColumnIndex(COLUMN_LATITUDE)),
                    res.getInt(res.getColumnIndex(COLUMN_STATUS)) > 0

            );

            r.setTaskList(getTasks(r.getLocationName()));
            res.moveToNext();
            return r;
        }
        return null;

    }

    //update alarm details
    public int updateReminder(Reminder reminder){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MODE, reminder.isMode());
        // updating row
        int res= db.update(TABLE_REMINDER, values, COLUMN_LOCATION_NAME + " = ?",
                new String[] { reminder.getLocationName() });
        db.close();
        return  res;
    }


    //remove reminder
    public int  deleteReminder(String locationName) {

        SQLiteDatabase db = this.getWritableDatabase();
        int res=deleteTask(locationName);
        if(res >0){
            res = db.delete(TABLE_REMINDER, COLUMN_LOCATION_NAME + "= ?  ", new String[]{locationName});
        }

        return res;
    }



    //CURD operations for tasks

    //Insert task to DB
    public boolean insertTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_LOCATION_NAME, task.getLocationName());
        contentValues.put(COLUMN_DESCRIPTION, task.getDescription());
        contentValues.put(COLUMN_STATUS, task.isStatus());

        db.insert(TABLE_TASK, null, contentValues);
        return true;
    }


    //get All task belongs to location
    public ArrayList<Task> getTasks(String locationName) {

        ArrayList<Task> array_list = new ArrayList<Task>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_TASK + " where" + COLUMN_LOCATION_NAME + "= ? ", new String[]{locationName});
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(new Task(
                    res.getString(res.getColumnIndex(COLUMN_LOCATION_NAME)),
                    res.getInt(res.getColumnIndex(COLUMN_STATUS)) > 0,
                    res.getString(res.getColumnIndex(COLUMN_DESCRIPTION))
            ));
            res.moveToNext();
        }
        return array_list;
    }


    //remove specific task
    public int  deleteTask(String locationName,String description) {

        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(TABLE_TASK, COLUMN_LOCATION_NAME + "= ? and  " + COLUMN_DESCRIPTION + "= ? ", new String[]{locationName,description});
        return res;
    }

    //update alarm details
    public int updateTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, task.isStatus());
        // updating row
        int res= db.update(TABLE_TASK, values, COLUMN_LOCATION_NAME + " = ? and "+COLUMN_DESCRIPTION+"= ?",
                new String[] { task.getLocationName(),task.getDescription() });
        db.close();
        return  res;
    }



    //remove all task for location
    public int  deleteTask(String locationName) {

        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(TABLE_TASK, COLUMN_LOCATION_NAME + "= ?  ", new String[]{locationName});
        return res;
    }


    public boolean insertAccount(String userName,String pw) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, userName);
        contentValues.put(COLUMN_PASSWORD, pw);


        db.insert(TABLE_USER, null, contentValues);
        return true;
    }

}
