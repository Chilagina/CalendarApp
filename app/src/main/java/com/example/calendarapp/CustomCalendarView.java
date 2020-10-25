package com.example.calendarapp;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.Nullable;

public class CustomCalendarView extends LinearLayout {

    ImageButton Previous, Next;
    TextView CurrentDate;
    GridView gridView;
    private static final int MAX_CALENDAR_DAYS = 42;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    Context context;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);

    MyGridAdapter myGridAdapter;
    AlertDialog alertDialog;
    List<Date> dates = new ArrayList<java.util.Date>();
    List<CalendarEvents> eventsList = new ArrayList<>();

    public CustomCalendarView(Context context) {
        super(context);
    }

    public CustomCalendarView(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        InitializeLayout();
        SetUpCalendar();

        Previous.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH, -1);
                SetUpCalendar();

            }
        });

        Next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH, 1);
                SetUpCalendar();

            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                final View addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_new_event_layout, null);
                final EditText EventName = addView.findViewById(R.id.eventsId);
                final TextView EventTime = addView.findViewById(R.id.eventTime);
                ImageButton SetTime = addView.findViewById(R.id.setEventTime);
                Button AddEvent = addView.findViewById(R.id.addEventButton);
                SetTime.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutes = calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(addView.getContext(), R.style.Theme_AppCompat_Dialog, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE,minute);
                                c.setTimeZone(TimeZone.getDefault());
                                SimpleDateFormat hformate = new SimpleDateFormat("K:mm a",Locale.ENGLISH);
                                String event_Time = hformate.format(c.getTime());
                                EventTime.setText(event_Time);
                            }
                        },hours,minutes,false);
                        timePickerDialog.show();

                    }
                });
                final  String date = dateFormat.format(dates.get(getVerticalScrollbarPosition()));
                final  String month = monthFormat.format(dates.get(getVerticalScrollbarPosition()));
                final  String year = yearFormat.format(dates.get(getVerticalScrollbarPosition()));

                AddEvent.setOnClickListener(new OnClickListener(){
                    @Override
                    public void onClick(View v){
                        //  SaveEvent (EventName.getText().toString(),EventTime.getText().toString(),date,month,year);
                        SetUpCalendar();
                        alertDialog.dismiss();

                    }
                });
                builder.setView(addView);
                alertDialog = builder.create();
                alertDialog.show();

            }
        });
    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // private void SaveEvent(String event, String time, String date, String month, String year,DBOpenHelper dbOpenHelper){

    // dbOpenHelper = new DBOpenHelper(context);
    // SQLiteDatabase database = DBOpenHelper.getWritableDatabase();
    // dbOpenHelper.SaveEvent(event, time, date, month, year, database);
    // dbOpenHelper.close();
    // Toast.makeText(context, "Event Added", Toast.LENGTH_SHORT).show();
    //  }

    private void InitializeLayout() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);
        Next = view.findViewById(R.id.nextButton);
        Previous = view.findViewById(R.id.previousButton);
        CurrentDate = view.findViewById(R.id.currentDateText);
        gridView = view.findViewById(R.id.calendarGridView);
    }

    private void SetUpCalendar() {
        String currentDate = dateFormat.format(calendar.getTime());
        CurrentDate.setText(currentDate);
        dates.clear();
        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int FirstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayOfMonth);
        //CollectEventsPerMonth(monthFormat.format(calendar.getTime()),yearFormat.format(calendar.getTime()));

        while (dates.size() <MAX_CALENDAR_DAYS) {
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH,1);
        }

        myGridAdapter = new MyGridAdapter(context,dates, calendar,eventsList);
        gridView.setAdapter(myGridAdapter);
    }

   // private void CollectEventsPerMonth(String Month,String year){
      //  eventsList.clear();
       // dbOpenHelper= new DBOpenHelper(context);
       // SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
      //  Cursor cursor = dbOpenHelper.ReadEventsPerMonth(Month,year,database);
      //  while (cursor.moveToNext()){
        //    String event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT));
        //    String time = cursor.getString(cursor.getColumnIndex(DBStructure.TIME));
        //    String date = cursor.getString(cursor.getColumnIndex(DBStructure.DATE));
        //    String month = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH));
        //    String Year = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR));
        //    CalendarEvents events = new CalendarEvents(event,time,date,month,Year);
        //    eventsList.add(events);
        }

       // cursor.close();
       // dbOpenHelper.close();

   // }
//}



