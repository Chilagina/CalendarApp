package com.example.calendarapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    CustomCalendarView customCalendarView;
    CalendarEvents calendarEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customCalendarView = (CustomCalendarView)findViewById(R.id.custom_calendar_view);
       // calendarEvents =(CalendarEvents)findViewById(R.id.custom_add_event);
    }
}